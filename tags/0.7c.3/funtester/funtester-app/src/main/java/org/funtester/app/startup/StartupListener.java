package org.funtester.app.startup;

import java.util.EventListener;

/**
 * Startup listener.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface StartupListener extends EventListener {
	
	void versionRead(final String version);
	
	void statusUpdated(final String status);

}
