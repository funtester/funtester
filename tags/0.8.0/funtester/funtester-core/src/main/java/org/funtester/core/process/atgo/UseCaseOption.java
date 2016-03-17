package org.funtester.core.process.atgo;

import java.util.ArrayList;
import java.util.List;

import org.funtester.common.Importance;
import org.funtester.core.software.Flow;
import org.funtester.core.software.ImportanceCalculator;
import org.funtester.core.software.UseCase;

/**
 * Use case option 
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class UseCaseOption {
	
	private final UseCase useCase;
	private int maxRecursiveFlowRepetitions = 1;
	private boolean selected = true;
	
	/** Just for performance */
	private final Importance importance;

	
	public UseCaseOption(final UseCase useCase) {
		this.useCase = useCase;
		this.importance = averageImportanceOf( useCase.getFlows() );
	}
	
	private Importance averageImportanceOf(final List< Flow > flows) {
		List< Importance > importances = new ArrayList< Importance >();
		for ( Flow f : flows ) {
			importances.add( f.getImportance() );
		}
		return ImportanceCalculator.averageImportance( importances );
	}
	
	public UseCase getUseCase() {
		return useCase;
	}

	public String getName() {
		return useCase.getName();
	}
	
	public Importance getImportance() {
		return importance;
	}

	public int getMaxRecursiveFlowRepetitions() {
		return maxRecursiveFlowRepetitions;
	}

	public void setMaxRecursiveFlowRepetitions(int maxRecursiveFlowRepetitions) {
		this.maxRecursiveFlowRepetitions = maxRecursiveFlowRepetitions;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Return {@code true} if the importance is equals to the given one.
	 * @param other
	 * @return
	 */
	public boolean importanceIs(final Importance other) {
		return importance.equals( other );
	}
	
	/**
	 * Return {@code true} if the importance is equals to the given one.
	 * @param other
	 * @return
	 */
	public boolean importanceHigherThan(final Importance other) {
		return importance.higherThan( other );
	}
}
