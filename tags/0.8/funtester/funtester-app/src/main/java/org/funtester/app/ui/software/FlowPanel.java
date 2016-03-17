package org.funtester.app.ui.software;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.EnumTranslation;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.software.actions.PostconditionActionContainer;
import org.funtester.app.ui.software.actions.StepActionContainer;
import org.funtester.app.ui.util.BaseTableModel;
import org.funtester.app.ui.util.CRUDTablePanel;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.JTableUtil;
import org.funtester.common.Importance;
import org.funtester.core.profile.StepKind;
import org.funtester.core.profile.Trigger;
import org.funtester.core.software.AlternateFlow;
import org.funtester.core.software.Complexity;
import org.funtester.core.software.Flow;
import org.funtester.core.software.FlowType;
import org.funtester.core.software.Frequency;
import org.funtester.core.software.ImportanceCalculator;
import org.funtester.core.software.NewStep;
import org.funtester.core.software.Postcondition;
import org.funtester.core.software.Priority;
import org.funtester.core.software.ReturnableFlow;
import org.funtester.core.software.Software;
import org.funtester.core.software.Step;
import org.funtester.core.software.UseCase;
import org.funtester.core.vocabulary.ActionNickname;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Panel for a {@link Flow}.
 *
 * @author Thiago Delgado Pinto
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class FlowPanel extends JPanel { // implements CommandEventListener {

	private static final long serialVersionUID = 140483603619934239L;

	private final Logger logger = LoggerFactory.getLogger( FlowPanel.class );

	private final UseCase useCase;
	private final Flow flow;
	private boolean configuring = true;
	private boolean editable = true;

	private final JLabel lblFlowType;
	private final JLabel flowType;

	private final JComboBox priority;
	private final JComboBox frequency;
	private final JComboBox complexity;
	private final JComboBox importance;
	private final JLabel lblImportanceSuggestion;

	private final JLabel lblDescription;
	private final JTextField description;

	private final JLabel lblStarterFlow;
	private final JComboBox starterFlow;

	private final JLabel lblStarterStep;
	private final JComboBox starterStep;

	private final JLabel lblReturningFlow;
	private final JComboBox returningFlow;

	private final JLabel lblReturningStep;
	private final JComboBox returningStep;

	private final JButton stepNewButton;
	private final JButton stepNewActionStepButton; // Visible for activation flows only
	private final JButton stepCloneButton;
	private final JButton stepEditButton;
	private final JButton stepRemoveButton;
	private final JButton stepUpButton;
	private final JButton stepDownButton;
	private final CRUDTablePanel stepContentPanel;

	private final JButton postconditionNewButton;
	private final JButton postconditionEditButton;
	private final JButton postconditionRemoveButton;
	private final CRUDTablePanel postconditionContentPanel;

	private final StepActionContainer stepAC;
	private BaseTableModel< Step > stepTM; // Can be recreated if the step type is changed
	private final PostconditionActionContainer postconditionAC;
	private BaseTableModel< Postcondition > postconditionTM; // Can be recreated if the step type is changed


	public FlowPanel(
			final Software software,
			final UseCase anUseCase,
			final Flow aFlow
			) {
		if ( null == software ) throw new RuntimeException( "software cannot be null");
		if ( null == anUseCase ) throw new RuntimeException( "useCase cannot be null");
		this.useCase = anUseCase;
		this.flow = aFlow.newCopy(); // Clone

		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("right:default"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("200px:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(76dlu;pref):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("90px:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(64dlu;min)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));

		//
		// FLOW TYPE
		//

		lblFlowType = new JLabel(Messages.getString("FlowPanel.lblFlowType.text")); //$NON-NLS-1$
		add(lblFlowType, "2, 2");

		flowType = new JLabel("Flow");
		add(flowType, "4, 2, 7, 1");

		//
		// BASIC INFO
		//

		JLabel lblPriority = new JLabel(Messages.getString("FlowPanel.lblPriority.text")); //$NON-NLS-1$
		add(lblPriority, "2, 4, right, center");

		final List< String > priorityList = EnumTranslation.createList( Priority.class );

		priority = new JComboBox( new DefaultComboBoxModel( priorityList.toArray( new String[ 0 ] ) ) );
		priority.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				onChangeImportanceInfluencer( e );
			}
		});
		priority.setName("priority");
		priority.setSelectedIndex(2);
		add(priority, "4, 4, fill, top");

		final List< String > frequencyList = EnumTranslation.createList( Frequency.class );

		final List< String > complexityList = EnumTranslation.createList( Complexity.class );

		JLabel lblComplexity = new JLabel(Messages.getString("FlowPanel.lblComplexity.text")); //$NON-NLS-1$
		add(lblComplexity, "6, 4, right, default");

		complexity = new JComboBox( new DefaultComboBoxModel( complexityList.toArray( new String[ 0 ] ) ) );
		complexity.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				onChangeImportanceInfluencer( e );
			}
		});
		complexity.setName("complexity");
		complexity.setSelectedIndex(2);
		add(complexity, "8, 4");

		JLabel lblFrequency = new JLabel(Messages.getString("FlowPanel.lblFrequency.text")); //$NON-NLS-1$
		add(lblFrequency, "2, 6, right, center");

		frequency = new JComboBox( new DefaultComboBoxModel( frequencyList.toArray( new String[ 0 ] ) ) );
		frequency.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				onChangeImportanceInfluencer( e );
			}
		});
		frequency.setName("frequency");
		frequency.setSelectedIndex(1);
		add(frequency, "4, 6");

		JLabel lblImportance = new JLabel(Messages.getString("FlowPanel.lblImportance.text")); //$NON-NLS-1$
		add(lblImportance, "6, 6, right, default");

		final List< String > importanceList = EnumTranslation.createList( Importance.class );

		importance = new JComboBox( importanceList.toArray( new String[ 0 ] ) );
		add(importance, "8, 6, fill, default");

		lblImportanceSuggestion = new JLabel("<value>");
		lblImportanceSuggestion.setToolTipText( Messages.getString("FlowPanel.lblImportanceSuggestion.toolTipText") ); //$NON-NLS-1$
		lblImportanceSuggestion.setForeground(Color.GRAY);
		lblImportanceSuggestion.setIcon( ImageUtil.loadIcon( ImagePath.tipIcon() ) );
		lblImportanceSuggestion.setFont(new Font("Tahoma", Font.ITALIC, 13));
		add(lblImportanceSuggestion, "10, 6");

		//
		// DESCRIPTION
		//

		lblDescription = new JLabel(Messages.getString("FlowPanel.lblDescription.text")); //$NON-NLS-1$
		add(lblDescription, "2, 8, right, default");

		description = new JTextField();
		description.setName("description");
		description.setColumns(10);
		add(description, "4, 8, 7, 1, fill, default");

		//
		// STARTER FLOW & STEP
		//

		lblStarterFlow = new JLabel(Messages.getString("FlowPanel.lblStarterFlow.text")); //$NON-NLS-1$
		add(lblStarterFlow, "2, 10, right, default");

		starterFlow = new JComboBox( this.useCase.getFlows().toArray( new Flow[ 0 ]  ) );
		starterFlow.setName("starterFlow");
		starterFlow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( configuring ) { return; }
				configureStarterStep();
			}
		});
		add(starterFlow, "4, 10");

		lblStarterStep = new JLabel(Messages.getString("FlowPanel.lblStarterStep.text")); //$NON-NLS-1$
		add(lblStarterStep, "6, 10, right, default");

		starterStep = new JComboBox(); // Filled when the user selects the starter flow
		starterStep.setName("starterStep");
		add(starterStep, "8, 10, 3, 1");

		//
		// RETURNING FLOW & STEP
		//

		lblReturningFlow = new JLabel(Messages.getString("FlowPanel.lblReturningFlow.text")); //$NON-NLS-1$
		add(lblReturningFlow, "2, 12, right, default");

		returningFlow = new JComboBox( this.useCase.getFlows().toArray( new Flow[ 0 ]  ) );
		returningFlow.setName("returningFlow");
		returningFlow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( configuring ) { return; }
				configureReturningStep();
			}
		});
		add(returningFlow, "4, 12, fill, default");

		lblReturningStep = new JLabel(Messages.getString("FlowPanel.lblReturningStep.text")); //$NON-NLS-1$
		add(lblReturningStep, "6, 12, right, default");

		returningStep = new JComboBox(); // Filled when the user selects the returning flow
		returningStep.setName("returningStep");
		add(returningStep, "8, 12, 3, 1, fill, default");

		//
		// STEPS
		//

		stepTM = createStepTableModel();
		stepAC = new StepActionContainer( stepTM, software );
		stepAC.setFlow( flow );

		final ActionListener selectionUpAL = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( configuring ) { return; }
				final int index = stepAC.getSelectedIndex() - 1;
				stepContentPanel.getTable().setRowSelectionInterval( index, index );
			}
		};
		final ActionListener selectionDownAL = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( configuring ) { return; }
				final int index = stepAC.getSelectedIndex() + 1;
				stepContentPanel.getTable().setRowSelectionInterval( index, index );
			}
		};
		stepAC.registerAfterMoveUpActionListener( selectionUpAL );
		stepAC.registerAfterMoveDownActionListener( selectionDownAL );


		JLabel lblSteps = new JLabel(Messages.getString("FlowPanel.lblSteps.text")); //$NON-NLS-1$
		add(lblSteps, "2, 14, right, top");

		JPanel stepsPanel = new JPanel();
		stepsPanel.setName("stepsPanel");
		add(stepsPanel, "4, 14, 7, 1, fill, fill");
		stepsPanel.setLayout(new BorderLayout(0, 0));

		JPanel stepTopPanel = new JPanel();
		stepsPanel.add(stepTopPanel, BorderLayout.NORTH);
		stepTopPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				RowSpec.decode("25px"),
				FormFactory.RELATED_GAP_ROWSPEC,}));

		stepNewButton = new JButton( Messages.alt( "_NEW", "New" ) );
		stepNewButton.setName( "stepNewButton" );
		stepNewButton.setIcon( ImageUtil.loadIcon( ImagePath.dropDownIcon() ) );
		stepNewButton.setIconTextGap(20);
		stepNewButton.setHorizontalTextPosition( SwingConstants.LEFT );
		stepTopPanel.add(stepNewButton, "1, 1");

		final JPopupMenu stepNewPopupMenu = new JPopupMenu();

		JMenuItem stepNewActionStepMenuItem = new JMenuItem( stepAC.getNewActionStepAction() );
		stepNewActionStepMenuItem.setName( "stepNewActionStepMenuItem" );
		stepNewActionStepMenuItem.setText( Messages.alt( "_ACTION_STEP_NEW", "New Action Step..." ) );
		stepNewPopupMenu.add( stepNewActionStepMenuItem );

		JMenuItem stepNewDocStepMenuItem = new JMenuItem( stepAC.getNewDocStepAction() );
		stepNewDocStepMenuItem.setName( "stepNewDocStepMenuItem" );
		stepNewDocStepMenuItem.setText( Messages.alt( "_DOC_STEP_NEW", "New Documentation Step..." ) );
		stepNewPopupMenu.add( stepNewDocStepMenuItem );

		JMenuItem stepNewUseCaseCallStepMenuItem = new JMenuItem( stepAC.getNewUseCaseCallStepAction() );
		stepNewUseCaseCallStepMenuItem.setName( "stepNewUseCaseCallStepMenuItem" );
		stepNewUseCaseCallStepMenuItem.setText( Messages.alt( "_USE_CASE_CALL_STEP_NEW", "New Use Case Call Step..." ) );
		stepNewPopupMenu.add( stepNewUseCaseCallStepMenuItem );

		JMenuItem stepNewOracleStepMenuItem = new JMenuItem( stepAC.getNewOracleStepAction() );
		stepNewOracleStepMenuItem.setName( "stepNewOracleStepMenuItem" );
		stepNewOracleStepMenuItem.setText( Messages.alt( "_ORACLE_STEP_NEW", "New Oracle Step..." ) );
		stepNewPopupMenu.add( stepNewOracleStepMenuItem );

		stepNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				stepNewPopupMenu.show( stepNewButton, e.getX(), e.getY() );
			}
		});


		// This button will only work for for activation flows
		stepNewActionStepButton = new JButton( Messages.alt( "_NEW", "New..." ) );
		stepNewActionStepButton.setName( "stepNewActionStepButton" );
		stepNewActionStepButton.setEnabled( false );
		stepNewActionStepButton.setVisible( false );
		stepTopPanel.add(stepNewActionStepButton, "2, 1");

		stepCloneButton = new JButton( "C" );
		stepCloneButton.setName( "stepCloneButton" );
		stepTopPanel.add(stepCloneButton, "4, 1");

		stepEditButton = new JButton( "E" );
		stepEditButton.setName( "stepEditButton" );
		stepTopPanel.add(stepEditButton, "6, 1");

		stepRemoveButton = new JButton( "R" );
		stepRemoveButton.setName( "stepRemoveButton" );
		stepTopPanel.add(stepRemoveButton, "8, 1");

		stepUpButton = new JButton("U");
		stepUpButton.setName( "stepUpButton" );
		stepTopPanel.add(stepUpButton, "10, 1");

		stepDownButton = new JButton("D");
		stepDownButton.setName( "stepDownButton" );
		stepTopPanel.add(stepDownButton, "12, 1");

		JPanel stepBottomPanel = new JPanel();
		stepsPanel.add(stepBottomPanel, BorderLayout.SOUTH);
		stepBottomPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,},
			new RowSpec[] {}));


		stepContentPanel = new CRUDTablePanel();
		stepContentPanel.setName( "stepContentPanel" );
		stepContentPanel.setTableModel( stepTM );
		stepContentPanel.addListSelectionListener( stepAC );

		JTableUtil.adjustMaxColumnWidths(
				stepContentPanel.getTable(), new int[] { 40 } );

		// stepContentPanel.setNewAction( stepAC.newDocStepAction() );
		stepContentPanel.setCloneAction( stepAC.getCloneAction() );
		stepContentPanel.setEditAction( stepAC.getEditAction() );
		stepContentPanel.setRemoveAction( stepAC.getRemoveAction() );
		stepContentPanel.setUpAction( stepAC.getUpAction() );
		stepContentPanel.setDownAction( stepAC.getDownAction() );

		stepsPanel.add(stepContentPanel, BorderLayout.CENTER);

		//
		// POSTCONDITIONS
		//

		JLabel lblPostconditions = new JLabel(Messages.getString("FlowPanel.lblPostconditions.text")); //$NON-NLS-1$
		add(lblPostconditions, "2, 16, right, top");

		JPanel postconditionPanel = new JPanel();
		postconditionPanel.setName("postconditionPanel");
		add(postconditionPanel, "4, 16, 7, 1, fill, fill");
		postconditionPanel.setLayout(new BorderLayout(0, 0));

		JPanel postconditionTopPanel = new JPanel();
		postconditionPanel.add(postconditionTopPanel, BorderLayout.NORTH);
		postconditionTopPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				RowSpec.decode("25px"),
				FormFactory.RELATED_GAP_ROWSPEC,}));

		postconditionNewButton = new JButton( "N" );
		postconditionNewButton.setName( "postconditionNewButton" );
		postconditionTopPanel.add(postconditionNewButton, "1, 1, left, top");

		postconditionEditButton = new JButton("E");
		postconditionEditButton.setName( "postconditionEditButton" );
		postconditionTopPanel.add(postconditionEditButton, "3, 1");

		postconditionRemoveButton = new JButton("R");
		postconditionRemoveButton.setName( "postconditionRemoveButton" );
		postconditionTopPanel.add(postconditionRemoveButton, "5, 1");

		postconditionTM = createPostconditionTableModel();
		postconditionAC = new PostconditionActionContainer( postconditionTM, software );
		postconditionAC.setFlow( flow ); // Important

		postconditionContentPanel = new CRUDTablePanel();
		postconditionContentPanel.setName( "postconditionContentPanel" );
		postconditionContentPanel.setTableModel( postconditionTM );
		postconditionContentPanel.addListSelectionListener( postconditionAC );

		JTableUtil.adjustMaxColumnWidths(
				postconditionContentPanel.getTable(), new int[] { 40 } );

		postconditionContentPanel.setNewAction( postconditionAC.getNewAction() );
		postconditionContentPanel.setEditAction( postconditionAC.getEditAction() );
		postconditionContentPanel.setRemoveAction( postconditionAC.getRemoveAction() );

		postconditionPanel.add(postconditionContentPanel, BorderLayout.CENTER);

		drawFlow( this.flow );
	}

	private void onChangeImportanceInfluencer(final ItemEvent e) {
		if ( configuring
				|| e.getStateChange() == ItemEvent.DESELECTED
				|| priority.getSelectedIndex() < 0
				|| complexity.getSelectedIndex() < 0
				|| frequency.getSelectedIndex() < 0 ) {
			return;
		}

		updateImportanceSuggestionFor(
			Priority.values()[ priority.getSelectedIndex() ],
			Complexity.values()[ complexity.getSelectedIndex() ],
			Frequency.values()[ frequency.getSelectedIndex() ]
			);
	}

	private void updateImportanceSuggestionFor(
			final Priority p,
			final Complexity c,
			final Frequency f
			) {
		Importance value = ImportanceCalculator.calculate( p, c, f );
		updateImportanceSuggestionWith( value );
	}

	private void updateImportanceSuggestionWith(final Importance i) {
		String value = EnumTranslation.translationForItem(
				Importance.class, i );

		lblImportanceSuggestion.setText( value );
	}

	public static boolean accepts(Object o) {
		return ( o instanceof Flow );
	}

	public Flow getFlow() {

		if ( priority.getSelectedIndex() >= 0 ) {
			flow.setPriority( Priority.values()[ priority.getSelectedIndex() ] );
		}
		if ( frequency.getSelectedIndex() >= 0 ) {
			flow.setFrequency( Frequency.values()[ frequency.getSelectedIndex() ] );
		}
		if ( complexity.getSelectedIndex() >= 0 ) {
			flow.setComplexity( Complexity.values()[ complexity.getSelectedIndex() ] );
		}

		if ( importance.getSelectedIndex() >= 0 ) {
			flow.setImportance( Importance.values()[ importance.getSelectedIndex() ] );
		}

		if ( flow instanceof AlternateFlow ) {

			AlternateFlow af = (AlternateFlow) flow;
			af.setDescription( description.getText() );

			Flow sFlow = null;
			if ( starterFlow.getSelectedIndex() >= 0 ) {
				sFlow = useCase.flowAt( starterFlow.getSelectedIndex() );
			}
			af.setStarterFlow( sFlow );

			Step sStep = null;
			if ( sFlow != null && starterStep.getSelectedIndex() >= 0 ) {
				sStep = sFlow.stepAt( starterStep.getSelectedIndex() );
			}
			af.setStarterStep( sStep );

			if ( flow.type().equals( FlowType.RETURNABLE ) ) {
				ReturnableFlow rf = (ReturnableFlow) flow;

				Flow rFlow = null;
				if ( returningFlow.getSelectedIndex() >= 0 ) {
					rFlow = useCase.flowAt( returningFlow.getSelectedIndex() );
				}
				rf.setReturningFlow( rFlow );

				Step rStep = null;
				if ( rFlow != null && returningStep.getSelectedIndex() >= 0 ) {
					rStep = rFlow.stepAt( returningStep.getSelectedIndex() );
				}
				rf.setReturningStep( rStep );
			}
		}
		//System.out.println( "Panel flow has steps # " + flow.getSteps().size() );
		return flow;
	}

	public void setEditable(final boolean editable) {
		this.editable = editable;
		adjustUIState( this.flow );
	}


	private void configureStarterStep() {
		final int index = starterFlow.getSelectedIndex();
		if ( index < 0 ) {
			// Make the starter *STEP* empty
			starterStep.setModel( new DefaultComboBoxModel() );
			return;
		}
		final Flow flow = useCase.flowAt( index );
		if ( null == flow ) { return; }
		// Replace the items in the starter step
		starterStep.setModel( new DefaultComboBoxModel(
				buildStepItemsFromFlow( flow ).toArray( new String[ 0 ] ) ) );
	}

	private void configureReturningStep() {
		final int index = returningFlow.getSelectedIndex();
		if ( index < 0 ) {
			// Make the returning *STEP* empty
			returningStep.setModel( new DefaultComboBoxModel() );
			return;
		}
		final Flow flow = useCase.flowAt( index );
		if ( null == flow ) { return; }
		// Replace the items in the returning step
		returningStep.setModel( new DefaultComboBoxModel(
				buildStepItemsFromFlow( flow ).toArray( new String[ 0 ] ) ) );
	}

	private void drawFlow(final Flow flow) {
		this.configuring = true;
		adjustUIState( flow );
		drawValues( flow );
		this.configuring = false;
	}

	private void adjustUIState(final Flow obj) {

		final boolean isAlternateFlow = ( obj instanceof AlternateFlow );
		final boolean isReturnableFlow = ( obj instanceof ReturnableFlow );

		// Priority, Frequency, Complexity, Importance
		priority.setEnabled( editable );
		frequency.setEnabled( editable );
		complexity.setEnabled( editable );
		importance.setEnabled( editable );
		lblImportanceSuggestion.setVisible( editable );

		// Description
		lblDescription.setVisible( isAlternateFlow );
		description.setVisible( isAlternateFlow );
		description.setEnabled( isAlternateFlow && editable );

		// Starter flow
		lblStarterFlow.setVisible( isAlternateFlow );
		starterFlow.setVisible( isAlternateFlow );
		starterFlow.setEnabled( isAlternateFlow && editable );

		// Starter step
		lblStarterStep.setVisible( isAlternateFlow );
		starterStep.setVisible( isAlternateFlow );
		starterStep.setEnabled( isAlternateFlow && editable );

		// Returning flow
		lblReturningFlow.setVisible( isReturnableFlow );
		returningFlow.setVisible( isReturnableFlow );
		returningFlow.setEnabled( isReturnableFlow && editable );

		// Returning step
		lblReturningStep.setVisible( isReturnableFlow );
		returningStep.setVisible( isReturnableFlow );
		returningStep.setEnabled( isReturnableFlow && editable );

		// Steps
		linkStepActions();
		stepNewButton.setEnabled( editable ); // The stepNewActionStepButton has no action
		stepContentPanel.setEditable( editable );

		// Postconditions
		linkPostconditionActions();
		postconditionContentPanel.setEditable( editable );

	}

	private void drawValues(final Flow obj) {

		final boolean isAlternateFlow = ( obj instanceof AlternateFlow );
		final boolean isReturnableFlow = ( obj instanceof ReturnableFlow );

		// Flow type
		switch ( flow.type() ) {
			case BASIC		: flowType.setText( Messages.alt( "_FLOW_BASIC", "Basic" ) ); break;
			case TERMINATOR : flowType.setText( Messages.alt( "_FLOW_TERMINATOR", "Terminator" ) ); break;
			case RETURNABLE : flowType.setText( Messages.alt( "_FLOW_RETURNABLE", "Returnable" ) ); break;
			case CANCELATOR : flowType.setText( Messages.alt( "_FLOW_CANCELATOR", "Cancelator" ) ); break;
			default			: flowType.setText( Messages.alt( "_FLOW_STARTER", "Starter" ) ); break;
		}

		// Priority, Frequency, Complexity
		priority.setSelectedItem( EnumTranslation.translationForItem( Priority.class, obj.getPriority() ) );
		frequency.setSelectedItem( EnumTranslation.translationForItem( Frequency.class, obj.getFrequency() ) );
		complexity.setSelectedItem( EnumTranslation.translationForItem( Complexity.class, obj.getComplexity() ) );
		importance.setSelectedItem( EnumTranslation.translationForItem( Importance.class, obj.getImportance() ) );
		// Importance suggestion
		updateImportanceSuggestionFor( obj.getPriority(), obj.getComplexity(), obj.getFrequency() );

		if ( isAlternateFlow ) {
			AlternateFlow af = (AlternateFlow) obj;

			// Description
			description.setText( af.getDescription() );

			// Starter Flow
			final int starterFlowIndex = useCase.indexOfFlow( af.getStarterFlow() );
			logger.debug( "starterFlowIndex: " + starterFlowIndex );

			if ( starterFlowIndex < starterFlow.getItemCount() ) {
				starterFlow.setSelectedIndex( starterFlowIndex ); // -1 too !
			} else {
				logger.warn( "Invalid starter flow index: " + String.valueOf( starterFlowIndex ) );
			}

			// Starter Step
			configureStarterStep();
			if ( af.getStarterStep() != null ) {
				final int starterStepIndex = af.getStarterFlow().indexOfStep( af.getStarterStep() );
				if ( starterStepIndex < starterStep.getItemCount() ) {
					starterStep.setSelectedIndex( starterStepIndex ); // -1 too !
				} else {
					logger.warn( "Invalid starter step index: " + String.valueOf( starterStepIndex ) );
				}
			}
		}


		if ( isReturnableFlow ) {
			ReturnableFlow rf = (ReturnableFlow) obj;

			// Returning Flow
			final int returningFlowIndex = useCase.indexOfFlow( rf.getReturningFlow() );
			logger.debug( "returningFlowIndex: " + returningFlowIndex );

			if ( returningFlowIndex < returningFlow.getItemCount() ) {
				returningFlow.setSelectedIndex( returningFlowIndex ); // -1 too !
			} else {
				logger.debug( "Invalid returning flow index: " + String.valueOf( returningFlowIndex ) );
			}

			// Returning Step
			configureReturningStep();
			if ( rf.getReturningStep() != null ) {
				final int returningStepIndex = rf.getReturningFlow().indexOfStep( rf.getReturningStep() );
				if ( returningStepIndex < returningStep.getItemCount() ) {
					returningStep.setSelectedIndex( returningStepIndex ); // -1 too !
				} else {
					logger.debug( "Invalid returning step index: " + String.valueOf( returningStepIndex ) );
				}

			}

		}
	}


	private void linkPostconditionActions() {
		if ( postconditionAC != null ) {
			postconditionNewButton.setAction( postconditionAC.getNewAction() );
			postconditionEditButton.setAction( postconditionAC.getEditAction() );
			postconditionRemoveButton.setAction( postconditionAC.getRemoveAction() );
		} else {
			postconditionNewButton.setEnabled( false );
			postconditionEditButton.setEnabled( false );
			postconditionRemoveButton.setEnabled( false );
		}
	}

	private void linkStepActions() {
		if ( stepAC != null ) {
			stepCloneButton.setAction( stepAC.getCloneAction() );
			stepEditButton.setAction( stepAC.getEditAction() );
			stepRemoveButton.setAction( stepAC.getRemoveAction() );
			stepUpButton.setAction( stepAC.getUpAction() );
			stepDownButton.setAction( stepAC.getDownAction() );
		} else {
			stepNewButton.setEnabled( false );
			stepCloneButton.setEnabled( false );
			stepEditButton.setEnabled( false );
			stepRemoveButton.setEnabled( false );
			stepUpButton.setEnabled( false );
			stepDownButton.setEnabled( false );
		}
	}


	private BaseTableModel< Step > createStepTableModel() {

		final String columns[] = { "#", Messages.alt( "_STEP", "Step" ) };

		BaseTableModel< Step > tableModel =
			new BaseTableModel< Step >(
				columns.length,
				flow.getSteps(),
				columns
				) {
			private static final long serialVersionUID = 1146810835860824248L;
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch ( columnIndex ) {
					case 0	: return rowIndex + 1;
					default	: {
						final Step step = itemAt( rowIndex );
						if ( null == step ) { return null; }

						StringBuilder sb = new StringBuilder();

						// Trigger
						if ( step.trigger().equals( Trigger.SYSTEM ) ) {
							sb.append( Messages.alt( "_SYSTEM", "System" ) );
						} else {
							sb.append( Messages.alt( "_ACTOR", "Actor" ) );
						}

						sb.append( " " ).append( makeSentence( step ) );
						return sb.toString();
					}
				}
			}
		};

		tableModel.setColumnClasses( Integer.class, String.class );
		return tableModel;
	}


	private String makeSentence(final Step aStep) {
		return ( new StepSentenceMaker() ).makeSentence( aStep );
	}


	private BaseTableModel< Postcondition > createPostconditionTableModel() {
		final String columns[] = {
			"#",
			Messages.alt( "_POSTCONDITION_DESCRIPTION", "Description" )
		};

		BaseTableModel< Postcondition > tableModel =
			new BaseTableModel< Postcondition >(
				columns.length,
				flow.getPostconditions(),
				columns
				) {
			private static final long serialVersionUID = 1146810835860824248L;
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch ( columnIndex ) {
					case 0	: return rowIndex + 1;
					case 1  : return itemAt( rowIndex ).getDescription();
					default	: return "?";
				}
			}
		};

		tableModel.setColumnClasses( Integer.class, String.class );
		return tableModel;
	}

	private List< String > buildStepItemsFromFlow(final Flow flow) {
		List< String > items = new ArrayList< String >();
		int i = 0;
		for ( Step step : flow.getSteps() ) {
			items.add( ++i + " - " + step.asSentence() );
		}
		return items;
	}

}
