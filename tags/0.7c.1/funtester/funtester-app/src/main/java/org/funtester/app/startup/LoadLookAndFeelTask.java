package org.funtester.app.startup;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task for loading the available look-and-feels.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class LoadLookAndFeelTask implements Task {
	
	private static final Logger logger = LoggerFactory.getLogger( LoadLookAndFeelTask.class );
	
	@Override
	public String description() {
		return Messages.alt( "_LOOK_AND_FEEL_LOADING", "Loading look and feel options..." );
	}

	@Override
	public boolean canContinueInCaseOfError() {
		return true;
	}

	@Override
	public void perform(final String args[], AppState appState)
			throws Exception {
		
		// Load the look-and-feels
		for ( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() ) {
			logger.debug( "Look and feel available: " + info.getName() );
			appState.putLookAndFeel( info.getName(), info.getClassName() );
		}
	}

}
