package org.funtester.app.ui.testing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.testing.TestExecutionReportDialog.ReportItem;
import org.funtester.common.report.TestMethodExecution;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Displays a {@link ReportItem}. This class is visible only inside its package.
 * 
 * @author Thiago Delgado Pinto
 *
 */
class ReportItemDialog extends JDialog {
	
	private static final long serialVersionUID = -4958462359881926884L;
	private final JPanel contentPanel = new JPanel();
	private JTextField methodNameField;
	private JTextField timeField;
	private JTextField statusField;
	private JTextField exceptionClassField;
	private JTextField classNameField;
	private JTextField erroreousFileLineNumberField;
	private JTextField erroneousLineOfCodeField;
	private JTextField erroneousFileField;


	/**
	 * Create the dialog.
	 */
	public ReportItemDialog(JDialog owner, final ReportItem item) {		
		super( owner, true ); // true is for modal
		
		if ( null == item ) {
			throw new IllegalArgumentException( "method should not be null." );
		}		
		final TestMethodExecution method = item.testMethod;
		
		setTitle( Messages.getString("ReportItemDialog.this.title") ); //$NON-NLS-1$
		setBounds( 100, 100, 806, 565 );
		getContentPane().setLayout( new BorderLayout() );
		contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
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
				RowSpec.decode("max(56dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblClassName = new JLabel(Messages.getString("ReportItemDialog.lblClassName.text")); //$NON-NLS-1$
			contentPanel.add(lblClassName, "2, 2, left, default");
		}
		{
			classNameField = new JTextField();
			classNameField.setText( item.testClassName );
			classNameField.setName("classNameField");
			classNameField.setEditable(false);
			classNameField.setColumns(10);
			contentPanel.add(classNameField, "4, 2, fill, default");
		}
		{
			JLabel lblMethodName = new JLabel(Messages.getString("ReportItemDialog.lblMethodName.text")); //$NON-NLS-1$
			contentPanel.add(lblMethodName, "2, 4, left, default");
		}
		{
			methodNameField = new JTextField();
			methodNameField.setForeground(new Color(128, 0, 128));
			methodNameField.setEditable(false);
			methodNameField.setName("methodNameField");
			methodNameField.setText( method.getName() );
			contentPanel.add(methodNameField, "4, 4, fill, default");
			methodNameField.setColumns(10);
		}
		{
			JLabel lblTime = new JLabel(Messages.getString("ReportItemDialog.lblTime.text")); //$NON-NLS-1$
			contentPanel.add(lblTime, "2, 6, left, default");
		}
		{
			timeField = new JTextField();
			timeField.setText( String.valueOf( method.getTimeInMillis() ) );
			timeField.setName("timeField");
			timeField.setEditable(false);
			timeField.setColumns(10);
			contentPanel.add(timeField, "4, 6, fill, default");
		}
		{
			JLabel lblStatus = new JLabel(Messages.getString("ReportItemDialog.lblStatus.text")); //$NON-NLS-1$
			contentPanel.add(lblStatus, "2, 8, left, default");
		}
		{
			statusField = new JTextField();
			statusField.setText( method.getStatus().toString() );
			statusField.setName("statusField");
			statusField.setEditable(false);
			statusField.setColumns(10);
			contentPanel.add(statusField, "4, 8, fill, default");
		}
		{
			JLabel lblExceptionClass = new JLabel(Messages.getString("ReportItemDialog.lblExceptionClass.text")); //$NON-NLS-1$
			contentPanel.add(lblExceptionClass, "2, 10, left, default");
		}
		{
			exceptionClassField = new JTextField();
			exceptionClassField.setText( method.getExceptionClass() );
			exceptionClassField.setName("exceptionClassField");
			exceptionClassField.setEditable(false);
			exceptionClassField.setColumns(10);
			contentPanel.add(exceptionClassField, "4, 10, fill, default");
		}
		{
			JLabel lblExceptionMessage = new JLabel(Messages.getString("ReportItemDialog.lblExceptionMessage.text")); //$NON-NLS-1$
			contentPanel.add(lblExceptionMessage, "2, 12, left, top");
		}
		{
			JScrollPane exceptionMessageScrollPane = new JScrollPane();
			contentPanel.add(exceptionMessageScrollPane, "4, 12, fill, fill");
			{
				JTextPane exceptionMessageField = new JTextPane();
				exceptionMessageScrollPane.setViewportView(exceptionMessageField);
				exceptionMessageField.setBackground(SystemColor.control);
				exceptionMessageField.setEditable(false);
				exceptionMessageField.setText( method.getExceptionMessage() );
			}
		}
		{
			JLabel lblStackTrace = new JLabel(Messages.getString("ReportItemDialog.lblStackTrace.text")); //$NON-NLS-1$
			contentPanel.add(lblStackTrace, "2, 14, left, top");
		}
		{
			JScrollPane stackTraceScrollPane = new JScrollPane();
			stackTraceScrollPane.setName("stackTraceScrollPane");
			contentPanel.add(stackTraceScrollPane, "4, 14, fill, fill");
			{
				JTextPane stackTraceField = new JTextPane();
				stackTraceField.setText( method.getStackTrace() );
				stackTraceField.setName("stackTraceField");
				stackTraceField.setEditable(false);
				stackTraceField.setBackground(SystemColor.control);
				stackTraceScrollPane.setViewportView(stackTraceField);			
			}
		}
		{
			JLabel lblErroneousFile = new JLabel(Messages.getString("ReportItemDialog.lblErroneousFile.text")); //$NON-NLS-1$
			contentPanel.add(lblErroneousFile, "2, 16, left, default");
		}
		{
			JPanel filePanel = new JPanel();
			contentPanel.add(filePanel, "4, 16, fill, fill");
			filePanel.setLayout(new FormLayout(new ColumnSpec[] {
					ColumnSpec.decode("default:grow"),
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,},
				new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.MIN_ROWSPEC,}));
			{
				final String text = ( method.getErroneousFile() != null )
					? method.getErroneousFile() : "";
				erroneousFileField = new JTextField();
				erroneousFileField.setText( text );
				erroneousFileField.setName("erroneousFileField");
				erroneousFileField.setEditable(false);
				erroneousFileField.setColumns(10);
				filePanel.add(erroneousFileField, "1, 2, fill, default");
			}
			{
				JButton openFileButton = new JButton(Messages.getString("ReportItemDialog.openFileButton.text")); //$NON-NLS-1$
				openFileButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						final String text = erroneousFileField.getText();
						if ( text.trim().isEmpty() ) {
							return;
						}
						final File file = new File( text );
						try {
							Desktop.getDesktop().open( file );
						} catch ( Exception e1 ) {
							JOptionPane.showMessageDialog( null,
									e1.getLocalizedMessage(), getTitle(),
									JOptionPane.ERROR_MESSAGE );
						}
					}
				});
				openFileButton.setName("openFileButton");
				filePanel.add(openFileButton, "3, 2");
			}
		}
		{
			JLabel lblErroreousFileLineNumber = new JLabel(Messages.getString("ReportItemDialog.lblErroreousFileLineNumber.text")); //$NON-NLS-1$
			contentPanel.add(lblErroreousFileLineNumber, "2, 18, left, default");
		}
		{
			final String text = ( method.getErroreousFileLineNumber() != null )
				? method.getErroreousFileLineNumber().toString() : "";
			erroreousFileLineNumberField = new JTextField();
			erroreousFileLineNumberField.setText( text );
			erroreousFileLineNumberField.setName("erroreousFileLineNumberField");
			erroreousFileLineNumberField.setEditable(false);
			erroreousFileLineNumberField.setColumns(10);
			contentPanel.add(erroreousFileLineNumberField, "4, 18, fill, default");
		}
		{
			JLabel lblErroneousLineOfCode = new JLabel(Messages.getString("ReportItemDialog.lblErroneousLineOfCode.text")); //$NON-NLS-1$
			contentPanel.add(lblErroneousLineOfCode, "2, 20, left, default");
		}
		{
			final String text = ( method.getErroreousLineOfCode() != null )
				? method.getErroreousLineOfCode().trim() : "";
			erroneousLineOfCodeField = new JTextField();
			erroneousLineOfCodeField.setText( text );
			erroneousLineOfCodeField.setName("erroneousLineOfCodeField");
			erroneousLineOfCodeField.setEditable(false);
			erroneousLineOfCodeField.setColumns(10);
			contentPanel.add(erroneousLineOfCodeField, "4, 20, fill, default");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			buttonPane.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
			getContentPane().add( buttonPane, BorderLayout.SOUTH );
			{
				JButton closeButton = new JButton( Messages.getString("ReportItemDialog.closeButton.text") ); //$NON-NLS-1$
				closeButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible( false );
					}
				});
				closeButton.setName("closeButton");
				closeButton.setActionCommand( "Close" );
				buttonPane.add( closeButton );
			}
		}
	}

}
