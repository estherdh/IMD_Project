package oose.p.c6.imd.Dao;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

class ConnectMySQL {
	private static Connection connection;

	//Alleen gebruikt voor testing. Aangezien static methods niet gemockt kunnen worden.
	static void setConnection(Connection connection) {
		ConnectMySQL.connection = connection;
	}

	static Connection getConnection() {
		if (connection == null) {
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setUser("root");
			dataSource.setPassword("root");
			dataSource.setServerName("localhost");
			dataSource.setDatabaseName("librariantest");
			try {
				connection = dataSource.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return connection;
	}
}
