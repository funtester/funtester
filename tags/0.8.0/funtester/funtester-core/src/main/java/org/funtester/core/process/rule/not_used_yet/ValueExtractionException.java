package org.funtester.core.process.rule.not_used_yet;

/**
 * Occur when a value extraction is not well succeeded.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ValueExtractionException extends Exception {
	
	private static final long serialVersionUID = -7100344237281684372L;

	public ValueExtractionException() {
		super();
	}

	public ValueExtractionException(String message, Throwable cause) {
		super( message, cause );
	}

	public ValueExtractionException(String message) {
		super( message );
	}

	public ValueExtractionException(Throwable cause) {
		super( cause );
	}
	
}
