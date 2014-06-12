package org.funtester.core.process.testing.invalid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.funtester.common.Importance;
import org.funtester.common.at.AbstractTestActionStep;
import org.funtester.common.at.AbstractTestElement;
import org.funtester.common.at.AbstractTestMethod;
import org.funtester.common.at.AbstractTestOracleStep;
import org.funtester.common.at.AbstractTestStep;
import org.funtester.common.util.StringUtil;
import org.funtester.core.process.rule.ElementValueGenerator;
import org.funtester.core.process.testing.IdGenerator;
import org.funtester.core.process.testing.TestMethodImportanceCalculator;
import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.profile.StepKind;
import org.funtester.core.software.ActionStep;
import org.funtester.core.software.BusinessRuleType;
import org.funtester.core.software.Element;
import org.funtester.core.software.OracleStep;
import org.funtester.core.software.Scenario;
import org.funtester.core.software.Step;

/**
 * Strategy that extends {@link AllExceptOneTMGS} and selects only the required
 * elements (fields) except one.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class AllRequiredsExceptOneTMGS  extends AllExceptOneTMGS {

	public AllRequiredsExceptOneTMGS(
			ElementValueGenerator valueGen,
			IdGenerator idGenerator
			) {
		super( valueGen, idGenerator );
	}
	
	public String getTestMethodBaseName() {
		return "all_the_required_fields_except_%s";
	}
	
	public List< AbstractTestMethod > generateTestMethods(
			final Scenario scenario
			) throws Exception {

		//
		// IDEA: Generate one method for each editable element.
		//		 All the elements but the chosen one will receive a valid value.
		//	
		
		ValidValueOption validOption = ValidValueOption.RANDOM_INSIDE_RANGE;
		
		List< AbstractTestMethod > methodList = new ArrayList< AbstractTestMethod >();
		
		Set< Element > elements = elementsFromScenarioUseCase( scenario );
		Set< Element > requiredOnes = justTheRequiredElements( elements );

		for ( Element e : requiredOnes ) {		
			AbstractTestMethod method = generateMethod(
					scenario, e, validOption );	
			methodList.add( method );
		}
		
		return methodList;	
	}


	/**
	 * Generate a test method (based on a scenario) where the
	 * <code>elementToBeIgnored</code> will not be included in the test
	 * and all the other elements will receive a valid value.	
	 * 
	 * @param scenario				the scenario used to generate the steps.
	 * @param elementToBeIgnored	the element to be ignored by the test. 
	 * @param validValueOption		the valid option used for all the elements.
	 * @return						the semantic method.
	 * @throws Exception
	 */
	private AbstractTestMethod generateMethod(
			final Scenario scenario,
			final Element elementToBeIgnored,
			final ValidValueOption validValueOption
			) throws Exception {
		
		Set< Element > elementsToCalculateImportance = new LinkedHashSet< Element >();
		
		Map< Element, Object > otherElementValues = new TreeMap< Element, Object >();
		
		AbstractTestMethod method = createSemanticTestMethod(
				scenario.getName(), elementToBeIgnored.getInternalName() );
		
		List< AbstractTestStep > semanticSteps = new ArrayList< AbstractTestStep >();
		
		Map< Element, String > oracleMessageMap = new HashMap< Element, String >();
		
		List< Step > scenarioSteps = scenario.getSteps();
		for ( Step step : scenarioSteps ) {
			
			final StepKind STEP_KIND = step.kind();
			
			//
			// Ignore use case call steps and documentation steps
			//
			if ( STEP_KIND.equals( StepKind.USE_CASE_CALL )
				|| STEP_KIND.equals( StepKind.DOC ) ) {
				getLogger().info( "Ignored " + STEP_KIND + " step with id " + step.getId() );
				continue;
			}
			
			if ( STEP_KIND.equals( StepKind.ORACLE ) ) {
				
				// Just use a oracle for the scenario use case. The other use cases
				// will be executed correctly, so they don't need a oracle.
				final boolean stepUseCaseIsTheSameAsTheScenarioUseCase =
					step.getFlow().getUseCase().equals( scenario.getUseCase() );
				
				if ( ! stepUseCaseIsTheSameAsTheScenarioUseCase ) {
					continue; // Continue to next step
				}
				
				List< String > messages = extractMessagesFromMappedElements(
						oracleMessageMap );
				final boolean emptyMessages = ( StringUtil.sumLength( messages.toArray( new String[ 0 ] ) ) < 1 );
				if ( emptyMessages ) {
					getLogger().debug( "Oracle ignored for having empty messages" );
					continue; // DO NOT GENERATE A ORACLE WITHOUT A MESSAGE
				}
				
				AbstractTestOracleStep semanticOracleStep = createSemanticOracleStep(
						(OracleStep) step );
				semanticOracleStep.setMessages( messages );
				
				// Add the step to the semantic step list
				semanticSteps.add( semanticOracleStep );
				
				continue; // Continue to next step
			}
			
			ActionStep as = (ActionStep) step; 
			AbstractTestActionStep semanticActionStep = createSemanticActionStep( as );

			Collection< Element > actionElements = as.getElements();
			for ( Element element : actionElements ) {
				
				if ( null == element ) {
					getLogger().error( "The element should not be null." );
					continue;
				}
				
				AbstractTestElement semanticElement = createSemanticElementWithoutValue( element );

				if ( element.isEditable() ) {
					
					// Add to calculate the importance
					elementsToCalculateImportance.add( element ); // The required element participates too
					
					// Ignore if not required
					if ( ! element.isRequired() ) {
						continue;
					}
					
					// If required, ignore if is the target element
					if ( element.equals( elementToBeIgnored ) ){
						// Set the oracle message in the map
						String message = element.businessRuleWithType( BusinessRuleType.REQUIRED ).getMessage();
						oracleMessageMap.put( element, message );							
						continue; 
					}					
										
					final Object value = generateValidValue(
							validValueOption, element, otherElementValues );					
					final String optionAsString = validValueOption.toString();
					
					otherElementValues.put( element, value ); // Set the element's value
					getLogger().debug( "@@@@ Element is " + element.toString() +
							" and value is " + value.toString() );
					
					// Set the generated value
					semanticElement.setValue( value );
					semanticElement.setValueConsideredValid( true );	
					semanticElement.setValueOption( optionAsString );
				} // if editable
				
				// Add the element to the step
				semanticActionStep.addElement( semanticElement );
			} // for elements
			
			// Add the step to the semantic step list
			semanticSteps.add( semanticActionStep );
		} // for steps
		
		method.setSteps( semanticSteps );
		
		// Calculate the importance
		TestMethodImportanceCalculator calculator = new TestMethodImportanceCalculator();
		Importance importance = calculator.calculate( scenario, elementsToCalculateImportance, validValueOption );
		// Set the importance
		method.setImportance( importance );
		
		return method;
	}
	
	/**
	 * Return only the required elements.
	 * 
	 * @param Elements	the editable elements to analyze.
	 * @return					a set with the required elements.
	 */
	private Set< Element > justTheRequiredElements(
			final Set< Element > Elements) {
		Set< Element > requiredSet = new LinkedHashSet< Element >();
		for ( Element ee : Elements ) {
			if ( ee.isRequired() ) {
				requiredSet.add( ee );
			}
		}
		return requiredSet;
	}

}
