package org.funtester.plugin.code;

import java.util.ArrayList;
import java.util.List;

/**
 * Test annotation
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TestAnnotation {
	
	private final List< String > imports = new ArrayList< String >();
	
	private String testCase;
	private String testMethod;
	
	private String categoryStart;
	private String categoryEnd;
	private String categoryEachStart;
	private String categoryEachSeparator;
	private String categoryEachEnd;
	
	private String setUpOnce;
	private String tearDownOnce;
	
	private String setUp;
	private String tearDown;

	
	public TestAnnotation() {
	}
	
	//
	// BUILDERS
	//
	
	public TestAnnotation addImport(final String value) {
		imports.add( value );
		return this;
	}

	public TestAnnotation withTestCase(final String value) {
		this.setTestCase( value );
		return this;
	}
	
	public TestAnnotation withTestMethod(final String value) {
		this.setTestMethod( value );
		return this;
	}
	
	public TestAnnotation withCategoryStart(final String value) {
		this.setCategoryStart( value );
		return this;
	}
	
	public TestAnnotation withCategoryEnd(final String value) {
		this.setCategoryEnd( value );
		return this;
	}
	
	public TestAnnotation withCategoryEachStart(final String value) {
		this.setCategoryEachStart( value );
		return this;
	}
	
	public TestAnnotation withCategoryEachSeparator(final String value) {
		this.setCategoryEachSeparator( value );
		return this;
	}
	
	public TestAnnotation withCategoryEachEnd(final String value) {
		this.setCategoryEachEnd( value );
		return this;
	}
	
	public TestAnnotation withSetUpOnce(final String value) {
		this.setSetUpOnce( value );
		return this;
	}
	
	public TestAnnotation withTearDownOnce(final String value) {
		this.setTearDownOnce( value );
		return this;
	}
	
	public TestAnnotation withSetUp(final String value) {
		this.setSetUp( value );
		return this;
	}
	
	public TestAnnotation withTearDown(final String value) {
		this.setTearDown( value );
		return this;
	}
	
	//
	// GETTERS AND SETTERS
	//
	
	public List< String > getImports() {
		return imports;
	}

	public String getTestCase() {
		return testCase;
	}

	public void setTestCase(String testCase) {
		this.testCase = testCase;
	}
	
	public String getTestMethod() {
		return testMethod;
	}

	public void setTestMethod(String testMethod) {
		this.testMethod = testMethod;
	}

	public String getCategoryStart() {
		return categoryStart;
	}

	public void setCategoryStart(String categoryStart) {
		this.categoryStart = categoryStart;
	}

	public String getCategoryEnd() {
		return categoryEnd;
	}

	public void setCategoryEnd(String categoryEnd) {
		this.categoryEnd = categoryEnd;
	}

	public String getCategoryEachStart() {
		return categoryEachStart;
	}

	public void setCategoryEachStart(String categoryEachStart) {
		this.categoryEachStart = categoryEachStart;
	}

	public String getCategoryEachSeparator() {
		return categoryEachSeparator;
	}

	public void setCategoryEachSeparator(String categoryEachSeparator) {
		this.categoryEachSeparator = categoryEachSeparator;
	}

	public String getCategoryEachEnd() {
		return categoryEachEnd;
	}

	public void setCategoryEachEnd(String categoryEachEnd) {
		this.categoryEachEnd = categoryEachEnd;
	}

	public String getSetUpOnce() {
		return setUpOnce;
	}

	public void setSetUpOnce(String setUpOnce) {
		this.setUpOnce = setUpOnce;
	}

	public String getTearDownOnce() {
		return tearDownOnce;
	}

	public void setTearDownOnce(String tearDownOnce) {
		this.tearDownOnce = tearDownOnce;
	}

	public String getSetUp() {
		return setUp;
	}

	public void setSetUp(String setUp) {
		this.setUp = setUp;
	}

	public String getTearDown() {
		return tearDown;
	}

	public void setTearDown(String tearDown) {
		this.tearDown = tearDown;
	}
	
}
