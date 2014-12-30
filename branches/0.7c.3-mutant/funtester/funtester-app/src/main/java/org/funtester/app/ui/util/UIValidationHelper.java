package org.funtester.app.ui.util;

import java.awt.Component;

import javax.swing.JComponent;

import org.funtester.common.util.Validator;
import org.funtester.core.util.InvalidValueException;

/**
 * Help with the UI validation.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class UIValidationHelper {
	
	private UIValidationHelper() {}

	/**
	 * Validate an object an show the appropriate message.
	 * 
	 * @param container		the component that contains the UI elements.
	 * @param msgTitle		the message title.
	 * @param validator		the object validator.
	 * @param obj			the object to be validated.
	 * @return				true if the object is valid, false otherwise.
	 */
	public static < T > boolean validate(
			final JComponent container,
			final String msgTitle,
			Validator< T > validator,
			T obj
			) {
		if ( null == container ) throw new IllegalArgumentException( "container is null" );
		if ( null == msgTitle ) throw new IllegalArgumentException( "title is null" );
		if ( null == validator ) throw new IllegalArgumentException( "validator is null" );
		if ( null == obj ) throw new IllegalArgumentException( "obj is null" );
		try {
			validator.validate( obj );
			return true;
		} catch (InvalidValueException ive ) {
			handleInvalidValueException( container, msgTitle, ive );
		} catch ( Exception e1 ) {
			String msg = e1.getLocalizedMessage();
			if ( null == msg || msg.isEmpty() ) {
				msg = "";
			}
			MsgUtil.info( container, msg, msgTitle );
		}
		return false;
	}
	
	
	public static void handleInvalidValueException(
			final JComponent container,
			final String msgTitle,
			final InvalidValueException ive
			) {
		handleException( container, msgTitle, ive );
		
		if ( ive.getValueName() != null && ! ive.getValueName().isEmpty() ) {
			// Tries to find the component and put the focus on it.
			Component c = UIUtil.findComponentWithName( container, ive.getValueName() );
			if ( c != null ) {
				c.requestFocusInWindow();
			}
		}
	}
	
	
	public static void handleException(
			final JComponent container,
			final String msgTitle,
			final Exception e
			) {
		String msg = e.getLocalizedMessage();
		if ( null == msg || msg.isEmpty() ) {
			e.printStackTrace();
			msg = String.format( "An unknown error of type \"%s\" has ocorred.",
					e.getClass().getName() );
			MsgUtil.error( container, msg, msgTitle );
		} else {
			MsgUtil.info( container, msg, msgTitle );
		}
		
	}
	
}
