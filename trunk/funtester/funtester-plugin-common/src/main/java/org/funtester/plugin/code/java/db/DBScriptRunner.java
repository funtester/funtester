package org.funtester.plugin.code.java.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Run database scripts in a transaction.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DBScriptRunner {
	
	private final Connection connection;

	/**
	 * Create the database script runner with some parameters.
	 * 
	 * @param driver	the database driver to be used.
	 * @param jdbcURL	the connection URL in JDBC format.
	 * @param user		the database user.
	 * @param password	the database password.	 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public DBScriptRunner(
			final String driver,
			final String jdbcURL,
			final String user,
			final String password
			) throws ClassNotFoundException, SQLException {
		Class.forName( driver );
		connection = DriverManager.getConnection( jdbcURL, user, password );
		connection.setAutoCommit( false );
	}
	
	/**
	 * Run some commands in a transaction.
	 * 
	 * @param commands	the commands to be executed in a transaction.
	 * @return			the count of the rows affected by each command line.	
	 * @throws SQLException
	 */
	public int[] run(final String ...commands) throws SQLException {
		Statement st = connection.createStatement();
		for ( String cmd : commands ) {
			st.addBatch( cmd );		
		}
		final int results[];
		try {		
			results = st.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw e;
		}
		return results;
	}
}
