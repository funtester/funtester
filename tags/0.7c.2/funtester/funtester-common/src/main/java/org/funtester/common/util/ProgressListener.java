package org.funtester.common.util;

import java.util.EventListener;

/**
 * Progress listener
 * 
 * @author Thiago Delgado Pinto
 * 
 */
public interface ProgressListener extends EventListener {
	
	/**
	 * Update the progress
	 * 
	 * @param current	Current step
	 * @param max		Max steps
	 * @param status	Status details
	 */
	void updateProgress(final int current, final int max, final String status);

}
