package org.funtester.app.ui.software.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.software.RegExDialog;
import org.funtester.app.ui.util.BaseAction;
import org.funtester.app.ui.util.BaseTableModel;
import org.funtester.app.ui.util.CRUDActionContainer;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.app.ui.util.UIUtil;
import org.funtester.core.software.RegEx;
import org.funtester.core.software.Software;

/**
 * RegEx action container
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class RegExActionContainer extends CRUDActionContainer< RegEx > {
	
	private Action newAction = null;
	private Action editAction = null;
	private Action removeAction = null;
	
	private final Software software;
		
	public RegExActionContainer(
			BaseTableModel< RegEx > tableModel,
			final Software software
			) {
		super( tableModel );
		this.software = software;
	}	

	public Action newAction() {
		if ( null == newAction ) {
			newAction = new BaseAction()
				.withName( Messages.alt( "_NEW", "New..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.regExNewIcon() ) )
				.withListener( createNewActionListener() )
				;
		}
		return newAction;
	}
	
	public Action editAction() {
		if ( null == editAction ) {
			editAction = new BaseAction()
				.withName( Messages.alt( "_EDIT", "Edit..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.regExEditIcon() ) )
				.withListener( createEditActionListener() )
				;
		}
		return editAction;
	}
	
	public Action removeAction() {
		if ( null == removeAction ) {
			removeAction = new BaseAction()
				.withName( Messages.alt( "_REMOVE", "Remove..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.regExRemoveIcon() ) )
				.withListener( createRemoveActionListener() )
				;
		}
		return removeAction;
	}
	
	private ActionListener createNewActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RegEx obj = createRegEx( software );
				if ( obj != null ) {
					getTableModel().add( obj );
				}
			}
		};
	}
	
	/**
	 * Create a regular expression (with id) using a IU.
	 * 
	 * @param software	software used for validation and the id generation.
	 * @return
	 */
	public static RegEx createRegEx(Software software) {
		RegExDialog dlg = new RegExDialog( software );
		UIUtil.centerOnScreen( dlg );
		dlg.setVisible( true );
		if ( ! dlg.isConfirmed() ) { return null; }
		
		RegEx obj = dlg.getRegEx().newCopy();
		obj.setId( software.generateIdFor( RegEx.class.getSimpleName() ) );
		
		return obj;
	}
	
	private ActionListener createEditActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( isSelectionEmpty() ) { return; }
				
				RegEx obj = getSelectedObject();
				RegExDialog dlg = new RegExDialog( software );
				dlg.setRegEx( obj );
				
				// Show the dialog
				UIUtil.centerOnScreen( dlg );
				dlg.setVisible( true );
				if ( dlg.isConfirmed() ) {
					obj.copy( dlg.getRegEx() );
					// Notify update
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
				RegEx obj = getSelectedObject();
				// Ask
				if ( ! MsgUtil.yesTo( null, Messages.alt( "_ASK_REMOVE", "Remove ?" ),
						obj.getName() ) ) {
					return;
				}
				// Remove
				getTableModel().remove( getSelectedIndex() );
			}
		};
	}	
	
}
