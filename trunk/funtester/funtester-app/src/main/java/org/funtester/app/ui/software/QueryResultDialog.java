package org.funtester.app.ui.software;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.ImageUtil;

/**
 * Query result dialog
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class QueryResultDialog extends JDialog {

	private static final long serialVersionUID = 7771058914321644876L;

	public static final int UNLIMITED_ROWS = 0;
	
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	
	public QueryResultDialog(
			final ResultSet resultSet,
			final int maxRows
			) throws Exception {
		setIconImage( ImageUtil.loadImage( ImagePath.scriptTestIcon() ) );
		setTitle(Messages.getString("QueryResultDialog.this.title")); //$NON-NLS-1$
		setModal( true );
		setBounds( 100, 100, 624, 373 );
		getContentPane().setLayout( new BorderLayout() );
		contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
	
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
		getContentPane().add( buttonPane, BorderLayout.SOUTH );
			
		JButton closeButton = new JButton( Messages.getString("QueryResultDialog.closeButton.text") ); //$NON-NLS-1$
		closeButton.setName( "closeButton" );
		buttonPane.add( closeButton );
		getRootPane().setDefaultButton( closeButton );
		
		ActionListener closeActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible( false );
			}
		};
		closeButton.addActionListener( closeActionListener );
		
		// Register the ESC key to call the cancelActionListener
		getRootPane().registerKeyboardAction(
				closeActionListener,
				KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ),
				JComponent.WHEN_IN_FOCUSED_WINDOW
				);
		
		draw( resultSet, maxRows );
	}

	private void draw(final ResultSet rs, final int maxRows) throws Exception {
		final int COLUMN_COUNT = rs.getMetaData().getColumnCount();
		final List< String > columnNames = new ArrayList< String >();
		for ( int i = 1; i <= COLUMN_COUNT; ++i ) {
			columnNames.add( rs.getMetaData().getColumnName( i ) );
		}
		
		final List< Map< Integer, String > > values = new ArrayList< Map< Integer, String > >();
		final int MAX_ROWS = ( UNLIMITED_ROWS == maxRows ) ? Integer.MAX_VALUE : maxRows;
		int rowCount = 0;
		while ( rs.next() && rowCount < MAX_ROWS ) {
			Map< Integer, String > map = new HashMap< Integer, String >();
			for ( int i = 1; i <= COLUMN_COUNT; ++i ) {
				map.put( i - 1, rs.getObject( i ).toString() );
			}
			values.add( map );
			rowCount++;
		}
		final int ROW_COUNT = rowCount;

		final TableModel tm = new AbstractTableModel() {

			private static final long serialVersionUID = 1898722954163768811L;

			@Override
			public String getColumnName(int col) {
				return columnNames.get( col );
			}
			
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				return values.get( rowIndex ).get( columnIndex );
			}
			
			@Override
			public int getRowCount() {
				return ROW_COUNT;
			}
			
			@Override
			public int getColumnCount() {
				return COLUMN_COUNT;
			}
		};
		
		table.setModel( tm );
		
		setTitle( getTitle() + " - " + ROW_COUNT + " " + Messages.alt( "_ROWS", " rows" ) );
	}

}
