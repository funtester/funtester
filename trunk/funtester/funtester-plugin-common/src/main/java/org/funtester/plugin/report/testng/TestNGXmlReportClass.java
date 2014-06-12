package org.funtester.plugin.report.testng;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * TestNG XML report class
 * 
 * @author Thiago Delgado Pinto
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"name", "testMethods"})
public class TestNGXmlReportClass {

	@XmlAttribute
	private String name;	
	
	@XmlElement(name="test-method")
	private TestNGXmlReportTestMethod[] testMethods;
	
	public TestNGXmlReportClass() {		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public TestNGXmlReportTestMethod[] getTestMethods() {
		return testMethods;
	}

	public void setTestMethods(TestNGXmlReportTestMethod[] testMethods) {
		this.testMethods = testMethods;
	}

	// FROM Object	
	@Override
	public String toString() {
		String text =
			"{" 
			+ " name: \"" + ( ( name != null ) ? name : "null" ) + "\"" 
			+ ", testMethods: "
			;
		if ( testMethods != null ) {
			text += "\n\t\t[";
			for ( TestNGXmlReportTestMethod method : testMethods ) {
				text += "\n\t\t\t" + method.toString() + ", ";
			}
			text += "\n\t\t]";			
		} else {
			text += "null";
		}
		text += " }";

		return text;
	}
}
