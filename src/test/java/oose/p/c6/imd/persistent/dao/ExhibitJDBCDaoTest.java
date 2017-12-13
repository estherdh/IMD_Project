package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.Era;
import oose.p.c6.imd.domain.Exhibit;
import oose.p.c6.imd.domain.Museum;
import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.persistent.ConnectMySQL;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

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
        Exhibit expectedExhibit = new Exhibit(3, "Trekker", "Deze trekker is geen tractor!", null, "object.png", 2015, 1, 2);

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

    @Test
    public void findEra(){
        //init
        Era expected = new Era(1, "test era");

        //test
        Era actualResult = dao.findEra(new User(1,"1","1","1",1,2),1);

        //check
        assertThat(actualResult.getId(), is(expected.getId()));
        assertThat(actualResult.getName(), is(expected.getName()));
    }

    @Test
    public void findMuseum(){
        //init
        Museum expected = new Museum(1, "test musei", "http://google.nl", "Nederland");

        //test
        Museum actualResult = dao.findMuseum(1);

        //check
        assertThat(actualResult.getId(), is(expected.getId()));
        assertThat(actualResult.getName(), is(expected.getName()));
        assertThat(actualResult.getSite(), is(expected.getSite()));
        assertThat(actualResult.getRegion(), is(expected.getRegion()));
    }

    @Test
    public void listMuseum(){
        Museum expected = new Museum(1, "test musei", "http://google.nl", "Nederland");
        Museum expected2 = new Museum(2, "De verzamel schuur", "http://google.twente", "Twente");

        List<Museum> list = dao.listMuseums();

        assertThat(list.get(0).getId(), is(expected.getId()));
        assertThat(list.get(0).getName(), is(expected.getName()));
        assertThat(list.get(0).getSite(), is(expected.getSite()));
        assertThat(list.get(0).getRegion(), is(expected.getRegion()));
        assertThat(list.get(1).getId(), is(expected2.getId()));
        assertThat(list.get(1).getName(), is(expected2.getName()));
        assertThat(list.get(1).getSite(), is(expected2.getSite()));
        assertThat(list.get(1).getRegion(), is(expected2.getRegion()));

    }

    @Test
    public void listEra(){
        //init
        Era expected = new Era(1, "test era");

        //test
        List<Era> actualResult = dao.listEra(new User(1,"1","1","1",1,2));

        //check
        assertThat(actualResult.get(0).getId(), is(expected.getId()));
        assertThat(actualResult.get(0).getName(), is(expected.getName()));
    }

    @Test
    public void listExhibits(){
        Exhibit expectedExhibit = new Exhibit(3, "Trekker", "Deze trekker is geen tractor!", null, "object.png", 2015, 1, 2);

        List<Exhibit> result = dao.list(new User(1,"1","1","1",1,3));

        assertThat(result.get(2).getId(), is(expectedExhibit.getId()));
        assertThat(result.get(2).getYear(), is(expectedExhibit.getYear()));
        assertThat(result.get(2).getImage(), is(expectedExhibit.getImage()));
        assertThat(result.get(2).getVideo(), is(expectedExhibit.getVideo()));
        assertThat(result.get(2).getDescription(), is(expectedExhibit.getDescription()));
        assertThat(result.get(2).getName(), is(expectedExhibit.getName()));
        assertThat(result.get(2).getEraId(), is(expectedExhibit.getEraId()));
        assertThat(result.get(2).getMuseumId(), is(expectedExhibit.getMuseumId()));
    }

    @Test
    public void listExhibitsByEra(){
        Exhibit expectedExhibit = new Exhibit(3, "Trekker", "Deze trekker is geen tractor!", null, "object.png", 2015, 1, 2);

        List<Exhibit> result = dao.listByEra(new User(1,"1","1","1",1,3), 1);

        assertThat(result.get(2).getId(), is(expectedExhibit.getId()));
        assertThat(result.get(2).getYear(), is(expectedExhibit.getYear()));
        assertThat(result.get(2).getImage(), is(expectedExhibit.getImage()));
        assertThat(result.get(2).getVideo(), is(expectedExhibit.getVideo()));
        assertThat(result.get(2).getDescription(), is(expectedExhibit.getDescription()));
        assertThat(result.get(2).getName(), is(expectedExhibit.getName()));
        assertThat(result.get(2).getEraId(), is(expectedExhibit.getEraId()));
        assertThat(result.get(2).getMuseumId(), is(expectedExhibit.getMuseumId()));
    }

    @Test
    public void listExhibitsByMuseum(){
        Exhibit expectedExhibit = new Exhibit(3, "Trekker", "Deze trekker is geen tractor!", null, "object.png", 2015, 1, 2);

        List<Exhibit> result = dao.listByMuseum(new User(1,"1","1","1",1,3), 2);

        assertThat(result.get(0).getId(), is(expectedExhibit.getId()));
        assertThat(result.get(0).getYear(), is(expectedExhibit.getYear()));
        assertThat(result.get(0).getImage(), is(expectedExhibit.getImage()));
        assertThat(result.get(0).getVideo(), is(expectedExhibit.getVideo()));
        assertThat(result.get(0).getDescription(), is(expectedExhibit.getDescription()));
        assertThat(result.get(0).getName(), is(expectedExhibit.getName()));
        assertThat(result.get(0).getEraId(), is(expectedExhibit.getEraId()));
        assertThat(result.get(0).getMuseumId(), is(expectedExhibit.getMuseumId()));
    }

    @Test
    public void findExhibitsNotYetInQuestlog() {
        //init
        Exhibit expectedExhibit = new Exhibit(1, "Het test object", "Dit object wordt altijd al gebruikt om te testen", null, "object.png", 1999, 1, 1);

        //test
        List<Exhibit> exhibits = dao.findExhibitsNotYetInQuestlog(1);
        Exhibit actualExhibit = exhibits.get(0);

        //check
        assertThat(actualExhibit, samePropertyValuesAs(expectedExhibit));
    }

    @Test
    public void findErasNotYetInQuestlogWithRemovedEras() {
        //init
        Era expectedEra = new Era(2, "tijdperk test2");

        //test
        List<Era> eras = dao.findErasNotYetInQuestlog(1, true);
        Era actualEra = eras.get(1);

        //check
        assertThat(actualEra, samePropertyValuesAs(expectedEra));
    }

    @Test
    public void findErasNotYetInQuestlogWithoutRemovedEras() {
        //init
        Era expectedEra = new Era(1, "tijdperk test");

        //test
        List<Era> eras = dao.findErasNotYetInQuestlog(1, false);
        Era actualEra = eras.get(0);

        //check
        assertThat(actualEra, samePropertyValuesAs(expectedEra));
    }
}
