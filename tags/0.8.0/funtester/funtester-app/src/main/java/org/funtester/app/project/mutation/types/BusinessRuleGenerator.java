package org.funtester.app.project.mutation.types;

import org.funtester.common.util.rand.LongRandom;
import org.funtester.core.software.BusinessRule;
import org.funtester.core.software.BusinessRuleType;
import org.funtester.core.software.MultiVC;
import org.funtester.core.software.RegEx;
import org.funtester.core.software.RegExBasedVC;
import org.funtester.core.software.SingleVC;
import org.funtester.core.software.ValueConfiguration;

/**
 * A simple business rule generator.
 *
 * @author Thiago Delgado Pinto
 *
 */
public final class BusinessRuleGenerator {

	public BusinessRule generateBusinessRuleWith(
			BusinessRuleType type,
			long id
			) {

		final LongRandom rand = new LongRandom();

		BusinessRule br = new BusinessRule();
		br.setType( type );
		br.setMessage( "MUTANT MESSAGE! RANDOM ID:" + rand.after( 0L ).toString()  );
		br.setId( id );

		ValueConfiguration vc = null;
		switch ( type ) {

			case EQUAL_TO: ; // next
			case MIN_LENGTH: ; // next
			case MAX_LENGTH: ; // next
			case MIN_VALUE: ; // next
			case MAX_VALUE: {
				vc = new SingleVC( rand.after( 0L ).toString() );
				break;
			}

			case NOT_ONE_OF: ; // next
			case ONE_OF: {
				MultiVC mvc = new MultiVC();
				mvc.addValue( rand.after( 0L ).toString() );
				mvc.addValue( rand.after( 0L ).toString() );
				mvc.addValue( rand.after( 0L ).toString() );
				vc = mvc;
				break;
			}

			case REG_EX: {
				RegExBasedVC rvc = new RegExBasedVC();
				rvc.setRegEx( new RegEx( "A mutant generated regex", "/[A-Z]{2,10}/" ) );
				vc = rvc;
				break;
			}

			default: // i.e. REQUIRED has no value configuration
				break;
		}

		br.setValueConfiguration( vc );

		return br;
	}

}
