package org.funtester.common.report;

import java.util.ArrayList;
import java.util.List;

/**
 * Test case execution information.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TestCaseExecution { // Do NOT inherit from TestExecution

	private String className;
	private List< TestMethodExecution > methods = new ArrayList< TestMethodExecution >();
	
	public TestCaseExecution() {
		super();
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List< TestMethodExecution > getMethods() {
		return methods;
	}

	public void setMethods(List< TestMethodExecution > methods) {
		this.methods = methods;
	}

	public boolean addMethod(TestMethodExecution testMethod) {
		return methods.add( testMethod );
	}
}
