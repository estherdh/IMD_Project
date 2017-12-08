package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.persistent.ConnectMySQL;

import javax.enterprise.inject.Default;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
        Connection connection = ConnectMySQL.getInstance().getConnection();
        List<User> users = new ArrayList<User>();
        try {
            ResultSet rs = connection.prepareStatement("SELECT * FROM Users").executeQuery();
            while (rs.next()) {
                users.add(generateNewUser(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return users;
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
            LOGGER.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    public User findUserByemail(String email) {
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
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return null;
    }

    private User generateNewUser(ResultSet rs) {
        try {
            return new User(rs.getInt("UserId"), rs.getString("email"), rs.getString("Password"), rs.getString("DisplayName"), rs.getInt("Coins"), rs.getInt("LanguageId"));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }
}
