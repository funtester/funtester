package org.funtester.app.startup;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppConfiguration;
import org.funtester.app.project.AppState;
import org.funtester.app.project.Directories;
import org.funtester.app.repository.json.JsonVocabularyProxyRepository;
import org.funtester.core.vocabulary.Vocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task for loading the existing vocabularies.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class LoadVocabulariesTask implements Task {
	
	private static final Logger logger = LoggerFactory.getLogger(
			LoadVocabulariesTask.class );

	@Override
	public String description() {
		return Messages.alt( "_VOCABULARIES_LOADING", "Loading vocabularies..." );
	}

	@Override
	public boolean canContinueInCaseOfError() {
		return false;
	}

	@Override
	public void perform(final String args[], AppState appState)
			throws Exception {
		
		// Put the parameter
		
		Map< Vocabulary, String > vocabularyMap = new HashMap< Vocabulary, String >();
		appState.setVocabularyMap( vocabularyMap );
		
		// Get the configuration
		
		final AppConfiguration cfg = appState.getConfiguration();
		
		if ( null == cfg || null == cfg.getDirectories() ) {
			final String msg = Messages.alt( "_CONFIGURATION_READING_ERROR", "Error while getting the configuration." );
			throw new Exception( msg );
		}		
		
		// Load the vocabularies
		
		String dirPath = cfg.getDirectories().getVocabulary();
		if ( null == dirPath ) {
			dirPath = Directories.DEFAULT.getVocabulary();
		}
		
		File dir = new File( dirPath );
		if ( ! dir.isDirectory() ) {
			final String msg = String.format( Messages.alt(
					"_VOCABULARY_DIRECTORY_NOT_FOUND",
					"Vocabulary directory not found: %s" ), dir.getAbsolutePath() );
			throw new Exception( msg );
		}
		
		for ( final File file : dir.listFiles() ) {
			final String filePath = file.getPath();
			logger.debug( "Vocabulary file is \"" + filePath + "\"" );
			Vocabulary v = new JsonVocabularyProxyRepository( filePath ).first().getVocabulary();
			vocabularyMap.put( v, filePath ); // Map a vocabulary to its file path
		}
	}

}
