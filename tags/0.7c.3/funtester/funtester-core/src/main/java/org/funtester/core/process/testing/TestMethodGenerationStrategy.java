package org.funtester.core.process.testing;

import java.util.List;

import org.funtester.common.at.AbstractTestMethod;
import org.funtester.core.software.Scenario;

/**
 * Allows to define strategies for test method generation, according to the
 * defined business rules.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface TestMethodGenerationStrategy {
	
	/**
	 * Returns	the strategy description.
	 * @return	the description.
	 */
	String getDescription();	
	
	/**
	 * Returns the test method base name. The base name could be formatted to
	 * accept the inclusion of other names inside it, like when we want to
	 * test a specific element.
	 * <p>
	 * <b>This name (event formatted) is used to identify the test strategy.</b> 
	 * </p>
	 * 
	 * @return the test method base name.
	 */
	String getTestMethodBaseName();
	
	/**
	 * For tests that respects the defined business rules, will be expected
	 * that the use case have success.
	 * 
	 * @return true if it is expected to have success, false otherwise.
	 */
	boolean expectedUseCaseSuccess();
	
	/**
	 * Generates semantic test methods according to the strategy.
	 * 
	 * @param scenario	the scenario used to generate the methods.
	 * @return			a list of semantic test methods.
	 * @throws Exception
	 */
	List< AbstractTestMethod > generateTestMethods(
			final Scenario scenario) throws Exception;
	
}
