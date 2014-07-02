package org.funtester.core.software;

import java.util.Arrays;

import org.funtester.common.util.EqUtil;
import org.funtester.core.profile.StepKind;
import org.funtester.core.profile.Trigger;

/**
 * Documentation step.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DocStep extends Step {
	
	private static final long serialVersionUID = -7738509458754613211L;
	
	private Trigger trigger = Trigger.SYSTEM;
	private String sentence = "";

	public DocStep() {
		super();
	}
	
	public DocStep(Flow flow) {
		super( flow );
	}
	
	public DocStep(Flow flow, Trigger trigger) {
		this( flow );
		setTrigger( trigger );
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
	}

	public String getSentence() {
		return sentence;
	}
	
	public void setSentence(String value) {
		this.sentence = value;
	}
	
	@Override
	public StepKind kind() {
		return StepKind.DOC;
	}	

	@Override
	public String asSentence() {
		return getSentence();
	}

	@Override
	public boolean isPerformable() {
		return false;
	}
	
	@Override
	public DocStep copy(final Step obj) {
		super.copy( obj );
		if ( obj instanceof DocStep ) {
			final DocStep that = (DocStep) obj;
			this.trigger = that.trigger;
			this.sentence = that.sentence;
		}
		return this;
	}

	@Override
	public DocStep newCopy() {
		return ( new DocStep() ).copy( this );
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() * Arrays.hashCode( new Object[] { sentence } );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof DocStep ) ) return false;
		final DocStep that = (DocStep) obj;
		return super.equals( that )
			&& EqUtil.equals( this.trigger, that.trigger )
			&& EqUtil.equalsIgnoreCase( this.sentence, that.sentence )
			;
	}
	
	//
	// Not necessary to override toString()
	//
}
