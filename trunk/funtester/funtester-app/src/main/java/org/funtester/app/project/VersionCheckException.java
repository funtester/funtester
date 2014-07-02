package org.funtester.app.project;

/**
 * Exception occurred when checking for a new version.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class VersionCheckException extends RuntimeException {

	private static final long serialVersionUID = 508731790590450392L;

	public VersionCheckException() {
	}

	public VersionCheckException(String message) {
		super( message );
	}

	public VersionCheckException(Throwable cause) {
		super( cause );
	}

	public VersionCheckException(String message, Throwable cause) {
		super( message, cause );
	}

}
