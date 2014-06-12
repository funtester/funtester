package org.funtester.core.profile;

import java.io.Serializable;
import java.util.Arrays;

import org.funtester.common.util.Copier;
import org.funtester.common.util.EqUtil;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

/**
 * An action that is triggered by the system or the user in a step.
 * 
 * @author Thiago Delgado Pinto
 *
 */
@JsonIdentityInfo(generator=PropertyGenerator.class, property="id", scope=Action.class)
public class Action
	implements Serializable, Copier< Action > {

	private static final long serialVersionUID = 1385926738638243628L;
	
	private long id = 0;
	private String name = "";
	private Trigger trigger = Trigger.SYSTEM;
	private StepKind kind = StepKind.ACTION;
	// The number of elements that the action can accept
	private int maxElements = 1;
	// Whether the action can make the elements editable
	private boolean makeElementsEditable = false;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Action() {
	}
	
	public Action(String name) {
		this();
		setName( name );
	}	

	public Action(String name, Trigger trigger) {
		this( name );
		setTrigger( trigger );
	}

	public Action(String name, Trigger trigger, StepKind kind) {
		this( name, trigger );
		setKind( kind );
	}	

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name  = name;		
	}
	
	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

	public StepKind getKind() {
		return kind;
	}

	public void setKind(StepKind kind) {
		this.kind = kind;
	}

	public int getMaxElements() {
		return maxElements;
	}

	public void setMaxElements(int maxElements) {
		this.maxElements = maxElements;
	}
	
	public boolean getMakeElementsEditable() {
		return makeElementsEditable;
	}

	public void setMakeElementsEditable(boolean makeElementsEditable) {
		this.makeElementsEditable = makeElementsEditable;
	}

	@Override
	public Action copy(final Action that) {
		this.id = that.id;
		this.trigger = that.trigger;
		this.name = that.name;
		this.kind = that.kind;		
		this.maxElements = that.maxElements;
		this.makeElementsEditable = that.makeElementsEditable;
		return this;
	}

	@Override
	public Action newCopy() {
		return ( new Action() ).copy( this );
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			id, name, trigger, kind, maxElements, makeElementsEditable
		} );
	}	
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof Action ) ) return false;
		Action that = (Action) obj;
		return // It is not necessary to compare the ids
			EqUtil.equalsIgnoreCase( this.name, that.name )
			&& EqUtil.equals( this.trigger, that.trigger )
			&& EqUtil.equals( this.kind, that.kind )
			;
	}
	
}
