package org.funtester.common.report;

import java.util.ArrayList;
import java.util.List;

/**
 * Test suite execution information.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TestSuiteExecution extends TestExecution {
		
	private String name;
	private List< TestCaseExecution > testCases = new ArrayList< TestCaseExecution >();
	
	public TestSuiteExecution() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List< TestCaseExecution > getTestCases() {
		return testCases;
	}

	public void setTestCases(List< TestCaseExecution > testCases) {
		this.testCases = testCases;
	}
	
	public boolean addTestCase(TestCaseExecution testCase) {
		return testCases.add( testCase );
	}
}
