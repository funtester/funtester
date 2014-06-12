package org.funtester.core.process.testing;

import java.util.ArrayList;
import java.util.Collection;

import org.funtester.common.Importance;
import org.funtester.core.process.rule.InvalidValueOptionInfluence;
import org.funtester.core.process.rule.ValidValueOptionInfluence;
import org.funtester.core.process.value.InvalidValueOption;
import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.software.BusinessRule;
import org.funtester.core.software.Element;
import org.funtester.core.software.ImportanceCalculator;
import org.funtester.core.software.Scenario;

/**
 * Calculate the {@link Importance} for a test method.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TestMethodImportanceCalculator {

	public static final float SCENARIO_IMPORTANCE_WEIGHT = 2.5f;
	public static final float BUSINESS_RULE_IMPORTANCE_WEIGHT = 1.5f;
	public static final float IMPORTANCE_DENOMINATOR = 4.0f;

	
	/**
	 * Calculate the importance of a test method based on its scenario and
	 * the business rule influenced by the given {@link ValidValueOption}.
	 * 
	 * @param scenario	the scenario of the test method.
	 * @param elements	the elements involved in the test.
	 * @param option	the option
	 * @return			an {@link Importance} value.
	 */
	public Importance calculate(
			final Scenario scenario,
			final Collection< Element > elements,
			final ValidValueOption option
			) {
		ValidValueOptionInfluence influence = new ValidValueOptionInfluence();
		
		Collection< Importance > values = new ArrayList< Importance >();
		for ( Element e : elements ) {
			for ( BusinessRule br : e.getBusinessRules() ) {
				//
				// Consider just the influenced business rules
				//
				if ( influence.of( option ).contains( br.getType() ) ) {
					values.add( br.getImportance() );
				}
			}
		}
		
		return calculateFor( scenario, values );
	}
	
	
	/**
	 * Calculate the importance of a test method based on its scenario and
	 * the business rule influenced by the given {@link InvalidValueOption}.
	 * 
	 * @param scenario	the scenario of the test method.
	 * @param elements	the elements involved in the test.
	 * @param option	the option
	 * @return			an {@link Importance} value.
	 */
	public Importance calculate(
			final Scenario scenario,
			final Collection< Element > elements,
			final InvalidValueOption option
			) {
		InvalidValueOptionInfluence influence = new InvalidValueOptionInfluence();
		
		Collection< Importance > values = new ArrayList< Importance >();
		for ( Element e : elements ) {
			for ( BusinessRule br : e.getBusinessRules() ) {
				//
				// Consider just the influenced business rules
				//
				if ( influence.of( option ).contains( br.getType() ) ) {
					values.add( br.getImportance() );
				}
			}
		}
		
		return calculateFor( scenario, values );
	}

	
	
	/**
	 * Calculate the importance of a test method based on its scenario and
	 * the average of the given importance values.
	 * 
	 * @param scenario
	 * @param values
	 * @return
	 */
	private Importance calculateFor(
			final Scenario scenario,
			Collection< Importance > values
			) {
		if ( values.isEmpty() ) {
			return scenario.importance();
		}
		
		final int scenarioImportance = ImportanceCalculator.ordinalOf( scenario.importance() );
		final float scenarioBias = scenarioImportance * SCENARIO_IMPORTANCE_WEIGHT;
		
		final int brImportance = ImportanceCalculator.ordinalOf(
				ImportanceCalculator.averageImportance( values ) );
		final float brBias = brImportance * BUSINESS_RULE_IMPORTANCE_WEIGHT;
		
		final int result = Math.round( ( scenarioBias + brBias ) / IMPORTANCE_DENOMINATOR );
		
		return Importance.values()[ result - 1 ];
	}

}
