package org.funtester.app.ui.common;

import java.io.File;

import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.util.SimpleFileChooser;
import org.funtester.common.util.FileUtil;

/**
 * Default file chooser.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class DefaultFileChooser {
	
	private DefaultFileChooser() {}

	/**
	 * Open a directory selection dialog and put the selected directory
	 * path in the given text field.
	 * 
	 * @param textField	the text field where to put the path.
	 */
	public static void chooseDirectory(JTextField textField) {
		
		File currentDirectory = directoryFor( textField.getText() );
		
		final String title = Messages.alt( "_CHOOSE_DIRECTORY", "Choose a directory" );
		final String path = SimpleFileChooser.chooseDirectory(
				textField.getParent(), title, currentDirectory );
		
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
	 */
	public static void chooseFile(
			JTextField textField,
			final FileNameExtensionFilter extFilter,
			final boolean chooseForSaving
			) {
		
		File currentDirectory = directoryFor( textField.getText() );
		
		final String title = ( chooseForSaving )
			? Messages.alt( "_SAVE_FILE", "Save file" )
			: Messages.alt( "_OPEN_FILE", "Open file" );
			
		final String path = SimpleFileChooser.chooseFile(
				textField.getParent(), title, currentDirectory, extFilter, chooseForSaving );
		
		if ( path != null ) {
			textField.setText( path );
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
