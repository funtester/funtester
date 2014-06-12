package org.funtester.app.ui.util;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JToolBar;

public class ToolBarUtil {
	
	private ToolBarUtil() {}
	
	/**
	 * Remove the text from each button IF the icon is set.
	 * 
	 * @param bar	the tool bar.
	 */
	public static void removeTextFromButtonsWithIcon(JToolBar bar) {
		for ( Component c : bar.getComponents() ) {
			if ( c instanceof JButton ) {
				JButton button = (JButton) c;
				if ( button.getIcon() != null ) {
					button.setText( "" );
				}
			} else if ( c instanceof JToolBar ) { // A toolbar inside another
				removeTextFromButtonsWithIcon( (JToolBar) c );
			}
		}
	}

}
