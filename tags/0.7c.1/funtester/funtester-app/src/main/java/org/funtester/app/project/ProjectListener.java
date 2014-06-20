package org.funtester.app.project;

import java.util.EventListener;

/**
 * Project listener
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface ProjectListener extends EventListener {

	void hasOpened(final Project p);
	
	void hasClosed(final Project p);
	
	void hasSaved(final Project p);
}
