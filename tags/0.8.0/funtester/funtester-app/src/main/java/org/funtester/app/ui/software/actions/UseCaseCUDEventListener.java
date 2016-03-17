package org.funtester.app.ui.software.actions;

import java.util.EventListener;

import org.funtester.core.software.Software;
import org.funtester.core.software.UseCase;

/**
 * Use case CUD (Create, Update, Delete) event listener
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface UseCaseCUDEventListener
	extends EventListener {
	
	void created(final Software parent, final UseCase obj, final int index);
	
	void updated(final Software parent, final UseCase obj, final int index);
	
	void deleted(final Software parent, final UseCase obj, final int index);
}
