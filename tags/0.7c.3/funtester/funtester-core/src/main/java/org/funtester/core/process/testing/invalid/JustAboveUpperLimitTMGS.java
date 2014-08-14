package org.funtester.core.process.testing.invalid;

import java.util.List;

import org.funtester.common.at.AbstractTestMethod;
import org.funtester.core.process.rule.ElementValueGenerator;
import org.funtester.core.process.testing.IdGenerator;
import org.funtester.core.process.value.InvalidValueOption;
import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.software.Scenario;

/**
 * Strategy that extends {@link AllExceptOneWithValidValuesTMGS} and generates
 * a value just above the upper limit for the selected element (field).
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class JustAboveUpperLimitTMGS
	extends AllExceptOneWithValidValuesTMGS {

		public JustAboveUpperLimitTMGS(
				ElementValueGenerator valueGen,
				IdGenerator idGenerator
				) {
			super( valueGen, idGenerator );
		}

		@Override
		public String getTestMethodBaseName() {
			return "%s_just_above_upper_limit";
		}

		public List< AbstractTestMethod > generateTestMethods(
				final Scenario scenario) throws Exception {		
			return generateMethodsForJustTheEditableElementsOfTheTargetUseCase(
					scenario,
					ValidValueOption.RANDOM_INSIDE_RANGE,
					InvalidValueOption.RIGHT_AFTER_MAX
					);
		}
	}