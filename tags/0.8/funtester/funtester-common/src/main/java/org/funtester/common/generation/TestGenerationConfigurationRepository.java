package org.funtester.common.generation;


/**
 * Repository of {@code TestGenerationConfiguration} objects.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface TestGenerationConfigurationRepository {

	/**
	 * Return the first {@code TestGenerationConfiguration}.
	 * 
	 * @return
	 * @throws Exception
	 */
	TestGenerationConfiguration first() throws Exception;
	
	/**
	 * Save a {@code TestGenerationConfiguration}.
	 * 
	 * @param obj	the object to save.
	 * @throws Exception
	 */
	void save(TestGenerationConfiguration obj) throws Exception;
	
}
