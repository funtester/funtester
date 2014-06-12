package org.funtester.app.project;

/**
 * Documentation generation exception.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DocGenerationException extends RuntimeException {

	private static final long serialVersionUID = 1114430147718696423L;

	public DocGenerationException() {
	}

	public DocGenerationException(String message) {
		super( message );
	}

	public DocGenerationException(Throwable cause) {
		super( cause );
	}

	public DocGenerationException(String message, Throwable cause) {
		super( message, cause );
	}

	public DocGenerationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super( message, cause, enableSuppression, writableStackTrace );
	}

}
