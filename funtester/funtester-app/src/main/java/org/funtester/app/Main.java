package org.funtester.app;

import java.awt.EventQueue;

import javax.swing.JOptionPane;

import org.apache.log4j.BasicConfigurator;
import org.funtester.app.project.AppState;
import org.funtester.app.startup.AdjustLocaleTask;
import org.funtester.app.startup.AdjustLookAndFeelTask;
import org.funtester.app.startup.BasicConfigurationTask;
import org.funtester.app.startup.LoadConfigurationTask;
import org.funtester.app.startup.LoadDatabaseDriverTemplateTask;
import org.funtester.app.startup.LoadDatabaseDriversTask;
import org.funtester.app.startup.LoadLocalesTask;
import org.funtester.app.startup.LoadLookAndFeelTask;
import org.funtester.app.startup.LoadPluginsTask;
import org.funtester.app.startup.LoadProfilesTask;
import org.funtester.app.startup.LoadProjectTask;
import org.funtester.app.startup.LoadVocabulariesTask;
import org.funtester.app.startup.StartupManager;
import org.funtester.app.ui.MainFrame;
import org.funtester.app.ui.SplashWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application starter. 
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class Main {
	
	/**
	 * Main
	 * 
	 * @param args	the application's arguments
	 */
	public static void main(final String[] args) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {	
				( new Main() ).start( args );
			}
		};
		// Run in Event Dispatch Thread
		EventQueue.invokeLater( runnable );
	}
	
	/**
	 * Start the application via GUI
	 * 
	 * @param args
	 */
	public void start(final String args[]) {
		
		//System.out.println( System.getProperties() );
		
		//
		// Start the logger (It is better keeping this code here because it
		// runs *before* calling other classes that uses the logger).
		//
		BasicConfigurator.configure();
		Logger logger = LoggerFactory.getLogger( Main.class );

		//
		// Create the splash window
		//
		final SplashWindow splash = new SplashWindow(); // show
		
		//
		// Start the application with the startup manager, using the splash
		// as a listener
		//
		StartupManager startupManager = new StartupManager();
		startupManager.addListener( splash );
		
		//
		// Add tasks in the right order (important)
		//
		startupManager.addTask( new BasicConfigurationTask( splash ) );
		startupManager.addTask( new LoadLocalesTask() );
		startupManager.addTask( new LoadConfigurationTask() );
		startupManager.addTask( new AdjustLocaleTask() );
		startupManager.addTask( new LoadProfilesTask() );
		startupManager.addTask( new LoadVocabulariesTask() );
		startupManager.addTask( new LoadDatabaseDriverTemplateTask() );
		startupManager.addTask( new LoadDatabaseDriversTask() );
		startupManager.addTask( new LoadPluginsTask() );
		startupManager.addTask( new LoadLookAndFeelTask() );
		startupManager.addTask( new AdjustLookAndFeelTask() );
		startupManager.addTask( new LoadProjectTask() );
		
		//
		// Start the manager
		//
		AppState appState;
		try {
			appState = startupManager.start( args );
		} catch ( Exception e ) {
		
			logger.error( e.getLocalizedMessage(), e );
			
			// Close the splash then show the error message
			splash.setVisible( false );
			
			JOptionPane.showMessageDialog( splash, e.getLocalizedMessage(),
					"FunTester", JOptionPane.ERROR_MESSAGE );

			return;
		} finally {
			startupManager.clearListeners();
			splash.finish();
		}
		
		//
		// Show the main frame
		//
		MainFrame main = new MainFrame( appState );
		main.setVisible( true );
	}
}
