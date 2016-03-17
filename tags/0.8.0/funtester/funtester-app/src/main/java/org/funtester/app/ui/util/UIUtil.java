package org.funtester.app.ui.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JComponent;

/**
 * User interface utilities
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class UIUtil {
	
	private UIUtil() {}

	/**
	 * Center a component on the screen.
	 * 
	 * @param component the component to center.
	 */
	public static void centerOnScreen(Component component) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = component.getWidth();
		int height = component.getHeight();
	    int x = ( screen.width - width ) / 2;
	    int y = ( screen.height - height ) / 2;
	    component.setBounds( x, y, width, height );
	}
	
	/**
	 * Find a component by name, inside a parent component. 
	 * @param parent	the parent component.
	 * @param name		the name of the component to find.
	 * @return			the component or null if not found.
	 */
	public static Component findComponentWithName(
			final JComponent parent,
			final String name
			) {
		if ( null == parent || null == name || name.isEmpty() ) { return null; }
		final Component CA[] = parent.getComponents();
		if ( null == CA || CA.length < 1 ) { return null; }
		for ( Component c : CA ) {
			if ( null == c.getName() ) continue; // Ignore a component without a name
			//System.out.println( "current component is >>> \"" + c.getName() + "\" and its type is >>> " + c.getClass().getName() );
			if ( c.getName().equalsIgnoreCase( name ) ) {
				return c;
			}
		}
		return null;
	}
}
