package org.funtester.app.ui.actions;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;

import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppState;
import org.funtester.app.project.Version;
import org.funtester.app.project.VersionChecker;
import org.funtester.app.ui.AboutDialog;
import org.funtester.app.ui.common.DefaultFileNameExtensionFilters;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.BaseAction;
import org.funtester.app.ui.util.CursorController;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.app.ui.util.SimpleFileChooser;
import org.funtester.app.ui.util.UIUtil;
import org.funtester.common.util.FileUtil;

/**
 * Container for the "Help" actions.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class HelpActionContainer implements PropertyChangeListener {
	
	private final JFrame owner;
	private final String title;
	private final AppState appState;
	
	private final CursorController cursorController;
	
	private Action openManualAction = null;
	private Action goTowebSiteAction = null;
	private Action checkForNewVersionAction = null;
	private Action showAboutDialogAction = null;
	
		
	public HelpActionContainer(
			final JFrame owner,
			final String title,
			final AppState appState
			) {
		this.owner = owner;
		this.title = title;
		this.appState = appState;
		
		cursorController = new CursorController( this.owner );
		cursorController.saveCursor();
	}
	
	private Component getOwner() {
		return owner;
	}
	
	private String getTitle() {
		return title;
	}
	
	private AppState getAppState() {
		return appState;
	}

	public Action getOpenManualAction() {
		if ( null == openManualAction ) {
			openManualAction = new BaseAction()
				.withName( Messages.alt( "_MENU_HELP_MANUAL", "Manual..." ) )
				.withListener( new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						openManualFile();
					}
				} )
				;
		}
		return openManualAction;
	}
	
	public Action getGoToWebSiteAction() {
		if ( null == goTowebSiteAction ) {
			goTowebSiteAction = new BaseAction()
				.withName( Messages.alt( "_MENU_HELP_WEBSITE", "Web site..." ) )
				.withListener( new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						goToWebSite();
					}
				} ) 
				;
		}
		return goTowebSiteAction;
	}
	
	public Action getCheckForNewVersionAction() {
		if ( null == checkForNewVersionAction ) {
			checkForNewVersionAction = new BaseAction()
				.withName( Messages.alt( "_MENU_HELP_CHECK_VERSION", "Check for a new version..." ) )
				.withListener( new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						checkForNewVersion();
					}
				} ) 
				;
		}
		return checkForNewVersionAction;		
	}

	public Action getShowAboutDialogAction() {
		if ( null == showAboutDialogAction ) { 
			showAboutDialogAction = new BaseAction()
				.withName( Messages.alt( "_MENU_HELP_ABOUT", "About..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.aboutIcon() ) )
				.withListener( new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						showAboutDialog();
					}
				} )
				;
		}
		return showAboutDialogAction;
	}
	
	
	/**
	 * Check for a new version and display a message to the user, informing
	 * about it.
	 */
	private void checkForNewVersion() {
		
		SwingWorker< Version, String >  worker= new SwingWorker< Version, String >() {

			@Override
			protected Version doInBackground() throws Exception {
				return ( new VersionChecker() ).newerVersion();
			}
			
			@Override
			public void done() {
				
				Version newerVersion;
				try {
					newerVersion = get();
				} catch ( InterruptedException e ) {
					String msg = Messages.alt( "_EXECUTION_CANCELLED", "Execution cancelled." ); 
					MsgUtil.info( owner, msg, title );
					return;
				} catch ( ExecutionException e ) {
					
					String originalMessage = e.getLocalizedMessage();
					// Search for a ":", that usually appears after the word "Exception"
					int index = originalMessage.indexOf( ":" );
					if ( index > 0 ) {
						originalMessage = originalMessage.substring( index + 1 );
					}
					
					String msg = String.format(
							Messages.alt(
									"_ERROR_CONTACTING_WEBSITE", 
									"Error while trying to contact the FunTester site. Please verify your internet connection.\n\nDetails: %s" ),
							originalMessage
							);
					
					MsgUtil.info( owner, msg, title );
					return;
				}
				
				Version currentVersion = appState.getAppInfo().getVersion();
				
				if ( newerVersion.after( currentVersion ) ) {
					
					String msg = String.format(
							Messages.alt(
									"_NEW_VERSION_AVAILABLE",
									"<html>Newer version available: <b>%s</b><br /><br />Would you like to open the download site?<br /></html>" ),
							newerVersion.toString()
							);
					
					if ( MsgUtil.yesTo( owner, msg, title ) ) {
						openWebSite( appState.getAppInfo().getDownloadSiteURL() );
					}
				} else {
					String msg = Messages.alt( "_VERSION_UPDATED",
							"You already have the latest version." );
					MsgUtil.info( owner, msg, title );
				}
			}
		};
		
		// Add *this* as a listener for changing the cursor 
		worker.addPropertyChangeListener( this );
		
		try {
			worker.execute();
		} catch (Exception e) {
			String msg = "" + e.getLocalizedMessage();  // TODO i18n
			MsgUtil.error( owner, msg, title );
			return;
		}
	}
	
	
	/**
	 * Open the manual file. 
	 */
	private void openManualFile() {
		
		Locale locale = Locale.getDefault();
		
		File manualFile = new File( manualFileNameForLocale( locale ) );
		
		if ( ! manualFile.exists() ) {
			
			String manualFilePath = manualFile.getAbsolutePath();
			
			if ( ! theUserWantsToSearchTheManual( manualFilePath ) ) {
				return;
			}

			manualFilePath = SimpleFileChooser.chooseFile(
					owner,
					Messages.alt( "_OPEN_FILE", "Open" ),
					FileUtil.currentDirectory(),
					DefaultFileNameExtensionFilters.MANUAL,
					true
					).getAbsolutePath();
					
			if ( null == manualFilePath ) {
				return;
			}
			
			manualFile = new File( manualFilePath );
		}
		
		try {
			Desktop.getDesktop().open( manualFile );
		} catch ( IOException ex ) {
			MsgUtil.error( getOwner(), ex.getLocalizedMessage(), getTitle() );
		}
	}
	
	/**
	 * Return a manual file name using a locale information.
	 * 
	 * @param locale
	 * @return
	 */
	private String manualFileNameForLocale(final Locale locale) {
		//return "manual/manual_pt_br.pdf";
		return String.format( "manual/manual_%s_%s.pdf",
				locale.getLanguage().toLowerCase(),
				locale.getCountry().toLowerCase()
				);
	}
	
	/**
	 * Ask the user for a manual file and return {@code true} whether he or
	 * she wants to search it.
	 * 
	 * @param fileName the file name.
	 * @return
	 */
	private boolean theUserWantsToSearchTheManual(final String fileName) {
		final String msg = String.format(
				Messages.alt( "_MANUAL_FILE_NOT_FOUND", "Manual file not found: \"%s\". Do you want to search it?" ),
				fileName );
		return MsgUtil.yesTo( getOwner(), msg, getTitle() );
	}
	
	/**
	 * Try to open the project website.
	 */
	private void goToWebSite() {
		openWebSite( getAppState().getAppInfo().getSiteURL() );
	}
	
	/**
	 * Try to open a web site.
	 * 
	 * @param url the website url.
	 */
	private void openWebSite(final String url) {
		try {
			Desktop.getDesktop().browse( new URI( url ) );
		} catch ( Exception e1 ) {
			MsgUtil.error( owner, e1.getLocalizedMessage(), getTitle() );
		}		
	}
	
	
	/**
	 * Show the "About" dialog.
	 */
	private void showAboutDialog() {
		AboutDialog dlg = new AboutDialog( appState );
		UIUtil.centerOnScreen( dlg );
		dlg.setVisible( true );
	}

	/**
	 * Lister to property changes in the SwingWorker and change the current
	 * cursor accordingly.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		// SwingWorker publishes its "state"
		
		if ( evt.getPropertyName().equals( "state" ) ) {
			
			// When the SwingWorker starts
			if ( evt.getNewValue() == StateValue.STARTED ) {
				cursorController.changeToBusyCursor();
				
			// When the SwingWorker is done
			} else if ( evt.getNewValue() == StateValue.DONE ) {
				cursorController.restoreCursor();
			}
		}
	}

}