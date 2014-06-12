package org.funtester.app.ui.util;

import java.awt.EventQueue;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

/**
 * Provide basic operations and configurations for the common table model uses.
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T>	the type handled by the model.
 */
public abstract class BaseTableModel< T >  extends AbstractTableModel {

	private static final long serialVersionUID = 5445434928279598570L;
	
	private int columnCount; // can be changed by copy
	private Collection< T > items; 
	private final Map< Integer, String > columnNames;
	private final Map< Integer, Class< ? extends Object > > columnClasses;
	private final Map< Integer, Boolean > columnEditable;
	
	/**
	 * Construct the table model.
	 * 
	 * @param columnCount	the number of columns.
	 * @param items			a reference to the items to be handled.
	 */
	public BaseTableModel(
			final int columnCount,
			Collection< T > items
			) {
		super();
		if ( null == items ) {
			throw new IllegalArgumentException( "items cannot be null." );
		}
		if ( columnCount < 1 ) {
			throw new IllegalArgumentException( "columnCount cannot be lesser than 1." );
		}
		this.columnCount = columnCount;
		this.items = items;		
		this.columnNames = new HashMap< Integer, String >();
		this.columnClasses = new HashMap< Integer, Class< ? extends Object > >();
		this.columnEditable = new HashMap< Integer, Boolean >();
		
		// Initializes column classes as String and editable as false
		for ( int i = 0; ( i < this.columnCount ); ++i ) {
			this.columnClasses.put( i, String.class );
			this.columnEditable.put( i, Boolean.FALSE );
		}
	}
	
	
	/**
	 * Construct the table model.
	 * 
	 * @param columnCount	the number of columns.
	 * @param items			the items to be handled.
	 * @param columnNames	the names for the columns.
	 */	
	public BaseTableModel(
			final int columnCount,
			Collection< T > items,
			final String ... columnNames
			) {
		this( columnCount, items );
		setColumnNames( columnNames );
	}
	
	
	/**
	 * Allow to copy another {@link BaseTableModel}
	 * 
	 * @param other				the base table model to be copied.
	 * @param newItemsToBeUsed	the new items to replace the old one.
	 * @return					a reference to <code>this</code>.
	 */
	public BaseTableModel< T > copy(
			final BaseTableModel< T > other,
			final Collection< T > newItemsToBeUsed
			) {
		this.columnCount = other.columnCount;
		
		getItems().clear();
		getItems().addAll( newItemsToBeUsed );
		
		this.columnNames.clear();
		this.columnNames.putAll( other.columnNames );
		
		this.columnClasses.clear();
		this.columnClasses.putAll( other.columnClasses );
		
		this.columnEditable.clear();
		this.columnEditable.putAll( other.columnEditable );
		
		return this;
	}
	
	

	/**
	 * Set all column names once.
	 * <p>This method allows chaining.</p>
	 * 
	 * @param names	the names to be set.
	 */
	public BaseTableModel< T > setColumnNames(String ... names) {
		int i = 0;
		for ( String name : names ) {
			columnNames.put( i, name );
			++i;
		}
		return this;
	}	
	
	/**
	 * Set all column classes once.
	 * <p>This method allows chaining.</p>
	 * 
	 * @param classes	the classes to be set.
	 */
	public BaseTableModel< T > setColumnClasses(Class< ? > ... classes) {
		int i = 0;
		for ( Class< ? > clazz : classes ) {
			columnClasses.put( i, clazz );
			++i;
		}
		return this;
	}
	
	/**
	 * Set the column name.
	 * 
	 * @param index	the column index.
	 * @param name	the column name.
	 * @return		true if set, false otherwise (invalid index).
	 */
	public boolean setColumnName(final int index, final String name) {
		if ( index >= columnCount ) {
			return false;
		}
		columnNames.put( index, name );
		return true;
	}
	
	/**
	 * Set the column class.
	 * 
	 * @param index	the column index.
	 * @param clazz	the column class.
	 * @return		true if set, false otherwise (invalid index).
	 */
	public boolean setColumnClass(int index, final Class< ? > clazz) {
		if ( index >= columnCount ) {
			return false;
		}		
		columnClasses.put( index, clazz );
		return true;
	}
	
	/**
	 * Return the column count.
	 */
	public int getColumnCount() {
		return columnCount;
	}
	
	public synchronized Collection< T > getItems() {
		return items; 
	}
	
	public synchronized void setItems(Collection< T > items) {
		this.items = null;
		this.items = items;
	}
	
	/**
	 * Return the item at the defined index.
	 * 
	 * @param index	the item index.
	 * @return		the item.
	 */
	public synchronized T itemAt(final int index) {
		try {
			if ( getItems() instanceof List ) {
				return ( ( List< T > ) getItems() ).get( index );
			}
			int i = 0;
			Iterator< T > it = getItems().iterator();
			while ( it.hasNext() ) {
				T obj = it.next(); 
				if ( index == i ) {
					return obj;
				}
				++i;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Return {@code true} whether a given object is in the items list.
	 * 
	 * @param obj	the object to verify.
	 * @return
	 */
	public boolean contains(T obj) {
		return items.contains( obj );
	}
	
	/**
	 * Add a object to the items and updates the model.
	 * 
	 * @param obj	the object to be added.
	 * @return		true if added.
	 */
	public boolean add(T obj) {
		int size = getItems().size();
		if ( getItems().add( obj ) ) {
			fireTableRowsInserted( size, size );
			return true;
		}
		return false;
	}
	
	/**
	 * Indicate that an object at the defined index was updated.
	 * 
	 * @param index	the object index.
	 * @return		true if updated, false otherwise.
	 */
	public boolean updated(final int index) {
		if ( index < 0 || index >= getItems().size() ) {
			return false;
		}
		fireTableRowsUpdated( index, index );
		return true;
	}
	
	/**
	 * Indicate that an object was inserted at the supplied index.
	 * 
	 * @param index	the index of the inserted item.
	 * @return		{@code true} if successful, {@code false} otherwise.
	 */
	public boolean inserted(final int index) {
		if ( index < 0 || index >= getItems().size() ) {
			return false;
		}
		fireTableRowsInserted( index, index );
		return true;
	}
	
	/**
	 * Indicate that a object was moved from an index to another.
	 * 
	 * @param from	the origin index.
	 * @param to	the destiny index.
	 * @return		true if updated, false otherwise.
	 */
	public boolean moved(final int from, final int to) {
		final int size = getItems().size();
		if ( ( from < 0 || from >= size ) || ( to < 0 || to >= size ) ) {
			return false;
		}
		final int firstRow, lastRow; 
		if ( to > from ) { firstRow = from; lastRow = to; }
		else { firstRow = to; lastRow = from; }
		fireTableRowsUpdated( firstRow, lastRow );
		return true;
	}
	
	/**
	 * Remove the object at the defined index and updates the model.
	 * 
	 * @param index	the object index.
	 * @return		true if removed, false otherwise.
	 */
	public synchronized boolean remove(final int index) {
		if ( index < 0 || index >= getItems().size() ) {
			return false;
		}
		T obj = itemAt( index );
		if ( null == obj ) { return false; }
		getItems(). remove( obj );
		
		try {
			EventQueue.invokeLater( new Runnable() {
				@Override
				public void run() {
					fireTableRowsDeleted( index, index );
				}
			} );
		} catch ( Exception e ) {
			return false;
		}
//		fireTableRowsDeleted( index, index );
		return true;
	}
	
	public void clear() {
		final int count = getRowCount();
		items.clear();
		if ( count > 0 ) {
			fireTableRowsDeleted( 0, count - 1 );
		}
	}	
	
	/**
	 * Get the number of objects.
	 */
	public int getRowCount() {
		return getItems().size();
	}
	
	@Override
	public String getColumnName(int columnIndex) {	
		return columnNames.get( columnIndex );
	}
	
	@Override
	public Class< ? extends Object > getColumnClass(int columnIndex) {
		return columnClasses.get( columnIndex );
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		if ( column >= 0 && column < columnCount ) {
			return columnEditable.get( column );
		}
		return false;	 	 
	}

	
	// TO BE DEFINED IN CHILD CLASSES
	public abstract Object getValueAt(int rowIndex, int columnIndex);

}
