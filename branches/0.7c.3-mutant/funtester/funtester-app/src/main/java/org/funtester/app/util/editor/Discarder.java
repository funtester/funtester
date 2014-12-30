package org.funtester.app.util.editor;

/**
 * Discarder
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T>	the object type to be discarded. 
 */
public interface Discarder< T > {
	
	/**
	 * Discard an object.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	void discard(T obj) throws Exception;

}
