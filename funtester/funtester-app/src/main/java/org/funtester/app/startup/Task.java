package org.funtester.app.startup;

import org.funtester.app.project.AppState;

/**
 * Startup task.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface Task {

	String description();
	
	boolean canContinueInCaseOfError();
	
	/**
	 * Perform the task using the arguments and setting the parameters.
	 * 
	 * @param args			the application arguments (in).
	 * @param parameters	the parameters (in, out)
	 * @throws Exception
	 */
	void perform(final String args[], AppState appState) throws Exception;
}
