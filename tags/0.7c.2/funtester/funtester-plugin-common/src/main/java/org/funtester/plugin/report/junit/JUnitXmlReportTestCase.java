package org.funtester.plugin.report.junit;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * JUnit XML report test case
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class JUnitXmlReportTestCase {

	@XmlAttribute
	private String name;
	
	@XmlAttribute
	private double time;
	
	@XmlAttribute
	private String classname;
	
	@XmlElementWrapper(name="failures")
	@XmlElement(name="failure")	
	private JUnitXmlReportFailure failures;
	
	
	public JUnitXmlReportTestCase() {		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public JUnitXmlReportFailure getFailures() {
		return failures;
	}

	public void setFailures(JUnitXmlReportFailure failures) {
		this.failures = failures;
	}	
}
