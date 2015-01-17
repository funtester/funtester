package org.funtester.app.project.mutation.types;

import java.util.ArrayList;
import java.util.List;

import org.funtester.app.project.mutation.SpecMutator;
import org.funtester.common.util.rand.LongRandom;
import org.funtester.core.software.BusinessRule;
import org.funtester.core.software.Element;
import org.funtester.core.software.MultiVC;
import org.funtester.core.software.SingleVC;
import org.funtester.core.software.UseCase;
import org.funtester.core.software.ValueConfiguration;
import org.funtester.core.software.ValueConfigurationKind;

/**
 * Mutant that change a business rule' value.
 *
 * @author Thiago Delgado Pinto
 *
 */
public class ChangeBusinessRuleValueSpecMutator implements SpecMutator {

	private static final long SMALL_NUMBER = -999999999l;

	private final LongRandom rand = new LongRandom();

	/** @inheritDoc */
	@Override
	public String mnemonic() {
		return "BRM-CV";
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
		int eIndex = rand.between( 0L, numberOfEditableElements - 1L ).intValue();
		Element e = editableOnes.get( eIndex );
		if ( null == e ) { return false; }

		// No business rules -> exit
		int numberOfBusinessRules = e.getBusinessRules().size();
		if ( numberOfBusinessRules < 1 ) {
			return false;
		}

		// Select a business rule randomly
		int brIndex = rand.between( 0L, numberOfBusinessRules - 1L ).intValue();
		BusinessRule br = e.getBusinessRules().get( brIndex );

		changeBusinessRuleValue( br );

		return true;
	}


	private void changeBusinessRuleValue(BusinessRule br) {

		ValueConfiguration vc = br.getValueConfiguration();
		if ( hasEligibleValueConfiguration( vc ) ) {
			br.setMessage( "A MUTANT MESSAGE. RANDOM: " + rand.after( 0L ).toString() );
			return;
		}

		ValueConfigurationKind kind = vc.kind();
		switch ( kind ) {

			case SINGLE: {
				SingleVC svc = (SingleVC) vc;
				Object oldValue = svc.getValue();
				Object newValue;
				do {
					newValue = rand.after( SMALL_NUMBER );
				} while ( newValue.equals( oldValue ) );

				svc.setValue( newValue );
				break;
			}

			case MULTI: {
				MultiVC mvc = (MultiVC) vc;
				int size = mvc.getValues().size();

				// No values -> add one -> exit
				if ( size < 1 ) {
					mvc.addValue( rand.after( SMALL_NUMBER ) );
					break;
				}

				// Remove a random value
				int index = rand.between( 0L, size - 1L ).intValue();
				mvc.remove( index );

				// Select a not existing value
				Object newValue;
				do {
					newValue = rand.after( SMALL_NUMBER );
				} while ( mvc.getValues().contains( newValue ) );

				mvc.addValue( newValue );

				break;
			}

			default:
				break;
		}
	}

	// eligible
	private boolean hasEligibleValueConfiguration(ValueConfiguration vc) {
		return null == vc
			|| ( vc != null
				&& ( vc.kind().equals( ValueConfigurationKind.QUERY_BASED )
					|| vc.kind().equals( ValueConfigurationKind.ELEMENT_BASED )
					|| vc.kind().equals( ValueConfigurationKind.REGEX_BASED ) ) )
			;
	}

}
