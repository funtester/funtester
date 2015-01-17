package org.funtester.app.project.mutation.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.funtester.app.project.mutation.SpecMutator;
import org.funtester.common.util.rand.LongRandom;
import org.funtester.core.software.BusinessRule;
import org.funtester.core.software.BusinessRuleType;
import org.funtester.core.software.Element;
import org.funtester.core.software.Software;
import org.funtester.core.software.UseCase;

/**
 * Mutant that add a business rule.
 *
 * @author Thiago Delgado Pinto
 *
 */
public class AddBusinessRuleSpecMutator implements SpecMutator {

	private final Software software;

	public AddBusinessRuleSpecMutator(Software s) {
		this.software = s;
	}

	/** @inheritDoc */
	@Override
	public String mnemonic() {
		return "BRM-RM";
	}

	/** @inheritDoc */
	@Override
	public boolean mutate(UseCase useCase) {

		// Choose the editable elements that also have business rules
		List< Element > elements = useCase.getElements();
		List< Element > editableOnes = new ArrayList< Element >();
		for ( Element e : elements ) {
			if ( e.isEditable() && ! e.getBusinessRules().isEmpty() ) {
				editableOnes.add( e );
			}
		}

		// No elements -> exit
		int numberOfEditableElements = editableOnes.size();
		if ( numberOfEditableElements < 1 ) { return false; }

		// Choose a random element
		final LongRandom rand = new LongRandom();
		int eIndex = rand.between( 0L, numberOfEditableElements - 1L ).intValue();
		Element e = editableOnes.get( eIndex );
		if ( null == e ) { return false; }

		// Select the unused business rule types
		List< BusinessRuleType > unusedTypes = Arrays.asList( BusinessRuleType.values() );
		for ( BusinessRule br : e.getBusinessRules() ) {
			unusedTypes.remove( br.getType() );
		}

		// No remaining types -> exit
		int numberOfUnusedTypes = unusedTypes.size();
		if ( numberOfUnusedTypes < 1 ) { return false; }

		// Select an unused type randomly
		int typeIndex = rand.between( 0L, numberOfUnusedTypes - 1L ).intValue();
		BusinessRuleType newType = BusinessRuleType.values()[ typeIndex ];

		// Create a business rule
		BusinessRuleGenerator brGen = new BusinessRuleGenerator();
		BusinessRule newBR = brGen.generateBusinessRuleWith( newType,
				software.generateIdFor( BusinessRule.class.getSimpleName() ) );

		// Add the business rule
		e.addBusinessRule( newBR );

		return true;
	}

}
