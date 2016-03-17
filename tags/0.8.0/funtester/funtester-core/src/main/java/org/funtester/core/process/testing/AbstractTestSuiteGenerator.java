package org.funtester.core.process.testing;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.funtester.common.at.AbstractTestCase;
import org.funtester.common.at.AbstractTestSuite;
import org.funtester.common.util.StringUtil;
import org.funtester.core.process.ResultSetCache;
import org.funtester.core.process.rule.ConnectionCache;
import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.process.rule.ElementValueGenerator;
import org.funtester.core.process.testing.invalid.AllRequiredsExceptOneTMGS;
import org.funtester.core.process.testing.invalid.InvalidFormatTMGS;
import org.funtester.core.process.testing.invalid.JustAboveUpperLimitTMGS;
import org.funtester.core.process.testing.invalid.JustBelowLowerLimitTMGS;
import org.funtester.core.process.testing.invalid.RandomAboveUpperLimitTMGS;
import org.funtester.core.process.testing.invalid.RandomBelowLowerLimitTMGS;
import org.funtester.core.process.testing.valid.JustAboveLowerLimitTMGS;
import org.funtester.core.process.testing.valid.JustBelowUpperLimitTMGS;
import org.funtester.core.process.testing.valid.LowerLimitTMGS;
import org.funtester.core.process.testing.valid.MedianTMGS;
import org.funtester.core.process.testing.valid.RandomTMGS;
import org.funtester.core.process.testing.valid.RequiredTMGS;
import org.funtester.core.process.testing.valid.UpperLimitTMGS;
import org.funtester.core.process.testing.valid.ZeroTMGS;
import org.funtester.core.software.IncludeFile;
import org.funtester.core.software.Scenario;
import org.funtester.core.software.UseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generate semantic test suites.
 *  
 * @author Thiago Delgado Pinto
 *
 */
public class AbstractTestSuiteGenerator {
	
	private static Logger logger = LoggerFactory.getLogger( AbstractTestSuiteGenerator.class );
	public static final String TEST_CASE_SUFFIX = "_Test";
	
	private final AbstractTestDatabaseScriptListConverter dbScriptConverter;
	private final ElementValueGenerator elementValueGenerator;
	private final IdGenerator idGenerator;
	private final List< TestMethodGenerationStrategy > generationStrategies; 
	
	/**
	 * Create the generator with the default strategies.
	 */
	public AbstractTestSuiteGenerator(DriverCache driverCache) {
		dbScriptConverter = new AbstractTestDatabaseScriptListConverter();
		ConnectionCache connectionCache = new ConnectionCache();
		ResultSetCache resultSetCache = new ResultSetCache();
		
		elementValueGenerator = new ElementValueGenerator(
				driverCache,
				connectionCache,
				resultSetCache
				);
		
		idGenerator = new AtomicIdGenerator( 0 );
		generationStrategies = new ArrayList< TestMethodGenerationStrategy >();
		
		addDefaultGenerationStrategies(
			generationStrategies, elementValueGenerator, idGenerator );
	}
	
	public ElementValueGenerator getElementValueGenerator() {
		return elementValueGenerator;
	}
	
	public IdGenerator getIdGenerator() {
		return idGenerator;
	}
	
	public List< TestMethodGenerationStrategy > getGenerationStrategies() {
		return generationStrategies;
	}
	
	/**
	 * Add default generation strategies to the given list.
	 * 
	 * @param l				the list of generation strategies where the
	 * 						strategies will be added.
	 * @param valueGen		the element value generator.
	 * @param idGenerator	the id generator.
	 */
	private void addDefaultGenerationStrategies(
			List< TestMethodGenerationStrategy > l,
			final ElementValueGenerator valueGen,
			final IdGenerator idGenerator
			) {
		 
		// Valid
		l.add( new LowerLimitTMGS( valueGen, idGenerator ) );
		l.add( new JustAboveLowerLimitTMGS( valueGen, idGenerator ) );
		l.add( new UpperLimitTMGS( valueGen, idGenerator ) );
		l.add( new JustBelowUpperLimitTMGS( valueGen, idGenerator ) );
		l.add( new MedianTMGS( valueGen, idGenerator ) );
		l.add( new ZeroTMGS( valueGen, idGenerator ) );
		l.add( new RandomTMGS( valueGen, idGenerator ) );
		l.add( new RequiredTMGS( valueGen, idGenerator ) );
		
		// Invalid
		l.add( new JustBelowLowerLimitTMGS( valueGen, idGenerator ) );
		l.add( new RandomBelowLowerLimitTMGS( valueGen, idGenerator ) );
		l.add( new JustAboveUpperLimitTMGS( valueGen, idGenerator ) );
		l.add( new RandomAboveUpperLimitTMGS( valueGen, idGenerator ) );
		l.add( new AllRequiredsExceptOneTMGS( valueGen, idGenerator ) );
		l.add( new InvalidFormatTMGS( valueGen, idGenerator ) );
	}
	
	
	/**
	 * Generate a {@code SemanticTestSuite} and all its test methods, from
	 * a map of scenarios.
	 * 
	 * @param softwareName	the software name for the generated test suite. 
	 * @param suiteName		the name for the generated test suite.
	 * @param scenariosMap	the scenarios used to generate the test methods.
	 * @return				the generated test suite.
	 */
	public AbstractTestSuite generateTestSuite(
			final String softwareName,
			final String suiteName,
			final Map< UseCase, List< Scenario > > scenariosMap
			) {
		
		AbstractTestSuite suite = new AbstractTestSuite( softwareName, suiteName );
		
		logger.debug( "Number of strategies: " + generationStrategies.size() );
			
		Set< Entry< UseCase, List< Scenario > > > entries = scenariosMap.entrySet();
		for ( Entry< UseCase, List< Scenario > > e : entries ) {	
			
			UseCase useCase = e.getKey();
			List< Scenario > scenarios  = e.getValue();

			if ( useCase.getIgnoreToGenerateTests() ) {
				continue; // Ignore for semantic test generation
			}
			
			
			// TEST GENERATION ---------------------------------------------
							
			logger.info( "Use case " + useCase.getName() + " with " + scenarios.size() + " scenarios." );
			for ( Scenario scenario : scenarios ) {
				
				final String useCaseName = useCase.getName();
				final String scenarioName = scenario.getName();
				
				AbstractTestCase testCase = new AbstractTestCase();
				testCase.setName( makeTestCaseName( useCaseName, scenarioName ) );
				testCase.setUseCaseName( useCaseName );
				testCase.setScenarioName( scenarioName );
				//testCase.setIncludeFiles( extractIncludeFiles( useCase.allNeededIncludeFiles() ) );
				testCase.setIncludeFiles( extractIncludeFilesContent( scenario.getIncludeFiles() ) );
				testCase.setScripts( dbScriptConverter.fromDatabaseScripts( useCase.getDatabaseScripts() ) );
				
				for ( TestMethodGenerationStrategy st : generationStrategies ) {
					logger.debug( "Current strategy is " + st.getDescription() );
					try {
						testCase.addAllTestMethods( st.generateTestMethods( scenario ) );
					} catch ( Exception e1 ) {
						logger.error( e1.getLocalizedMessage() );
						e1.printStackTrace();
						continue; // Ignore the strategy
					}
				} // for
				
				suite.add( testCase );
				logger.debug( "Test case SIZE is " + testCase.getTestMethods().size() );
			} // for scenario
			
			// -------------------------------------------------------------
		} // for use case	
		
		return suite;
	}


	private Set< String > extractIncludeFilesContent(Set< IncludeFile > includeFiles) {
		Set< String > content = new LinkedHashSet< String >();
		for ( IncludeFile f : includeFiles ) {
			content.add( f.getName() );
		}
		return content;
	}


	private String makeTestCaseName(String useCaseName, String scenarioName) {
		return StringUtil.buildNameForCode( useCaseName ) + "_"
			+ StringUtil.removeAllInvalidCharactersForCodeName(
					scenarioName.toUpperCase().replaceAll( "\\,", "\\_" ) )
			+ TEST_CASE_SUFFIX
			;
	}	
}
