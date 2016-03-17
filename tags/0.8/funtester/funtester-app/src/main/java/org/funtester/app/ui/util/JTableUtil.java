package org.funtester.app.ui.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * JTable utilities
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class JTableUtil {
	
	interface TableColumnAction {
		void doWithColumn(final int index, final TableColumn column);
	}
	
	private JTableUtil() {}
	
	private static void doWithTableColumns(
			JTable table, final int max, TableColumnAction action) {
		final int columnCount = table.getColumnCount();
		for ( int i = 0; i < columnCount && i < max; ++i ) {
			action.doWithColumn( i, table.getColumnModel().getColumn( i ) );
		}
	}
	
	/**
	 * Adjust column widths.
	 * 
	 * @param table		the table to adjust
	 * @param widths	the array of widgets
	 */
	public static void adjustPreferredColumnWidths(JTable table, final int widths[]) {
		TableColumnAction action = new TableColumnAction() {		
			@Override
			public void doWithColumn(final int index, TableColumn column) {
				column.setPreferredWidth( widths[ index ] );
			}
		};
		doWithTableColumns( table, widths.length, action );
	}
	
	/**
	 * Adjust maximum column widths.
	 * 
	 * @param table		the table to adjust
	 * @param widths	the array of widgets
	 */	
	public static void adjustMaxColumnWidths(JTable table, final int widths[]) {
		TableColumnAction action = new TableColumnAction() {		
			@Override
			public void doWithColumn(final int index, TableColumn column) {
				column.setMaxWidth( widths[ index ] );
			}
		};
		doWithTableColumns( table, widths.length, action );
	}
	
	/**
	 * Adjust minimum column widths.
	 * 
	 * @param table		the table to adjust
	 * @param widths	the array of widgets
	 */
	public static void adjustMinColumnWidths(JTable table, final int widths[]) {
		TableColumnAction action = new TableColumnAction() {		
			@Override
			public void doWithColumn(final int index, TableColumn column) {
				column.setMinWidth( widths[ index ] );
			}
		};
		doWithTableColumns( table, widths.length, action );
	}
	
	/**
	 * Set all columns' cell renderer.
	 * 
	 * @param table		the table to set.
	 * @param renderer	the TableCellRenderer.
	 */
	public static void setCellRenderer(JTable table, final TableCellRenderer renderer) {
		TableColumnAction action = new TableColumnAction() {		
			@Override
			public void doWithColumn(final int index, TableColumn column) {
				column.setCellRenderer( renderer );
			}
		};
		doWithTableColumns( table, table.getColumnCount(), action );
	}
	
	
	/**
	 * Keep the current renderers and only set their foreground and background
	 * colors.
	 * 
	 * 	How to use it:
	 * 	<code>
	 * 		table = new JTable() {
	 * 			@Override
	 *			public Component prepareRenderer(TableCellRenderer renderer,
	 *					int rowIndex, int colIndex) {
	 *				Component c = super.prepareRenderer( renderer, rowIndex, colIndex );
	 *				JTableUtil.prepareRenderer( c, this,
	 *					Color.YELLOW, Color.BLACK, renderer, rowIndex, colIndex );
	 *				return c;
	 *			}
	 * 		}
	 * </code>
	 * 
	 * 
	 * @param superPrepareRendererMethodReturnedComponent
	 * 								the component returned from
	 * 								<code>super.prepareRenderer</code> method.
	 * @param table					the table to render.
	 * @param unselectedBackground	unselected background color.
	 * @param unselectedForeground	unselected foreground color.
	 * @param rowIndex 				the row to render.
	 * @param colIndex				the column to render.
	 * 
	 * @return						the modified <code>superPrepareRendererMethodReturnedComponent</code>.
	 */
	public static Component renderTableRows(
			Component superPrepareRendererMethodReturnedComponent,
			JTable table,
			Color unselectedBackground,
			Color unselectedForeground,
			int rowIndex,
			int colIndex
			) {
		Component c = superPrepareRendererMethodReturnedComponent;
		 
		boolean isRowSelected = 
				Arrays.asList( table.getSelectedRows() ).contains( rowIndex );
		boolean isColSelected = 
				Arrays.asList( table.getSelectedColumns() ).contains( colIndex );
		boolean isSelected = isRowSelected && isColSelected;
		 
		if ( table.isEnabled() ) {
			if ( isSelected ) {
				c.setBackground( table.getSelectionBackground() );
				c.setForeground( table.getSelectionForeground() );
			} else {
				c.setBackground( table.getBackground() );
				c.setForeground( table.getForeground() );
				if ( 0 != rowIndex % 2 ) { // if is odd
					if ( unselectedBackground != null ) {
						c.setBackground( unselectedBackground );
					}
					if ( unselectedForeground != null ) {
						c.setForeground( unselectedForeground );
					}
				}
			}
		} else {
			c.setBackground( SystemColor.inactiveCaptionText );
			c.setForeground( SystemColor.inactiveCaption );
		}
		return c;
	 }

}
