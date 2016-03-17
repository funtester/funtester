package org.funtester.app.ui.software.actions;

import java.util.EventListener;

import org.funtester.core.software.QueryConfig;
import org.funtester.core.software.Software;

/**
 * Query config CUD (Create, Update, Delete) event listener
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface QueryConfigCUDEventListener extends EventListener {
		
	void created(final Software parent, final QueryConfig obj, final int index);
	
	void updated(final Software parent, final QueryConfig obj, final int index);
	
	void deleted(final Software parent, final QueryConfig obj, final int index);
}