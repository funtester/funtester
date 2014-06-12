package org.funtester.core.process.testing.valid;

import java.util.List;

import org.funtester.common.at.AbstractTestMethod;
import org.funtester.core.process.rule.ElementValueGenerator;
import org.funtester.core.process.testing.IdGenerator;
import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.software.Scenario;

/**
 * Strategy that extends {@link AllWithValidValuesTMGS} and generates
 * random valid values for all the scenario elements (fields).
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class RandomTMGS extends AllWithValidValuesTMGS {
	
	public RandomTMGS(
			ElementValueGenerator valueGen,
			IdGenerator idGenerator
			) {
		super( valueGen, idGenerator );
	}

	public String getTestMethodBaseName() {
		return "all_fields_with_random_values";
	}

	public List< AbstractTestMethod > generateTestMethods(
			final Scenario scenario
			) throws Exception {
		return generateOneMethodWithValidValues(
				scenario,
				ValidValueOption.RANDOM_INSIDE_RANGE,
				EditableElementsUse.ALL
				);
	}
}