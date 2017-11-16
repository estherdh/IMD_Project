package oose.p.c6.imd.persistent;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class ConnectMySQL {
	private static Logger logger = Logger.getLogger(ConnectMySQL.class.getName());
	private Properties properties;

	public ConnectMySQL()
	{
		this(new Properties());
	}

	public ConnectMySQL(Properties properties)
	{
		try {
			this.properties = properties;
			this.properties.load(getClass().getClassLoader().getResourceAsStream("database.properties"));
			Class.forName(properties.getProperty("driver"));
		} catch (ClassNotFoundException e) {
			logger.severe(e.getMessage());
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}

	}

	public Connection getConnection()
	{
		try {
			return DriverManager.getConnection(properties.getProperty("databaseurl") + "?user=" + properties.getProperty("user") + "&password=" + properties.getProperty("password"));
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		}
		return null;
	}
}
