package org.funtester.app.ui.software;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.software.actions.DatabaseConfigActionContainer;
import org.funtester.app.ui.software.actions.DatabaseConfigCUDEventListener;
import org.funtester.app.ui.software.actions.QueryConfigActionContainer;
import org.funtester.app.ui.software.actions.QueryConfigCUDEventListener;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.software.DatabaseConfig;
import org.funtester.core.software.DatabaseDriverConfig;
import org.funtester.core.software.QueryConfig;
import org.funtester.core.software.Software;
import org.funtester.core.util.UpdateListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Present the database and query tree and listen to model updates.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DatabaseQueryPanel extends JPanel
	implements UpdateListener
		, DatabaseConfigCUDEventListener
		, QueryConfigCUDEventListener {

	private static final long serialVersionUID = 5645674850859443257L;
	
	private final Software software;
	private final Map< DatabaseDriverConfig, String > driverFileMap;
	private final DriverCache driverCache;
	private final DatabaseConfigActionContainer databaseConfigAC;
	private final QueryConfigActionContainer queryConfigAC;
	
	private final JSplitPane splitPane;
	private final DefaultMutableTreeNode rootNode;
	private final DefaultTreeModel treeModel;
	private final JTree tree;
	private JPanel rightSidePanel;
	private final JButton databaseNewButton;
	private final JButton databaseEditButton;
	private final JButton databaseRemoveButton;
	private final JButton queryNewButton;
	private final JButton queryEditButton;
	private final JButton queryRemoveButton;
	

	public DatabaseQueryPanel(
			final Software software,
			final Map< DatabaseDriverConfig, String > driverFileMap,
			final DriverCache driverCache,
			final DatabaseConfigActionContainer dbcAC,
			final QueryConfigActionContainer qcAC
			) {
		this.software = software;
		this.driverFileMap = driverFileMap; 
		this.driverCache = driverCache;
		this.databaseConfigAC = dbcAC;
		this.queryConfigAC = qcAC;
		
		setLayout(new BorderLayout(0, 0));
		
		splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setName("leftPanel");
		splitPane.setLeftComponent(leftPanel);
		leftPanel.setLayout(new BorderLayout(0, 0));
		
		//treeModel = new DatabaseQueryTreeModel( software );
		rootNode = createRootNodeForSoftware( software );
		treeModel = new DefaultTreeModel( rootNode );
		tree = new JTree( treeModel );
		tree.setName("databaseQueryTree");
		tree.setMinimumSize( new Dimension( 200, 200 ) );
		tree.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
		tree.setAutoscrolls( true );
		tree.setCellRenderer( createTreeCellRenderer() );
		tree.addMouseListener( createMouseListener() );
		leftPanel.add( tree );
		
		rightSidePanel = new JPanel();
		splitPane.setRightComponent(rightSidePanel);
		rightSidePanel.setLayout(new BorderLayout(0, 0));
		
		JPanel buttonsPanel = new JPanel();
		add(buttonsPanel, BorderLayout.NORTH);
		buttonsPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		databaseNewButton = new JButton( Messages.alt( "_NEW" , "New" ) );
		databaseNewButton.setName("databaseNewButton");
		buttonsPanel.add(databaseNewButton, "1, 2, default, top");
		
		databaseEditButton = new JButton( Messages.alt( "_EDIT" , "Edit" ) );
		databaseEditButton.setName("databaseEditButton");
		buttonsPanel.add(databaseEditButton, "3, 2");
		
		databaseRemoveButton = new JButton( Messages.alt( "_REMOVE" , "Remove" ) );
		databaseRemoveButton.setName("databaseRemoveButton");
		buttonsPanel.add(databaseRemoveButton, "5, 2");
		
		queryNewButton = new JButton( Messages.alt( "_NEW" , "New" ) );
		queryNewButton.setName("queryNewButton");
		buttonsPanel.add(queryNewButton, "9, 2");
		
		queryEditButton = new JButton(  Messages.alt( "_EDIT" , "Edit" ) );
		queryEditButton.setName("queryEditButton");
		buttonsPanel.add(queryEditButton, "11, 2");
		
		queryRemoveButton = new JButton( Messages.alt( "_REMOVE" , "Remove" ) );
		queryRemoveButton.setName("queryRemoveButton");
		buttonsPanel.add(queryRemoveButton, "13, 2");
		
		// DATABASE CONFIG
		
		dbcAC.addListener( this );
		dbcAC.addDatabaseConfigCUDEventListener( this );
		tree.addTreeSelectionListener( dbcAC ); // Add the container as a selection listener
		
		databaseNewButton.setAction( dbcAC.getNewAction() );
		databaseEditButton.setAction( dbcAC.getEditAction() );
		databaseRemoveButton.setAction( dbcAC.gerRemoveAction() );
		
		// QUERY CONFIG
		
		qcAC.addListener( this );
		qcAC.addQueryConfigCUDEventListener( this );
		tree.addTreeSelectionListener( qcAC ); // Add the container as a selection listener
		
		queryNewButton.setAction( qcAC.getNewAction() );
		queryEditButton.setAction( qcAC.getEditAction() );
		queryRemoveButton.setAction( qcAC.getRemoveAction() );
		
		// UPDATE
		
		updateButtonsState( false, false );
	}
	
	private MouseListener createMouseListener() {
		return new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				if ( 2 == e.getClickCount() ) {
					if ( tree.isSelectionEmpty() ) { return; }
					Object selected = tree.getSelectionPath().getLastPathComponent();
					if ( ! ( selected instanceof DefaultMutableTreeNode ) ) { return; } 
					Object obj = ( ( DefaultMutableTreeNode ) selected ).getUserObject();
					if ( obj instanceof DatabaseConfig ) {
						databaseConfigAC.getEditAction().actionPerformed( null );
					} else if ( obj instanceof QueryConfig ) {
						queryConfigAC.getEditAction().actionPerformed( null );
					}
				}
			}
			
			@Override public void mouseReleased(MouseEvent e) { }
			
			@Override public void mouseExited(MouseEvent e) { }
			
			@Override public void mouseEntered(MouseEvent e) {}
			
			@Override public void mouseClicked(MouseEvent e) {}
		};
	}

	private DefaultMutableTreeNode createRootNodeForSoftware(final Software s) {
		final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode( s, true );
		for ( DatabaseConfig dbc : s.getDatabaseConfigurations() ) {
			DefaultMutableTreeNode dbcNode = new DefaultMutableTreeNode( dbc, true );
			final List< QueryConfig > queries = s.queryConfigurationsForDatabase( dbc );
			for ( QueryConfig qc : queries ) {
				DefaultMutableTreeNode queryNode = new DefaultMutableTreeNode( qc, false ); // do not allow children (leaf)
				dbcNode.add( queryNode );
			}
			rootNode.add( dbcNode );
		}
		return rootNode;
	}
	
	public void updateTreeUI() {
		tree.updateUI();
	}

	@Override
	public void updated(final Object o) {
		rightSidePanel.removeAll();
		
		final boolean isDatabaseConfig = isDatabaseConfig( o );
		if ( isDatabaseConfig ) {
			rightSidePanel.add( createDatabaseConfigPanel( (DatabaseConfig) o ), BorderLayout.CENTER );
		}
		
		final boolean isQueryConfig = isQueryConfig( o );
		if ( isQueryConfig ) {
			rightSidePanel.add( createQueryConfigPanel( (QueryConfig) o ), BorderLayout.CENTER );
		}
		
		rightSidePanel.updateUI();
		
		updateButtonsState( isDatabaseConfig, isQueryConfig );
	}
	
	private boolean isDatabaseConfig(Object o) {
		return o instanceof DatabaseConfig;
	}
	
	private boolean isQueryConfig(Object o) {
		return o instanceof QueryConfig;
	}	
	
	private void updateButtonsState(
			final boolean isDatabaseConfig,
			final boolean isQueryConfig
			) {
		setButtonEnabled( databaseNewButton, true );
		setButtonEnabled( databaseEditButton, isDatabaseConfig );
		setButtonEnabled( databaseRemoveButton, isDatabaseConfig );
		
		setButtonEnabled( queryNewButton, isDatabaseConfig );
		setButtonEnabled( queryEditButton, isQueryConfig );
		setButtonEnabled( queryRemoveButton, isQueryConfig );
	}
	
	private void setButtonEnabled(JButton b, final boolean enabled) {
		if ( null == b ) throw new NullPointerException( "the button cannot be null" );
		if ( b.getAction() != null )
			b.getAction().setEnabled( enabled );
		else
			b.setEnabled( enabled );
	}

	private JPanel createDatabaseConfigPanel(DatabaseConfig obj) {
		DatabaseConfigDialog dlg = new DatabaseConfigDialog( software, driverFileMap, driverCache );
		dlg.setEditable( false );
		dlg.setDatabaseConfig( obj );
		return dlg.getContentPanel();
	}

	private JPanel createQueryConfigPanel(QueryConfig obj) {
		QueryConfigDialog dlg = new QueryConfigDialog( software, driverCache );
		dlg.setEditable( false );
		dlg.setQueryConfig( obj );
		return dlg.getContentPanel();
	}
	
	private TreeCellRenderer createTreeCellRenderer() {
		
		class DatabaseQueryTreeCellRenderer extends DefaultTreeCellRenderer {
			private static final long serialVersionUID = 4416089274454956038L;
			
			private final Icon softwareIcon;
			private final Icon databaseIcon;
			private final Icon queryIcon;
			
			public DatabaseQueryTreeCellRenderer() {
				super();
				softwareIcon = ImageUtil.loadIcon( ImagePath.softwareIcon() );
				databaseIcon = ImageUtil.loadIcon( ImagePath.databaseIcon() );
				queryIcon = ImageUtil.loadIcon( ImagePath.queryIcon() );
			}
			
			@Override
			public Component getTreeCellRendererComponent(JTree tree,
					Object value, boolean sel, boolean expanded, boolean leaf,
					int row, boolean hasFocus) {
				super.getTreeCellRendererComponent( tree, value, sel, expanded,
					leaf, row, hasFocus );
				if ( null == value ) { return this; }
				Object selected = ( (DefaultMutableTreeNode) value ).getUserObject();
				// Decide which icon to use
				if ( isDatabaseConfig( selected ) ) {
					setIcon( databaseIcon );
				} else if ( isQueryConfig( selected ) ) {
					setIcon( queryIcon );
				} else {
					setIcon( softwareIcon );
				}
				return this;
			}
		};
		
		return new DatabaseQueryTreeCellRenderer();
	}

	@Override
	public void created(Software parent, DatabaseConfig obj, int index) {
		DefaultMutableTreeNode dbcNode =
				new DefaultMutableTreeNode( obj, true );
		rootNode.insert( dbcNode, index );
		treeModel.nodesWereInserted( rootNode, new int[] { index } );
		
		// Select the node on the tree
		TreeNode[] nodes = treeModel.getPathToRoot( dbcNode );
		tree.setSelectionPath( new TreePath( nodes ) );
	}

	@Override
	public void updated(Software parent, DatabaseConfig obj, int index) {
		treeModel.nodesChanged( rootNode, new int[] { index } );
		
		// Show the node on the panel
		updated( obj );
	}

	@Override
	public void deleted(Software parent, DatabaseConfig obj, int index) {

		DefaultMutableTreeNode dbcNode = (DefaultMutableTreeNode)
				rootNode.getChildAt( index );
		
		dbcNode.removeAllChildren();
		rootNode.remove( dbcNode );
		
		treeModel.nodesWereRemoved( rootNode, new int[] { index },
			new Object[] { dbcNode } );
		
		// Select the root node (this updates the panel too)
		tree.setSelectionPath( new TreePath( rootNode ) );		
	}

	@Override
	public void created(Software parent, QueryConfig obj, int index) {

		DefaultMutableTreeNode dbcNode = databaseConfigNode( parent, obj );
		if ( null == dbcNode ) { return; }
		
		DefaultMutableTreeNode qcNode = new DefaultMutableTreeNode( obj, false );
		dbcNode.insert( qcNode, index );

		treeModel.nodesWereInserted( dbcNode, new int[] { index } );
		
		// Select the node on the tree
		TreeNode[] nodes = treeModel.getPathToRoot( qcNode );
		tree.setSelectionPath( new TreePath( nodes ) );
	}

	@Override
	public void updated(Software parent, QueryConfig obj, int index) {
		DefaultMutableTreeNode dbcNode = databaseConfigNode( parent, obj );
		if ( null == dbcNode ) { return; }
		treeModel.nodesChanged( dbcNode, new int[] { index } );
		
		// Show the node on the panel
		updated( obj );
	}

	@Override
	public void deleted(Software parent, QueryConfig obj, int index) {
		DefaultMutableTreeNode dbcNode = databaseConfigNode( parent, obj );
		if ( null == dbcNode ) { return; }
		
		DefaultMutableTreeNode qcNode = (DefaultMutableTreeNode)
				dbcNode.getChildAt( index );
		if ( null == qcNode ) { return; }
		
		dbcNode.remove( qcNode );
		
		treeModel.nodesWereRemoved( dbcNode, new int[] { index },
				new Object[] { qcNode } );
		
		// Select the database config node (this updates the panel too)
		TreeNode[] nodes = treeModel.getPathToRoot( dbcNode );
		tree.setSelectionPath( new TreePath( nodes ) );		
	}
	
	private DefaultMutableTreeNode databaseConfigNode(
			final Software parent, final QueryConfig obj
			) {
		final int dbcIndex = parent.indexOfDatabaseConfiguration(
				obj.getDatabaseConfig() );
		
		return (DefaultMutableTreeNode) rootNode.getChildAt( dbcIndex );
	}
}
