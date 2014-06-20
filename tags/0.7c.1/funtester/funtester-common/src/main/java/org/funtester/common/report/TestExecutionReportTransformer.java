package org.funtester.common.report;

/**
 * Transform a report into a {@link TestExecutionReport}.
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T> the report type to transform.
 */
public interface TestExecutionReportTransformer< T > {
	
	TestExecutionReport transform(final T report);

}
