package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.ConnectMySQL;
import oose.p.c6.imd.persistent.dao.DAOFactory;
import oose.p.c6.imd.persistent.dao.IExhibitDao;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExhibitViewQuestGeneratorTest {

    private int userId;
    private List<Exhibit> expectedExhibits;

    private Connection conn;

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
        userId = 1;

        expectedExhibits = (new ArrayList<Exhibit>() {{
            add(new Exhibit(4, "Voorbeeld streektaal", "Dit papier bevat een stuk tekst in streektaal: Oet de goaldn korenaarn skeup God de Tweantenaarn, en oet t kaf en d restn de leu oet t Westn",
                    null, null, "2017 n.C.", 1, 2));
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

        //check
        ResultSet rs = conn.prepareStatement("SELECT * FROM questproperties WHERE EntryId = 10").executeQuery();
        rs.next();
        int i = Integer.parseInt((rs.getString(3)));

        assertEquals(i, expectedExhibits.get(0).getId());
    }
}
