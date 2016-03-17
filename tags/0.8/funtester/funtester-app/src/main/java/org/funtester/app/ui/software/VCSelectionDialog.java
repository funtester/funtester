package org.funtester.app.ui.software;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import org.funtester.app.ui.common.DefaultButtons;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.software.BusinessRuleType;
import org.funtester.core.software.Element;
import org.funtester.core.software.Software;
import org.funtester.core.software.ValueConfiguration;
import org.funtester.core.software.ValueType;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import org.funtester.app.i18n.Messages;

/**
 * Selection dialog for a {@link ValueConfiguration}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class VCSelectionDialog extends JDialog {

	private static final long serialVersionUID = 1850375282088378589L;
	
	private final JPanel contentPanel;
	private final VCSelectionPanel selectionPanel;
	private boolean confirmed = false;
	private JLabel lblConfiguration;

	public VCSelectionDialog(
			final ValueType valueType,
			Collection< Element > elements,
			Software software,
			final DriverCache driverCache
			) {
		
		contentPanel = new JPanel();
		contentPanel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		setModal( true );
		setBounds( 100, 100, 611, 382 );
		getContentPane().setLayout( new BorderLayout() );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("220px:grow"),}));
		
		lblConfiguration = new JLabel(Messages.getString("VCSelectionDialog.lblConfiguration.text")); //$NON-NLS-1$
		contentPanel.add(lblConfiguration, "1, 1, default, top");
		
		
		selectionPanel = new VCSelectionPanel(
				valueType,
				elements,
				software,
				driverCache
				);
		selectionPanel.setName( "selectionPanel" );
		selectionPanel.setBusinessRuleType( null ); // None filter

		contentPanel.add( selectionPanel, "3, 1, fill, fill" );

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
		getContentPane().add( buttonPane, BorderLayout.SOUTH );

		JButton okButton = DefaultButtons.createOkButton();
		okButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ValueConfiguration vc = getValueConfiguration();
				if ( null == vc ) { return; }
				confirmed = true;
				setVisible( false );
			}
		} );
		buttonPane.add( okButton );
		getRootPane().setDefaultButton( okButton );
		
		final ActionListener cancelActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible( false );
			}
		};		

		JButton cancelButton = DefaultButtons.createCancelButton();
		cancelButton.addActionListener( cancelActionListener );
		buttonPane.add( cancelButton );
		
		// Register the ESC key to call the cancelActionListener
		getRootPane().registerKeyboardAction(
				cancelActionListener,
				KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ),
				JComponent.WHEN_IN_FOCUSED_WINDOW
				);
	}
	
	public void setBusinessRuleType(BusinessRuleType brType) {
		selectionPanel.setBusinessRuleType( brType );
	}
	
	public ValueConfiguration getValueConfiguration() {
		try {
			return selectionPanel.getValueConfiguration();
		} catch ( Exception e1 ) {
			MsgUtil.info( getRootPane(), e1.getLocalizedMessage(), getTitle() );
			return null;
		}
	}

	public void setValueConfiguration(ValueConfiguration obj) {
		selectionPanel.setValueConfiguration( obj );
	}
	
	public boolean isConfirmed() {
		return confirmed;
	}

}
