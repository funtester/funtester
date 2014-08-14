package org.funtester.common.util;

/**
 * Indicates that something can copy a certain type. 
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T>	The target type (usually the own class/interface)
 */
public interface Copier< T > {

	/**
	 * Copies the values from another object and return itself (this) updated.
	 *  
	 * @param that Object to be copied.
	 * @return The current object updated.
	 */
	T copy(final T that);

	/**
	 * Make a new copy of itself.
	 * Usually returns <code>new T().copy( this );</code>
	 * @return
	 */
	T newCopy();
}
