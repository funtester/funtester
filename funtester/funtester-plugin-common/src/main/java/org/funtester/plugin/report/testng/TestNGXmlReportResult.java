package org.funtester.plugin.report.testng;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * TestNG's test execution report.<br />
 * <p>
 * Basic structure:
 * <ul>
 * 	<li>A report has one suite.</li>
 * 	<li>A suite has at least one test.</li>
 * 	<li>A test has at least one class.</li>
 *  <li>A class has at least one test method.</li>
 * 	<li>A test method can generate a exception in case of failure.</li>
 *  <li>A exception has a message and a stack trace.</li>
 * </ul>   
 * </p>
 *  
 * @author Thiago Delgado Pinto
 *
 */
@XmlRootElement(name="testng-results")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"skipped", "failed", "total", "passed", "reporterOutput", "suite"})
public class TestNGXmlReportResult {

	@XmlAttribute
	private int skipped;
	
	@XmlAttribute
	private int failed;
	
	@XmlAttribute
	private int total;
	
	@XmlAttribute
	private int passed;
	
	@XmlElement(name="reporter-output")
	private String reporterOutput;
	
	@XmlElement(name="suite")
	private TestNGXmlReportSuite suite;
	
	public TestNGXmlReportResult() {		
	}

	public int getSkipped() {
		return skipped;
	}

	public void setSkipped(int skipped) {
		this.skipped = skipped;
	}

	public int getFailed() {
		return failed;
	}

	public void setFailed(int failed) {
		this.failed = failed;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPassed() {
		return passed;
	}

	public void setPassed(int passed) {
		this.passed = passed;
	}

	public String getReporterOutput() {
		return reporterOutput;
	}

	public void setReporterOutput(String reporterOutput) {
		this.reporterOutput = reporterOutput;
	}

	public TestNGXmlReportSuite getSuite() {
		return suite;
	}

	public void setSuite(TestNGXmlReportSuite suite) {
		this.suite = suite;
	}
	
	// From Object
	
	public String toString() {
		return "{"
			+ " skipped: " + skipped 
			+ ", failed: " + failed
			+ ", total: " + total
			+ ", passed: " + passed
			+ ", reporterOutput: \"" + ( reporterOutput != null ? reporterOutput.toString() : "null" ) + "\""
			+ ", suite: " + ( suite != null ? suite.toString() : "null" )
			+ " }"
			;
	}
}
