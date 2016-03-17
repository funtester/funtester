package org.funtester.core.process.testing.valid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.funtester.common.Importance;
import org.funtester.common.at.AbstractTestActionStep;
import org.funtester.common.at.AbstractTestElement;
import org.funtester.common.at.AbstractTestMethod;
import org.funtester.common.at.AbstractTestStep;
import org.funtester.core.process.rule.ElementValueGenerator;
import org.funtester.core.process.testing.DefaultTMGS;
import org.funtester.core.process.testing.IdGenerator;
import org.funtester.core.process.testing.TestMethodImportanceCalculator;
import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.profile.StepKind;
import org.funtester.core.software.ActionStep;
import org.funtester.core.software.Element;
import org.funtester.core.software.Scenario;
import org.funtester.core.software.Step;

/**
 * Strategy that generates only one test method and all the scenario elements
 * (fields) receive valid values.
 *
 * @author Thiago Delgado Pinto
 *
 */
public abstract class AllWithValidValuesTMGS extends DefaultTMGS {

	enum EditableElementsUse {
		ALL,
		REQUIRED_ONLY
	}

	public AllWithValidValuesTMGS(
			ElementValueGenerator valueGen,
			IdGenerator idGenerator
			) {
		super( valueGen, idGenerator );
	}

	/// Expects success because all the elements will have valid values
	@Override
	public boolean expectedUseCaseSuccess() {
		return true;
	}

	/**
	 * Generate one test method where all the elements will receive valid
	 * values according to the given option.
	 * <p>
	 * If <code>forTheRequiredElementsOnly</code> is passed, just the
	 * required editable elements will be considered in the generation process.
	 * </p>
	 *
	 * @param scenario	the scenario used to generate the test method.
	 * @param option	the valid value option.
	 * @param EditableElementsUse
	 * 					indicates if use just the required elements or not.
	 * @return			a semantic test method.
	 * @throws Exception
	 */
	protected List< AbstractTestMethod > generateOneMethodWithValidValues(
			final Scenario scenario,
			final ValidValueOption option,
			final EditableElementsUse ElementsUse
			) throws Exception {

		Set< Element > elementsToCalculateImportance = new LinkedHashSet< Element >();

		Map< Element, Object > otherElementValues = new TreeMap< Element, Object >();

		// Creates the semantic method list
		List< AbstractTestMethod > methodList = new ArrayList< AbstractTestMethod >();

		// Creates the semantic method
		AbstractTestMethod method = createSemanticTestMethod( scenario.getName() );
		List< AbstractTestStep > semanticSteps = new ArrayList< AbstractTestStep >();

		// Create the semantic steps
		List< Step > scenarioSteps = scenario.getSteps();
		for ( Step step : scenarioSteps ) {

			final StepKind STEP_KIND = step.kind();

			//
			// Ignore all but action steps (StepKind.ACTION)
			//
			if ( ! STEP_KIND.equals( StepKind.ACTION ) ) {
				getLogger().info( "Ignored " + STEP_KIND + " step with id " + step.getId() );
				continue;
			}

			ActionStep as = (ActionStep) step;
			AbstractTestActionStep semanticActionStep = createSemanticActionStep( as );

			Collection< Element > elements = as.getElements();
			for ( Element element : elements ) {

				if ( null == element ) {
					getLogger().error( "The element should not be null." );
					continue;
				}

				// Creates the semantic element
				AbstractTestElement semanticElement = createSemanticElementWithoutValue( element );

				if ( element.isEditable() ) {

					elementsToCalculateImportance.add( element );

					// Ignore the current element if it is wanted just the
					// required elements and the current element is not required
					if ( ElementsUse.equals( EditableElementsUse.REQUIRED_ONLY )
							&& ! element.isRequired() ) {
						continue;
					}

					// Generate its value
					Object value = generateValidValue(
							option, element, otherElementValues );
					otherElementValues.put( element, value ); // Set the element's value

					// Set the generated value
					semanticElement.setValue( value );
					semanticElement.setValueConsideredValid( true );
					semanticElement.setValueOption( option.toString() );

				} // if editable

				// Add the element to the step
				semanticActionStep.addElement( semanticElement );
			} // for elements
			// Add the step to the semantic step list
			semanticSteps.add( semanticActionStep );
		} // for steps

		// Add the steps to the semantic method
		method.setSteps( semanticSteps );

		// Calculate the importance
		TestMethodImportanceCalculator calculator = new TestMethodImportanceCalculator();
		Importance importance = calculator.calculate( scenario, elementsToCalculateImportance, option );
		// Set the importance
		method.setImportance( importance );

		// Add the semantic method to the list
		methodList.add( method );

		return methodList;
	}

}
