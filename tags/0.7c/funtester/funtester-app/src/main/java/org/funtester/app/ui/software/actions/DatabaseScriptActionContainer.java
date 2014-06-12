package org.funtester.app.ui.software.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.util.BaseAction;
import org.funtester.app.ui.util.BaseTableModel;
import org.funtester.app.ui.util.CRUDActionContainer;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.core.software.DatabaseScript;

/**
 * Database script action container
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DatabaseScriptActionContainer
	extends CRUDActionContainer< DatabaseScript > {
		
	private Action newAction;
	private Action editAction;
	private Action removeAction;
	
	public DatabaseScriptActionContainer(
			final BaseTableModel< DatabaseScript > tableModel
			) {
		super( tableModel );
	}

	public Action getNewAction() {
		if ( null == newAction ) {
			newAction = new BaseAction()
				.withName( Messages.alt( "_NEW", "New..." ) )
				.withListener( createNewActionListener() )
				;
		}
		return newAction;
	}
	
	public Action getEditAction() {
		if ( null == editAction ) {
			editAction = new BaseAction()
				.withName( Messages.alt( "_EDIT", "Edit..." ) )
				.withListener( createEditActionListener() )
				;
		}
		return editAction;
	}
	
	public Action getRemoveAction() {
		if ( null == removeAction ) {
			removeAction = new BaseAction()
				.withName( Messages.alt( "_REMOVE", "Remove..." ) )
				.withListener( createRemoveActionListener() )
				;
		}
		return removeAction;
	}
	
	//
	// PRIVATE
	//
	
	private ActionListener createNewActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showDatabaseScript( false );
			}
		};
	}
	
	private ActionListener createEditActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showDatabaseScript( true );
			}
		};
	}
	
	private ActionListener createRemoveActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( isSelectionEmpty() ) { return; }
				final String msg = Messages.getString( "_ASK_REMOVE" );
				final String title = Messages.alt( "_REMOVE", "Remover" );
				if ( MsgUtil.yesTo( null, msg, title ) ) {
					getTableModel().remove( getSelectedIndex() );
				}
			}
		};
	}
	
	/**
	 * Show a database script.
	 * 
	 * @param editMode
	 */
	private void showDatabaseScript(final boolean editMode) {
		// TODO ...
	}
}
