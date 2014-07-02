package org.funtester.common.util;

import java.util.EventListener;

/**
 * Processing listener
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface ProcessingListener extends EventListener {
	
	void started(String details);
	
	void finished(String details);

}
