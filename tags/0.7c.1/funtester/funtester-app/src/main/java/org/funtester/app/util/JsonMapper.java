package org.funtester.app.util;

import java.io.File;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

/**
 * JSON object mapper.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class JsonMapper {
	
	private static ObjectMapper mapper = null;
	
	private JsonMapper() {} // Hide constructor
		
	public static ObjectMapper getMapper() {
		if ( mapper != null ) {
			return mapper;
		}
		mapper = new ObjectMapper();
		configure( mapper );
		return mapper;
	}
	
	public static void configure(ObjectMapper mapper) {
		mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
		mapper.configure( SerializationFeature.INDENT_OUTPUT, true );
		mapper.registerModule( new JodaModule() );
	}

	public static <T> T readObject(
			final String fileName,
			final Class< T > clazz
			) throws Exception {
		return getMapper().readValue( new File( fileName ), clazz );
	}
	
	public static <T> void writeObject(final String fileName, final T object) throws Exception {
		getMapper().writeValue( new File( fileName ), object );
	}
}
