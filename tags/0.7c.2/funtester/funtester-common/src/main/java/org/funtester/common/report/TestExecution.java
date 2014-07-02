package org.funtester.common.report;

/**
 * Test execution information.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TestExecution {

	private int totalTests;		/// Total number of tests
	private int totalPassed;	/// Number of tests with PASS status
	private int totalSkipped;	/// Number of tests with SKIP status
	private int totalFailures;	/// Number of tests with FAIL status
	private int totalErrors;	/// Number of tests with ERROR status
	private int totalUnknown;	/// Number of tests with unknown status
	private long timeInMillis;	/// Execution time in milliseconds
	
	public TestExecution() {		
	}

	public int getTotalTests() {
		return totalTests;
	}

	public void setTotalTests(int totalTests) {
		this.totalTests = totalTests;
	}

	public int getTotalPassed() {
		return totalPassed;
	}

	public void setTotalPassed(int totalPassed) {
		this.totalPassed = totalPassed;
	}

	/**
	 * Increases the total passed.
	 * 
	 * @return	the new total passed.
	 */
	public int increaseTotalPassed() {
		return ++totalPassed;
	}
	
	public int getTotalSkipped() {
		return totalSkipped;
	}

	public void setTotalSkipped(int totalSkipped) {
		this.totalSkipped = totalSkipped;
	}

	public int getTotalFailures() {
		return totalFailures;
	}

	public void setTotalFailures(int totalFailures) {
		this.totalFailures = totalFailures;
	}
	
	/**
	 * Decrease the total of failures.
	 * 
	 * @return the new total of failures.
	 */
	public int decreaseTotalFailures() {
		return --totalFailures;
	}

	public int getTotalErrors() {
		return totalErrors;
	}

	public void setTotalErrors(int totalErrors) {
		this.totalErrors = totalErrors;
	}

	public int getTotalUnknown() {
		return totalUnknown;
	}

	public void setTotalUnknown(int totalUnknown) {
		this.totalUnknown = totalUnknown;
	}

	public long getTimeInMillis() {
		return timeInMillis;
	}

	public void setTimeInMillis(long timeInMillis) {
		this.timeInMillis = timeInMillis;
	}
	
	/**
	 * Returns the average time in milliseconds.
	 */
	public long averageTimeInMillis() {
		return totalTests > 0 ? timeInMillis / totalTests : 0;
	}	
}
