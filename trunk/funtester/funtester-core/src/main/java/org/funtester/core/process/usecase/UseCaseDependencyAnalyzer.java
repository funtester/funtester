package org.funtester.core.process.usecase;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.funtester.core.software.UseCase;

/**
 * Analyzes use cases' dependencies.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface UseCaseDependencyAnalyzer {

	/**
	 * Return the use cases in topological order.
	 * 
	 * @param useCases	Use cases to be analyzed.
	 * @return			Use cases in topological order.
	 */
	List< UseCase > topologicalOrder(Collection< UseCase > useCases);

	/**
	 * Cycles detected in use cases' dependencies.
	 * 
	 * @param useCases	Use cases to be analyzed.
	 * @return			Cycled use cases.
	 */
	Set< UseCase > cycles(Collection< UseCase > useCases);

}