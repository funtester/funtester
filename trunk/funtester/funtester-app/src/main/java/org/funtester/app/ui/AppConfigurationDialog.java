package org.funtester.app.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppConfiguration;
import org.funtester.app.project.AppConfigurationManager;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.ImageUtil;

/**
 * Application configuration dialog
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class AppConfigurationDialog extends JDialog {

	private static final long serialVersionUID = 347149445381994053L;
	private final AppConfigurationPanel contentPanel;

	/**
	 * Create the dialog.
	 */
	public AppConfigurationDialog(
			final AppConfigurationManager manager
			) {	
		if ( null == manager ) throw new IllegalArgumentException( "manager is null" );
		if ( null == manager.getLocales() ) throw new IllegalArgumentException( "manager locales is null" );
		if ( null == manager.getLookAndFeels() ) throw new IllegalArgumentException( "manager look-and-feels is null" );
		
		setIconImage( ImageUtil.loadImage( ImagePath.configurationIcon() ) );
		setModal(true);
		setTitle(Messages.getString("AppConfigurationDialog.this.title")); //$NON-NLS-1$
		setBounds( 100, 100, 714, 353 );
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		contentPanel = new AppConfigurationPanel(
				manager.getLocales(),
				manager.getLookAndFeels()
				);
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
			getContentPane().add( buttonPane, BorderLayout.SOUTH );
			{
				JButton okButton = new JButton( Messages.getString( "_OK" ) );
				okButton.setIcon( ImageUtil.loadIcon( ImagePath.okIcon() ) );
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							manager.save( contentPanel.get() );
							dispose();
						} catch ( Exception e1 ) {
							JOptionPane.showMessageDialog( contentPanel,
									e1.getLocalizedMessage(), getTitle(),
									JOptionPane.ERROR_MESSAGE );
						}
					}
				});
				{
					JLabel lblTip = new JLabel(Messages.getString("AppConfigurationDialog.lblTip.text")); //$NON-NLS-1$
					lblTip.setForeground(SystemColor.textInactiveText);
					buttonPane.add(lblTip);
				}
				okButton.setActionCommand( "OK" );
				buttonPane.add( okButton );
				getRootPane().setDefaultButton( okButton );
			}
			{
				JButton cancelButton = new JButton( Messages.getString( "_CANCEL" ) );
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setIcon( ImageUtil.loadIcon( ImagePath.cancelIcon() ) );
				cancelButton.setActionCommand( "Cancel" );
				buttonPane.add( cancelButton );
			}
		}
	}

	public void draw(AppConfiguration configuration) {
		contentPanel.draw( configuration );
	}

}
