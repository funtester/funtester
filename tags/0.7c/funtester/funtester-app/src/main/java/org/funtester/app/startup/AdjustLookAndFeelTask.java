package org.funtester.app.startup;

import javax.swing.UIManager;

import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppState;

/**
 * Task for adjusting the look-and-feel of the UI.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class AdjustLookAndFeelTask implements Task {
	
	private final String DEFAULT_LOOK_AND_FEEL_NAME = "Windows"; 
	private final String DEFAULT_LOOK_AND_FEEL_CLASS = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	
	@Override
	public String description() {
		return Messages.alt(  "_LOOK_AND_FEEL_ADJUSTING", "Adjusting look-and-feel..." );
	}

	@Override
	public boolean canContinueInCaseOfError() {
		return true;
	}
	
	@Override
	public void perform(final String args[], AppState appState)
			throws Exception {
		
		String lafName = appState.getLookAndFeelName();
		
		if ( null == lafName || lafName.isEmpty() ) {
			
			try {
				UIManager.installLookAndFeel(
						DEFAULT_LOOK_AND_FEEL_NAME,
						DEFAULT_LOOK_AND_FEEL_CLASS );
			} catch (Exception e) {
				// Do nothing
			}
			
			lafName = DEFAULT_LOOK_AND_FEEL_NAME;
			appState.setLookAndFeelName( lafName );	
		}
		
		String lafClass = appState.getLookAndFeelMap().get( lafName );
		if ( null == lafClass ) {
			lafClass = DEFAULT_LOOK_AND_FEEL_CLASS;
			appState.putLookAndFeel( lafName, lafClass );
		}

		UIManager.setLookAndFeel( lafClass );
		
		//UIManager.setLookAndFeel( new SubstanceBusinessLookAndFeel() );
		//UIManager.setLookAndFeel( new SubstanceBusinessBlackSteelLookAndFeel() );
		
		//JDialog.setDefaultLookAndFeelDecorated( true );
		//JFrame.setDefaultLookAndFeelDecorated( true );
	}

}
