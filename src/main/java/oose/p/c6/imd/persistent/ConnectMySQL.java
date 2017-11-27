package oose.p.c6.imd.persistent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectMySQL {
	private static final Logger LOGGER = Logger.getLogger(ConnectMySQL.class.getName());
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
			LOGGER.log(Level.SEVERE, e.toString(), e);
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
			LOGGER.log(Level.SEVERE, e.toString(), e);
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return this.connection;
	}
}
