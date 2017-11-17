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
    @Override
    public void add(User entity) {

    }

    @Override
    public void update(User updatedEntity) {

    }

    @Override
    public void remove(User entity) {

    }

    @Override
    public List<User> list() {
        Connection connection = new ConnectMySQL().getConnection();
        List<User> users = new ArrayList<>();
        try {
            ResultSet rs = connection.prepareStatement("SELECT * FROM user").executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getInt("UserId"), rs.getString("Username"), rs.getString("Password"), rs.getString("DisplayName"), rs.getInt("Coins")));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User find(int id) {
        Connection connection = new ConnectMySQL().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM user WHERE UserId = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new User(rs.getInt("UserId"), rs.getString("Username"), rs.getString("Password"), rs.getString("DisplayName"), rs.getInt("Coins"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User findUserByUsername(String username) {
        Connection connection = new ConnectMySQL().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE Username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                User u = new User(rs.getInt("UserId"), rs.getString("Username"), rs.getString("Password"), rs.getString("DisplayName"), rs.getInt("Coins"));
                System.out.println("YOU ARELOOKING FOR THIS LINE IN THIS TRACE" + u.getId());
                return u; //Deze was niet aanwezig...
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String helloWorld() {
        return "Hello?";
    }
}
