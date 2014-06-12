package org.funtester.core.process.testing.invalid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.funtester.core.process.rule.ElementValueGenerator;
import org.funtester.core.process.testing.DefaultTMGS;
import org.funtester.core.process.testing.IdGenerator;
import org.funtester.core.profile.StepKind;
import org.funtester.core.software.ActionStep;
import org.funtester.core.software.Element;
import org.funtester.core.software.OracleStep;
import org.funtester.core.software.Scenario;
import org.funtester.core.software.Step;

/**
 * Strategy for generating valid values for all the elements (fields)
 * except one.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public abstract class AllExceptOneTMGS extends DefaultTMGS {
	
	public AllExceptOneTMGS(
			ElementValueGenerator valueGen,
			IdGenerator idGenerator
			) {
		super( valueGen, idGenerator );
	}
	
	// It is not expected success because one element will not respect the
	// defined business rules
	@Override
	public boolean expectedUseCaseSuccess() {
		return false;
	}
	
	
	/**
	 * Extract the mapped messages to a string list.
	 * 
	 * @param oracleMessageMap	the map with the elements' messages.
	 * @return					a string list with the messages.
	 */
	protected List< String > extractMessagesFromMappedElements(
			final Map< Element, String > oracleMessageMap
			) {
		List< String > messages = new ArrayList< String >();
		Set< Entry< Element, String > > entries = oracleMessageMap.entrySet();
		for ( Entry< Element, String > e : entries ) {
			messages.add( e.getValue() );
		}		
		return messages;
	}

	/**
	 * Return only the editable elements that belongs to the scenario's use
	 * case.
	 * 
	 * @param scenario	the scenario to be analyzed.
	 * @return			a set of editable elements.
	 */
	protected Set< Element > elementsFromScenarioUseCase(
			final Scenario scenario
			) {
		Set< Element > set = new LinkedHashSet< Element >();
		for ( Step step : scenario.getSteps() ) {
			if ( ! step.isPerformable() ) {
				continue;
			}		
			// Ignore if the use case is different
			if ( ! step.useCase().equals( scenario.getUseCase() ) ) {
				continue;
			}
			
			final Collection< Element > elements;
			if ( step.kind() == StepKind.ACTION ) {
				elements = ((ActionStep) step ).getElements();
			} else if ( step.kind() == StepKind.ORACLE ) {
				elements = ((OracleStep) step ).getElements();
			} else {
				continue;
			}
			
			for ( Element e : elements ) {
				if ( null == e ) {
					getLogger().error( "Element should not be null." );
					continue;
				}
				if ( e.isEditable() ) {
					if ( set.contains( e ) ) {
						continue;
					}
					set.add( e );
				}
			}
		}
		return set;
	}	

}
