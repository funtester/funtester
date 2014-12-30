package org.funtester.app.ui.actions;

import javax.swing.SwingUtilities;

import org.apache.commons.exec.LogOutputStream;
import org.funtester.app.common.MessageListener;
import org.funtester.common.util.Announcer;

/**
 * CollectingLogOutputStream
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class CollectingLogOutputStream extends LogOutputStream {
	
	private final Announcer< MessageListener > announcer =
			Announcer.to( MessageListener.class );
	
	public void addMessageListener(MessageListener l) {
		announcer.addListener( l );
	}
	
	public void removeMessageListener(MessageListener l) {
		announcer.removeListener( l );
	}	
	
	@Override
	protected void processLine(final String line, final int level) {
		
		// Updates the UI
		
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				announcer.announce().published( line );
			}
		} );
		
		// Add to the console
		
		System.out.println( line );
		System.out.flush();
	}
	
} // class
