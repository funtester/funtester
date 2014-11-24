package org.funtester.common.at;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Action-based abstract test step.
 *
 * @see {@link AbstractTestStep}
 *
 * @author Thiago Delgado Pinto
 *
 */
public class AbstractTestActionStep extends AbstractTestStep {

	private static final long serialVersionUID = 4110972799050538201L;

	private List< AbstractTestElement > elements =
		new ArrayList< AbstractTestElement >();

	public AbstractTestActionStep() {
		super();
	}

	public AbstractTestActionStep(
			final long id,
			final long useCaseId,
			final long flowId,
			final long stepId,
			final String actionName
			) {
		super( id, useCaseId, flowId, stepId, actionName );
	}

	@JsonIgnore
	public AbstractTestStepKind kind() {
		return AbstractTestStepKind.ACTION;
	}

	public List< AbstractTestElement > getElements() {
		return elements;
	}

	public void setElements(List< AbstractTestElement > elements) {
		this.elements = elements;
	}

	public boolean addElement(AbstractTestElement element) {
		return elements.add( element );
	}

	public int numberOfElements() {
		return elements.size();
	}

	// FROM Copier

	public AbstractTestStep copy(final AbstractTestStep that) {
		super.copy( that );
		if ( that instanceof AbstractTestActionStep ) {
			this.elements.clear();
			this.elements.addAll( ((AbstractTestActionStep) that).elements );
		}
		return this;
	}

	public AbstractTestStep newCopy() {
		return ( new AbstractTestActionStep() ).copy( this );
	}

	@Override
	public int hashCode() {
		return super.hashCode() * 31 * Arrays.hashCode( new Object[] {
			elements
		} );
	}

	@Override
	public boolean equals(Object o) {
		if ( ! ( o instanceof AbstractTestActionStep ) ) {
			return false;
		}
		AbstractTestActionStep that = (AbstractTestActionStep) o;
		return super.equals( o )
			&& this.elements.equals( that.elements );
	}
}
