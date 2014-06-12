package org.funtester.core.util;

/**
 * Driver unavailable exception
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DriverUnavailableException extends Exception {

	private static final long serialVersionUID = 7763894159209380148L;

	public DriverUnavailableException() {
		super();
	}

	public DriverUnavailableException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super( message, cause, enableSuppression, writableStackTrace );
	}

	public DriverUnavailableException(String message, Throwable cause) {
		super( message, cause );
	}

	public DriverUnavailableException(String message) {
		super( message );
	}

	public DriverUnavailableException(Throwable cause) {
		super( cause );
	}

}
