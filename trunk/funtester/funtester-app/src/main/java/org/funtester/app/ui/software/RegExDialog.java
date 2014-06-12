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
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.DefaultButtons;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.UIValidationHelper;
import org.funtester.app.validation.RegExValidator;
import org.funtester.core.software.RegEx;
import org.funtester.core.software.Software;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * RegEx dialog
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class RegExDialog extends JDialog {

	private static final long serialVersionUID = 6481327720101783387L;
	
	private final Software software;
	private final RegEx regEx = new RegEx();
	private boolean confirmed = false;
	
	private final JPanel contentPanel = new JPanel();
	private final JTextField name;
	private final JTextField expression;
	private JTextField testValue;
	private JLabel resultValue;
	

	public RegExDialog(Software software) {
		this.software = software;
		
		setModal( true );
		setTitle( Messages.alt( "_REGEX", "Regular Expression" ) );
		setIconImage( ImageUtil.loadImage( ImagePath.regExIcon() ) );
		setBounds( 100, 100, 636, 238 );

		getContentPane().setLayout( new BorderLayout() );
		contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		contentPanel.setLayout(
				new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("73px"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("335px:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,})				
				);

		JLabel lblName = new JLabel( Messages.getString("RegExDialog.lblName.text") ); //$NON-NLS-1$
		contentPanel.add(lblName, "2, 2, fill, center");
		
		name = new JTextField();
		name.setName( "name" );
		contentPanel.add(name, "4, 2, 3, 1, fill, fill");
		name.setColumns(30);
		
		JLabel lblExpresion = new JLabel( Messages.getString("RegExDialog.lblExpresion.text") ); //$NON-NLS-1$
		contentPanel.add(lblExpresion, "2, 4, left, top");
		
		expression = new JTextField();
		expression.setName( "expression" );
		contentPanel.add(expression, "4, 4, 3, 1, fill, fill");
		expression.setColumns(200);
		
		JPanel testPanel = new JPanel();
		testPanel.setName("testPanel");
		testPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), Messages.getString("RegExDialog.testPanel.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-2$
		contentPanel.add(testPanel, "2, 7, 5, 1, fill, fill");
		testPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(30dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblValue = new JLabel(Messages.getString("RegExDialog.lblValue.text")); //$NON-NLS-1$
		testPanel.add(lblValue, "2, 2, left, default");
		
		testValue = new JTextField();
		testValue.setName("testValue");
		testPanel.add(testValue, "4, 2, fill, default");
		testValue.setColumns(10);
		
		JButton testButton = new JButton(Messages.getString("RegExDialog.testButton.text")); //$NON-NLS-1$
		testButton.addActionListener(new ActionListener() {
			public void actionPerformed(
					ActionEvent e) {
				if ( ! validateRegEx() ) { return; }
				
				/*
				if ( expression.getText().isEmpty() ) {
					final String msg = "Expressão vazia"; // TODO i18n
					MsgUtil.info( contentPanel, msg, getTitle() );
					expression.requestFocusInWindow();
					return;
				}
				
				try {
					Pattern.compile( expression.getText() );
				} catch (Exception ex1) {
					resultValue.setText( "Invalid expression: " + ex1.getLocalizedMessage() );
					expression.requestFocusInWindow();
					return;
				}
				*/
				
				try {
					boolean match = testValue.getText().matches( expression.getText() );
					resultValue.setText( match ? "match" : "doesn't match" );
					testValue.requestFocusInWindow();
				} catch (Exception ex) {
					resultValue.setText( "doesn't match: " + ex.getLocalizedMessage() );
					testValue.requestFocusInWindow();
				}
			}
		});
		testButton.setName("testButton");
		testPanel.add(testButton, "6, 2");
		
		JButton clearButton = new JButton( Messages.getString("RegExDialog.clearButton.text") ); //$NON-NLS-1$
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testValue.setText( "" );
				testValue.requestFocusInWindow();
			}
		});
		clearButton.setName("clearButton");
		testPanel.add(clearButton, "8, 2");
		
		JLabel lblResult = new JLabel("Result:");
		testPanel.add(lblResult, "2, 4, left, default");
		
		resultValue = new JLabel(Messages.getString("RegExDialog.resultValue.text")); //$NON-NLS-1$
		resultValue.setName("resultValue");
		testPanel.add(resultValue, "4, 4, 3, 1");

		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new MatteBorder(1, 0, 0, 0, (Color) SystemColor.scrollbar));
		buttonPane.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
		getContentPane().add( buttonPane, BorderLayout.SOUTH );

		JButton okButton = DefaultButtons.createOkButton();
		buttonPane.add( okButton );
		okButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( ! validateRegEx() ) { return; }
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
		
		drawRegEx();
	}

	public void setRegEx(final RegEx other) {
		regEx.copy( other );
		drawRegEx();
	}

	public RegEx getRegEx() {
		regEx.setName( name.getText() );
		regEx.setExpression( expression.getText() );
		return regEx;
	}

	public boolean isConfirmed() {
		return confirmed;
	}
	
	private void drawRegEx() {
		name.setText( regEx.getName() );
		expression.setText( regEx.getExpression() );
	}	
	
	private boolean validateRegEx() {
		return UIValidationHelper.validate(
				contentPanel, getTitle(), new RegExValidator( software ), getRegEx() );
	}
}
