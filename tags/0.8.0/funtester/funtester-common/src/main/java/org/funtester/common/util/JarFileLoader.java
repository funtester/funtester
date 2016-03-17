package org.funtester.common.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * JAR file loader.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class JarFileLoader extends URLClassLoader {

	public JarFileLoader() {
		super( new URL[0] );
	}
	
	public JarFileLoader(URL[] arg0) {
		super( arg0 );
	}

	public JarFileLoader(URL[] arg0, ClassLoader arg1) {
		super( arg0, arg1 );
	}

	public JarFileLoader(URL[] arg0, ClassLoader arg1,
			URLStreamHandlerFactory arg2) {
		super( arg0, arg1, arg2 );
	}

	public void addJarFile(final String path) throws MalformedURLException {
		final String JAR_PREFIX = "jar:file:///";		
		final String JAR_SUFFIX = "!/";		
		String urlPath = path;
		// Add prefix if not exists
		if ( ! urlPath.startsWith( JAR_PREFIX ) ) {
			urlPath = JAR_PREFIX + urlPath;
		}
		// Add suffix if not exists		
		if ( ! urlPath.endsWith( JAR_SUFFIX ) ) {
			urlPath = urlPath + JAR_SUFFIX;
		}
		// Try to add the path
		addURL( new URL( urlPath ) );			
	}
	
	
	public void addClassFile(final String path) throws MalformedURLException {
		final String FILE_PREFIX = "file:///";		
		//final String FILE_SUFFIX = "";		
		String urlPath = path;
		// Add prefix if not exists
		if ( ! urlPath.startsWith( FILE_PREFIX ) ) {
			urlPath = FILE_PREFIX + urlPath;
		}
		/*
		// Add suffix if not exists		
		if ( ! urlPath.endsWith( FILE_SUFFIX ) ) {
			urlPath = urlPath + FILE_SUFFIX;
		}
		*/
		// Try to add the path
		addURL( new URL( urlPath ) );			
	}
	
	/*
	public static void main(String []args) {
		//URL urls [] = {};
		//JarFileLoader l = new JarFileLoader( urls );
		JarFileLoader l = new JarFileLoader();
		try {
			l.addJarFile( "C:\\dev\\workspace\\suct_branch_step\\src\\eclipse\\miglayout15-swing.jar" );
			System.out.println( "OK" );
		} catch ( MalformedURLException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/	
}
