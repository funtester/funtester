package org.funtester.common.util;

import java.util.EventListener;

/**
 * A generic selection listener.
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <AnnouncedType> the announced type.
 */
public interface SelectionListener< AnnouncedType > extends EventListener {
	
	/**
	 * Indicates that a object has been selected.
	 * 
	 * @param obj	Selected object.
	 */
	void selected(AnnouncedType obj);

}
