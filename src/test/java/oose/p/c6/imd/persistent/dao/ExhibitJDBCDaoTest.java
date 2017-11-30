package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.Exhibit;
import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.persistent.ConnectMySQL;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExhibitJDBCDaoTest {
    private Connection conn;
    private ExhibitJDBCDao dao;

    @Before
    public void setUp() throws Exception {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("testDatabase.properties"));
        ConnectMySQL.getInstance().setProperties(properties);
        ConnectMySQL.getInstance().getConnection().close();
        this.conn = ConnectMySQL.getInstance().getConnection();
        RunScript.execute(conn, new FileReader("src/main/resources/sqlScript.sql"));
        dao = new ExhibitJDBCDao();
    }

    @After
    public void closeConn() throws SQLException {
        Connection conn = ConnectMySQL.getInstance().getConnection();
        conn.createStatement().executeUpdate("DROP ALL OBJECTS");
        conn.close();
    }

    @Test
    public void findExhibitWithTranslation() throws Exception {
        //init
        Exhibit expectedExhibit = new Exhibit(3, "Trekker", "Deze trekker is geen tractor!", null, "object.png", 2015);

        //test
        Exhibit actualResult = dao.find(new User(1,"1","1","1",1,3),3);

        //check
        assertThat(actualResult.getId(), is(expectedExhibit.getId()));
        assertThat(actualResult.getName(), is(expectedExhibit.getName()));
        assertThat(actualResult.getDescription(), is(expectedExhibit.getDescription()));
        assertThat(actualResult.getVideo(), is(expectedExhibit.getVideo()));
        assertThat(actualResult.getImage(), is(expectedExhibit.getImage()));
        assertThat(actualResult.getYear(), is(expectedExhibit.getYear()));

    }

}
