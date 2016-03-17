package org.funtester.core.software;

import java.util.Arrays;

import org.funtester.common.util.EqUtil;
import org.funtester.core.profile.StepKind;
import org.funtester.core.profile.Trigger;
import org.funtester.core.vocabulary.ActionNickname;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * A step that the user can fire an action.
 *
 * @author Thiago Delgado Pinto
 *
 */
public class ActionStep extends ElementBasedStep {

	private static final long serialVersionUID = -2879606740119088016L;

	private Trigger trigger = Trigger.ACTOR;

	@JsonIdentityReference(alwaysAsId=true)
	@JsonTypeInfo(defaultImpl=ActionNickname.class, use=Id.NAME)
	private ActionNickname actionNickname;

	public ActionStep() {
		super();
	}

	public ActionStep(Flow flow) {
		super( flow );
	}

	public ActionStep(Flow flow, Trigger trigger, ActionNickname aNickname) {
		this( flow );
		setTrigger( trigger );
		setActionNickname( aNickname );
	}

	public ActionNickname getActionNickname() {
		return actionNickname;
	}

	public void setActionNickname(ActionNickname actionNickname) {
		this.actionNickname = actionNickname;
	}

	@Override
	public Trigger trigger() {
		return getTrigger();
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
		ActionNickname nick = getActionNickname();
		if ( nick != null ) {
			if ( ! nick.actionTrigger().equals( trigger() ) ) {
				setActionNickname( null );
				getElements().clear();
			}
		}
	}

	@Override
	public StepKind kind() {
		return StepKind.ACTION;
	}

	@Override
	public String asSentence() {
		final StringBuilder sb = new StringBuilder();
		if ( getActionNickname() != null ) {
			sb.append( getActionNickname().getNickname() ).append( " " );
		}
		sb.append( elementsAsText() );
		return sb.toString();
	}

	@Override
	public ActionStep copy(final Step obj) {
		super.copy( obj );
		if ( obj instanceof ActionStep ) {
			final ActionStep that = (ActionStep) obj;
			this.trigger = that.trigger;
			this.actionNickname = that.actionNickname; // Reference
		}
		return this;
	}

	@Override
	public ActionStep newCopy() {
		return ( new ActionStep() ).copy( this );
	}

	@Override
	public int hashCode() {
		return super.hashCode() * Arrays.hashCode( new Object[] {
			this.actionNickname
			} );
	}

	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof ActionStep ) ) return false;
		final ActionStep that = (ActionStep) obj;
		return super.equals( that )
			&& EqUtil.equals( this.actionNickname, that.actionNickname )
			;
	}

	//
	// Not necessary to overwrite toString
	//
}
