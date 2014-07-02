package org.funtester.core.process.testing.invalid;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.funtester.common.at.AbstractTestMethod;
import org.funtester.core.process.rule.ElementValueGenerator;
import org.funtester.core.process.testing.IdGenerator;
import org.funtester.core.process.value.InvalidValueOption;
import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.software.BusinessRuleType;
import org.funtester.core.software.Element;
import org.funtester.core.software.Scenario;

/**
 * Strategy that extends {@link AllExceptOneWithValidValuesTMGS} and generates
 * one element (field) with invalid format.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class InvalidFormatTMGS extends AllExceptOneWithValidValuesTMGS {

	public InvalidFormatTMGS(
			ElementValueGenerator valueGen, IdGenerator idGenerator) {
		super( valueGen, idGenerator );
	}

	@Override
	public String getTestMethodBaseName() {
		return "%s_with_invalid_format";
	}
	
	protected List< AbstractTestMethod >
	generateMethodsForJustTheElementWithDefinedFormat(
			final Scenario scenario,
			final ValidValueOption otherElementsOption,
			final InvalidValueOption chosenElementOption
			) throws Exception {
		
		//
		// IDEA: Generate one method for each editable element that has a
		//		 format (regex) business rule defined. 
		//		 All the elements but the chosen one will receive a valid value.
		//	
		
		List< AbstractTestMethod > methodList = new ArrayList< AbstractTestMethod >();
		
		Set< Element > elements = elementsFromScenarioUseCase( scenario );
		Set< Element > eeWithFormat = elementsWithFormat( elements );

		for ( Element e : eeWithFormat ) {		
			AbstractTestMethod method = generateMethod(
					scenario, e, chosenElementOption, otherElementsOption );	
			methodList.add( method );
		}
		
		return methodList;		
	}

	protected Set< Element > elementsWithFormat(
			final Set< Element > Elements) {
		Set< Element > elements = new LinkedHashSet< Element >();
		for ( Element ee : Elements ) {
			if ( ee.businessRuleWithType( BusinessRuleType.REG_EX ) != null ) {
				elements.add( ee );
			}
		}
		return elements;
	}

	public List< AbstractTestMethod > generateTestMethods(Scenario scenario)
			throws Exception {
		return generateMethodsForJustTheEditableElementsOfTheTargetUseCase(
				scenario,
				ValidValueOption.RANDOM_INSIDE_RANGE,
				InvalidValueOption.INVALID_FORMAT
				);
	}

}
