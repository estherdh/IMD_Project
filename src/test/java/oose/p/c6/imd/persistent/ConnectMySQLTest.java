package oose.p.c6.imd.persistent;

import org.junit.After;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class ConnectMySQLTest {
	private Connection connection;

	@After
	public void setConnection() {
		connection = null;
	}

	@Test
	public void getConnectionTest() throws SQLException {
		//test
		connection = ConnectMySQL.getInstance().getConnection();
		//check
		connection.close();
		assertThat(connection.getMetaData().getUserName(), is(equalTo("root@localhost")));
	}

	@Test
	public void getUserTest() throws SQLException {
		//test
		connection = ConnectMySQL.getInstance().getConnection();

		ResultSet rs = connection.prepareStatement("Select * From users").executeQuery();

		assertTrue(rs.next());
	}
}