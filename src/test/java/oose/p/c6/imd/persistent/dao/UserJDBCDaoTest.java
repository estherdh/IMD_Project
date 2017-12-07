package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.persistent.ConnectMySQL;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserJDBCDaoTest {
    private Connection conn;
    private UserJDBCDao dao;

    @Before
    public void setUp() throws Exception {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("testDatabase.properties"));
        ConnectMySQL.getInstance().setProperties(properties);
        ConnectMySQL.getInstance().getConnection().close();
        this.conn = ConnectMySQL.getInstance().getConnection();
        RunScript.execute(conn, new FileReader("src/main/resources/sqlScript.sql"));
        dao = new UserJDBCDao();
    }

    @After
    public void closeConn() throws SQLException {
        Connection conn = ConnectMySQL.getInstance().getConnection();
        conn.createStatement().executeUpdate("DROP ALL OBJECTS");
        conn.close();
    }

    @Test
    public void matchUsersTest(){
        User test = new User(1, "mail", "password", "DeNaam", 0, 1);
        User test2 = new User(1, "mail", "password", "DeNaam", 0, 1);
        matchUsers(test,test2);
    }

    @Test
    public void findUser() throws SQLException {
        User expected = new User(1, "mail", "password", "DeNaam", 0, 1);
        User actual = dao.find(1);
        matchUsers(actual, expected);
    }

    private void matchUsers(User actual, User expected){
        assertEquals(expected.getCoins(), actual.getCoins());
        assertEquals(expected.getDisplayName(), actual.getDisplayName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getLanguageId(), actual.getLanguageId());
        assertEquals(expected.getPassword(), actual.getPassword());
    }

    @Test
    public void listUsers() throws SQLException {
        User test = new User(1, "mail", "password", "DeNaam", 0, 1);
        User test2 = new User(2, "test@void", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", "muspi merol", 0, 2);
        ArrayList<User> expected = new ArrayList<>();
        expected.add(test);
        expected.add(test2);
        List<User> actual = dao.list();
        assertEquals(3, actual.size());
        matchUsers(actual.get(0), expected.get(0));
        matchUsers(actual.get(1), expected.get(1));
    }

    @Test
    public void addUser() throws SQLException {
        User u = new User(4, "test@user@db", "tested", "testUser", 10, 2);
        dao.add(u);
        System.out.println("user add executed");
        User actual = dao.find(4);
        matchUsers(actual, u);
    }

    @Test
    public void update() throws SQLException {
        User u = new User(3, "test@user@db", "tested", "testUser", 10, 2);
        dao.update(u);
        User actual = dao.find(3);
        matchUsers(actual, u);
    }

    @Test
    public void removeUser() throws SQLException {
        User u = dao.find(3);
        dao.remove(u);
        User actual = dao.find(3);
        assertNull(actual);
    }
}
