package org.funtester.core.process.rule.not_used_yet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.funtester.core.software.Element;
import org.funtester.core.software.ElementBasedVC;
import org.funtester.core.software.MultiVC;
import org.funtester.core.software.QueryBasedVC;
import org.funtester.core.software.RegExBasedVC;
import org.funtester.core.software.SingleVC;
import org.funtester.core.software.ValueConfiguration;

/**
 * Value configuration value extractor
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ValueConfigurationValueExtractor {
	
	private final QueryBasedValueExtractor queryBasedValueExtractor;
	
	public ValueConfigurationValueExtractor(
			QueryBasedValueExtractor queryBasedValueExtractor
			) {
		this.queryBasedValueExtractor = queryBasedValueExtractor;
	}

	
	public List< Object > extractValues(
			final ValueConfiguration vc,
			final Map< Element, Object > valuesFromOtherElements
			) throws ValueExtractionException {
		
		List< Object > values = new ArrayList< Object >();
		if ( null == vc ) { return values; }
		
		switch ( vc.kind() ) {
			case SINGLE: { // Just get the value
				SingleVC svc = (SingleVC) vc;
				values.add( svc.getValue() );
				break;
			}
			case MULTI: { // Just get the values
				MultiVC mvc = (MultiVC) vc;
				values.addAll( mvc.getValues() );
				break;
			}
			case ELEMENT_BASED: {  // Just get the value(s)
				ElementBasedVC evc = (ElementBasedVC) vc;
				Object value = valuesFromOtherElements.get( evc.getReferencedElement() );
				if ( value != null ) {
					if ( value instanceof List ) { // Useful ? Will happen ?
						List< ? > valueList = (List< ? >) value;
						values.addAll( valueList );
					} else {
						values.add( value );
					}
				}
				break;
			}
			case REGEX_BASED: { // Use the regular expression as a value
				RegExBasedVC rvc = (RegExBasedVC) vc;
				if ( rvc.getRegEx() != null ) {
					values.add( rvc.getRegEx().getExpression() );
				}
				break;
			}
			case QUERY_BASED: { // Execute the query with its parameters and get the values
				QueryBasedVC qvc = (QueryBasedVC) vc;
				List< ? > valueList;
				try {
					valueList = queryBasedValueExtractor.extractValues(
						qvc, valuesFromOtherElements );
				} catch (Exception e) {
					throw new ValueExtractionException( e.getLocalizedMessage(), e );
				}
				values.addAll( valueList );
				break;
			}
			default : ; // Will return an empty list
		}
		
		return values;
	}
	
}
