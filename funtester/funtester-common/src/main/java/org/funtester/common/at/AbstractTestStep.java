package org.funtester.common.at;

import java.io.Serializable;
import java.util.Arrays;

import org.funtester.common.util.Copier;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

/**
 * Abstract test step
 * 
 * @author Thiago Delgado Pinto
 *
 */
@JsonIdentityInfo( generator=PropertyGenerator.class, property="id", scope=AbstractTestStep.class )
@JsonTypeInfo( use=Id.NAME, include=As.PROPERTY, property="type" )
@JsonSubTypes( {
	@JsonSubTypes.Type( value=AbstractTestActionStep.class, name="action" ),
	@JsonSubTypes.Type( value=AbstractTestOracleStep.class, name="oracle" )
	} )
public class AbstractTestStep
	implements Serializable, Copier< AbstractTestStep > {

	private static final long serialVersionUID = -7389548894237848597L;
	
	/** Abstract step identification */
	private long id;
	
	/** Use case id used to keep track of the requirements */
	private long useCaseId;
	
	/** Flow id used to keep track of the requirements */
	private long flowId;
	
	/** Step id used to keep track of the requirements */
	private long stepId;
	
	/** Name of the action performed by the system or the user, according to
	 * the software's profile. */
	private String actionName; 
	
	
	public AbstractTestStep() {
	}
	
	public AbstractTestStep(
			final long id,
			final long useCaseId,
			final long flowId,
			final long stepId,
			final String actionName
			) {
		this();
		this.id = id;
		this.useCaseId = useCaseId;
		this.flowId = flowId;
		this.stepId = stepId;
		this.actionName = actionName;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUseCaseId() {
		return useCaseId;
	}

	public void setUseCaseId(long useCaseId) {
		this.useCaseId = useCaseId;
	}

	public long getFlowId() {
		return flowId;
	}

	public void setFlowId(long flowId) {
		this.flowId = flowId;
	}

	public long getStepId() {
		return stepId;
	}

	public void setStepId(long stepId) {
		this.stepId = stepId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public AbstractTestStep copy(final AbstractTestStep that) {
		this.id = that.id;
		this.useCaseId = that.useCaseId;
		this.flowId = that.flowId;
		this.stepId = that.stepId;
		this.actionName = that.actionName;
		return this;
	}
	
	public AbstractTestStep newCopy() {
		return ( new AbstractTestStep() ).copy( this );
	}
	
	@Override
	public String toString() {
		return actionName;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			useCaseId, flowId, stepId, actionName
		} );
	}
	
	@Override
	public boolean equals(Object o) {
		if ( ! ( o instanceof AbstractTestStep ) ) {
			return false;
		}
		AbstractTestStep that = (AbstractTestStep) o;
		return this.useCaseId == that.useCaseId
			&& this.flowId == that.flowId
			&& this.stepId == that.stepId
			&& this.actionName.equalsIgnoreCase( that.actionName )
			;
	}
}
