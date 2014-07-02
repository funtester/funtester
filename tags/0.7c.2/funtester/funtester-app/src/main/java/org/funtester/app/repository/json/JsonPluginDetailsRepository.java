package org.funtester.app.repository.json;

import org.funtester.app.project.PluginInfo;
import org.funtester.app.repository.PluginDetailsRepository;
import org.funtester.app.util.JsonMapper;

/**
 * JSON repository for a {@link PluginInfo}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class JsonPluginDetailsRepository implements PluginDetailsRepository {
	
	private final String fileName;
	
	public JsonPluginDetailsRepository(final String fileName) {
		this.fileName = fileName;
	}

	@Override
	public PluginInfo first() throws Exception {
		return JsonMapper.readObject( fileName, PluginInfo.class );
	}

	@Override
	public void save(PluginInfo obj) throws Exception {
		JsonMapper.writeObject( fileName, obj );
	}

}
