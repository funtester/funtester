package org.funtester.app.ui.testing;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.funtester.app.i18n.Messages;
import org.funtester.common.report.TestExecutionReport;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Report about dialog
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ReportAboutDialog extends JDialog {

	private static final long serialVersionUID = -1885611584723526992L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public ReportAboutDialog(JDialog owner, final TestExecutionReport report) {
		super( owner, true ); // true is for modal
		setTitle( Messages.getString( "ReportAboutDialog.this.title" ) ); //$NON-NLS-1$
		setBounds( 100, 100, 790, 285 );
		getContentPane().setLayout( new BorderLayout() );
		contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		contentPanel.setLayout( new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
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
				FormFactory.DEFAULT_ROWSPEC,}) );

		JLabel lblCreation = new JLabel(
				Messages.getString( "ReportAboutDialog.lblCreation.text" ) ); //$NON-NLS-1$
		contentPanel.add( lblCreation, "2, 2, left, default" );

		JTextField creation = new JTextField();
		creation.setName( "creation" );
		creation.setFocusable( false );
		creation.setEditable( false );
		contentPanel.add( creation, "4, 2, fill, default" );
		creation.setColumns( 10 );
		creation.setText( report.getCreation() );

		JLabel lblExtension = new JLabel(
				Messages.getString( "ReportAboutDialog.lblExtension.text" ) ); //$NON-NLS-1$
		contentPanel.add( lblExtension, "2, 4, left, default" );

		JTextField extension = new JTextField();
		extension.setName( "tool" );
		extension.setFocusable( false );
		extension.setEditable( false );
		extension.setColumns( 10 );
		extension.setText( report.getToolName() );
		contentPanel.add( extension, "4, 4, fill, default" );

		JLabel lblTargetLanguage = new JLabel(
				Messages.getString( "ReportAboutDialog.lblTargetLanguage.text" ) ); //$NON-NLS-1$
		contentPanel.add( lblTargetLanguage, "2, 6, left, default" );

		JTextField targetLanguage = new JTextField();
		targetLanguage.setName( "targetLanguage" );
		targetLanguage.setFocusable( false );
		targetLanguage.setEditable( false );
		targetLanguage.setColumns( 10 );
		targetLanguage.setText( report.getTargetLanguage() );
		contentPanel.add( targetLanguage, "4, 6, fill, default" );

		JLabel lblTargetUserInterfaces = new JLabel(
				Messages.getString( "ReportAboutDialog.lblTargetUserInterfaces.text" ) ); //$NON-NLS-1$
		contentPanel.add( lblTargetUserInterfaces, "2, 8, left, default" );

		JTextField targetUserInterfaces = new JTextField();
		targetUserInterfaces.setText( report.getTargetGUI() );
		targetUserInterfaces.setName( "targetGUI" );
		targetUserInterfaces.setFocusable( false );
		targetUserInterfaces.setEditable( false );
		targetUserInterfaces.setColumns( 10 );
		contentPanel.add( targetUserInterfaces, "4, 8, fill, default" );

		JLabel lblTargetFrameworks = new JLabel(
				Messages.getString( "ReportAboutDialog.lblTargetFrameworks.text" ) ); //$NON-NLS-1$
		contentPanel.add( lblTargetFrameworks, "2, 10, left, default" );

		JTextField targetFrameworks = new JTextField();
		targetFrameworks.setName( "targetFrameworks" );
		targetFrameworks.setFocusable( false );
		targetFrameworks.setEditable( false );
		targetFrameworks.setColumns( 10 );
		targetFrameworks.setText( report.getTargetTestingFrameworks() );
		contentPanel.add( targetFrameworks, "4, 10, fill, default" );

		JLabel lblReportFile = new JLabel(
				Messages.getString( "ReportAboutDialog.lblReportFile.text" ) ); //$NON-NLS-1$
		contentPanel.add( lblReportFile, "2, 12, left, default" );
		
		JPanel panel = new JPanel();
		contentPanel.add(panel, "4, 12, fill, fill");
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("475px:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				RowSpec.decode("22px"),}));

		JTextField reportFile = new JTextField();
		panel.add(reportFile, "1, 1, fill, top");
		reportFile.setEditable( false );
		reportFile.setName( "reportFile" );
		reportFile.setColumns( 10 );
		reportFile.setText( report.getOriginalTestResultFile() );

		JButton openButton = new JButton(
				Messages.getString( "ReportAboutDialog.openButton.text" ) ); //$NON-NLS-1$
		panel.add(openButton, "3, 1");
		openButton.setName( "openButton" );
		openButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final File file = new File( report.getOriginalTestResultFile() );
				if ( !file.exists() ) {
					final String MSG = String.format( Messages.alt(
							"_FILE_NO_LONGER_EXISTS", "This file no longer exists: \"%s\"." ),
							report.getOriginalTestResultFile() );
					JOptionPane.showMessageDialog( null, MSG, getTitle(),
							JOptionPane.INFORMATION_MESSAGE );
					return;
				}
				try {
					Desktop.getDesktop().open( file );
				} catch ( IOException e2 ) {
					JOptionPane.showMessageDialog( null,
							e2.getLocalizedMessage(), getTitle(),
							JOptionPane.ERROR_MESSAGE );
				}
			}
		} );

		JPanel buttonPane = new JPanel();
		buttonPane.setBorder( new TitledBorder( null, "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null ) );
		buttonPane.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
		getContentPane().add( buttonPane, BorderLayout.SOUTH );
		{
			JButton closeButton = new JButton(
					Messages.getString( "ReportAboutDialog.closeButton.text" ) ); //$NON-NLS-1$
			closeButton.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible( false );
				}
			} );
			closeButton.setName( "closeButton" );
			closeButton.setActionCommand( "Cancel" );
			buttonPane.add( closeButton );
		}

	}

}
