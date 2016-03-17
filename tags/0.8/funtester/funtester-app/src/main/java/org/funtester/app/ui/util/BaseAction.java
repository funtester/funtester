package org.funtester.app.ui.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 * Base action with helper methods and a parameterized {@code ActionListener}.
 * 
 * @author Thiago Delgado Pinto
 * 
 * @see {@link javax.swing.Action}
 * @see http://docs.oracle.com/javase/6/docs/api/javax/swing/Action.html
 *
 */
public class BaseAction extends AbstractAction {

	private static final long serialVersionUID = 5104914875536743334L;
	private ActionListener listener = null;

	public BaseAction() {
		super();
	}

	public BaseAction(String name) {
		super( name );
	}

	public BaseAction(String name, Icon icon) {
		super( name );
		withIcon( icon );
	}
	
	public BaseAction withName(final String name) {
		putValue( NAME, name );
		if ( null == getValue( SHORT_DESCRIPTION ) ) {
			withShortDescription( name );
		}
		return this;
	}
	
	public BaseAction withMnemonic(final String key) {
		putValue( MNEMONIC_KEY, key );		
		return this;
	}
	
	public BaseAction withAcceleratorKey(final KeyStroke ks) {
		putValue( ACCELERATOR_KEY, ks );
		updateShortDescription();
		return this;
	}
	
	public BaseAction withIcon(final Icon icon) {
		if ( icon != null ) {
			putValue( SMALL_ICON, icon );
		}
		return this;
	}
	
	public BaseAction withShortDescription(final String description) {
		putValue( SHORT_DESCRIPTION, description );
		updateShortDescription();
		return this;
	}
	
	private void updateShortDescription() {
		if ( null == getValue( SHORT_DESCRIPTION )
				|| null == getValue( ACCELERATOR_KEY ) ) {
			return;
		}
		String description = getValue( SHORT_DESCRIPTION ).toString();
		// Remove the parenthesis
		int index = description.indexOf( "(" );
		if ( index >= 0 ) {
			description = description.substring( 0, index - 1 );
		}		
		final String acceleratorKey = KeyUtil.keyDescription(
				(KeyStroke) getValue( ACCELERATOR_KEY ) );
		description = description + " (" + acceleratorKey + ")";
		putValue( SHORT_DESCRIPTION, description );
	}
	
	public BaseAction withLongDescription(final String description) {
		putValue( LONG_DESCRIPTION, description );
		return this;
	}	
	
	public BaseAction withListener(ActionListener listener) {
		this.listener = listener;
		return this;
	}
	
	public BaseAction withEnabled(final boolean enabled) {
		setEnabled( enabled );
		return this;
	}
	
	public void actionPerformed(ActionEvent e) {
		if ( null == listener ) {
			return;
		}
		listener.actionPerformed( e );
	}	
}
