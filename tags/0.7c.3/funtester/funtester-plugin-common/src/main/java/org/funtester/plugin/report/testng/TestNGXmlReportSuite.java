package org.funtester.plugin.report.testng;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

/**
 * TestNG XML report suite
 * 
 * @author Thiago Delgado Pinto
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"name", "durationMillis", "startedAt", "finishedAt", "groups", "tests"})
public class TestNGXmlReportSuite {
	
	@XmlAttribute
	private String name;	
	
	@XmlAttribute(name="duration-ms")
	private long durationMillis;	
	
	@XmlJavaTypeAdapter(DateTimeXmlAdapter.class)
	@XmlAttribute(name="started-at")
	private DateTime startedAt;	
	
	@XmlJavaTypeAdapter(DateTimeXmlAdapter.class)
	@XmlAttribute(name="finished-at")
	private DateTime finishedAt;
	
	@XmlElement(name="groups")
	private String groups;
		
	@XmlElement(name="test")
	private TestNGXmlReportTest[] tests;

	public TestNGXmlReportSuite() {		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDurationMillis() {
		return durationMillis;
	}

	public void setDurationMillis(long durationMillis) {
		this.durationMillis = durationMillis;
	}

	public DateTime getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(DateTime startedAt) {
		this.startedAt = startedAt;
	}

	public DateTime getFinishedAt() {
		return finishedAt;
	}

	public void setFinishedAt(DateTime finishedAt) {
		this.finishedAt = finishedAt;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}
		
	public TestNGXmlReportTest[] getTests() {
		return tests;
	}

	public void setTests(TestNGXmlReportTest[] tests) {
		this.tests = tests;
	}

	// FROM Object
	@Override
	public String toString() {
		String text =
			"{"
			+ " name: \"" + ( name != null ? name : "null" ) + "\""
			+ ", durationMillis: " + durationMillis
			+ ", startedAt: \"" + ( startedAt != null ? startedAt.toString() : "null" ) + "\""
			+ ", finishedAt: \"" + ( finishedAt != null ? finishedAt.toString() : "null" ) + "\""  
			+ ", groups: \"" + ( ( groups != null ) ? groups : "null" ) + "\""
			+ ", tests: "
			;
		if ( tests != null ) {
			text += "\n[";
			for ( TestNGXmlReportTest test : tests ) {
				text += "\n\t" + test.toString() + ", ";
			}
			text += "\n]";
		} else {
			text += "null";
		}
		text += " }";
		
		return text;
	}
}
