package org.funtester.app.ui.util;

import java.util.EventListener;

/**
 * Command event listener.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface CommandEventListener extends EventListener {

	void execute(Object o);

}
