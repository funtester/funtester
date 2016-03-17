package org.funtester.app.ui.util;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.funtester.common.util.FilePathUtil;
import org.funtester.common.util.PathType;

/**
 * File choosing utility.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class SimpleFileChooser {
	
	private SimpleFileChooser() {}
	
	
	/**
	 * Show a directory chooser and returns the selected directory.
	 * 
	 * @param owner				the dialog owner.
	 * @param dialogTitle		the dialog title.
	 * @param currentDirectory	the current path.
	 * @return					the selected path or null whether the user
	 * 							cancelled.
	 */
	public static File chooseDirectory(
			final Component owner,
			final String dialogTitle,
			final File currentDirectory
			) {
		JFileChooser chooser = createFileChooser( dialogTitle, currentDirectory );
		chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
		chooser.setDialogType( JFileChooser.OPEN_DIALOG );
		
		final int result = chooser.showOpenDialog( owner );
		return selectedPathFrom( chooser, result );
	}
	
	
	/**
	 * Show a directory chooser and return the selected directory path,
	 * according to the reference directory and the path type. Whether the
	 * path is relative, the chosen directory will be relative to the
	 * reference directory. Otherwise, it will be an absolute path.
	 * 
	 * @param owner					the dialog owner.
	 * @param dialogTitle			the dialog title.
	 * @param referenceDirectory	the reference directory.
	 * @param pathType				whether the path is relative or absolute.
	 * @return						the selected path.
	 */
	public static String chooseDirectoryPath(
			final Component owner,
			final String dialogTitle,
			final File referenceDirectory,
			final PathType pathType
			) {
		final File dir = chooseDirectory( owner, dialogTitle, referenceDirectory );
		if ( null == dir ) {
			return null;
		}
		
		return pathFor( referenceDirectory, pathType, dir );
	}
	
	
	/**
	 * Open a file chooser and return the selected file path,
	 * according to the reference directory and the path type. Whether the
	 * path is relative, the chosen file will be relative to the
	 * reference directory. Otherwise, it will be an absolute path.
	 * 
	 * @param owner					the dialog owner.
	 * @param dialogTitle			the dialog title.
	 * @param referenceDirectory	the reference directory.
	 * @param pathType				whether the path is relative or absolute.
	 * @param extFilter				the file extension filter.
	 * @return
	 */
	public static String openFilePath(
			final Component owner,
			final String dialogTitle,
			final File referenceDirectory,
			final PathType pathType,
			final FileNameExtensionFilter extFilter
			) {
		File file = chooseFile( owner, dialogTitle, referenceDirectory, extFilter, false );
		if ( null == file ) {
			return null;
		}
		return pathFor( referenceDirectory, pathType, file );
	}
	

	/**
	 * Show a file dialog to open or save a file.
	 * 
	 * @param owner				the dialog owner.
	 * @param dialogTitle		the dialog title.
	 * @param currentDirectory	the current path.
	 * @param extFilter			the file extension filter.
	 * @param chooseForSaving	option for saving or opening the file.
	 * @return					the selected file or null whether the user
	 * 							cancelled.
	 */
	public static File chooseFile(
			final Component owner,
			final String dialogTitle,
			final File currentDirectory,
			final FileNameExtensionFilter extFilter,
			final boolean chooseForSaving
			) {
		JFileChooser chooser = createFileChooser( dialogTitle, currentDirectory );
		chooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
		
		if ( extFilter != null ) {
			chooser.addChoosableFileFilter( extFilter );
			chooser.setFileFilter( extFilter );
		} else {
			chooser.setAcceptAllFileFilterUsed( true );
		}
		chooser.setDialogType( chooseForSaving ? JFileChooser.SAVE_DIALOG : JFileChooser.OPEN_DIALOG );

		final int result = ( chooseForSaving ) ? chooser.showSaveDialog( owner )
			: chooser.showOpenDialog( owner );
			
		return selectedPathFrom( chooser, result );
	}
	
	/**
	 * Create a {@link JFileChooser}.
	 * 
	 * @param dialogTitle		the dialog title
	 * @param currentDirectory	the current directory
	 * @return					a {@link JFileChooser} object.
	 */
	private static JFileChooser createFileChooser(
			final String dialogTitle,
			final File currentDirectory
			) {
		JFileChooser chooser = new JFileChooser() {
			private static final long serialVersionUID = 590760511661686717L;
			
			@Override
			protected JDialog createDialog(Component parent)
					throws HeadlessException {
				JDialog dialog = super.createDialog( parent );
				dialog.setLocationByPlatform( true ); // Needed to use with "always on top"
				dialog.setAlwaysOnTop( true );
				return dialog;
			}
		};
		chooser.setCurrentDirectory( currentDirectory );
		chooser.setDialogTitle( dialogTitle );
		chooser.setAcceptAllFileFilterUsed( false );
		return chooser;
	}
	
	/**
	 * Return the select File from a {@link JFileChooser}.
	 * 
	 * @param chooser		the {@link JFileChooser}.
	 * @param dialogResult	the result from the dialog.
	 * @return
	 */
	private static File selectedPathFrom(
			final JFileChooser chooser,
			final int dialogResult
			) {
		if ( JFileChooser.APPROVE_OPTION == dialogResult ) {
			return chooser.getSelectedFile();
		}
		return null;
	}
	
	
	/**
	 * Return a path according to a reference directory and a path type.
	 *  
	 * @param referenceDirectory	the reference directory.
	 * @param pathType				the path type.
	 * @param originalPath 			the original path.
	 * @return
	 */
	private static String pathFor(
			final File referenceDirectory,
			final PathType pathType,
			final File originalPath
			) {
		if ( pathType.equals( PathType.ABSOLUTE ) ) {
			return originalPath.getAbsolutePath();
		}
		
		return FilePathUtil.toRelativePath(
				originalPath.getAbsolutePath(), referenceDirectory.getAbsolutePath() );
	}
}
