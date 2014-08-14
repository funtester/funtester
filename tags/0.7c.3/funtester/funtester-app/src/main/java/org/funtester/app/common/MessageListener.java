package org.funtester.app.common;

import java.util.EventListener;

/**
 * Message listener.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface MessageListener extends EventListener {

	/**
	 * Indicate that a message was published.
	 * 
	 * @param message published message.
	 */
	void published(final String message);
}
