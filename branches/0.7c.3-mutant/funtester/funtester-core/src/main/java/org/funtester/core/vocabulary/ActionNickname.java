package org.funtester.core.vocabulary;

import java.io.Serializable;
import java.util.Arrays;

import org.funtester.common.util.Copier;
import org.funtester.common.util.EqUtil;
import org.funtester.core.profile.Action;
import org.funtester.core.profile.StepKind;
import org.funtester.core.profile.Trigger;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

/**
 * A nickname for an action.
 * 
 * @author Thiago Delgado Pinto
 *
 */
@JsonIdentityInfo(generator=PropertyGenerator.class, property="id", scope=ActionNickname.class)
public class ActionNickname
	implements
		Serializable
		, Copier< ActionNickname >
		, Comparable< ActionNickname > {

	private static final long serialVersionUID = 1508468164278080117L;
	
	private long id = 0;
	@JsonIdentityReference(alwaysAsId=true)
	private Action action;
	private String nickname = "";

	/**
	 * Default constructor.
	 */	
	public ActionNickname() {
	}
	
	/**
	 * Creates using a action and a nickname.
	 * 
	 * @param action	Action
	 * @param nickname	Nickname
	 */
	public ActionNickname(Action action, String nickname) {
		this();
		setAction( action );
		setNickname( nickname );
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Action getAction() {
		return action;
	}
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public StepKind actionKind() {
		checkAction();
		return getAction().getKind();
	}
	
	public String actionName() {
		checkAction();
		return getAction().getName();
	}

	public int maxElements() {
		checkAction();
		return getAction().getMaxElements();
	}

	public Trigger actionTrigger() {
		checkAction();
		return getAction().getTrigger();
	}
	
	private void checkAction() {
		if ( null == getAction() ) {
			throw new RuntimeException( "Has no defined Action" );
		}
	}
	
	@Override
	public ActionNickname copy(final ActionNickname that) {
		this.id = that.id;
		this.nickname = that.nickname;
		this.action = that.action; // Reference
		return this;
	}

	@Override
	public ActionNickname newCopy() {
		return ( new ActionNickname() ).copy( this );
	}
	
	@Override
	public String toString() {
		return getNickname();
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( !( obj instanceof ActionNickname ) ) return false;
		final ActionNickname that = (ActionNickname) obj;
		return // Not necessary to compare the ids
			EqUtil.equals( this.action, that.action )
			&& EqUtil.equalsIgnoreCase( this.nickname, that.nickname )
			;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			id, action, nickname
		} );
	}
	
	@Override
	public int compareTo(final ActionNickname o) {
		return getNickname().compareToIgnoreCase( o.getNickname() );
	}	
}
