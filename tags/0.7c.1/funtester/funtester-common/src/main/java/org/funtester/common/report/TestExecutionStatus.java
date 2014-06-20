package org.funtester.common.report;

/**
 * Test execution status (PASS, FAIL, ERROR, SKIP, UNKNOWN).
 * 
 * <p>
 * A PASS status occurs when a test runs successfully.
 * </p>
 * <p>
 * A FAIL status occurs when a test does not meet its expectations.
 * </p>
 * <p>
 * A ERROR status occurs when a test code is incorrect.
 * </p> 
 * <p>
 * A SKIP status occurs when a test that was was not executed because another
 * test that it depend upon failed. This status was added to make this tool
 * compatible with testng. Nevertheless, this tool do not generate tests that
 * depend on each other (yet), so, in theory, this status will never appear in
 * test results. See http://testng.org/doc/documentation-main.html for more
 * information about SKIP test results.
 * </p>
 * <p>
 * A UNKNOWN status occurs when the status was not recognized when importing
 * the tests from a execution report file.
 * </p>
 * 
 * 
 * @author Thiago Delgado Pinto
 *
 */
public enum TestExecutionStatus {
	PASS,
	FAIL,
	ERROR,
	SKIP,	
	UNKNOWN
}
