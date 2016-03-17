package org.funtester.tests;

import static org.testng.Assert.*;
import org.funtester.app.util.editor.ObjectEditor;
import org.funtester.core.software.Software;
import org.testng.annotations.Test;

/**
 * Test {@link ObjectEditor}
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ObjectEditorTest {

	@Test
	public void enterEditMode() {
		Software s = new Software( "xpto" );
		ObjectEditor< Software > e = new ObjectEditor< Software >( s );
		assertFalse( e.isEditing() );
		e.edit();
		assertTrue( e.isEditing() );
	}
	
	@Test
	public void savedObjectContineTheSameObjectButDifferentValue() throws Exception {
		Software s = new Software( "xpto" );
		int hash = System.identityHashCode( s );
		
		ObjectEditor< Software > e = new ObjectEditor< Software >( s );
		e.edit();		
		e.getObject().setName( "xpto v2" );
		e.save();
		
		Software s2 = (Software) e.getObject();
		int hash2 = System.identityHashCode( s2 );
		
		assertEquals( hash, hash2 );
		assertEquals( e.getObject().getName(), "xpto v2" );
	}
	
	@Test
	public void canceledObjectContineTheSameObjectAndValue() throws Exception {
		Software s = new Software( "xpto" );
		int hash = System.identityHashCode( s );
		
		ObjectEditor< Software > e = new ObjectEditor< Software >( s );
		e.edit();
		e.getObject().setName( "xpto v2" );
		e.cancel();
		
		Software s2 = (Software) e.getObject();
		int hash2 = System.identityHashCode( s2 );
		
		assertEquals( hash, hash2 );
		assertEquals( e.getObject().getName(), "xpto" );
	}
}
