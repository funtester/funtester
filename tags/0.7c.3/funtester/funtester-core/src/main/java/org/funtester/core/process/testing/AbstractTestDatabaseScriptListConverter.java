package org.funtester.core.process.testing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.funtester.common.at.AbstractTestDatabaseConnection;
import org.funtester.common.at.AbstractTestDatabaseScript;
import org.funtester.core.software.DatabaseConfig;
import org.funtester.core.software.DatabaseScript;

/**
 * Converts a list of {@link AbstractTestDatabaseScript} from another source.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class AbstractTestDatabaseScriptListConverter {
	
	/**
	 * Generates a list of {@link AbstractTestDatabaseScript} from a list of
	 * {@link DatabaseScript}.
	 * 
	 * @param databaseScripts	the scripts to be converted.
	 * @return					a converted list.
	 */
	public List< AbstractTestDatabaseScript > fromDatabaseScripts(
			final List< DatabaseScript > databaseScripts) {
		List< AbstractTestDatabaseScript > scripts = new ArrayList< AbstractTestDatabaseScript >();
		
		Map< String, AbstractTestDatabaseConnection > connections =
			new HashMap< String, AbstractTestDatabaseConnection >();
		
		for ( DatabaseScript dbs : databaseScripts ) {
			final String dbName = dbs.getDatabaseConfig().getName();
			if ( ! connections.containsKey( dbName ) ) {
				connections.put( dbName, convertoToSemanticConnection( dbs.getDatabaseConfig() ) );
			}
			
			AbstractTestDatabaseScript script = new AbstractTestDatabaseScript();
			script.setDescription( dbs.getDescription() );			
			script.setConnection( connections.get( dbName ) );
			script.copyCommands( dbs.getCommands() );
			
			scripts.add( script );
		}
		return scripts;
	}


	private AbstractTestDatabaseConnection convertoToSemanticConnection(
			final DatabaseConfig dbC) {
		AbstractTestDatabaseConnection c = new AbstractTestDatabaseConnection();
		c.setName( dbC.getName() );
		c.setDriver( dbC.getDriver() );
		c.setUser( dbC.getUser() );
		c.setPassword( dbC.getPassword() );
		c.setJdbcURL( dbC.toJdbcUrl() );
		return c;
	}

}
