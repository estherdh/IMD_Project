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
			Class.forName(properties.getProperty("driver"));
			this.connection = DriverManager.getConnection(properties.getProperty("databaseurl") + "?user=" + properties.getProperty("user") + "&password=" + properties.getProperty("password"));
		} catch (ClassNotFoundException e) {
			logger.severe(e.getMessage());
			e.printStackTrace();

		} catch (IOException e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
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


	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection()
	{
		return this.connection;
	}
}
