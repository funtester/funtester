package org.funtester.common.exec;

/**
 * A very simple command parser.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class CommandParser {
	
	public static final String FILE_OPTION = "{file}";
	public static final String DIR_OPTION = "{dir}";

	public String parse(
			final String command,
			final String fileName,
			final String dir
			) {
		String newCmd = command.replace( FILE_OPTION, fileName );
		newCmd = newCmd.replace( DIR_OPTION, dir );
		return newCmd;
	}
	
	public boolean hasFileOption(final String command) {
		return command.contains( FILE_OPTION );
	}
	
	public boolean hasDirOption(final String command) {
		return command.contains( DIR_OPTION );
	}
	
}
