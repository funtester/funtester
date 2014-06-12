package org.funtester.plugin.report.junit;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * JUnit XML report failure
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class JUnitXmlReportFailure {

	@XmlAttribute
	private String type;
	
	@XmlAttribute
	private String message;
		
	@XmlAttribute
	private String name;	// "name" is the default attribute when the content
							// is between tags. For example,
							// <mytag>Hello</mytag> is equivalent to
							// <mytag name="Hello" ></mytag>
	
	public JUnitXmlReportFailure() {		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
