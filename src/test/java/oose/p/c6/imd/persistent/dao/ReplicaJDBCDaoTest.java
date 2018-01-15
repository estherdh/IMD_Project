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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class ReplicaJDBCDaoTest
{
    private User user;

    private Connection conn;
    private IExhibitDao exhibitDao;
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
        exhibitDao = mock(IExhibitDao.class);
        DAOFactory.setExhibitDao(exhibitDao);
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
    public void findAvailableReplicasTest() throws SQLException {
        Era era = new Era(1, "tijdperk test");
        Exhibit exhibit = new Exhibit(1, "Het test object",
                "Dit object wordt altijd al gebruikt om te testen", null, new ArrayList<String>() {{
                    add("imagetest1");
                    add("imagetest2");
        }},
                "1999 n.C.", 1, 1);
        exhibit.setEra(era);
        Replica replica = new Replica(1, 1, 10, "traktor", 2, 0, exhibit);
        // init
        List<Replica> expected = new ArrayList<Replica>() {{
            add(replica);
        }};
        Mockito.when(exhibitDao.createExhibitFromResultset(any(ResultSet.class))).thenReturn(exhibit);
        // test
        List<Replica> actual = dao.findAvailableReplicas(user);
        // check result
        assertThat(actual.size(), samePropertyValuesAs(expected.size()));
        for(int i = 0; i < expected.size(); i++) {
            //check replica
            assertThat(actual.get(i).getId(), is(expected.get(i).getId()));
            assertThat(actual.get(i).getPrice(), is(expected.get(i).getPrice()));
            assertThat(actual.get(i).getType(), is(expected.get(i).getType()));
            assertThat(actual.get(i).getPosition(), is(0));
            assertThat(actual.get(i).getSprite(), is(expected.get(i).getSprite()));
        }
        Mockito.verify(exhibitDao, times(1)).createExhibitFromResultset(any(ResultSet.class));
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
    public void getReplicasFromUserTest() throws SQLException {
        Era era = new Era(1, "tijdperk test");
        Exhibit exhibit = new Exhibit(1, "Het test object",
                "Dit object wordt altijd al gebruikt om te testen", null, new ArrayList<String>() {{
            add("imagetest1");
            add("imagetest2");
        }},
                "1999 n.C.", 1, 1);
        exhibit.setEra(era);
        Replica replica = new Replica(2, 1, 15, "test1", 2, 1, exhibit);
        // init
        List<Replica> expected = new ArrayList<Replica>() {{
            add(replica);
        }};
        Mockito.when(exhibitDao.createExhibitFromResultset(any(ResultSet.class))).thenReturn(exhibit);
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
        }
        Mockito.verify(exhibitDao, times(2)).createExhibitFromResultset(any(ResultSet.class));
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
