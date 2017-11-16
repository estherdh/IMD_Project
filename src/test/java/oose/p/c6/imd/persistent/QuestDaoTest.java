package oose.p.c6.imd.persistent;

import oose.p.c6.imd.persistent.dao.QuestDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class QuestDaoTest {
	private QuestDao dao;
	private Connection conn;

	@Before
	public void setUp() throws SQLException {
		conn = DriverManager.getConnection("jdbc:h2:~/librarian", "sa", "");
		dao = new QuestDao();
	}

	@After
	public void closeConn() throws SQLException {
		conn.createStatement().executeUpdate("DROP TABLE IF EXISTS QuestLog");
		conn.close();
	}

	@Test
	public void removeQuestTestSuccess() throws SQLException {
		//init
		boolean expectedResult = true;
		conn.createStatement().executeUpdate("CREATE TABLE QuestLog" +
				"(" +
				"EntryId INT NOT NULL," +
				"UserId INT NOT NULL," +
				"QuestTypeId INT NOT NULL," +
				"State TINYINT(1) NOT NULL DEFAULT '0'" +
				")" +
				";");
		conn.createStatement().executeUpdate("INSERT INTO QuestLog VALUES " +
				"(1, 2, 2, 0)," +
				"(1, 3, 5, 1)");
		//test
		boolean actualResult = dao.removeQuest(1, 3);
		ResultSet resultDeleted = conn.createStatement().executeQuery("SELECT * FROM QuestLog WHERE EntryId = 1 AND UserId = 3");
		ResultSet resultNotDeleted = conn.createStatement().executeQuery("SELECT * FROM QuestLog WHERE EntryId = 1 AND UserId = 2");
		//check
		assertThat(actualResult, is(expectedResult));
		assertThat(resultDeleted.next(), is(false));
		assertThat(resultNotDeleted.next(), is(true));
	}

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