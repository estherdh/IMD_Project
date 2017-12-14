package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.Era;
import oose.p.c6.imd.domain.Exhibit;
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
            add(new Replica(1, 1, 10, "traktor", 2));
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
    public void getReplicasFromUserTest() {
        Era era = new Era(1, "tijdperk test");
        Exhibit exhibit = new Exhibit(1, "Het test object",
                "Dit object wordt altijd al gebruikt om te testen", null, "object.png",
                1999, era, 1);
        Replica replica = new Replica(2, exhibit, 15, "test1", 2, 1);
        // init
        List<Replica> expected = new ArrayList<Replica>() {{
            add(replica);
        }};
        // test
        List<Replica> actual = dao.getReplicasFromUser(user);
        // check result
        assertThat(actual.size(), samePropertyValuesAs(expected.size()));
        for(int i = 0; i < expected.size(); i++) {
            //check replica
            assertThat(actual.get(i).getId(), is(expected.get(i).getId()));
            assertThat(actual.get(i).getPrice(), is(expected.get(i).getPrice()));
            assertThat(actual.get(i).getType(), is(expected.get(i).getType()));
            assertThat(actual.get(i).getPosition(), is(expected.get(i).getPosition()));
            assertThat(actual.get(i).getSprite(), is(expected.get(i).getSprite()));
            //check exhibit
            assertThat(actual.get(i).getExhibit().getDescription(), is(expected.get(i).getExhibit().getDescription()));
            assertThat(actual.get(i).getExhibit().getImage(), is(expected.get(i).getExhibit().getImage()));
            assertThat(actual.get(i).getExhibit().getName(), is(expected.get(i).getExhibit().getName()));
            assertThat(actual.get(i).getExhibit().getVideo(), is(expected.get(i).getExhibit().getVideo()));
            assertThat(actual.get(i).getExhibit().getYear(), is(expected.get(i).getExhibit().getYear()));
            //check era
            assertThat(actual.get(i).getExhibit().getEra().getName(), is(expected.get(i).getExhibit().getEra().getName()));
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
