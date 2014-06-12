package org.funtester.app.ui.util;

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

import org.funtester.common.util.Copier;
import org.funtester.common.util.Validator;

/**
 * Simple editing dialog.
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T>	The editing type. Should extends <code>Copier</code>.
 */
public abstract class EditingDialog< T extends Copier< ? super T > >
	extends JDialog {

	private static final long serialVersionUID = -3352928806335106549L;
	
	protected final JPanel contentPanel = new JPanel();
	protected final JPanel buttonPane;
	protected final JButton okButton;
	protected final JButton cancelButton;
	
	private T object;	
	private boolean objectWasSet = false;
	private boolean confirmed = false;

	public EditingDialog() {
		
		setDefaultCloseOperation( HIDE_ON_CLOSE );
		setModal( true );
		setBounds( 100, 100, 450, 300 );
		getContentPane().setLayout( new BorderLayout() );
		contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		contentPanel.setLayout(new BorderLayout(0, 0));
	
		buttonPane = new JPanel();
		buttonPane.setBorder(new MatteBorder(1, 0, 0, 0, (Color) SystemColor.scrollbar));
		buttonPane.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
		getContentPane().add( buttonPane, BorderLayout.SOUTH );
		
		okButton = new JButton( "OK" );
		okButton.setName( "okButton" );
		buttonPane.add( okButton );
		okButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( ! populateObject() ) { return; }
				if ( ! validateObject() ) { return; }
				confirmed = true;
				setVisible( false );
			}
		} );
		getRootPane().setDefaultButton( okButton );

		cancelButton = new JButton( "Cancel" );
		cancelButton.setName( "cancelButton" );
		buttonPane.add( cancelButton );
		final ActionListener cancelActionListener = new ActionListener() {
			@Override
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
		
		object = createObject();
		
		// Do NOT call drawObject because in child dialogs the widgets are not
		// created.
	}

	public T getObject() {
		return object;
	}
	
	/**
	 * Copy and object.
	 * 
	 * @param obj	the object to copy.
	 */
	public void copyObject(final T obj) {
		this.object.copy( obj );
		this.objectWasSet = true;
		drawObject( this.object );
	}
	
	/**
	 * Set a reference to the original object.
	 * 
	 * @param obj	the object to reference.
	 */
	public void setObject(final T obj) {
		this.object = obj;
		this.objectWasSet = true;
		drawObject( this.object );
	}
	
	/**
	 * Show the object in the dialog.
	 */
	public void showObject() {
		// Draw only if the object was not set
		if ( ! objectWasSet ) {
			drawObject( this.object );
		}
		setVisible( true );
	}
	
	/**
	 * Return {@code true} whether the user has clicked OK.
	 * @return
	 */
	public boolean isConfirmed() {
		return confirmed;
	}
	
	/**
	 * Validate the current object.
	 * @return
	 */
	protected boolean validateObject() {
		return UIValidationHelper.validate(
				rootPane, getTitle(), createValidator(), this.object );
	}
	
	/**
	 * Create an object to be used by the IU.
	 * @return
	 */
	protected abstract T createObject();

	/**
	 * Populate the object and return {@code true} if successful.
	 * @return
	 */
	protected abstract boolean populateObject();
	
	/**
	 * Draw an object.
	 * @param obj	the object to be drawn.
	 */
	protected abstract void drawObject(final T obj);
	
	/**
	 * Create a validator.
	 * @return
	 */
	protected abstract Validator< T > createValidator();
}
