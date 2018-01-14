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
import static org.mockito.Mockito.*;

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
        Quest expectedQuest1 = new Quest(1, "(EN)Scan qr code", "(EN)Scan a qr code", 10, 1, 0, 0, new QrScanQuest(new HashMap<>()));
        Quest expectedQuest2 = new Quest(2, "(NL)Stuur tekst", "(NL)Stuur een bepaald stuk tekst op", 15, 1, 0, 0, new QrScanQuest(new HashMap<>()));
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
        ResultSet resultDeleted = conn.createStatement().executeQuery("SELECT Removed FROM QuestLog WHERE EntryId = 1 AND UserId = 1");
        ResultSet resultNotDeleted = conn.createStatement().executeQuery("SELECT Removed FROM QuestLog WHERE EntryId = 3 AND UserId = 1");
        ResultSet resultNotDeleted2 = conn.createStatement().executeQuery("SELECT * FROM questproperties WHERE EntryId = 3");
        //check
        assertTrue(actualResult);
        assertThat(resultDeleted.next(), is(true));
        assertThat(resultNotDeleted.next(), is(false));
        assertThat(resultNotDeleted2.next(), is(true));
    }

    @Test
    public void addQuestToQuestlogType1Test() throws SQLException {
        //init
        int userId = 1;
        Map<String, String> properties = new HashMap<>();

        String value = "AAB";

        int questTypeId = 1;

        properties.put("TestKey", value);

        String expectedDescription = "Bezoek De verzamel schuur en scan de QR-code.";

        //test
        dao.addQuestToQuestlog(properties, userId, questTypeId);

        //check
        ResultSet resultAdded = conn.createStatement().executeQuery("SELECT qp.Key, qp.Value, qp.Description FROM QuestProperties qp " +
                "WHERE EntryId = 10 ORDER BY qp.Key ASC");

        resultAdded.next();
        assertEquals("TestKey", resultAdded.getString(1));
        assertEquals(value, resultAdded.getString(2));
        assertEquals(expectedDescription, resultAdded.getString(3));
    }

    @Test
    public void addQuestToQuestlogType3Test() throws SQLException {
        //init
        int userId = 1;
        Map<String, String> properties = new HashMap<>();

        String value1 = "AAA";
        int value2 = 1;

        int questTypeId = 3;

        properties.put("TestKey", value1);
        properties.put("TestKey2", String.valueOf(value2));

        String expectedDescription1 = null;
        String expectedDescription2 = "Bekijk de schat Het test object uit het tijdperk tijdperk test.";

        //test
        dao.addQuestToQuestlog(properties, userId, questTypeId);

        //check
        ResultSet resultAdded = conn.createStatement().executeQuery("SELECT qp.Key, qp.Value, qp.Description FROM QuestProperties qp " +
                "WHERE EntryId = 10 ORDER BY qp.Key ASC");
        resultAdded.next();
        assertEquals("TestKey", resultAdded.getString(1));
        assertEquals(value1, resultAdded.getString(2));
        assertEquals(expectedDescription1, resultAdded.getString(3));

        resultAdded.next();
        assertEquals("TestKey2", resultAdded.getString(1));
        assertEquals(String.valueOf(value2), resultAdded.getString(2));
        assertEquals(expectedDescription2, resultAdded.getString(3));
    }

    @Test
    public void addQuestToQuestlogType4Test() throws SQLException {
        //init
        int userId = 1;
        Map<String, String> properties = new HashMap<>();

        int value = 1;

        int questTypeId = 4;

        properties.put("TestKey", String.valueOf(value));

        String expectedDescription = "Open het boek uit tijdperk tijdperk test om de quest te voltooien.";

        //test
        dao.addQuestToQuestlog(properties, userId, questTypeId);

        //check
        ResultSet resultAdded = conn.createStatement().executeQuery("SELECT qp.Key, qp.Value, qp.Description FROM QuestProperties qp " +
                "WHERE EntryId = 10 ORDER BY qp.Key ASC");
        resultAdded.next();
        assertEquals("TestKey", resultAdded.getString(1));
        assertEquals(String.valueOf(value), resultAdded.getString(2));
        assertEquals(expectedDescription, resultAdded.getString(3));
    }

    @Test
    public void findQuest() {
        User test2 = new User(2, "test@void", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", "muspi merol", 0, 2);
        Quest q = dao.find(2, test2);
        assertEquals(q.getName(), "(EN)Scan qr code");
        assertEquals(q.getReward(), 10);
    }
}