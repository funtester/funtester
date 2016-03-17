package org.funtester.core.util;

import java.util.EventListener;

/**
 * Update listener.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface UpdateListener extends EventListener {
	
	void updated(final Object o);

}
