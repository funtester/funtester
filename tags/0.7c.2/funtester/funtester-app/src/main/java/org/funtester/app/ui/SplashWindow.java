package org.funtester.app.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.funtester.app.startup.StartupListener;
import org.funtester.app.ui.common.ImagePath;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Splash Window
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class SplashWindow extends JWindow implements StartupListener {
	
	private static final long serialVersionUID = 8233015489235839073L;

	private JPanel contentPane;
	private JLabel lblVersion;
	private JLabel lblStatus;	

	public SplashWindow() {
		setBounds( 1, 1, 321, 192 );
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder( new LineBorder(new Color(0, 0, 0)) );
		setContentPane( contentPane );
		
		JLabel lblImage = new JLabel(""); //$NON-NLS-1$
		lblImage.setIcon( new ImageIcon( ImagePath.splashImage() ) );
		lblImage.setBackground(Color.WHITE);
		lblImage.setHorizontalAlignment( SwingConstants.CENTER );
		lblImage.setVerticalAlignment( SwingConstants.CENTER );
		
		lblVersion = new JLabel( "" );
		lblVersion.setForeground(Color.DARK_GRAY);
		lblVersion.setFont(new Font("Arial Black", Font.BOLD, 13));
		lblVersion.setHorizontalAlignment( SwingConstants.CENTER );	
		
		lblStatus = new JLabel( "" );
		lblStatus.setBackground(Color.WHITE);
		lblStatus.setFont(new Font("Verdana", Font.PLAIN, 10));
		lblStatus.setHorizontalAlignment( SwingConstants.CENTER );	
		lblStatus.setText( "..." );
		
		FormLayout layout = new FormLayout(new ColumnSpec[] {
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("max(147dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				RowSpec.decode("max(70dlu;min)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,});
		
		contentPane.setLayout( layout );
		contentPane.add(lblImage, "2, 2, fill, fill");
		contentPane.add(lblVersion, "2, 4, fill, bottom");
		contentPane.add(lblStatus, "2, 6, center, center");
		
		// Centering on the screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = ( screen.width - getWidth() ) / 2;
		int y = ( screen.height - getHeight() ) / 2;

		setBounds( x, y, getWidth(), getHeight() );
		
		setVisible( true );
	}

	public void setVersion(final String version) {
		lblVersion.setText( version );
		lblVersion.updateUI();
	}

	public void setStatus(final String message) {
		lblStatus.setText( message );
		lblStatus.updateUI();
	}
	
	/**
	 * Set the window invisible and dispose it.
	 */
	public void finish() {
		setVisible( false );
		dispose();
	}
	
	@Override
	public void versionRead(final String version) {
		setVersion( version );
	}

	@Override
	public void statusUpdated(final String status) {
		setStatus( status );
	}
}