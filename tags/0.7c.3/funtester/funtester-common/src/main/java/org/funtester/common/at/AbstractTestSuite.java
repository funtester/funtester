package org.funtester.common.at;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

/**
 * Abstract test suite
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class AbstractTestSuite extends AbstractTest {
	
	private static final long serialVersionUID = -2212930620703933476L;
	
	private String softwareName = "";
	
	private String creation = DateTime.now().toString();
	
	private List< AbstractTestDatabaseConnection > connections = 
		new ArrayList< AbstractTestDatabaseConnection >();
	
	private List< AbstractTestCase > testCases =
		new ArrayList< AbstractTestCase >();
	
	
	public AbstractTestSuite() {
		super();
	}
	
	public AbstractTestSuite(String softwareName, String name) {
		this();
		setSoftwareName( softwareName );
		setName( name );
	}

	public String getSoftwareName() {
		return softwareName;
	}

	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}

	public String getCreation() {
		return creation;
	}
	
	public void setCreation(String creation) {
		this.creation = creation;
	}
	
	public List< AbstractTestDatabaseConnection > getConnections() {
		return connections;
	}

	public void setConnections(List< AbstractTestDatabaseConnection > connections) {
		this.connections = connections;
	}

	public List< AbstractTestCase > getTestCases() {
		return testCases;
	}
	
	public void setTestCases(List< AbstractTestCase > testCases) {
		this.testCases = testCases;
	}

	/**
	 * Adds a list of test cases.
	 * 
	 * @param tests	the list to be added.
	 * @return		true if added, false otherwise.
	 */
	public boolean addAll(List< AbstractTestCase > tests) {
		return testCases.addAll( tests );
	}
	
	/**
	 * Adds a {@code SemanticTestCase}
	 * 
	 * @param test	the {@code SemanticTestCase} to be added.
	 * @return		true if added, false otherwise.
	 */
	public boolean add(AbstractTestCase test) {
		return testCases.add( test );
	}
	
	/**
	 * Returns a {@code SemanticTestCase} with a certain name.
	 * 
	 * @param name	the name of the {@code SemanticTestCase} to find.
	 * @return		the test case or {@code null} if not found.
	 */
	public AbstractTestCase testCaseWithName(final String name) {
		for ( AbstractTestCase stc : testCases ) {
			if ( stc.getName().equalsIgnoreCase( name ) ) {
				return stc;
			}
		}
		return null;
	}
	
	// FROM Copier
	
	@Override
	public AbstractTest copy(AbstractTest that) {
		super.copy( that );
		if ( ! ( that instanceof AbstractTestSuite ) ) {
			return this;
		}
		AbstractTestSuite suite = (AbstractTestSuite) that;
		this.softwareName = suite.softwareName;
		this.creation = suite.creation;
		this.testCases.clear();
		this.testCases.addAll( suite.testCases );
		return this;
	}
	
	@Override
	public AbstractTest newCopy() {
		return new AbstractTestSuite().copy( this );
	}
}
