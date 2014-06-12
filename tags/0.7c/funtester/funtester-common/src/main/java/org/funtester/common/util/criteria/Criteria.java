package org.funtester.common.util.criteria;

/**
 * Criteria
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T> the object type to use in the criteria.
 */
public interface Criteria< T > {

	/**
	 * Return {@code true} whether it matches the criteria.
	 * @param obj
	 * @return
	 */
	boolean matches(final T obj);
	
}
