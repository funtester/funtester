package org.funtester.app.ui.software.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.software.ActorDialog;
import org.funtester.app.ui.util.BaseAction;
import org.funtester.app.ui.util.BaseTableModel;
import org.funtester.app.ui.util.CRUDActionContainer;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.app.ui.util.UIUtil;
import org.funtester.core.software.Actor;
import org.funtester.core.software.Software;

/**
 * Actor action container
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ActorActionContainer extends CRUDActionContainer< Actor > {
	
	private Action newAction = null;
	private Action editAction = null;
	private Action removeAction = null;
	
	private final Software software;
		
	public ActorActionContainer(
			BaseTableModel< Actor > tableModel,
			final Software software
			) {
		super( tableModel );
		this.software = software;
	}	

	public Action newAction() {
		return ( null == newAction )
				? newAction = new BaseAction()
					.withName( Messages.alt( "_NEW", "New..." ) )
					.withIcon( ImageUtil.loadIcon( ImagePath.actorNewIcon() ) )
					.withListener( createActorNewActionListener() )
				: newAction;
	}
	
	public Action editAction() {
		return ( null == editAction )
				? editAction = new BaseAction()
					.withName( Messages.alt( "_EDIT", "Edit..." ) )
					.withIcon( ImageUtil.loadIcon( ImagePath.actorEditIcon() ) )
					.withListener( createActorEditActionListener() )
				: editAction;
	}
	
	public Action removeAction() {
		return ( null == removeAction )
				? removeAction = new BaseAction()
					.withName( Messages.alt( "_REMOVE", "Remove..." ) )
					.withIcon( ImageUtil.loadIcon( ImagePath.actorRemoveIcon() ) )
					.withListener( createActorRemoveActionListener() )
				: removeAction;
	}
	
	private ActionListener createActorNewActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActorDialog dlg = new ActorDialog( software );
				UIUtil.centerOnScreen( dlg );
				dlg.setVisible( true );
				if ( dlg.isConfirmed() ) {
					Actor obj = dlg.getActor().newCopy();
					obj.setId( software.generateIdFor( Actor.class.getSimpleName() ) );
					// Add
					getTableModel().add( obj );
				}
				dlg = null;
			}
		};
	}
	
	private ActionListener createActorEditActionListener() {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( isSelectionEmpty() ) { return; }
				Actor a = getSelectedObject();
				ActorDialog dlg = new ActorDialog( software );
				dlg.setActor( a );
				// Show the dialog
				UIUtil.centerOnScreen( dlg );
				dlg.setVisible( true );
				if ( dlg.isConfirmed() ) {
					a.copy( dlg.getActor() );
					// Notify
					getTableModel().updated( getSelectedIndex() );
					getAnnouncer().announce().updated( a );
				}
				dlg = null;
			}
		};
	}
	
	
	private ActionListener createActorRemoveActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( isSelectionEmpty() ) { return; }
				Actor a = getSelectedObject();
				// Ask
				if ( ! MsgUtil.yesTo( null, Messages.alt( "_ASK_REMOVE", "Remove ?" ),
						a.getName() ) ) {
					return;
				}
				getTableModel().remove( getSelectedIndex() );
			}
		};
	}	
	
}
