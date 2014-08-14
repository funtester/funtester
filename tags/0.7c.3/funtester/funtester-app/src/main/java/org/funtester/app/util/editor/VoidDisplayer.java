package org.funtester.app.util.editor;

/**
 * A displayer that does nothing.
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T>
 */
public final class VoidDisplayer< T > implements Displayer< T > {

	@Override
	public void display(T obj, DisplayState ds) {
		// Do nothing
	}

}
