package org.funtester.app.ui.software;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.DefaultButtons;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.UIValidationHelper;
import org.funtester.app.validation.PreconditionValidator;
import org.funtester.core.software.Precondition;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Precondition dialog
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class PreconditionDialog extends JDialog {

	private static final long serialVersionUID = -1699949091233606715L;
	private final JPanel contentPanel = new JPanel();
	private JTextField description;
	
	private final Precondition precondition = new Precondition(); 
	private boolean confirmed = false;

	public PreconditionDialog() {
		
		setDefaultCloseOperation( HIDE_ON_CLOSE );
		setTitle(Messages.getString("PreconditionDialog.this.title")); //$NON-NLS-1$
		setIconImage( ImageUtil.loadImage( ImagePath.postconditionIcon() ) );
		setModal( true );
		setBounds( 100, 100, 490, 127 );
		getContentPane().setLayout( new BorderLayout() );
		contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblDescription = new JLabel(Messages.getString("PreconditionDialog.lblDescription.text")); //$NON-NLS-1$
		contentPanel.add(lblDescription, "2, 2, right, default");
		
		description = new JTextField();
		description.setName("description");
		contentPanel.add(description, "4, 2, fill, default");
		description.setColumns(10);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
		getContentPane().add( buttonPane, BorderLayout.SOUTH );
		
		JButton okButton = DefaultButtons.createOkButton();
		buttonPane.add( okButton );
		getRootPane().setDefaultButton( okButton );
		okButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( ! validatePrecondition() ) { return; }
				setVisible( false );
				confirmed = true;
			}
		} );
		
		JButton cancelButton = DefaultButtons.createCancelButton();
		buttonPane.add( cancelButton );
		final ActionListener cancelActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible( false );
			}
		};
		cancelButton.addActionListener( cancelActionListener );
		
		// Register the ESC key to call the cancelActionListener
		getRootPane().registerKeyboardAction(
				cancelActionListener,
				KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ),
				JComponent.WHEN_IN_FOCUSED_WINDOW
				);
	}

	public boolean isConfirmed() {
		return confirmed;
	}
	
	public void setPrecondition(final Precondition p) {
		this.precondition.copy( p );
		draw();
	}

	public Precondition getPrecondition() {
		precondition.setDescription( description.getText() );
		return precondition;
	}
	
	
	private void draw() {
		description.setText( precondition.getDescription() );
		description.requestFocusInWindow();
	}
	
	private boolean validatePrecondition() {
		return UIValidationHelper.validate( contentPanel, getTitle(),
				new PreconditionValidator(), getPrecondition() );
	}
}
