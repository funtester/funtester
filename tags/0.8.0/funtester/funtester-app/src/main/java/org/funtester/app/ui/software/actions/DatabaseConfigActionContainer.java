package org.funtester.app.ui.software.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.Action;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.software.DatabaseConfigDialog;
import org.funtester.app.ui.util.BaseAction;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.app.ui.util.TreeSingleSelectionKeeper;
import org.funtester.app.ui.util.UIUtil;
import org.funtester.common.util.Announcer;
import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.software.DatabaseConfig;
import org.funtester.core.software.DatabaseDriverConfig;
import org.funtester.core.software.Software;

/**
 * Database config action container
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DatabaseConfigActionContainer
	extends TreeSingleSelectionKeeper< DatabaseConfig > {
	
	private final Software software;
	private final Map< DatabaseDriverConfig, String > driverFileMap;
	private final DriverCache driverCache;

	private Action newAction = null;
	private Action editAction = null;
	private Action removeAction = null;
	
	private final Announcer< DatabaseConfigCUDEventListener > cudAnnouncer =
			Announcer.to( DatabaseConfigCUDEventListener.class );
	
	public DatabaseConfigActionContainer(
			final Software software,
			final Map< DatabaseDriverConfig, String > driverFileMap,
			final DriverCache driverCache
			) {
		this.software = software;
		this.driverFileMap = driverFileMap;
		this.driverCache = driverCache;
	}
	
	public boolean addDatabaseConfigCUDEventListener(DatabaseConfigCUDEventListener l) {
		return cudAnnouncer.addListener( l );
	}
	
	public boolean removeDatabaseConfigCUDEventListener(DatabaseConfigCUDEventListener l) {
		return cudAnnouncer.removeListener( l );
	}
	
	public Action getNewAction() {
		if ( null == newAction ) {
			newAction = new BaseAction()
				.withName( Messages.alt( "_NEW", "New..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.databaseNewIcon() ) )
				.withListener( createNewActionListener() )
				;
		}
		return newAction;
	}
	
	public Action getEditAction() {
		if ( null == editAction ) {
			editAction = new BaseAction()
				.withName( Messages.alt( "_EDIT", "Edit..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.databaseEditIcon() ) )
				.withListener( createEditActionListener() )
				;
		}
		return editAction;
	}
	
	public Action gerRemoveAction() {
		if ( null == removeAction ) {
			removeAction = new BaseAction()
				.withName( Messages.alt( "_REMOVE", "Remove..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.databaseRemoveIcon() ) )
				.withListener( createRemoveActionListener() )
				;
		}
		return removeAction;
	}
	
	private ActionListener createNewActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DatabaseConfigDialog dlg = new DatabaseConfigDialog( software, driverFileMap, driverCache );
				UIUtil.centerOnScreen( dlg );
				dlg.setVisible( true );
				if ( dlg.isConfirmed() ) {
					DatabaseConfig obj = dlg.getDatabaseConfig().newCopy();
					obj.setId( software.generateIdFor( DatabaseConfig.class.getSimpleName() ) );
					//System.out.println( obj );
					// Add
					software.addDatabaseConfiguration( obj );
					// Announce
					//getAnnouncer().announce().updated( obj );
					cudAnnouncer.announce().created( software, obj,
							software.indexOfDatabaseConfiguration( obj ) );
				}
				dlg = null;
			}
		};
	}
	
	private ActionListener createEditActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( isSelectionEmpty() ) { return; }
				DatabaseConfig obj = getSelectedObject();
				DatabaseConfigDialog dlg = new DatabaseConfigDialog( software, driverFileMap, driverCache );
				dlg.setDatabaseConfig( obj );
				// Show the dialog
				UIUtil.centerOnScreen( dlg );
				dlg.setVisible( true );
				if ( dlg.isConfirmed() ) {
					obj.copy( dlg.getDatabaseConfig() );
					// Announce
					//getAnnouncer().announce().updated( obj );
					cudAnnouncer.announce().updated( software, obj,
							software.indexOfDatabaseConfiguration( obj ) );
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
				DatabaseConfig obj = getSelectedObject();
				// Ask
				if ( ! MsgUtil.yesTo( null, Messages.alt( "_ASK_REMOVE", "Remove ?" ),
						obj.getName() ) ) {
					return;
				}
				final int index = software.indexOfDatabaseConfiguration( obj );
				// Remove
				software.removeDatabaseConfiguration( obj );
				// Announce
				//getAnnouncer().announce().updated( software );
				cudAnnouncer.announce().deleted( software, obj, index );
			}
		};
	}
}
