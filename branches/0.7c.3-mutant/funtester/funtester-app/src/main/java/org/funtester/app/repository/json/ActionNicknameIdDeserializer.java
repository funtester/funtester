package org.funtester.app.repository.json;

import java.io.IOException;

import org.funtester.core.vocabulary.ActionNickname;
import org.funtester.core.vocabulary.Vocabulary;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * ActionNickname id deserializer
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ActionNicknameIdDeserializer extends JsonDeserializer< ActionNickname >{
	
	private final Vocabulary vocabulary;

	public ActionNicknameIdDeserializer(Vocabulary vocabulary) {
		this.vocabulary = vocabulary;
	}

	@Override
	public ActionNickname deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		long id = jp.getIntValue();
		return vocabulary.nicknameWithId( id );
	}

}
