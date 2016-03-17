package org.funtester.app.util.editor;

import org.funtester.common.util.Copier;

/**
 * Allow controlling the edition of an object. Use a {@link Displayer}, a
 * {@link Savior}, and a {@link Discarder} to separate each concern, and
 * focus on controlling the editing state. 
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T> the type of the object to be edited.
 */
public class ObjectEditor< T extends Copier< ? super T > > {
	
	private enum EditorState { VIEWING, EDITING };
	
	private Displayer< T > displayer;
	private Savior< T > savior;
	private Discarder< T > discarder;
	
	private EditorState state;
	private T original;
	private T copy;
	
	
	/**
	 * Create the editor with the object to be edited.
	 * @param obj
	 */
	public ObjectEditor(T obj) {
		displayer = new VoidDisplayer< T >();
		savior = new VoidSavior< T >();
		discarder = new VoidDiscarder< T >();
		state = EditorState.VIEWING;
		original = obj;
		copy = null;
	}
	
	/** Return a object, depending on the editor state. */
	public T getObject() {
		return isEditing() ? copy : original; 
	}
	
	public void setDisplayer(Displayer< T > displayer) {
		this.displayer = displayer;
	}

	public void setSavior(Savior< T > savior) {
		this.savior = savior;
	}

	public void setDiscarder(Discarder< T > discarder) {
		this.discarder = discarder;
	}

	/**
	 * Return {@code true} whether in editing state.
	 * @return
	 */
	public boolean isEditing() {
		return EditorState.EDITING == state;
	}

	/** Display */
	public void display() {
		if ( null == original ) { return; }
		getDisplayer().display( original, DisplayState.DISABLED );
	}
	
	/** Edit */
	@SuppressWarnings("unchecked")
	public void edit() {
		if ( EditorState.EDITING == state ) { return; }
		if ( null == original ) { return; }
		// Create a copy
		copy = (T) original.newCopy();
		// Enter editing mode
		state = EditorState.EDITING;
		// Display the copy enabled
		getDisplayer().display( copy, DisplayState.ENABLED );
	}
	
	/** Cancel 
	 * @throws Exception
	 * */
	public void cancel() throws Exception {
		if ( EditorState.VIEWING == state ) { return; }
		if ( null == copy ) { return; }
		// Discard the copy
		getDiscarder().discard( copy );
		copy = null;
		// Back to viewing state
		state = EditorState.VIEWING;
		// Redisplay the original disabled
		display();
	}
	
	/** Save 
	 * @throws Exception
	 * */
	public void save() throws Exception {
		if ( EditorState.VIEWING == state ) { return; }
		if ( null == copy ) { return; }
		// Save the copy
		getSavior().save( copy );
		// Saved successfully, then copy back
		original.copy( copy );
		// Destroy the copy
		copy = null;
		// Back to viewing state
		state = EditorState.VIEWING;
		// Redisplay the original disabled
		display();
	}

	//
	// PRIVATE
	//
	
	/** Return a non null displayer */
	private Displayer< T > getDisplayer() {
		if ( null == displayer ) {
			displayer = new VoidDisplayer< T >();
		}
		return displayer;
	}
	
	/** Return a non null savior */
	private Savior< T > getSavior() {
		if ( null == savior ) {
			savior = new VoidSavior< T >();
		}
		return savior;
	}
	
	/** Return a non null discarder */
	private Discarder< T > getDiscarder() {
		if ( null == discarder ) {
			discarder = new VoidDiscarder< T >();
		}
		return discarder;
	}
}
