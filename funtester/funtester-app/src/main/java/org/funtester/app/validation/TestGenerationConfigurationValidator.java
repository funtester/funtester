package org.funtester.app.validation;

import java.io.File;

import org.funtester.app.i18n.Messages;
import org.funtester.common.generation.TestGenerationConfiguration;
import org.funtester.common.util.Validator;
import org.funtester.core.util.InvalidValueException;

/**
 * Test generation configuration validator
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TestGenerationConfigurationValidator implements
		Validator< TestGenerationConfiguration > {

	@Override
	public void validate(final TestGenerationConfiguration obj) throws Exception {
	
		/* ignored
		if ( null == obj.getSoftwareFile()
				|| obj.getSoftwareFile().isEmpty() ) {
			String msg = "Please inform the software file."; // TODO i18n
			throw new InvalidValueException( msg, "softwareFile" );
		}
		*/
		
		if ( null == obj.getSemanticTestFile()
				|| obj.getSemanticTestFile().isEmpty() ) {
			String msg = Messages.alt( "_TGC_INFORM_ABSTRACT_TEST_FILE",
					"Please inform the abstract test file." );
			throw new InvalidValueException( msg, "semanticTestFile" );
		}
		
		if ( obj.getGenerateSemanticTests() ) { // New abstract test, the directory should exists
			
			File f = new File( obj.getSemanticTestFile() );
			String dirPath = f.getParent();
			if ( dirPath != null ) {
				File dir = new File( dirPath );
				if ( ! dir.exists() ) {
					String msg = Messages.alt( "_TGC_ABSTRACT_TEST_FILE_DIR_SHOULD_EXIST",
							"The directory of the abstract test file should exists." );
					throw new InvalidValueException( msg, "semanticTestFile" );	
				}
			}
			
		} else { // The file should exists
			File f = new File( obj.getSemanticTestFile() );
			if ( ! f.exists() ) {
				String msg = Messages.alt( "_TGC_ABSTRACT_TEST_FILE_SHOULD_EXIST",
						"The abstract test file should exists." );
				throw new InvalidValueException( msg, "semanticTestFile" );	
			}
		}
		
		//
		// Plugin
		//
		
		if ( null == obj.getPluginId()
				|| obj.getPluginId().isEmpty() ) {
			String msg = Messages.alt( "_TGC_INFORM_PLUGIN",
					"Please inform the plugin." );
			throw new InvalidValueException( msg, "plugin" );
		}
		
		if ( null == obj.getTestingFramework()
				|| obj.getTestingFramework().isEmpty() ) {
			String msg = Messages.alt( "_TGC_INFORM_TESTING_FRAMEWORK",
					"Please inform the testing framework." );
			throw new InvalidValueException( msg, "testingFramework" );
		}
		
		//
		// Code generation
		//
		
		if ( obj.getGenerateCode() ) {
			
			if ( null == obj.getOutputDirectory()
					|| obj.getOutputDirectory().isEmpty() ) {
				String msg = Messages.alt( "_TGC_INFORM_OUTPUT_DIR",
						"Please inform the output directory." );
				throw new InvalidValueException( msg, "outputDirectory" );
			} else {
				File outputDir = new File( obj.getOutputDirectory() ); 
				if ( ! outputDir.isDirectory() || ! outputDir.exists() ) {
					String msg = Messages.alt( "_TGC_OUTPUT_DIR_SHOULD_EXIST",
							"The output directory should be an existing directory." );
					throw new InvalidValueException( msg, "outputDirectory" );
				}
			}
	
			// The main class is optional !
			/*
			if ( null == obj.getMainClass()
					|| obj.getMainClass().isEmpty() ) {
				String msg = "Please inform the main class."; // TODO i18n
				throw new InvalidValueException( msg, "mainClass" );
			}
			*/
			
			// The package is optional !
			 /*
			if ( null == obj.getPackageName()
					|| obj.getPackageName().isEmpty() ) {
				String msg = "Please inform the package name."; // TODO i18n
				throw new InvalidValueException( msg, "packageName" );
			}
			
			*/
			
			if ( obj.getTimeoutInMS() < 0 ) {
				String msg = Messages.alt( "_TGC_INFORM_POSITIVE_TIMEOUT",
						"Please inform a positive timeout." );
				throw new InvalidValueException( msg, "timeoutInMS" );
			}
		
		}
		
		//
		// Test run
		//
		
		if ( obj.getRun() ) {

			// The commands to run are optional
			/*
			if ( null == obj.getCommandsToRun()
					|| obj.getCommandsToRun().isEmpty() ) {
				String msg = "Please inform the commands to run."; // TODO i18n
				throw new InvalidValueException( msg, "commandsToRun" );
			}
			*/
			
			if ( null == obj.getOriginalResultsFile()
					|| obj.getOriginalResultsFile().isEmpty() ) {
				String msg = Messages.alt( "_TGC_INFORM_ORIGINAL_RESULTS_FILE",
						"Please inform the original results file." );
				throw new InvalidValueException( msg, "originalResultsFile" );
			}
			
			if ( null == obj.getConvertedResultsFile()
					|| obj.getConvertedResultsFile().isEmpty() ) {
				String msg = Messages.alt( "_TGC_INFORM_CONVERTED_RESULTS_FILE",
						"Please inform the converted results file." );
				throw new InvalidValueException( msg, "convertedResultsFile" );
			}
		}
	}

}
