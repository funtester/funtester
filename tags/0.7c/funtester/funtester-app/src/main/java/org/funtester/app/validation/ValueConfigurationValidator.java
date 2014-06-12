package org.funtester.app.validation;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.EnumTranslation;
import org.funtester.common.util.Validator;
import org.funtester.core.software.ElementBasedVC;
import org.funtester.core.software.MultiVC;
import org.funtester.core.software.ParameterConfig;
import org.funtester.core.software.QueryBasedVC;
import org.funtester.core.software.RegExBasedVC;
import org.funtester.core.software.SingleVC;
import org.funtester.core.software.ValueConfiguration;
import org.funtester.core.software.ValueType;
import org.funtester.core.software.ValueTypeBasedConverter;
import org.funtester.core.util.InvalidValueException;

/**
 * Validator for a {@link ValueConfiguration}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ValueConfigurationValidator implements
		Validator< ValueConfiguration > {

	private final ValueType valueType;
	
	public ValueConfigurationValidator(final ValueType valueType) {
		this.valueType = valueType;
	}
	
	@Override
	public void validate(final ValueConfiguration obj) throws Exception {
		switch ( obj.kind() ) {
			case SINGLE			: validateSingleVC( obj ); break;
			case MULTI			: validateMultiVC( obj ); break;
			case ELEMENT_BASED	: validateElementBasedVC( obj ); break;
			case QUERY_BASED	: validateQueryBasedVC( obj ); break;
			case REGEX_BASED	: validateRegExBasedVC( obj ); break;
			default				: ; // Do nothing
		}
	}

	private void validateSingleVC(final ValueConfiguration obj) throws Exception {
		final SingleVC vc = (SingleVC) obj;
		validateValueForType( valueType, vc.getValue() );
	}
	
	private void validateMultiVC(final ValueConfiguration obj) throws Exception {
		final MultiVC vc = (MultiVC) obj;
		for ( Object value : vc.getValues() ) {
			validateValueForType( valueType, value );
		}
	}
	
	private void validateElementBasedVC(ValueConfiguration obj) throws Exception {
		final ElementBasedVC vc = (ElementBasedVC) obj;
		if ( null == vc.getReferencedElement() ) {
			String msg = Messages.alt( "_VALUE_CONFIGURATION_NO_ELEMENT",
					"Please choose the referenced element." );
			throw new InvalidValueException( msg, "referencedElement" );
		}
	}
	
	private void validateQueryBasedVC(final ValueConfiguration obj) throws Exception {
		final QueryBasedVC vc = (QueryBasedVC) obj;
		
		// Query config
		if ( null == vc.getQueryConfig() ) {
			String msg = Messages.alt( "_VALUE_CONFIGURATION_NO_QUERY",
					"Please choose a query." );
			throw new InvalidValueException( msg, "queryConfig" );
		}
		
		// Target column
		if ( null == vc.getTargetColumn() || vc.getTargetColumn().isEmpty() ) {
			String msg = Messages.alt( "_VALUE_CONFIGURATION_NO_TARGET_COLUMN",
					"Please choose the target column." );
			throw new InvalidValueException( msg, "targetColumn" );
		}

		// Parameters
		int i = 1;
		for ( ParameterConfig pc : vc.getParameters() ) {
			if ( null == pc || null == pc.getValueConfiguration() ) {
				String msg = String.format( Messages.alt(
						"_VALUE_CONFIGURATION_PARAMETER_NOT_CONFIGURED",
						"The parameter %d was not configured." ),
						i );
				throw new InvalidValueException( msg, "parameter" );
			}			
			validate( pc.getValueConfiguration() );
			++i;
		}
	}
	
	private void validateRegExBasedVC(ValueConfiguration obj) throws Exception {
		final RegExBasedVC vc = (RegExBasedVC) obj;
		
		if ( null == vc.getRegEx() ) {
			String msg = Messages.alt( "_VALUE_CONFIGURATION_NO_REGEX",
					"Please choose a regular expression." );
			throw new InvalidValueException( msg, "regEx" );
		}
	}
	
	private void validateValueForType(
			final ValueType valueType,
			final Object value
			) throws InvalidValueException {
		Object converted = null;
		
		try {
			converted = ValueTypeBasedConverter.convert( valueType, value );
		} catch (Exception e) {
			String msg = String.format( Messages.alt( "_VALUE_CONFIGURATION_WRONG_TYPE",
					"The value is wrong for its type. Expected: " ),
					EnumTranslation.translationForItem( ValueType.class, valueType ) );
			throw new InvalidValueException( msg, "valueType" );
		}
		
		if ( null == converted ) {
			String msg = Messages.alt( "_VALUE_CONFIGURATION_NULL_VALUE",
					"The business rule value cannot be null." );
			throw new InvalidValueException( msg, "valueType" );
		}
	}

}
