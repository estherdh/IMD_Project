package oose.p.c6.imd.persistent;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.ejb.Singleton;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;
import com.mysql.cj.jdbc.Driver;

public class ConnectMySQL {
	private static Logger logger = Logger.getLogger(ConnectMySQL.class.getName());
	private Properties properties;
	private Connection connection;
	private static ConnectMySQL instance;

	private ConnectMySQL()
	{
		this(new Properties());
	}

	private ConnectMySQL(Properties properties)
	{
		try {
			this.properties = properties;
			this.properties.load(getClass().getClassLoader().getResourceAsStream("database.properties"));
		} catch (IOException e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		}

	}

	public static ConnectMySQL getInstance() {
		if (instance == null) {
			instance = new ConnectMySQL();
		}
		return instance;
	}


	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Connection getConnection()
	{
		try {
			if (connection == null || this.connection.isClosed()) {
				Class.forName(properties.getProperty("driver"));
				this.connection = DriverManager.getConnection(properties.getProperty("databaseurl"), properties.getProperty("user"), properties.getProperty("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return this.connection;
	}
}
