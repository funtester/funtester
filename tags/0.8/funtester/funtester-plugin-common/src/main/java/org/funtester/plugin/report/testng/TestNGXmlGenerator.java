package org.funtester.plugin.report.testng;

import java.util.Map;
import java.util.TreeMap;

import org.funtester.common.at.AbstractTestCase;
import org.funtester.common.at.AbstractTestSuite;

/**
 * TestNG XML generator
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TestNGXmlGenerator {
	
	public static final String FILE_NAME = "testng.xml";
	
	private static final String TEST_NAME = "funtester";

	
	private static final String HEADER = "<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\" >";	
	private static final String ENDL = "\n";
	
	private int level = 0;
	
	public Map< String, StringBuilder > generate(
			final String classPackage,
			final AbstractTestSuite suite
			) {
		Map< String, StringBuilder > map = new TreeMap< String, StringBuilder >();
		StringBuilder sb = new StringBuilder();
		
		sb.append( HEADER ).append( ENDL );
		sb.append( suiteStart( suite.getName() ) ).append( ENDL );
		
		sb.append( testStart( TEST_NAME ) ).append( ENDL );
		sb.append( classListStart() ).append( ENDL );
		for ( AbstractTestCase stc : suite.getTestCases() ) {
			sb.append( newClass( makeClassName( classPackage, stc.getName() ) ) ).append( ENDL );
		}	
		sb.append( classListEnd() ).append( ENDL );
		sb.append( testEnd() ).append( ENDL );
		
		sb.append( suiteEnd() );
		
		map.put( suite.getName(), sb );
		return map;
	}
	
	
	private String makeClassName(final String testPackage, final String className) {
		if ( testPackage.isEmpty() ) {
			return className;
		}
		return testPackage + "." + className;
	}

	// TESTNG XML RELATED
	
	private String suiteStart(final String name) {
		String text = levelStr() + "<suite name=\"" + name + "\" verbose=\"1\" >";
		increaseLevel();
		return text;
	}
	
	private String suiteEnd() {
		decreaseLevel();
		return levelStr() + "</suite>";
	}
	
	private String testStart(final String name) {
		String text = levelStr() + "<test name=\"" + name + "\" >";
		increaseLevel();
		return text;
	}
	
	private String testEnd() {
		decreaseLevel();
		return levelStr() + "</test>";
	}
	
	private String classListStart() {
		String text = levelStr() + "<classes>";
		increaseLevel();
		return text;
	}
	
	private String classListEnd() {
		decreaseLevel();
		return levelStr() + "</classes>";
	}
	
	private String newClass(final String fullClassName) {
		return levelStr() + "<class name=\"" + fullClassName + "\" />";
	}
	
	// UTILITIES
	
	private String levelStr() {
		StringBuilder b = new StringBuilder();
		for ( int i = 0; ( i < level ); ++i) {
			b.append( "\t" );
		}
		return b.toString();
	}
	
	private void increaseLevel() {
		level++;
	}
	
	private void decreaseLevel() {
		level--;
	}
	
/*
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >
  
<suite name="Suite1" verbose="1" >
  <test name="Nopackage" >
    <classes>
       <class name="NoPackageTest" />
    </classes>
  </test>
 
  <test name="Regression1">
    <classes>
      <class name="test.sample.ParameterSample"/>
      <class name="test.sample.ParameterTest"/>
    </classes>
  </test>
</suite>
 
 */

}
