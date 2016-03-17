package org.funtester.core.software;

import java.util.Arrays;

import org.funtester.common.util.EqUtil;
import org.funtester.core.profile.StepKind;
import org.funtester.core.profile.Trigger;
import org.funtester.core.vocabulary.ActionNickname;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A step that calls a use case.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class UseCaseCallStep extends Step {

	private static final long serialVersionUID = 8856974092727120620L;

	// Gives the action for the plugin
	@JsonIdentityReference(alwaysAsId=true)
	private ActionNickname actionNickname;
	
	//
	// As per version 2.3.1, Jackson cannot solve forward references. So
	// as a workaround, the referencedElementId is kept. 
	//
	//@JsonIdentityReference(alwaysAsId=true)
	@JsonIgnore
	private UseCase useCase; // The use case to call
	// Workaround... :(
	private long referencedUseCaseId = 0;
	
	
	public UseCaseCallStep() {
	}

	public UseCaseCallStep(Flow flow) {
		super( flow );
	}
	
	@Override
	public Trigger trigger() {
		return Trigger.SYSTEM;
	}
	
	public ActionNickname getActionNickname() {
		return actionNickname;
	}

	public void setActionNickname(ActionNickname actionNickname) {
		this.actionNickname = actionNickname;
	}

	public UseCase getUseCase() {
		return useCase;
	}

	public void setUseCase(UseCase useCase) {
		this.useCase = useCase;
	}
	
	public long getReferencedUseCaseId() {
		return referencedUseCaseId;
	}

	public void setReferencedUseCaseId(long referencedUseCaseId) {
		this.referencedUseCaseId = referencedUseCaseId;
	}

	@Override
	public StepKind kind() {
		return StepKind.USE_CASE_CALL;
	}
	
	@Override
	public boolean isPerformable() {
		return true;
	}	
	
	@Override
	public String asSentence() {
		StringBuilder sb = new StringBuilder();
		if ( getActionNickname() != null ) {
			sb.append( getActionNickname().getNickname() ).append( " " );
		}
		if ( getUseCase() != null ) sb.append( getUseCase().getName() );
		return sb.toString();
	}	
	
	@Override
	public UseCaseCallStep copy(final Step obj) {
		super.copy( obj );
		if ( obj instanceof UseCaseCallStep ) {
			UseCaseCallStep that = (UseCaseCallStep) obj;
			this.actionNickname = that.actionNickname; // Reference
			this.useCase = that.useCase; // Reference
			this.referencedUseCaseId = that.referencedUseCaseId; // Workaround...
		}
		return this;
	}
	
	@Override
	public UseCaseCallStep newCopy() {
		return ( new UseCaseCallStep() ).copy( this );
	}
	
	//
	// Not necessary to override toString()
	//
	
	@Override
	public int hashCode() {
		// Do not use "useCase" to avoid recursion
		return super.hashCode() * Arrays.hashCode( new Object[] {
			actionNickname
		} );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof UseCaseCallStep ) ) return false;
		final UseCaseCallStep that = (UseCaseCallStep) obj;
		return super.equals( that )
			&& EqUtil.equals( this.actionNickname, that.actionNickname )
			&& EqUtil.equalsAdresses( this.useCase, that.useCase ) // Compare addresses to avoid recursion
			;
	}
}
