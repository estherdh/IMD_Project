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

@Default
public class UserJDBCDao implements IUserDao {
    public void add(User entity) {

    }

    public void update(User updatedEntity) {

    }

    public void remove(User entity) {

    }

    public List<User> list() {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        List<User> users = new ArrayList<User>();
        try {
            ResultSet rs = connection.prepareStatement("SELECT * FROM user").executeQuery();
            while (rs.next()) {
//                users.add(new User(rs.getInt("UserId"), rs.getString("email"), rs.getString("Password"), rs.getString("DisplayName"), rs.getInt("Coins")));
            }
            connection.close();
        } catch (SQLException e) {
        }
        return users;
    }

    public User find(int id) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        ResultSet rs = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM user WHERE UserId = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if(rs.next()){
//                return new User(rs.getInt("UserId"), rs.getString("email"), rs.getString("Password"), rs.getString("DisplayName"), rs.getInt("Coins"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findUserByemail(String email) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        ResultSet rs = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
            ps.setString(1, email);
            rs = ps.executeQuery();
            if(rs.next()){
//                User u = new User(rs.getInt("UserId"), rs.getString("email"), rs.getString("Password"), rs.getString("DisplayName"), rs.getInt("Coins"));
//                System.out.println("YOU ARELOOKING FOR THIS LINE IN THIS TRACE" + u.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String helloWorld() {
        return "Hello?";
    }
}
