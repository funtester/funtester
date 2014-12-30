package org.funtester.core.process.msg;

/**
 * Formatted message comparator
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class FormattedMessageComparator implements MessageComparator {
	
	public boolean areClose(final String defined, final String got) {
		final String DELIMITER = "|"; 
		String text = defined;
		//System.out.println("Text BEFORE: " + text);
		text = text.replaceAll( "%d", DELIMITER );
		text = text.replaceAll( "%f", DELIMITER );
		text = text.replaceAll( "%s", DELIMITER );
		//System.out.println("Text  AFTER: " + text);		
		int index, start = 0, end;		
		do {
			index = text.indexOf( DELIMITER, start );
			end = ( index < 0 ) ? text.length() - 1 : index; 			
			String sub = text.substring( start, end );
			//System.out.println( "Sub: " + sub);
			if ( ! text.toLowerCase().contains( sub.toLowerCase() ) )  {
				return false;
			}			
			start = index + 1;
		} while ( index >= 0 );
		return true;	
	}

}
