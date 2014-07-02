package org.funtester.tests;

import static org.testng.Assert.assertEquals;

import org.funtester.app.project.Version;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test for {@link Version}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class VersionTest {
	
	
	@DataProvider(name="versions")
	public Object[][] versions() {
		return new Object[][] {
			// Equal
			{ null, null, null, null, null, null, 0 },
			{ "", "", "", "", "", "", 0 },
			{ "1", "0", "0", "1", "0", "0", 0 },
			
			// Major than
			{ "1", "0", "1", "1", "0", "0", 1 },
			{ "1", "1", "0", "1", "0", "0", 1 },
			{ "2", "0", "0", "1", "0", "0", 1 },
			{ "1", "0", "a", "1", "0", null, 1 },
			{ "1", "0", "b", "1", "0", "a", 1 },
			
			// Lesser than
			{ "1", "0", "0", "2", "0", "0", -1 },
			{ "1", "0", "0", "1", "0", "1", -1 },
			{ "1", "0", "0", "1", "1", "0", -1 },
			{ "1", "0", null, "1", "0", "a", -1 },
			{ "1", "0", "a", "1", "0", "b", -1 }
		};
	}

	@Test( dataProvider="versions" )
	public void compareCorreclty(
			final String major1,
			final String minor1,
			final String release1,
			final String major2,
			final String minor2,
			final String release2,
			final int expected
			) {
		Version va = new Version( major1, minor1, release1 );
		Version vb = new Version( major2, minor2, release2 );
		
		int result = va.compareTo( vb );
		
		assertEquals( result, expected );
	}
	
	@Test
	public void correctStringFormat() {
		Version v = new Version( "1", "2", "3" );
		assertEquals( v.toString(), "1.2.3" );
	}
	
}
