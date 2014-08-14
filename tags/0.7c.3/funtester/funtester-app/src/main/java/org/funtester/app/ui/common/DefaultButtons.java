package org.funtester.app.ui.common;

import javax.swing.JButton;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.util.ImageUtil;

/**
 * Default buttons.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class DefaultButtons {
	
	private DefaultButtons() {}

	public static JButton createOkButton() {
		JButton okButton = new JButton( Messages.alt( "_OK", "OK" ) );
		okButton.setName( "okButton" );
		okButton.setActionCommand( "OK" );
		okButton.setIcon( ImageUtil.loadIcon( ImagePath.okIcon() ) );
		return okButton;
	}
	
	public static JButton createCancelButton() {
		JButton cancelButton = new JButton( Messages.alt(  "_CANCEL", "Cancel" ) );
		cancelButton.setName( "cancelButton" );
		cancelButton.setActionCommand( "Cancel" );
		cancelButton.setIcon( ImageUtil.loadIcon( ImagePath.cancelIcon() ) );
		return cancelButton;
	}

}
