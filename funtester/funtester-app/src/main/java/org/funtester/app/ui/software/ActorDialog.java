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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.DefaultButtons;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.UIValidationHelper;
import org.funtester.app.ui.util.thirdparty.FocusUtil;
import org.funtester.app.validation.ActorValidator;
import org.funtester.core.software.Actor;
import org.funtester.core.software.Software;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Actor dialog
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ActorDialog extends JDialog {

	private static final long serialVersionUID = 6481327720101783387L;
	private final JPanel contentPanel = new JPanel();
	private final JTextField name;
	private final JTextArea description;
	
	private final Actor actor = new Actor();
	private boolean confirmed = false;
	
	public ActorDialog(Software software) {
		setModal( true );
		setTitle( Messages.getString( "_ACTOR" ) ); //$NON-NLS-1$
		setIconImage( ImageUtil.loadImage( ImagePath.actorIcon() ) );
		setBounds( 100, 100, 461, 207 );

		getContentPane().setLayout( new BorderLayout() );
		contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		contentPanel.setLayout(
				new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("73px"),
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("335px"),
						FormFactory.RELATED_GAP_COLSPEC,},
					new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("20px"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("78px"),
						FormFactory.RELATED_GAP_ROWSPEC,} )				
				);

		JLabel lblName = new JLabel(Messages.getString("ActorDialog.lblName.text")); //$NON-NLS-1$
		contentPanel.add(lblName, "2, 2, fill, center");
		
		name = new JTextField();
		name.setName( "name" );
		contentPanel.add(name, "4, 2, fill, fill");
		name.setColumns(30);
		
		JLabel lblDescription = new JLabel(Messages.getString("ActorDialog.lblDescription.text")); //$NON-NLS-1$
		contentPanel.add(lblDescription, "2, 4, fill, top");
		
		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, "4, 4, fill, fill");
		
		description = new JTextArea();
		description.setLineWrap(true);
		FocusUtil.patch( description );
		description.setName( "description" );
		description.setFont( name.getFont() ); // Same Font as name	
		scrollPane.setViewportView(description);

		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new MatteBorder(1, 0, 0, 0, (Color) SystemColor.scrollbar));
		buttonPane.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
		getContentPane().add( buttonPane, BorderLayout.SOUTH );

		JButton okButton = DefaultButtons.createOkButton();
		buttonPane.add( okButton );
		okButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( ! validateActor() ) { return; }
				confirmed = true;
				setVisible( false );
			}
		} );
		
		getRootPane().setDefaultButton( okButton );

		JButton cancelButton = DefaultButtons.createCancelButton();
		buttonPane.add( cancelButton );
		
		ActionListener cancelActionListener = new ActionListener() {
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
		
		drawActor();
	}

	public void setActor(final Actor other) {
		actor.copy( other );
		drawActor();
	}

	public Actor getActor() {
		actor.setName( name.getText() );
		actor.setDescription( description.getText() );
		return actor;
	}

	public boolean isConfirmed() {
		return confirmed;
	}
	
	private void drawActor() {
		name.setText( actor.getName() );
		description.setText( actor.getDescription() );
	}	
	
	private boolean validateActor() {
		return UIValidationHelper.validate(
				contentPanel, getTitle(), new ActorValidator(), getActor() );
	}
}
