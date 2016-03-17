package org.funtester.common.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteStreamHandler;

/**
 * Run commands.
 * 
 * @author Thiago Delgado Pinto
 */
public class CommandRunner {
	
	/**
	 * Run a command and wait for its termination.
	 * 
	 * @param command	the command to be executed.
	 * @return			the command exit value.
	 * @throws IOException 
	 * @throws ExecuteException 
	 */
	public int runAndWait(final String command) throws ExecuteException, IOException  {
		return runAndWait( command, null );
	}
	
	/**
	 * Run a command and wait for its termination.
	 * 
	 * @param command
	 * 			the command to be executed.
	 * @param streamHandler
	 * 			the stream that handles the command output. Ignored if null.
	 * @return	a exit value.
	 * @throws IOException 
	 * @throws ExecuteException 
	 * 
	 * <p>How to use it:</p>
	 * <pre>
	 * <code>
	 *  public class CollectingLogOutputStream extends LogOutputStream {
	 *    @Override
	 *    protected void processLine(String line, int level) {
	 *      System.out.println( "Level " + level + " - " + line );
	 *    }
	 *  } // class
	 *
	 *  CollectingLogOutputStream outStream = new CollectingLogOutputStream();
	 *  PumpStreamHandler streamHandler = new PumpStreamHandler( outStream );
	 * 
	 *  CommandRunner.runAndWait( "cmd /K dir /w", streamHandler );
	 * </code>
	 * </pre>
	 * 
	 */
	public int runAndWait(
			final String command,
			ExecuteStreamHandler streamHandler
			) throws ExecuteException, IOException  {
		CommandLine cmdLine = CommandLine.parse( command );
		DefaultExecutor executor = new DefaultExecutor();
		if ( streamHandler != null ) {
			executor.setStreamHandler( streamHandler );
		}
		return executor.execute( cmdLine );
	}	

	
	/**
	 * Open a file with the OS defined application.
	 * 
	 * @param fileName	the file to be opened.
	 * @throws IOException
	 */
	public void open(final String fileName) throws IOException {
		Desktop.getDesktop().open( new File( fileName ) );
	}

}
