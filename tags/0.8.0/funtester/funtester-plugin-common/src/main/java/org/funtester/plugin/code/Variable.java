package org.funtester.plugin.code;

/**
 * Variable
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class Variable {
	
	private String type;
	private String name;
	
	public Variable() {
	}
	
	public Variable(final String type, final String name) {
		this.type = type;
		this.name = name;
	}
	
	//
	// BUILDERS
	//
	
	public Variable withType(final String value) {
		setType( value );
		return this;
	}
	
	public Variable withName(final String value) {
		setName( value );
		return this;
	}
	
	//
	// GETTERS AND SETTERS
	//

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
