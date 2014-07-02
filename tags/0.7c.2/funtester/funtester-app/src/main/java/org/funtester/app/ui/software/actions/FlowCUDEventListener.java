package org.funtester.app.ui.software.actions;

import java.util.EventListener;

import org.funtester.core.software.Flow;
import org.funtester.core.software.UseCase;

/**
 * Flow CUD (Create, Update, Delete) event listener
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface FlowCUDEventListener 
	extends EventListener {
		
	void created(final UseCase parent, final Flow obj, final int index);
	
	void updated(final UseCase parent, final Flow obj, final int index);
	
	void deleted(final UseCase parent, final Flow obj, final int index);

}
