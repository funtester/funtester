package org.funtester.common.at;

import java.util.ArrayList;
import java.util.List;

import org.funtester.common.Importance;

/**
 * Test method for a given scenario.
 *  
 * @author Thiago Delgado Pinto
 */
public class AbstractTestMethod extends AbstractTest {

	private static final long serialVersionUID = 4080212910268354489L;
	
	/** Strategy used to generate the scenario. */
	private String strategyKind;
	
	/** A scenario name is typically composed by its flows. */
	private String scenarioName;
	
	/** The test expectation is important to fix test execution results. */
	private boolean expectedSuccess;
	
	/** Importance is useful to filter the tests to execute. Currently is
	 * calculated using the scenario importance value and the average
	 * importance for the business rules involved. */
	private Importance importance = Importance.MEDIUM;

	/** An abstract step can be transformed in one or more command lines */
	private List< AbstractTestStep > steps;
	
	
	public AbstractTestMethod() {
		super();
		steps = new ArrayList< AbstractTestStep >();
	}	
	
	public String getStrategyKind() {
		return strategyKind;
	}

	public void setStrategyKind(String strategyKind) {
		this.strategyKind = strategyKind;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public boolean isExpectedSuccess() {
		return expectedSuccess;
	}

	public void setExpectedSuccess(boolean expectedSuccess) {
		this.expectedSuccess = expectedSuccess;
	}

	public Importance getImportance() {
		return importance;
	}

	public void setImportance(Importance importance) {
		this.importance = importance;
	}

	public List< AbstractTestStep > getSteps() {
		return steps;
	}

	public void setSteps(List< AbstractTestStep > steps) {
		this.steps = steps;
	}
	
	// FROM Copier

	@Override
	public AbstractTest copy(AbstractTest obj) {
		super.copy( obj );
		if ( ! ( obj instanceof AbstractTestMethod ) ) {
			return this;
		}
		AbstractTestMethod that = (AbstractTestMethod) obj;
		
		this.strategyKind = that.strategyKind;
		this.scenarioName = that.scenarioName;
		this.expectedSuccess = that.expectedSuccess;
		this.importance = that.importance;
		this.steps.clear();
		this.steps.addAll( that.steps );
		
		return this;
	}

	@Override
	public AbstractTest newCopy() {
		return ( new AbstractTestMethod() ).copy( this );
	}
}