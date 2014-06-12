package org.funtester.plugin.report.testng;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

/**
 * TestNG XML report test method
 * 
 * @author Thiago Delgado Pinto
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"name", "durationMillis", "startedAt", "finishedAt", "status", "signature", "isConfig", "exception"})
public class TestNGXmlReportTestMethod {

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

	@XmlAttribute
	private String status;
	
	@XmlAttribute
	private String signature;
	
	@XmlAttribute(name="is-config")
	private boolean isConfig;
	
	@XmlElement
	private TestNGXmlReportException exception;
	
	public TestNGXmlReportTestMethod() {		
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public boolean isConfig() {
		return isConfig;
	}

	public void setConfig(boolean isConfig) {
		this.isConfig = isConfig;
	}

	public TestNGXmlReportException getException() {
		return exception;
	}

	public void setException(TestNGXmlReportException exception) {
		this.exception = exception;
	}

	@Override
	public String toString() {
		return "{"
			+ " name: \"" + ( name != null ? name : "null" ) + "\""
			+ ", durationMillis: " + durationMillis
			+ ", startedAt: \"" + ( startedAt != null ? startedAt.toString() : "null" ) + "\""
			+ ", finishedAt: \"" + ( finishedAt != null ? finishedAt.toString() : "null" ) + "\""
			+ ", status: \"" + ( status != null ? status : "null" ) + "\""
			+ ", signature: \"" +( signature != null ? signature : "null" ) + "\""
			+ ", isConfig: " + isConfig
			+ ", exception: " + ( exception != null ? exception.toString() : "null" )
			+ " }"
			;
	}
}
