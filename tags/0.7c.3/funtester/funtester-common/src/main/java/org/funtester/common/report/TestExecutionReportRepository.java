package org.funtester.common.report;

/**
 * Repository for a {@link TestExecutionReport}. 
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface TestExecutionReportRepository {
	
	/**
	 * Load the first {@link TestExecutionReport}.
	 * @return
	 * @throws Exception
	 */
	TestExecutionReport first() throws Exception;
	
	/**
	 * Save a {@link TestExecutionReport}.
	 * @param obj	the object to save.
	 * @throws Exception
	 */
	void save(TestExecutionReport obj) throws Exception;

}
