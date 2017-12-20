package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.Notification;
import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.persistent.ConnectMySQL;
import oose.p.c6.imd.persistent.NotificationCreator;

import javax.enterprise.inject.Default;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                PreparedStatement ps2 = connection.prepareStatement("SELECT np.key, np.value FROM usernotification un\n" +
                        "INNER JOIN notificationproperties np ON np.UserNotificationId = un.UserNotificationId\n" +
                        "WHERE un.userNotificationId = ?");
                ps2.setInt(1, rs.getInt("NotificationId"));
                ResultSet rs2 = ps2.executeQuery();
                Map<String, String> properties = new HashMap<String, String>();
                while(rs2.next()){
                    properties.put(rs2.getString(1), rs2.getString(2));
                }
                notifications.add(NotificationCreator.createNotification(u, rs.getString("notificationText"), properties, rs.getInt("NotificationTypeId"), rs.getString("date"), rs.getBoolean("read"), rs.getInt("id")));
            }
            return notifications;
        } catch (Exception e) {
            return (List<Notification>) handleException(e, new ArrayList<Notification>());
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
