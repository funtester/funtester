package org.funtester.app.util.editor;

/**
 * Savior
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T>	the object type to be saved. 
 */
public interface Savior< T > {
	
	/**
	 * Saves an object.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	void save(T obj) throws Exception;

}
