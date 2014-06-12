package org.funtester.plugin.code.java.db;

/**
 * Database script runner generator
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DBScriptRunnerGenerator {
	
	private static final String EOL = "\n";
	private StringBuilder sb = new StringBuilder();
	
	public static void main(String args[]) {
		System.out.println( new DBScriptRunnerGenerator().generate( "mypackage" ) );
	}

	public String generate(final String aPackage) {		
		final String TAB1 = "\t";
		final String TAB2 = "\t\t";
		final String TAB3 = "\t\t\t";
		
		add( "package " + aPackage + ";" );
		add( "" );
		add( "import java.sql.Connection;" );
		add( "import java.sql.DriverManager;" );
		add( "import java.sql.SQLException;" );
		add( "import java.sql.Statement;" );
		add( "" );
		add( "/**" );
		add( " * Runs database scripts in a transaction." );
		add( " * " );
		add( " * @author FunTester" );
		add( " *" );
		add( " */" );
		add( "public class DBScriptRunner {" );
		add( "" );
		add( TAB1 + "private final Connection connection;" );
		add( TAB1 );
		add( TAB1 + "/**" );
		add( TAB1 + " * Creates the database script runner with some parameters." );
		add( TAB1 + " * " );
		add( TAB1 + " * @param driver	the database driver to be used." );
		add( TAB1 + " * @param jdbcURL	the connection URL in JDBC format." );
		add( TAB1 + " * @param user		the database user." );
		add( TAB1 + " * @param password	the database password." );	 
		add( TAB1 + " * @throws ClassNotFoundException" );
		add( TAB1 + " * @throws SQLException" );
		add( TAB1 + " */" );
		add( TAB1 + "public DBScriptRunner(" );
		add( TAB3 + "final String driver," );
		add( TAB3 + "final String jdbcURL," );
		add( TAB3 + "final String user," );
		add( TAB3 + "final String password" );
		add( TAB3 + ") throws ClassNotFoundException, SQLException {" );
		add( TAB2 + "Class.forName( driver );" );
		add( TAB2 + "connection = DriverManager.getConnection( jdbcURL, user, password );" );
		add( TAB2 + "connection.setAutoCommit( false );" );
		add( TAB1 + "}" );
		add( TAB1 );
		add( TAB1 + "/**" );
		add( TAB1 + " * Run some commands in a transaction." );
		add( TAB1 + " * " );
		add( TAB1 + " * @param commands	the commands to be executed in a transaction." );
		add( TAB1 + " * @return			the count of the rows affected by each command line." );	
		add( TAB1 + " * @throws SQLException" );
		add( TAB1 + " */" );
		add( TAB1 + "public int[] run(final String ...commands) throws SQLException {" );	
		add( TAB2 + "Statement st = connection.createStatement();" );
		add( TAB2 + "for ( String cmd : commands ) {" );
		add( TAB3 + 	"st.addBatch( cmd );" );
		add( TAB2 + "}" );
		add( TAB2 + "final int results[];" );
		add( TAB2 + "try {" );
		add( TAB3 + "results = st.executeBatch();" );
		add( TAB3 + "connection.commit();" );
		add( TAB2 + "} catch (SQLException e) {" );
		add( TAB3 + "connection.rollback();" );
		add( TAB3 + "throw e;" );
		add( TAB2 + "}" ); // catch
		add( TAB2 + "return results;" );
		add( TAB1 + "}" ); // method
		add( "}" ); // class
				
		return sb.toString();
	}
	
	private final void add(final String s) {
		sb.append( s ).append( EOL );
	}	
}
