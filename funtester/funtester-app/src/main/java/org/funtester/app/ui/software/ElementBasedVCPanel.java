package org.funtester.app.ui.software;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.funtester.core.software.Element;
import org.funtester.core.software.ElementBasedVC;
import org.funtester.core.software.ValueConfiguration;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import org.funtester.app.i18n.Messages;
/**
 * Panel for an {@link ElementBasedVC}. 
 * 
 * @author Thiago Delgado Pinto
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ElementBasedVCPanel extends VCPanel {

	private static final long serialVersionUID = -4038302367089540858L;
	
	private final ElementBasedVC valueConfiguration = new ElementBasedVC();
	
	private final JComboBox element;

	public ElementBasedVCPanel(Collection< Element > elements) {
		
		// Choose just the editable elements 
		final List< Element > editableElements = new ArrayList< Element >();
		for ( Element e : elements ) {
			if ( e.isEditable() ) {
				editableElements.add( e );
			}
		}
		
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblElement = new JLabel(Messages.getString("ElementBasedVCPanel.lblElement.text")); //$NON-NLS-1$
		add(lblElement, "2, 2, right, default");
		
		element = new JComboBox( new DefaultComboBoxModel(
				editableElements.toArray( new Element[ 0 ] ) ) );
		element.setName("element");
		add(element, "4, 2, fill, default");
	}

	@Override
	public ValueConfiguration getValueConfiguration() {
		Element e = ( element.getSelectedItem() instanceof Element )
				? (Element) element.getSelectedItem() : null;
		valueConfiguration.setReferencedElement( e );
		return valueConfiguration;
	}
	
	@Override
	public void setValueConfiguration(ValueConfiguration obj) {
		valueConfiguration.copy( obj );
		drawVC();
	}

	private void drawVC() {
		element.setSelectedItem( valueConfiguration.getReferencedElement() );
	}

}
