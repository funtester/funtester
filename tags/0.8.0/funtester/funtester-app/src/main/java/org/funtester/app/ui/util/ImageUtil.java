package org.funtester.app.ui.util;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Image utilities.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ImageUtil {
	
	private ImageUtil() {}
	
	/**
	 * Loads an icon.
	 * 
	 * @param path	Icon path
	 * @return		The loaded icon or null if error
	 */
	public static Icon loadIcon(String path) {
		try {
			return new ImageIcon( classLoader().getResource( path ) );
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Loads an image.
	 * 
	 * @param path	Image path
	 * @return		The loaded image or null if error
	 */
	public static Image loadImage(String path) {
		try {
			return Toolkit.getDefaultToolkit().getImage( classLoader().getResource( path ) );
		} catch (Exception e) {
			return null;
		}			
	}
	
	public static ClassLoader classLoader() {
		return ImageUtil.class.getClassLoader();
	}

}
