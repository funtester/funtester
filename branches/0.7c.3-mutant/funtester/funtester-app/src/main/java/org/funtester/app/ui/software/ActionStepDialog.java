package org.funtester.app.ui.software;

import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.DefaultEditingDialog;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.thirdparty.FocusUtil;
import org.funtester.app.ui.util.DocumentAutoCompleter;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.validation.ActionStepValidator;
import org.funtester.app.validation.ElementParser;
import org.funtester.common.util.ItemsParser;
import org.funtester.common.util.Validator;
import org.funtester.core.profile.StepKind;
import org.funtester.core.profile.Trigger;
import org.funtester.core.software.ActionStep;
import org.funtester.core.software.Element;
import org.funtester.core.software.UseCase;
import org.funtester.core.vocabulary.ActionNickname;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Dialog for editing an {@link ActionStep}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ActionStepDialog extends DefaultEditingDialog< ActionStep > {
	
	private static final long serialVersionUID = 9182896976694980967L;
	private static final String SEPARATOR = ",";
	
	private final Logger logger = LoggerFactory.getLogger( ActionStepDialog.class );
	
	private final UseCase useCase;
	private final List< ActionNickname > nicknames;
	private final Set< Element > newElements = new LinkedHashSet< Element >();
	
	private final JRadioButton system;
	private final JRadioButton actor;
	private final JLabel lblActionNickname;
	private final JComboBox actionNickname;
	private final JLabel lblElements;
	private final JTextArea elements;
	private final JScrollPane elementsScrollPane;
	private final JTextArea availableElements;
	private final JLabel lblAvailableElements;
	private final JScrollPane availableElementsScrollPane;
	private final JLabel maxElements;
	
	
	public ActionStepDialog(
			final UseCase useCase,
			final Collection< ActionNickname > nicknames
			) {
		this.useCase = useCase;
		this.nicknames = new ArrayList< ActionNickname >();
		this.nicknames.addAll( nicknames );
		// Guarantee the filtered nicknames will also be ordered ;)
		Collections.sort( this.nicknames );
		
		setTitle(Messages.getString("ActionStepDialog.this.title")); //$NON-NLS-1$
		setIconImage( ImageUtil.loadImage( ImagePath.stepIcon() ) );
		setBounds( 100, 100, 579, 350 );
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.MIN_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblTrigger = new JLabel( Messages.getString("ActionStepDialog.lblTrigger.text") ); //$NON-NLS-1$
		contentPanel.add(lblTrigger, "2, 2, right, default");
		
		final ActionListener setFocusAL = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filterActions( actor.isSelected() ? Trigger.ACTOR : Trigger.SYSTEM );
				setFocusOnAction();
			}
		}; 		
		
		actor = new JRadioButton( Messages.getString( "_ACTOR" ) );
		actor.setName("actor");
		actor.addActionListener( setFocusAL );
		contentPanel.add(actor, "4, 2");
		
		system = new JRadioButton( Messages.getString( "_SYSTEM" ) );
		system.setName("system");
		system.addActionListener( setFocusAL );
		contentPanel.add(system, "6, 2");
		
		final ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add( actor );
		buttonGroup.add( system );
		
		lblActionNickname = new JLabel(Messages.getString("ActionStepDialog.lblActionNickname.text")); //$NON-NLS-1$
		contentPanel.add(lblActionNickname, "2, 4, right, default");
		
		actionNickname = new JComboBox( nicknames.toArray( new ActionNickname[ 0 ] ) );
		actionNickname.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if ( null == maxElements
					|| e.getStateChange() == ItemEvent.DESELECTED ) {
					return;
				}
				ActionNickname obj = (ActionNickname) e.getItem();
				if ( obj != null ) {
					updateMaxElements( obj.maxElements() );
				} else {
					logger.warn( "Couldn't get the selected actionNickname." );
				}
			}
		});
		actionNickname.setName("actionNickname");
		contentPanel.add(actionNickname, "4, 4, 3, 1, fill, default");
		
		lblElements = new JLabel(Messages.getString("ActionStepDialog.lblElements.text")); //$NON-NLS-1$
		contentPanel.add(lblElements, "2, 6, right, top");
		
		elementsScrollPane = new JScrollPane();
		contentPanel.add(elementsScrollPane, "4, 6, 3, 3, fill, fill");
		
		elements = new JTextArea();
		elements.setName( "elements" );
		elements.setLineWrap( true );
		elementsScrollPane.setViewportView( elements );
		
		final List< String > wordList = new ArrayList< String >();
		for ( Element ie : useCase.getElements() ) {
			wordList.add( ie.getName() );
		}
		Collections.sort( wordList );
		
		DocumentAutoCompleter dac = new DocumentAutoCompleter( elements, wordList );
		dac.setMinChars( 1 );

		FocusUtil.patch( elements ); // Make the TAB key to work properly with the TextArea
		
		maxElements = new JLabel( "" );
		updateMaxElements( 1 );
		maxElements.setForeground(Color.GRAY);
		contentPanel.add(maxElements, "2, 8, right, top");
		
		lblAvailableElements = new JLabel( Messages.getString("ActionStepDialog.lblAvailableElements.text") ); //$NON-NLS-1$
		contentPanel.add(lblAvailableElements, "2, 10, default, top");
		
		availableElementsScrollPane = new JScrollPane();
		contentPanel.add(availableElementsScrollPane, "4, 10, 3, 1, fill, fill");
		
		availableElements = new JTextArea();
		availableElementsScrollPane.setViewportView(availableElements);
		availableElements.setEditable(false);
		availableElements.setLineWrap(true);
		availableElements.setName( "availableElements" );
		availableElements.setBackground(SystemColor.control);
	}
	
	private void updateMaxElements(final int max) {
		maxElements.setText( String.format(
				Messages.alt( "ActionStepDialog.maxElements.text", "(max. %d)" ),
				max ) );
	}

	private void filterActions(final Trigger trigger) {
		actionNickname.setModel( new DefaultComboBoxModel(
			actionNicknamesWithTrigger( trigger )
			));
		actionNickname.setSelectedIndex( -1 );
		updateMaxElements( 0 );
	}
	
	private ActionNickname[] actionNicknamesWithTrigger(final Trigger trigger) {
		List< ActionNickname > l = new ArrayList< ActionNickname >();
		for ( ActionNickname a : nicknames ) {
			if ( a.actionTrigger().equals( trigger )
				&& a.actionKind().equals( StepKind.ACTION ) ) {
				l.add( a );
			}
		}
		return l.toArray( new ActionNickname[ 0 ] );
	}
	
	public Set< Element > getNewElements() {
		return newElements;
	}

	private void setFocusOnAction() {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				actionNickname.requestFocusInWindow();
			}
		} );
	}

	@Override
	protected ActionStep createObject() {
		return new ActionStep();
	}

	@Override
	protected boolean populateObject() {
		ActionStep obj = getObject();
		obj.setTrigger( system.isSelected() ? Trigger.SYSTEM : Trigger.ACTOR );
		obj.setActionNickname( (ActionNickname) actionNickname.getSelectedItem() );
		ElementParser.parseElementsTo( elements.getText(), useCase, obj.getElements(), newElements );
		return true;
	}

	@Override
	protected void drawObject(final ActionStep obj) {
		if ( obj.getTrigger().equals( Trigger.SYSTEM ) )
			system.setSelected( true );
		else
			actor.setSelected( true );
		
		filterActions( obj.getTrigger() );
		actionNickname.setSelectedItem( obj.getActionNickname() );
		
		final String elementAsText = ItemsParser.textFromItems(
				SEPARATOR + " ", obj.getElements() );
		elements.setText( elementAsText );
		
		updateMaxElements( obj.getActionNickname() != null ? obj.getActionNickname().maxElements() : 1 );
		
		final String availableElementsAsText = ItemsParser.textFromItems(
				SEPARATOR + " ", useCase.getElements() );
		availableElements.setText( availableElementsAsText );
	}

	@Override
	protected Validator< ActionStep > createValidator() {
		return new ActionStepValidator();
	}

}