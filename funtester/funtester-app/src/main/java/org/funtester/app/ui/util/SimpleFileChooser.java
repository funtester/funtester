package org.funtester.app.ui.util;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * File choosing utility.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class SimpleFileChooser {

	/**
	 * Show a dialog to choose a directory and returns the selected directory.
	 * 
	 * @param owner				the dialog owner.
	 * @param dialogTitle		the dialog title.
	 * @param currentDirectory	the current path.
	 * @return					the selected path or null whether the user
	 * 							cancelled.
	 */
	public static String chooseDirectory(
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
	 * Show a file dialog to open or save a file.
	 * 
	 * @param owner				the dialog owner.
	 * @param dialogTitle		the dialog title.
	 * @param currentDirectory	the current path.
	 * @param extFilter			the file extension filter.
	 * @param chooseForSaving	option for saving or opening the file.
	 * @return					the selected path or null whether the user
	 * 							cancelled.
	 */
	public static String chooseFile(
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
	 * Return the select path from a {@link JFileChooser}.
	 * 
	 * @param chooser		the {@link JFileChooser}.
	 * @param dialogResult	the result from the dialog.
	 * @return
	 */
	private static String selectedPathFrom(
			final JFileChooser chooser,
			final int dialogResult
			) {
		if ( JFileChooser.APPROVE_OPTION == dialogResult ) {
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}
}
