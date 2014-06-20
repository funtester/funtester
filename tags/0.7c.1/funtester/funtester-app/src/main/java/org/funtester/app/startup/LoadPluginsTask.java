package org.funtester.app.startup;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.funtester.app.common.DefaultFileExtensions;
import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppConfiguration;
import org.funtester.app.project.AppState;
import org.funtester.app.project.PluginInfo;
import org.funtester.app.repository.PluginDetailsRepository;
import org.funtester.app.repository.json.JsonPluginDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task for loading the plug-ins.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class LoadPluginsTask implements Task {
	
	private static final Logger logger = LoggerFactory.getLogger( LoadPluginsTask.class ); 
	private static final String FILE_PATTERN = ".*(." + DefaultFileExtensions.PLUGIN + ")";

	@Override
	public String description() {
		return Messages.alt( "_PLUGIN_LOADING", "Loading plug-ins..." );
	}

	@Override
	public boolean canContinueInCaseOfError() {
		return true;
	}

	@Override
	public void perform(String[] args, AppState appState)
			throws Exception {
		
		// Put the plug-ins on the parameters
		
		Map< PluginInfo, String > pluginMap = new HashMap< PluginInfo, String >();
		appState.setPluginMap( pluginMap );

		// Get the plug-in directory from the application configuration
		
		AppConfiguration cfg = appState.getConfiguration();
		if ( null == cfg ) {
			final String msg = Messages.alt( "_CONFIGURATION_READING_ERROR", "Error while getting the configuration." );
			throw new Exception( msg );
		}
		
		String dirPath = cfg.getPluginDirectory();
		if ( null == dirPath ) {
			dirPath = AppConfiguration.DEFAULT.getPluginDirectory();
		}
		
		final File dir = new File( dirPath );
		if ( ! dir.isDirectory() ) {
			throw new Exception( "Not a directory: " + dir.getAbsolutePath() );
		}

		// Load the plug-ins
		
		for ( File f : dir.listFiles() ) {
			
			final String fileName = f.getName();
			if ( ! fileName.matches( FILE_PATTERN ) ) { continue; }
			
			final String pluginPath = dirPath.endsWith( File.separator ) ?
					dirPath + fileName : dirPath + File.separator + fileName;
			
			logger.debug( "Detected plugin path: " + pluginPath );
			
			try {
				PluginDetailsRepository repo = new JsonPluginDetailsRepository( pluginPath );
				PluginInfo plugin = repo.first();
				pluginMap.put( plugin, fileName );
			} catch (Exception e) {
				// Just log the error if it could not load the plugin
				logger.error( e.getLocalizedMessage() );
			}			
		}
	}

}
