package org.funtester.app.util.editor;

/**
 * A discarder that does nothing.
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T>
 */
public final class VoidDiscarder< T > implements Discarder< T > {

	@Override
	public void discard(T obj) throws Exception {
		// Do nothing
	}
	
}
