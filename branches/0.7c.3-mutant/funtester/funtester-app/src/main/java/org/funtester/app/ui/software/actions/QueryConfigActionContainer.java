package org.funtester.app.ui.software.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.software.QueryConfigDialog;
import org.funtester.app.ui.util.BaseAction;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.app.ui.util.TreeSingleSelectionKeeper;
import org.funtester.app.ui.util.UIUtil;
import org.funtester.common.util.Announcer;
import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.software.QueryConfig;
import org.funtester.core.software.Software;

/**
 * Query config action container
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class QueryConfigActionContainer
	extends TreeSingleSelectionKeeper< QueryConfig > {
	
	private final Software software;
	private final DriverCache driverCache;
	
	private Action newAction = null;
	private Action editAction = null;
	private Action removeAction = null;

	private final Announcer< QueryConfigCUDEventListener > cudAnnouncer =
			Announcer.to( QueryConfigCUDEventListener.class );
	
	public QueryConfigActionContainer(
			final Software software,
			final DriverCache driverCache
			) {
		this.software = software;
		this.driverCache = driverCache;
	}
	
	public boolean addQueryConfigCUDEventListener(QueryConfigCUDEventListener l) {
		return cudAnnouncer.addListener( l );
	}
	
	public boolean removeQueryConfigCUDEventListener(QueryConfigCUDEventListener l) {
		return cudAnnouncer.removeListener( l );
	}

	public Action getNewAction() {
		if ( null == newAction ) {
			newAction = new BaseAction()
				.withName( Messages.alt( "_NEW", "New..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.queryNewIcon() ) )
				.withListener( createNewActionListener() )
				;
		}
		return newAction;
	}
	
	public Action getEditAction() {
		if ( null == editAction ) {
			editAction = new BaseAction()
				.withName( Messages.alt( "_EDIT", "Edit..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.queryEditIcon() ) )
				.withListener( createEditActionListener() )
				;
		}
		return editAction;
	}
	
	public Action getRemoveAction() {
		if ( null == removeAction ) {
			removeAction = new BaseAction()
				.withName( Messages.alt( "_REMOVE", "Remove..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.queryRemoveIcon() ) )
				.withListener( createRemoveActionListener() )
				;
		}
		return removeAction;
	}
	
	private ActionListener createNewActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				QueryConfig obj = createQueryConfig( software, driverCache );
				
				if ( obj != null ) {
					final int index = software.queryConfigurationsForDatabase(
							obj.getDatabaseConfig() ).indexOf( obj );
					// Announce
					cudAnnouncer.announce().created( software, obj, index );
				}
			}
		};
	}
	
	/**
	 * Create a {@link QueryConfig} by the UI.
	 * 
	 * @return
	 */
	public static QueryConfig createQueryConfig(
			final Software software,
			final DriverCache driverCache
			) {
		QueryConfigDialog dlg = new QueryConfigDialog( software, driverCache );
		UIUtil.centerOnScreen( dlg );
		dlg.setVisible( true );
		if ( dlg.isConfirmed() ) {
			QueryConfig obj = dlg.getQueryConfig().newCopy();
			obj.setId( software.generateIdFor( QueryConfig.class.getSimpleName() ) );
			// Add
			software.addQueryConfiguration( obj );
			// Return
			return obj;
		}
		return null;
	}
	
	private ActionListener createEditActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( isSelectionEmpty() ) { return; }
				QueryConfig obj = getSelectedObject();
				QueryConfigDialog dlg = createConfigDialog();
				dlg.setQueryConfig( obj );
				// Show the dialog
				UIUtil.centerOnScreen( dlg );
				dlg.setVisible( true );
				if ( dlg.isConfirmed() ) {
					obj.copy( dlg.getQueryConfig() );
					// Announce
					// getAnnouncer().announce().updated( obj );
					
					final int index = software.queryConfigurationsForDatabase(
							obj.getDatabaseConfig() ).indexOf( obj );
					
					cudAnnouncer.announce().updated( software, obj, index );
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
				QueryConfig obj = getSelectedObject();
				// Ask
				if ( ! MsgUtil.yesTo( null, Messages.alt( "_ASK_REMOVE", "Remove ?" ),
						obj.getName() ) ) {
					return;
				}
				
				final int index = software.queryConfigurationsForDatabase(
						obj.getDatabaseConfig() ).indexOf( obj );
				
				// Remove
				software.removeQueryConfiguration( obj );
				// Announce
				// getAnnouncer().announce().updated( software );
				
				cudAnnouncer.announce().deleted( software, obj, index );
			}
		};
	}
	
	private QueryConfigDialog createConfigDialog() {
		return new QueryConfigDialog( software, driverCache );
	}
}
