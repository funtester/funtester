package org.funtester.core.process.testing.valid;

import java.util.List;

import org.funtester.common.at.AbstractTestMethod;
import org.funtester.core.process.rule.ElementValueGenerator;
import org.funtester.core.process.testing.IdGenerator;
import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.software.Scenario;

/**
 * Strategy that extends {@link AllWithValidValuesTMGS} and generates
 * zero values (or equivalents) for all the scenario elements (fields).
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class ZeroTMGS extends AllWithValidValuesTMGS {
		
	public ZeroTMGS(
			ElementValueGenerator valueGen,
			IdGenerator idGenerator
			) {
		super( valueGen, idGenerator );
	}

	public String getTestMethodBaseName() {
		return "all_field_values_with_zero_or_equivalent";
	}

	public List< AbstractTestMethod > generateTestMethods(
			final Scenario scenario
			) throws Exception {
		return generateOneMethodWithValidValues(
				scenario,
				ValidValueOption.ZERO,
				EditableElementsUse.ALL
				);
	}
}