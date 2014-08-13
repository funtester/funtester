package org.funtester.app.util.editor;

/**
 * A savior that does nothing.
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T>
 */
public final class VoidSavior< T > implements Savior< T > {

	@Override
	public void save(T obj) {
		// Do nothing
	}

}
