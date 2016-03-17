package org.funtester.app.repository.json;

import org.funtester.app.util.JsonMapper;
import org.funtester.core.profile.Profile;
import org.funtester.core.profile.ProfileRepository;

/**
 * JSON repository for a {@link Profile}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class JsonProfileRepository implements ProfileRepository {
	
	private final String filePath;

	public JsonProfileRepository(final String filePath) {
		this.filePath = filePath;
	}
	
	@Override
	public Profile first() throws Exception {
		return JsonMapper.readObject( filePath, Profile.class );
	}

	@Override
	public void save(Profile obj) throws Exception {
		JsonMapper.writeObject( filePath, obj );
	}
}
