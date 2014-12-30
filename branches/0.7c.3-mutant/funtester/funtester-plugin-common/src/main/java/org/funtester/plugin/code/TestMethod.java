package org.funtester.plugin.code;

import java.util.ArrayList;
import java.util.List;

/**
 * Test method
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TestMethod {
	
	private String name;
	private List< String > commands = new ArrayList< String >();
	
	public TestMethod() {
	}
	
	//
	// BUILDERS
	//
	
	public TestMethod withName(String value) {
		setName( value );
		return this;
	}
	
	public TestMethod addCommand(String value) {
		commands.add( value );
		return this;
	}
	
	public TestMethod withCommands(List< String > value) {
		setCommands( value );
		return this;
	}
	
	//
	// GETTERS AND SETTERS
	//

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List< String > getCommands() {
		return commands;
	}

	public void setCommands(List< String > commands) {
		this.commands = commands;
	}

}
