package org.funtester.core.process.msg;

/**
 * Message comparator
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface MessageComparator {

	boolean areClose(final String defined, final String got);
	
}
