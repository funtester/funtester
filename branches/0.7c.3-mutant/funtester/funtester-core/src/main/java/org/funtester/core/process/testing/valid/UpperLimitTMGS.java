package org.funtester.core.process.testing.valid;

import java.util.List;

import org.funtester.common.at.AbstractTestMethod;
import org.funtester.core.process.rule.ElementValueGenerator;
import org.funtester.core.process.testing.IdGenerator;
import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.software.Scenario;

/**
 * Strategy that extends {@link AllWithValidValuesTMGS} and generates
 * values at the upper limit of each scenario element (field).
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class UpperLimitTMGS extends AllWithValidValuesTMGS {
	
	public UpperLimitTMGS(
			ElementValueGenerator valueGen,
			IdGenerator idGenerator
			) {
		super( valueGen, idGenerator );
	}

	public String getTestMethodBaseName() {
		return "all_field_values_at_the_upper_limit";
	}

	public List< AbstractTestMethod > generateTestMethods(
			final Scenario scenario
			) throws Exception {
		return generateOneMethodWithValidValues(
				scenario,
				ValidValueOption.MAX,
				EditableElementsUse.ALL
				);
	}
}