package org.funtester.core.software;

import java.util.Arrays;

import org.funtester.common.util.EqUtil;
import org.funtester.core.profile.ElementType;
import org.funtester.core.profile.StepKind;
import org.funtester.core.profile.Trigger;
import org.funtester.core.vocabulary.ActionNickname;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

/**
 * Oracle step is an action step with additional information about how the
 * expected message (according to the business rules) is about to be displayed
 * and what element type is used to display the message.
 *
 * @author Thiago Delgado Pinto
 *
 * @see OracleMessageOccurrence
 * @see ElementType
 *
 */
public class OracleStep extends ElementBasedStep {

	private static final long serialVersionUID = 5132259085508881624L;

	// Help to compose the sentence and to give the action for the plugin
	@JsonIdentityReference(alwaysAsId=true)
	private ActionNickname actionNickname;
	// How the message is displayed
	private OracleMessageOccurrence messageOccurrence = OracleMessageOccurrence.ONCE_FOR_THE_FIRST_ELEMENT;
	// What element type used to display the message
	@JsonIdentityReference(alwaysAsId=true)
	private ElementType elementType;

	public OracleStep() {
		super();
	}

	public OracleStep(Flow flow) {
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

	public OracleMessageOccurrence getMessageOccurrence() {
		return messageOccurrence;
	}

	public void setMessageOccurrence(OracleMessageOccurrence messageOccurrence) {
		this.messageOccurrence = messageOccurrence;
	}

	public ElementType getElementType() {
		return elementType;
	}

	public void setElementType(ElementType elementType) {
		this.elementType = elementType;
	}

	@Override
	public StepKind kind() {
		return StepKind.ORACLE;
	}

	@Override
	public String asSentence() {
		StringBuffer sb = new StringBuffer();
		if ( getActionNickname() != null ) {
			sb.append( getActionNickname().getNickname() ).append( " " );
		}
		sb.append( elementsAsText() );
		return sb.toString();
	}

	@Override
	public OracleStep copy(final Step obj) {
		super.copy( obj );
		if ( obj instanceof OracleStep ) {
			OracleStep that = (OracleStep) obj;
			this.actionNickname = that.actionNickname; // Reference
			this.messageOccurrence = that.messageOccurrence;

			if ( this.elementType != null && that.elementType != null ) {
				this.elementType.copy( that.elementType );
			} else {
				this.elementType = null;
				if ( that.elementType != null ) {
					this.elementType = that.elementType.newCopy();
				}
			}
		}
		return this;
	}

	@Override
	public OracleStep newCopy() {
		return ( new OracleStep() ).copy( this );
	}

	//
	// Not necessary to override toString()
	//

	@Override
	public int hashCode() {
		return super.hashCode() * Arrays.hashCode( new Object[] {
			actionNickname, messageOccurrence, elementType
		} );
	}

	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof OracleStep ) ) { return false; }
		final OracleStep that = (OracleStep) obj;
		return super.equals( that )
			&& EqUtil.equals( this.actionNickname, that.actionNickname )
			&& EqUtil.equals( this.messageOccurrence, that.messageOccurrence )
			&& EqUtil.equals( this.elementType, that.elementType )
			;
	}
}
