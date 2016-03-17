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

	public static final ActionType DEFAULT_ACTION_TYPE = ActionType.WIDGET;

	private long id = 0;

	/** Name of the action. e.g. "click" */
	private String name = "";

	/** Who can trigger this action **/
	private Trigger triggers[] = {}; // NEW

	/** Categorizes the action **/
	private ActionType type = DEFAULT_ACTION_TYPE; // NEW

	// -------------------------------------------------------------------------
	private Trigger trigger = Trigger.SYSTEM; // LEGACY
	/** Target step kind **/
	private StepKind kind = StepKind.ACTION; // LEGACY
	// -------------------------------------------------------------------------

	/** The number of elements that the action can accept **/
	private int maxElements = 1;

	/** Whether the action can make the elements editable **/
	private boolean makeElementsEditable = false;

	/** The number of values that the action can accept **/
	private int maxValues = 0; // NEW

	/** Whether the action can serve as an oracle **/
	private boolean canServeAsOracle = false;


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
		this.name  = name;
	}

	public Trigger[] getTriggers() {
		return triggers;
	}

	public void setTriggers(Trigger triggers[]) {
		this.triggers = triggers;
	}

	/**
	 * Return {@code true} whether the action can be triggered by the given trigger.
	 * @param trigger
	 * @return
	 */
	public boolean canBeTriggeredBy(final Trigger trigger) {
		for ( Trigger t : this.triggers ) {
			if ( t.equals( trigger ) ) {
				return true;
			}
		}
		return false;
	}


	public ActionType getType() {
		return type;
	}

	public void setType(ActionType type) {
		this.type = type;
	}

	/**
	 * Return {@code true} whether the type is one of the given types.
	 * @param types
	 * @return
	 */
	public boolean typeIsOneOf(ActionType ... types) {
		for ( ActionType t : types ) {
			if ( this.type == t ) {
				return true;
			}
		}
		return false;
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

	public int getMaxValues() {
		return maxValues;
	}

	public void setMaxValues(int maxValues) {
		this.maxValues = maxValues;
	}

	public boolean getCanServeAsOracle() {
		return canServeAsOracle;
	}

	public void setCanServeAsOracle(boolean canServeAsOracle) {
		this.canServeAsOracle = canServeAsOracle;
	}

	@Override
	public Action copy(final Action that) {
		this.id = that.id;
		this.name = that.name;

		this.triggers = that.triggers;
		this.trigger = that.trigger;

		this.type = that.type;

		this.kind = that.kind;

		this.maxElements = that.maxElements;
		this.makeElementsEditable = that.makeElementsEditable;

		this.maxValues = that.maxValues;
		this.canServeAsOracle = that.canServeAsOracle;

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
			id
			, name
			, triggers	// NEW
			, trigger	// LEGACY
			, type		// NEW
			, kind		// LEGACY
			, maxElements
			, makeElementsEditable
			, maxValues	// NEW
			, canServeAsOracle // NEW
		} );
	}

	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof Action ) ) return false;
		Action that = (Action) obj;
		return // It is not necessary to compare the ids
			EqUtil.equalsIgnoreCase( this.name, that.name )

			&& EqUtil.equals( this.triggers, that.triggers )
			&& EqUtil.equals( this.type, that.type )

			// -- REMOVE ------------------------------------------------------
			&& EqUtil.equals( this.trigger, that.trigger )
			&& EqUtil.equals( this.kind, that.kind )
			// -----------------------------------------------------------------
			;
	}

}
