package org.funtester.common.util;

/**
 * Exception occurred when loading a driver.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DriverLoadException extends Exception {
	
	private static final long serialVersionUID = 961098089801380170L;

	public DriverLoadException() {
		super();
	}

	public DriverLoadException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super( message, cause, enableSuppression, writableStackTrace );
	}

	public DriverLoadException(String message, Throwable cause) {
		super( message, cause );
	}

	public DriverLoadException(String message) {
		super( message );
	}

	public DriverLoadException(Throwable cause) {
		super( cause );
	}

}
