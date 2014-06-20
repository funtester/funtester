package org.funtester.app.ui.software.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collections;

import javax.swing.Action;
import javax.swing.KeyStroke;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.software.UseCaseDialog;
import org.funtester.app.ui.util.BaseAction;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.app.ui.util.UIUtil;
import org.funtester.common.util.Announcer;
import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.software.Software;
import org.funtester.core.software.UseCase;

/**
 * Use case action container
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class UseCaseActionContainer {
	
	private final Software software;
	private UseCase useCase = null;
	private final DriverCache driverCache;
	
	private Action newAction = null;
	private Action editAction = null;
	private Action removeAction = null;
	
	private final Announcer< UseCaseCUDEventListener > announcer =
			new Announcer< UseCaseCUDEventListener >(
					UseCaseCUDEventListener.class );
	
	public UseCaseActionContainer(
			final Software software,
			final DriverCache driverCache
			) { 
		this.software = software;
		this.driverCache = driverCache;
	}
	
	public boolean addListener(UseCaseCUDEventListener listener) {
		return getAnnouncer().addListener( listener );
	}
	
	public boolean removeListener(UseCaseCUDEventListener listener) {
		return getAnnouncer().removeListener( listener );
	}
	
	public Action getNewAction() {
		if ( null == newAction ) {
			newAction = new BaseAction()
				.withName( Messages.alt( "_USE_CASE_NEW", "New Use Case..." ) )	
				.withIcon( ImageUtil.loadIcon( ImagePath.useCaseNewIcon() ) )
				.withEnabled( false ) // default disabled -> is enabled dynamically
				.withAcceleratorKey( KeyStroke.getKeyStroke( KeyEvent.VK_U, KeyEvent.CTRL_MASK ) )
				.withListener( createUseCaseNewActionListener() )
				;
		}
		return newAction;
	}
	
	public Action getEditAction() {
		if ( null == editAction ) {
			editAction = new BaseAction()
				.withName( Messages.alt( "_USE_CASE_EDIT", "Edit Use Case..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.useCaseEditIcon() ) )
				.withEnabled( false ) // default disabled -> is enabled dynamically
				.withListener( createUseCaseEditActionListener() )
				;
		}
		return editAction;
	}
	
	public Action getRemoveAction() {
		if ( null == removeAction ) {
			removeAction = new BaseAction()
				.withName( Messages.alt( "_USE_CASE_REMOVE", "Remove Use Case..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.useCaseRemoveIcon() ) )
				.withEnabled( false ) // default disabled -> is enabled dynamically
				.withListener( createUseCaseRemoveActionListener() )
				;
		}
		return removeAction;
	}
	
	private Software getSoftware() {
		return software;
	}
	
	private UseCase getUseCase() {
		return useCase;
	}
	
	public void setUseCase(UseCase uc) {
		useCase = uc;
	}
	
	private boolean thereIsNotACurrentUseCase() {
		return null == useCase;
	}
	
	private Announcer< UseCaseCUDEventListener > getAnnouncer() {
		return announcer;
	}
	
	private ActionListener createUseCaseNewActionListener() {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				UseCaseDialog dialog = new UseCaseDialog( null, software, driverCache, true );
				UIUtil.centerOnScreen( dialog );
				dialog.setVisible( true );
				
				if ( dialog.isConfirmed() ) {
					UseCase uc = dialog.getUseCase().newCopy();
					uc.setId( getSoftware().generateIdFor( UseCase.class.getSimpleName() ) );
					
					getSoftware().addUseCase( uc );
					
					// Sort the use cases
					Collections.sort( getSoftware().getUseCases() );
					// Select
					setUseCase( uc );
					// Notify
					getAnnouncer().announce().created( getSoftware(), uc, getSoftware().indexOfUseCase( uc ) );
				} else {
					setUseCase( null ); // Empty selection
				}
			}
		};
	}
	
	private ActionListener createUseCaseEditActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( thereIsNotACurrentUseCase() ) {
					return;
				}
				UseCase uc = getUseCase();
				UseCaseDialog dlg = new UseCaseDialog( uc, software, driverCache, true );
				UIUtil.centerOnScreen( dlg );
				dlg.setVisible( true );		
				if ( dlg.isConfirmed() ) {
					// Updates the use case
					uc.copy( dlg.getUseCase() );
					
					// Sort the use cases
					Collections.sort( getSoftware().getUseCases() );
					
					// Notify
					getAnnouncer().announce().updated( getSoftware(), uc, getSoftware().indexOfUseCase( uc ) );
				}	
			}
		};
	}
	
	private ActionListener createUseCaseRemoveActionListener() {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( thereIsNotACurrentUseCase() ) {
					return;
				}
				UseCase uc = getUseCase();
				// Ask
				if ( ! MsgUtil.yesTo( null, Messages.alt( "_ASK_REMOVE", "Remove ?" ),
						uc.getName() ) ) {
					return;
				}
				
				final int index = getSoftware().indexOfUseCase( uc );
				
				getSoftware().removeUseCase( uc );
				// Update the selection
				setUseCase( null );
				
				// Notify
				getAnnouncer().announce().deleted( getSoftware(), uc, index );
			}
		};		
	}

}
