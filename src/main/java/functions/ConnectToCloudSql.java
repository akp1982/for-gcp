package functions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class ConnectToCloudSql {
	private static final String CLOUD_SQL_CONNECTION_NAME = System.getenv("CLOUD_SQL_CONNECTION_NAME");
	private static final String DB_USER = System.getenv("DB_USER");
	private static final String DB_PASS = System.getenv("DB_PASS");
	private static final String DB_NAME = System.getenv("DB_NAME");

	@SuppressFBWarnings(value = "USBR_UNNECESSARY_STORE_BEFORE_RETURN", justification = "Necessary for sample region tag.")
	public DataSource createConnectionPool() {

		HikariConfig config = new HikariConfig();

		config.setJdbcUrl(String.format("jdbc:mysql:///%s", DB_NAME));
		config.setUsername(DB_USER); // e.g. "root", "mysql"
		config.setPassword(DB_PASS); // e.g. "my-password"

		config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory");
		config.addDataSourceProperty("cloudSqlInstance", CLOUD_SQL_CONNECTION_NAME);

		// The ipTypes argument can be used to specify a comma delimited list of
		// preferred IP types
		// for connecting to a Cloud SQL instance. The argument ipTypes=PRIVATE will
		// force the
		// SocketFactory to connect with an instance's associated private IP.
		config.addDataSourceProperty("ipTypes", "PUBLIC,PRIVATE");

		config.setMaximumPoolSize(5);

		config.setMinimumIdle(5);

		config.setConnectionTimeout(10000); // 10 seconds

		config.setIdleTimeout(600000); // 10 minutes

		config.setMaxLifetime(1800000); // 30 minutes

		DataSource pool = new HikariDataSource(config);

		return pool;
	}
	
	  public void createTable(DataSource pool) throws SQLException {
		    // Safely attempt to create the table schema.
		    try (Connection conn = pool.getConnection()) {
		      String stmt =
		          "CREATE TABLE IF NOT EXISTS votes ( "
		              + "vote_id SERIAL NOT NULL, time_cast timestamp NOT NULL, candidate CHAR(6) NOT NULL,"
		              + " PRIMARY KEY (vote_id) );";
		      try (PreparedStatement createTableStatement = conn.prepareStatement(stmt);) {
		        createTableStatement.execute();
		      }
		    }
		  }

}
