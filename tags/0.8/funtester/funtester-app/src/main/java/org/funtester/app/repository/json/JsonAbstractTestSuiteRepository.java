package org.funtester.app.repository.json;

import java.io.File;

import org.funtester.app.repository.AbstractTestSuiteRepository;
import org.funtester.app.util.JsonMapper;
import org.funtester.common.at.AbstractTestSuite;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON repository for an {@link AbstractTestSuite}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class JsonAbstractTestSuiteRepository implements
		AbstractTestSuiteRepository {
	
	private final String filePath;
	private final ObjectMapper mapper;
	
	public JsonAbstractTestSuiteRepository(final String filePath) {
		this.filePath = filePath;
		this.mapper = new ObjectMapper();
		JsonMapper.configure( mapper );
	}	

	@Override
	public AbstractTestSuite first() throws Exception {
		return mapper.readValue( new File( filePath ), AbstractTestSuite.class );
	}

	@Override
	public void save(final AbstractTestSuite obj) throws Exception {
		mapper.writeValue( new File( filePath ), obj );
	}

}
