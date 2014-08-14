package org.funtester.app.ui.software;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.funtester.app.ui.common.EnumTranslation;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.core.software.MultiVC;
import org.funtester.core.software.ValueConfiguration;
import org.funtester.core.software.ValueType;
import org.funtester.core.software.ValueTypeBasedConverter;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import org.funtester.app.i18n.Messages;

/**
 * Panel for a {@link MultiVC}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class MultiVCPanel extends VCPanel {

	private static final long serialVersionUID = -4038302367089540858L;
	
	private final MultiVC valueConfiguration = new MultiVC();
	
	private final ValueType valueType;
	private final DefaultListModel model = new DefaultListModel();
	
	private final JTextField value;
	private final JList values;
	private final JButton addButton;
	private final JButton removeButton;
	private JPanel buttonPanel;
	private JPanel movePanel;
	private JButton moveDownButton;
	private JButton moveUpButton;

	/**
	 * Create the panel.
	 */
	public MultiVCPanel(final ValueType valueType) {
		this.valueType = valueType;
		
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
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblValues = new JLabel(Messages.getString("MultiVCPanel.lblValues.text")); //$NON-NLS-1$
		add(lblValues, "2, 2, right, default");
		
		value = new JTextField();
		value.setName("value");
		add(value, "4, 2, fill, default");
		value.setColumns(10);
		
		addButton = new JButton(Messages.getString("MultiVCPanel.addButton.text")); //$NON-NLS-1$
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( addValue() ) {
					value.setText( "" );
					value.requestFocusInWindow();
				}
			}
		});
		addButton.setName("addButton");
		add(addButton, "6, 2");
		
		JScrollPane valuesScrollPane = new JScrollPane();
		add(valuesScrollPane, "4, 4, fill, fill");
		
		values = new JList();
		values.setName("values");
		values.setModel( model );
		valuesScrollPane.setViewportView(values);
		values.addListSelectionListener( new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				updateButtonsState();
			}
		} );
		
		buttonPanel = new JPanel();
		add(buttonPanel, "6, 4, fill, fill");
		buttonPanel.setLayout(new BorderLayout(0, 0));
		
		removeButton = new JButton(Messages.getString("MultiVCPanel.removeButton.text")); //$NON-NLS-1$
		buttonPanel.add(removeButton, BorderLayout.NORTH);
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( removeValue() ) {
					value.setText( "" );
					value.requestFocusInWindow();
				}
			}
		});
		removeButton.setName("removeButton");
		
		movePanel = new JPanel();
		buttonPanel.add(movePanel, BorderLayout.SOUTH);
		movePanel.setLayout(new BorderLayout(0, 0));
		
		moveUpButton = new JButton(Messages.getString("MultiVCPanel.moveUpButton.text")); //$NON-NLS-1$
		moveUpButton.setName( "moveUpButton" );
		moveUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveUp();
			}
		});
		movePanel.add(moveUpButton, BorderLayout.NORTH);
		
		moveDownButton = new JButton(Messages.getString("MultiVCPanel.moveDownButton.text")); //$NON-NLS-1$
		moveDownButton.setName( "moveDownButton" );
		moveDownButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveDown();
			}
		});
		movePanel.add(moveDownButton, BorderLayout.SOUTH);
		
		updateButtonsState();
	}

	@Override
	public ValueConfiguration getValueConfiguration() {
		return valueConfiguration;
	}
	
	@Override
	public void setValueConfiguration(ValueConfiguration obj) {
		valueConfiguration.copy( obj );
		drawVC();
	}

	private void drawVC() {
		model.clear();
		for ( Object o : valueConfiguration.getValues() ) {
			model.addElement( o );
		}
		updateButtonsState();
	}

	private boolean addValue() {
		Object val = value.getText();
		Object converted;
		try {
			converted = ValueTypeBasedConverter.convert( valueType, val );
		} catch ( Exception e ) {
			String msg = "The value is wrong for its type. Expected: " +
					EnumTranslation.translationForItem( ValueType.class, valueType ); // TODO i18n
			MsgUtil.error( getRootPane(), msg, addButton.getText() );
			value.requestFocusInWindow();
			return false;
		}
		if ( model.contains( converted ) ) {
			String msg = "This value already exists."; // TODO i18n
			MsgUtil.info( getRootPane(), msg, addButton.getText() );
			value.requestFocusInWindow();
			return false;
		}
	
		model.addElement( converted );
		valueConfiguration.addValue( converted );
		
		updateButtonsState();
		
		return true;
	}

	private boolean removeValue() {
		final int index = values.getSelectedIndex();
		if ( index < 0 ) { return false; }
		valueConfiguration.remove( index );
		model.remove( index );
		
		updateButtonsState();
		
		return true;
	}
	
	private boolean moveUp() {
		final int fromIndex = values.getSelectedIndex();
		if ( fromIndex <= 0 ) { return false; }
		final int toIndex = fromIndex - 1;
		
		swapValues( fromIndex, toIndex );
		return true;
	}
	
	private boolean moveDown() {
		final int size = model.size();
		final int fromIndex = values.getSelectedIndex();
		if ( 1 == size || fromIndex >= size - 1 ) { return false; }
		final int toIndex = fromIndex + 1;
		
		swapValues( fromIndex, toIndex );
		return true;
	}
	
	private void swapValues(final int fromIndex, final int toIndex) {
		Object toObject = model.get( toIndex );
		Object fromObject = model.get( fromIndex );
		
		// Swap in the model
		model.set( toIndex, fromObject );
		model.set( fromIndex, toObject );
		
		// Swap in the VC
		valueConfiguration.set( toIndex, fromObject );
		valueConfiguration.set( fromIndex, toObject );
		
		// Select the up index
		values.setSelectedIndex( toIndex );
		
		updateButtonsState();
	}
	
	private void updateButtonsState() {
		final int size = model.size();
		final int index = values.getSelectedIndex();
		removeButton.setEnabled( index >= 0 );
		moveUpButton.setEnabled( index > 0 );
		moveDownButton.setEnabled( index >= 0 && size > 1 && index < size - 1 );
	}
}
