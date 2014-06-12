package org.funtester.app.ui.software;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.DefaultEditingDialog;
import org.funtester.app.ui.common.EnumTranslation;
import org.funtester.app.ui.software.actions.BusinessRuleActionContainer;
import org.funtester.app.ui.util.BaseTableModel;
import org.funtester.app.ui.util.CRUDTablePanel;
import org.funtester.app.ui.util.JTableUtil;
import org.funtester.app.validation.ElementValidator;
import org.funtester.common.util.Validator;
import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.profile.ElementType;
import org.funtester.core.software.BusinessRule;
import org.funtester.core.software.BusinessRuleType;
import org.funtester.core.software.Element;
import org.funtester.core.software.Software;
import org.funtester.core.software.ValueType;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Dialog for editing an {@link Element}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ElementDialog extends DefaultEditingDialog< Element > {

	private static final long serialVersionUID = -5597642436141083104L;

	private final BusinessRuleActionContainer brAC;
	
	private final JTextField name;
	private final JTextField internalName;
	private final JComboBox elementType;
	private final JCheckBox editable;
	private final JComboBox valueType;
	private final CRUDTablePanel businessRulesContentPanel;
	
	
	public ElementDialog(
			final Software software,
			final Collection< ElementType > elementTypes,
			final Collection< Element > elements,
			final DriverCache driverCache
			) {
		
		super();
		
		setTitle(Messages.getString("ElementDialog.this.title")); //$NON-NLS-1$
		//setIconImage( ImageUtil.loadImage( ImagePath.elementIcon() ) );
		setBounds( 100, 100, 745, 508 );
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JLabel lblName = new JLabel(Messages.getString("ElementDialog.lblName.text")); //$NON-NLS-1$
		contentPanel.add(lblName, "2, 2, right, default");
		
		name = new JTextField();
		name.setName("name");
		contentPanel.add(name, "4, 2, fill, default");
		name.setColumns(10);
		
		JLabel lblInternalName = new JLabel(Messages.getString("ElementDialog.lblInternalName.text")); //$NON-NLS-1$
		contentPanel.add(lblInternalName, "2, 4, right, default");
		
		internalName = new JTextField();
		internalName.setName("internalName");
		contentPanel.add(internalName, "4, 4, fill, default");
		internalName.setColumns(10);
		
		JLabel lblElementType = new JLabel(Messages.getString("ElementDialog.lblElementType.text")); //$NON-NLS-1$
		contentPanel.add(lblElementType, "2, 6, right, default");
		
		elementType = new JComboBox( elementTypes.toArray( new ElementType[ 0 ] ));
		elementType.setName("elementType");
		contentPanel.add(elementType, "4, 6, fill, default");
		
		editable = new JCheckBox(Messages.getString("ElementDialog.editable.text")); //$NON-NLS-1$
		editable.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				final boolean isEditable =
					( e.getStateChange() == ItemEvent.SELECTED );
				changeEditableState( isEditable );
			}
		});
		editable.setMargin(new Insets(2, 0, 2, 2));
		editable.setName("editable");
		contentPanel.add(editable, "4, 8");

		JLabel lblValueType = new JLabel(Messages.getString("ElementDialog.lblValueType.text")); //$NON-NLS-1$
		contentPanel.add(lblValueType, "2, 10, right, default");
		
		final List< String > valueTypeList = EnumTranslation.createList( ValueType.class );
		
		valueType = new JComboBox( valueTypeList.toArray( new String[ 0 ] ) );
		valueType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if ( e.getStateChange() == ItemEvent.SELECTED ) {
					int index = valueType.getSelectedIndex();
					if ( index < 0 ) { return; }
					// Necessary to set the right value type because the
					// validation of the value configuration
					brAC.setValueType( ValueType.values()[ index ] );
				}
			}
		});
		valueType.setName("valueType");
		contentPanel.add(valueType, "4, 10, fill, default");
		
		JLabel lblBusinessRules = new JLabel(Messages.getString("ElementDialog.lblBusinessRules.text")); //$NON-NLS-1$
		contentPanel.add(lblBusinessRules, "2, 12, default, top");
		
		JPanel businessRulesPanel = new JPanel();
		businessRulesPanel.setName("businessRulesPanel");
		contentPanel.add(businessRulesPanel, "4, 12, fill, fill");
		businessRulesPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel businessRulesButtonsPanel = new JPanel();
		businessRulesButtonsPanel.setName("businessRulesButtonsPanel");
		businessRulesPanel.add(businessRulesButtonsPanel, BorderLayout.NORTH);
		businessRulesButtonsPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,},
			new RowSpec[] {
				RowSpec.decode("25px"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		BaseTableModel< BusinessRule > brTM = createBusinessRuleTableModel( getObject() );
		brAC = new BusinessRuleActionContainer( brTM, software, elements, driverCache );
		
		JButton newButton = new JButton( brAC.getNewAction() );
		newButton.setName( "newButton" );
		businessRulesButtonsPanel.add(newButton, "1, 1");
		
		JButton editButton = new JButton( brAC.getEditAction() );
		editButton.setName( "editButton" );
		businessRulesButtonsPanel.add(editButton, "3, 1");
		
		JButton removeButton = new JButton( brAC.getRemoveAction() );
		removeButton.setName( "removeButton" );
		businessRulesButtonsPanel.add(removeButton, "5, 1");
		
		businessRulesContentPanel = new CRUDTablePanel();
		businessRulesContentPanel.setName("businessRulesContentPanel");
		businessRulesContentPanel.setTableModel( brTM );
		businessRulesContentPanel.addListSelectionListener( brAC );
		
		businessRulesContentPanel.setNewAction( brAC.getNewAction() );
		businessRulesContentPanel.setEditAction( brAC.getEditAction() );
		businessRulesContentPanel.setRemoveAction( brAC.getRemoveAction() );
		
		JTableUtil.adjustMinColumnWidths( businessRulesContentPanel.getTable(),
				new int[] { 30, 150, 100 } );
		
		JTableUtil.adjustMaxColumnWidths( businessRulesContentPanel.getTable(),
				new int[] { 30, 200 } );
		
		businessRulesPanel.add(businessRulesContentPanel, BorderLayout.CENTER);
	}
	
	
	protected void changeEditableState(boolean isEditable) {
		valueType.setEnabled( isEditable );
		businessRulesContentPanel.setEditable( isEditable );
	}

	@Override
	protected boolean populateObject() {
		Element obj = getObject();
		obj.setName( name.getText() );
		obj.setInternalName( internalName.getText() );
		obj.setType( (ElementType) elementType.getSelectedItem() );
		obj.setEditable( editable.isSelected() );
		if ( valueType.getSelectedIndex() >= 0 ) {
			obj.setValueType( ValueType.values()[ valueType.getSelectedIndex() ] );
		}
		return true;
	}

	@Override
	protected Element createObject() {
		return new Element();
	}
	
	@Override
	protected void drawObject(final Element obj) {
		name.setText( obj.getName() );
		internalName.setText( obj.getInternalName() );
		elementType.setSelectedItem( obj.getType() );
		editable.setSelected( obj.isEditable() );
		valueType.setSelectedItem( EnumTranslation.translationForItem(
				ValueType.class, obj.getValueType() ) );
		
		changeEditableState( obj.isEditable() );
	}

	@Override
	protected Validator< Element > createValidator() {
		return new ElementValidator();
	}

	private BaseTableModel< BusinessRule > createBusinessRuleTableModel(
			final Element element
			) {
		final String[] columns = {
				"#",
				Messages.alt( "_BUSINESS_RULE_TYPE", "Type" ),
				Messages.alt( "_BUSINESS_RULE_MESSAGE", "Message" ),
			};

		BaseTableModel< BusinessRule > tableModel =
			new BaseTableModel< BusinessRule >(
				columns.length,
				element.getBusinessRules(),
				columns
				) {
			private static final long serialVersionUID = -7888067903848502841L;

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch ( columnIndex ) {
					case 0 : return rowIndex + 1;
					case 1 : return EnumTranslation.translationForItem(
							BusinessRuleType.class,
							itemAt( rowIndex ).getType()
							);
					default : return itemAt( rowIndex ).getMessage();
				}
			}
		};
		
		tableModel.setColumnClasses( Integer.class, String.class );
		return tableModel;
	}
	
}
