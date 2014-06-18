package org.funtester.app.ui.software;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.DefaultButtons;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.app.ui.util.UIValidationHelper;
import org.funtester.app.validation.DatabaseConfigValidator;
import org.funtester.common.util.ConnectionFactory;
import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.software.DatabaseConfig;
import org.funtester.core.software.DatabaseDriverConfig;
import org.funtester.core.software.Software;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Database config dialog
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DatabaseConfigDialog extends JDialog {

	private static final long serialVersionUID = -6191880760650566886L;
	
	private final DatabaseConfig databaseConfig = new DatabaseConfig();
	private boolean confirmed = false;
	private final Software software;
	private final DriverCache driverCache;
	
	//
	// Widgets
	//
	
	private final JPanel contentPanel = new JPanel();
	
	private final JLabel lblDriverTemplate;
	@SuppressWarnings("rawtypes")
	private final JComboBox driverTemplate;
	private final JButton applyButton;
	
	private JTextField name;
	private JTextField driver;
	private JTextField dialect;
	private JTextField type;
	private JTextField host;
	private JTextField port;
	private JTextField path;
	private JTextField user;
	private JPasswordField password;
	private JLabel lblJdbcURL;
	private JLabel jdbcURL;
	private JButton testConnectionButton;	

	/**
	 * Create the dialog.
	 * 
	 * @param software
	 * @param driversTypesMap
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DatabaseConfigDialog(
			final Software software,
			final Map< DatabaseDriverConfig, String > driverFileMap,
			final DriverCache driverCache
			) {
		this.software = software;
		this.driverCache = driverCache;
		
		setIconImage( ImageUtil.loadImage( ImagePath.databaseIcon() ) );
		setModal( true );
		setTitle(Messages.getString("DatabaseConfigDialog.this.title")); //$NON-NLS-1$
		setBounds( 100, 100, 582, 501 );
		getContentPane().setLayout( new BorderLayout() );
		contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		lblDriverTemplate = new JLabel( Messages.getString("DatabaseConfigDialog.lblDriverTemplate.text")); //$NON-NLS-1$
		contentPanel.add( lblDriverTemplate, "2, 2, right, default" );
		
		Set< String > driverNames = new LinkedHashSet();
		for ( DatabaseDriverConfig ddc : driverFileMap.keySet() ) {
			driverNames.add( ddc.getName() );
		}
		
		driverTemplate = new JComboBox( new DefaultComboBoxModel(
				driverNames.toArray( new String[ 0 ] ) ) );
		driverTemplate.setName( "driverTemplate" );
		driverTemplate.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if ( e.getStateChange() != ItemEvent.SELECTED ) {
					return;
				}
				applyButton.setEnabled( driverTemplate.getSelectedIndex() >= 0 );
				applyButton.updateUI();
			}
		});
		contentPanel.add(driverTemplate, "4, 2, 3, 1, fill, default");
		
		applyButton = new JButton(Messages.getString("DatabaseConfigDialog.applyButton.text")); //$NON-NLS-1$
		applyButton.setName( "applyButton" );
		contentPanel.add(applyButton, "8, 2");
		applyButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index =  driverTemplate.getSelectedIndex();
				if ( index < 0 ) { return; }
				
				String name = driverTemplate.getSelectedItem().toString();
				DatabaseDriverConfig selected = null;
				for ( DatabaseDriverConfig ddc : driverFileMap.keySet() ) {
					if ( ddc.getName().equalsIgnoreCase( name ) ) {
						selected = ddc;
						break;
					}
				}
				if ( null == selected ) { return; }
				
				final String msg = Messages.getString( "_DATABASE_ASK_OVERWRITE_SETTINGS" );
				if ( ! MsgUtil.yesTo( contentPanel, msg, getTitle() ) ) { return; }
				copyDriverTemplate( selected );
				draw();
			}
		} );
		
		JLabel lblName = new JLabel(Messages.getString("DatabaseConfigDialog.lblName.text")); //$NON-NLS-1$
		contentPanel.add(lblName, "2, 6, right, default");
		
		name = new JTextField();
		name.setName("name");
		contentPanel.add(name, "4, 6, 5, 1, fill, default");
		name.setColumns(10);
		
		JLabel lblDriver = new JLabel(Messages.getString("DatabaseConfigDialog.lblDriver.text")); //$NON-NLS-1$
		contentPanel.add(lblDriver, "2, 10, right, default");
		
		driver = new JTextField();
		driver.setName("driver");
		contentPanel.add(driver, "4, 10, 5, 1, fill, default");
		driver.setColumns(10);
		
		JLabel lblDialect = new JLabel(Messages.getString("DatabaseConfigDialog.lblDialect.text")); //$NON-NLS-1$
		contentPanel.add(lblDialect, "2, 12, right, default");
		
		dialect = new JTextField();
		dialect.setName("dialect");
		contentPanel.add(dialect, "4, 12, 5, 1, fill, default");
		dialect.setColumns(10);
		
		JLabel lblType = new JLabel(Messages.getString("DatabaseConfigDialog.lblType.text")); //$NON-NLS-1$
		contentPanel.add(lblType, "2, 14, right, default");
		
		type = new JTextField();
		type.setName("type");
		contentPanel.add(type, "4, 14, 5, 1, fill, default");
		type.setColumns(10);
		type.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				databaseConfig.setType( type.getText() );
				updateJdbcURL();
			}
		});
		
		JLabel lblHost = new JLabel(Messages.getString("DatabaseConfigDialog.lblHost.text")); //$NON-NLS-1$
		contentPanel.add(lblHost, "2, 16, right, default");
		
		host = new JTextField();
		host.setName("host");
		contentPanel.add(host, "4, 16, 5, 1, fill, default");
		host.setColumns(10);
		host.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				databaseConfig.setHost( host.getText() );
				updateJdbcURL();				
			}
		});
		
		JLabel lblPort = new JLabel(Messages.getString("DatabaseConfigDialog.lblPort.text")); //$NON-NLS-1$
		contentPanel.add(lblPort, "2, 18, right, default");
		
		port = new JTextField();
		port.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				Integer portValue;
				try {
					portValue = Integer.valueOf( port.getText() );
				} catch (Exception ex) {
					portValue = 0;
				}
				databaseConfig.setPort( portValue );
				updateJdbcURL();
			}
		});
		port.setName("port");
		contentPanel.add(port, "4, 18, fill, default");
		port.setColumns(10);
		
		JLabel lblPath = new JLabel(Messages.getString("DatabaseConfigDialog.lblPath.text")); //$NON-NLS-1$
		contentPanel.add(lblPath, "2, 20, right, default");
		
		path = new JTextField();
		path.setName("path");		
		contentPanel.add(path, "4, 20, 5, 1, fill, default");
		path.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				databaseConfig.setPath( path.getText() );
				updateJdbcURL();
			}
		});		
		path.setColumns(10);
		
		JLabel lblUser = new JLabel(Messages.getString("DatabaseConfigDialog.lblUser.text")); //$NON-NLS-1$
		contentPanel.add(lblUser, "2, 22, right, default");
		
		user = new JTextField();
		user.setName("user");
		contentPanel.add(user, "4, 22, 5, 1, fill, default");
		user.setColumns(10);
		
		JLabel lblPassword = new JLabel(Messages.getString("DatabaseConfigDialog.lblPassword.text")); //$NON-NLS-1$
		contentPanel.add(lblPassword, "2, 24, right, default");
		
		password = new JPasswordField();
		contentPanel.add(password, "4, 24, 5, 1, fill, default");
		
		lblJdbcURL = new JLabel(Messages.getString("DatabaseConfigDialog.lblJdbcURL.text")); //$NON-NLS-1$
		contentPanel.add(lblJdbcURL, "2, 26");
		
		jdbcURL = new JLabel("");
		contentPanel.add(jdbcURL, "4, 26, 5, 1");
		
		testConnectionButton = new JButton(Messages.getString("DatabaseConfigDialog.testConnectionButton.text")); //$NON-NLS-1$
		testConnectionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testConnection();
			}
		});
		testConnectionButton.setIcon( ImageUtil.loadIcon( ImagePath.databaseConnectIcon() ) );
		testConnectionButton.setName( "testConnectionButton" );
		contentPanel.add(testConnectionButton, "4, 30");
		
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
				if ( ! validateDatabaseConfig() ) { return; }
				confirmed = true;
				setVisible( false );
			}
		} );		

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
		
		draw();
	}

	public boolean isConfirmed() {
		return confirmed;
	}
	
	public DatabaseConfig getDatabaseConfig() {
		
		databaseConfig.setName( name.getText() );
		databaseConfig.setDriver( driver.getText() );
		databaseConfig.setDialect( dialect.getText() );
		databaseConfig.setType( type.getText() );
		databaseConfig.setHost( host.getText() );
		if ( port.getText().matches( "[0-9]{1,5}" ) ) {
			int portNumber = Integer.parseInt( port.getText() );
			databaseConfig.setPort( portNumber );
		} else {
			databaseConfig.setPort( 0 );
		}
		databaseConfig.setPath( path.getText() );
		databaseConfig.setUser( user.getText() );
		databaseConfig.setPassword( new String( password.getPassword() ) );
		
		return databaseConfig;
	}
	
	public void setDatabaseConfig(DatabaseConfig that) {
		databaseConfig.copy( that );
		draw();
	}
	
	public void setEditable(final boolean editable) {
		lblDriverTemplate.setVisible( editable );
		driverTemplate.setEnabled( editable );
		driverTemplate.setVisible( editable );
		applyButton.setEnabled( editable );
		applyButton.setVisible( editable );
		
		name.setEditable( editable );
		driver.setEditable( editable );
		dialect.setEditable( editable );
		type.setEditable( editable );
		host.setEditable( editable );
		port.setEditable( editable );
		path.setEditable( editable );
		user.setEditable( editable );
		password.setEditable( editable );
		// buttons too ?
	}
	
	public JPanel getContentPanel() {
		return contentPanel;
	}

	private void draw() {
		driverTemplate.setSelectedItem( null ); // None selected
		
		name.setText( databaseConfig.getName() );
		driver.setText( databaseConfig.getDriver() );
		dialect.setText( databaseConfig.getDialect() );
		type.setText( databaseConfig.getType() );
		host.setText( databaseConfig.getHost() );
		port.setText( new Integer( databaseConfig.getPort() ).toString() );
		path.setText( databaseConfig.getPath() );
		user.setText( databaseConfig.getUser() );
		password.setText( databaseConfig.getPassword() );
		jdbcURL.setText( databaseConfig.toJdbcUrl() );
	}
	
	private boolean validateDatabaseConfig() {
		return UIValidationHelper.validate(
				contentPanel,
				getTitle(),
				new DatabaseConfigValidator( this.software ),
				getDatabaseConfig() );
	}
	
	private void updateJdbcURL() {
		jdbcURL.setText( databaseConfig.toJdbcUrl() );
		jdbcURL.updateUI();
	}
	
	/**
	 * Try to connect with the database configuration.
	 */
	private void testConnection() {
		
		if ( ! validateDatabaseConfig() ) { return; }
		final DatabaseConfig dbc = getDatabaseConfig();
		
		Driver driver = driverCache.get( dbc.getDriver() );
		if ( null == driver ) {
			String msg = String.format( Messages.getString( "_JDBC_DRIVER_UNAVAILABLE" ),
					dbc.getDriver() );
			MsgUtil.error( contentPanel, msg, getTitle() );
			return;
		}
		
		try {
			ConnectionFactory.createConnection( driver, dbc.toJdbcUrl(),
					dbc.getUser(), dbc.getPassword(), dbc.getDialect() );
			final String msg = Messages.alt( "_CONNECTED", "Connected!" );
			MsgUtil.info( contentPanel, msg, getTitle() );
		/*
		} catch ( FileNotFoundException e ) {
			String msg = String.format( Messages.getString( "_JDBC_DRIVER_NOT_FOUND" ),
					e.getLocalizedMessage() );
			MsgUtil.error( contentPanel, msg, getTitle() );
		} catch ( DriverNotAvailableException e ) {			
			String msg = String.format( Messages.getString( "_JDBC_DRIVER_UNAVAILABLE" ),
					e.getLocalizedMessage() );
			MsgUtil.error( contentPanel, msg, getTitle() );
		} catch ( DriverLoadException e ) {
			final String msg = String.format( Messages.getString( "_JDBC_DRIVER_LOAD_ERROR" ),
					e.getLocalizedMessage() );
			MsgUtil.error( contentPanel, msg, getTitle() );
		*/
		} catch ( SQLException e ) {
			String msg = String.format(
					Messages.getString( "_DATABASE_CONNECTION_ERROR" ),
					e.getLocalizedMessage()
					);
			MsgUtil.error( contentPanel, msg, getTitle() );
		} catch ( Exception e ) {
			MsgUtil.error( contentPanel, e.getLocalizedMessage(), getTitle() );
		}
	}
	
	private void copyDriverTemplate(final DatabaseDriverConfig obj) {
		databaseConfig.setType( obj.getType() );
		databaseConfig.setDriver( obj.getDriverClass() );
		databaseConfig.setPort( obj.getDefaultPort() );
		databaseConfig.setUser( obj.getDefaultUser() );
		databaseConfig.setPassword( obj.getDefaultPassword() );
	}	
}
