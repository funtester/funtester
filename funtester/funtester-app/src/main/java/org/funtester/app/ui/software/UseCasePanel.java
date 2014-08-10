package org.funtester.app.ui.software;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.software.actions.DatabaseScriptActionContainer;
import org.funtester.app.ui.software.actions.ElementActionContainer;
import org.funtester.app.ui.software.actions.IncludeFileActionContainer;
import org.funtester.app.ui.software.actions.PreconditionActionContainer;
import org.funtester.app.ui.thirdparty.FocusUtil;
import org.funtester.app.ui.util.BaseTableModel;
import org.funtester.app.ui.util.CRUDTablePanel;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.JTableUtil;
import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.software.Actor;
import org.funtester.core.software.ConditionState;
import org.funtester.core.software.DatabaseScript;
import org.funtester.core.software.Element;
import org.funtester.core.software.ElementUseAnalyzer;
import org.funtester.core.software.Flow;
import org.funtester.core.software.IncludeFile;
import org.funtester.core.software.Software;
import org.funtester.core.software.UseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Use case panel
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class UseCasePanel
		extends JPanel
		implements FocusListener {

	private static final long serialVersionUID = 1398336879713074759L;
	private static final Logger logger = LoggerFactory.getLogger( UseCasePanel.class );
	
	private final UseCase useCase;
	
	private final JTabbedPane tabbedPane;
	
	// INFORMATION
	
	private final JPanel informationTabPanel;
	private final JCheckBox ignoreToGenerateTests;	
	private final JTextField name;
	private final JTextArea description;
	private final JTextField actors;
	private final JScrollPane actorScrollPane;
	private final JTable actorsTable;
	private final JScrollPane descriptionScrollPane;

	private final JPanel preconditionPanel;
	private final JPanel preconditionButtonsPanel;
	private final JButton preconditionNewButton;
	private final JButton preconditionEditButton;
	private final JButton preconditionRemoveButton;
	private final JPopupMenu preconditionNewPopupMenu;
	private final JMenuItem preconditionNewMenuItem;
	private final JMenuItem preconditionNewFromPostconditionMenuItem;
	private final CRUDTablePanel preconditionTablePanel;
	
	private final ActorSelectionTableModel actorSelectionTM;
	private final BaseTableModel< ConditionState > conditionStateTM;
	private final PreconditionActionContainer preconditionAC;
	
	// ELEMENT
	
	private final JPanel elementTabPanel;
	private final JPanel elementButtonPanel;
	private final CRUDTablePanel elementTablePanel;
	private final JButton elementNewButton;
	private final JButton elementEditButton;
	private final JButton elementRemoveButton;
	
	private final BaseTableModel< Element > elementTM;
	private final ElementActionContainer elementAC;
	
	// INCLUDE FILE
	
	private final JPanel includeFileTabPanel;
	private final JPanel includeFileButtonPanel;
	private final JButton includeFileNewButton;
	private final JButton includeFileEditButton;
	private final JButton includeFileRemoveButton;
	private final CRUDTablePanel includeFileTablePanel;
	
	private final BaseTableModel< IncludeFile > includeFileTM;
	private final IncludeFileActionContainer includeFileAC;
	
	// DATABASE
	
	private final JPanel databaseScriptTabPanel;
	private final JPanel databaseScriptButtonPanel;
	private final JButton databaseScriptNewButton;
	private final JButton databaseScriptEditButton;
	private final JButton databaseScriptRemoveButton;
	private final CRUDTablePanel databaseScriptTablePanel;
	
	private final BaseTableModel< DatabaseScript > databaseScriptTM;
	private final DatabaseScriptActionContainer databaseScriptAC;
	
	public UseCasePanel(
			final UseCase anUseCase,
			final Software software,
			final DriverCache driverCache
			) {
		
		if ( null == software ) throw new IllegalArgumentException( "software cannot be null." );
		this.useCase = ( anUseCase != null ) ? anUseCase.newCopy() : new UseCase();
		
		logger.debug( " this.useCase.elements @ " + System.identityHashCode( this.useCase.getElements() ) + " with size " + this.useCase.getElements().size() );

		actorSelectionTM = new ActorSelectionTableModel( software.getActors() );
		conditionStateTM = createConditionStateTableModel( this.useCase );
		elementTM = createElementTableModel( this.useCase );
		includeFileTM = createIncludeFileTableModel( this.useCase );
		databaseScriptTM = createDatabaseScriptTableModel( this.useCase );
		
		preconditionAC = new PreconditionActionContainer( conditionStateTM, software );
		preconditionAC.setCurrentUseCase( this.useCase ); // Reference
		elementAC = new ElementActionContainer( elementTM, this.useCase, software, driverCache );
		includeFileAC = new IncludeFileActionContainer( includeFileTM );
		databaseScriptAC = new DatabaseScriptActionContainer( databaseScriptTM );
		
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(320dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(35dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, "2, 2, fill, fill");
		
		//
		// INFORMATION TAB
		//
		
		informationTabPanel = new JPanel();
		informationTabPanel.setName( "informationTabPanel" );
		tabbedPane.addTab(Messages.getString("UseCasePanel.informationTabPanel.title"), null, informationTabPanel, null); //$NON-NLS-1$
		informationTabPanel.setLayout(new FormLayout(new ColumnSpec[] {
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
				RowSpec.decode("max(44dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		ignoreToGenerateTests = new JCheckBox(Messages.getString("UseCasePanel.ignoreToGenerateTests.text")); //$NON-NLS-1$
		informationTabPanel.add(ignoreToGenerateTests, "4, 2");
		
		JLabel lblName = new JLabel(Messages.getString("UseCasePanel.lblName.text")); //$NON-NLS-1$
		informationTabPanel.add(lblName, "2, 4");
		
		name = new JTextField();
		name.setName("name");
		informationTabPanel.add(name, "4, 4");
		name.addFocusListener( this );
		
		JLabel lblDescription = new JLabel(Messages.getString("UseCasePanel.lblDescription.text")); //$NON-NLS-1$
		informationTabPanel.add(lblDescription, "2, 6, default, top");
		
		descriptionScrollPane = new JScrollPane();
		informationTabPanel.add(descriptionScrollPane, "4, 6, fill, fill");
		
		description = new JTextArea();
		description.setName("description");
		descriptionScrollPane.setViewportView(description);
		description.setFont(new Font("Tahoma", Font.PLAIN, 13));
		description.addFocusListener( this );
		
		JLabel lblActors = new JLabel(Messages.getString("UseCasePanel.lblActors.text")); //$NON-NLS-1$
		informationTabPanel.add(lblActors, "2, 8");
		
		actors = new JTextField();
		actors.setName("actors");
		actors.setEditable(false);
		informationTabPanel.add(actors, "4, 8");
		actors.setColumns(10);
		
		actorScrollPane = new JScrollPane();
		actorScrollPane.setName("actorScrollPane");
		informationTabPanel.add(actorScrollPane, "4, 10");
		
		actorsTable = new JTable();
		actorsTable.setName("actorsTable");
		actorScrollPane.setViewportView(actorsTable);
		actorsTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		actorsTable.setShowVerticalLines( false );
		actorsTable.setShowHorizontalLines( false );
		actorsTable.setShowGrid( false );
		actorsTable.setModel( actorSelectionTM );
		actorsTable.getColumnModel().getColumn(0).setPreferredWidth(15);
		actorsTable.getColumnModel().getColumn(0).setMaxWidth(15);
		actorsTable.getColumnModel().getColumn(1).setMaxWidth(600);
		
		// Hide table headers
		actorsTable.setTableHeader( null );
		actorScrollPane.setColumnHeaderView( null );
		
		JLabel lblPreconditions = new JLabel(Messages.getString("UseCasePanel.lblPreconditions.text")); //$NON-NLS-1$
		informationTabPanel.add(lblPreconditions, "2, 12, default, top");
		
		// Precondition
		
		preconditionPanel = new JPanel();
		preconditionPanel.setName( "preconditionPanel" );
		informationTabPanel.add(preconditionPanel, "4, 12, fill, fill");
		preconditionPanel.setLayout(new BorderLayout(0, 0));
		
		preconditionButtonsPanel = new JPanel();
		preconditionPanel.add(preconditionButtonsPanel, BorderLayout.NORTH);
		preconditionButtonsPanel.setName( "preconditionButtonsPanel" );
		preconditionButtonsPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,},
			new RowSpec[] {
				RowSpec.decode("25px"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		preconditionNewButton = new JButton( Messages.alt( "_NEW", "Novo..." ) );
		preconditionButtonsPanel.add(preconditionNewButton, "1, 1");
		preconditionNewButton.setIcon( ImageUtil.loadIcon( ImagePath.dropDownIcon() ) );
		preconditionNewButton.setIconTextGap(20);
		preconditionNewButton.setHorizontalTextPosition(SwingConstants.LEFT);
		preconditionNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//if ( e.getX() > preconditionNewButton.getWidth() / 2 ) {
					preconditionNewPopupMenu.show( preconditionNewButton, e.getX(), e.getY() );
				//}
			}
		});
		
		preconditionEditButton = new JButton( preconditionAC.editAction() );
		preconditionButtonsPanel.add(preconditionEditButton, "3, 1");
		
		preconditionRemoveButton = new JButton( preconditionAC.removeAction() );
		preconditionButtonsPanel.add(preconditionRemoveButton, "5, 1");
		
		preconditionTablePanel = new CRUDTablePanel();
		preconditionTablePanel.setName( "preconditionTablePanel" );
		preconditionTablePanel.setTableModel( conditionStateTM );
		// Set the action container as a selection listener		
		preconditionTablePanel.addListSelectionListener( preconditionAC );
		// Link the actions (no new action, new is a menu)
		preconditionTablePanel.setEditAction( preconditionAC.editAction() );
		preconditionTablePanel.setRemoveAction( preconditionAC.removeAction() );
		// Adjust the column widths
		JTableUtil.adjustMaxColumnWidths( preconditionTablePanel.getTable(),
				new int[] { 40 } );		
		
		preconditionPanel.add(preconditionTablePanel);
		
		preconditionNewPopupMenu = new JPopupMenu();
		preconditionNewFromPostconditionMenuItem = new JMenuItem(
				preconditionAC.newFromPostconditionAction() );		
		preconditionNewPopupMenu.add(preconditionNewFromPostconditionMenuItem);
		preconditionNewMenuItem = new JMenuItem( preconditionAC.preconditionNewAction() );
		preconditionNewPopupMenu.add(preconditionNewMenuItem);		
		
		//
		// ELEMENT TAB
		//
		
		elementTabPanel = new JPanel();
		elementTabPanel.setName( "elementTabPanel" );
		elementTabPanel.setLayout(new BorderLayout(0, 0));
		
		tabbedPane.addTab(Messages.getString("UseCasePanel.elementTabPanel.title"), null, elementTabPanel, null);
		
		elementButtonPanel = new JPanel();
		elementTabPanel.add(elementButtonPanel, BorderLayout.NORTH);
		elementButtonPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		elementNewButton = new JButton( elementAC.getNewAction() );
		elementNewButton.setName( "elementNewButton" );
		elementButtonPanel.add(elementNewButton, "1, 2");
		
		elementEditButton = new JButton( elementAC.getEditAction() );
		elementEditButton.setName( "elementEditButton" );
		elementButtonPanel.add(elementEditButton, "3, 2");
		
		elementRemoveButton = new JButton( elementAC.getRemoveAction() );
		elementRemoveButton.setName( "elementRemoveButton" );
		elementButtonPanel.add(elementRemoveButton, "5, 2");
		
		elementTablePanel = new CRUDTablePanel();
		elementTablePanel.setName( "elementTablePanel" );
		elementTablePanel.setTableModel( elementTM );
		elementTablePanel.addListSelectionListener( elementAC );
		
		JTableUtil.adjustMinColumnWidths( elementTablePanel.getTable(), new int[] { 20, 150, 150, 100, 30 } );
		JTableUtil.adjustMaxColumnWidths( elementTablePanel.getTable(), new int[] { 30, 400, 400, 300, 60 } );
		
		elementTablePanel.setNewAction( elementAC.getNewAction() );
		elementTablePanel.setEditAction( elementAC.getEditAction() );
		elementTablePanel.setRemoveAction( elementAC.getRemoveAction() );
		
		elementTabPanel.add(elementTablePanel, BorderLayout.CENTER);
		
		//
		// INCLUDE FILE TAB
		//
		
		includeFileTabPanel = new JPanel();
		includeFileTabPanel.setName( "includeFileTabPanel" );
		includeFileTabPanel.setLayout(new BorderLayout(0, 0));
		
		tabbedPane.addTab(Messages.getString("UseCasePanel.includeFileTabPanel.title"), null, includeFileTabPanel, null);
		
		
		includeFileButtonPanel = new JPanel();
		includeFileButtonPanel.setName( "includeFileButtonPanel" );
		includeFileTabPanel.add(includeFileButtonPanel, BorderLayout.NORTH);
		includeFileButtonPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		includeFileNewButton = new JButton( includeFileAC.getNewAction() );
		includeFileNewButton.setName( "includeFileNewButton" );
		includeFileButtonPanel.add(includeFileNewButton, "1, 2");
		
		includeFileEditButton = new JButton( includeFileAC.getEditAction() );
		includeFileEditButton.setName( "includeFileEditButton" );
		includeFileButtonPanel.add(includeFileEditButton, "3, 2");
		
		includeFileRemoveButton = new JButton( includeFileAC.getRemoveAction() );
		includeFileRemoveButton.setName( "includeFileRemoveButton" );
		includeFileButtonPanel.add(includeFileRemoveButton, "5, 2");
		
		includeFileTablePanel = new CRUDTablePanel();
		includeFileTablePanel.setName( "includeFileTablePanel" );
		includeFileTablePanel.setTableModel( includeFileTM );		
		includeFileTablePanel.addListSelectionListener( includeFileAC );
		
		JTableUtil.adjustMaxColumnWidths( includeFileTablePanel.getTable(), new int[] { 40 } );
		
		includeFileTablePanel.setNewAction( includeFileAC.getNewAction() );
		includeFileTablePanel.setEditAction( includeFileAC.getEditAction() );
		includeFileTablePanel.setRemoveAction( includeFileAC.getRemoveAction() );
		
		includeFileTabPanel.add(includeFileTablePanel, BorderLayout.CENTER);

		//
		// DATABASE SCRIPT TAB
		//
		
		databaseScriptTabPanel = new JPanel();
		databaseScriptTabPanel.setVisible(false);
		databaseScriptTabPanel.setName( "databaseScriptTabPanel" );
		tabbedPane.addTab(Messages.getString("UseCasePanel.databaseScriptTabPanel.title"), null, databaseScriptTabPanel, null);
		databaseScriptTabPanel.setLayout(new BorderLayout(0, 0));
		
		databaseScriptButtonPanel = new JPanel();
		databaseScriptButtonPanel.setName( "databaseScriptButtonPanel" );
		databaseScriptTabPanel.add(databaseScriptButtonPanel, BorderLayout.NORTH);
		databaseScriptButtonPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		databaseScriptNewButton = new JButton( databaseScriptAC.getNewAction() );
		databaseScriptNewButton.setName( "databaseScriptNewButton" ); //$NON-NLS-1$
		databaseScriptButtonPanel.add(databaseScriptNewButton, "1, 2");
		
		databaseScriptEditButton = new JButton( databaseScriptAC.getEditAction() );
		databaseScriptEditButton.setName( "databaseScriptEditButton" );
		databaseScriptButtonPanel.add(databaseScriptEditButton, "3, 2");
		
		databaseScriptRemoveButton = new JButton( databaseScriptAC.getRemoveAction() );
		databaseScriptRemoveButton.setName( "databaseScriptRemoveButton" );
		databaseScriptButtonPanel.add(databaseScriptRemoveButton, "5, 2");
		
		databaseScriptTablePanel = new CRUDTablePanel();
		databaseScriptTablePanel.setName( "databaseScriptTablePanel" );
		databaseScriptTabPanel.add(databaseScriptTablePanel);
		databaseScriptTablePanel.setTableModel( databaseScriptTM );
		databaseScriptTablePanel.addListSelectionListener( databaseScriptAC );
		databaseScriptTablePanel.setName( "databaseScriptListPanel" );		
		databaseScriptTablePanel.setNewAction( databaseScriptAC.getNewAction() );
		databaseScriptTablePanel.setEditAction( databaseScriptAC.getEditAction() );
		databaseScriptTablePanel.setRemoveAction( databaseScriptAC.getRemoveAction() );
		
		JTableUtil.adjustMaxColumnWidths( databaseScriptTablePanel.getTable(), new int[] { 40 } );
		
		//
		// DRAW
		//
		
		FocusUtil.patch( description );
		FocusUtil.patch( actors );
		FocusUtil.patch( actorsTable );
		FocusUtil.patch( actorScrollPane );
		
		draw( this.useCase );
		
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				name.requestFocusInWindow();
			}
		} );
	}

	private void draw(final UseCase uc) {
		ignoreToGenerateTests.setSelected( uc.getIgnoreToGenerateTests() );
		name.setText( uc.getName() );
		description.setText( uc.getDescription() );
		actors.setText( actorNames( uc.getActors() ) );
		actorSelectionTM.setSelectedActors( uc.getActors() );
	}

	private String actorNames(final Collection< Actor > actors) {
		int count = actors.size();
		final String SEPARATOR = ", ";
		StringBuilder b = new StringBuilder();
		for ( Actor a : actors ) {
			b.append( a.getName() );
			if ( count > 1 ) {
				b.append( SEPARATOR );
				count--;
			}
		}
		return b.toString();
	}

	public static boolean accepts(Object o) {
		return ( o instanceof UseCase );
	}
	
	public UseCase get() {
		useCase.setIgnoreToGenerateTests( ignoreToGenerateTests.isSelected() );
		useCase.setName( name.getText() );
		useCase.setDescription( description.getText() );
		return useCase;
	}	
	
	public void setEditable(final boolean editable) {
		ignoreToGenerateTests.setEnabled( editable );
		name.setEditable( editable );
		description.setEditable( editable );
		description.setBackground( name.getBackground() );
		// actors is ALWAYS not editable
		actors.setVisible( ! editable ); // actors TextField is shown ONLY IF not editable
		actorScrollPane.setVisible( editable );
		if ( actorScrollPane.isVisible() ) {
			Rectangle r = actorScrollPane.getBounds();
			r.y = actors.getBounds().y;
			actorScrollPane.setBounds( r );
		}
		setPreconditionEditable( editable );
		elementTablePanel.setEditable( editable );
		includeFileTablePanel.setEditable( editable );
		//databaseScriptTablePanel.setEditable( editable );
		databaseScriptTablePanel.setEditable( false ); // For now
	}

	private void setPreconditionEditable(final boolean editable) {
		preconditionTablePanel.setEditable( editable );
		preconditionButtonsPanel.setEnabled( editable );
		preconditionButtonsPanel.setVisible( editable );
	}

	@Override
	public void focusGained(FocusEvent e) { }

	@Override
	public void focusLost(FocusEvent e) {
		if ( e.isTemporary() ) {
			return;
		}		
		final Object o = e.getSource();
		if ( name == o ) {
			useCase.setName( ( (JTextField)  o ).getText() );
		} else if ( description == o ) {
			useCase.setDescription( ( (JTextArea) o ).getText() );
		}
	}
	
	private BaseTableModel< ConditionState > createConditionStateTableModel(
			final UseCase anUseCase
			) {
		
		final String[] columns = {
			"#",
			Messages.alt( "_PRECONDITION_DESCRIPTION", "Description" )
		};

		BaseTableModel< ConditionState > tableModel =
			new BaseTableModel< ConditionState >(
				columns.length,
				anUseCase.getPreconditions(),
				columns
				) {
			private static final long serialVersionUID = 1146810835860824248L;
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch ( columnIndex ) {
					case 0 : return rowIndex + 1;
					default: return itemAt( rowIndex ).getDescription();
				}
			}
		};
		
		tableModel.setColumnClasses( Integer.class, String.class );
		return tableModel;
	}
	
	private BaseTableModel< Element > createElementTableModel(
			final UseCase anUseCase
			) {
		final String[] columns = {
			"#",
			Messages.alt( "_ELEMENT", "Element" ),
			Messages.alt( "_ELEMENT_INTERNAL_NAME", "Internal Name" ),
			Messages.alt( "_ELEMENT_TYPE", "Type" ),
			Messages.alt( "_ELEMENT_EDITABLE", "Editable" ),
			Messages.alt( "_ELEMENT_FLOWS_USING", "Flows using the element" )
		};
		
		final ElementUseAnalyzer analyzer = new ElementUseAnalyzer();
		BaseTableModel< Element > tableModel =
			new BaseTableModel< Element >(
				columns.length,
				anUseCase.getElements(),
				columns
				) {
			private static final long serialVersionUID = -5631384311194352912L;
			
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch ( columnIndex ) {
					case 0 : return rowIndex + 1;
					case 1 : return itemAt( rowIndex ).getName();
					case 2 : return itemAt( rowIndex ).getInternalName();
					case 3 : return ( itemAt( rowIndex ).getType() != null )
							? itemAt( rowIndex ).getType().getName() : "";
					case 4 : return itemAt( rowIndex ).isEditable()
							? Messages.alt( "_YES",  "Yes" )
							: Messages.alt( "_NO",  "No" );
					default: return flowsUsingElement( itemAt( rowIndex ) );
				}
			}
			
			private String flowsUsingElement(final Element e) {
				Collection< Flow > flows = analyzer.flowsUsingElement(
						e,
						anUseCase.getFlows()
						);
				final int last = flows.size() - 1;
				StringBuilder sb = new StringBuilder();
				int i = 0;
				for ( Flow f : flows ) {
					sb.append( f.shortName() );
					if ( i < last ) sb.append( ", " );
					++i;
				}
				return sb.toString();
			}
		};
		
		tableModel.setColumnClasses( Integer.class, String.class );
		return tableModel;
	}	

	private BaseTableModel< IncludeFile > createIncludeFileTableModel(
			final UseCase anUseCase
			) {
		
		final String[] columns = {
				"#",
				Messages.alt( "_INCLUDE_FILE", "File" )
			};
		
		BaseTableModel< IncludeFile > tableModel =
			new BaseTableModel< IncludeFile >(
				columns.length,
				anUseCase.getIncludeFiles(),
				columns
				) {
			private static final long serialVersionUID = 1146810835860824248L;
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch ( columnIndex ) {
					case 0	: return rowIndex + 1;
					default	: return itemAt( rowIndex );
				}
			}
		};
		
		tableModel.setColumnClasses( Integer.class, String.class );
		return tableModel;
	}
	
	private BaseTableModel< DatabaseScript > createDatabaseScriptTableModel(
			final UseCase anUseCase
			) {
		
		final String[] columns = {
				"#",
				Messages.alt( "_DATABASE_SCRIPT_DESCRIPTION", "Description" )
			};
		
		BaseTableModel< DatabaseScript > tableModel =
			new BaseTableModel< DatabaseScript >(
				columns.length,
				anUseCase.getDatabaseScripts(),
				columns
				) {
			private static final long serialVersionUID = -814933737958200603L;
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch ( columnIndex ) {
					case 0	: return rowIndex + 1;
					default	: return itemAt( rowIndex ).getDescription();
				}
			}
		};
		
		tableModel.setColumnClasses( Integer.class, String.class );
		return tableModel;
	}

}
