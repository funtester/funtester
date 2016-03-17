package org.funtester.app.project.mutation.types;

import org.funtester.app.project.mutation.SpecMutator;
import org.funtester.common.util.rand.LongRandom;
import org.funtester.core.software.Element;
import org.funtester.core.software.UseCase;
import org.funtester.core.software.ValueType;

/**
 * Mutant that changes an element value type.
 *
 * @author Thiago Delgado Pinto
 *
 */
public class ChangeElementValueTypeSpecMutator implements SpecMutator {

	/** @inheritDoc */
	@Override
	public String mnemonic() {
		return "EM-CD";
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

		changeElementValueType( e );
		return true;
	}


	private void changeElementValueType(Element e) {

		int size = ValueType.values().length;
		int index = e.getValueType().ordinal();

		// Choose a random element
		LongRandom rand = new LongRandom();
		int newIndex;
		do {
			newIndex = rand.between( 0L, size - 1L ).intValue();
		} while ( newIndex == index );

		// Change the value type
		ValueType newValueType = ValueType.values()[ newIndex ];
		e.setValueType( newValueType );
	}

}
