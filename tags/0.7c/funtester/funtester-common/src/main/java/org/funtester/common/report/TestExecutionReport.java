package org.funtester.common.report;

import java.util.ArrayList;
import java.util.List;

/**
 * Test execution report.
 * <p>
 * Basic structure:
 * <ul>
 * 	<li>A report can have more than one test suite.</li>
 *  <li>A test suite can have more than one test case.</li>
 *  <li>A test case can have more than one test method.</li>
 *  <li>A test method can have (inside) a exception message, exception class
 *  and a stack trace.</li>
 * </ul>
 * </p>
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TestExecutionReport extends TestExecution {

	/** Creation date and time in JodaTime string format */
	private String creation;
	
	/** Tool used to generate the report file */
	private String toolName;
	
	/** Target programming language */
	private String targetLanguage;
	
	/** Target GUI frameworks. Examples: Swing, Web */
	private String targetGUI;
	
	/** Target testing frameworks. Examples: TestNG and FEST, JUnit and Selenium */
	private String targetTestingFrameworks;
	
	/** Original file with test results */
	private String originalTestResultFile;
	
	/** Test suites */
	private List< TestSuiteExecution > suites = new ArrayList< TestSuiteExecution >();
	
	
	public TestExecutionReport() {
	}	

	public String getCreation() {
		return creation;
	}

	public void setCreation(String creation) {
		this.creation = creation;
	}	
	
	public String getToolName() {
		return toolName;
	}

	public void setToolName(String toolName) {
		this.toolName = toolName;
	}

	public String getTargetLanguage() {
		return targetLanguage;
	}

	public void setTargetLanguage(String targetLanguage) {
		this.targetLanguage = targetLanguage;
	}

	public String getTargetGUI() {
		return targetGUI;
	}

	public void setTargetGUI(String targetGUI) {
		this.targetGUI = targetGUI;
	}

	public String getTargetTestingFrameworks() {
		return targetTestingFrameworks;
	}

	public void setTargetTestingFrameworks(String frameworks) {
		this.targetTestingFrameworks = frameworks;
	}

	public String getOriginalTestResultFile() {
		return originalTestResultFile;
	}

	public void setOriginalTestResultFile(String originalTestResultFile) {
		this.originalTestResultFile = originalTestResultFile;
	}

	public List< TestSuiteExecution > getSuites() {
		return suites;
	}

	public void setSuites(List< TestSuiteExecution > suites) {
		this.suites = suites;
	}

	public boolean addSuite(TestSuiteExecution rs) {
		return suites.add( rs );
	}
}
