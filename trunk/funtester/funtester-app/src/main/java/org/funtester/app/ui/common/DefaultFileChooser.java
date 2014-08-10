package org.funtester.app.ui.common;

import java.io.File;

import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.util.SimpleFileChooser;
import org.funtester.common.util.FilePathUtil;
import org.funtester.common.util.FileUtil;
import org.funtester.common.util.PathType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default file chooser.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class DefaultFileChooser {
	
	private static final Logger logger = LoggerFactory.getLogger( DefaultFileChooser.class );
		
	private DefaultFileChooser() {}
	

	/**
	 * Open a directory selection dialog and put the selected directory
	 * path in the given text field.
	 * 
	 * @param textField				the text field where to put the path.
	 * @param referenceDirectory	the reference directory.
	 * @param pathType				option for choosing the path type.
	 */
	public static void chooseDirectory(
			final JTextField textField,
			final File referenceDirectory,
			final PathType pathType
			) {
		
		final String title = Messages.alt( "_CHOOSE_DIRECTORY", "Choose a directory" );
		
		final File textFieldDir = new File( FilePathUtil.toAbsolutePath(
				textField.getText(), referenceDirectory.getAbsolutePath() ) );
		
		logger.debug( ">>- " + referenceDirectory.getAbsolutePath() );
		logger.debug( ">>= " + textFieldDir.getAbsolutePath() );
		
		String path = SimpleFileChooser.chooseDirectoryPath( 
				textField, title, textFieldDir, pathType );
		
		if ( path != null ) {
			textField.setText( path );
		}
	}
	
	
	/**
	 * Open a file selection dialog and put the selected file path in the given
	 * text field.
	 * 
	 * @param textField			the text field where to put the path.
	 * @param extFilter			the extension filter.
	 * @param chooseForSaving	option for saving or opening the file.
	 * @param pathType			option for choosing the path type.
	 */
	public static void chooseFile(
			JTextField textField,
			final FileNameExtensionFilter extFilter,
			final boolean chooseForSaving,
			final PathType pathType
			) {
		
		File currentDirectory = directoryFor( textField.getText() );
		
		final String title = ( chooseForSaving )
			? Messages.alt( "_SAVE_FILE", "Save file" )
			: Messages.alt( "_OPEN_FILE", "Open file" );
			
		final File file = SimpleFileChooser.chooseFile(
				textField.getParent(), title, currentDirectory, extFilter, chooseForSaving );
		
		if ( file != null ) {
			textField.setText( pathType == PathType.ABSOLUTE ? file.getAbsolutePath() : file.getPath() );
		}
	}
	
	/**
	 * Return the current directory for the given path. If the path is
	 * empty, return the current directory.
	 * 
	 * @param aPath
	 * @return
	 */
	private static File directoryFor(final String aPath) {
		
		if ( null == aPath || aPath.isEmpty() ) {
			return FileUtil.currentDirectory();
		}
		
		File path = new File( aPath );
		if ( path.isFile() ) {
			File parent = path.getParentFile();
			if ( null == parent || ! path.exists() ) {
				return FileUtil.currentDirectory();
			}
			return new File( parent.getPath() );
		}
		
		if ( path.exists() ) {
			return path;
		}
		
		return FileUtil.currentDirectory();
	}  
}
