package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.*;
import oose.p.c6.imd.persistent.ConnectMySQL;
import org.h2.jdbc.JdbcSQLException;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.omg.CORBA.Any;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
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
		Quest expectedQuest1 = new Quest("(EN)Scan qr code", "(EN)Scan a qr code", 10, new QrScanQuest(new HashMap<>()));
		Quest expectedQuest2 = new Quest("(NL)Stuur tekst", "(NL)Stuur een bepaald stuk tekst op", 15, new QrScanQuest(new HashMap<>()));
		//test
		List<Quest> actualResult = dao.getQuestsForUser(2, 2);
		//check
		assertThat(actualResult.get(0).getName(), is(expectedQuest1.getName()));
		assertThat(actualResult.get(0).getDescription(), is(expectedQuest1.getDescription()));
		assertThat(actualResult.get(0).getReward(), is(expectedQuest1.getReward()));
		assertTrue(actualResult.get(0).getQuestStrategy() instanceof IQuestType);
		assertThat(actualResult.get(1).getName(), is(expectedQuest2.getName()));
		assertThat(actualResult.get(1).getDescription(), is(expectedQuest2.getDescription()));
		assertThat(actualResult.get(1).getReward(), is(expectedQuest2.getReward()));
		assertTrue(actualResult.get(1).getQuestStrategy() instanceof IQuestType);
	}

	@Test
	public void getQuestsFromUserTestUserDoesntExist() throws Exception {
		//test
		List<Quest> actualResult = dao.getQuestsForUser(1200, 2);
		//check
		assertThat(actualResult.size(), is(0));
	}

	@Test
	public void getQuestsFromUserTestErrorShouldReturnNull() throws Exception {
		//init
		List<Quest> expectedResult = null;
		conn.createStatement().executeUpdate("DROP ALL OBJECTS"); //When there are not tables the query, the query will always throw an error.
		//test
		List<Quest> actualResult = dao.getQuestsForUser(1,1);
		assertThat(actualResult, is(expectedResult));
	}

//	@Test
//	public void removeQuestTestSuccess() throws SQLException {
//		//init
//		boolean expectedResult = true;
//		conn.createStatement().executeUpdate("CREATE TABLE QuestLog" +
//				"(" +
//				"EntryId INT NOT NULL," +
//				"UserId INT NOT NULL," +
//				"QuestTypeId INT NOT NULL," +
//				"State TINYINT(1) NOT NULL DEFAULT '0'" +
//				")" +
//				";");
//		conn.createStatement().executeUpdate("INSERT INTO QuestLog VALUES " +
//				"(1, 2, 2, 0)," +
//				"(1, 3, 5, 1)");
//		//test
//		boolean actualResult = dao.removeQuest(1, 3);
//		ResultSet resultDeleted = conn.createStatement().executeQuery("SELECT * FROM QuestLog WHERE EntryId = 1 AND UserId = 3");
//		ResultSet resultNotDeleted = conn.createStatement().executeQuery("SELECT * FROM QuestLog WHERE EntryId = 1 AND UserId = 2");
//		//check
//		assertThat(actualResult, is(expectedResult));
//		assertThat(resultDeleted.next(), is(false));
//		assertThat(resultNotDeleted.next(), is(true));
//	}

	@Test
	public void removeQuestTestException() throws SQLException {
		//init
		boolean expectedResult = false;
		//test
		boolean actualResult = dao.removeQuest(0, 0);
		//check
		assertThat(actualResult, is(expectedResult));
	}


}