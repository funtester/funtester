package org.funtester.app.ui.software;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.ParameterMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.DefaultButtons;
import org.funtester.app.ui.util.BaseTableModel;
import org.joda.time.DateTime;

/**
 * Query parameter dialog
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class QueryParameterDialog extends JDialog {

	private static final long serialVersionUID = -1544505472416136046L;
	private static final int COLUMN_COUNT = 3;
	private static final int VALUE_COLUMN = COLUMN_COUNT - 1;
	
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	
	private ParameterMetaData metadata;
	private List< Object > values = new ArrayList< Object >();
	private List< Integer > types = new ArrayList< Integer >();
	private boolean confirmed = false;

	public QueryParameterDialog(final ParameterMetaData metadata) throws SQLException {
		this.metadata = metadata;
		populateWithValuesAccordingTo( metadata );
		
		setModal( true );
		setTitle(Messages.getString("QueryParameterDialog.this.title")); //$NON-NLS-1$
		setBounds( 100, 100, 538, 296 );
		getContentPane().setLayout( new BorderLayout() );
		contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable( createTableModel() );
		table.setName( "table" );
		table.setRowSelectionAllowed(false);		
		// Tells the table to stop editing when it lost its focus
		table.putClientProperty( "terminateEditOnFocusLost", Boolean.TRUE );
		scrollPane.setViewportView(table);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
		getContentPane().add( buttonPane, BorderLayout.SOUTH );
		
		JButton okButton = DefaultButtons.createOkButton();
		buttonPane.add( okButton );
		getRootPane().setDefaultButton( okButton );
		okButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO validate ?
				confirmed = true;
				setVisible( false );
			}
		} );
	
		JButton cancelButton = DefaultButtons.createCancelButton();
		buttonPane.add( cancelButton );
		final ActionListener cancelActionListener = new ActionListener() {
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
	}
	
	
	public List< Object > getValues() {
		return values;
	}
	
	public List< Integer > getTypes() {
		return types;
	}
	
	public boolean isConfirmed() {
		return confirmed;
	}
	
	private void setValue(final int rowIndex, final String value) {
		final int paramType = types.get( rowIndex );
		switch ( paramType ) {
			// String				
			case Types.NVARCHAR		: ;
			case Types.NCHAR		: ;
			case Types.VARCHAR		: ;
			case Types.CHAR			: {
				values.set( rowIndex, new String( value ) );
				break;
			}
			// Integer
			case Types.BIGINT 		: ;
			case Types.INTEGER		: ;
			case Types.SMALLINT		: ;
			case Types.TINYINT		: {
				int intValue;
				try {
					intValue = Integer.parseInt( value );
					values.set( rowIndex, new Integer( intValue ) );
				} catch (Exception e) { }
				
				break;
			}
			// Double
			case Types.DOUBLE		: ;
			case Types.FLOAT		: ;
			case Types.DECIMAL		: ;
			case Types.NUMERIC		: {
				double doubleValue;
				try {
					doubleValue = Double.parseDouble( value );
					values.set( rowIndex, new Double( doubleValue ) );
				} catch (Exception e) { }
				break;
			}
			// Date and Time
			case Types.TIMESTAMP	: ;
			case Types.TIME			: ;
			case Types.DATE			: {
				DateTime dt;
				try {
					dt = DateTime.parse( value );
					values.set( rowIndex, dt );
				} catch (Exception e) { }
				break;
			}
			
			// Default to string
			default: values.set( rowIndex, new String( "" ) );
		} // for
	}
	
	private void populateWithValuesAccordingTo(final ParameterMetaData m) throws SQLException {
		final int COUNT = m.getParameterCount();
		values.clear();
		types.clear();
		for ( int i = 1; i <= COUNT; ++i ) {
			int paramType;
			try {
				paramType = m.getParameterType( i );
			} catch ( SQLException e ) {
				e.printStackTrace();
				paramType = Types.VARCHAR; // default to String
			}
			types.add( paramType );
			
			switch ( paramType ) {
				// String				
				case Types.NVARCHAR		: ;
				case Types.NCHAR		: ;
				case Types.VARCHAR		: ;
				case Types.CHAR			: {
					values.add( new String( "" ) ); break;
				}
				// Integer
				case Types.BIGINT 		: ;
				case Types.INTEGER		: ;
				case Types.SMALLINT		: ;
				case Types.TINYINT		: {
					values.add( new Integer( 0 ) ); break;
				}
				// Double
				case Types.DOUBLE		: ;
				case Types.FLOAT		: ;
				case Types.DECIMAL		: ;
				case Types.NUMERIC		: {
					values.add( new Double( 0.0 ) ); break;
				}
				// Date and Time
				case Types.TIMESTAMP	: ;
				case Types.TIME			: ;
				case Types.DATE			: {
					values.add( new DateTime() ); break;
				}
				
				// Default to string
				default: values.add( new String( "" ) );
			} // for
		}
	}

	private BaseTableModel< Object > createTableModel() {
		// TODO i18n
		return new BaseTableModel< Object >( COLUMN_COUNT, values, "#", "Type", "Value" ) {
			private static final long serialVersionUID = 1L;
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch ( columnIndex ) {
					case 0: return rowIndex;
					case 1: {
						try {
							return metadata.getParameterTypeName( rowIndex + 1 );
						} catch ( SQLException e ) {
							e.printStackTrace();
							return "Unknown type";
						}
					}
					default: return values.get( rowIndex );
				}
			} 
			
			@Override
			public boolean isCellEditable(final int row, final int col) {
				final int lastColumn = ( COLUMN_COUNT - 1 );
				return lastColumn == col;
			}
			
			@Override
			public void setValueAt(Object value, int row, int col) {
				if ( col != VALUE_COLUMN ) { return; }
				setValue( row, value.toString() );
				fireTableCellUpdated( row, col );
			}
		};
	}

}
