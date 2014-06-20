package org.funtester.app.ui.software.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.software.PostconditionDialog;
import org.funtester.app.ui.util.BaseAction;
import org.funtester.app.ui.util.BaseTableModel;
import org.funtester.app.ui.util.CRUDActionContainer;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.app.ui.util.UIUtil;
import org.funtester.core.software.ConditionState;
import org.funtester.core.software.Flow;
import org.funtester.core.software.Postcondition;
import org.funtester.core.software.Software;

/**
 * Postcondition action container
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class PostconditionActionContainer
		extends CRUDActionContainer< Postcondition > {
	
	private Action newAction;
	private Action editAction;
	private Action removeAction;
	
	private final Software software;
	private Flow flow;
	
	public PostconditionActionContainer(
			BaseTableModel< Postcondition > tableModel,
			final Software software
			) {
		super( tableModel );
		this.software = software;
	}
	public Flow getFlow() {
		return flow;
	}
	
	public void setFlow(Flow flow) {
		this.flow = flow;
	}	
	
	public Software getSoftware() {
		return software;
	}

	public Action getNewAction() {
		if ( null == newAction ) {
			ActionListener al = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showPrecondition( false );
				}
			};
			newAction = new BaseAction()
				.withName( Messages.getString( "_NEW" ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.postconditionNewIcon() ) )
				.withListener( al );
		}
		return newAction;
	}		

	public Action getEditAction() {
		if ( null == editAction ) {
			editAction = new BaseAction()
				.withName( Messages.alt( "_EDIT", "Edit..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.postconditionEditIcon() ) )
				.withListener( new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						showPrecondition( true );
					}
				} );
		}
		return editAction;
	}
	
	public Action getRemoveAction() {
		if ( null == removeAction ) {
			removeAction = new BaseAction()
				.withName( Messages.alt( "_REMOVE", "Remove..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.postconditionRemoveIcon() ) )
				.withListener( createRemoveActionListener() );
		}
		return removeAction;
	}	

	//
	// PRIVATE
	//
	
	private void showPrecondition(final boolean edit) {
	
		final Postcondition obj;
		if ( edit ) {
			obj = getSelectedObject();
		} else {
			obj = new Postcondition();
			obj.setOwnerFlow( flow );
		}
		
		if ( null == obj ) {
			return;
		}
		
		PostconditionDialog dlg = new PostconditionDialog();
		UIUtil.centerOnScreen( dlg );
		dlg.setPostcondition( obj );
		dlg.setVisible( true );
		if ( ! dlg.isConfirmed() ) { return; }
		obj.copy( dlg.getPostcondition() );
		if ( edit ) {
			// Update the current object
			getTableModel().updated( getSelectedIndex() );
		} else {
			obj.setId( software.generateIdFor( ConditionState.class.getSimpleName() ) );
			// Add
			getTableModel().add( obj ); 
		}
	}
	
	private ActionListener createRemoveActionListener() {
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

}
