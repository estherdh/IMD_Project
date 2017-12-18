package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.ConnectMySQL;
import oose.p.c6.imd.persistent.dao.DAOFactory;
import oose.p.c6.imd.persistent.dao.ExhibitJDBCDao;
import oose.p.c6.imd.persistent.dao.IExhibitDao;
import oose.p.c6.imd.persistent.dao.IQuestDAO;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExhibitViewQuestGeneratorTest {

    private int userId;
    List<Exhibit> expectedExhibits;

    private Connection conn;
    private ExhibitJDBCDao dao;
    private IQuestDAO qd = mock(IQuestDAO.class);

    @Mock
    private IExhibitDao exhibitDao;

    @InjectMocks
    private ExhibitViewQuestGenerator exhibitQuest;

    @Before
    public void setUp() throws Exception {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("testDatabase.properties"));
        ConnectMySQL.getInstance().setProperties(properties);
        ConnectMySQL.getInstance().getConnection().close();
        this.conn = ConnectMySQL.getInstance().getConnection();
        RunScript.execute(conn, new FileReader("src/main/resources/sqlScript.sql"));
        dao = new ExhibitJDBCDao();
        userId = 1;

        expectedExhibits = (new ArrayList<Exhibit>() {{
//            add(new Exhibit(1, "Het test object", "Dit object wordt altijd al gebruikt om te testen", null, "object.png", 1999, 1, 1));
            add(new Exhibit(4, "Voorbeeld streektaal", "Dit papier bevat een stuk tekst in streektaal: Oet de goaldn korenaarn skeup God de Tweantenaarn, en oet t kaf en d restn de leu oet t Westn",
                    null, "object.png", 2017, 1, 2));
        }});

        when(exhibitDao.findExhibitsNotYetInQuestlog(userId)).thenReturn(expectedExhibits);
        DAOFactory.setExhibitDao(exhibitDao);
    }

    @After
    public void closeConn() throws SQLException {
        Connection conn = ConnectMySQL.getInstance().getConnection();
        conn.createStatement().executeUpdate("DROP ALL OBJECTS");
        conn.close();
    }


    @Test
    public void questIsGeneratedTest() throws SQLException {

        //test

        exhibitQuest.generateQuest(userId);
        List<Exhibit> actualExhibitsNotAdded = exhibitQuest.findExhibitsNotYetInQuestlog(userId);


        //check
        ResultSet rs = conn.prepareStatement("SELECT * FROM questproperties WHERE EntryId = 8").executeQuery();
        rs.next();
        int i = Integer.parseInt((rs.getString(3)));
        assertEquals(i, expectedExhibits.get(0).getId());
    }

    @Test
    public void propertyValuesAreCorrectTest() {
    }
}
