package org.funtester.core.software;

import java.util.Arrays;

import org.funtester.common.util.EqUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Value configuration based on the value of an existing element. 
 *  
 * @author Thiago Delgado Pinto
 *
 */
public class ElementBasedVC extends ValueConfiguration {

	//
	// As per version 2.3.1, Jackson cannot solve forward references. So
	// as a workaround, the referencedElementId is kept. 
	//
	//@JsonIdentityReference(alwaysAsId=true)
	@JsonIgnore
	private Element referencedElement = null;
	// Workaround... :(
	private long referencedElementId = 0;
	
	public ElementBasedVC() {
	}
	
	/**
	 * Constructs the configuration with the referenced element.
	 * 
	 * @param referencedElement the referenced element.
	 */
	public ElementBasedVC(Element referencedElement) {
		setReferencedElement( referencedElement );
	}
	
	@Override
	public ValueConfigurationKind kind() {
		return ValueConfigurationKind.ELEMENT_BASED;
	}
	
	public Element getReferencedElement() {
		return referencedElement;
	}
	
	public void setReferencedElement(Element element) {
		this.referencedElement = element;
		if ( this.referencedElement != null ) {
			this.referencedElementId = this.referencedElement.getId();
		}
	}
	
	public long getReferencedElementId() { return referencedElementId; }
	public void setReferencedElementId(long id) { referencedElementId = id; }

	@Override
	public ElementBasedVC copy(final ValueConfiguration obj) {
		super.copy( obj );
		if ( obj instanceof ElementBasedVC ) {
			final ElementBasedVC that = (ElementBasedVC) obj;
			this.referencedElement = that.referencedElement; // Reference
			this.referencedElementId = that.referencedElementId;
		}
		return this;
	}

	@Override
	public ElementBasedVC newCopy() {
		return ( new ElementBasedVC() ).copy( this );
	}
	
	@Override 
	public String toString() {
		return ( referencedElement != null ) 
			? "referencedElement: " + referencedElement.toString() : "";
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			kind(), referencedElement 
		} );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof ElementBasedVC ) ) return false;
		final ElementBasedVC that = (ElementBasedVC) obj;
		return EqUtil.equals( this.kind(), that.kind() )
			&& EqUtil.equals( this.referencedElement, that.referencedElement )
			;
	}
}
