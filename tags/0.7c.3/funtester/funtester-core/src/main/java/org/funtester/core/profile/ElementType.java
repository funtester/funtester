package org.funtester.core.profile;

import java.io.Serializable;
import java.util.Arrays;

import org.funtester.common.util.Copier;
import org.funtester.common.util.EqUtil;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

/**
 * Use to describe the type of an {@code Element}. It can be, for
 * instance, a button, a textbox, a combobox, a window, etc. 
 * 
 * @author Thiago Delgado Pinto
 *
 */
@JsonIdentityInfo(generator=PropertyGenerator.class, property="id", scope=ElementType.class)
public class ElementType
	implements Serializable, Copier< ElementType >, Comparable< ElementType > {

	private static final long serialVersionUID = -178287433188543611L;

	private long id = 0;
	private String name = "";
	private ElementKind kind = ElementKind.UNKNOWN;
	// Whether the element is editable or not (ie: a textbox is editable but a button is not).
	private boolean editable = false;
	
	
	public ElementType() {
	}
	
	public ElementType(String name, ElementKind kind) {
		this();
		setName( name );
		setKind( kind );
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ElementKind getKind() {
		return kind;
	}
	
	public void setKind(ElementKind kind) {
		this.kind = kind;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	public ElementType copy(final ElementType that) {
		this.name = that.name;
		this.kind = that.kind;
		this.editable = that.editable;
		return this;
	}

	@Override
	public ElementType newCopy() {
		return ( new ElementType() ).copy( this );
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof ElementType ) ) return false;
		ElementType that = (ElementType) obj;
		return // It is not necessary to compare the ids
			EqUtil.equalsIgnoreCase( this.name, that.name )
			&& EqUtil.equals( this.kind, that.kind )
			;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			id, name, kind, editable
		} );
	}

	@Override
	public int compareTo(ElementType o) {
		if ( null == name ) return -1;
		return name.compareTo( o.name );
	}
	
}