package org.funtester.app.ui.software;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.MatteBorder;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.DefaultButtons;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.UIValidationHelper;
import org.funtester.app.validation.FlowValidator;
import org.funtester.core.software.Flow;
import org.funtester.core.software.Software;
import org.funtester.core.software.UseCase;

/**
 * Dialog for editing a {@link Flow}.
 * 
 * TODO inherit from DefaultEditingDialog instead of JDialog.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class FlowDialog extends JDialog {

	private static final long serialVersionUID = -3452471600931073677L;
	
	private final FlowPanel contentPanel;
	private boolean confirmed = false;

	public FlowDialog(
			final Software software,
			final UseCase useCase,
			final Flow flow
			) {
		contentPanel = new FlowPanel( software, useCase, flow );
		
		setModal( true );
		setTitle( Messages.getString("FlowDialog.this.title") ); //$NON-NLS-1$
		setIconImage( ImageUtil.loadImage( ImagePath.flowIcon() ) );
		setBounds( 100, 100, 918, 700 );
		getContentPane().setLayout( new BorderLayout() );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new MatteBorder(1, 0, 0, 0, (Color) SystemColor.scrollbar));
		buttonPane.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
		getContentPane().add( buttonPane, BorderLayout.SOUTH );
	
		JButton okButton = DefaultButtons.createOkButton();
		okButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( ! validateFlow() ) { return; }
				confirmed = true;
				setVisible( false );
			}
		} );		
		buttonPane.add( okButton );
		getRootPane().setDefaultButton( okButton );

		JButton cancelButton = DefaultButtons.createCancelButton();
		ActionListener cancelActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible( false );
			}
		}; 
		cancelButton.addActionListener( cancelActionListener );
		buttonPane.add( cancelButton );
		
		// Register the ESC key to call the cancelActionListener
		getRootPane().registerKeyboardAction(
				cancelActionListener,
				KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ),
				JComponent.WHEN_IN_FOCUSED_WINDOW
				);		
	}
	
	private boolean validateFlow() {
		return UIValidationHelper.validate(
				contentPanel, getTitle(), new FlowValidator(), getFlow() );
	}

	public Flow getFlow() {
		return contentPanel.getFlow();
	}
	
	public boolean isConfirmed() {
		return confirmed;
	}

}
