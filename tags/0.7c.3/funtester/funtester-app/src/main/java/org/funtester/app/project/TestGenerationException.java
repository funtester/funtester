package org.funtester.app.project;

/**
 * Test generation exception
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TestGenerationException extends RuntimeException {

	private static final long serialVersionUID = -3092479103924875542L;

	public TestGenerationException() {
	}

	public TestGenerationException(String message) {
		super( message );
	}

	public TestGenerationException(Throwable cause) {
		super( cause );
	}

	public TestGenerationException(String message, Throwable cause) {
		super( message, cause );
	}

}
