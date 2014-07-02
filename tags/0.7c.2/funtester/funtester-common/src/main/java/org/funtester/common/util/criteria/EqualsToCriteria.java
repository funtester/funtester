package org.funtester.common.util.criteria;

/**
 * Equals to criteria.
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T>
 */
public class EqualsToCriteria< T > implements Criteria< T > {
	
	private final T aObj; 
	
	public EqualsToCriteria(final T aObj) {
		this.aObj = aObj;
	}

	@Override
	public boolean matches(T obj) {
		return obj.equals( aObj );
	}

}
