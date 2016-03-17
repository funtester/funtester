package org.funtester.core.software;

import java.util.ArrayList;
import java.util.List;

import org.funtester.common.util.ItemsParser;
import org.funtester.core.profile.ActionType;
import org.funtester.core.profile.StepKind;
import org.funtester.core.profile.Trigger;
import org.funtester.core.vocabulary.ActionNickname;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Step
 *
 * @author Thiago Delgado Pinto
 *
 */
public class NewStep extends Step {

	private static final long serialVersionUID = -9017714821144243635L;

	public static final String SEPARATOR = ", ";

	private Trigger trigger = Trigger.ACTOR;

	@JsonIdentityReference(alwaysAsId=true)
	private ActionNickname actionNickname = null;

	@JsonIdentityReference(alwaysAsId=true)
	private List< Element > elements = new ArrayList< Element >();

	private List< String > values = new ArrayList< String >();

	//
	// As per version 2.3.1, Jackson cannot solve forward references. So
	// as a workaround, the referencedElementId is kept.
	//
	//@JsonIdentityReference(alwaysAsId=true)
	@JsonIgnore
	private UseCase useCase = null;
	// Workaround... :(
	private long referencedUseCaseId = 0;


	public NewStep() {
		super();
	}

	public NewStep(Flow flow) {
		super( flow );
	}

	//
	// INHERITED
	//

	@Override
	public StepKind kind() {
		if ( null == getActionNickname() ) { return StepKind.DOC; }

		// LEGACY SUPPORT -----------------------------------------------------
		ActionType type = getActionNickname().getAction().getType();
		if ( ActionType.BUSINESS_RULE_CHECK == type ) {
			return StepKind.ORACLE;
		} else if ( ActionType.USE_CASE_CALL == type ) {
			return StepKind.USE_CASE_CALL;
		}
		// --------------------------------------------------------------------

		return StepKind.ACTION;
	}


	@Override
	public Trigger trigger() {
		return getTrigger();
	}


	@Override
	public String asSentence() { // LEGACY
		return "";
	}


	@Override
	public boolean isPerformable() {
		return getActionNickname() != null;
	}


	//
	// ATTRIBUTES
	//


	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

	public ActionNickname getActionNickname() {
		return actionNickname;
	}

	public void setActionNickname(ActionNickname actionNickname) {
		this.actionNickname = actionNickname;
	}

	public List< Element > getElements() {
		return elements;
	}

	public void setElements(List< Element > elements) {
		this.elements = elements;
	}

	/** Return the elements as text */
	public String elementsAsText() {
		return ItemsParser.textFromItems( SEPARATOR, elements );
	}

	public List< String > getValues() {
		return values;
	}

	public void setValues(List< String > values) {
		this.values = values;
	}

	/** Return the values as text */
	public String valuesAsText() {
		return ItemsParser.textFromItems( SEPARATOR, values );
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
	public NewStep copy(final Step other) {

		super.copy( other );

		NewStep that = (NewStep) other;
		if ( null == that ) {
			return this;
		}

		this.trigger = that.trigger;
		this.actionNickname = that.actionNickname; // reference

		this.elements.clear();
		this.elements.addAll( that.elements ); // references

		this.values.clear();
		this.values.addAll( that.values ); // copy

		this.useCase = that.useCase; // reference

		this.referencedUseCaseId = that.referencedUseCaseId; // Workaround

		return this;
	}

	@Override
	public NewStep newCopy() {
		return ( new NewStep() ).copy( this );
	}

}
