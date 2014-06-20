package org.funtester.common.at;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract test database script
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class AbstractTestDatabaseScript {

	private String description;
	private List< String > commands = new ArrayList< String >();
	private AbstractTestDatabaseConnection connection;

	public AbstractTestDatabaseScript() {
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List< String > getCommands() {
		return commands;
	}

	public void setCommands(List< String > commands) {
		this.commands = commands;
	}
	
	public void copyCommands(List< String > commandsToCopy) {
		commands.clear();
		commands.addAll( commandsToCopy );
	}

	public AbstractTestDatabaseConnection getConnection() {
		return connection;
	}

	public void setConnection(AbstractTestDatabaseConnection connection) {
		this.connection = connection;
	}	
}
