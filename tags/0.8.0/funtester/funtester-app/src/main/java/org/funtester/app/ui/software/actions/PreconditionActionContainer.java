package org.funtester.app.ui.software.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Action;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.software.PreconditionDialog;
import org.funtester.app.ui.software.PreconditionFromPostconditionDialog;
import org.funtester.app.ui.util.BaseAction;
import org.funtester.app.ui.util.BaseTableModel;
import org.funtester.app.ui.util.CRUDActionContainer;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.app.ui.util.UIUtil;
import org.funtester.core.software.ConditionState;
import org.funtester.core.software.ConditionStateKind;
import org.funtester.core.software.Flow;
import org.funtester.core.software.Postcondition;
import org.funtester.core.software.Precondition;
import org.funtester.core.software.Software;
import org.funtester.core.software.UseCase;

/**
 * Precondition action container
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class PreconditionActionContainer
	extends CRUDActionContainer<  ConditionState > {
	
	private Action newAction;
	private Action newFromPostconditionAction;
	private Action editAction;
	private Action removeAction;
	
	private final Software software;
	private UseCase currentUseCase = null;
	
	public PreconditionActionContainer(
			BaseTableModel< ConditionState > tableModel,
			final Software software
			) {
		super( tableModel );
		this.software = software;
	}
	
	public Software getSoftware() {
		return software;
	}
	
	public UseCase getCurrentUseCase() {
		return currentUseCase;
	}
	
	public void setCurrentUseCase(UseCase useCase) {
		currentUseCase = useCase;
	}
	
	public boolean currentUseCaseIsUndefined() {
		return null == currentUseCase;
	}

	public Action preconditionNewAction() {
		if ( null == newAction ) {
			ActionListener al = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showPrecondition( null );
				}
			};
			newAction = new BaseAction()
			.withName( Messages.getString( "_PRECONDITION_NEW" ) )
				.withListener( al );
		}
		return newAction;
	}	

	public Action newFromPostconditionAction() {
		if ( null == newFromPostconditionAction ) {
			newFromPostconditionAction = new BaseAction()
				.withName( Messages.getString( "_PRECONDITION_NEW_FROM_POSTCONDITION" ) )
				.withListener( createNewPreconditionFromPostcondition() );
		}
		return newFromPostconditionAction;
	}	

	public Action editAction() {
		if ( null == editAction ) {
			editAction = new BaseAction()
				.withName( Messages.alt( "_EDIT", "Edit..." ) )
				.withListener( new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ConditionState conditionState = getSelectedObject();
						if ( null == conditionState ) { return; }
						if ( conditionState.kind() == ConditionStateKind.PRECONDITION ) {
							showPrecondition( (Precondition) conditionState );
						} else {
							showPreconditionFromPostcondition( (Postcondition) conditionState );
						}
					}
				} );
		}
		return editAction;
	}

	public Action removeAction() {
		if ( null == removeAction ) {
			removeAction = new BaseAction()
			.withName( Messages.alt( "_REMOVE", "Remove..." ) )
				.withListener( createRemoveAction() )
				; 
		}
		return removeAction;
	}	

	//
	// PRIVATE
	//
	
	
	private ActionListener createRemoveAction() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( isSelectionEmpty() ) { return; }
				final String msg = Messages.alt( "_CONFIRM_REMOVE", "Remove ?" );
				final String title = Messages.alt( "_CONFIRMATION", "Confirmation" );
				if ( ! MsgUtil.yesTo( null, msg, title ) ) {
					return;
				}
				final int index = getSelectedIndex();
				getTableModel().remove( index );
			}
		};
	}

	
	private ActionListener createNewPreconditionFromPostcondition() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showPreconditionFromPostcondition( null );
			}
		};
	}	
	
	/**
	 * Show a given precondition.
	 * 
	 * @param p	the precondition to show or <code>null</code> to create a
	 * 			new precondition.
	 */
	private void showPrecondition(Precondition p) {
		PreconditionDialog dlg = new PreconditionDialog();
		UIUtil.centerOnScreen( dlg );
		if ( p != null ) {
			dlg.setPrecondition( p );
		}
		dlg.setVisible( true );
		if ( ! dlg.isConfirmed() ) {
			return;
		}
		if ( p != null ) {
			p.copy( dlg.getPrecondition() ); // Update the current object
			getTableModel().updated( getSelectedIndex() );
		} else {
			Precondition obj = dlg.getPrecondition().newCopy();
			obj.setId( software.generateIdFor( ConditionState.class.getSimpleName() ) );
			
			getTableModel().add( obj ); // Add it
		}
	}
	
	private void showPreconditionFromPostcondition(Postcondition p) {
		
		if ( currentUseCaseIsUndefined() ) {
			return;
		}
		
		Map< UseCase, Map< Flow, List< Postcondition > > > useCaseMap =
				new LinkedHashMap< UseCase, Map< Flow, List< Postcondition > > >();
		
		fillMapWithPostconditions(
				software.getUseCases(),
				currentUseCase,
				useCaseMap
				);
		
		if ( useCaseMap.isEmpty() ) {
			final String msg = Messages.alt( "_PRECONDITION_NO_POSTCONDITIONS",
					"There are no postconditions from other use cases to choose." );
			MsgUtil.info( null, msg, Messages.alt( "_NEW", "New" ) );
			return;
		}
		
		PreconditionFromPostconditionDialog dlg =
				new PreconditionFromPostconditionDialog( useCaseMap );
		UIUtil.centerOnScreen( dlg );
		
		if ( p != null ) {
			dlg.setPostcondition( p );
		}
		dlg.setVisible( true );
		
		if ( dlg.isConfirmed() ) {
			if ( null == p ) {
				Postcondition newPostcondition = dlg.getPostcondition().newCopy();
				
				//
				// Workaround for dealing with Jackson 2.3.1. Mixed objects
				// and ids, where the ids have forward references are not
				// handled correctly. So a new postcondition from another flow
				// will ALWAYS be COPIED, not referenced. :(
				//
				// Generate the id for the new postcondition :(
				newPostcondition.setId( software.generateIdFor( ConditionState.class.getSimpleName() ) );
				
				getTableModel().add( newPostcondition );
			} else {
				
				//
				// Workaround: save the current id, copy the new object
				// and restore the id. This is necessary for not having
				// to create a new object. It is better using the old id
				// with new data.
				//
				
				final long oldId = p.getId();
				p.copy( dlg.getPostcondition().newCopy() );
				p.setId( oldId ); 
				
				getTableModel().updated( getSelectedIndex() );
			}
		}
	}
	
	
	private void fillMapWithPostconditions(
			final List< UseCase > useCases,
			final UseCase currentUseCase,
			Map< UseCase, Map< Flow, List< Postcondition > > > useCaseMap
			) {
		for ( UseCase uc : useCases ) {
			
			// Ignore the current use case
			if ( uc.equals( currentUseCase ) ) { continue; } 
			
			for ( Flow f : uc.getFlows() ) {
				
				// Ignore flow without postconditions
				if ( f.numberOfPostconditions() < 1 ) { continue; }
				
				Map< Flow, List< Postcondition > > flowMap = useCaseMap.get( uc );
				if ( null == flowMap ) {
					flowMap = new LinkedHashMap< Flow, List< Postcondition > >();
					useCaseMap.put( uc, flowMap );
				}
				
				flowMap.put( f, f.getPostconditions() );
			}
		}
	}
}
