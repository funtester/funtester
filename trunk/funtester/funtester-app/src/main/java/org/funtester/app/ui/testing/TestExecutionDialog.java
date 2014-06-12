package org.funtester.app.ui.testing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker.StateValue;
import javax.swing.border.EmptyBorder;

import org.funtester.app.common.MessageListener;
import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.ImageUtil;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Test execution dialog.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TestExecutionDialog extends JDialog
		implements MessageListener
		, PropertyChangeListener
		{

	private static final long serialVersionUID = 8761354846680052911L;
	
	private final JPanel contentPanel = new JPanel();
	private final JTextArea output;

	/**
	 * Create the dialog.
	 */
	public TestExecutionDialog(JFrame owner) {
		super( owner );
		
		setTitle(Messages.getString("TestExecutionDialog.this.title")); //$NON-NLS-1$
		setIconImage( ImageUtil.loadImage( ImagePath.consoleIcon() ) );
		setBounds( 100, 100, 891, 566 );
		getContentPane().setLayout( new BorderLayout() );
		contentPanel.setBorder( new EmptyBorder(0, 0, 5, 0) );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		
		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, "1, 1, fill, fill");
		
		output = new JTextArea();
		output.setFont(new Font("Consolas", Font.PLAIN, 14));
		output.setName("output");
		output.setLineWrap(true);
		output.setForeground(Color.GREEN);
		output.setBackground(Color.BLACK);
		scrollPane.setViewportView(output);

		JPanel buttonPane = new JPanel();
		getContentPane().add( buttonPane, BorderLayout.SOUTH );

		ActionListener closeAL = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible( false );
				dispose();
			}
		}; 
		
		JButton closeButton =  new JButton( Messages.getString("TestExecutionDialog.closeButton.text") ); //$NON-NLS-1$
		closeButton.addActionListener( closeAL );
		buttonPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JButton upButton = new JButton( ImageUtil.loadIcon( ImagePath.fontSizeUp() ) );
		upButton.setToolTipText(Messages.getString("TestExecutionDialog.upButton.toolTipText")); //$NON-NLS-1$
		upButton.setName("upButton");
		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				float size = output.getFont().getSize();
				output.setFont( output.getFont().deriveFont( size + 1 ) );
			}
		});
		buttonPane.add(upButton, "2, 2");
		
		JButton downButton = new JButton( ImageUtil.loadIcon( ImagePath.fontSizeDown() ) );
		downButton.setToolTipText(Messages.getString("TestExecutionDialog.downButton.toolTipText")); //$NON-NLS-1$
		downButton.setName("downButton");
		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				float size = output.getFont().getSize();
				output.setFont( output.getFont().deriveFont( size - 1 ) );
			}
		});
		buttonPane.add(downButton, "4, 2");
		
		JButton copyButton = new JButton( "" );
		copyButton.setToolTipText(Messages.getString("TestExecutionDialog.copyButton.toolTipText")); //$NON-NLS-1$
		copyButton.setName("copyButton");
		copyButton.setIcon( ImageUtil.loadIcon( ImagePath.copyIcon() ) );
		
		copyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized( output ) {
					StringSelection stringSelection = new StringSelection ( output.getText() );
					Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
					clpbrd.setContents( stringSelection, null );
				}
			}
		});
		
		buttonPane.add(copyButton, "6, 2, center, default");
		closeButton.setName( "closeButton" );
		closeButton.setActionCommand( "Close" );
		buttonPane.add( closeButton, "9, 2, fill, top" );
		getRootPane().setDefaultButton( closeButton );
		
		// Register the ESC key to call the cancelActionListener
		getRootPane().registerKeyboardAction(
				closeAL,
				KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ),
				JComponent.WHEN_IN_FOCUSED_WINDOW
				);
	}

	@Override
	public void published(final String message) {
		output.append( message );
		output.append( "\n" );
		output.setCaretPosition( output.getText().length() );
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		if ( evt.getPropertyName().equals( "state" ) ) {
			
			if ( evt.getNewValue() == StateValue.STARTED ) {
				
				setTitle( Messages.getString( "TestExecutionDialog.this.title" )
						+ " - " + Messages.alt( "_IN_PROGRESS", "In progresss..." ) );
				
			} else if ( evt.getNewValue() == StateValue.DONE ) {
				
				setTitle( Messages.getString( "TestExecutionDialog.this.title" )
						+ " - " + Messages.alt( "_FINISHED", "Finished" ) );
				
			}
		}
	}
}
