package org.funtester.common.util;

/**
 * Database useful constants.
 * 
 * @author	Thiago Delgado Pinto
 * @see		http://codeoftheday.blogspot.com/2012/12/java-database-connectivity-jdbc-url.html
 */
public final class DBConsts {
	
	private DBConsts() {}
	
	public static final String JDBC = "jdbc";
	
	// Types
	public static final String MYSQL_TYPE		= "mysql";
	public static final String FIREBIRD_TYPE	= "firebirdsql";
	public static final String POSTGRESQL_TYPE	= "postgresql";
	public static final String HSQLDB_TYPE		= "hsqldb";
	public static final String ORACLE_TYPE		= "oracle";
	public static final String SQLSERVER_TYPE	= "sqlserver";

	// Drivers
	public static final String MYSQL_DRIVER			= "com.mysql.jdbc.Driver";
	public static final String FIREBIRD_DRIVER		= "org.firebirdsql.jdbc.FBDriver";
	public static final String POSTGRESQL_DRIVER	= "org.postgresql.Driver";
	public static final String HSQLDB_DRIVER		= "org.hsqldb.jdbcDriver";
	public static final String ORACLE_DRIVER		= "oracle.jdbc.driver.OracleDriver";
	public static final String SQLSERVER_DRIVER		= "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	
	// Dialects
	public static final String MYSQL_HIBERATE_DIALECT		= "org.hibernate.dialect.MySQLDialect";
	public static final String FIREBIRD_HIBERATE_DIALECT	= "org.hibernate.dialect.FirebirdDialect";
	public static final String POSTGRESQL_HIBERATE_DIALECT	= "org.hibernate.dialect.PostgreSQLDialect";
	public static final String HSQLDB_HIBERATE_DIALECT		= "org.hibernate.dialect.HSQLDialect";
	public static final String ORACLE_HIBERATE_DIALECT		= "org.hibernate.dialect.OracleDialect";
	public static final String SQLSERVER_HIBERATE_DIALECT	= "org.hibernate.dialect.SQLServerDialect";		
	
	// Ports
	public static final int MYSQL_PORT		= 3306;
	public static final int FIREBIRD_PORT	= 3050;
	public static final int POSTGRESQL_PORT	= 5342;
	public static final int HSQLDB_PORT		= 9001;
	public static final int ORACLE_PORT		= 1521;
	public static final int SQLSERVER_PORT	= 1433;
	
	
	// JDBC connection string
	public static final String JDBC_CONNECTION_STRING = "jdbc:%s://%s:%d/%s";
	
	public static String toJDBCString(final String dbtype, final String host, final int port, final String dbname) {
		return String.format( JDBC_CONNECTION_STRING, dbtype, host, port, dbname );
	}
	
}
