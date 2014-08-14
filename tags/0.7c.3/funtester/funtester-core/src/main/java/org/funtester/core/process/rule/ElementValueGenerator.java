package org.funtester.core.process.rule;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.funtester.common.util.ConnectionFactory;
import org.funtester.common.util.rand.LongRandom;
import org.funtester.core.process.ResultSetCache;
import org.funtester.core.process.value.DateTimeValueGenerator;
import org.funtester.core.process.value.DateValueGenerator;
import org.funtester.core.process.value.DoubleValueGenerator;
import org.funtester.core.process.value.InvalidValueOption;
import org.funtester.core.process.value.LongValueGenerator;
import org.funtester.core.process.value.StringValueGenerator;
import org.funtester.core.process.value.TimeValueGenerator;
import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.process.value.ValueGenerator;
import org.funtester.core.process.value.XegerRegExValueGenerator;
import org.funtester.core.software.BusinessRule;
import org.funtester.core.software.BusinessRuleType;
import org.funtester.core.software.DatabaseConfig;
import org.funtester.core.software.Element;
import org.funtester.core.software.ElementBasedVC;
import org.funtester.core.software.MultiVC;
import org.funtester.core.software.ParameterConfig;
import org.funtester.core.software.QueryBasedVC;
import org.funtester.core.software.QueryConfig;
import org.funtester.core.software.RegEx;
import org.funtester.core.software.RegExBasedVC;
import org.funtester.core.software.SingleVC;
import org.funtester.core.software.ValueConfiguration;
import org.funtester.core.software.ValueType;
import org.funtester.core.util.DriverUnavailableException;
import org.funtester.core.util.convertion.ObjectConverter;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * BusinessRuleType vs ValueConfiguration compatibility:
 * 
 * 					    +-----------+-----------+------------+------------+----------+--------+------------+--------+
 * 					    | MIN_VALUE | MAX_VALUE | MIN_LENGTH | MAX_LENGTH | EQUAL_TO | ONE_OF | NOT_ONE_OF | REG_EX |
 * ---------------------+-----------+-----------+------------+------------+----------+--------+------------+--------+
 * ElementBasedVC		|     X           X           X            X           X 
 * MultiVC				|                                                                X          X
 * SingleVC				|     X           X           X            X                              
 * RegExBasedVC			|                                                                                      X
 * QueryBasedVC			|     X           X           X            X                     X          X
 * ---------------------+-----------+-----------+------------+------------+----------+--------+-------------+-------+
 * 
 * BusinessRuleType vs InvalidValueOption influence:
 * 
 * 					    +-----------+-----------+------------+------------+----------+--------+------------+--------+
 * 						| MIN_VALUE | MAX_VALUE | MIN_LENGTH | MAX_LENGTH | EQUAL_TO | ONE_OF | NOT_ONE_OF | REG_EX |
 * ---------------------+-----------+-----------+------------+------------+----------+--------+------------+--------+
 * RIGHT_BEFORE_MIN		|     X                       X                        X         X          X
 * RANDOM_BEFORE_MIN	|     X                       X                        X         X          X
 * RIGHT_AFTER_MAX		|                 X                        X           X         X          X                   
 * RANDOM_AFTER_MAX		|                 X                        X           X         X          X
 * INVALID_FORMAT		|                                                                                      X
 * ---------------------+-----------+-----------+------------+------------+----------+--------+------------+--------+
 * 
 * Notes:
 * 
 * - RIGHT_BEFORE_MIN returns null if min is ValueGenerator's MIN_VALUE.
 * - RANDOM_BEFORE_MIN returns null if min is ValueGenerator's MIN_VALUE.
 * - RIGHT_AFTER_MAX returns null if max is ValueGenerator's MAX_VALUE.
 * - RANDOM_AFTER_MAX returns null if max is ValueGenerator's MAX_VALUE.
 * - INVALID_FORMAT returns null if no REG_EX configuration is defined.
 * - If EQUAL_TO is defined, just return a value not contained in the list.
 * - If ONE_OF is defined, just return a value not contained in the list.
 * - If NOT_ONE_OF is defined, just return a value contained in the list.
 *   
 *
 * BusinessRuleType vs ValidValueOption influence:
 * 
 * 					    +-----------+-----------+------------+------------+----------+--------+------------+--------+
 * 						| MIN_VALUE | MAX_VALUE | MIN_LENGTH | MAX_LENGTH | EQUAL_TO | ONE_OF | NOT_ONE_OF | REG_EX |
 * ---------------------+-----------+-----------+------------+------------+----------+--------+------------+--------+
 * MIN					|     X                       X                        X         X           X
 * MAX					|                 X                        X           X         X           X
 * ZERO					|     X           X           X                        X         X           X
 * MEDIAN				|     X           X           X            X           X         X           X
 * RIGHT_AFTER_MIN		|     X                       X                        X         X           X
 * RIGHT_BEFORE_MAX		|                 X                        X           X         X           X
 * RANDOM_INSIDE_RANGE	|     X           X           X            X           X         X           X
 * ---------------------+-----------+-----------+------------+------------+----------+--------+------------+--------+
 * 
 * Notes:
 * 
 * - MIN_VALUE assumes the ValueGenerator's MIN_VALUE when not defined.
 * - MAX_VALUE assumes the ValueGenerator's MAX_VALUE when not defined.
 * - ZERO assumes the ValueGenerator's MIN_VALUE when it doesn't exists in the
 *   range.
 * 
 * - REG_EX with another option: it is ignored (for valid values).
 * 
 * - MIN_VALUE, MIN_VALUE, MIN_LENGTH or MAX_LENGTH
 *   used with EQUAL_TO, ONE_OF or NOT_ONE_OF: the value need to be verified
 *   against the defined value(s).
 * 
 */

/**
 * Generates value for an editable element.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ElementValueGenerator { // TODO refactor this class
	
	private final Logger logger = LoggerFactory.getLogger( ElementValueGenerator.class );
	
	public static final int MAX_TRIES_WITH_RANDOM_VALUE = 50;
	
	private final DriverCache driverCache;
	private final ConnectionCache connectionCache;
	private final ResultSetCache resultSetCache;
	
	public ElementValueGenerator(
			DriverCache driverCache,
			ConnectionCache connectionCache,
			ResultSetCache resultSetCache
			) {
		this.driverCache = driverCache;
		this.connectionCache = connectionCache;
		this.resultSetCache = resultSetCache;
	}
	
	
	/**
	 * Generates a valid value according to the business rules.
	 * 
	 * @param option				The valid value option.
	 * @param ee					The element with the rules.
	 * @param otherElementValues	The other elements' values.	
	 * @return						A valid value or null if there are
	 * 								conflicting rules.
	 * @throws Exception
	 */
	public Object generateValidValue( // TODO refactor this method (too long) 
			final ValidValueOption option,
			final Element ee,
			final Map< Element, Object > otherElementValues
			) throws Exception {
		
		
		BusinessRule equalToBR = ee.businessRuleWithType( BusinessRuleType.EQUAL_TO );
		BusinessRule oneOfBR = ee.businessRuleWithType( BusinessRuleType.ONE_OF );
		BusinessRule notOneOfBR = ee.businessRuleWithType( BusinessRuleType.NOT_ONE_OF );
		BusinessRule regExBR = ee.businessRuleWithType( BusinessRuleType.REG_EX );
		BusinessRule minValueBR = ee.businessRuleWithType( BusinessRuleType.MIN_VALUE );
		BusinessRule maxValueBR = ee.businessRuleWithType( BusinessRuleType.MAX_VALUE );
		BusinessRule minLengthBR = ee.businessRuleWithType( BusinessRuleType.MIN_LENGTH );
		BusinessRule maxLengthBR = ee.businessRuleWithType( BusinessRuleType.MAX_LENGTH );
		
		// Get regex expression if defined
		
		Object regExExpression = ( regExBR != null ) ? firstConfigurationValue(
				regExBR.getValueConfiguration(), otherElementValues ) : null;				
		String expression = ( regExExpression != null ) ? regExExpression.toString() : null;
		
		// Get minValue and maxValue if defined
		
		Object minValue = ( minValueBR != null ) ? firstConfigurationValue(
				minValueBR.getValueConfiguration(), otherElementValues ) : null;
		
		Object maxValue = ( maxValueBR != null ) ? firstConfigurationValue(
				maxValueBR.getValueConfiguration(), otherElementValues ) : null;
		
		// Get minLength and maxLength if defined
		
		Integer minLength = ( minLengthBR != null )
			? ObjectConverter.toInt( firstConfigurationValue( minLengthBR.getValueConfiguration(), otherElementValues ) )
			: null;
		
		Integer maxLength = ( maxLengthBR != null )
			? ObjectConverter.toInt( firstConfigurationValue( maxLengthBR.getValueConfiguration(), otherElementValues ) )
			: null;					
		
		// 
		// If there is a EQUAL_TO, ONE_OF or NOT_ONE_OF rule, check the values
		// against other compatible rules. Only the values passing these rules
		// will be considered when choosing the value to return.
		//
		// IMPORTANT: Both EQUAL_TO, ONE_OF and NOT_ONE_OF are NOT automatically
		// generated, but defined through a query, manually or got from other
		// element. Of course, this other element's value can be automatically
		// generated, but it is generated before current element's generation.
		//		
		if ( equalToBR != null || oneOfBR != null ) {
			List< Object > values;
			if ( equalToBR != null ) {
				values = configurationValues(			
					equalToBR.getValueConfiguration(), otherElementValues );
			} else {
				values = configurationValues(			
					oneOfBR.getValueConfiguration(), otherElementValues );
			}
			
			if ( values != null ) {
								
				List< Object > consideredValid = new ArrayList< Object >();
							
				for ( Object value : values ) {
					// REGEX				
					if ( expression != null ) {											
						// If the current value does not match the expression...
						if ( ! value.toString().matches( expression ) ) {
							continue; // ...tries the next value.
						}						
					}		
					
					// MIN_VALUE, MAX_VALUE
					if ( minValueBR != null || maxValueBR != null ) {										
						if ( ! isBetween( ee.getValueType(), value, minValue, maxValue ) ) {
							continue; // Try the next value
						}					
					}
					// MIN_LENGTH, MAX_LENGTH
					if ( minLengthBR != null || maxLengthBR != null ) {										
						int length = value.toString().length();
						if ( ! isInLengthRange( length, minLength, maxLength ) ) {
							continue; // Try the next value
						}
					}
					
					// Passed all the rules, so it's considered valid				
					consideredValid.add( value );
				} // for
				
				int numberOfValues = consideredValid.size();
				
				if ( numberOfValues >= 1 ) {
					int index = indexAccordingToTheValidOption( numberOfValues, option );
					if  ( index >= 0 ) {
						return consideredValid.get( index );
					}
					// Continue to other rules
				}
				// Continue to other rules
			}
			// Continue to other rules
			
		} // end of EQUAL_TO or ONE_OF
		
		//
		// NOT_ONE_OF
		//
		if ( notOneOfBR != null ) {
			List< Object > values = configurationValues(
					notOneOfBR.getValueConfiguration(), otherElementValues );			
			ValueGenerator< ? > gen;
			Object value;
			// Generate a value until it is not in the list
			do {														
				if ( expression != null ) { // REGEX
					gen = new XegerRegExValueGenerator( expression );
				} else if ( minValueBR != null || maxValueBR != null ) { // MIN_VALUE, MAX_VALUE
					gen = valueGeneratorForValueType( ee.getValueType(), minValue, maxValue );
				} else if ( minLengthBR != null || maxLengthBR != null ) { // MIN_LENGTH, MAX_LENGTH										
					gen = valueGeneratorForValueType( ee.getValueType(), minLength, maxLength );
				} else { // if ( null == value ) { // None rule defined
					gen = valueGeneratorForValueType( ee.getValueType(), null, null );					
				}			
				value = gen.validValue( option );
			} while ( values.contains( value ) );
			return value;
		}
		
		//
		// Generates for other rules according to the precedence
		//
		
		// 1. REGEX
		if ( regExBR != null ) {
			if ( expression != null ) {					
				ValueGenerator< ? > gen = new XegerRegExValueGenerator( expression );
				return gen.validValue( option );
			}
		}
		// 2. MIN_VALUE, MAX_VALUE
		if ( minValueBR != null || maxValueBR != null ) {			
			ValueGenerator< ? > gen = valueGeneratorForValueType( ee.getValueType(), minValue, maxValue );
			return gen.validValue( option );					
		}
		
		// 3. MIN_LENGTH, MAX_LENGTH
		if ( minLengthBR != null || maxLengthBR != null ) {										
			ValueGenerator< ? > gen = valueGeneratorForValueType( ee.getValueType(), minLength, maxLength );
			return gen.validValue( option );
		}
		
		// No rules defined, then generates a value according to the type
		ValueGenerator< ? > gen = valueGeneratorForValueType( ee.getValueType(), null, null );
		return gen.validValue( option );	
	}
	

	/**
	 * Generates a invalid value according to the business rules.
	 * 
	 * @param option				The invalid value option.
	 * @param ee					The element with the rules.
	 * @param otherElementValues	The other elements' values.	
	 * @return						A invalid value or null if there are
	 * 								conflicting rules.
	 * @throws Exception
	 */
	public Object generateInvalidValue(  // TODO refactor this method (too long)
			final InvalidValueOption option,
			final Element ee,
			final Map< Element, Object > otherElementValues
			) throws Exception {
		
		logger.debug( "InvalidValueOption: " + option );
		logger.debug( "Available BusinessRules: " + ee.getBusinessRules() );
		
		BusinessRule equalToBR = ee.businessRuleWithType( BusinessRuleType.EQUAL_TO );
		BusinessRule oneOfBR = ee.businessRuleWithType( BusinessRuleType.ONE_OF );
		BusinessRule notOneOfBR = ee.businessRuleWithType( BusinessRuleType.NOT_ONE_OF );
		BusinessRule regExBR = ee.businessRuleWithType( BusinessRuleType.REG_EX );
		BusinessRule minValueBR = ee.businessRuleWithType( BusinessRuleType.MIN_VALUE );
		BusinessRule maxValueBR = ee.businessRuleWithType( BusinessRuleType.MAX_VALUE );
		BusinessRule minLengthBR = ee.businessRuleWithType( BusinessRuleType.MIN_LENGTH );
		BusinessRule maxLengthBR = ee.businessRuleWithType( BusinessRuleType.MAX_LENGTH );
		
		//
		// Handle the cases of nullity
		//		
		switch ( option ) {
			case INVALID_FORMAT		: if ( null == regExBR ) return null; break;
			case RIGHT_BEFORE_MIN	: ; // next
			case RANDOM_BEFORE_MIN	: if ( null == minValueBR && null == minLengthBR && null == oneOfBR && null == notOneOfBR && null == equalToBR ) return null; break;
			case RIGHT_AFTER_MAX	: ; // next
			case RANDOM_AFTER_MAX	: if ( null == maxValueBR && null == maxLengthBR && null == oneOfBR && null == notOneOfBR && null == equalToBR ) return null; break;
			default					: throw new RuntimeException( "Unexpected option: " + option.toString() );
		}
		
		//
		// INVALID_FORMAT
		//
		if ( option.equals( InvalidValueOption.INVALID_FORMAT )	) {
			//
			// Returns a invalid value according to the format
			//			
			final Object expressionObj = firstConfigurationValue(
					regExBR.getValueConfiguration(), otherElementValues );
			final String expression = ( expressionObj != null ) ? expressionObj.toString() : null;
			if ( expression != null ) {					
				ValueGenerator< ? > gen = new XegerRegExValueGenerator( expression );
				return gen.invalidValue( option );
			}
			// Returns null if no expression is configured
			return null;
		}
		
		//
		// RIGHT_BEFORE_MIN, RANDOM_BEFORE_MAX, RIGHT_AFTER_MAX, RANDOM_AFTER_MAX
		//

		// With EQUAL_TO or ONE_OF	
		if ( equalToBR != null || oneOfBR != null ) {
			//
			// Generate values out of the list
			//
			
			List< Object > values;
			if ( equalToBR != null ) {
				values = configurationValues(			
					equalToBR.getValueConfiguration(), otherElementValues );
			} else {
				values = configurationValues(			
					oneOfBR.getValueConfiguration(), otherElementValues );
			}			
			//
			// For STRING values, generates random strings until one of them
			// not to be found in the list.
			//
			if ( ee.getValueType().equals( ValueType.STRING ) ) {
				// If value is null, just generates a random string
				if ( null == values ) {
					return ( new StringValueGenerator( null, null ) ).invalidValue( option ); 
				}									
				int maxLength = maximumLength( values );
				StringValueGenerator gen = new StringValueGenerator( 0, maxLength );
				for ( int i = 0; i < MAX_TRIES_WITH_RANDOM_VALUE; ++i ) {
					String value = gen.validValue( ValidValueOption.RANDOM_INSIDE_RANGE );
					if ( ! values.contains( value ) ) {
						return value;
					}
				}
				// Generates a value with a greater length
				return gen.invalidValue( InvalidValueOption.RIGHT_AFTER_MAX );
			}
			//
			// For INTEGER, DOUBLE, DATETIME, DATE and TIME values,
			// get min and max to generate values out of the list.
			//
			Object minValue, maxValue;
			ValueType valueType = ee.getValueType();
			// Choose the *CONTRARY* value for min and max to get the minimum
			// and maximum later. (It is not to use in a range)
			switch ( valueType ) {
				case INTEGER	: {
					minValue = new Long( LongValueGenerator.MAX_VALUE );
					maxValue = new Long( LongValueGenerator.MIN_VALUE );
					break;
				}
				case DOUBLE		: {
					minValue = new Double( DoubleValueGenerator.MAX_VALUE );
					maxValue = new Double( DoubleValueGenerator.MIN_VALUE );
					break;
				}
				case DATETIME	: {
					minValue = new DateTime( DateTimeValueGenerator.MAX_VALUE );
					maxValue = new DateTime( DateTimeValueGenerator.MIN_VALUE );
					break;
				}
				case DATE		: {
					minValue = new LocalDate( DateValueGenerator.MAX_VALUE );
					maxValue = new LocalDate( DateValueGenerator.MIN_VALUE );
					break;
				}
				case TIME		: {
					minValue = new LocalTime( TimeValueGenerator.MAX_VALUE );
					maxValue = new LocalTime( TimeValueGenerator.MIN_VALUE );
					break;
				}
				default			: return null; // Invalid type						
			}
			// Continue the code above and calculates the minValue and maxValue
			for ( Object value : values ) {
				switch ( valueType ) {
					case INTEGER	: {
						Long current = ObjectConverter.toLong( value );
						Long min = ObjectConverter.toLong( minValue );
						Long max = ObjectConverter.toLong( maxValue );
						if ( current != null && min != null && max != null ) {
							if ( current < min ) {
								minValue = current;
							}
							if ( current > max ) {
								maxValue = current;
							}
						}
						break;
					}
					case DOUBLE		: {
						Double current = ObjectConverter.toDouble( value );
						Double min = ObjectConverter.toDouble( minValue );
						Double max = ObjectConverter.toDouble( maxValue );
						if ( current != null && min != null && max != null ) {
							if ( current < min ) {
								minValue = current;
							}
							if ( current > max ) {
								maxValue = current;
							}
						}
						break;
					}
					case DATETIME	: {
						DateTime current = ObjectConverter.toDateTime( value );
						DateTime min = ObjectConverter.toDateTime( minValue );
						DateTime max = ObjectConverter.toDateTime( maxValue );
						if ( current != null && min != null && max != null ) {
							if ( current.isBefore( min ) ) {
								minValue = current;
							}
							if ( current.isAfter( max ) ) {
								maxValue = current;
							}
						}
						break;
					}
					case DATE		: {
						LocalDate current = ObjectConverter.toLocalDate( value );
						LocalDate min = ObjectConverter.toLocalDate( minValue );
						LocalDate max = ObjectConverter.toLocalDate( maxValue );
						if ( current != null && min != null && max != null ) {
							if ( current.isBefore( min ) ) {
								minValue = current;
							}
							if ( current.isAfter( max ) ) {
								maxValue = current;
							}
						}
						break;
					}
					case TIME		: {
						LocalTime current = ObjectConverter.toLocalTime( value );
						LocalTime min = ObjectConverter.toLocalTime( minValue );
						LocalTime max = ObjectConverter.toLocalTime( maxValue );
						if ( current != null && min != null && max != null ) {
							if ( current.isBefore( min ) ) {
								minValue = current;
							}
							if ( current.isAfter( max ) ) {
								maxValue = current;
							}
						}
						break;
					}
					default : {}  // do nothing
				}
			}
			// Generate a invalid value based on the min and max values
			ValueGenerator< ? > gen = valueGeneratorForValueType( ee.getValueType(), minValue, maxValue );
			return gen.invalidValue( option );
			
		} // end with EQUAL_TO or ONE_OF
				

		// With NOT_ONE_OF generates a random valid value!
		if ( notOneOfBR != null ) {							
			List< Object > values = configurationValues(			
					notOneOfBR.getValueConfiguration(), otherElementValues );
					
			int numberOfValues = values.size();
			if ( numberOfValues < 1 ) {
				return null; // None valid value
			}
			int index = ( new LongRandom() ).between( 0L, numberOfValues - 1L ).intValue();
			return ( index >= 0 ) ? values.get( index ) : null;
		}
		
		// With MIN_VALUE or MAX_VALUE
		if ( minValueBR != null || maxValueBR != null ) {						
			Object minValue = ( minValueBR != null ) ? firstConfigurationValue(
					minValueBR.getValueConfiguration(), otherElementValues ) : null;
			
			Object maxValue = ( maxValueBR != null ) ? firstConfigurationValue(
					maxValueBR.getValueConfiguration(), otherElementValues ) : null;
			
			ValueGenerator< ? > gen = valueGeneratorForValueType( ee.getValueType(), minValue, maxValue );
			return gen.invalidValue( option );					
		}
		
		// With MIN_LENGTH or MAX_LENGTH
		if ( minLengthBR != null || maxLengthBR != null ) {							
			Integer minLength = ( minLengthBR != null )
				? ObjectConverter.toInt( firstConfigurationValue( minLengthBR.getValueConfiguration(), otherElementValues ) )
				: null;
			
			Integer maxLength = ( maxLengthBR != null )
				? ObjectConverter.toInt( firstConfigurationValue( maxLengthBR.getValueConfiguration(), otherElementValues ) )
				: null;
			
			ValueGenerator< ? > gen = valueGeneratorForValueType( ee.getValueType(), minLength, maxLength );
			return gen.invalidValue( option );
		}
		
		// All other case, returns null
		return null;
	}
		

	private int maximumLength(List< Object > values) {
		int maxLength = 0;				
		for ( Object value : values ) {
			if ( null == value ) continue;
			String s = value.toString();
			int len = s.length();
			if ( len > maxLength ) {
				maxLength = len;
			}					
		}
		return maxLength;
	}
	
	
	private int indexAccordingToTheValidOption(
			final int numberOfValues,
			final ValidValueOption option
			) {
		int index;
		switch ( option ) {
			case ZERO				: index = 0; break;
			case MIN				: index = 0; break;
			case MEDIAN 			: index = numberOfValues / 2; break;
			case MAX				: index = ( numberOfValues > 1 ) ? numberOfValues - 1 : 0; break;
			case RIGHT_AFTER_MIN	: index = ( numberOfValues > 1 ) ? 1 : 0; break;
			case RIGHT_BEFORE_MAX	: index = ( numberOfValues > 2 ) ? numberOfValues - 2 : 0; break;
			case RANDOM_INSIDE_RANGE: index = (new LongRandom()).between( 0L, numberOfValues - 1L ).intValue(); break;
			default					: index = -1;
		}
		return index;
	}
	
	private Object firstConfigurationValue(
			final ValueConfiguration vc,
			final Map< Element, Object > otherElementValues
			) throws Exception {
		List< Object > values = configurationValues( vc, otherElementValues );
		if ( values.isEmpty() ) {					
			return null;			
		}						
		return values.get( 0 );
	}
	

	private boolean isInLengthRange(int length, Integer minLength,
			Integer maxLength) {
		return length >= minLength && length <= maxLength;
	}


	private boolean isBetween(ValueType valueType, Object value,
			Object minValue, Object maxValue) {		
		if ( valueType.equals( ValueType.INTEGER ) ) {
			Long v = ObjectConverter.toLong( value );
			Long min = ObjectConverter.toLong( minValue );
			Long max = ObjectConverter.toLong( maxValue );
			if ( v != null && min != null && max != null ) {
				return ( v >= min && v <= max );
			}
		} else if ( valueType.equals( ValueType.DOUBLE ) ) {
			Double v = ObjectConverter.toDouble( value );
			Double min = ObjectConverter.toDouble( minValue );
			Double max = ObjectConverter.toDouble( maxValue );
			if ( v != null && min != null && max != null ) {
				return ( v >= min && v <= max );
			}
		} else if ( valueType.equals( ValueType.DATETIME ) ) {
			DateTime v = ObjectConverter.toDateTime( value );
			DateTime min = ObjectConverter.toDateTime( minValue );
			DateTime max = ObjectConverter.toDateTime( maxValue );
			if ( v != null && min != null && max != null ) {
				return ( v.isEqual( min ) || v.isAfter( min ) )
					&& ( v.isEqual( max ) || v.isBefore( max ) );
			}
		} else if ( valueType.equals( ValueType.DATE ) ) {
			LocalDate v = ObjectConverter.toLocalDate( value );
			LocalDate min = ObjectConverter.toLocalDate( minValue );
			LocalDate max = ObjectConverter.toLocalDate( maxValue );
			if ( v != null && min != null && max != null ) {
				return ( v.isEqual( min ) || v.isAfter( min ) )
					&& ( v.isEqual( max ) || v.isBefore( max ) );			
			}
		} else if ( valueType.equals( ValueType.TIME ) ) {
			LocalTime v = ObjectConverter.toLocalTime( value );
			LocalTime min = ObjectConverter.toLocalTime( minValue );
			LocalTime max = ObjectConverter.toLocalTime( maxValue );
			if ( v != null && min != null && max != null ) {
				return ( v.isEqual( min ) || v.isAfter( min ) )
					&& ( v.isEqual( max ) || v.isBefore( max ) );
			}
		}
		return false;
	}

	/**
	 * Chooses the right value generator according to the value type.
	 * 
	 * @param valueType	The value type.
	 * @param min		The minimal (can be a value or a length). 
	 * @param max		The maximal (can be a value or a length).
	 * @return			The value generator.
	 * @throws RuntimeException.
	 */
	private ValueGenerator< ? > valueGeneratorForValueType(
			final ValueType valueType, final Object min, final Object max) {
		switch ( valueType ) {
			case STRING		: {
				Integer minLength = ObjectConverter.toInt( min );
				Integer maxLength = ObjectConverter.toInt( max );				
				return new StringValueGenerator( minLength, maxLength );
			}
			case INTEGER	: {			
				Long minValue = ObjectConverter.toLong( min );
				Long maxValue = ObjectConverter.toLong( max );						
				return new LongValueGenerator( minValue, maxValue );
			}
			case DOUBLE		: {
				Double minValue = ObjectConverter.toDouble( min );
				Double maxValue = ObjectConverter.toDouble( max );				
				return new DoubleValueGenerator( minValue, maxValue );				
			}
			case DATETIME	: {
				DateTime minValue = ObjectConverter.toDateTime( min );
				DateTime maxValue = ObjectConverter.toDateTime( max );				
				return new DateTimeValueGenerator( minValue, maxValue );
			}
			case DATE		: {
				LocalDate minValue = ObjectConverter.toLocalDate( min );
				LocalDate maxValue = ObjectConverter.toLocalDate( max );
				return new DateValueGenerator( minValue, maxValue );
			}
			case TIME		: {
				LocalTime minValue = ObjectConverter.toLocalTime( min );
				LocalTime maxValue = ObjectConverter.toLocalTime( max );
				return new TimeValueGenerator( minValue, maxValue );
			}
			default			: {
				throw new RuntimeException( "Not supported value type: " + valueType );
			}
		}		
	}
	

	/**
	 * Load values according to the configuration.
	 * 
	 * @param vc					The value configuration.
	 * @param otherElementsValues	The other element values.
	 * @return						A list of objects (can be empty).
	 * @throws Exception
	 */
	protected List< Object > configurationValues(
			final ValueConfiguration vc,
			final Map< Element, Object > otherElementsValues
			) throws Exception {		
		
		List< Object > values = new ArrayList< Object >();
		if ( null == vc ) {
			return values;
		}
		if ( vc instanceof SingleVC ) {
			
			values.add( ( (SingleVC) vc ).getValue() );
			
		} else if ( vc instanceof MultiVC ) {
			
			values.addAll( ( (MultiVC) vc ).getValues() );
			
		} else if ( vc instanceof QueryBasedVC ) {
			
			QueryBasedVC qvc = ( (QueryBasedVC) vc );
			values.addAll( valuesFromQueryConfiguration( qvc, otherElementsValues ) );
			logger.debug( "# Query element value	: " + values );
			
		} else if ( vc instanceof ElementBasedVC ) {
			
			Element ee = ( (ElementBasedVC) vc ).getReferencedElement();
			Object value = otherElementsValues.get( ee );
			if ( value != null ) {
				if ( value instanceof List ) {
					List< ? > valueList = (List< ? >) value;
					values.addAll( valueList );
				} else {
					values.add( value );
				}
			}
			logger.debug( "# Referenced element		: " + ee.getName() );
			logger.debug( "# Referenced element value	: " + values );
			
		} else if ( vc  instanceof RegExBasedVC ) {
			
			RegEx regex = ( (RegExBasedVC) vc ).getRegEx();
			values.add( regex.getExpression() );
			
		}
		return values;
	}

	
	/**
	 * Loads a list of values from a query configuration. This method is
	 * public so that it can be used in test cases.
	 * 
	 * @param qvc					the query configuration.
	 * @param otherElementValues	the other element's values
	 * @return						a list of values (can be empty).
	 * @throws Exception
	 */
	public List< Object > valuesFromQueryConfiguration(
			final QueryBasedVC qvc,
			final Map< Element, Object > otherElementValues
			) throws Exception {
		
		List< Object > values = new ArrayList< Object >();
		if ( null == qvc ) { return values; }
	
		QueryConfig qc = qvc.getQueryConfig();
		if ( null == qc ) { return values; }
		DatabaseConfig dbc = qc.getDatabaseConfig();
		if ( null == dbc ) { return values; }
		
		//
		// Get the database driver
		//
		
		final String driverCacheKey = DriverCache.makeKey( dbc.getDriver() );
		Driver driver = driverCache.get( driverCacheKey );
		if ( null == driver ) {
			String msg = "The request driver is not available: %s. Please configure a driver template and try again.";
			String fmtMsg = String.format( msg, dbc.getDriver() );
			throw new DriverUnavailableException( fmtMsg );
		}
		
		//
		// Load the database connection
		//
		
		final String databaseCacheKey = ConnectionCache.keyFor(
				driverCacheKey, dbc.getName(), dbc.getPort() ); 
				
		Connection c = connectionCache.get( databaseCacheKey );
		if ( null == c ) {
			c = ConnectionFactory.createConnection(
					driver,
					dbc.toJdbcUrl(),
					dbc.getUser(),
					dbc.getPassword(),
					dbc.getDialect()
					);
			connectionCache.put( databaseCacheKey, c );
		}
		
		//
		// Prepare the command statement
		//
		
		final String queryCommand = qc.getCommand();
		logger.debug( "Command is " + queryCommand );
		
		PreparedStatement statement = c.prepareStatement( queryCommand );
		List< ParameterConfig > parameters = qvc.getParameters();
		logger.debug( "Parameters are " + parameters );
		
		//
		// Extract the parameter values
		//
		
		List< Object > parameterValues = new ArrayList< Object >();

		for ( ParameterConfig parameter : parameters ) {
			
			Object parameterValue = configurationValues(
					parameter.getValueConfiguration(),
					otherElementValues
					);
			
			logger.debug( "### Parameter value class	: " + parameterValue.getClass().getName() );
			logger.debug( "### Parameter value			: " + parameterValue );
			logger.debug( "### Parameter value is list	: " + (parameterValue instanceof List) );
			
			if ( parameterValue instanceof List ) {
				List< ? > valueList = (List< ? >) parameterValue;
				if ( valueList.isEmpty() ) {
					parameterValue = "";
				} else {
					parameterValue = valueList.get( 0 );
				}
				logger.debug( "### Value			: " + parameterValue );
			}
			
			if ( null == parameterValue ) {
				parameterValue = ""; // Need to be a value
			}
			
			parameterValues.add( parameterValue );
		}
		
		
		//
		// Adjust parameters to match the parameters count
		//
		
		final int queryParameterCount = statement.getParameterMetaData().getParameterCount();
		
		while ( parameterValues.size() < queryParameterCount ) {
			parameterValues.add( "" ); // Add empty strings as the parameter value
		}
		
		//
		// Truncate string parameters to the column size
		//
		// Get parameters metadata info
		final ParameterMetaData paramMetaData = statement.getParameterMetaData();
		if ( paramMetaData != null ) {
			
			int index = 1;
			for ( Object parameterValue : parameterValues ) {
			
				try {
					final int maxPrecision = paramMetaData.getPrecision( index );
					final String paramClassName = paramMetaData.getParameterClassName( index );
					
					// Truncates string values to not give an error in the query,
					// like DataTruncation.
					
					// TODO make this for other value types like date, time,
					// double, etc.
					if ( paramClassName != null && parameterValue != null
							&& paramClassName.equals( String.class.getName() ) 
							&& parameterValue.getClass().getName().equals( String.class.getName() ) ) {
						
						String valueStr = (String) parameterValue;
						if ( valueStr.length() > maxPrecision ) {
							parameterValue = valueStr.substring( 0, maxPrecision );
						}
					}
				} catch (SQLException e) {
					logger.debug( "Database does not support to get its metadata. DETAILS: " 
							+ e.getLocalizedMessage() );
					// Continue on error	
				}
			
				index++;
			} // for
			
		} // if
		
		logger.debug( "parameter values are: " + parameterValues );
		
		//
		// Set the parameter value in the query
		//
		
		for ( int index = 1; index <= queryParameterCount; ++index ) {
			Object parameterValue = parameterValues.get( index - 1 );
			statement.setObject( index, parameterValue );
		}
		
		//
		
		final String targetColumnName = qvc.getTargetColumn() != null ? qvc.getTargetColumn() : "";
		logger.debug( "targetColumnName is: " + targetColumnName );
		if ( targetColumnName.isEmpty() ) {
			return values;
		}
		
		// VERY IMPORTANT: use the query parameters values as part of the key
		final String resultSetCacheKey = ResultSetCache.keyFor( dbc.getName(), queryCommand, parameterValues.toString() );

		List< Map< String, Object > > cacheValues = resultSetCache.get( resultSetCacheKey );
		
		if ( null == cacheValues ) { // Not in cache
			
			//
			// Create the result set cache
			//
			
			logger.debug( "It does not have the key: " + resultSetCacheKey );
			
			ResultSet rs = statement.executeQuery();
			final int columnCount = rs.getMetaData().getColumnCount();
			logger.debug( "Column count is: " + columnCount );
			
			cacheValues = new ArrayList< Map< String, Object > >();
			
			logger.debug( "resultSet is closed: " + rs.isClosed() );
			
			int row = 1;
			while ( rs.next() ) {
				logger.debug( "row " + row );
			
				// Create the map and add it to the cache
				Map< String, Object > map = new TreeMap< String, Object >();
				for ( int i = 1; ( i <= columnCount ); ++i ) {
					Object value = rs.getObject( i ) ;
					String columnName = rs.getMetaData().getColumnName( i );
					logger.debug( "Column is: " + columnName + " and its value is: " + value );
					map.put( columnName, value );					
				}
				cacheValues.add( map );
				
				// Add the column value
				
				Object targetColumnValue = null;
				try {
					targetColumnValue = rs.getObject( targetColumnName );
				} catch (SQLException e) {
					targetColumnValue = "";
				}
				logger.debug( "TargetColumnValue is " + targetColumnValue );
				values.add( targetColumnValue );
				
				row++;
			}
			rs.close();
			
			// Add the cache values
			resultSetCache.put( resultSetCacheKey, cacheValues );
			logger.debug( "Created resultSetCache with key: " + resultSetCacheKey );
			
			return values;
		}
		
		//
		// The query values exist in the cache
		//
		
		logger.debug( "It has the key: " + resultSetCacheKey );
		logger.debug( "Target column is: " + targetColumnName );
		
		for ( Map< String, Object > map : cacheValues ) {
			Object value = map.get( targetColumnName );
			logger.debug( "Cached value is: " + value );
			values.add( value );
		}
		return values;
	}

}