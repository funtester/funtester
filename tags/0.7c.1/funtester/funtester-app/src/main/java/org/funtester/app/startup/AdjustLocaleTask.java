package org.funtester.app.startup;

import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppConfiguration;
import org.funtester.app.project.AppState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task for adjusting current locale according to the loaded ones.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class AdjustLocaleTask implements Task {
	
	private static Logger logger = LoggerFactory.getLogger( AdjustLocaleTask.class );

	@Override
	public String description() {
		return Messages.alt( "_LOCALE_ADJUSTING", "Adjusting locales..." ); 
	}

	@Override
	public boolean canContinueInCaseOfError() {
		return true;
	}

	@Override
	public void perform(final String args[], AppState appState)
			throws Exception {

		AppConfiguration cfg = appState.getConfiguration();
		if ( null == cfg ) {			
			throw new Exception( String.format( Messages.alt(
					"_PARAMETER_NOT_FOUND", "Expected parameter not found: \"%s\"." ),
					"configuration" ) );
		}		
		// Adjusting
		Locale locale = new Locale( cfg.getLocaleLanguage(), cfg.getLocaleCountry() );
		logger.debug( "Locale is " + locale.toString() );
		
		Locale.setDefault( locale );
		Messages.changeLocaleTo( locale );		
		JOptionPane.setDefaultLocale( locale );
		JPanel.setDefaultLocale( locale );
	}

}
