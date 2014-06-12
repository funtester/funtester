package org.funtester.core.util;

/**
 * Domain related exception.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DomainException extends Exception {

	private static final long serialVersionUID = 7462280368421340295L;

	public DomainException() { }

	public DomainException(String message) {
		super( message );
	}

	public DomainException(Throwable cause) {
		super( cause );
	}

	public DomainException(String message, Throwable cause) {
		super( message, cause );
	}

	public DomainException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super( message, cause, enableSuppression, writableStackTrace );
	}

}
