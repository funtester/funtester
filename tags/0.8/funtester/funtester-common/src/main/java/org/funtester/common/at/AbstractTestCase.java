package org.funtester.common.at;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Test case for a use case.
 *  
 * @author Thiago Delgado Pinto
 *
 */
public class AbstractTestCase extends AbstractTest {

	private static final long serialVersionUID = 9132730105977543143L;
	
	private String useCaseName;
	private String scenarioName;
	private Set< String > includeFiles = new LinkedHashSet< String >();
	private List< AbstractTestDatabaseScript > scripts =
		new ArrayList< AbstractTestDatabaseScript >();
	private List< AbstractTestMethod > testMethods =
		new ArrayList< AbstractTestMethod >();
		
	public AbstractTestCase() {
		super();
	}
	
	public AbstractTestCase(String name) {
		super( name );
	}
	
	public String getUseCaseName() {
		return useCaseName;
	}

	public void setUseCaseName(String useCaseName) {
		this.useCaseName = useCaseName;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public Set< String > getIncludeFiles() {
		return includeFiles;
	}

	public void setIncludeFiles(Set< String > includeFiles) {
		this.includeFiles = includeFiles;
	}

	public List< AbstractTestDatabaseScript > getScripts() {
		return scripts;
	}

	public void setScripts(List< AbstractTestDatabaseScript > scripts) {
		this.scripts = scripts;
	}

	public List< AbstractTestMethod > getTestMethods() {
		return testMethods;
	}

	public void setTestMethods(List< AbstractTestMethod > testMethods) {
		this.testMethods = testMethods;
	}

	/**
	 * Adds a list of {@code SemanticTestMethod}.
	 * 
	 * @param tests	the list of {@code SemanticTestMethod} to add.
	 * @return		true if added, false otherwise.
	 */
	public boolean addAllTestMethods(List< AbstractTestMethod > tests) {
		return this.testMethods.addAll( tests );
	}
	
	/**
	 * Returns a {@code SemanticTestMethod} with a certain name.
	 * 
	 * @param name	the name of the {@code SemanticTestMethod} to find.
	 * @return		the {@code SemanticTestMethod} or {@code null} if not found.
	 */
	public AbstractTestMethod testMethodWithName(String name) {
		for ( AbstractTestMethod stm : testMethods ) {
			if ( stm.getName().equalsIgnoreCase( name ) ) {
				return stm;
			}
		}
		return null;
	}
	
	// FROM Copier

	@Override
	public AbstractTest copy(AbstractTest that) {		
		if ( ! ( that instanceof AbstractTestCase ) ) {
			return this;
		}
		super.copy( that );
		AbstractTestCase test = (AbstractTestCase) that;
		this.useCaseName = test.useCaseName;
		this.includeFiles.clear();
		this.includeFiles.addAll( test.includeFiles );
		this.testMethods.clear();
		this.testMethods.addAll( test.testMethods );
		return this;
	}

	@Override
	public AbstractTest newCopy() {
		return ( new AbstractTestCase() ).copy( this );
	}
}
