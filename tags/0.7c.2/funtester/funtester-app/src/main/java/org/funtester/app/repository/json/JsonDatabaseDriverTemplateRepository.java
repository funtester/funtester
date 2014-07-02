package org.funtester.app.repository.json;

import org.funtester.app.repository.DatabaseDriverTemplateRepository;
import org.funtester.app.util.JsonMapper;
import org.funtester.core.software.DatabaseDriverConfig;

/**
 * JSON repository for {@link DatabaseDriverConfig}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class JsonDatabaseDriverTemplateRepository implements
		DatabaseDriverTemplateRepository {
	
	private final String fileName;
	
	public JsonDatabaseDriverTemplateRepository(final String fileName) {
		this.fileName = fileName;
	}

	@Override
	public DatabaseDriverConfig first() throws Exception {
		return JsonMapper.readObject( fileName, DatabaseDriverConfig.class );
	}

	@Override
	public void save(DatabaseDriverConfig obj) throws Exception {
		JsonMapper.writeObject( fileName, obj );
	}

}
