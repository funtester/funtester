package org.funtester.app.ui.software;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.funtester.app.ui.software.actions.RegExActionContainer;
import org.funtester.core.software.RegEx;
import org.funtester.core.software.RegExBasedVC;
import org.funtester.core.software.Software;
import org.funtester.core.software.ValueConfiguration;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import org.funtester.app.i18n.Messages;

/**
 * Panel for a {@link RegExBasedVC}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class RegExBasedVCPanel extends VCPanel {

	private static final long serialVersionUID = -4038302367089540858L;

	private final Software software;
	private final JComboBox regEx;
	private final JButton newButton;
	
	private final RegExBasedVC valueConfiguration = new RegExBasedVC();
	private JLabel lblExpression;
	private JTextField expression;

	public RegExBasedVCPanel(
			Software software
			) {
		this.software = software;
		
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblRegEx = new JLabel(Messages.getString("RegExBasedVCPanel.lblRegEx.text")); //$NON-NLS-1$
		add(lblRegEx, "2, 2, right, default");
		
		regEx = new JComboBox( new DefaultComboBoxModel(
				software.getRegularExpressions().toArray( new RegEx[ 0 ] ) ) );
		regEx.setName("regEx");
		
		regEx.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if ( e.getStateChange() == ItemEvent.SELECTED ) {
					RegEx obj = (RegEx) regEx.getSelectedItem();
					if ( null == obj) { return; }
					expression.setText( obj.getExpression() );
				}
			}
		});
		
		add(regEx, "4, 2, fill, default");
		
		newButton = new JButton(Messages.getString("RegExBasedVCPanel.newButton.text")); //$NON-NLS-1$
		newButton.setName("newButton");
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createRegEx();
			}
		});
		add(newButton, "6, 2");
		
		lblExpression = new JLabel(Messages.getString("RegExBasedVCPanel.lblExpression.text")); //$NON-NLS-1$
		lblExpression.setEnabled(false);
		add(lblExpression, "2, 4, right, default");
		
		expression = new JTextField();
		expression.setEnabled(false);
		add(expression, "4, 4, fill, default");
		expression.setColumns(10);
		if ( regEx.getSelectedItem() != null ) {
			expression.setText(
					( (RegEx) regEx.getSelectedItem() ).getExpression() );
		}
	}

	@Override
	public ValueConfiguration getValueConfiguration() {
		RegEx obj = ( regEx.getSelectedItem() instanceof RegEx )
				? (RegEx) regEx.getSelectedItem() : null;
		valueConfiguration.setRegEx( obj );
		return valueConfiguration;
	}
	
	@Override
	public void setValueConfiguration(ValueConfiguration obj) {
		valueConfiguration.copy( obj );
		drawVC();
	}

	private void drawVC() {
		RegEx obj = valueConfiguration.getRegEx();
		regEx.setSelectedItem( obj );
		expression.setText( obj.getExpression() );
	}
	
	private void createRegEx() {
		RegEx obj = RegExActionContainer.createRegEx( software );
		if ( null == obj ) { return; }
		software.addRegularExpression( obj );
		
		// Update the combobox
		regEx.setModel( new DefaultComboBoxModel(
			software.getRegularExpressions().toArray( new RegEx[ 0 ] ) ) );
	}

}
