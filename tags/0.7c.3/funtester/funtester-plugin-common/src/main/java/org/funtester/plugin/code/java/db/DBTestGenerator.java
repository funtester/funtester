package org.funtester.plugin.code.java.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Database test generator.
 * 
 * TODO finish implementing it
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DBTestGenerator {
		
	DBScriptRunner scriptRunner;
	
	public String generate() {
		StringBuilder sb = new StringBuilder();

		return sb.toString();
	}

	
	public void beforeClass() throws Exception {
		scriptRunner = new DBScriptRunner(
				"",
				"",
				"",
				""
				);
	}
	
	public void beforeEachTest() throws Exception {
		scriptRunner.run( commandsToRunBeforeEachTest() );
	}
	
	// Returns the commands for the XXX database
	private String[] commandsToRunBeforeEachTest() throws Exception {
		List< String > commands = new ArrayList< String >();
		commands.add( "" );
		commands.add( "" );
		return commands.toArray( new String[0] );
	}

}
