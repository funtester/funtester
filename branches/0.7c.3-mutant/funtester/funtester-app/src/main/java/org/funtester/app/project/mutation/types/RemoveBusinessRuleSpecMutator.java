package org.funtester.app.project.mutation.types;

import java.util.ArrayList;
import java.util.List;

import org.funtester.app.project.mutation.SpecMutator;
import org.funtester.common.util.rand.LongRandom;
import org.funtester.core.software.Element;
import org.funtester.core.software.UseCase;

/**
 * Mutant that removes a business rule.
 *
 * @author Thiago Delgado Pinto
 *
 */
public class RemoveBusinessRuleSpecMutator implements SpecMutator {

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
		int eSize = editableOnes.size();
		if ( eSize < 1 ) { return false; }

		// Choose a random element
		LongRandom rand = new LongRandom();
		int eIndex = rand.between( 0L, eSize - 1L ).intValue();
		Element e = editableOnes.get( eIndex );
		if ( null == e ) { return false; }

		int brSize = e.getBusinessRules().size();
		if ( brSize < 1 ) { return false; }

		// Choose a random business rule
		int brIndex = rand.between( 0L, brSize - 1L ).intValue();

		// Remove the business rule
		e.getBusinessRules().remove( brIndex );

		return true;
	}

}
