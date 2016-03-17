package org.funtester.common.report;

/**
 * Test method execution information.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TestMethodExecution { // Do NOT inherit from TestExecution
			
	private String name;
	private boolean isForConfiguration;			// Informs if the method is for configuration purposes
	private long timeInMillis;					// Execution time in milliseconds
	private TestExecutionStatus status;
	// All the attributes from now on can be null if the test pass.
	private String exceptionClass;					
	private String exceptionMessage;				
	private String stackTrace;
	// Values got from stack trace analysis
	private String erroneousFile;
	private Integer erroreousFileLineNumber;	 
	private String erroreousLineOfCode;
	private Long erroneousSemanticStepId;
	
	
	public TestMethodExecution() {		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isForConfiguration() {
		return isForConfiguration;
	}

	public void setForConfiguration(boolean isForConfiguration) {
		this.isForConfiguration = isForConfiguration;
	}
	
	public long getTimeInMillis() {
		return timeInMillis;
	}

	public void setTimeInMillis(long timeInMillis) {
		this.timeInMillis = timeInMillis;
	}
	
	public TestExecutionStatus getStatus() {
		return status;
	}

	public void setStatus(TestExecutionStatus status) {
		this.status = status;
	}

	public String getExceptionClass() {
		return exceptionClass;
	}

	public void setExceptionClass(String exceptionClass) {
		this.exceptionClass = exceptionClass;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
	
	public String getErroneousFile() {
		return erroneousFile;
	}

	public void setErroneousFile(String erroneousFile) {
		this.erroneousFile = erroneousFile;
	}

	public Integer getErroreousFileLineNumber() {
		return erroreousFileLineNumber;
	}

	public void setErroreousFileLineNumber(Integer erroreousFileLineNumber) {
		this.erroreousFileLineNumber = erroreousFileLineNumber;
	}

	public String getErroreousLineOfCode() {
		return erroreousLineOfCode;
	}

	public void setErroreousLineOfCode(String erroreousLineOfCode) {
		this.erroreousLineOfCode = erroreousLineOfCode;
	}

	public Long getErroneousSemanticStepId() {
		return erroneousSemanticStepId;
	}

	public void setErroneousSemanticStepId(Long erroneousSemanticStepId) {
		this.erroneousSemanticStepId = erroneousSemanticStepId;
	}
}
