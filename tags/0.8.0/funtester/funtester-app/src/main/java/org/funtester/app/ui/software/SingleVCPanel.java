package org.funtester.app.ui.software;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.funtester.core.software.SingleVC;
import org.funtester.core.software.ValueConfiguration;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import org.funtester.app.i18n.Messages;

/**
 * Panel for a {@link SingleVC}. 
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class SingleVCPanel extends VCPanel {

	private static final long serialVersionUID = -4038302367089540858L;
	
	private final SingleVC valueConfiguration = new SingleVC();
	
	private JTextField value;

	/**
	 * Create the panel.
	 */
	public SingleVCPanel() {
		
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblValue = new JLabel(Messages.getString("SingleVCPanel.lblValue.text")); //$NON-NLS-1$
		add(lblValue, "2, 2, right, default");
		
		value = new JTextField();
		value.setName("value");
		add(value, "4, 2, fill, default");
		value.setColumns(10);
	}

	@Override
	public ValueConfiguration getValueConfiguration() {
		valueConfiguration.setValue( value.getText() );
		return valueConfiguration;
	}
	
	@Override
	public void setValueConfiguration(ValueConfiguration obj) {
		valueConfiguration.copy( obj );
		drawVC();
	}

	private void drawVC() {
		value.setText( valueConfiguration.getValue().toString() );
	}
	
}
