package org.funtester.app.ui.software.actions;

import java.util.EventListener;

import org.funtester.core.software.DatabaseConfig;
import org.funtester.core.software.Software;

/**
 * Database config CUD (Create, Update, Delete) event listener
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface DatabaseConfigCUDEventListener extends EventListener {
		
	void created(final Software parent, final DatabaseConfig obj, final int index);
	
	void updated(final Software parent, final DatabaseConfig obj, final int index);
	
	void deleted(final Software parent, final DatabaseConfig obj, final int index);
}
