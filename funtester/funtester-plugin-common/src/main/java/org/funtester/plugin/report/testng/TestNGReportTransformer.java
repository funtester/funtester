package org.funtester.plugin.report.testng;

import org.funtester.common.report.TestCaseExecution;
import org.funtester.common.report.TestExecutionReport;
import org.funtester.common.report.TestExecutionReportTransformer;
import org.funtester.common.report.TestExecutionStatus;
import org.funtester.common.report.TestMethodExecution;
import org.funtester.common.report.TestSuiteExecution;
import org.joda.time.DateTime;

/**
 * TestNG report transformer
 *
 * @author Thiago Delgado Pinto
 *
 */
public class TestNGReportTransformer
	implements TestExecutionReportTransformer< TestNGXmlReportResult >{

	private final String originalTestResultFile;


	public TestNGReportTransformer(final String originalTestResultFile) {
		this.originalTestResultFile = originalTestResultFile;
	}

	public TestExecutionReport transform(final TestNGXmlReportResult tngReport) {
		TestExecutionReport testReport = new TestExecutionReport();

//		System.out.println( "TNG Report: " + tngReport );

		// REPORT
		testReport.setToolName( "Funtester for TestNG and FEST Swing" );
		testReport.setTargetLanguage( "Java" );
		testReport.setTargetGUI( "Swing" );
		testReport.setTargetTestingFrameworks( "TestNG 6.8, FEST Swing 1.2.1" );
		testReport.setOriginalTestResultFile( originalTestResultFile );

		testReport.setCreation( ( new DateTime() ).toString()  );

		if ( null == tngReport || null == tngReport.getSuite() ) {
			return testReport; // Return as is
		}

		testReport.setTimeInMillis( ( tngReport.getSuite() != null ) ? tngReport.getSuite().getDurationMillis() : 0 );
		testReport.setTotalTests( tngReport.getTotal() );
		testReport.setTotalPassed( tngReport.getPassed() );
		testReport.setTotalFailures( tngReport.getFailed() );
		testReport.setTotalErrors( 0 ); // TestNG does not use ERROR but only FAIL
		testReport.setTotalSkipped( tngReport.getSkipped() );

		// SUITE
		TestNGXmlReportSuite tngSuite = tngReport.getSuite();

		TestSuiteExecution testSuite = new TestSuiteExecution();
		testSuite.setName( tngSuite.getName() );
		testSuite.setTimeInMillis( tngSuite.getDurationMillis() );
		testSuite.setTotalTests( tngReport.getTotal() );
		testSuite.setTotalPassed( tngReport.getPassed() );
		testSuite.setTotalFailures( tngReport.getFailed() );
		testSuite.setTotalErrors( 0 ); // TestNG does not use ERROR but only FAIL
		testSuite.setTotalSkipped( tngReport.getSkipped() );

		testReport.addSuite( testSuite ); // Add the created suite

		int totalUnknown = 0;

		for ( TestNGXmlReportTest tngTest : tngSuite.getTests() ) {
			if ( null == tngTest || null == tngTest.getClasses() ) { continue; }
			// Ignore the tests and go to the classes
			for ( TestNGXmlReportClass tngClass : tngTest.getClasses() ) {

				// TEST CASE
				TestCaseExecution testCase = new TestCaseExecution();
				testCase.setClassName( tngClass.getName() );

				for ( TestNGXmlReportTestMethod tngMethod: tngClass.getTestMethods() )  {

					// TEST METHOD
					TestMethodExecution testMethod = new TestMethodExecution();
					testMethod.setName( tngMethod.getName() );
					testMethod.setForConfiguration( tngMethod.isConfig() );
					testMethod.setTimeInMillis( tngMethod.getDurationMillis() );
					testMethod.setStatus( toTestExecutionStatus( tngMethod.getStatus() ) );
					// Compute the unknown results
					if ( testMethod.getStatus() == TestExecutionStatus.UNKNOWN ) {
						totalUnknown++;
					}

					TestNGXmlReportException tngException = tngMethod.getException();
					if ( tngException != null ) {
						testMethod.setExceptionClass( tngException.getClazz() );
						testMethod.setExceptionMessage(tngException.getMessage() );
						testMethod.setStackTrace( tngException.getFullStackTrace() );
					} // if

					testCase.addMethod( testMethod ); // Add the test method to the test case
				} // for method

				testSuite.addTestCase( testCase ); // Add the test case to the suite
			} // for class
		}
		// Set the total unknown
		testSuite.setTotalUnknown( totalUnknown );
		testReport.setTotalUnknown( totalUnknown );

		return testReport;
	}


	private TestExecutionStatus toTestExecutionStatus(final String status) {
		for ( TestExecutionStatus st : TestExecutionStatus.values() ) {
			if ( st.toString().equalsIgnoreCase( status ) ) {
				return st;
			}
		}
		return TestExecutionStatus.UNKNOWN;
	}

}
