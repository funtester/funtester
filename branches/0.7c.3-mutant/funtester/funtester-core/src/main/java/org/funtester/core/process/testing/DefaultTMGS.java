package org.funtester.core.process.testing;

import java.util.Map;

import org.funtester.common.at.AbstractTestActionStep;
import org.funtester.common.at.AbstractTestElement;
import org.funtester.common.at.AbstractTestMethod;
import org.funtester.common.at.AbstractTestOracleStep;
import org.funtester.core.process.rule.ElementValueGenerator;
import org.funtester.core.process.value.InvalidValueOption;
import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.software.ActionStep;
import org.funtester.core.software.Element;
import org.funtester.core.software.FlowType;
import org.funtester.core.software.OracleStep;
import org.funtester.core.software.Step;
import org.funtester.core.software.ValueType;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default {@link TestMethodGenerationStrategy}
 * 
 * @author Thiago Delgado Pinto
 *
 */
public abstract class DefaultTMGS implements TestMethodGenerationStrategy {

	private final ElementValueGenerator valueGenerator;
	private final IdGenerator idGenerator;
	private final org.slf4j.Logger logger;

	/**
	 * Constructor
	 * 
	 * @param valueGen		the value generator.
	 * @param idGenerator	the id generator.
	 */
	public DefaultTMGS(
			ElementValueGenerator valueGen,
			IdGenerator idGenerator
			) {
		this.valueGenerator = valueGen;
		this.idGenerator = idGenerator;
		this.logger = LoggerFactory.getLogger( getClass() );
	}	
	
	@Override
	public String getDescription() {
		return getClass().getSimpleName();
	}

	// OTHER
	
	protected Object generateValidValue( 
			final ValidValueOption option,
			final Element ee,
			final Map< Element, Object > otherElementValues
			) throws Exception {
		Object value =
			valueGenerator.generateValidValue( option, ee, otherElementValues );
		return formatedValue( value, ee.getValueType() );
	}
	
	public Object generateInvalidValue( 
			final InvalidValueOption option,
			final Element ee,
			final Map< Element, Object > otherElementValues
			) throws Exception {
		Object value =
			valueGenerator.generateInvalidValue( option, ee, otherElementValues );
		return formatedValue( value, ee.getValueType() );
	}
	
	/**
	 * Return the formatted value when applicable (just for DATE, TIME, and
	 * DATETIME value types).
	 *  
	 * @param value		the value to be formatted.
	 * @param valueType	the value type.
	 * @return			the formatted value or the original value.
	 */
	private Object formatedValue(final Object value, final ValueType valueType) {
		// Convert DATE, TIME and DATETIME to string using the current locale
		if ( value != null ) {
			if ( ValueType.DATE == valueType ) {
				org.joda.time.LocalDate date = (org.joda.time.LocalDate) value;
				return date.toString( DateTimeFormat.mediumDate() );
			} else if ( ValueType.DATE == valueType ) {
				org.joda.time.LocalTime time = (org.joda.time.LocalTime) value;
				return time.toString( DateTimeFormat.mediumTime() );
			} else if ( ValueType.DATETIME == valueType ) {
				org.joda.time.DateTime dateTime = (org.joda.time.DateTime) value;
				return dateTime.toString( DateTimeFormat.mediumDateTime() );
			}
		}
		return value;
	}

	protected IdGenerator getIdGenerator() {
		return this.idGenerator;
	}
	
	protected Logger getLogger() {
		return this.logger;
	}
	
	
	/**
	 * Create a {@code SemanticTestMethod} using the test strategy information
	 * plus the scenario name.
	 * 
	 * @param scenarioName	the name of the test scenario, usually composed by
	 * 						its flows. 
	 * @param nameArgs		the arguments involved in method name generation.
	 * @return				the semantic test method.
	 */
	protected AbstractTestMethod createSemanticTestMethod(
			final String scenarioName,
			final Object... nameArgs 
			) {
		AbstractTestMethod m = new AbstractTestMethod();
		m.setStrategyKind( getTestMethodBaseName() );
		m.setName( buildMethodName( nameArgs ) );
		m.setExpectedSuccess( expectedUseCaseSuccess() );
		m.setScenarioName( scenarioName );
		return m;
	}
		
	
	/**
	 * Create a {@code SemanticActionStep} from an {@code ActionStep}.
	 * 
	 * @param step	the action step.
	 * @return		a semantic action step.
	 */
	protected AbstractTestActionStep createSemanticActionStep(final ActionStep step) {
		return new AbstractTestActionStep(
				idGenerator.generate(), // generate the id
				useCaseId( step ),
				step.getFlow().getId(),
				step.getId(),				
				step.getActionNickname().actionName()
			);
	}
	
	/**
	 * Creates a {@code SemanticOracleStep} from an {@code ActionStep}.
	 * 
	 * @param step	the oracle step.
	 * @return		an semantic oracle step.
	 */
	protected AbstractTestOracleStep createSemanticOracleStep(final OracleStep step) {
		return new AbstractTestOracleStep(
				idGenerator.generate(), // generate the id
				useCaseId( step ),
				step.getFlow().getId(),
				step.getId(),
				step.getActionNickname().actionName()
			);
	}
	
	/**
	 * Get the right use case id according to the flow type. For activation
	 * flows, referenced only as {@code FlowType.FLOW}, it should be returned
	 * -1, that indicates that the semantic step has no owner use case (some
	 * use case that we don't known starts the current use case).
	 * 
	 * @param step	the step
	 * @return		an use case id.
	 */
	protected long useCaseId(final Step step) {
		long useCaseId = step.getFlow().getUseCase().getId();
		if ( step.getFlow().type() == FlowType.FLOW ) { // ActivationFlow
			return useCaseId = -1; // Indicates that none use case possesses it
		}
		return useCaseId;
	}
	
	
	/**
	 * Creates a {@link AbstractTestElement} from a {@link Element}
	 * without giving it a value.
	 * 
	 * @param ie	the {@link Element}
	 * @return		a {@link AbstractTestElement}.
	 */
	protected AbstractTestElement createSemanticElementWithoutValue(final Element ie) {
		AbstractTestElement semanticElement = new AbstractTestElement();
		semanticElement.setType( ie.typeName() );
		semanticElement.setInternalName( ie.getInternalName() );
		semanticElement.setName( ie.getName() );		
		return semanticElement;
	}
	
	/**
	 * Can be used to try to transform a name in a valid name for code.
	 * 
	 * @param name	the name to be transformed.
	 * @return		the valid name.
	 */
	protected String makeValidNameForCode(final String name) {
		return name
			.trim()
			.toLowerCase()
			.replaceAll( " ", "_" )
			.replaceAll( "[^A-Z-a-z0-9_]", "" );
	}
	
	/**
	 * Used by {@link DefaultTMGS#createSemanticTestMethod(String, Object...)}
	 * to generate the method name.
	 * 
	 * @param args	the arguments of the formatted string.
	 * @return		the scenario name.
	 */
	protected String buildMethodName(final Object... args) {
		return String.format( getTestMethodBaseName(), args );
	}
}