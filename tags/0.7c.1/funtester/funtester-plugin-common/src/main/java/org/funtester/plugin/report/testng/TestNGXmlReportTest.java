package org.funtester.plugin.report.testng;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

/**
 * TestNG XML report test
 * 
 * @author Thiago Delgado Pinto
 *
 */
@XmlType(propOrder={"name", "durationMillis", "startedAt", "finishedAt", "classes"})
@XmlAccessorType(XmlAccessType.FIELD)
public class TestNGXmlReportTest {
	
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

	@XmlElement(name="class")
	private TestNGXmlReportClass[] classes; 
	
	public TestNGXmlReportTest() {		
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

	public TestNGXmlReportClass[] getClasses() {
		return classes;
	}

	public void setClasses(TestNGXmlReportClass[] classes) {
		this.classes = classes;
	}

	// FROM Object	
	@Override
	public String toString() {
		String text = "{"
			+ " name: \"" + ( name != null ? name : "null" ) + "\""
			+ ", durationMillis: " + durationMillis
			+ ", startedAt: \"" + ( startedAt != null ? startedAt.toString() : "null" ) + "\""
			+ ", finishedAt: \"" + ( finishedAt != null ? finishedAt.toString() : "null" ) + "\""			
			+ ", classes: "
			;
		if ( classes != null ) {
			text += "\n\t[";
			for ( TestNGXmlReportClass clazz : classes ) {
				text += "\n\t\t" + clazz.toString() + ", ";
			}
			text += "\n\t]";
		} else {	
			text += "null";
		}		
		text += " }";
		return text;
	}
}
