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
import org.funtester.core.process.testing.OracleMessageChooser;
import org.funtester.core.process.testing.TestMethodImportanceCalculator;
import org.funtester.core.process.value.InvalidValueOption;
import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.profile.StepKind;
import org.funtester.core.software.ActionStep;
import org.funtester.core.software.Element;
import org.funtester.core.software.OracleStep;
import org.funtester.core.software.Scenario;
import org.funtester.core.software.Step;

/**
 * Strategy that extends {@link AllExceptOneTMGS} and allow generating
 * one abstract test method for each editable element of the target use case.
 * Each method verifies the element business rule.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public abstract class AllExceptOneWithValidValuesTMGS extends AllExceptOneTMGS {
			
	public AllExceptOneWithValidValuesTMGS(
			ElementValueGenerator valueGen,
			IdGenerator idGenerator
			) {
		super( valueGen, idGenerator );
	}

	/**
	 * Generate methods for just the editable elements of scenario's use case.
	 * <p>
	 * It is very important to generate methods with for only the editable
	 * elements that belongs to the scenario's use case. If we generate
	 * invalid values for elements from other use cases, the test execution
	 * will not succeed because the target use case will be not achieve in
	 * execution time.
	 * </p>
	 * 
	 * @param scenario
	 * @param otherElementsOption
	 * @param chosenElementOption
	 * @return
	 * @throws Exception
	 */
	protected List< AbstractTestMethod > generateMethodsForJustTheEditableElementsOfTheTargetUseCase(
			final Scenario scenario,
			final ValidValueOption otherElementsOption,
			final InvalidValueOption chosenElementOption
			) throws Exception {
		
		//
		// IDEA: Generate one method for each editable element.
		//		 All the elements but the chosen one will receive a valid value.
		//	
		
		List< AbstractTestMethod > methodList = new ArrayList< AbstractTestMethod >();
		
		Set< Element > elements = elementsFromScenarioUseCase( scenario );

		for ( Element e : elements ) {
			AbstractTestMethod method = generateMethod(
					scenario, e, chosenElementOption, otherElementsOption );
			methodList.add( method );
		}
		
		return methodList;		
	}

	/**
	 * Generate a test method (based on a scenario) where the
	 * {@code elementToHaveAnInvalidValue} will receive an invalid value
	 * defined by the {@code invalidValueOption} and the other elements will
	 * receive a valid value defined by the {@code validOptionForOtherElements}. 
	 * 
	 * @param scenario						the scenario used to generate the steps.
	 * @param elementToHaveAnInvalidValue	the element to be an invalid value. 
	 * @param invalidValueOption			the invalid option used to the target invalid element.
	 * @param validOptionForOtherElements	the valid option used for the other elements.
	 * @return								the semantic method.
	 * @throws Exception
	 */
	protected AbstractTestMethod generateMethod(
			final Scenario scenario,
			final Element elementToHaveAnInvalidValue,
			final InvalidValueOption invalidValueOption,
			final ValidValueOption validOptionForOtherElements
			) throws Exception {
		
		Set< Element > elementsToCalculateImportance = new LinkedHashSet< Element >();
		
		Map< Element, Object > otherElementValues = new TreeMap< Element, Object >();
		
		AbstractTestMethod method = createSemanticTestMethod(
				scenario.getName(), elementToHaveAnInvalidValue.getInternalName() );
		
		List< AbstractTestStep > semanticSteps = new ArrayList< AbstractTestStep >();
		
		OracleMessageChooser messageChooser = new OracleMessageChooser();
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
			
			
			if ( step.kind().equals( StepKind.ORACLE ) ) {
				
				// Just use an oracle for the scenario's use case. The other
				// use cases will be executed correctly, so they don't need
				// an oracle.
				final boolean stepUseCaseIsTheSameAsTheScenarioUseCase =
					step.getFlow().getUseCase().equals( scenario.getUseCase() );
				
				if ( ! stepUseCaseIsTheSameAsTheScenarioUseCase ) {
					continue; // Continue to next step
				}
					
				List< String > messages = extractMessagesFromMappedElements(
						oracleMessageMap );
				final boolean emptyMessages = ( StringUtil.sumLength( messages.toArray( new String[ 0 ] ) ) < 1 );
				if ( emptyMessages ) {
					getLogger().warn( "Oracle ignored for having empty messages" );
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
			
			//
			// Generate values for the editable elements
			//
			
			Collection< Element > elements = as.getElements();
			for ( Element element : elements ) {
				if ( null == element ) {
					getLogger().error( "Element should not be null." );
					continue;
				}
				
				AbstractTestElement semanticElement = createSemanticElementWithoutValue( element );
				
				if ( element.isEditable() ) {
					
					// Add to calculate the importance
					elementsToCalculateImportance.add( element );

					Object value;
					String optionAsString;
					
					///
					/// A invalid value will be use if (and only if) the
					/// current element is equal to the target element
					/// and the element's use case is the same as the scenario.
					///
					/// IMPORTANT:				
					/// It's very important to NOT generate invalid values for
					/// steps from use cases that executes BEFORE the target
					/// use case because it will not be achieved by the test.
					///
					boolean useInvalidValue = element.equals( elementToHaveAnInvalidValue );
					if ( element.getUseCase() != null && useInvalidValue ) {
						useInvalidValue = element.getUseCase().equals( scenario.getUseCase() );
					}
					
					getLogger().debug( "Use invalid value: " + useInvalidValue );
					
					if ( useInvalidValue ){
						// Set the oracle message in the map
						String message = messageChooser.choose( element, invalidValueOption );
						oracleMessageMap.put( element, message );				
						
						// Generates a invalid value
						value = generateInvalidValue(
								invalidValueOption, element, otherElementValues );
						optionAsString = invalidValueOption.toString();
						
					} else { // Generate a valid value
						value = generateValidValue(
							validOptionForOtherElements, element, otherElementValues );
						optionAsString = validOptionForOtherElements.toString();
					}
					
					otherElementValues.put( element, value ); // Set the element's value
					getLogger().debug( "@@@@ Element is " + element.toString() );
					getLogger().debug( " and value is " + ( ( value != null ) ? value.toString() : "null" ) );
					
					// Set the generated value
					semanticElement.setValue( value );
					semanticElement.setValueConsideredValid( ! useInvalidValue );	
					semanticElement.setValueOption( optionAsString );
				} // if editable
				
				// Add the semantic element to the semantic step
				semanticActionStep.addElement( semanticElement );
			} // for elements
			
			// Add the step to the semantic step list
			semanticSteps.add( semanticActionStep );
		} // for steps
		
		method.setSteps( semanticSteps );
		
		// Calculate the importance
		TestMethodImportanceCalculator calculator = new TestMethodImportanceCalculator();
		Importance importance = calculator.calculate( scenario, elementsToCalculateImportance, invalidValueOption );
		// Set the importance
		method.setImportance( importance );
		
		return method;
	}
	
}
