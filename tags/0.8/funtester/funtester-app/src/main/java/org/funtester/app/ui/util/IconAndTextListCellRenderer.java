package org.funtester.app.ui.util;

import java.awt.Component;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * Allows combobox's items to have a image icon + text.
 * 
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <KeyTypeForTheMap>	The key type for the map.
 */
public class IconAndTextListCellRenderer< KeyTypeForTheMap >
	extends DefaultListCellRenderer {
	
	private static final long serialVersionUID = 8600636957425222012L;
	
    private Map< KeyTypeForTheMap, Icon > iconMap = null;  
    
    /**
     * The constructor needs to receive the icon map. This map will be used
     * to set the icon in the label used for the combobox's cell. 
     * 
     * @param iconMap	The map used to get the icon.
     */
    public IconAndTextListCellRenderer(Map< KeyTypeForTheMap, Icon > iconMap) {  
        this.iconMap = iconMap;  
    } 	

	@SuppressWarnings("rawtypes")
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {		
		// Get the label that represents each cell on the combobox  
		JLabel label = (JLabel) super.getListCellRendererComponent(
				list, value, index, isSelected, cellHasFocus );		
		if ( null == label ) {
			return null;
		}
		Icon icon = iconMap.get( value ); // value is used as the key  
		// Now, places the icon
		label.setIcon( icon );
		return label;
	}
}
