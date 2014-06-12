package org.funtester.core.process.value;

/**
 * Default value generator
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T> the generator value type
 */
public abstract class DefaultValueGenerator< T >
	implements ValueGenerator< T > {

	public abstract boolean hasAvailableValuesOutOfTheRange();	
		
	public T validValue(ValidValueOption option) {
		if ( ValidValueOption.ZERO == option ) {			
			return zero();
		} else if ( ValidValueOption.MIN == option ) {
			return min();
		} else if ( ValidValueOption.MAX == option ) {
			return max();
		} else if ( ValidValueOption.MEDIAN == option ) {
			return hasAnyValueBetween( min(), max() ) ? middle() : min(); 
		} else if ( ValidValueOption.RIGHT_AFTER_MIN == option ) {			
			return hasNext( min() ) ? next( min() ) : min();
		} else if ( ValidValueOption.RIGHT_BEFORE_MAX == option ) {
			return hasPrior( max() ) ? prior( max() ) : max();
		} else if ( ValidValueOption.RANDOM_INSIDE_RANGE == option ) {
			return hasAnyValueBetween( min(), max() ) ? randomBetween( min(), max() ) : min();
		}
		return null;
	}

	public T invalidValue(InvalidValueOption option) {
		if ( ! hasAvailableValuesOutOfTheRange() ) {
			return null;
		}
		if ( InvalidValueOption.RIGHT_BEFORE_MIN == option ) {
			return prior( min() );
		} else if ( InvalidValueOption.RIGHT_AFTER_MAX == option ) {
			return next( max() );
		} else if ( InvalidValueOption.RANDOM_BEFORE_MIN == option ) {
			return randomBefore( min() );
		} else if ( InvalidValueOption.RANDOM_AFTER_MAX == option ) {
			return randomAfter( max() );
		}
		return null;
	}	
	
	/**
	 * Returns the minimum value.
	 * @return
	 */
	public abstract T min();
	
	/**
	 * Returns the maximum value.
	 * @return
	 */	
	public abstract T max();

	/**
	 * Returns the zero value, or the minimum as zero if it is not available.
	 * @return
	 */
	protected abstract T zero();
	
	/**
	 * Returns the middle value.
	 * @return
	 */
	protected abstract T middle();
	
	/**
	 * Returns true if a value is before the maximum.
	 * 
	 * @param value	the value to be verified.
	 * @return
	 */
	protected abstract boolean hasNext(T value);
	
	/**
	 * Returns a value after the supplied value.
	 * 
	 * @param value	the current value.
	 * @return
	 */
	protected abstract T next(T value);
	
	/**
	 * Returns true if a value is after the minimum.
	 * 
	 * @param value	the value to be verified.
	 * @return
	 */	
	protected abstract boolean hasPrior(T value);
	
	/**
	 * Returns a value before the supplied value.
	 * 
	 * @param value
	 * @return
	 */	
	protected abstract T prior(T value);
	
	/**
	 * Returns a random value before the supplied value.
	 * 
	 * @param value
	 * @return
	 */
	protected abstract T randomBefore(T value);
	
	/**
	 * Returns a random value after the supplied value.
	 * 
	 * @param value
	 * @return
	 */	
	protected abstract T randomAfter(T value);
	
	/**
	 * Returns true if there is a value between a minimum and a maximum value.
	 *  
	 * @param min	the minimum value.
	 * @param max	the maximum value.
	 * @return
	 */
	protected abstract boolean hasAnyValueBetween(T min, T max);
	
	/**
	 * Returns a value between a minimum and a maximum value.
	 * 
	 * @param min	the minimum value.
	 * @param max	the maximum value.
	 * @return
	 */
	protected abstract T randomBetween(T min, T max);
}