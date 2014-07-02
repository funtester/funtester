package org.funtester.app.ui.software;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.funtester.app.ui.common.DefaultEditingDialog;
import org.funtester.app.ui.common.EnumTranslation;
import org.funtester.app.ui.util.UIValidationHelper;
import org.funtester.app.validation.BusinessRuleValidator;
import org.funtester.app.validation.ValueConfigurationValidator;
import org.funtester.common.util.Validator;
import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.software.BusinessRule;
import org.funtester.core.software.BusinessRuleType;
import org.funtester.core.software.Element;
import org.funtester.core.software.Software;
import org.funtester.core.software.ValueConfiguration;
import org.funtester.core.software.ValueType;
import org.funtester.core.util.InvalidValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import org.funtester.app.i18n.Messages;

/**
 * Dialog for editing a {@link BusinessRule}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class BusinessRuleDialog extends DefaultEditingDialog< BusinessRule > {

	private static final long serialVersionUID = -8836385954054491341L;
	private static final Logger logger = LoggerFactory.getLogger( BusinessRule.class );
	
	private final ValueType valueType;
	private final Collection< BusinessRule > otherBusinessRules;
	
	private final JTextField message;
	private final JComboBox businessRuleType;
	
	private final JLabel lblValueConfigurationKind;
	private final VCSelectionPanel vcSelectionPanel;
	
	public BusinessRuleDialog(
			final ValueType valueType,
			Collection< BusinessRule > otherBusinessRules,
			Collection< Element > elements,
			Software software,
			final DriverCache driverCache
			) {
		this.valueType = valueType;
		this.otherBusinessRules = otherBusinessRules;
		
		setTitle(Messages.getString("BusinessRuleDialog.this.title")); //$NON-NLS-1$
		setBounds( 100, 100, 743, 413 );
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,}));
		
		JLabel lblBusinessRuleType = new JLabel(Messages.getString("BusinessRuleDialog.lblBusinessRuleType.text")); //$NON-NLS-1$
		contentPanel.add(lblBusinessRuleType, "2, 2, right, default");
		
		final List< String > businessRuleTypeValues = EnumTranslation.createList(
				BusinessRuleType.class );
		
		businessRuleType = new JComboBox( new DefaultComboBoxModel(
				businessRuleTypeValues.toArray( new String[ 0 ] ) ) );
		businessRuleType.setName("businessRuleType");
		
		businessRuleType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if ( e.getStateChange() == ItemEvent.SELECTED ) {
					int index = businessRuleType.getSelectedIndex();
					if ( index < 0 ) { return; }
					BusinessRuleType brType = BusinessRuleType.values()[ index ];
					vcSelectionPanel.setBusinessRuleType( brType );
				}
			}
		});

		contentPanel.add(businessRuleType, "4, 2, 5, 1, fill, default");
		
		lblValueConfigurationKind = new JLabel(Messages.getString("BusinessRuleDialog.lblValueConfigurationKind.text")); //$NON-NLS-1$
		contentPanel.add(lblValueConfigurationKind, "2, 4, default, top");
		
		vcSelectionPanel = new VCSelectionPanel( valueType, elements, software, driverCache );
		contentPanel.add(vcSelectionPanel, "4, 4, 5, 1, fill, fill");
		
		JLabel lblMessage = new JLabel(Messages.getString("BusinessRuleDialog.lblMessage.text")); //$NON-NLS-1$
		contentPanel.add(lblMessage, "2, 6, right, default");
		
		message = new JTextField();
		message.setText( "" );
		contentPanel.add(message, "4, 6, 5, 1, fill, default");
		message.setColumns(10);
	}

	@Override
	protected BusinessRule createObject() {
		return new BusinessRule();
	}

	@Override
	protected boolean populateObject() {
		BusinessRule obj = getObject();
		obj.setMessage( message.getText() );
		
		if ( businessRuleType.getSelectedIndex() >= 0 ) {
			logger.debug( "setting VC in the BR....");
			
			BusinessRuleType brType = BusinessRuleType.values()[ businessRuleType.getSelectedIndex() ];
			obj.setType( brType );
			
			ValueConfiguration vc;
			try {
				vc = vcSelectionPanel.getValueConfiguration();
			} catch ( InvalidValueException ive ) {
				UIValidationHelper.handleInvalidValueException(
						getRootPane(), getTitle(), ive );
				return false;
			} catch ( Exception e ) {
				UIValidationHelper.handleException( getRootPane(), getTitle(), e );
				return false;
			}
			logger.debug( " vc is " + ( vc != null ? vc.toString() : "null" ) );
			obj.setValueConfiguration( vc );
		} else {
			logger.debug( "vc undefined VC in the BR!");
		}
		
		return true;
	}

	@Override
	protected void drawObject(final BusinessRule obj) {
		message.setText( obj.getMessage() );
		
		businessRuleType.setSelectedItem( EnumTranslation.translationForItem(
			BusinessRuleType.class, obj.getType() ) );

		vcSelectionPanel.setValueConfiguration( obj.getValueConfiguration() );
	}

	@Override
	protected Validator< BusinessRule > createValidator() {
		ValueConfigurationValidator vcValidator = new ValueConfigurationValidator( valueType );
		return new BusinessRuleValidator( vcValidator, otherBusinessRules );
	}
	
}
