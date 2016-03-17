package org.funtester.app.repository.json;

import java.io.IOException;

import org.funtester.core.profile.ElementType;
import org.funtester.core.profile.Profile;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * ElementType id deserializer
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ElementTypeIdDeserializer extends JsonDeserializer< ElementType >{
	
	private final Profile profile;

	public ElementTypeIdDeserializer(final Profile profile) {
		this.profile = profile;
	}

	@Override
	public ElementType deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		long id = jp.getIntValue();
		return profile.typeWithId( id );
	}

}
