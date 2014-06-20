package org.funtester.app.repository.json;

import org.funtester.app.project.AppConfiguration;
import org.funtester.app.repository.AppConfigurationRepository;
import org.funtester.app.util.JsonMapper;

/**
 * JSON repository for an {@link AppConfiguration}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class JsonAppConfigurationRepository implements
		AppConfigurationRepository {
	
	private final String filePath;
	
	public JsonAppConfigurationRepository(final String filePath) {
		this.filePath = filePath;
	}

	@Override
	public AppConfiguration first() throws Exception {
		return JsonMapper.readObject( filePath, AppConfiguration.class );
	}

	@Override
	public void save(AppConfiguration obj) throws Exception {
		JsonMapper.writeObject( filePath, obj );
	}

}
