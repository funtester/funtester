package org.funtester.core.util;

/**
 * Repository exception
 *  
 * @author Thiago Delgado Pinto
 *
 */
public class RepositoryException extends Exception {
	
	private static final long serialVersionUID = -4707873300563145251L;
	
	public RepositoryException() {
		super();
	}

	public RepositoryException(String message, Throwable cause) {
		super( message, cause );
	}

	public RepositoryException(String message) {
		super( message );
	}

	public RepositoryException(Throwable cause) {
		super( cause );
	}

}
