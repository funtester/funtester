package org.funtester.app.ui.software;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
import org.funtester.app.validation.SoftwareValidator;
import org.funtester.core.software.Software;
import org.funtester.core.vocabulary.Vocabulary;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Software dialog
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class SoftwareDialog extends JDialog {

	private static final long serialVersionUID = -864975241188238021L;
	
	private boolean confirmed = false;
	private final List< Vocabulary > vocabularies;
	private Software software = new Software();
	
	private final JPanel contentPanel = new JPanel();
	private final JTextField name;
	@SuppressWarnings( {"rawtypes" } )
	private final JComboBox vocabulary;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SoftwareDialog(final Collection< Vocabulary > vocabularies) {
		this.vocabularies = new ArrayList< Vocabulary >();
		this.vocabularies.addAll( vocabularies );
		
		setModal( true ); // Default to modal
		setIconImage( ImageUtil.loadImage( ImagePath.softwareIcon() ) );
		setTitle( Messages.getString( "SoftwareDialog.this.title" ) ); //$NON-NLS-1$
		setBounds( 100, 100, 474, 155 );
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
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, } ) );

		JLabel lblName = new JLabel(
				Messages.getString( "SoftwareDialog.lblName.text" ) ); //$NON-NLS-1$
		contentPanel.add( lblName, "2, 2, right, default" );

		name = new JTextField();
		name.setName( "name" );
		contentPanel.add( name, "4, 2, fill, default" );
		name.setColumns( 10 );

		JLabel lblVocabulary = new JLabel(
				Messages.getString( "SoftwareDialog.lblVocabulary.text" ) ); //$NON-NLS-1$
		contentPanel.add( lblVocabulary, "2, 4, right, default" );

		vocabulary = new JComboBox( makeVocabularyItems( this.vocabularies ) );
		vocabulary.setName( "vocabulary" );
		contentPanel.add( vocabulary, "4, 4, fill, default" );

		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new MatteBorder(1, 0, 0, 0, (Color) SystemColor.scrollbar));
		buttonPane.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
		getContentPane().add( buttonPane, BorderLayout.SOUTH );

		JButton okButton = DefaultButtons.createOkButton();
		okButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( ! validateSoftware() ) { return; }
				confirmed = true;
				setVisible( false );
				dispose();
			}
		} );
		buttonPane.add( okButton );
		getRootPane().setDefaultButton( okButton );

		JButton cancelButton = DefaultButtons.createCancelButton();
		final ActionListener cancelActionListener = new ActionListener() {
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
		
		draw();
	}

	private Object[] makeVocabularyItems(
			final Collection< Vocabulary > vocabularies
			) {
		List< String > items = new ArrayList< String >();
		for ( Vocabulary v : vocabularies ) {
			String content = v.getName() + " (" + v.getLocale().getDisplayLanguage() + ")";
			items.add( content );
		}
		return items.toArray( new String[ 0 ] );
	}

	public boolean isConfirmed() {
		return confirmed;
	}
	
	public void setSoftware(Software software) {
		this.software.copy( software );
		draw();
	}

	public Software getSoftware() {
		software.setName( name.getText() );
		
		final int index = vocabulary.getSelectedIndex();
		if ( index < 0 ) {
			software.setVocabulary( null );
		} else {
			Vocabulary v = vocabularies.get( index );
			software.setVocabulary( v );
		}
		
		return software;
	}

	private void draw() {
		name.setText( software.getName() );
		
		Vocabulary v = software.getVocabulary();
		if ( null == v ) {
			vocabulary.setSelectedIndex( -1 );
		} else {
			int index = vocabularies.indexOf( v );
			vocabulary.setSelectedIndex( index );
		}
	}
	
	private boolean validateSoftware() {
		return UIValidationHelper.validate( contentPanel, getTitle(),
				new SoftwareValidator(), getSoftware() );
	}	

}
