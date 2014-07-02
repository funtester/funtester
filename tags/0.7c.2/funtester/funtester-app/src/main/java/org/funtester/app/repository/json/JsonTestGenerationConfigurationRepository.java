package org.funtester.app.repository.json;

import org.funtester.app.util.JsonMapper;
import org.funtester.common.generation.TestGenerationConfiguration;
import org.funtester.common.generation.TestGenerationConfigurationRepository;

/**
 * JSON repository for a {@link TestGenerationConfiguration}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class JsonTestGenerationConfigurationRepository implements
		TestGenerationConfigurationRepository {
	
	private final String filePath;

	public JsonTestGenerationConfigurationRepository(final String filePath) {
		this.filePath = filePath;
	}
	
	@Override
	public TestGenerationConfiguration first() throws Exception {
		return JsonMapper.readObject( filePath, TestGenerationConfiguration.class );
	}

	@Override
	public void save(TestGenerationConfiguration obj) throws Exception {
		JsonMapper.writeObject( filePath, obj );
	}

}
