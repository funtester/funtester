package org.funtester.core.software;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.funtester.common.util.Copier;
import org.funtester.common.util.CopierUtil;
import org.funtester.common.util.EqUtil;
import org.funtester.core.profile.ElementType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

/**
 * An element that the user can interact with (i.e.: a widget).
 * 
 * @author Thiago Delgado Pinto
 *
 */
@JsonIdentityInfo(generator=PropertyGenerator.class, property="id", scope=Element.class)
public class Element
	implements
		Serializable,
		Comparable< Element >,
		Copier< Element > {
	
	private static final long serialVersionUID = 2608871430383350681L;
	
	/** Identification */
	private long id = 0;
	
	/** A name that the user can understand */
	private String name = "";
	
	/** The widget name */
	private String internalName = "";
	
	@JsonIdentityReference(alwaysAsId=true)
	/** Element type (like a button, etc.) */
	private ElementType type = null;
	
	@JsonIdentityReference(alwaysAsId=true)
	@JsonBackReference
	/** The owner use case */
	private UseCase useCase = null;
	
	/** Whether the element is editable it can have a {@code ValueType}
	 * and a list of {@code BusinessRule}. */
	private boolean editable = false;
	
	/** For editable elements only */
	private ValueType valueType = ValueType.STRING;
	
	/** For editable elements only */
	private List< BusinessRule > businessRules = new ArrayList< BusinessRule >();
	
	
	public Element() {
	}
	
	public Element(String name) {
		this();
		setName( name );
	}
	
	/**
	 * Create a not editable element.
	 * 
	 * @param name
	 * @param internalName
	 * @param type
	 * @return
	 */
	public static Element createNotEditable(
			final String name,
			final String internalName,
			final ElementType type
			) {
		Element e = new Element( name );
		e.setInternalName( internalName );
		e.setType( type );
		return e;
	}
	
	/**
	 * Create an editable element.
	 * 
	 * @param name
	 * @param internalName
	 * @param type
	 * @param valueType
	 * @return
	 */
	public static Element createEditable(
			final String name,
			final String internalName,
			final ElementType type,
			final ValueType valueType
			) {
		Element e = createNotEditable( name, internalName, type );
		e.setEditable( true );
		e.setValueType( valueType );
		return e;
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
	
	public String getInternalName() {
		return internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}

	public ElementType getType() {
		return type;
	}

	public void setType(ElementType type) {
		this.type = type;
	}
	
	public String typeName() {
		return ( getType() != null ) ? getType().getName() : null;
	}
	
	public UseCase getUseCase() {
		return useCase;
	}

	public void setUseCase(UseCase useCase) {
		this.useCase = useCase;
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public ValueType getValueType() {
		return valueType;
	}
	
	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}

	public List< BusinessRule > getBusinessRules() {
		return businessRules;
	}

	public void setBusinessRules(List< BusinessRule > businessRules) {
		this.businessRules = businessRules;
	}
	
	/**
	 * Returns a business rule with the supplied type or null if not found.
	 * @param type	The business rule type to find.
	 * @return		The business rule or null if not found.
	 */
	public BusinessRule businessRuleWithType(BusinessRuleType type) {
		for ( BusinessRule br : businessRules ) {
			if ( br.getType().equals( type ) ) {
				return br;
			}
		}
		return null;
	}
	
	/**
	 * Add a business rule.
	 * @param br	The business rule to add.
	 * @return		true if added, false otherwise.
	 */
	public boolean addBusinessRule(BusinessRule br) {
		return businessRules.add( br );
	}
	
	/**
	 * A element is required if a business rule with the type
	 * {@link BusinessRuleType#REQUIRED} is defined.
	 * 
	 * @return if the element is required.
	 */
	@JsonIgnore
	public boolean isRequired() {
		return ( businessRuleWithType( BusinessRuleType.REQUIRED ) != null );
	}	

	@Override
	public Element copy(final Element that) {
		if ( this == that ) { return this; } // Leave if they are the same 
		this.id = that.id;
		this.name = that.name;
		this.internalName = that.internalName;
		this.type = that.type; // Reference
		this.useCase = that.useCase; // Reference
		this.editable = that.editable;
		this.valueType = that.valueType;
		CopierUtil.copyCollection( that.businessRules, this.businessRules );
		return this;
	}

	@Override
	public Element newCopy() {
		return ( new Element() ).copy( this );
	}
	
	@Override
	public int compareTo(final Element o) {
		return getName().compareToIgnoreCase( o.getName() );
	}	
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public int hashCode() {
		// Use case should not be used to avoid circular hashCode
		return Arrays.hashCode( new Object[] {
			id, name, internalName, type, valueType, businessRules
		} );
	}
	
	@Override
	public boolean equals(Object o) {
		if ( ! ( o instanceof Element ) ) return false;
		final Element that = (Element) o;
		return
			EqUtil.equals( this.useCase, that.useCase )
			&& EqUtil.equalsIgnoreCase( this.name, that.name )
			;
	}	
}
