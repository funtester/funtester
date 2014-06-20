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
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.DefaultButtons;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.UIValidationHelper;
import org.funtester.app.validation.IncludeFileValidator;
import org.funtester.core.software.IncludeFile;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Include file dialog
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class IncludeFileDialog extends JDialog {

	private static final long serialVersionUID = 8585839218961952057L;
	private final JPanel contentPanel = new JPanel();
	private JTextField name;
	
	private IncludeFile includeFile = new IncludeFile( "" );
	private boolean confirmed = false;

	public IncludeFileDialog() {
		
		setDefaultCloseOperation( HIDE_ON_CLOSE );
		setName( "includeFileDialog" );
		setTitle( Messages.getString( "IncludeFileDialog.this.title" ) ); //$NON-NLS-1$
		setIconImage( ImageUtil.loadImage( ImagePath.includeFileIcon() ) );
		setModal( true );
		setBounds( 100, 100, 444, 133 );
		getContentPane().setLayout( new BorderLayout() );
		contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		contentPanel
				.setLayout( new FormLayout( new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode( "default:grow" ),
						FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, } ) );

		JLabel lblInclude = new JLabel(
				Messages.getString( "IncludeFileDialog.lblInclude.text" ) ); //$NON-NLS-1$
		contentPanel.add( lblInclude, "2, 2, right, default" );

		name = new JTextField();
		name.setName( "name" );
		contentPanel.add( name, "4, 2, fill, default" );
		name.setColumns( 10 );

		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new MatteBorder(1, 0, 0, 0, (Color) SystemColor.scrollbar));
		buttonPane.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
		getContentPane().add( buttonPane, BorderLayout.SOUTH );

		JButton okButton = DefaultButtons.createOkButton();
		buttonPane.add( okButton );
		getRootPane().setDefaultButton( okButton );
		
		okButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( ! validadeIncludeFile() ) { return; }
				confirmed = true;
				setVisible( false );
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
		
		draw();
	}
	
	public boolean isConfirmed() {
		return confirmed;
	}
	
	public IncludeFile getIncludeFile() {
		includeFile.setName( name.getText() );
		return includeFile;
	}
	
	public void setIncludeFile(IncludeFile obj) {
		includeFile.copy( obj );
		draw();
	}
	
	private void draw() {
		name.setText( includeFile.getName() );
	}
	
	private boolean validadeIncludeFile() {
		return UIValidationHelper.validate( contentPanel, getTitle(),
				new IncludeFileValidator(), getIncludeFile() );
	}

}
