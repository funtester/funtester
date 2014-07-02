package org.funtester.core.process.testing.valid;

import java.util.List;

import org.funtester.common.at.AbstractTestMethod;
import org.funtester.core.process.rule.ElementValueGenerator;
import org.funtester.core.process.testing.IdGenerator;
import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.software.Scenario;

/**
 * Strategy that extends {@link AllWithValidValuesTMGS} and generates
 * values just above the lower limit of each scenario element (field).
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class JustAboveLowerLimitTMGS extends AllWithValidValuesTMGS {
	
	public JustAboveLowerLimitTMGS(
			ElementValueGenerator valueGen,
			IdGenerator idGenerator
			) {
		super( valueGen, idGenerator );
	}
	
	public String getTestMethodBaseName() {
		return "all_field_values_just_above_lower_limit";
	}

	public List< AbstractTestMethod > generateTestMethods(
			final Scenario scenario
			) throws Exception {
		return generateOneMethodWithValidValues(
				scenario,
				ValidValueOption.RIGHT_AFTER_MIN,
				EditableElementsUse.ALL
				);
	}
}