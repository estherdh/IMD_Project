package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.Replica;
import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.persistent.ConnectMySQL;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

public class ReplicaJDBCDaoTest
{
    private User user;

    private Connection conn;
    private ReplicaJDBCDao dao;

    @Before
    public void setUp() throws Exception {
        user = new User(1, "mail", "password", "DeNaam", 0, 1);

        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("testDatabase.properties"));
        ConnectMySQL.getInstance().setProperties(properties);
        ConnectMySQL.getInstance().getConnection().close();
        this.conn = ConnectMySQL.getInstance().getConnection();
        RunScript.execute(conn, new FileReader("src/main/resources/sqlScript.sql"));
        dao = new ReplicaJDBCDao();
    }

    @After
    public void closeConn() throws SQLException {
        Connection conn = ConnectMySQL.getInstance().getConnection();
        conn.createStatement().executeUpdate("DROP ALL OBJECTS");
        conn.close();
    }

    @Test
    public void findReplicaTest() {
        // init
        Replica expectedReplica = new Replica (2, 1, 15, "test1", 2);
        // test
        Replica replica = dao.find(2);
        // check result
        assertThat(replica, samePropertyValuesAs(expectedReplica));
    }

    @Test
    public void findAvailableReplicasTest() {
        // init
        List<Replica> expectedReplicaList = new ArrayList<Replica>(){{
            add(new Replica(1, 6, 10, "traktor", 2));
        }};
        // test
        List<Replica> replicaList = dao.findAvailableReplicas(user);
        // check result
        assertThat(replicaList.size(), is(expectedReplicaList.size()));
        for(int i = 0; i < expectedReplicaList.size(); i++) {
            assertThat(replicaList.get(i), samePropertyValuesAs(expectedReplicaList.get(i)));
        }
    }

    @Test
    public void getFreePositionsTest() {
        // init
        List<Integer> expected = new ArrayList<Integer>() {{
            add(3);
            add(2);
        }};
        // test
        List<Integer> actual = dao.getFreePositions(user, 2);
        // check result
        assertThat(actual.size(), samePropertyValuesAs(expected.size()));
        for(int i = 0; i < expected.size(); i++) {
            assertThat(actual.get(i), samePropertyValuesAs(expected.get(i)));
        }
    }

    @Test
    public void getPositionsForReplicaTypeTest() {
        // init
        List<Integer> expected = new ArrayList<Integer>() {{
            add(1);
            add(2);
            add(3);
        }};
        // test
        List<Integer> actual = dao.getPositionsForReplicaType(2);
        // check result
        assertThat(actual.size(), samePropertyValuesAs(expected.size()));
        for(int i = 0; i < expected.size(); i++) {
            assertThat(actual.get(i), samePropertyValuesAs(expected.get(i)));
        }
    }

    @Test
    public void getReplicasFromUserTest() {
        // init
        List<Replica> expected = new ArrayList<Replica>() {{
            add(new Replica(2, 1, 15, "test1", 2));
        }};
        // test
        List<Replica> actual = dao.getReplicasFromUser(user);
        // check result
        assertThat(actual.size(), samePropertyValuesAs(expected.size()));
        for(int i = 0; i < expected.size(); i++) {
            assertThat(actual.get(i), samePropertyValuesAs(expected.get(i)));
        }
    }

    @Test
    public void updateReplicaPositionTest() throws SQLException {
        // init
        Replica replica = new Replica(3, 1, 12, "test2", 2);
        // test
        dao.updateReplicaPosition(user, replica, 2);
        // check result
        Connection conn = ConnectMySQL.getInstance().getConnection();
        ResultSet rs = conn.prepareStatement("SELECT * FROM librarian.userreplica WHERE UserId = 1 AND ReplicaId = 3;").executeQuery();
        rs.next();
        assertThat(rs.getInt("ReplicaPositionId"), is(2));
    }

    @Test
    public void giveReplicaToUserTest() throws SQLException {
        // init
        Replica replica = new Replica(1, 1, 12, "image.png", 1);
        // test
        dao.giveReplicaToUser(user, replica);
        // check result
        Connection conn = ConnectMySQL.getInstance().getConnection();
        ResultSet rs = conn.prepareStatement("SELECT 1 FROM librarian.userreplica WHERE UserId = 1 AND ReplicaId = 1;").executeQuery();
        assertTrue(rs.first());
    }
}
