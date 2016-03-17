package org.funtester.app.project.mutation.types;

import org.funtester.app.project.mutation.SpecMutator;
import org.funtester.common.util.rand.LongRandom;
import org.funtester.core.profile.ElementType;
import org.funtester.core.profile.Profile;
import org.funtester.core.software.Element;
import org.funtester.core.software.UseCase;

/**
 * Mutant that changes an element type.
 *
 * @author Thiago Delgado Pinto
 *
 */
public class ChangeElementDateTypeSpecMutator implements SpecMutator {

	private Profile profile;

	public ChangeElementDateTypeSpecMutator(Profile profile) {
		this.profile = profile;
	}

	/** @inheritDoc */
	@Override
	public String mnemonic() {
		return "EM-CT";
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

		return changeElementType( e );
	}


	private boolean changeElementType(Element e) {

		boolean oldTypeIsEditable = e.isEditable();

		for ( ElementType newType : profile.getTypes() ) {
			if ( ! newType.equals( e.getType() )
				&& newType.isEditable() != oldTypeIsEditable ) {
				e.setType( newType );
				return true;
			}
		}
		return false;
	}

}
