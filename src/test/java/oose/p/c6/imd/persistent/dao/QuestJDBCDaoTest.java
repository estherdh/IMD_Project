package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.*;
import oose.p.c6.imd.persistent.ConnectMySQL;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuestJDBCDaoTest {
    private Connection conn;
    private QuestJDBCDao dao;

    @Before
    public void setUp() throws Exception {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("testDatabase.properties"));
        ConnectMySQL.getInstance().setProperties(properties);
        ConnectMySQL.getInstance().getConnection().close();
        this.conn = ConnectMySQL.getInstance().getConnection();
        RunScript.execute(conn, new FileReader("src/main/resources/sqlScript.sql"));
        dao = new QuestJDBCDao();
    }

    @After
    public void closeConn() throws SQLException {
        Connection conn = ConnectMySQL.getInstance().getConnection();
        conn.createStatement().executeUpdate("DROP ALL OBJECTS");
        conn.close();
    }

    @Test
    public void getQuestsFromUserTestSuccess() throws Exception {
        //init
        QuestFactory factory = mock(QuestFactory.class);
        QuestFactory.setFactory(factory);
        when(factory.generateQuest(any(QuestTypes.class), any(Map.class))).thenReturn(new QrScanQuest(new HashMap<>()));
        Quest expectedQuest1 = new Quest(1, "(EN)Scan qr code", "(EN)Scan a qr code", 10, new QrScanQuest(new HashMap<>()));
        Quest expectedQuest2 = new Quest(2, "(NL)Stuur tekst", "(NL)Stuur een bepaald stuk tekst op", 15, new QrScanQuest(new HashMap<>()));
        //test
        List<Quest> actualResult = dao.getQuestsForUser(2, 2);
        //check
        assertThat(actualResult.get(0).getName(), is(expectedQuest1.getName()));
        assertThat(actualResult.get(0).getDescription(), is(expectedQuest1.getDescription()));
        assertThat(actualResult.get(0).getReward(), is(expectedQuest1.getReward()));
        assertTrue(actualResult.get(0).getQuestType() instanceof IQuestType);
        assertThat(actualResult.get(1).getName(), is(expectedQuest2.getName()));
        assertThat(actualResult.get(1).getDescription(), is(expectedQuest2.getDescription()));
        assertThat(actualResult.get(1).getReward(), is(expectedQuest2.getReward()));
        assertTrue(actualResult.get(1).getQuestType() instanceof IQuestType);
    }

    @Test
    public void getQuestsFromUserTestUserDoesntExist() throws Exception {
        //test
        List<Quest> actualResult = dao.getQuestsForUser(1200, 2);
        //check
        assertThat(actualResult.size(), is(0));
    }

    @Test
    public void getQuestsFromUserTestErrorShouldReturnEmptyArray() throws Exception {
        //init;
        conn.createStatement().executeUpdate("DROP ALL OBJECTS"); //When there are not tables the query, the query will always throw an error.
        //test
        List<Quest> actualResult = dao.getQuestsForUser(1, 1);
        assertThat(actualResult.size(), is(0));
    }

    @Test
    public void setQuestCompleteTestSuccess() throws Exception {
        //test
        dao.setQuestComplete(1);
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM questlog WHERE completed = 1");
        //check
        assertTrue(rs.next());
        assertFalse(rs.next());
    }

    @Test
    public void removeQuestFromQuestLogSuccess() throws SQLException {
        //test
        boolean actualResult = dao.removeQuestFromQuestLog(1, 1);
        ResultSet resultDeleted = conn.createStatement().executeQuery("SELECT * FROM QuestLog WHERE EntryId = 1 AND UserId = 1");
        ResultSet resultNotDeleted = conn.createStatement().executeQuery("SELECT * FROM QuestLog WHERE EntryId = 4 AND UserId = 1");
        ResultSet resultNotDeleted2 = conn.createStatement().executeQuery("SELECT * FROM questproperties WHERE EntryId = 4");
        //check
        assertTrue(actualResult);
        assertThat(resultDeleted.next(), is(false));
        assertThat(resultNotDeleted.next(), is(true));
        assertThat(resultNotDeleted2.next(), is(true));
    }

    @Test
    public void addQuestToQuestlog() throws SQLException {
        //init
        int userId = 1;
        Map<String, String> properties = new HashMap<>();

        String key = "TopstukNaam";
        int value = 1;
        int questTypeId = 2;

        properties.put("TestKey", key);
        properties.put("TestKey2", String.valueOf(value));

        //test
        dao.addQuestToQuestlog(properties, userId, questTypeId);

        //check
        ResultSet resultAdded = conn.createStatement().executeQuery("SELECT qp.Key, qp.Value FROM QuestProperties qp " +
                "WHERE EntryId = 8 ORDER BY qp.Key ASC");
        resultAdded.next();
        assertEquals("TestKey", resultAdded.getString(1));
        assertEquals(key, resultAdded.getString(2));

        resultAdded.next();
        assertEquals("TestKey2", resultAdded.getString(1));
        assertEquals(String.valueOf(value), resultAdded.getString(2));
    }

	@Test
	public void findQuest(){
		User test2 = new User(2, "test@void", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", "muspi merol", 0, 2);
		Quest q = dao.find(2 , test2);
		assertEquals(q.getName(), "(EN)Scan qr code");
		assertEquals(q.getReward(), 10);
	}

}