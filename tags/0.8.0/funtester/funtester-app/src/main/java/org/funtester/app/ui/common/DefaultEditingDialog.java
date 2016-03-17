package org.funtester.app.ui.common;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.util.EditingDialog;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.common.util.Copier;

/**
 * Default editing dialog. Adds i18n and icons to the OK and Cancel buttons.
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T>	A class that implements Copier.
 */
public abstract class DefaultEditingDialog< T extends Copier< ? super T > >
	extends EditingDialog< T > {

	private static final long serialVersionUID = -4913985554505668892L;

	public DefaultEditingDialog() {
		super();
		setBounds( 100, 100, 450, 300 );
		
		okButton.setText( Messages.getString( "_OK" ) );
		okButton.setIcon( ImageUtil.loadIcon( ImagePath.okIcon() ) );
		
		cancelButton.setText( Messages.getString( "_CANCEL" ) );
		cancelButton.setIcon( ImageUtil.loadIcon( ImagePath.cancelIcon() ) );
	}

}
