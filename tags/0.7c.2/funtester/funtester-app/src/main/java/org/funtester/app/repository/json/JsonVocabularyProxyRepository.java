package org.funtester.app.repository.json;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.funtester.app.project.VocabularyProxy;
import org.funtester.app.repository.VocabularyProxyRepository;
import org.funtester.app.util.JsonMapper;
import org.funtester.core.profile.Action;
import org.funtester.core.profile.Profile;
import org.funtester.core.profile.ProfileRepository;
import org.funtester.core.vocabulary.ActionNickname;
import org.funtester.core.vocabulary.Vocabulary;


/**
 * JSON vocabulary repository.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class JsonVocabularyProxyRepository implements VocabularyProxyRepository {
	
	private final String filePath;
	
	public JsonVocabularyProxyRepository(final String filePath) {
		this.filePath = filePath;
	}

	@Override
	public VocabularyProxy first() throws Exception {
		
		@SuppressWarnings("unchecked")
		Map< String, Object > m = JsonMapper.readObject( filePath, Map.class );
		
		final String profileFileName = (String) m.get( "profileFile" );
		File profileFile = new File( profileFileName );
		if ( ! profileFile.canRead() ) {
			final String MSG = "Arquivo inexistente ou não pode ser lido: \"%s.\""; // TODO i18n
			throw new Exception( MSG  );
		}
		profileFile = null; // close
		
		ProfileRepository profileRepository = new JsonProfileRepository( profileFileName );
		Profile profile = profileRepository.first();
		
		VocabularyProxy vp = mapToVocabularyProxy( m, profile, profileFileName );
		
		return vp;
	}	

	@Override
	public void save(VocabularyProxy obj) throws Exception {
		JsonMapper.writeObject( filePath, vocabularyProxyToMap( obj ) );
	}

	//
	// to Vocabulary
	//
	
	private VocabularyProxy mapToVocabularyProxy(
			final Map< String, Object > m,
			final Profile profile,
			final String fileName
			) {
		VocabularyProxy vp = new VocabularyProxy();
		vp.setProfileFile( fileName );
		
		@SuppressWarnings("unchecked")
		Vocabulary vocabulary = mapToVocabulary(
				(Map< String, Object >) m.get( "vocabulary" ),
				profile
				);
		
		vp.setVocabulary( vocabulary );
		return vp;
	}
		
	
	@SuppressWarnings("unchecked")
	private Vocabulary mapToVocabulary(
			final Map< String, Object > m,
			final Profile profile
			) {
		Vocabulary v = new Vocabulary();
		
		v.setProfile( profile );
		v.setName( (String) m.get( "name" ) );
		
		Locale locale = mapToLocale( (Map< String, Object >) m.get( "locale" ) );
		v.setLocale( locale );
		
		List< ActionNickname > actionNicknames = mapToListOfActionNickname(
				(List< Map< String, Object > >) m.get( "nicknames" ),
				profile
				);
				
		v.setNicknames( actionNicknames );
		
		return v;
	}

	private Locale mapToLocale(Map< String, Object > m) {
		return new Locale(
				(String) m.get( "language" ),
				(String) m.get( "country" )
				);
	}
	
	private List< ActionNickname > mapToListOfActionNickname(
			final List< Map< String, Object > > listOfMap,
			final Profile profile
			) {
		List< ActionNickname > l = new ArrayList< ActionNickname >();
		for ( Map< String, Object > m : listOfMap ) {
			l.add( mapToActionNickname( m, profile ) );
		}
		return l;
	}
	
	private ActionNickname mapToActionNickname(
			final Map< String, Object > m,
			final Profile profile
			) {
		ActionNickname an = new ActionNickname();
		an.setId( (Integer) m.get( "id" ) );
		an.setNickname( (String) m.get( "nickname" ) );
		Integer actionId = (Integer) m.get( "action" );
		//System.out.print( "Defining action with id " + actionId );
		Action action = profile.actionWithId( actionId );
		//System.out.println( " > action is " + ( ( action != null ) ? action.toString() : "null" ) );
		an.setAction( action );
		
		return an;
	}

	//
	// to Map
	//
	
	private Map< String, Object > vocabularyProxyToMap(VocabularyProxy obj) {
		Map< String, Object > m = new HashMap< String, Object >();
		m.put( "profileFile", obj.getProfileFile() );
		m.put( "vocabulary", vocabularyToMap( obj.getVocabulary() ) );
		return m;
	}	
	
	Map< String, Object > vocabularyToMap(Vocabulary v) {
		Map< String, Object > m = new HashMap< String, Object >();
		m.put( "name", v.getName() );
		m.put( "locale", localeToMap( v.getLocale() ) );
		m.put( "nicknames", nicknamesToMapList( v.getNicknames() ) );
		return m;
	}
	
	
	private List< Map< String, Object > > nicknamesToMapList(
			Collection< ActionNickname > nicknames) {
		List< Map< String, Object > > l =
				new ArrayList< Map< String, Object > >();
		for ( ActionNickname an : nicknames ) {
			l.add( actionNicknameToMap( an ) );
		}
		return l;
	}

	private Map< String, Object > actionNicknameToMap(ActionNickname an) {
		Map< String, Object > m = new HashMap< String, Object >();
		m.put( "action", an.getAction().getId() );
		m.put( "nickname",  an.getNickname() );
		return m;
	}

	Map< String, Object > localeToMap(Locale l) {
		Map< String, Object > m = new HashMap< String, Object >();
		m.put( "language", l.getLanguage() );
		m.put( "country", l.getCountry() );
		return m;
	}

}
