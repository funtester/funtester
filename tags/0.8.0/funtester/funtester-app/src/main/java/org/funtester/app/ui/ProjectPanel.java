package org.funtester.app.ui;

import java.awt.BorderLayout;
import java.util.Collection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppState;
import org.funtester.app.ui.software.DatabaseQueryPanel;
import org.funtester.app.ui.software.UseCaseListPanel;
import org.funtester.app.ui.software.actions.ActorActionContainer;
import org.funtester.app.ui.software.actions.DatabaseConfigActionContainer;
import org.funtester.app.ui.software.actions.QueryConfigActionContainer;
import org.funtester.app.ui.software.actions.RegExActionContainer;
import org.funtester.app.ui.software.actions.UseCaseListActionContainer;
import org.funtester.app.ui.util.BaseTableModel;
import org.funtester.app.ui.util.CRUDTablePanel;
import org.funtester.app.ui.util.JTableUtil;
import org.funtester.core.software.Actor;
import org.funtester.core.software.RegEx;
import org.funtester.core.software.Software;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Project panel.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ProjectPanel extends JPanel {

	private static final long serialVersionUID = 4187301841622282844L;
	
	private final UseCaseListPanel useCaseListPanel;
	
	private final ActorActionContainer actorAC;
	private final RegExActionContainer regExAC;
	private final DatabaseConfigActionContainer databaseConfigAC;
	private final QueryConfigActionContainer queryConfigAC;
	
	public ProjectPanel(final AppState appState) {		
		if ( null == appState ) throw new IllegalArgumentException( "state is null" );
		
		final Software software = appState.getCurrentSoftware();
		if ( null == software ) throw new IllegalArgumentException( "current software is null" );
		
		// ACTOR
		
		final BaseTableModel< Actor > actorTM =
				createActorTableModel( software.getActors() );
		actorAC = new ActorActionContainer( actorTM, software );
		
		// REGEX
		
		final BaseTableModel< RegEx > regExTM =
				createRegExTableModel( software.getRegularExpressions() );
		regExAC = new RegExActionContainer( regExTM, software );
		

		
		// DATABASE
		databaseConfigAC = new DatabaseConfigActionContainer(
				software,
				appState.getDriverTemplateMap(),
				appState.getDriverCache()
				);
		
		// QUERY
		queryConfigAC = new QueryConfigActionContainer( software,
				appState.getDriverCache()  );
		
		
		setLayout( new BorderLayout(0, 0) );
		
		// Top
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		add( topPanel, BorderLayout.NORTH );
		// Center
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add( tabbedPane, BorderLayout.CENTER );
		
		useCaseListPanel = new UseCaseListPanel( appState );
		
		JPanel panelUseCases = new JPanel();
		tabbedPane.addTab( Messages.getString("_USE_CASES"), null, panelUseCases, null);  //$NON-NLS-1$
		panelUseCases.setLayout(new BorderLayout(0, 0));		
		panelUseCases.add( useCaseListPanel );
		
		// ACTOR
		
		JPanel actorPanel = new JPanel();
		tabbedPane.addTab( Messages.getString("_ACTORS"), null, actorPanel, null );
		actorPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel actorButtonPanel = new JPanel();
		actorPanel.add(actorButtonPanel, BorderLayout.NORTH);
		actorButtonPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JButton actorNew = new JButton( actorAC.newAction() );
		actorNew.setName("actorNew");
		actorButtonPanel.add(actorNew, "1, 2");
		
		JButton actorEdit = new JButton( actorAC.editAction() );
		actorEdit.setName("actorEdit");
		actorButtonPanel.add(actorEdit, "3, 2");
		
		JButton actorRemove = new JButton( actorAC.removeAction() );
		actorRemove.setName("actorRemove");
		actorButtonPanel.add(actorRemove, "5, 2");
		
		CRUDTablePanel actorContentPanel = new CRUDTablePanel();
		actorPanel.add(actorContentPanel, BorderLayout.CENTER);
		actorContentPanel.setTableModel( actorTM );
		actorContentPanel.addListSelectionListener( actorAC );
		actorContentPanel.setNewAction( actorAC.newAction() );
		actorContentPanel.setEditAction( actorAC.editAction() );
		actorContentPanel.setRemoveAction( actorAC.removeAction() );
		JTableUtil.adjustMinColumnWidths( actorContentPanel.getTable(), new int[] { 15, 100, 200 } );
		JTableUtil.adjustMaxColumnWidths( actorContentPanel.getTable(), new int[] { 30, 200 } );
		
		// DATABASE
		
		final JPanel databasePanel = new JPanel();
		databasePanel.setLayout(new BorderLayout(0, 0));
		tabbedPane.addTab( Messages.getString("_DATABASES_AND_QUERIES"), null, databasePanel, null ); //$NON-NLS-1$
		
		final DatabaseQueryPanel databaseQueryPanel = new DatabaseQueryPanel(
					appState.getCurrentSoftware(),
					appState.getDriverTemplateMap(),
					appState.getDriverCache(),
					databaseConfigAC,
					queryConfigAC
					);
		databasePanel.add(databaseQueryPanel, BorderLayout.CENTER);
		
		// REGEX
		
		final JPanel regExPanel = new JPanel();
		tabbedPane.addTab( Messages.getString("_REGEXS"), null, regExPanel, null );
		regExPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel regExButtonPanel = new JPanel();
		regExPanel.add(regExButtonPanel, BorderLayout.NORTH);
		regExButtonPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JButton regExNew = new JButton( regExAC.newAction() );
		regExNew.setName("regExNew");
		regExButtonPanel.add(regExNew, "1, 2");
		
		JButton regExEdit = new JButton( regExAC.editAction() );
		regExEdit.setName("regExEdit");
		regExButtonPanel.add(regExEdit, "3, 2");
		
		JButton regExRemove = new JButton( regExAC.removeAction() );
		regExRemove.setName("regExRemove");
		regExButtonPanel.add(regExRemove, "5, 2");
		
		final CRUDTablePanel regExContentPanel = new CRUDTablePanel();
		regExPanel.add(regExContentPanel, BorderLayout.CENTER);
		regExContentPanel.setTableModel( regExTM );
		regExContentPanel.addListSelectionListener( regExAC );
		regExContentPanel.setNewAction( regExAC.newAction() );
		regExContentPanel.setEditAction( regExAC.editAction() );
		regExContentPanel.setRemoveAction( regExAC.removeAction() );
		JTableUtil.adjustMaxColumnWidths( regExContentPanel.getTable(), new int[] { 40, 300 } );
		
		// Configure the tabbed pane to update some tab data when they are
		// activated.
		
		tabbedPane.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane tp = (JTabbedPane) e.getSource();
				int index = tp.getSelectedIndex();
				if ( index == tp.indexOfTabComponent( regExPanel ) ) {
					regExContentPanel.getTable().updateUI();
				} else if ( index == tp.indexOfTabComponent( databasePanel ) ) {
					databaseQueryPanel.updateTreeUI();
				}
			}
		} );		
	}

	public UseCaseListActionContainer getUseCaseListActionContainer() {
		return useCaseListPanel.getActionContainer();
	}
	
	public ActorActionContainer getActorActionContainer() {
		return actorAC;
	}
	
	public RegExActionContainer getRegExActionContainer() {
		return regExAC;
	}
	
	public DatabaseConfigActionContainer getDatabaseConfigActionContainer() {
		return databaseConfigAC;
	}
	
	public QueryConfigActionContainer getQueryConfigActionContainer() {
		return queryConfigAC;
	}
	
	private BaseTableModel< Actor > createActorTableModel(Collection< Actor > actors) {
		if ( null == actors ) throw new IllegalArgumentException( "actors is null" );
		
		final String columns[] = {
				"#",
				Messages.alt( "_ACTOR_NAME", "Name" ),
				Messages.alt( "_ACTOR_DESCRIPTION", "Description" )
		};
		
		BaseTableModel< Actor > tableModel = new BaseTableModel< Actor >(
				columns.length,
				actors,
				columns
				) {
			
			private static final long serialVersionUID = 8648427168130748316L;

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch ( columnIndex ) {
					case 0	: return rowIndex + 1;
					case 1  : return itemAt( rowIndex ).getName();
					default	: return itemAt( rowIndex ).getDescription();
				}
			}
		};
		tableModel.setColumnClasses( Integer.class, String.class, String.class );
		return tableModel;
	}

	private BaseTableModel< RegEx > createRegExTableModel(
			List< RegEx > regularExpressions) {
		if ( null == regularExpressions ) throw new IllegalArgumentException( "regularExpressions is null" );
		
		final String columns[] = {
				"#",
				Messages.alt( "_REGEX_NAME", "Name" ),
				Messages.alt( "_REGEX_EXPRESSION", "Expression" )
		};
		
		BaseTableModel< RegEx > tableModel = new BaseTableModel< RegEx >(
				columns.length,
				regularExpressions,
				columns
				) {

			private static final long serialVersionUID = 2840326955137256465L;

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch ( columnIndex ) {
					case 0	: return rowIndex + 1;
					case 1  : return itemAt( rowIndex ).getName();
					default	: return itemAt( rowIndex ).getExpression();
				}
			}
		};
		tableModel.setColumnClasses( Integer.class, String.class, String.class );
		return tableModel;
	}
}
