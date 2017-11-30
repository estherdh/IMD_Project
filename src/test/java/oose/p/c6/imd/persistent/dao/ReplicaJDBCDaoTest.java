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
        Replica expectedReplica = new Replica(2, 1, 12, "image.png", 1);
        Replica replica = dao.find(2);

        assertThat(replica, samePropertyValuesAs(expectedReplica));
    }

    @Test
    public void findAvailableReplicasTest() {
        List<Replica> replicaList = dao.findAvailableReplicas(user);
        List<Replica> expectedReplicaList = new ArrayList<Replica>(){{
            add(new Replica(1, 1, 5, "image.png", 1));
            add(new Replica(2, 1, 12, "image.png", 1));
        }};

        assertThat(replicaList.get(0), samePropertyValuesAs(expectedReplicaList.get(0)));
        assertThat(replicaList.get(1), samePropertyValuesAs(expectedReplicaList.get(1)));
    }

    @Test
    public void giveReplicaToUserTest() throws SQLException {
        Replica replica = new Replica(1, 1, 12, "image.png", 1);
        dao.giveReplicaToUser(user, replica);

        Connection conn = ConnectMySQL.getInstance().getConnection();
        ResultSet rs = conn.prepareStatement("SELECT 1 FROM librarian.userreplica WHERE UserId = 1 AND ReplicaId = 1;").executeQuery();

        assertTrue(rs.first());
    }
}
