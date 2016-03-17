package org.funtester.app.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import org.funtester.app.common.DefaultFileExtensions;
import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppState;
import org.funtester.app.project.DocGenerator;
import org.funtester.app.project.FileState;
import org.funtester.app.project.HTMLDocGenerator;
import org.funtester.app.project.Project;
import org.funtester.app.project.ProjectListener;
import org.funtester.app.repository.ProjectRepository;
import org.funtester.app.repository.json.JsonProjectRepositoryWithRelativePaths;
import org.funtester.app.ui.common.DefaultFileNameExtensionFilters;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.software.SoftwareDialog;
import org.funtester.app.ui.util.BaseAction;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.app.ui.util.SimpleFileChooser;
import org.funtester.app.ui.util.UIUtil;
import org.funtester.common.util.Announcer;
import org.funtester.common.util.FileUtil;
import org.funtester.core.software.Software;

/**
 * Container for the "File" actions.
 *
 * TODO refactor this class!
 *
 * @author Thiago Delgado Pinto
 *
 */
public class FileActionContainer {

	private final JFrame owner;

	private Action fileNewAction = null;
	private Action fileOpenAction = null;
	private Action fileCloseAction = null;
	private Action fileSaveAction = null;
	private Action fileSaveAsAction = null;
	private Action filePrintAction = null;
	private Action fileExportAsHTMLAction = null;
	private Action fileExitAction = null;


	private final AppState appState;

	private final Announcer< ProjectListener > announcer =
			Announcer.to( ProjectListener.class );

	public FileActionContainer(JFrame owner, AppState appState) {
		this.owner = owner;
		this.appState = appState;
		updateActionsEnabledState();
	}

	private Announcer< ProjectListener > getAnnouncer() {
		return announcer;
	}

	public void addListener(ProjectListener listener) {
		getAnnouncer().addListener( listener );
	}

	public void removeListener(ProjectListener listener) {
		getAnnouncer().removeListener( listener );
	}

	public Action getFileNewAction() {
		return ( null == fileNewAction )
			? fileNewAction = new BaseAction()
				.withName( Messages.alt( "_MENU_FILE_NEW", "New..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.newIcon() ) )
				.withAcceleratorKey( KeyStroke.getKeyStroke( KeyEvent.VK_N, KeyEvent.CTRL_MASK ) )
				.withListener( createFileNewActionListener() )
			: fileNewAction;
	}

	public Action getFileOpenAction() {
		return ( null == fileOpenAction )
				? fileOpenAction = new BaseAction()
					.withIcon( ImageUtil.loadIcon( ImagePath.openIcon() ) )
					.withName( Messages.alt( "_MENU_FILE_OPEN", "Open..." ) )
					.withAcceleratorKey( KeyStroke.getKeyStroke( KeyEvent.VK_O, KeyEvent.CTRL_MASK ) )
					.withListener( createFileOpenActionListener() )
				: fileOpenAction;
	}

	public Action getFileCloseAction() {
		return ( null == fileCloseAction )
			? fileCloseAction = new BaseAction()
				.withName( Messages.alt( "_MENU_FILE_CLOSE", "Close" ) )
				.withListener( createFileCloseActionListener() )
			: fileCloseAction;
	}

	public Action getFileSaveAction() {
		return ( null == fileSaveAction )
				? fileSaveAction = new BaseAction()
					.withName( Messages.alt( "_MENU_FILE_SAVE", "Save" ) )
					.withIcon( ImageUtil.loadIcon( ImagePath.saveIcon() ) )
					.withAcceleratorKey( KeyStroke.getKeyStroke( KeyEvent.VK_S, KeyEvent.CTRL_MASK ) )
					.withListener( createFileSaveActionListener() )
				: fileSaveAction;
	}

	public Action getFileSaveAsAction() {
		return ( null == fileSaveAsAction )
				? fileSaveAsAction = new BaseAction()
					.withName( Messages.alt( "_MENU_FILE_SAVE_AS", "Save As..." ) )
					.withListener( createFileSaveAsActionListener() )
				: fileSaveAsAction;
	}

	public Action getFilePrintAction() {
		return ( null == filePrintAction )
				? filePrintAction = new BaseAction()
					.withName( Messages.alt( "_MENU_FILE_PRINT", "Print..." ) )
					.withAcceleratorKey( KeyStroke.getKeyStroke( KeyEvent.VK_P, KeyEvent.CTRL_MASK ) )
					.withListener( createFilePrintActionListener() )
				: filePrintAction;
	}

	public Action getFileExportAsHTMLAction() {
		return ( null == fileExportAsHTMLAction )
			? fileExportAsHTMLAction = new BaseAction()
				.withName( Messages.alt( "_MENU_FILE_EXPORT_HTML", "As HTML..." ) )
				.withListener( createFileExportAsHTMLActionListener() )
			: fileExportAsHTMLAction;
	}


	public Action getFileExitAction() {
		return ( null == fileExitAction )
			? fileExitAction = new BaseAction()
				.withName( Messages.alt( "_MENU_FILE_EXIT", "Exit" ) )
				.withListener( createFileExitActionListener() )
			: fileExitAction;
	}

	public void updateActionsEnabledState() {
		final boolean isClosed = ( FileState.CLOSED == appState.getProjectFileState() );
		// fileNewAction() is always enabled
		// fileOpenAction() is always enabled
		getFileCloseAction().setEnabled( ! isClosed );
		getFileSaveAction().setEnabled( ! isClosed );
		getFileSaveAsAction().setEnabled( ! isClosed );
		getFilePrintAction().setEnabled( ! isClosed );
		//getFileExportAsHTMLAction().setEnabled( ! isClosed );
	}


	private ActionListener createFileCloseActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		};
	}

	private ActionListener createFileExitActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( ! close() ) { return; }
				// Terminate the execution
				System.exit( 0 );
			}
		};
	}

	private ActionListener createFileNewActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				final ActionListener closeAL = createFileCloseActionListener();
				closeAL.actionPerformed( e );
				if ( ! appState.getProjectFileState().equals( FileState.CLOSED ) ) { return; }

				SoftwareDialog dlg = new SoftwareDialog( appState.getVocabularyMap().keySet() );
				UIUtil.centerOnScreen( dlg );
				dlg.setVisible( true );
				if (! dlg.isConfirmed() ) { return; }

				Software software = dlg.getSoftware().newCopy();

				final String vocabularyFileName =
						appState.getVocabularyMap().get( software.getVocabulary() );

				Project project = new Project();
				project.setVocabularyFile( vocabularyFileName );
				project.setSoftware( software );

				appState.setProject( project );
				appState.setProjectFileState( FileState.DIRTY );
				appState.setProjectFileName( "" ); // Not saved yet

				// Notify
				getAnnouncer().announce().hasOpened( project );

				updateActionsEnabledState();
			}
		};
	}

	private ActionListener createFileOpenActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				final ActionListener closeAL = createFileCloseActionListener();
				closeAL.actionPerformed( e );
				if ( ! appState.getProjectFileState().equals( FileState.CLOSED ) ) { return; }

				final String dialogTitle = Messages.alt( "_OPEN_FILE", "Open" );
				final File currentDirectory = new File( "." );

				String filePath = SimpleFileChooser.chooseFile(
						owner, dialogTitle, currentDirectory,
						DefaultFileNameExtensionFilters.PROJECT, false
						).getAbsolutePath();

				// Has cancelled? Exit!
				if ( null == filePath ) {
					return;
				}
				final String expectedExtension = "." + DefaultFileExtensions.PROJECT;
				if  ( ! filePath.endsWith( expectedExtension ) ) {
					filePath += expectedExtension;
				}

				// Open
				final Project project;
				try {
					project = createProjectRepository( filePath ).first();
				} catch ( Exception e1 ) {
					e1.printStackTrace();
					MsgUtil.error( owner, e1.getLocalizedMessage(), dialogTitle );
					return;
				}

				appState.setProject( project );
				appState.setProjectFileState( FileState.DIRTY );
				appState.setProjectFileName( filePath );

				// Notify
				getAnnouncer().announce().hasOpened( project );

				updateActionsEnabledState();
			}
		};
	}

	private ActionListener createFileSaveActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save( false );
			}
		};
	}

	private ActionListener createFileSaveAsActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save( true );
			}
		};
	}

	private ActionListener createFileExportAsHTMLActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DocGenerator g = new HTMLDocGenerator();
				try {					
					g.generate( appState.getProject().getSoftware() );
				} catch ( Exception e1 ) {
					MsgUtil.error( owner, e1.getLocalizedMessage(), "Export" );
				}
			}
		};
	}

	private ActionListener createFilePrintActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Not available whether the project is closed
				if ( ! appState.getProjectFileState().equals( FileState.CLOSED ) ) { return; }


				// TODO: Complete this action.
			}
		};
	}


	private boolean save(final boolean saveAsAnotherFile) {

		if ( ! appState.getProjectFileState().equals( FileState.CLOSED ) ) {

			final String dialogTitle = Messages.alt( "_SAVE_FILE", "Save" );
			String projectFilePath = appState.getProjectFileName();

			/*
			final File targetDirectory = ( saveAsAnotherFile )
				? new File( "." )
				: new File( System.getProperty( "user.home", "." ) );
			*/
			final File targetDirectory = FileUtil.currentDirectory();

			final boolean isNewProject = ( null == projectFilePath || projectFilePath.isEmpty() );
			if ( isNewProject || saveAsAnotherFile ) {

				projectFilePath = SimpleFileChooser.chooseFile(
						owner, dialogTitle, targetDirectory,
						DefaultFileNameExtensionFilters.PROJECT, true
						).getAbsolutePath();

				// Has cancelled? Exit!
				if ( null == projectFilePath ) {
					return false;
				}
			}

			final String expectedProjectExtension = "." + DefaultFileExtensions.PROJECT;
			if  ( ! projectFilePath.endsWith( expectedProjectExtension ) ) {
				projectFilePath += expectedProjectExtension;
			}

			final String softwareFilePath = projectFilePath.replace(
					expectedProjectExtension, "." + DefaultFileExtensions.SOFTWARE );

			Project project = appState.getProject();
			project.setSoftwareFile( softwareFilePath );

			// Save
			try {
				createProjectRepository( projectFilePath ).save( project );
			} catch ( Exception e1 ) {
				e1.printStackTrace();
				MsgUtil.error( owner, e1.getLocalizedMessage(), dialogTitle );
				return false;
			}

			appState.setProjectFileState( FileState.SAVED );
			appState.setProjectFileName( projectFilePath );
		}
		// Notify
		getAnnouncer().announce().hasSaved( appState.getProject() );
		updateActionsEnabledState();

		return true;
	}


	private boolean close() {
		// If it is dirty, ask for saving
		if ( appState.getProjectFileState() == FileState.DIRTY ) {
			// Ask if want to save
			final String msg = Messages.alt( "_WANNA_SAVE", "Do you want to save?" );
			final String title = Messages.alt( "_CLOSE", "Close" );

			final int answer =  MsgUtil.answerToYesNoCancel( owner, msg, title );
			if ( MsgUtil.isYes( answer ) ) {
				if ( ! save( false ) ) {
					return false;
				}
			} else if ( MsgUtil.isCancel( answer ) ) {
				return false; // Exit
			}
			// Do not want to save, then continue...
		}

		// Destroy the current project
		appState.closeProject();
		updateActionsEnabledState();

		// Notify
		getAnnouncer().announce().hasClosed( appState.getProject() );

		return true;
	}

	private ProjectRepository createProjectRepository(final String filePath) {
		return new JsonProjectRepositoryWithRelativePaths( filePath );
	}

}
