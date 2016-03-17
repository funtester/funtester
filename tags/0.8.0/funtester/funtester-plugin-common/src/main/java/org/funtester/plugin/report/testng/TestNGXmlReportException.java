package org.funtester.plugin.report.testng;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * TestNG XML report exception
 * 
 * @author Thiago Delgado Pinto
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"clazz", "message", "fullStackTrace"})
public class TestNGXmlReportException {
	
	@XmlAttribute(name="class")
	private String clazz;
	
	@XmlElement
	private String message;
	
	@XmlElement(name="full-stacktrace")
	private String fullStackTrace;

	public TestNGXmlReportException() {		
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFullStackTrace() {
		return fullStackTrace;
	}

	public void setFullStackTrace(String fullStackTrace) {
		this.fullStackTrace = fullStackTrace;
	}
	
	@Override
	public String toString() {
		return "{" 
			+ " clazz: \"" + ( clazz != null ? clazz : "null" ) + "\""
			+ ", message: \"" + ( message != null ? message : "null" ) + "\""
			+ ", fullStackTrace: \"" + ( fullStackTrace != null ? fullStackTrace : "null" ) + "\""
			+ " }"
			;
	}

}
