package org.funtester.app.util.editor;

/**
 * Displayer
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T>	the object type to be discarded. 
 */
public interface Displayer< T > {
	
	/**
	 * Display an object.
	 * 
	 * @param obj
	 * @param ds
	 * @throws Exception
	 */
	void display(final T obj, final DisplayState ds);

}
