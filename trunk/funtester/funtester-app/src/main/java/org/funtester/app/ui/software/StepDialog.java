package org.funtester.app.ui.software;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.eclipse.wb.swing.FocusTraversalOnArray;
import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.DefaultEditingDialog;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.thirdparty.FocusUtil;
import org.funtester.app.ui.util.DocumentAutoCompleter;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.validation.ElementParser;
import org.funtester.common.util.ItemsParser;
import org.funtester.common.util.Validator;
import org.funtester.core.profile.Action;
import org.funtester.core.profile.ActionType;
import org.funtester.core.profile.StepKind;
import org.funtester.core.profile.Trigger;
import org.funtester.core.software.Element;
import org.funtester.core.software.NewStep;
import org.funtester.core.software.Step;
import org.funtester.core.software.UseCase;
import org.funtester.core.vocabulary.ActionNickname;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

@SuppressWarnings("rawtypes")
public class StepDialog extends DefaultEditingDialog< NewStep > {


	private static final long serialVersionUID = 8895167312918625328L;

	private final UseCase currentUseCase;
	private final Set< Element > newElements = new LinkedHashSet< Element >();

	private final List< UseCase > orderedUseCases;
	private final List< ActionNickname > orderedNicknames;

	private final JComboBox trigger;

	private final JCheckBox actionCheck;
	private final JComboBox actionNickname;

	private final JLabel lblContent;
	private final JLabel lblMax;
	private final JScrollPane contentScrollPane;
	private final JTextArea content;

	private final JLabel lblUseCase;
	private final JComboBox useCase;
	private final JLabel lblAvailable;
	private final JTextArea available;
	private final JScrollPane availableScrollPane;
	private JButton addAllEditables;
	private JButton addAll;
	private JLabel lblValue;
	private JTextArea txtrValue;
	private JScrollPane scrollPane;
	private JTextField sentence;



	@SuppressWarnings({ "unchecked" })
	public StepDialog(
			final UseCase currentUseCase,
			final Collection< UseCase > useCases,
			final Collection< ActionNickname > nicknames
			) {
		super();

		this.currentUseCase = currentUseCase;

		// Sort use cases
		this.orderedUseCases = new ArrayList< UseCase >();
		this.orderedUseCases.addAll( useCases );
		Collections.sort( this.orderedUseCases );

		// Sort nicknames
		this.orderedNicknames = new ArrayList< ActionNickname >();
		this.orderedNicknames.addAll( nicknames );
		Collections.sort( this.orderedNicknames );

		// Sort use case elements
		final List< String > eList = new ArrayList< String >();
		for ( Element e : currentUseCase.getElements() ) {
			eList.add( e.getName() );
		}
		Collections.sort( eList );


		setTitle("Step");
		setIconImage( ImageUtil.loadImage( ImagePath.stepIcon() ) );

		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.MIN_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.MIN_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.MIN_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.MIN_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));

		sentence = new JTextField();
		sentence.setName("sentence");
		sentence.setEditable(false);
		contentPanel.add(sentence, "2, 2, 15, 1, fill, default");
		sentence.setColumns(10);

		JLabel lblTrigger = new JLabel("Trigger:");
		contentPanel.add(lblTrigger, "2, 6");

		actionCheck = new JCheckBox("Action:");
		actionCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				boolean actionIsSelected = ItemEvent.SELECTED == e.getStateChange();

				ActionType actionType = Action.DEFAULT_ACTION_TYPE;
				ActionNickname nickname = null;
				if ( actionIsSelected ) {
					nickname = (ActionNickname) e.getItem();
					Action action = nickname.getAction();
					actionType = action.getType();
				}

				getObject().setActionNickname( nickname );
				updateSentence();

				decideWhatToEnable( actionIsSelected, actionType );
			}
		});
		actionCheck.setName("actionCheck");
		contentPanel.add(actionCheck, "4, 6");

		lblContent = new JLabel("Content:");
		contentPanel.add(lblContent, "6, 6");

		lblMax = new JLabel("(máx. 0)");
		lblMax.setForeground(Color.GRAY);
		contentPanel.add(lblMax, "8, 6");

		addAllEditables = new JButton("Add All Editables");
		addAllEditables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addElements( currentUseCase.getElements(), true );
			}
		});
		addAllEditables.setName("addAllEditables");
		contentPanel.add(addAllEditables, "10, 6");

		addAll = new JButton("Add All");
		addAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addElements( currentUseCase.getElements(), false );
			}
		});
		addAll.setName("addAll");
		contentPanel.add(addAll, "12, 6");

		lblValue = new JLabel("Value:");
		contentPanel.add(lblValue, "14, 6");

		lblUseCase = new JLabel("Use Case:");
		contentPanel.add(lblUseCase, "16, 6");

		final String triggerValues[] = {
				Messages.getString( "_ACTOR" ),
				Messages.getString( "_SYSTEM" )
		};

		trigger = new JComboBox( triggerValues );
		trigger.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if ( ItemEvent.DESELECTED == e.getStateChange() ) { return; }

				Trigger trigger =
						e.getItem().toString().equalsIgnoreCase( triggerValues[ 0 ] )
						? Trigger.ACTOR : Trigger.SYSTEM;

				// Put the value into the object
				getObject().setTrigger( trigger );
				updateSentence();

				filterActionsByTrigger( trigger );
			}
		});
		contentPanel.add(trigger, "2, 8, fill, default");

		actionNickname = new JComboBox(
				new DefaultComboBoxModel( orderedUseCases.toArray( new UseCase[ 0 ] ) )
				);
		actionNickname.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				boolean selected = ItemEvent.SELECTED == e.getStateChange();
				if ( ! selected ) { return; }

				ActionNickname nickname = (ActionNickname) actionNickname.getSelectedItem();

				getObject().setActionNickname( nickname );
				updateSentence();

				if ( null == nickname || null == nickname.getAction() ) { return; }
				Action action = nickname.getAction();

				decideWhatToEnable( true, action.getType() );
				updateMaxElements( action.getMaxElements() );
			}
		});
		actionNickname.setName("actionNickname");
		contentPanel.add(actionNickname, "4, 8, fill, default");

		scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, "14, 8, 1, 3, fill, fill");

		txtrValue = new JTextArea();
		scrollPane.setViewportView(txtrValue);
		txtrValue.setText("value");

		useCase = new JComboBox(
				new DefaultComboBoxModel( orderedUseCases.toArray( new UseCase[ 0 ] ) )
				);
		useCase.setName("useCase");
		contentPanel.add(useCase, "16, 8, fill, default");

		contentScrollPane = new JScrollPane();
		contentPanel.add(contentScrollPane, "6, 8, 7, 3, fill, fill");

		content = new JTextArea();
		content.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				
				ElementParser.parseElementsTo(
						content.getText(),
						currentUseCase, 
						getObject().getElements(),
						newElements
						);
				
				
				updateSentence();
			}
		});
		content.setName("content");
		contentScrollPane.setViewportView(content);
		FocusUtil.patch( content );
		// Autocomplete
		DocumentAutoCompleter dac = new DocumentAutoCompleter( content, eList );
		dac.setMinChars( 1 );

		lblAvailable = new JLabel("Available:");
		contentPanel.add(lblAvailable, "4, 12, right, default");

		availableScrollPane = new JScrollPane();
		availableScrollPane.setName("availableScrollPane");
		contentPanel.add(availableScrollPane, "6, 12, 7, 3, fill, fill");

		available = new JTextArea();
		available.setBackground(SystemColor.control);
		available.setEditable(false);
		availableScrollPane.setViewportView(available);
		available.setName("available");
		contentPanel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{lblTrigger, trigger, actionCheck, actionNickname, lblContent, contentScrollPane, lblMax, content, lblUseCase, useCase}));
	}

	//__________________________________________________________________________

	protected void updateSentence() {
		sentence.setText( ( new StepSentenceMaker() ).makeSentence( getObject() ) );
	}

	//__________________________________________________________________________

	private void decideWhatToEnable(
			final boolean actionIsSelected,
			final ActionType actionType
			) {
		boolean hasWidget = false;
		boolean hasValue = false;
		boolean hasUseCase = false;

		if ( actionIsSelected ) {
			hasWidget = ActionType.WIDGET == actionType;
			hasValue = ActionType.VALUE == actionType;
			hasUseCase = ActionType.USE_CASE_CALL == actionType;
		}

		// Action
		actionNickname.setEnabled( actionIsSelected );

		// Content
		lblContent.setVisible( hasWidget || hasValue );
		lblMax.setVisible( hasWidget || hasValue );
		content.setEnabled( hasWidget || hasValue );
		content.setVisible( hasWidget || hasValue );
		lblAvailable.setVisible( hasWidget );
		available.setVisible( hasWidget );

		// Use case
		lblUseCase.setVisible( hasUseCase );
		useCase.setEnabled( hasUseCase );
		useCase.setVisible( hasUseCase );
	}

	//__________________________________________________________________________

	protected boolean selectedActionCanCallUseCase() {
		ActionNickname nickname = (ActionNickname) actionNickname.getSelectedItem();
		if ( null == nickname ) { return false; }

		Action action = nickname.getAction();
		if ( null == action ) { return false; }

		return ActionType.USE_CASE_CALL == action.getType();
	}

	//__________________________________________________________________________

	private void addElements(
			final Collection< Element > useCaseElements,
			final boolean justTheEditable
			) {
		// Use a list to keep the user order
		List< String > items = new ArrayList< String >();

		// Add the user typed items
		ItemsParser.addFromParse( content.getText(), NewStep.SEPARATOR, items );

		// Add all other editable items
		for ( Element ie : useCaseElements ) {
			if ( justTheEditable && ! ie.isEditable() ) {
				continue;
			}
			final String content = ie.getName();
			if ( items.contains( content ) ) {
				continue;
			}
			items.add( content );
		}

		// Write back as text
		content.setText( ItemsParser.textFromItems( NewStep.SEPARATOR + " ", items ) );
	}

	//__________________________________________________________________________

	private void updateMaxElements(final int max) {
		lblMax.setText( String.format(
				Messages.alt( "StepDialog.lblMax.text", "(max. %d)" ), max ) );
	}

	//__________________________________________________________________________

	private void filterActionsByTrigger(final Trigger trigger) {
		actionNickname.setModel(
				new DefaultComboBoxModel( actionNicknamesWithTrigger( trigger ) ));
		actionNickname.setSelectedIndex( -1 );
		updateMaxElements( 0 );
	}

	//__________________________________________________________________________

	private ActionNickname[] actionNicknamesWithTrigger(final Trigger trigger) {
		List< ActionNickname > l = new ArrayList< ActionNickname >();
		for ( ActionNickname an : orderedNicknames ) {
			Action a = an.getAction();
			if ( a != null && a.canBeTriggeredBy( trigger ) ) {
				l.add( an );
			}
		}
		return l.toArray( new ActionNickname[ 0 ] );
	}

	//__________________________________________________________________________

	@Override
	protected NewStep createObject() {
		return new NewStep();
	}

	@Override
	protected boolean populateObject() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void drawObject(NewStep obj) {
		trigger.setSelectedIndex( obj.trigger().ordinal() );
		actionNickname.setSelectedItem( obj.getActionNickname() );
		//content.setText(  );
	}

	@Override
	protected Validator< NewStep > createValidator() {
		// TODO Auto-generated method stub
		return null;
	}

}
