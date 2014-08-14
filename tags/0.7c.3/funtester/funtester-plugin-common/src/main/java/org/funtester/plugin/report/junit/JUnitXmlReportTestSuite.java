package org.funtester.plugin.report.junit;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * JUnit's test execution report.<br />
 * <p>
 * Basic structure:
 * <ul>
 * 	<li>A suite has at least one test case.</li>
 * 	<li>A test case has a name, a test method name a can have more than one
 *  failure</li>
 * 	<li>A failure has a message, a type that is a exception class name, and
 *  the stack trace in his name.</li>
 * </ul>   
 * </p>
 *  
 * @author Thiago Delgado Pinto
 *
 */
@XmlRootElement
public class JUnitXmlReportTestSuite {
	
	@XmlAttribute
	private String hostname;
	
	@XmlAttribute
	private String name;
	
	@XmlAttribute
	private int tests;
	
	@XmlAttribute
	private int failures;
	
	@XmlAttribute
	private String timestamp; // TODO see the right type
	
	@XmlAttribute
	private double time;
	
	@XmlAttribute
	private int errors;
		
	@XmlElementWrapper(name="testCases")
	@XmlElement(name="testcase")	
	private List< JUnitXmlReportTestCase > testCases;
	

	public JUnitXmlReportTestSuite() {		
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTests() {
		return tests;
	}

	public void setTests(int tests) {
		this.tests = tests;
	}

	public int getFailures() {
		return failures;
	}

	public void setFailures(int failures) {
		this.failures = failures;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public int getErrors() {
		return errors;
	}

	public void setErrors(int errors) {
		this.errors = errors;
	}

	public List< JUnitXmlReportTestCase > getTestCases() {
		return testCases;
	}

	public void setTestCases(List< JUnitXmlReportTestCase > testCases) {
		this.testCases = testCases;
	}
}
