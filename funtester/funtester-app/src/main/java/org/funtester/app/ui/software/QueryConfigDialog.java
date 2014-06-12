package org.funtester.app.ui.software;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.DefaultButtons;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.DocumentAutoCompleter;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.app.ui.util.UIUtil;
import org.funtester.app.ui.util.UIValidationHelper;
import org.funtester.app.ui.util.thirdparty.FocusUtil;
import org.funtester.app.util.SQLUtil;
import org.funtester.app.validation.QueryConfigValidator;
import org.funtester.common.util.ConnectionFactory;
import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.software.DatabaseConfig;
import org.funtester.core.software.QueryConfig;
import org.funtester.core.software.Software;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Query config dialog
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class QueryConfigDialog extends JDialog {

	private static final long serialVersionUID = -6191880760650566886L;
	
	private Software software;
	private final DriverCache driverCache;
	private final QueryConfig queryConfig = new QueryConfig();
	private boolean confirmed = false;
	private boolean editable = true;

	private final JPanel contentPanel = new JPanel();
	@SuppressWarnings("rawtypes")
	private final JComboBox database;
	private final JTextField name;
	private final JScrollPane commandScrollPane;
	private final JTextArea command;
	private final JButton testButton;
	private final JCheckBox viewResults;
	private final JCheckBox limit;
	private final JSpinner rows;
	private final JLabel lblRows;
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public QueryConfigDialog(
			final Software software,
			final DriverCache driverCache
			) {
		this.software = software;
		this.driverCache = driverCache;
		
		setIconImage( ImageUtil.loadImage( ImagePath.queryIcon() ) );
		setModal( true );
		setTitle(Messages.getString("QueryConfigDialog.this.title")); //$NON-NLS-1$
		setBounds( 100, 100, 718, 372 );
		getContentPane().setLayout( new BorderLayout() );
		contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:max(57dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblDatabase = new JLabel(Messages.getString("QueryConfigDialog.lblDatabase.text")); //$NON-NLS-1$
		contentPanel.add(lblDatabase, "2, 2, left, default");
		
		database = new JComboBox( software.getDatabaseConfigurations().toArray( new DatabaseConfig[ 0 ] ));
		database.setName("database");
		contentPanel.add(database, "4, 2, fill, default");
		
		JLabel lblName = new JLabel(Messages.getString("QueryConfigDialog.lblName.text")); //$NON-NLS-1$
		contentPanel.add(lblName, "2, 4, left, default");
		
		name = new JTextField();
		name.setName("name");
		contentPanel.add(name, "4, 4, fill, default");
		name.setColumns(10);
		
		JLabel lblCommand = new JLabel(Messages.getString("QueryConfigDialog.lblCommand.text")); //$NON-NLS-1$
		contentPanel.add(lblCommand, "2, 6, default, top");
		
		commandScrollPane = new JScrollPane();
		commandScrollPane.setName("commandScrollPane");
		contentPanel.add(commandScrollPane, "4, 6, fill, fill");
		
		command = new JTextArea();
		command.setName("command");
		command.setFont( name.getFont() ); // Same Font as name
		FocusUtil.patch( command );	
		commandScrollPane.setViewportView(command);
		
		List< String > words =  new ArrayList< String >();
		Collections.addAll( words, SQLUtil.SQL_WORDS );
		( new DocumentAutoCompleter( command, words ) ).setMinChars( 1 );
		
		JPanel panel = new JPanel();
		contentPanel.add(panel, "4, 8, fill, fill");
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		testButton = new JButton(Messages.getString("QueryConfigDialog.testButton.text")); //$NON-NLS-1$
		testButton.setIcon( ImageUtil.loadIcon( ImagePath.scriptTestIcon() ) );
		testButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testCommand();
			}
		});
		panel.add(testButton, "1, 2");
		
		viewResults = new JCheckBox( Messages.getString("QueryConfigDialog.viewResults.text") ); //$NON-NLS-1$
		viewResults.setName( "viewResults" );		
		viewResults.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateLimitState( canEnableLimit() );
			}
		});
		panel.add(viewResults, "3, 2");
		
		limit = new JCheckBox(Messages.getString("QueryConfigDialog.limit.text")); //$NON-NLS-1$
		limit.setSelected( true );
		limit.setName( "limit" );
		panel.add(limit, "7, 2");
		
		rows = new JSpinner();
		rows.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(10)));
		rows.setValue( new Integer( 10 ) );
		panel.add(rows, "9, 2");
		
		lblRows = new JLabel(Messages.getString("QueryConfigDialog.lblRows.text")); //$NON-NLS-1$
		panel.add(lblRows, "11, 2");
		
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
				if ( ! validateQueryConfig() ) { return; }
				setVisible( false );
				confirmed = true;
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
		
		updateLimitState( canEnableLimit() );
	}

	public boolean isConfirmed() {
		return confirmed;
	}
	
	public QueryConfig getQueryConfig() {
		queryConfig.setDatabaseConfig( (DatabaseConfig) database.getSelectedItem() );
		queryConfig.setName( name.getText() );
		queryConfig.setCommand( command.getText() );
		
		return queryConfig;
	}
	
	public void setQueryConfig(QueryConfig that) {
		queryConfig.copy( that );
		draw();
	}
	
	public void selectDatabaseConfig(final DatabaseConfig dbc) {
		database.setSelectedItem( dbc );
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	public void setEditable(final boolean editable) {
		this.editable = editable;
		
		database.setEnabled( editable );
		name.setEditable( editable );
		command.setEditable( editable );
		command.setBackground( name.getBackground() );
		
		testButton.setEnabled( editable );
		viewResults.setEnabled( editable );
		updateLimitState( editable );
	}
	
	public JPanel getContentPanel() {
		return contentPanel;
	}

	private void draw() {
		database.setSelectedItem( queryConfig.getDatabaseConfig() );
		name.setText( queryConfig.getName() );
		command.setText( queryConfig.getCommand() );
		
		// Database selection must be enable when editable AND new query
		database.setEnabled( editable && 0 == queryConfig.getId() );
	}
	
	private boolean validateQueryConfig() {
		return UIValidationHelper.validate( contentPanel, getTitle(),
				new QueryConfigValidator( this.software ), getQueryConfig() );
	}
	
	// TODO refactor
	private void testCommand() {
		if ( ! validateQueryConfig() ) { return; }
		
		final DatabaseConfig dbc = queryConfig.getDatabaseConfig();
		
		Driver driver = driverCache.get( dbc.getDriver() );
		if ( null == driver ) {
			String msg = String.format( Messages.getString( "_JDBC_DRIVER_UNAVAILABLE" ),
					dbc.getDriver() );
			MsgUtil.error( contentPanel, msg, getTitle() );
			return;
		}
		
		java.sql.Connection c;
		try {
			c = ConnectionFactory.createConnection( driver, dbc.toJdbcUrl(),
					dbc.getUser(), dbc.getPassword(), dbc.getDialect() );
			
			if ( c != null ) {
				c.setAutoCommit( false ); // Important to prevent modifying the database
			}
		/*
		} catch ( FileNotFoundException e ) {
			String msg = String.format( Messages.getString( "_JDBC_DRIVER_NOT_FOUND" ),
					e.getLocalizedMessage() );
			MsgUtil.error( contentPanel, msg, getTitle() );
			return;
		} catch ( DriverNotAvailableException e ) {			
			String msg = String.format( Messages.getString( "_JDBC_DRIVER_UNAVAILABLE" ),
					e.getLocalizedMessage() );
			MsgUtil.error( contentPanel, msg, getTitle() );
			return;
		} catch ( DriverLoadException e ) {
			final String msg = String.format( Messages.getString( "_JDBC_DRIVER_LOAD_ERROR" ),
					e.getLocalizedMessage() );
			MsgUtil.error( contentPanel, msg, getTitle() );
			return;
		*/
		} catch ( SQLException e ) {
			String msg = String.format(
					Messages.getString( "_DATABASE_CONNECTION_ERROR" ),
					e.getLocalizedMessage()
					);
			MsgUtil.error( contentPanel, msg, getTitle() );
			return;
		} catch ( Exception e ) {
			MsgUtil.error( contentPanel, e.getLocalizedMessage(), getTitle() );
			return;
		}
		
		if ( null == c ) {
			String msg = "Could not create the connection."; // TODO i18n
			MsgUtil.error( contentPanel, msg, getTitle() );
			return;
		}
		
		PreparedStatement ps;
		try {
			ps = c.prepareStatement( queryConfig.getCommand() );
			final int paramCount = ps.getParameterMetaData().getParameterCount();
			if ( paramCount > 0 ) {
				final boolean canContinue = askForParameters( ps );
				if ( ! canContinue ) {
					return;
				}
			}
			
			if ( ! ps.execute() ) {
				final String msg = "Error"; // TODO i18n
				MsgUtil.info( contentPanel, msg, getTitle() );
			} else if ( viewResults.isSelected() ) {
				final ResultSet rs = ps.getResultSet();
				final int MAX_ROWS = ( limit.isSelected() )
						? (Integer) rows.getValue()
						: QueryResultDialog.UNLIMITED_ROWS; 
				showQueryResult( rs, MAX_ROWS );
				rs.close();
			} else {
				final String msg = Messages.alt( "_SUCCESS", "Success!" );
				MsgUtil.info( contentPanel, msg, getTitle() );
			}
		} catch ( Exception e ) {			
			final String msg = String.format( // TODO i18n
					"Erro ao tentar preparar o comando.\nDetalhes: %s",
					e.getLocalizedMessage() ); 
			MsgUtil.error( contentPanel, msg, getTitle() );
		} finally {
			try {
				// Undo the executed command, for safety
				if (  c != null ) c.rollback();
			} catch ( SQLException e ) {
				// Suppress exceptions
			}
		}
	}

	private void showQueryResult(final ResultSet rs, final int maxRows)
			throws Exception {
		QueryResultDialog qrDlg = new QueryResultDialog( rs, maxRows );
		UIUtil.centerOnScreen( qrDlg );
		qrDlg.setVisible( true );
	}

	private boolean askForParameters(PreparedStatement ps) throws SQLException {
		QueryParameterDialog dlg = new QueryParameterDialog( ps.getParameterMetaData() );
		UIUtil.centerOnScreen( dlg );
		dlg.setVisible( true );
		final boolean confirmed = dlg.isConfirmed();
		if ( confirmed ) {
			List< Object > values = dlg.getValues();
			int index = 1;
			for ( Object o : values ) {
				ps.setObject( index, o ); // Set the parameter value
				index++;
			}
		}
		return confirmed;
	}
	
	private boolean canEnableLimit() {
		return editable && viewResults.isSelected();
	}
	
	private void updateLimitState(final boolean enabled) {
		limit.setEnabled( enabled );
		rows.setEnabled( enabled );
		// Update the Color
		lblRows.setForeground( limit.getForeground() );
	}	
}
