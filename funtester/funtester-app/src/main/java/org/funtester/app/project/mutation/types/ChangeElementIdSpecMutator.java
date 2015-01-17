package org.funtester.app.project.mutation.types;

import org.funtester.app.project.mutation.SpecMutator;
import org.funtester.common.util.rand.LongRandom;
import org.funtester.core.software.Element;
import org.funtester.core.software.UseCase;

/**
 * Mutant that changes an element identification.
 *
 * @author Thiago Delgado Pinto
 *
 */
public class ChangeElementIdSpecMutator implements SpecMutator {

	/** @inheritDoc */
	@Override
	public String mnemonic() {
		return "EM-CI";
	}

	/** @inheritDoc */
	@Override
	public boolean mutate(UseCase useCase) {

		// No elements -> exit
		int numberOfElements = useCase.getElements().size();
		if ( numberOfElements < 1 ) { return false; }

		// Choose a random element
		LongRandom rand = new LongRandom();
		int index = rand.between( 0L, numberOfElements - 1L ).intValue();
		Element e = useCase.elementAt( index );

		// Change the internal name
		String newInternalName = e.getInternalName()
				+ "_" + rand.between( 1000L, 99999L ).toString();
		e.setInternalName( newInternalName );

		return true;
	}

}
