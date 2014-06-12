package org.funtester.core.process.usecase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.funtester.core.software.UseCase;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Graph based use cases' dependency analyzer.
 * 
 * @see UseCaseDependencyAnalyzer
 * @see JGraphT at https://github.com/jgrapht/
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class GraphBasedUseCaseDependencyAnalyzer implements UseCaseDependencyAnalyzer {
	
	private final static Logger logger = LoggerFactory.getLogger(
			GraphBasedUseCaseDependencyAnalyzer.class );
	
	/**
	 * Make a directed graph of use cases.
	 *  
	 * @param useCases	Collection of use cases to make the graph.
	 * @return			A directed graph.
	 */
	private DirectedGraph< UseCase, DefaultEdge > makeGraph(
			Collection< UseCase > useCases
			) {
		DirectedGraph< UseCase, DefaultEdge > graph =
			new DefaultDirectedGraph< UseCase, DefaultEdge >( DefaultEdge.class );		
		for ( UseCase uc : useCases ) {
			logger.debug( "makeGraph - ANALYZING: " + uc.getName() );
			graph.addVertex( uc ); // Use case as vertex			
			// HERE THE USE CASE DEPENDENCIES ARE GOT.
			Set< UseCase > dependencies = uc.useCaseDependencies();
			logger.debug( "makeGraph - # OF USE CASE DEPENDENCIES: " + dependencies.size() );
			for ( UseCase dep : dependencies ) {
				if ( ! graph.containsVertex( dep ) ) {
					graph.addVertex( dep );
				}
				graph.addEdge( dep, uc ); // IMPORTANT: dep BEFORE uc 
			}
		}
		return graph;
	}


	/* (non-Javadoc)
	 * @see software.UseCaseDependencyAnalyzer#topologicalOrder()
	 */
	public List< UseCase > topologicalOrder(Collection< UseCase > useCases) {
		DirectedGraph< UseCase, DefaultEdge > useCaseGraph = makeGraph( useCases );
		TopologicalOrderIterator< UseCase, DefaultEdge > iterator = 
			new TopologicalOrderIterator< UseCase, DefaultEdge >( useCaseGraph );
		List< UseCase > orderedUseCases = new ArrayList< UseCase >();
		while ( iterator.hasNext() ) {
			UseCase useCase = iterator.next();
			logger.debug( "topologicalOrder - ADDING " + useCase.getName() );
			orderedUseCases.add( useCase );
		}
		return orderedUseCases;
	}
	
	/* (non-Javadoc)
	 * @see software.UseCaseDependencyAnalyzer#cycles()
	 */
	public Set< UseCase > cycles(Collection< UseCase > useCases) {
		DirectedGraph< UseCase, DefaultEdge > useCaseGraph = makeGraph( useCases );
		CycleDetector< UseCase, DefaultEdge > detector =
			new CycleDetector< UseCase, DefaultEdge >( useCaseGraph );
		if ( ! detector.detectCycles() ) {
			return new LinkedHashSet< UseCase >(); // Empty
		}
		return detector.findCycles();
	}
}
