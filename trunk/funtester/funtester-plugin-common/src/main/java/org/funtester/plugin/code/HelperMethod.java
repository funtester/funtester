package org.funtester.plugin.code;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper method
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class HelperMethod {

	private String returnType;
	private String name;
	private List< Variable > args = new ArrayList< Variable >();
	private List< String > commands = new ArrayList< String >();
	
	public HelperMethod() {
	}
	
	//
	// BUILDERS
	//
	
	public HelperMethod withReturnType(String value) {
		setReturnType( value );
		return this;
	}
	
	public HelperMethod withName(String value) {
		setName( value );
		return this;
	}
	
	public HelperMethod addArg(Variable value) {
		args.add( value );
		return this;
	}
	
	public HelperMethod addCommand(String value) {
		commands.add( value );
		return this;
	}
	
	public HelperMethod withArgs(List< Variable > value) {
		setArgs( value );
		return this;
	}
	
	public HelperMethod withCommands(List< String > value) {
		setCommands( value );
		return this;
	}
	
	//
	// GETTERS AND SETTERS
	//

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List< Variable > getArgs() {
		return args;
	}

	public void setArgs(List< Variable > args) {
		this.args = args;
	}

	public List< String > getCommands() {
		return commands;
	}

	public void setCommands(List< String > commands) {
		this.commands = commands;
	}
	
}
