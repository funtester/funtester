package org.funtester.app.ui.software.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Action;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.software.ElementDialog;
import org.funtester.app.ui.util.BaseAction;
import org.funtester.app.ui.util.BaseTableModel;
import org.funtester.app.ui.util.CRUDActionContainer;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.app.ui.util.UIUtil;
import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.profile.ElementType;
import org.funtester.core.software.Element;
import org.funtester.core.software.ElementUseAnalyzer;
import org.funtester.core.software.Software;
import org.funtester.core.software.UseCase;

/**
 * Action container for an {@link Element}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ElementActionContainer
		extends CRUDActionContainer< Element > {
	
	private Action newAction;
	private Action editAction;
	private Action removeAction;
	
	private final Software software;
	private final UseCase useCase;
	private final List< ElementType > elementTypes;
	private final DriverCache driverCache;
	
	public ElementActionContainer(
			BaseTableModel< Element > tableModel,
			UseCase useCase,
			final Software software,
			final DriverCache driverCache
			) {
		super( tableModel );
		this.useCase = useCase;
		this.software = software;
		this.driverCache = driverCache;
		
		// Create an sorted list from the profile types
		this.elementTypes = new ArrayList< ElementType >();
		this.elementTypes.addAll( software.getVocabulary().getProfile().getTypes() ); // TODO refactor (chain)
		Collections.sort( this.elementTypes );
	}

	public Action getNewAction() {
		if ( null == newAction ) {
			newAction = new BaseAction()
				.withName( Messages.alt( "_NEW", "New..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.elementNewIcon() ) )
				.withListener( new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						showElement( false );
					}
				} );
		}
		return newAction;
	}		

	public Action getEditAction() {
		if ( null == editAction ) {
			editAction = new BaseAction()
				.withName( Messages.alt( "_EDIT", "Edit..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.elementEditIcon() ) )
				.withListener( new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						showElement( true );
					}
				} );
		}
		return editAction;
	}
	
	public Action getRemoveAction() {
		if ( null == removeAction ) {
			removeAction = new BaseAction()
				.withName( Messages.alt( "_REMOVE", "Remove..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.elementRemoveIcon() ) )
				.withListener( new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						removeElement();
					}
				} );
		}
		return removeAction;
	}	

	//
	// PRIVATE
	//
	
	/**
	 * Show an element.
	 * 
	 * @param edit	{@code true} to edit the element, {@code false} to create.
	 */
	private void showElement(final boolean edit) {
		
		Element obj;
		if ( edit ) {
			obj = getSelectedObject();
			if ( null == obj ) {
				showNoSelectionDetectedMessage( Messages.getString( "_EDIT" ) );
				return;
			}
		} else {
			obj = new Element();
			obj.setUseCase( useCase );
		}
		
		// Show the dialog
		ElementDialog dlg = new ElementDialog( software, elementTypes, useCase.getElements(), driverCache );
		UIUtil.centerOnScreen( dlg );
		dlg.copyObject( obj );
		dlg.showObject();
		if ( ! dlg.isConfirmed() ) { return; }
		
		obj.copy( dlg.getObject() );
		
		if ( edit ) {
			getTableModel().updated( getSelectedIndex() ); // Update
		} else { // New
			obj.setId( software.generateIdFor( Element.class.getSimpleName() ) );
			getTableModel().add( obj ); // Add it
		}
	}
	
	/**
	 * Remove the selected element.
	 */
	private void removeElement() {
		if ( isSelectionEmpty() ) {
			showNoSelectionDetectedMessage( Messages.getString( "_REMOVE" ) );
			return;
		}
		
		// Check for the element use
		ElementUseAnalyzer analyzer = new ElementUseAnalyzer();
		final int count = analyzer.flowsUsingElement(
			getSelectedObject(), useCase.getFlows() ).size();
		if ( count > 0 ) {
			final String msg = String.format( Messages.getString( "_ELEMENT_CANNOT_REMOVE" ), count );
			final String title = Messages.alt( "_REMOVE", "Remove" );
			MsgUtil.info( null, msg, title );
			return;
		}
		
		// Confirm the action
		final String msg = Messages.alt( "_CONFIRM_REMOVE", "Remove ?" );
		final String title = Messages.alt( "_CONFIRMATION", "Confirmation" );
		if ( ! MsgUtil.yesTo( null, msg, title ) ) {
			return;
		}
		
		// Remove
		final int index = getSelectedIndex();
		getTableModel().remove( index );
	}
	
	private void showNoSelectionDetectedMessage(final String title) {
		String msg = Messages.alt( "_ELEMENT_NO_SELECTION_DETECTED", "No selection detected." );
		MsgUtil.error( null, msg, title );
	}

}
