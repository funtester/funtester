package org.funtester.core.software;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.funtester.common.util.EqUtil;
import org.funtester.common.util.ItemsParser;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

/**
 * An abstract step class that handles elements. Its possible children are
 * those whose action fires over elements, like an ActionStep or an OracleStep.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public abstract class ElementBasedStep extends Step {

	private static final long serialVersionUID = -3257057557744128368L;
	private static final String SEPARATOR = ", ";
	
	@JsonIdentityReference(alwaysAsId=true)
	private List< Element > elements = new ArrayList< Element >();
	
	public ElementBasedStep() {
		super();
	}
	
	public ElementBasedStep(Flow flow) {
		super( flow );
	}
	
	public List< Element > getElements() {
		return elements;
	}
	
	public void setElements(List< Element > elements) {
		this.elements = elements;
	}
	
	/**
	 * Adds a element to the step.
	 * 
	 * @param element	the element to be added.
	 * @return			true if added, false otherwise.
	 */
	public boolean addElement(Element element) {
		return elements.add( element );
	}
	
	/**
	 * Add more than one element.
	 * 
	 * @param elements	an array of elements.
	 */
	public void addElements(Element ...elements) {
		for ( Element element : elements ) {
			this.elements.add( element );
		}
	}
	
	/**
	 * Return true if contains a given element.
	 * @param e	the element to find.
	 * @return
	 */
	public boolean containsElement(Element e) {
		return elements.contains( e );
	}	
	
	/**
	 * Returns the elements as text.
	 * @return	the elements as text.
	 */
	public String elementsAsText() {
		return ItemsParser.textFromItems( SEPARATOR, getElements() );
	}	
	
	/**
	 * Remove all the elements.
	 */
	public void removeElements() {
		getElements().clear();
	}

	/**
	 * Returns the number of elements in the step.
	 * 
	 * @return	the number of elements in the step.
	 */
	public int numberOfElements() {
		return getElements().size();
	}
	
	
	/**
	 * Return the first element or null if there are no elements.
	 * @return
	 */
	public Element firstElement() {
		for ( Element ie : elements ) {
			return ie; // Return in the first item
		}
		return null;
	}	

	@Override
	public boolean isPerformable() {
		return true;
	}
	
	@Override
	public ElementBasedStep copy(final Step obj) {
		super.copy( obj );
		if ( obj instanceof ElementBasedStep ) {
			ElementBasedStep that = (ElementBasedStep) obj;
			this.elements.clear();
			this.elements.addAll( that.elements ); // Copy references
		}
		return this;
	}
	
	//
	// Can not override newCopy (abstract class)
	//
	
	//
	// Not necessary to override toString()
	//
	
	@Override
	public int hashCode() {
		return super.hashCode() * Arrays.hashCode( new Object[] { elements } );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof ElementBasedStep ) ) return false;
		final ElementBasedStep that = (ElementBasedStep) obj;
		return super.equals( obj )
			&& EqUtil.equals( this.elements, that.elements )
			;
	}
}
