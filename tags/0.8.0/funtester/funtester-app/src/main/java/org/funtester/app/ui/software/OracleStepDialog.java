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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.DefaultEditingDialog;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.thirdparty.FocusUtil;
import org.funtester.app.ui.util.DocumentAutoCompleter;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.validation.ElementParser;
import org.funtester.app.validation.OracleStepValidator;
import org.funtester.common.util.ItemsParser;
import org.funtester.common.util.Validator;
import org.funtester.core.profile.ElementType;
import org.funtester.core.profile.StepKind;
import org.funtester.core.software.Element;
import org.funtester.core.software.OracleMessageOccurrence;
import org.funtester.core.software.OracleStep;
import org.funtester.core.software.UseCase;
import org.funtester.core.vocabulary.ActionNickname;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Dialog for editing an {@link OracleStep}.
 *
 * @author Thiago Delgado Pinto
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class OracleStepDialog extends DefaultEditingDialog< OracleStep > {

	private static final long serialVersionUID = 7549965606669889494L;
	private static final String SEPARATOR = ",";

	private final Logger logger = LoggerFactory.getLogger( ActionStepDialog.class );

	private final UseCase useCase;
	private final Set< Element > newElements = new LinkedHashSet< Element >();

	private final JComboBox messageOccurrence;
	private final JComboBox elementType;
	private final JLabel lblActionNickname;
	private final JComboBox actionNickname;
	private final JLabel lblElements;
	private final JTextArea elements;
	private final JButton addAllEditableButton;
	private final JScrollPane elementsScrollPane;
	private final JButton addAllButton;
	private final JTextArea availableElements;
	private final JLabel lblAvailableElements;
	private final JScrollPane availableElementsScrollPane;
	private final JLabel maxElements;


	public OracleStepDialog(
			final UseCase useCase,
			final Collection< ActionNickname > nicknames
			) {
		this.useCase = useCase;

		setTitle(Messages.getString("OracleStepDialog.this.title")); //$NON-NLS-1$
		setIconImage( ImageUtil.loadImage( ImagePath.stepIcon() ) );
		setBounds( 100, 100, 739, 305 );
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
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
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));

		lblActionNickname = new JLabel(Messages.getString("OracleStepDialog.lblActionNickname.text")); //$NON-NLS-1$
		contentPanel.add(lblActionNickname, "2, 2, right, default");

		actionNickname = new JComboBox( new DefaultComboBoxModel(
				nicknamesForOracleStep( nicknames ) ) );
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
		if ( actionNickname.getItemCount() == 1 ) {
			actionNickname.setSelectedIndex( 0 );
		}
		contentPanel.add(actionNickname, "4, 2, 3, 1, fill, default");

		addAllEditableButton = new JButton(Messages.getString("OracleStepDialog.addAllEditableButton.text")); //$NON-NLS-1$
		addAllEditableButton.setName( "addAllEditableButton" );
		addAllEditableButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addElements( useCase.getElements(), true );
			}
		});
		contentPanel.add(addAllEditableButton, "4, 4, default, top");

		addAllButton = new JButton(Messages.getString("OracleStepDialog.addAllButton.text")); //$NON-NLS-1$
		addAllButton.setName("addAllButton");
		addAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addElements( useCase.getElements(), false );
			}
		});
		contentPanel.add(addAllButton, "6, 4");

		lblElements = new JLabel(Messages.getString("OracleStepDialog.lblElements.text")); //$NON-NLS-1$
		contentPanel.add(lblElements, "2, 6, right, top");

		elementsScrollPane = new JScrollPane();
		contentPanel.add(elementsScrollPane, "4, 6, 3, 3, fill, fill");

		elements = new JTextArea();
		elements.setLineWrap(true);
		elementsScrollPane.setViewportView(elements);
		elements.setName("elements");

		FocusUtil.patch( elements ); // Make the TAB to work properly

		final List< String > eList = new ArrayList< String >();
		for ( Element e : useCase.getElements() ) {
			eList.add( e.getName() );
		}
		Collections.sort( eList );

		DocumentAutoCompleter dac = new DocumentAutoCompleter( elements, eList );
		dac.setMinChars( 1 );

		JLabel lblMessageOccurrence = new JLabel(Messages.getString("OracleStepDialog.lblMessageOccurrence.text")); //$NON-NLS-1$
		lblMessageOccurrence.setVisible(false);

		maxElements = new JLabel( "" ); //$NON-NLS-1$
		maxElements.setForeground(Color.GRAY);
		contentPanel.add(maxElements, "2, 8, right, top");
		updateMaxElements( 1 );

		lblAvailableElements = new JLabel(Messages.getString("OracleStepDialog.lblAvailableElements.text")); //$NON-NLS-1$
		contentPanel.add(lblAvailableElements, "2, 10, right, top");

		availableElementsScrollPane = new JScrollPane();
		contentPanel.add(availableElementsScrollPane, "4, 10, 3, 1, fill, fill");

		availableElements = new JTextArea();
		availableElements.setBackground(SystemColor.control);
		availableElementsScrollPane.setViewportView(availableElements);
		availableElements.setName( "availableElements" );
		availableElements.setLineWrap(true);
		contentPanel.add(lblMessageOccurrence, "2, 12, right, default");

		messageOccurrence = new JComboBox();
		messageOccurrence.setVisible(false);
		messageOccurrence.setName( "messageOccurrence" );
		contentPanel.add(messageOccurrence, "4, 12, 3, 1, fill, default");

		JLabel lblElementType = new JLabel(Messages.getString("OracleStepDialog.lblElementType.text")); //$NON-NLS-1$
		lblElementType.setVisible(false);
		contentPanel.add(lblElementType, "2, 14, right, default");

		elementType = new JComboBox();
		elementType.setVisible(false);
		elementType.setName( "elementType" );
		contentPanel.add(elementType, "4, 14, 3, 1, fill, default");
	}

	public Collection< Element > getNewElements() {
		return newElements;
	}

	private void updateMaxElements(final int max) {
		maxElements.setText( String.format(
				Messages.alt( "OracleStepDialog.maxElements.text", "(max. %d)" ),
				max ) );
	}

	private ActionNickname[] nicknamesForOracleStep(Collection< ActionNickname > nicknames) {
		List< ActionNickname > list = new ArrayList< ActionNickname >();
		for ( ActionNickname n : nicknames ) {
			if ( n.actionKind().equals( StepKind.ORACLE ) ) {
				list.add( n );
			}
		}
		Collections.sort( list );
		return list.toArray( new ActionNickname[ 0 ] );
	}

	private void addElements(
			final Collection< Element > useCaseElements,
			final boolean justTheEditable
			) {
		// Use a list to keep the user order
		List< String > items = new ArrayList< String >();

		// Add the user typed items
		ItemsParser.addFromParse( elements.getText(), SEPARATOR, items );

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
		elements.setText( ItemsParser.textFromItems( SEPARATOR + " ", items ) );
	}

	@Override
	protected OracleStep createObject() {
		return new OracleStep();
	}

	@Override
	protected boolean populateObject() {
		OracleStep obj = getObject();
		obj.setActionNickname( (ActionNickname) actionNickname.getSelectedItem() );
		ElementParser.parseElementsTo( elements.getText(), useCase, obj.getElements(), newElements );
		obj.setMessageOccurrence( (OracleMessageOccurrence) messageOccurrence.getSelectedItem() );
		obj.setElementType( (ElementType) elementType.getSelectedItem() );
		return true;
	}

	@Override
	protected void drawObject(final OracleStep obj) {
		ActionNickname aNickname = obj.getActionNickname();
		actionNickname.setSelectedItem( aNickname );
		// Update max elements
		updateMaxElements( aNickname != null ? aNickname.maxElements() : 0 );

		elements.setText( obj.elementsAsText() );
		messageOccurrence.setSelectedItem( obj.getMessageOccurrence() );
		elementType.setSelectedItem( obj.getElementType() );

		final String availableElementsAsText = ItemsParser.textFromItems(
				SEPARATOR + " ", useCase.getElements() );
		availableElements.setText( availableElementsAsText );
	}

	@Override
	protected Validator< OracleStep > createValidator() {
		return new OracleStepValidator();
	}
}
