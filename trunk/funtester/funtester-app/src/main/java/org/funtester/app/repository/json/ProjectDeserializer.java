package org.funtester.app.repository.json;

import java.io.IOException;

import org.funtester.app.project.Project;
import org.funtester.app.project.VocabularyProxy;
import org.funtester.app.repository.VocabularyProxyRepository;
import org.funtester.core.software.Software;
import org.funtester.core.vocabulary.Vocabulary;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class ProjectDeserializer extends JsonDeserializer< Project >{

	@Override
	public Project deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		
		ObjectCodec oc = jp.getCodec();
		JsonNode node = oc.readTree( jp );
		
		final String vocabularyFile = node.get( "vocabularyFile" ).asText();
		
		
		// This uses a NEW mapper !!!
		VocabularyProxyRepository vpr = new JsonVocabularyProxyRepository( vocabularyFile );
		VocabularyProxy vocabularyProxy;
		try {
			vocabularyProxy = vpr.first();
		} catch ( Exception e ) {
			throw new IOException( e );
		}
		Vocabulary vocabulary = vocabularyProxy.getVocabulary();
		
		jp.nextValue();
		Software software = jp.readValueAs( Software.class );
		software.setVocabulary( vocabulary );
		
		Project p = new Project();
		p.setVocabularyFile( vocabularyFile );
		p.setSoftware( software );
		return p;
	}

}
