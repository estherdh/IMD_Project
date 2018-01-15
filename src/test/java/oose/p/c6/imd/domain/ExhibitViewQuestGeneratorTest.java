package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.ConnectMySQL;
import oose.p.c6.imd.persistent.dao.DAOFactory;
import oose.p.c6.imd.persistent.dao.IExhibitDao;
import oose.p.c6.imd.persistent.dao.IUserDao;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExhibitViewQuestGeneratorTest {

    private int userId;
    private List<Exhibit> expectedExhibits;

    private Connection conn;

    @Mock
    private IExhibitDao exhibitDao;

    @Mock
    private IUserDao userDao;

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
        DAOFactory.setUserDao(userDao);
    }

    @After
    public void closeConn() throws SQLException {
        Connection conn = ConnectMySQL.getInstance().getConnection();
        conn.createStatement().executeUpdate("DROP ALL OBJECTS");
        conn.close();
    }


    @Test
    public void questIsGeneratedTest() throws SQLException {
        //init
        Era era = new Era(1, "tijdperk test");
        User mockUser = mock(User.class);
        when(userDao.find(userId)).thenReturn(mockUser);
        when(exhibitDao.findEra(mockUser, era.getId())).thenReturn(era);

        //test
        exhibitQuest.generateQuest(userId);

        //check
        ResultSet rs = conn.prepareStatement("SELECT questproperties.Value FROM questproperties WHERE EntryId = 10 ORDER BY PropertyId DESC").executeQuery();
        rs.next();
        int i = Integer.parseInt((rs.getString(1)));

        assertEquals(expectedExhibits.get(0).getId(), i);
    }
}
