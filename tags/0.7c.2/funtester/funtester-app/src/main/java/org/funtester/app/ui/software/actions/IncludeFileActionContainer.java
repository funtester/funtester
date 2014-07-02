package org.funtester.app.ui.software.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.software.IncludeFileDialog;
import org.funtester.app.ui.util.BaseAction;
import org.funtester.app.ui.util.BaseTableModel;
import org.funtester.app.ui.util.CRUDActionContainer;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.app.ui.util.UIUtil;
import org.funtester.core.software.IncludeFile;

/**
 * Include file action container
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class IncludeFileActionContainer extends CRUDActionContainer< IncludeFile > {
	
	private Action newAction;
	private Action editAction;
	private Action removeAction;
	
	public IncludeFileActionContainer(
			BaseTableModel< IncludeFile > tableModel
			) {
		super( tableModel );
	}
	
	public Action getNewAction() {
		if ( null == newAction ) {
			newAction = new BaseAction()
				.withName( Messages.alt( "_NEW", "New..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.includeFileNewIcon() ) )
				.withListener( createNewActionListener() )
				;
		}
		return newAction;
	}
	
	public Action getEditAction() {
		if ( null == editAction ) {
			editAction = new BaseAction()
				.withName( Messages.alt( "_EDIT", "Edit..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.includeFileEditIcon() ) )
				.withListener( createEditActionListener() );
				;
		}
		return editAction;
	}
	
	public Action getRemoveAction() {
		if ( null == removeAction ) {
			removeAction = new BaseAction()
				.withName( Messages.alt( "_REMOVE", "Remove..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.includeFileRemoveIcon() ) )
				.withListener( createRemoveActionListener() );
				;
		}
		return removeAction;
	}
	
	private ActionListener createNewActionListener() {
		return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					IncludeFileDialog dlg = new IncludeFileDialog();
					UIUtil.centerOnScreen( dlg );
					dlg.setVisible( true );
					if ( dlg.isConfirmed() ) {
						IncludeFile obj = dlg.getIncludeFile().newCopy();
						// Do not use an id (for now)
						getTableModel().add( obj ); // Add it
						getAnnouncer().announce().updated( obj ); // Notify
					}
					dlg = null;
				}
			};
	}
	
	private ActionListener createEditActionListener() {
		return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					IncludeFile obj = getSelectedObject();
					IncludeFileDialog dlg = new IncludeFileDialog();
					dlg.setIncludeFile( obj );
					UIUtil.centerOnScreen( dlg );
					dlg.setVisible( true );
					if ( dlg.isConfirmed() ) {
						obj.copy( dlg.getIncludeFile() );
						getTableModel().updated( getSelectedIndex() );
					}
					dlg = null;
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

}
