package org.funtester.app.ui.software;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppState;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.software.actions.FlowCUDEventListener;
import org.funtester.app.ui.software.actions.UseCaseCUDEventListener;
import org.funtester.app.ui.software.actions.UseCaseListActionContainer;
import org.funtester.app.ui.util.GenericTreeMouseAdapter;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.core.software.Flow;
import org.funtester.core.software.Software;
import org.funtester.core.software.UseCase;
import org.funtester.core.util.UpdateListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Use case list panel
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class UseCaseListPanel extends JPanel
	implements UpdateListener, UseCaseCUDEventListener, FlowCUDEventListener {

	private static final long serialVersionUID = 4867560048167778085L;

	private final JPanel rightSidePanel;
	private final GenericTreeMouseAdapter treeMouseAdapter; // For the double click
	private final JTree tree;
	private final DefaultTreeModel treeModel;
	private final DefaultMutableTreeNode rootNode;
	
	private final UseCaseListActionContainer useCaseListAC;
	private final AppState appState;
	
	private UseCase currentUseCase = null;
	

	public UseCaseListPanel(AppState appState) {
		if ( null == appState ) throw new IllegalArgumentException( "state is null" );
		if ( null == appState.getCurrentSoftware() ) throw new IllegalArgumentException( "currentSoftware is null" );
		this.appState = appState;
		
		useCaseListAC = new UseCaseListActionContainer(
				appState.getCurrentSoftware(), appState.getDriverCache() );
		// Register this panel as a listener
		useCaseListAC.addUseCaseCUDEventListener( this );
		useCaseListAC.addFlowCUDEventListener( this );
		
		setLayout( new BorderLayout( 0, 0 ) );
		
		JSplitPane splitPane = createSplitPane();
		
		JPanel leftSidePanel = new JPanel();
		splitPane.setLeftComponent( leftSidePanel );
		leftSidePanel.setLayout( new BorderLayout( 0, 0 ) );
		
		rightSidePanel = new JPanel();
		splitPane.setRightComponent( rightSidePanel );
		rightSidePanel.setLayout( new BorderLayout( 0, 0 ) );
		
		
		rootNode = createRootNodeFromSoftware( appState.getCurrentSoftware() );
		treeModel = new DefaultTreeModel( rootNode );
		
		tree = createTree( leftSidePanel );
		tree.setModel( treeModel );
		tree.addTreeSelectionListener( createTreeSelectionListener() );
		tree.addTreeSelectionListener( useCaseListAC );
		
		final JPopupMenu popupMenu = createPopupMenu( tree, useCaseListAC );
		
		// This component that configures the DOUBLE CLICK to call the
		// execute command
		treeMouseAdapter = new GenericTreeMouseAdapter( tree, popupMenu );
		treeMouseAdapter.addCommandEventListener( useCaseListAC ); // Double click opens the selected object to edit
		
		tree.addMouseListener( treeMouseAdapter );
		selectRootNode( tree, treeModel );
	}

	private DefaultMutableTreeNode createRootNodeFromSoftware(Software software) {
		final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode( software, true );
		for ( UseCase uc : software.getUseCases() ) {
			DefaultMutableTreeNode ucNode = new DefaultMutableTreeNode( uc, true );
			for ( Flow f : uc.getFlows() ) {
				DefaultMutableTreeNode flowNode = new DefaultMutableTreeNode( f, false ); // do not allow children (leaf)
				ucNode.add( flowNode );
			}
			rootNode.add( ucNode );
		}
		return rootNode;
	}	
	

	public UseCaseListActionContainer getActionContainer() {
		return useCaseListAC;
	}

	@Override
	public void updated(Object o) {
		select( o );
	}	

	private void selectRootNode(final JTree tree, final TreeModel treeModel) {
		tree.setSelectionPath( new TreePath( treeModel.getRoot() ) );
	}

	private JSplitPane createSplitPane() {
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
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
		
		//
		// USE CASE
		//
		
		JButton useCaseNewButton = new JButton( useCaseListAC.useCaseNewAction() );
		useCaseNewButton.setName("useCaseNewButton");
		useCaseNewButton.setText( Messages.alt( "_NEW", "New" ) );
		panel.add(useCaseNewButton, "1, 2, default, top");
		
		JButton useCaseEditButton = new JButton( useCaseListAC.useCaseEditAction() );
		useCaseEditButton.setName("useCaseEditButton");
		useCaseEditButton.setText( Messages.alt( "_EDIT", "Edit" ) );
		panel.add(useCaseEditButton, "3, 2");
		
		JButton useCaseRemoveButton = new JButton( useCaseListAC.useCaseRemoveAction() );
		useCaseRemoveButton.setName("useCaseButton");
		useCaseRemoveButton.setText( Messages.alt( "_REMOVE", "Remove" ) );
		panel.add(useCaseRemoveButton, "5, 2");
		
		
		final JPopupMenu stepNewPopupMenu = new JPopupMenu();
		
		JMenuItem flowNewBasicFlowMenuItem = new JMenuItem( useCaseListAC.flowNewBasicFlowAction() );
		flowNewBasicFlowMenuItem.setName( "flowNewBasicFlowMenuItem" );
		stepNewPopupMenu.add( flowNewBasicFlowMenuItem );
		
		JMenuItem flowNewTerminateFlowMenuItem = new JMenuItem( useCaseListAC.flowNewTerminateFlowAction() );
		flowNewTerminateFlowMenuItem.setName( "flowNewTerminateFlowMenuItem" );
		stepNewPopupMenu.add( flowNewTerminateFlowMenuItem );
		
		JMenuItem flowNewReturnableFlowMenuItem = new JMenuItem( useCaseListAC.flowNewReturnableFlowAction() );
		flowNewReturnableFlowMenuItem.setName( "flowNewReturnableFlowMenuItem" );
		stepNewPopupMenu.add( flowNewReturnableFlowMenuItem );
		
		JMenuItem flowNewCancelatorFlowMenuItem = new JMenuItem( useCaseListAC.flowNewCancelatorFlowAction() );
		flowNewCancelatorFlowMenuItem.setName( "flowNewCancelatorFlowMenuItem" );
		stepNewPopupMenu.add( flowNewCancelatorFlowMenuItem );		
		
		//
		// FLOW
		//
		
		final JButton flowNewButton = new JButton( Messages.alt( "_NEW", "New" ) );
		flowNewButton.setHorizontalTextPosition(SwingConstants.LEFT);
		flowNewButton.setName("flowNewButton");
		flowNewButton.setIcon( ImageUtil.loadIcon( ImagePath.dropDownIcon() ) );
		panel.add(flowNewButton, "9, 2");
		
				flowNewButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						stepNewPopupMenu.show( flowNewButton, e.getX(), e.getY() );
					}
				});			
		
		JButton flowEditButton = new JButton( useCaseListAC.flowEditAction() );
		flowEditButton.setName("flowEditButton");
		flowEditButton.setText( Messages.alt( "_EDIT", "Edit" ) );
		panel.add(flowEditButton, "11, 2");
		
		JButton flowRemoveButton = new JButton( useCaseListAC.flowRemoveAction() );
		flowRemoveButton.setName("flowRemoveButton");
		flowRemoveButton.setText( Messages.alt( "_REMOVE", "Remove" ) );
		panel.add(flowRemoveButton, "13, 2");
		
		//
		// the split pane
		//
		
		JSplitPane splitPane = new JSplitPane();
		add( splitPane, BorderLayout.CENTER );
		splitPane.setDividerSize(7);
		splitPane.setDividerLocation(400);
		splitPane.setOneTouchExpandable(true);
		
		return splitPane;
	}

	private JTree createTree(JPanel leftSidePanel) {
		JTree tree = new JTree();
		tree.setName("useCaseTree");
		tree.setScrollsOnExpand(false);
		tree.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		tree.setAutoscrolls( true );
		tree.setCellRenderer( createTreeCellRenderer() );
		leftSidePanel.add( tree, BorderLayout.CENTER );
		return tree;
	}

	private JPopupMenu createPopupMenu(JTree tree, UseCaseListActionContainer container) {
		JPopupMenu popupMenu = new JPopupMenu();
		tree.add( popupMenu );
		
		JMenuItem useCaseNewMenu = new JMenuItem( container.useCaseNewAction() );
		useCaseNewMenu.setName("useCaseNewMenu");
		popupMenu.add(useCaseNewMenu);
		
		JMenuItem useCaseEditMenu = new JMenuItem( container.useCaseEditAction() );
		useCaseEditMenu.setName("useCaseEditMenu");
		popupMenu.add(useCaseEditMenu);
		
		JMenuItem useCaseRemoveMenu = new JMenuItem( container.useCaseRemoveAction() );
		useCaseRemoveMenu.setName("useCaseRemoveMenu");
		popupMenu.add(useCaseRemoveMenu);
		
		JSeparator separator = new JSeparator();
		popupMenu.add(separator);
		
		JMenuItem flowNewBasicFlowMenuItem = new JMenuItem( useCaseListAC.flowNewBasicFlowAction() );
		flowNewBasicFlowMenuItem.setName( "flowNewBasicFlowMenuItem" );
		popupMenu.add( flowNewBasicFlowMenuItem );
		
		JMenuItem flowNewTerminateFlowMenuItem = new JMenuItem( useCaseListAC.flowNewTerminateFlowAction() );
		flowNewTerminateFlowMenuItem.setName( "flowNewTerminateFlowMenuItem" );
		popupMenu.add( flowNewTerminateFlowMenuItem );
		
		JMenuItem flowNewReturnableFlowMenuItem = new JMenuItem( useCaseListAC.flowNewReturnableFlowAction() );
		flowNewReturnableFlowMenuItem.setName( "flowNewReturnableFlowMenuItem" );
		popupMenu.add( flowNewReturnableFlowMenuItem );
		
		JMenuItem flowNewCancelatorFlowMenuItem = new JMenuItem( useCaseListAC.flowNewCancelatorFlowAction() );
		flowNewCancelatorFlowMenuItem.setName( "flowNewCancelatorFlowMenuItem" );
		popupMenu.add( flowNewCancelatorFlowMenuItem );			
		
		JMenuItem flowEditMenu = new JMenuItem( container.flowEditAction() );
		flowEditMenu.setName( "flowEditMenu" );
		popupMenu.add(flowEditMenu);
		
		JMenuItem flowRemoveMenu = new JMenuItem( container.flowRemoveAction() );
		flowRemoveMenu.setName( "flowRemoveMenu" );
		popupMenu.add(flowRemoveMenu);
		
		return popupMenu;
	}
	
	private TreeSelectionListener createTreeSelectionListener() {
		return new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				// If the item has been deleted...
				if ( null == e.getNewLeadSelectionPath() ) {
					return; // ...exit
				}
				// The last component is the selected one
				Object object = e.getPath().getLastPathComponent();
				if ( null == object ) {
					return;
				}
				
				
				// Update controls
				select( ( ( DefaultMutableTreeNode ) object ).getUserObject() ); 
			}		
		};
	}
	
	private TreeCellRenderer createTreeCellRenderer() {
		
		class SoftwareTreeCellRenderer extends DefaultTreeCellRenderer {
			
			private static final long serialVersionUID = -5187182088942053117L;
			private final Icon softwareIcon;
			private final Icon useCaseIcon;
			private final Icon flowIcon;
			
			public SoftwareTreeCellRenderer() {
				super();
				softwareIcon = ImageUtil.loadIcon( ImagePath.softwareIcon() );
				useCaseIcon = ImageUtil.loadIcon( ImagePath.useCaseIcon() );
				flowIcon = ImageUtil.loadIcon( ImagePath.flowIcon() );
			}
			
			@Override
			public Component getTreeCellRendererComponent(JTree tree,
					Object value, boolean sel, boolean expanded, boolean leaf,
					int row, boolean hasFocus) {
				super.getTreeCellRendererComponent( tree, value, sel, expanded,
					leaf, row, hasFocus );
				
				//Object object = value;
				Object object = ( ( DefaultMutableTreeNode ) value ).getUserObject();
				// Decide which icon to use
				if ( UseCasePanel.accepts( object ) ) {
					setIcon( useCaseIcon );
				} else if ( FlowPanel.accepts( object ) ) {
					setIcon( flowIcon );
				} else {
					setIcon( softwareIcon );
				}
				return this;
			}
		};
		
		return new SoftwareTreeCellRenderer();
	}

	private void select(final Object o) {
		rightSidePanel.removeAll();
		if ( null == o ) { return; }
		
		final boolean isUseCase = UseCasePanel.accepts( o );
		final boolean isFlow = FlowPanel.accepts( o );
		
		if ( isUseCase ) {
			currentUseCase = (UseCase) o;
			
			UseCasePanel useCasePanel = new UseCasePanel(
					currentUseCase,
					appState.getCurrentSoftware(),
					appState.getDriverCache()
					);
			useCasePanel.setEditable( false );
			
			rightSidePanel.add( useCasePanel, BorderLayout.CENTER );
			
		} else if ( isFlow ) {
			currentUseCase = ( (Flow) o ).getUseCase();
			
			FlowPanel flowPanel = createFlowPanel( (Flow) o );
			flowPanel.setEditable( false );
			
			rightSidePanel.add( flowPanel, BorderLayout.CENTER );
		}
		rightSidePanel.updateUI();
		
		// Update the actions (and the buttons and menus)
		useCaseListAC.updateActions( o );
	}
	
	private FlowPanel createFlowPanel(Flow flow) {
		return new FlowPanel(
				appState.getCurrentSoftware(),
				currentUseCase,
				flow
				);
	}

	@Override
	public void created(final Software parent, final UseCase obj, final int index) {
		DefaultMutableTreeNode useCaseNode =
				new DefaultMutableTreeNode( obj, true );
		rootNode.insert( useCaseNode, index );
		treeModel.nodesWereInserted( rootNode, new int[] { index } );
		
		// Select the node on the tree
		TreeNode[] nodes = treeModel.getPathToRoot( useCaseNode );
		tree.setSelectionPath( new TreePath( nodes ) );
	}

	@Override
	public void updated(final Software parent, final UseCase obj, final int index) {
		treeModel.nodesChanged( rootNode, new int[] { index } );
		
		// Show the node on the panel
		select( obj );
	}

	@Override
	public void deleted(final Software parent, final UseCase obj, final int index) {
		
		DefaultMutableTreeNode useCaseNode = (DefaultMutableTreeNode)
				rootNode.getChildAt( index );
		
		useCaseNode.removeAllChildren();
		rootNode.remove( useCaseNode );
		
		treeModel.nodesWereRemoved( rootNode, new int[] { index },
			new Object[] { useCaseNode } );
		
		// Select the root node (this updates the panel too)
		tree.setSelectionPath( new TreePath( rootNode ) );
	}

	@Override
	public void created(UseCase parent, Flow obj, int index) {
		if ( index < 0 ) { return; }
		DefaultMutableTreeNode useCaseNode = useCaseNode( parent );
		if ( null == useCaseNode ) { return; }
		
		DefaultMutableTreeNode flowNode = new DefaultMutableTreeNode( obj, false );
		useCaseNode.insert( flowNode, index );

		treeModel.nodesWereInserted( useCaseNode, new int[] { index } );
		
		// Select the node on the tree
		TreeNode[] nodes = treeModel.getPathToRoot( flowNode );
		tree.setSelectionPath( new TreePath( nodes ) );
	}

	@Override
	public void updated(UseCase parent, Flow obj, int index) {
		if ( index < 0 ) { return; }
		DefaultMutableTreeNode useCaseNode = useCaseNode( parent );
		if ( null == useCaseNode ) { return; }
		treeModel.nodesChanged( useCaseNode, new int[] { index } );
		
		// Show the node on the panel
		select( obj );
	}

	@Override
	public void deleted(UseCase parent, Flow obj, int index) {
		if ( index < 0 ) { return; }
		DefaultMutableTreeNode useCaseNode = useCaseNode( parent );
		if ( null == useCaseNode ) { return; }
		
		DefaultMutableTreeNode flowNode = (DefaultMutableTreeNode)
				useCaseNode.getChildAt( index );
		if ( null == flowNode ) { return; }
		
		useCaseNode.remove( flowNode );
		
		treeModel.nodesWereRemoved( useCaseNode, new int[] { index },
				new Object[] { flowNode } );
		
		// Select the use case node (this updates the panel too)
		TreeNode[] nodes = treeModel.getPathToRoot( useCaseNode );
		tree.setSelectionPath( new TreePath( nodes ) );
	}
	
	private DefaultMutableTreeNode useCaseNode(UseCase parent) {
		final int useCaseIndex =
				appState.getProject().getSoftware().indexOfUseCase( parent );
		if ( useCaseIndex < 0 ) { return null; }
		DefaultMutableTreeNode useCaseNode = (DefaultMutableTreeNode)
				rootNode.getChildAt( useCaseIndex );
		return useCaseNode;
	}
}
