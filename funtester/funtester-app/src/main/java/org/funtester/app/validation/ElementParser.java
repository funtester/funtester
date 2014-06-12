package org.funtester.app.validation;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.funtester.common.util.ItemsParser;
import org.funtester.core.software.Element;
import org.funtester.core.software.UseCase;

/**
 * Simple element parser.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ElementParser {
	
	public static final String SEPARATOR = ",";

	/**
	 * Parse elements from a text, adding them to a collection.
	 * 
	 * @param text				the text to be parsed.
	 * @param useCase			the use case to check each element. 
	 * @param parsedElements	all the parsed elements.
	 * @param createdElements	just the created elements.
	 */
	public static void parseElementsTo(
			final String text,
			final UseCase useCase,
			Collection< Element > parsedElements,
			Collection< Element > createdElements
			) {
		parsedElements.clear();
		createdElements.clear();
		
		// Use a LinkedHashSet to keep the user defined order
		final Set< String > items = new LinkedHashSet< String >();
		ItemsParser.addFromParse( text, SEPARATOR, items );
		// Find the each item text in the use case. If it does't exists, then
		// create an element with the text.
		for ( String s : items ) {
			final String elementName = s.trim();
			// Find the element in the use case
			Element ie = useCase.elementWithName( elementName );
			if ( null == ie ) { // Not exists, create
				ie = new Element( elementName );
				createdElements.add( ie );
			}
			parsedElements.add( ie );
		}
	}	

}
