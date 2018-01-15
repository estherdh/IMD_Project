package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.Notification;
import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.persistent.ConnectMySQL;
import oose.p.c6.imd.persistent.NotificationCreator;

import javax.enterprise.inject.Default;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Default
public class UserJDBCDao implements IUserDao {
    private static final Logger LOGGER = Logger.getLogger(UserJDBCDao.class.getName());

    public void add(User entity) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        try{
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Users (`Email`, `Password`, `DisplayName`, `Coins`, `LanguageId`) VALUES (?, ?, ?, ?, ?)");
            ps = fillVariables(ps, entity.getEmail(), entity.getPassword(), entity.getDisplayName(), entity.getCoins(), entity.getLanguageId());
            ps.execute();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    private PreparedStatement fillVariables(PreparedStatement ps, String email, String password, String displayname, int coins, int languageid) throws SQLException {
        ps.setString(1, email);
        ps.setString(2, password);
        ps.setString(3, displayname);
        ps.setInt(4, coins);
        ps.setInt(5, languageid);
        return ps;
    }

    public void update(User entity) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        try{
            PreparedStatement ps = connection.prepareStatement("UPDATE Users SET `Email` = ?," +
                    " `Password` = ?," +
                    " `DisplayName` = ?," +
                    " `Coins` = ?," +
                    " `LanguageId` = ? " +
                    "WHERE UserId = ?");
            ps = fillVariables(ps, entity.getEmail(), entity.getPassword(), entity.getDisplayName(), entity.getCoins(), entity.getLanguageId());
            ps.setInt(6, entity.getId());
            ps.execute();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void remove(User entity) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        try{
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Users WHERE UserId = ?");
            ps.setInt(1, entity.getId());
            ps.execute();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    public List<User> list() {
        try {
            Connection connection = ConnectMySQL.getInstance().getConnection();
            List<User> users = new ArrayList<User>();
            ResultSet rs = connection.prepareStatement("SELECT * FROM Users").executeQuery();
            while (rs.next()) {
                users.add(generateNewUser(rs));
            }
            return users;
        } catch (Exception e) {
            return (List<User>) handleException(e, new ArrayList<User>());
        }
    }

    public User find(int id) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        ResultSet rs = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Users WHERE UserId = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            User returnUser = null;
            if(rs.next()){
                returnUser = generateNewUser(rs);
            }
            return returnUser;
        } catch (SQLException e) {
            return (User) handleException(e, null);
        }
    }

    public User findUserByEmail(String email) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        ResultSet rs = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Users WHERE Email = ?");
            ps.setString(1, email);
            rs = ps.executeQuery();
            User returnUser = null;
            if(rs.next()){
                returnUser = generateNewUser(rs);
            }
            connection.close();
            return returnUser;
        } catch (SQLException e) {
            return (User) handleException(e, null);
        }
    }

    @Override
    public List<Notification> listNotification(User u) {
        try {
            Connection connection = ConnectMySQL.getInstance().getConnection();
            List<Notification> notifications = new ArrayList<Notification>();
            PreparedStatement ps = connection.prepareStatement("SELECT un.UserNotificationId as `NotificationId`, n.notificationText as `notificationText`, n.notificationId as `id`, un.Read AS `read`, un.Date AS `date`, n.NotificationId as `NotificationTypeId` FROM usernotification un\n" +
                    "INNER JOIN users u ON u.UserId = un.UserId\n" +
                    "INNER JOIN notification n ON n.NotificationId = un.NotificationId\n" +
                    "WHERE un.UserId = ? \n" +
                    "AND n.languageId IN (SELECT COALESCE((SELECT languageId FROM Notification n WHERE n.NotificationId = un.NotificationId AND languageId = ?),  1))\n");
            ps.setInt(1, u.getId());
            ps.setInt(2, u.getLanguageId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                notifications.add(createNotification(u, rs, connection));
            }
            return notifications;
        } catch (Exception e) {
            return (List<Notification>) handleException(e, new ArrayList<Notification>());
        }
    }

    @Override
    public void addNotification(int typeId, Map<String, String> properties, User user) {
        int notificationId = addNotificationToUsernotificationAndGetId(typeId, user.getId());
        if (notificationId > 0) {
            properties.forEach((k,v)-> addPropertiesToNotification(notificationId, k, v));
        }

    }

    private void addPropertiesToNotification(int notificationId, String key, String value) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO notificationproperties (UserNotificationId, `Key`, `Value`) VALUES (?,?,?)");
            ps.setInt(1, notificationId);
            ps.setString(2, key);
            ps.setString(3, value);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    private int addNotificationToUsernotificationAndGetId(int typeId,  int userId) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO usernotification(NotificationId, UserId) VALUES (? , ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, typeId);
            ps.setInt(2, userId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            return 0;
        }
    }

    @Override
    public User getUserByQuestId(int id) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM questlog q JOIN users u ON q.UserId = u.UserId WHERE q.EntryId = ?;");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return generateNewUser(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    @Override
    public Notification findNotification(User user, int notificationId) {
        try {
            Connection connection = ConnectMySQL.getInstance().getConnection();
            Notification notification = null;
            PreparedStatement ps = connection.prepareStatement("SELECT un.UserNotificationId as `NotificationId`, n.notificationText as `notificationText`, n.notificationId as `id`, un.Read AS `read`, un.Date AS `date`, n.NotificationId as `NotificationTypeId` FROM usernotification un\n" +
                    "INNER JOIN users u ON u.UserId = un.UserId\n" +
                    "INNER JOIN notification n ON n.NotificationId = un.NotificationId\n" +
                    "WHERE un.UserId = ? \n" +
                    "AND un.UserNotificationId = ? \n" +
                    "AND n.languageId IN (SELECT COALESCE((SELECT languageId FROM Notification n WHERE n.NotificationId = un.NotificationId AND languageId = ?),  1))\n");
            ps.setInt(1, user.getId());
            ps.setInt(2, notificationId);
            ps.setInt(3, user.getLanguageId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                notification = createNotification(user, rs, connection);
            }
            return notification;
        } catch (Exception e) {
            return (Notification) handleException(e, null);
        }
    }

    private Notification createNotification(User u, ResultSet rs, Connection conn) throws SQLException {
        PreparedStatement ps2 = conn.prepareStatement("SELECT np.key, np.value FROM usernotification un\n" +
                "INNER JOIN notificationproperties np ON np.UserNotificationId = un.UserNotificationId\n" +
                "WHERE un.userNotificationId = ?");
        ps2.setInt(1, rs.getInt("NotificationId"));
        ResultSet rs2 = ps2.executeQuery();
        Map<String, String> properties = new HashMap<String, String>();
        while(rs2.next()){
            properties.put(rs2.getString(1), rs2.getString(2));
        }
        return NotificationCreator.createNotification(u, rs.getString("notificationText"), properties, rs.getInt("NotificationTypeId"), rs.getString("date"), rs.getBoolean("read"), rs.getInt("id"));
    }

    @Override
    public void updateNotification(Notification n) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE usernotification " +
                    "SET `read` = ? " +
                    "WHERE `userNotificationId` = ? ");
            ps.setBoolean(1, n.getRead());
            ps.setInt(2, n.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    private User generateNewUser(ResultSet rs) {
        try {
            return new User(rs.getInt("UserId"), rs.getString("email"), rs.getString("Password"), rs.getString("DisplayName"), rs.getInt("Coins"), rs.getInt("LanguageId"));
        } catch (SQLException e) {
            return (User) handleException(e, null);
        }
    }

    protected Object handleException(Exception e, Object o){
        LOGGER.log(Level.SEVERE, e.toString(), e);
        return o;
    }
}