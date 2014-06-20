package org.funtester.core.util;

/**
 * Keeps a value name for helping the UI about putting the focus in the
 * right component. 
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class InvalidValueException extends DomainException {

	private static final long serialVersionUID = 9064137578108103102L;
	private String valueName = "";
	
	public InvalidValueException(
			final String message,
			final String valueName
			) {
		super( message );
		this.valueName = valueName;
	}

	public String getValueName() {
		return valueName;
	}
}
