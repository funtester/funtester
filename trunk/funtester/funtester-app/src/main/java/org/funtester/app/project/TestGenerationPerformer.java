package org.funtester.app.project;

import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.exec.PumpStreamHandler;
import org.funtester.app.common.DefaultFileExtensions;
import org.funtester.app.repository.AbstractTestSuiteRepository;
import org.funtester.app.repository.json.JsonAbstractTestSuiteRepository;
import org.funtester.app.repository.json.JsonTestGenerationConfigurationRepository;
import org.funtester.common.at.AbstractTestSuite;
import org.funtester.common.generation.TestGenerationConfiguration;
import org.funtester.common.generation.TestGenerationConfigurationRepository;
import org.funtester.common.util.CommandRunner;
import org.funtester.common.util.FilePathUtil;
import org.funtester.core.process.scenario.SoftwareScenariosGenerator;
import org.funtester.core.process.testing.AbstractTestSuiteGenerator;
import org.funtester.core.software.Scenario;
import org.funtester.core.software.Software;
import org.funtester.core.software.UseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test generation performer
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TestGenerationPerformer {
	
	private static final Logger logger = LoggerFactory.getLogger( TestGenerationPerformer.class );
	
	private final AppState appState;
	
	public TestGenerationPerformer(final AppState appState) {
		this.appState = appState;
	}

	/**
	 * Perform the given {@link TestGenerationConfiguration}. 
	 * 
	 * @param cfg		the {@link TestGenerationConfiguration}.
	 * @param outStream	the {@link OutputStream} for the plug-in execution.
	 * 
	 * @throws TestGenerationException
	 */
	public void performConfiguration(
			final TestGenerationConfiguration cfg,
			final OutputStream outStream
			)
			throws TestGenerationException {
		
		//
		// Generate semantic tests
		//
		
		if ( cfg.getGenerateSemanticTests() ) {
			
			logger.debug( "Generating abstract tests (" + cfg.getSemanticTestFile() + ")..."  );
			
			try {
				generateSemanticTests( cfg );
			} catch ( Exception e ) {
				throw new TestGenerationException( e.getLocalizedMessage() );
			}
		}
		
		if ( ! cfg.getGenerateCode() && ! cfg.getRun() ) {
			return;
		}

		//
		// Search for the selected plug-in
		//
		
		final PluginInfo plugin = appState.pluginWithId( cfg.getPluginId() );
		if ( null == plugin ) {
			String msg = "Plugin id not found: " + cfg.getPluginId(); // TODO i18n
			throw new TestGenerationException( msg );
		}

		//
		// Create/overwrite the configuration file
		//
		
		logger.debug( "Creating a configuration file for the plug-in..." );
		
		String projectFileName = appState.getProjectFileName().replace( "\\", "/" );
		logger.debug( "Project file name is: "  + projectFileName );
		
		final String configurationFile = FilePathUtil.changeFileExtension(
				projectFileName, DefaultFileExtensions.PLUGIN_CONFIGURATION );
		
		TestGenerationConfigurationRepository configurationRepository =
				new JsonTestGenerationConfigurationRepository( configurationFile );
		
		try {
			// Save the configuration file to use it as a parameter to the
			// plug-in command
			configurationRepository.save( cfg );
			
		} catch ( Exception ex ) {
			throw new TestGenerationException( ex.getLocalizedMessage() );
		}
		
		//
		// Calling the plug-in
		//
		
		logger.debug( "Calling the plug-in..." );
		
		callPlugin( outStream, cfg, plugin, configurationFile );
	}
	
	
	
	private void generateSemanticTests(final TestGenerationConfiguration cfg) throws Exception {
		
		AbstractTestSuite testSuite = generateTestSuite( cfg );
		
		final String semanticTestFilePath = cfg.getSemanticTestFile();
		
		AbstractTestSuiteRepository testSuiteRepo =
				new JsonAbstractTestSuiteRepository( semanticTestFilePath );
		
		testSuiteRepo.save( testSuite );
	}
	
	
	
	private AbstractTestSuite generateTestSuite(
			final TestGenerationConfiguration cfg
			) {
		// Should come from the user ----------------------------------
		
		// Do not get from the user for now {
		int maxFlowRepetitions = 1;
		final String suiteName = appState.getConfiguration().getDefaultTestSuiteName();
		// }
		
		// ------------------------------------------------------------
		
		final Software software = appState.getProject().getSoftware();
		
		//
		// 1. Generate all the scenarios for the software
		//
		
		logger.debug( ">>> GENERATING ALL THE SCENARIOS..." );
		
		Map< UseCase, Integer > useCaseToMaxRepetitionsMap = new LinkedHashMap< UseCase, Integer >();
		for ( UseCase uc : software.getUseCases() ) {
			useCaseToMaxRepetitionsMap.put( uc, maxFlowRepetitions );
		}
		
		SoftwareScenariosGenerator scenarioGen = new SoftwareScenariosGenerator();
		
		Map< UseCase, List< Scenario > > scenariosMap = scenarioGen.generate(
				useCaseToMaxRepetitionsMap,
				maxFlowRepetitions
				);
		
		//
		// 2. Convert them to semantic tests
		//
		
		logger.debug( ">>> CONVERTING INTO SEMANTIC TESTS..." );
		
		AbstractTestSuiteGenerator testSuiteGen =
				new AbstractTestSuiteGenerator( appState.getDriverCache() );
		
		final AbstractTestSuite testSuite = testSuiteGen.generateTestSuite(
			software.getName(), suiteName, scenariosMap );
		
		return testSuite;
	}
	
	
	/**
	 * Call the plug-in. The console output goes to an {@link OutputStream}.
	 *  
	 * @param outStream				the stream for the console output.
	 * @param cfg					the generation and execution configuration.
	 * @param plugin				the plug-in for generating the tests.
	 * @param configurationFile		the configuration file to save and pass as
	 * 								an argument to the plug-in.
	 */
	private void callPlugin(
			final OutputStream outStream,
			final TestGenerationConfiguration cfg,
			final PluginInfo plugin,
			final String configurationFile
			) {
		
		try {
			PumpStreamHandler streamHandler = new PumpStreamHandler( outStream );
			
			CommandRunner runner = new CommandRunner();
			final String commandWithoutCfgFile = plugin.getCommandToRun();
			
			final String configurationFileParam = "\\{(cfg)\\}";
			final String command = commandWithoutCfgFile.toLowerCase().replaceFirst(
					configurationFileParam, configurationFile );
			
			logger.debug( "Command is: " + command );
			
			final int exitValue = runner.runAndWait( command, streamHandler );
			if ( exitValue != 0 ) { // Not successful
				return;
			}
		} catch ( Exception ex ) {
			throw new TestGenerationException( ex.getLocalizedMessage() );
		}		
	}
}
