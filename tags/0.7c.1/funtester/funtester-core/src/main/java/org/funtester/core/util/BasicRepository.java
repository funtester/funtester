package org.funtester.core.util;

/**
 * Basic repository
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T> type handle by the repository.
 */
public interface BasicRepository< T > {

	T first() throws Exception;
	
	void save(T obj) throws Exception; 
}