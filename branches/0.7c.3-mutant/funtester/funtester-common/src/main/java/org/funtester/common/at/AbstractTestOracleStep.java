package org.funtester.common.at;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Oracle-based abstract test step
 * 
 * @see {@link AbstractTestStep}
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class AbstractTestOracleStep extends AbstractTestStep {

	private static final long serialVersionUID = 4498256866252978916L;
	
	private List< String > messages = new ArrayList< String >();
	
	public AbstractTestOracleStep() {
		super();
	}
	
	public AbstractTestOracleStep(
			final long id,
			final long useCaseId,
			final long flowId,
			final long stepId,
			final String actionName
			) {
		super( id, useCaseId, flowId, stepId, actionName );
	}

	public List< String > getMessages() {
		return messages;
	}

	public void setMessages(List< String > messages) {
		this.messages = messages;
	}

	@Override
	public AbstractTestStep copy(final AbstractTestStep that) {
		super.copy( that );
		if ( that instanceof AbstractTestOracleStep ) {
			this.messages.clear();
			this.messages.addAll( ((AbstractTestOracleStep) that).messages );
		}
		return this;
	}

	@Override
	public AbstractTestStep newCopy() {
		return ( new AbstractTestOracleStep() ).copy( this );
	}	
	
	@Override
	public int hashCode() {
		return super.hashCode() * 31 * Arrays.hashCode( new Object[] {
			messages
		} );
	}
	
	@Override
	public boolean equals(Object o) {
		if ( ! ( o instanceof AbstractTestOracleStep ) ) {
			return false;
		}
		AbstractTestOracleStep that = (AbstractTestOracleStep) o;
		return super.equals( o )
			&& this.messages.equals( that.messages );
	}
}
