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
import org.funtester.app.startup.LoadManualsTask;
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
		
		//
		// Detect available manuals
		//
		
		LoadManualsTask task = new LoadManualsTask();
		try {
			task.perform( new String[ 0 ], appState );
		} catch ( Exception e ) {
			MsgUtil.error( getOwner(), e.getLocalizedMessage(), getTitle() );
			return;
		}
		
		String localizedManualFile = "";
		String languageManualFile = "";
		
		String americanEnglishManualFile = "";
		String englishManualFile = "";
		
		
		if ( appState.hasManuals() ) {
			
			Locale defaultLocale = Locale.getDefault();
			
			final String localizedManualPattern = LoadManualsTask.manualPatternForLanguageAndCountry(
					defaultLocale.getLanguage(), defaultLocale.getCountry() );
		
			final String languageManualPattern = LoadManualsTask.manualPatternForLanguage(
					defaultLocale.getLanguage() );
			
			final String americanEnglishManualPattern = LoadManualsTask.manualPatternForLanguageAndCountry(
					Locale.ENGLISH.getLanguage(), Locale.ENGLISH.getCountry() );
			
			final String englishManualPattern = LoadManualsTask.manualPatternForLanguage(
					Locale.ENGLISH.getLanguage() );
			
			
			System.out.println( "patterns: "
					+ "\n" + localizedManualPattern
					+ "\n" + languageManualPattern
					+ "\n" + americanEnglishManualPattern
					+ "\n" + englishManualPattern
					
					);
			
			
			for ( String manual : appState.getManuals() ) {
				
				String m = manual.toLowerCase();
				
				System.out.println( "manual: " + m );
				
				// Match LANGUAGE & COUNTRY ?
				if ( m.matches( localizedManualPattern ) ) {
					localizedManualFile = manual;
					break;
				}
				
				// Match LANGUAGE ?
				if ( m.matches( languageManualPattern ) ) {
					languageManualFile = manual;
				}
				
				// Match ENGLISH LANGUAGE & COUNTRY ?
				if ( m.matches( americanEnglishManualPattern ) ) {
					americanEnglishManualFile = manual;
				}
				
				// Match ENGLISH LANGUAGE ?
				if ( m.matches( englishManualPattern ) ) {
					englishManualFile = manual;
				}
				
			} // for
		}
		
		String manualFile = localizedManualFile;
		if ( manualFile.isEmpty() ) {
			manualFile = languageManualFile;
		}
		
		if ( manualFile.isEmpty()
				&& (   ! americanEnglishManualFile.isEmpty()
					|| ! englishManualFile.isEmpty() ) ) {
			
			String msg = Messages.alt( "",
					"A manual for your language was not found. Do you want to open the english manual?" );
			if ( MsgUtil.yesTo( getOwner(), msg, getTitle() ) ) {
				
				if ( ! americanEnglishManualFile.isEmpty() ) {
					manualFile = americanEnglishManualFile;
				} else {
					manualFile = englishManualFile;
				}
			}
		}
		
		if ( manualFile.isEmpty() ) {
			
			String msg = Messages.alt( "_MANUAL_FILE_ASK_FIND_MANUALLY", "Do you want to find the manual manually?" );
			if ( ! MsgUtil.yesTo( getOwner(), msg, getTitle() ) ) {
				return;
			}

			manualFile = SimpleFileChooser.chooseFile(
					owner,
					Messages.alt( "_OPEN_FILE", "Open" ),
					FileUtil.currentDirectory(),
					DefaultFileNameExtensionFilters.MANUAL,
					true
					).getAbsolutePath();
					
			if ( null == manualFile ) {
				return;
			}
		}
		
		try {
			Desktop.getDesktop().open( new File( manualFile ) );
		} catch ( IOException ex ) {
			MsgUtil.error( getOwner(), ex.getLocalizedMessage(), getTitle() );
		}
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