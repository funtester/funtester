package org.funtester.plugin.code;

import java.util.ArrayList;
import java.util.List;

/**
 * Test case content
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TestCase {
	
	private List< String > headerComments = new ArrayList< String >();
	private String namespace = "";
	private List< String > imports = new ArrayList< String >();
	private List< String > comments = new ArrayList< String >();
	private List< String > categories = new ArrayList< String >();
	private String name = "";
	private List< Variable > attributes = new ArrayList< Variable >();
	private List< HelperMethod > helperMethods = new ArrayList< HelperMethod >();
	private List< String > setUpOnceCommands = new ArrayList< String >();
	private List< String > tearDownOnceCommands = new ArrayList< String >();
	private List< String > setUpCommands = new ArrayList< String >();
	private List< String > tearDownCommands = new ArrayList< String >();
	private List< TestMethod > methods = new ArrayList< TestMethod >();
	
	public TestCase() {
	}
	
	//
	// BUILDERS 
	//
	
	public TestCase addHeaderComment(final String value) {
		headerComments.add( value );
		return this;
	}
	
	public TestCase withNamespace(final String value) {
		setNamespace( value );
		return this;
	}
	
	public TestCase addImport(final String value) {
		imports.add( value );
		return this;
	}
	
	public TestCase addComment(final String value) {
		comments.add( value );
		return this;
	}
	
	public TestCase addCategory(final String value) {
		categories.add( value );
		return this;
	}
	
	public TestCase withName(final String value) {
		setName( value );
		return this;
	}
	
	public TestCase addAttribute(final Variable value) {
		attributes.add( value );
		return this;
	}
	
	public TestCase addHelperMethod(final HelperMethod value) {
		helperMethods.add( value );
		return this;
	}
	
	public TestCase addSetUpOnceCommand(final String value) {
		setUpOnceCommands.add( value );
		return this;
	}
	
	public TestCase addTearDownOnceCommand(final String value) {
		tearDownOnceCommands.add( value );
		return this;
	}
	
	public TestCase addSetUpCommand(final String value) {
		setUpCommands.add( value );
		return this;
	}
	
	public TestCase addTearDownCommand(final String value) {
		tearDownCommands.add( value );
		return this;
	}
	
	public TestCase addMethod(final TestMethod value) {
		methods.add( value );
		return this;
	}
	
	//
	// GETTERS AND SETTERS
	//

	public List< String > getHeaderComments() {
		return headerComments;
	}

	public void setHeaderComments(List< String > headerComments) {
		this.headerComments = headerComments;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public List< String > getImports() {
		return imports;
	}

	public void setImports(List< String > imports) {
		this.imports = imports;
	}

	public List< String > getComments() {
		return comments;
	}

	public void setComments(List< String > comments) {
		this.comments = comments;
	}

	public List< String > getCategories() {
		return categories;
	}

	public void setCategories(List< String > categories) {
		this.categories = categories;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List< Variable > getAttributes() {
		return attributes;
	}

	public void setAttributes(List< Variable > attributes) {
		this.attributes = attributes;
	}

	public List< HelperMethod > getHelperMethods() {
		return helperMethods;
	}

	public void setHelperMethods(List< HelperMethod > helperMethods) {
		this.helperMethods = helperMethods;
	}

	public List< String > getSetUpOnceCommands() {
		return setUpOnceCommands;
	}

	public void setSetUpOnceCommands(List< String > setUpOnceCommands) {
		this.setUpOnceCommands = setUpOnceCommands;
	}

	public List< String > getTearDownOnceCommands() {
		return tearDownOnceCommands;
	}

	public void setTearDownOnceCommands(List< String > tearDownOnceCommands) {
		this.tearDownOnceCommands = tearDownOnceCommands;
	}

	public List< String > getSetUpCommands() {
		return setUpCommands;
	}

	public void setSetUpCommands(List< String > setUpCommands) {
		this.setUpCommands = setUpCommands;
	}

	public List< String > getTearDownCommands() {
		return tearDownCommands;
	}

	public void setTearDownCommands(List< String > tearDownCommands) {
		this.tearDownCommands = tearDownCommands;
	}

	public List< TestMethod > getMethods() {
		return methods;
	}

	public void setMethods(List< TestMethod > methods) {
		this.methods = methods;
	}
	
}
