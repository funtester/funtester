package org.funtester.app.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

/**
 * Testing framework information
 * 
 * @author Thiago Delgado Pinto
 *
 */
@JsonIdentityInfo(generator=PropertyGenerator.class, property="id", scope=FrameworkInfo.class)
public class FrameworkInfo {
	
	/** Internal identification. Examples: "junit", "testng" */
	private String id;
	/** Name presented to the user. Examples: "JUnit", "TestNG" */
	private String name;
	
	public FrameworkInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
