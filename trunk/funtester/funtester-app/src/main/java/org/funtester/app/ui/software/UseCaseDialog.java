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
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.DefaultButtons;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.UIValidationHelper;
import org.funtester.app.validation.UseCaseValidator;
import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.software.Software;
import org.funtester.core.software.UseCase;

/**
 * Use case dialog
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class UseCaseDialog extends JDialog {

	private static final long serialVersionUID = 1837171862369218564L;
	private final UseCasePanel contentPanel;
	
	private boolean confirmed = false;
	private final Software software;

	public UseCaseDialog(
			final UseCase useCase,
			final Software aSoftware,
			final DriverCache driverCache,
			final boolean editable
			) {	
		this.software = aSoftware;
		
		setDefaultCloseOperation( HIDE_ON_CLOSE );
		setModal( true ); // Default to modal
		setIconImage( ImageUtil.loadImage( ImagePath.useCaseIcon() ) );
		setTitle( Messages.getString( "_USE_CASE" ) ); //$NON-NLS-1$
		setBounds( 100, 100, 870, 680 );
		getContentPane().setLayout( new BorderLayout() );

		contentPanel = new UseCasePanel( useCase, aSoftware, driverCache );
		contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		contentPanel.setEditable( editable );

		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new MatteBorder(1, 0, 0, 0, (Color) SystemColor.scrollbar));
		buttonPane.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
		getContentPane().add( buttonPane, BorderLayout.SOUTH );

		JButton okButton = DefaultButtons.createOkButton();
		okButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( ! validateUseCase() ) { return; }
				confirmed = true;
				setVisible( false );
			}
		} );

		buttonPane.add( okButton );
		getRootPane().setDefaultButton( okButton );

		JButton cancelButton = DefaultButtons.createCancelButton();
		final ActionListener cancelActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();	
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

	public boolean isConfirmed() {
		return confirmed;
	}

	public UseCase getUseCase() {
		return contentPanel.get();
	}
	
	/*
	public void setUseCase(UseCase useCase) {
		contentPanel.setUseCase( useCase );
	} */
	
	private boolean validateUseCase() {
		return UIValidationHelper.validate( getRootPane(), getTitle(),
				new UseCaseValidator( software ), contentPanel.get() );
	}
}
