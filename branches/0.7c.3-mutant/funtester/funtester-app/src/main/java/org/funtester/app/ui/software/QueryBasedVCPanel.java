package org.funtester.app.ui.software;

import java.awt.Cursor;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Driver;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.EnumTranslation;
import org.funtester.app.ui.software.actions.QueryConfigActionContainer;
import org.funtester.app.ui.util.BaseTableModel;
import org.funtester.app.ui.util.CRUDTablePanel;
import org.funtester.app.ui.util.JTableUtil;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.app.ui.util.UIUtil;
import org.funtester.app.validation.ValueConfigurationValidator;
import org.funtester.common.util.ConnectionFactory;
import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.software.DatabaseConfig;
import org.funtester.core.software.Element;
import org.funtester.core.software.ParameterConfig;
import org.funtester.core.software.QueryBasedVC;
import org.funtester.core.software.QueryConfig;
import org.funtester.core.software.Software;
import org.funtester.core.software.ValueConfiguration;
import org.funtester.core.software.ValueConfigurationKind;
import org.funtester.core.software.ValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Panel for a {@link QueryBasedVC}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class QueryBasedVCPanel extends VCPanel {

	private static final long serialVersionUID = -4038302367089540858L;
	
	private static Logger logger = LoggerFactory.getLogger( QueryBasedVCPanel.class );
	
	/** The query-based value configuration */
	private final QueryBasedVC valueConfiguration = new QueryBasedVC();
	
	/** A map for all the available query configurations. Each key is
	 * create with {@link QueryBasedVCPanel#makeQueryKey(QueryConfig)} */
	private final Map< String, QueryConfig > queryMap;
	
	/** A map with the columns of each {@link QueryConfig}.
	 * The values are created on demand. */
	private final Map< QueryConfig, List< String > > columnsMap;
	
	/** A cache for database connections. The values are created on demand. */
	private final Map< DatabaseConfig, java.sql.Connection > connectionsMap;
	
	/** A table model to present {@link ParameterConfig}s. */
	private BaseTableModel< ParameterConfig > parametersTM;
	
	//
	// Constructor parameters
	//
	private final ValueType valueType;
	private final Collection< Element > elements;
	private final Software software;
	private final DriverCache driverCache;

	//
	// Widgets
	//	
	private final JComboBox query;
	private final JButton newQueryButton;
	private final JTextArea command;
	private final CRUDTablePanel parametersPanel;
	private final JButton configureButton;
	private final JLabel lblTargetColumn;
	private final JComboBox targetColumn;

	/**
	 * Create the panel.
	 * 
	 * @param valueType
	 * @param elements			the elements of the use case.
	 * @param software			the current software.
	 * @param availableDrivers	the available drivers.
	 * @param queries			the queries of the software.
	 */
	public QueryBasedVCPanel(
			final ValueType valueType,
			Collection< Element > elements,
			Software software,
			final DriverCache driverCache,
			Collection< QueryConfig > queries
			) {
		
		this.valueType = valueType;
		this.elements = elements;
		this.software = software;
		this.driverCache = driverCache;
		
		this.columnsMap = new LinkedHashMap< QueryConfig, List< String > >();
		this.connectionsMap = new LinkedHashMap< DatabaseConfig, java.sql.Connection >();
		
		// Create a table model with empty parameters because the value configuration
		// is new at this point. This is necessary only to adjust the table. Later,
		// the setValueConfiguration will be called and it will rebuild the table model
		// from the (not new) value configuration.
		this.parametersTM = createParameterConfigTableModel( valueConfiguration.getParameters() );
		
		this.queryMap = new LinkedHashMap< String, QueryConfig >();
		for ( QueryConfig qc : queries ) {
			String key = makeQueryKey( qc );
			this.queryMap.put( key, qc );
		}
		
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblQuery = new JLabel(Messages.getString("QueryBasedVCPanel.lblQuery.text")); //$NON-NLS-1$
		add(lblQuery, "2, 2, left, default");
		
		query = new JComboBox( new DefaultComboBoxModel(
				queryMap.keySet().toArray( new String[ 0 ] )) );
		query.setName("query");
		
		query.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if ( e.getStateChange() == ItemEvent.SELECTED ) {
					if ( query.getSelectedIndex() < 0 ) { return; }
					QueryConfig qc = queryMap.get( query.getSelectedItem() );
					adjustIU( qc );
				}
			}
		});
		
		add(query, "4, 2, 3, 1, fill, default");
		
		newQueryButton = new JButton(Messages.getString("QueryBasedVCPanel.newQueryButton.text")); //$NON-NLS-1$
		newQueryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createQueryConfig();
			}
		});
		newQueryButton.setName("newQueryButton");
		add(newQueryButton, "8, 2");
		
		JLabel lblCommand = new JLabel(Messages.getString("QueryBasedVCPanel.lblCommand.text")); //$NON-NLS-1$
		lblCommand.setForeground(SystemColor.textInactiveText);
		add(lblCommand, "2, 4, left, top");
		
		command = new JTextArea();
		command.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		command.setBackground(SystemColor.menu);
		command.setLineWrap(true);
		command.setText("");
		command.setForeground(SystemColor.textInactiveText);
		command.setEditable(false);
		command.setName("command");
		add(command, "4, 4, 5, 1, fill, fill");
		
		lblTargetColumn = new JLabel(Messages.getString("QueryBasedVCPanel.lblTargetColumn.text")); //$NON-NLS-1$
		add(lblTargetColumn, "2, 6, left, default");
		
		targetColumn = new JComboBox();
		add(targetColumn, "4, 6, 5, 1, fill, default");
		
		JLabel lblParameters = new JLabel(Messages.getString("QueryBasedVCPanel.lblParameters.text")); //$NON-NLS-1$
		add(lblParameters, "2, 8, left, top");
		
		parametersPanel = new CRUDTablePanel();
		parametersPanel.setName( "parametersPanel" );
		parametersPanel.setTableModel( parametersTM );
		add(parametersPanel, "4, 8, 5, 1, fill, fill");
		
		adjustColumnsSize( parametersPanel.getTable() );
		
		configureButton = new JButton(Messages.getString("QueryBasedVCPanel.configureButton.text")); //$NON-NLS-1$
		configureButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				configureParameter();
			}
		});
		configureButton.setName("configureButton");
		add(configureButton, "4, 10");

		// 
		query.setSelectedIndex( -1 );
	}

	private void adjustColumnsSize(JTable table) {
		JTableUtil.adjustMinColumnWidths( table, new int[] { 30 } );
		JTableUtil.adjustMaxColumnWidths( table, new int[] { 30 } );
	}

	@Override
	public ValueConfiguration getValueConfiguration() throws Exception {
		
		// Query config
		if ( query.getSelectedIndex() >= 0 ) {
			QueryConfig qc = queryMap.get( query.getSelectedItem() );
			valueConfiguration.setQueryConfig( qc );
		} else {
			valueConfiguration.setQueryConfig( null );
		}
		
		// Target column
		if ( targetColumn.getSelectedIndex() >= 0 ) {
			valueConfiguration.setTargetColumn(
					(String) targetColumn.getSelectedItem() );
		} else {
			valueConfiguration.setTargetColumn( "" );
		}
		
		// Parameters are filled on user interaction
		
		
		// Validate
		ValueConfigurationValidator validator = new ValueConfigurationValidator( valueType );
		validator.validate( valueConfiguration );
		
		return valueConfiguration;
	}
	
	@Override
	public void setValueConfiguration(ValueConfiguration obj) {
		valueConfiguration.copy( obj );
		drawVC();
	}

	private void drawVC() {
		QueryConfig qc = valueConfiguration.getQueryConfig();
		if ( qc != null ) {
			query.setSelectedItem( makeQueryKey( qc ) );
			command.setText( qc.getCommand() );
			targetColumn.setSelectedItem( valueConfiguration.getTargetColumn() );
		} else {
			query.setSelectedIndex( -1 );
			command.setText( "" );
			targetColumn.setSelectedIndex( -1 );
		}
		
		
		logger.debug( "Parameters: " + valueConfiguration.getParameters() );
		
		parametersTM = createParameterConfigTableModel(
				valueConfiguration.getParameters() );
		
		parametersPanel.setTableModel( parametersTM );
		
		adjustColumnsSize( parametersPanel.getTable() );
	}
	
	private BaseTableModel< ParameterConfig > createParameterConfigTableModel(
			Collection< ParameterConfig > parameters
			) {
		final String columns[] = {
			"#",
			Messages.alt( "_VALUE_CONFIGURATION_KIND", "Value configuration" )
		};
		return new BaseTableModel< ParameterConfig >(
				columns.length,
				parameters,
				columns
				) {
			private static final long serialVersionUID = 1714955788460048165L;

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch ( columnIndex ) {
					case 0: return rowIndex + 1;
					case 1: {
						ValueConfiguration vc = itemAt( rowIndex ).getValueConfiguration();
						if ( vc != null ) {
							return EnumTranslation.translationForItem(
									ValueConfigurationKind.class,
									vc.kind()
									);
						} else {
							return "";
						}
					}
					default: return null;
				}
			}
		};
	}
	
	private void adjustIU(QueryConfig qc) {
		
		valueConfiguration.setQueryConfig( qc );
		
		// Show the query the command
		command.setText( qc.getCommand() );
		
		// Fill the target column combobox
		fillTargetColumnCombo( qc );
		
		// Adjust the parameters on the table model
		adjustParameters( qc );
	}
	
	/**
	 * Make a key using the query configuration.
	 * 
	 * @param qc
	 * @return
	 */
	private String makeQueryKey(final QueryConfig qc) {
		return qc.getDatabaseConfig().getName() + " - " + qc.getName();
	}

	/**
	 * Fill the combo using the columns loaded from the {@link QueryConfig}.
	 * 
	 * @param qc
	 */
	private void fillTargetColumnCombo(final QueryConfig qc) {
		
		// Get the column names from the map
		List< String > columnNames = columnsMap.get( qc );

		// Not in the list ?
		if ( null == columnNames ) {
			columnNames = extractColumnNamesFor( qc );
			if ( ! columnNames.isEmpty() ) {
				columnsMap.put( qc, columnNames );
			}
		}
		
		targetColumn.setModel( new DefaultComboBoxModel(
			columnNames.toArray( new String[ 0 ] ) ) );
			
		targetColumn.setSelectedIndex( -1 );
	}

	/**
	 * Return a list of columns. If there is a problem loading the query,
	 * return a empty list.
	 * 
	 * @param qc	the query to run.
	 * @return		a list of columns.
	 */
	private List< String > extractColumnNamesFor(final QueryConfig qc) {
		
		List< String > columnNames = new ArrayList< String >();
		
		DatabaseConfig dbc = qc.getDatabaseConfig();
		java.sql.Connection c = connectionFor( dbc );
		if ( null == c ) { return columnNames; } // Empty list
		
		PreparedStatement ps;
		try {
			ps = c.prepareStatement( qc.getCommand() );
			
			ResultSetMetaData meta = ps.getMetaData();
			if ( meta != null ) {
				
				
				final int count = meta.getColumnCount();
				if ( count > 0 ) {
					
					for ( int i = 1; i <= count; ++i ) {
						String columnName = ps.getMetaData().getColumnName( i );
						columnNames.add( columnName );
					}
				}
			} else {
				final String msg = "Error"; // TODO i18n
				MsgUtil.info( getRootPane(), msg, Messages.alt( "_ERROR", "Error" ) );
			}
			
		} catch ( Exception e ) {
			final String msg = String.format( // TODO i18n
					"Erro ao tentar preparar o comando.\nDetalhes: %s",
					e.getLocalizedMessage() ); 
			MsgUtil.error( getRootPane(), msg, Messages.alt( "_ERROR", "Error" ) );
		} finally {
			try {
				// Undo the executed command, for safety
				if (  c != null ) c.rollback();
			} catch ( SQLException e ) {
				// Suppress exceptions
			}
		}
		
		return columnNames;
	}

	
	private java.sql.Connection connectionFor(DatabaseConfig dbc) {
		java.sql.Connection c = connectionsMap.get( dbc );
		if ( null == c ) {
			Cursor cursor = getCursor();
			try {
				setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
				
				Driver driver = driverCache.get( dbc.getDriver() );

				if ( null == driver ) {
					String msg = String.format( Messages.getString( "_JDBC_DRIVER_UNAVAILABLE" ),
							dbc.getDriver() );
					MsgUtil.error( this, msg, Messages.alt( "_ERROR", "Error" ) );
					return null;
				}
				
				c = ConnectionFactory.createConnection( driver, dbc.toJdbcUrl(),
						dbc.getUser(), dbc.getPassword(), dbc.getDialect() );
				c.setAutoCommit( false ); // Important to prevent modifying the database
				
				connectionsMap.put( dbc, c );
			} catch ( Exception e1 ) {
				MsgUtil.error( getRootPane(), e1.getLocalizedMessage(),
						Messages.alt( "_ERROR", "Error" ) );
				return null;
				
			} finally {
				setCursor( cursor );
			}
		}
		return c;
	}
	

	private void adjustParameters(QueryConfig qc) {
		java.sql.Connection c = connectionFor( qc.getDatabaseConfig() );
		if ( null == c ) { return; }
	
		PreparedStatement ps;
		try {
			ps = c.prepareStatement( qc.getCommand() );
			
			ParameterMetaData metadata = ps.getParameterMetaData();
			if ( null == metadata ) {
				String msg = "Could not retrive the query parameters."; // TODO i18n
				MsgUtil.error( getRootPane(), msg, Messages.alt( "_ERROR", "Error" ) );
				return;
			}
			
			final int count = ps.getParameterMetaData().getParameterCount();
			
			// Make the number of parameters equal
			
			while ( parametersTM.getRowCount() > count ) {
				parametersTM.remove( parametersTM.getRowCount() - 1 );
			}
			
			while ( parametersTM.getRowCount() < count ) {
				parametersTM.add( new ParameterConfig() );
			}
			
		} catch (Exception e) {
			MsgUtil.error( getRootPane(), e.getLocalizedMessage(),
					Messages.alt( "_ERROR", "Error" ) );
		}
	}
	
	private void configureParameter() {
		final int index = parametersPanel.getTable().getSelectedRow();
		if ( index < 0 ) { return; }
		if ( index >= valueConfiguration.getParameters().size() ) {
			return;
		}
		ParameterConfig pc = valueConfiguration.getParameters().get( index );
		ValueConfiguration vc = pc.getValueConfiguration();
		
		final String title = String.format(
				Messages.alt( "_PARAMETER_CONFIG_TITLE", "Parameter %d" ),
				index + 1
				);
		
		VCSelectionDialog dlg = new VCSelectionDialog(
				valueType, elements, software, driverCache );
		dlg.setTitle( title );
		UIUtil.centerOnScreen( dlg );
		if ( vc != null ) {
			dlg.setValueConfiguration( vc );
		}
		dlg.setVisible( true );
		if ( dlg.isConfirmed() ) {
			vc = dlg.getValueConfiguration().newCopy();
			
			if ( 0 == vc.getId() ) { // Is new ?
				vc.setId( software.generateIdFor( ValueConfiguration.class.getSimpleName() ) );
			}
			
			pc.setValueConfiguration( vc );
			
			// Update the table
			parametersPanel.getTable().updateUI();
		}
	}
	
	private void createQueryConfig() {
		QueryConfig obj = QueryConfigActionContainer.createQueryConfig(
				software, driverCache );
		if ( null == obj ) { return; }
		
		// Update the combobox
		query.setModel( new DefaultComboBoxModel(
			software.getQueryConfigurations().toArray( new QueryConfig[ 0 ] ) ) );
	}
}
