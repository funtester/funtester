package org.funtester.app.repository.json;

import org.funtester.app.util.JsonMapper;
import org.funtester.common.report.TestExecutionReport;
import org.funtester.common.report.TestExecutionReportRepository;

/**
 * JSON repository for {@link TestExecutionReport}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class JsonTestExecutionReportRepository implements
		TestExecutionReportRepository {
	
	private final String fileName;
	
	public JsonTestExecutionReportRepository(final String fileName) {
		this.fileName = fileName;
	}

	public TestExecutionReport first() throws Exception {
		return JsonMapper.readObject( fileName, TestExecutionReport.class );
	}

	public void save(TestExecutionReport obj) throws Exception {
		JsonMapper.writeObject( fileName, obj );
	}

}
