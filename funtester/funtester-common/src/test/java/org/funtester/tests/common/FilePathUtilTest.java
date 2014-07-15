package org.funtester.tests.common;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.funtester.common.util.FilePathUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test the {@link FilePathUtil} class.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class FilePathUtilTest {

	private final String currentDir = "/one/two/three/four";
	
	@DataProvider(name="innerPaths")
	public Object[][] innerPaths() {
		return new Object[][] {
			// CurrentDir, Expected, Path
			{ currentDir, "file.ext", "/one/two/three/four/file.ext" },
			{ currentDir, "five/file.ext", "/one/two/three/four/five/file.ext" },
			{ currentDir, "five/six/file.ext", "/one/two/three/four/five/six/file.ext" },
			{ currentDir, "five/six/seven/file.ext", "/one/two/three/four/five/six/seven/file.ext" },
			{ currentDir, "five/six/seven/eight/file.ext", "/one/two/three/four/five/six/seven/eight/file.ext" },
		};
	}
	
	@Test(dataProvider="innerPaths")
	public void shouldConvertToRelativePath(
			String currentDir, String expected, String path) {
		
		String result = FilePathUtil.toRelativePath( path, currentDir );
		assertEquals( result, expected );
	}
	
	@Test( dataProvider="innerPaths" )
	public void shouldConvertFromRelativePath(
			String currentDir, String path, String expected) {
		
		String result = FilePathUtil.toAbsolutePath( path, currentDir );	
		assertEquals( result, expected );
	}
	
	
	@DataProvider(name="outerPaths")
	public Object[][] outerPaths() {
		return new Object[][] {
			// Current dir, Path, Expected
			{ currentDir, "/one/two/three/four/file.ext", "file.ext" },
			{ currentDir, "/one/two/three/file.ext", "../file.ext" },
			{ currentDir, "/one/two/file.ext", "../../file.ext" },
			{ currentDir, "/one/file.ext", "../../../file.ext" },
			{ currentDir, "/file.ext", "../../../../file.ext" },
		};
	}
	
	@Test(dataProvider="outerPaths")
	public void makeRelativePathForOuterPathCorrectly(
			String currentDir, String path, String expected) {
		
		String result = FilePathUtil.toRelativePath( path, currentDir );
		assertEquals( result, expected );
	}
	
	@Test(dataProvider="outerPaths")
	public void makeOuterPathFromRelativePathCorrectly(
			String currentDir, String expected, String path) {
		
		String result = FilePathUtil.toAbsolutePath( path, currentDir );
		assertEquals( result, expected );
	}

	
	@DataProvider(name="dirAndFileCombinations")
	public Object[][] dirAndFileCombinations() {
		return new Object[][] {
			{ "", "file.ext", "/file.ext" },
			{ "", "/file.ext", "/file.ext" },
			{ "/", "file.ext", "/file.ext" },
			{ "/", "/file.ext", "/file.ext" },
			{ "/one", "file.ext", "/one/file.ext" },
			{ "/one", "/file.ext", "/one/file.ext" },
			{ "/one/", "file.ext", "/one/file.ext" },
			{ "/one/", "/file.ext", "/one/file.ext" },
		};
	}
	
	@Test( dataProvider="dirAndFileCombinations" )
	public void makeFileNameCorrectly(String directory, String fileName, String expected) {
		String result = FilePathUtil.makeFileName( directory, fileName );
		assertEquals( result, expected );
	}
	
	@DataProvider(name="mixedPaths")
	public Object[][] mixedPaths() {
		return new Object[][] {
			{ "one/two/a", "../b/file.ext", "one/two/b/file.ext" },
		};
	}

	@Test( dataProvider="mixedPaths" )
	public void mixFileNameCorrectly(String directory, String fileName, String expected) {
		String result = FilePathUtil.toAbsolutePath( fileName, directory );
		assertEquals( result, expected );
	}

}
