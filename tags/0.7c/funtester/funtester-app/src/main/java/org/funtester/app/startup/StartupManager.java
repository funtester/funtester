package org.funtester.app.startup;

import java.util.ArrayList;
import java.util.List;

import org.funtester.app.project.AppState;
import org.funtester.common.util.Announcer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A GUI independent startup manager.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class StartupManager {

	private final Logger logger = LoggerFactory.getLogger( StartupManager.class );	

	private List< Task > tasks = new ArrayList< Task >();

	private final Announcer< StartupListener > announcer =
			Announcer.to( StartupListener.class );

	
	/**
	 * Register a listener and create default tasks.
	 * 
	 * @param listener	the startup listener.
	 */
	public StartupManager() {
	}
	
	/**
	 * Add a {@link StartupListener}.
	 * 
	 * @param l	the listener to add.
	 * @return	{@code true} if added, {@link false} otherwise.
	 */
	public boolean addListener(StartupListener l) {
		return announcer.addListener( l );
	}
	
	public void clearListeners() {
		announcer.clear();
	}
	
	/**
	 * Add a {@link Task}.
	 * 
	 * @param obj	the task to add.
	 * @return		{@code true} if added, {@link false} otherwise.
	 */
	public boolean addTask(Task obj) {
		return tasks.add( obj );
	}
	
	/**
	 * Start the application with the given arguments.
	 * 
	 * @param args	the application's arguments.
	 * @return		a map with the application parameters loading and configured
	 * 				during the initialization process.
	 * @throws Exception
	 */
	public AppState start(final String args[]) throws Exception {
		
		AppState appState = new AppState();
		
		for ( Task task : tasks ) {
			// Announce the task
			announcer.announce().statusUpdated( task.description() );
			try {
				task.perform( args, appState );
			} catch ( Exception e ) {
				if ( ! task.canContinueInCaseOfError() ) {
					throw new Exception( e.getLocalizedMessage(), e );
				} else {
					logger.warn( e.getLocalizedMessage() );
				}
			}
		}
		
		return appState;
	}
	
}
