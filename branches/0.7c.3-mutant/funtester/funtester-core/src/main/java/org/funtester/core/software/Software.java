package org.funtester.core.software;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.funtester.common.util.Copier;
import org.funtester.common.util.CopierUtil;
import org.funtester.common.util.EqUtil;
import org.funtester.core.profile.Profile;
import org.funtester.core.vocabulary.Vocabulary;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Software
 *
 * @author Thiago Delgado Pinto
 *
 */
public class Software
	implements Serializable, Copier< Software > {

	private static final long serialVersionUID = -8104207198929111603L;

	private String name = "";
	@JsonIgnore
	private Vocabulary vocabulary = null;
	private List< Actor > actors = new ArrayList< Actor >();
	private List< RegEx > regularExpressions = new ArrayList< RegEx >();
	private List< DatabaseConfig > databaseConfigurations = new ArrayList< DatabaseConfig >();
	private List< QueryConfig > queryConfigurations = new ArrayList< QueryConfig >();
	private List< UseCase > useCases = new ArrayList< UseCase >();
	// Last id for many objects controlled in the application.
	// The used pair is: (class name, last id)
	private Map< String, AtomicInteger > lastIds = new HashMap< String, AtomicInteger >();


	public Software() {
	}

	/**
	 * Constructs a software with a name.
	 *
	 * @param name	the software name.
	 */
	public Software(String name) {
		this();
		setName( name );
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vocabulary getVocabulary() {
		return vocabulary;
	}

	public void setVocabulary(Vocabulary vocabulary) {
		this.vocabulary = vocabulary;
	}

	/**
	 * Get the actors related to the use case.
	 *
	 * @return	A collection of actors.
	 */
	public List< Actor > getActors() {
		return actors;
	}

	public void setActors(List< Actor > actors) {
		if ( null == actors ) {
			throw new IllegalArgumentException( "'actors' should not be null." );
		}
		this.actors = actors;
	}

	/**
	 * Add a actor.
	 *
	 * @param actor	Actor to be added.
	 * @return		<code>true</code> if successfully added.
	 */
	public boolean addActor(Actor actor) {
		if ( actors.contains( actor ) ) {
			return false;
		}
		return actors.add( actor );
	}

	/**
	 * Get the actor with a given name.
	 *
	 * @param name	Actor's name.
	 * @return		The <code>Actor</code> or <code>null</code> if not found.
	 */
	public Actor actorWithName(String name) {
		for ( Actor actor : actors ) {
			if ( actor.getName().equalsIgnoreCase( name ) ) {
				return actor;
			}
		}
		return null;
	}

	public Actor actorAt(int index) {
		return actors.get( index );
	}

	public int indexOfActor(Actor a) {
		return actors.indexOf( a );
	}

	/**
	 * Removes a actor.
	 *
	 * @param actor	Actor to be removed.
	 * @return		<code>true</code> if successfully removed.
	 */
	public boolean removeActor(Actor actor) {
		return actors.remove( actor );
	}

	public List< RegEx > getRegularExpressions() {
		return regularExpressions;
	}

	public void setRegularExpressions(List< RegEx > regularExpressions) {
		this.regularExpressions = regularExpressions;
	}

	/**
	 * Add a regular expression.
	 *
	 * @param obj	the regular expression to add.
	 * @return		<code>true</code> if added, <code>false</code> otherwise.
	 */
	public boolean addRegularExpression(RegEx obj) {
		return regularExpressions.add( obj );
	}

	/**
	 * Return <code>true</code> whether the software contains a given
	 * regular expression.
	 *
	 * @param obj	the regular expression to verify.
	 * @return
	 */
	public boolean containsRegularExpression(RegEx obj) {
		return regularExpressions.contains( obj );
	}

	public List< DatabaseConfig > getDatabaseConfigurations() {
		return databaseConfigurations;
	}

	public void setDatabaseConfigurations(
			List< DatabaseConfig > databaseConfigurations) {
		this.databaseConfigurations = databaseConfigurations;
	}

	/**
	 * Add a database configuration.
	 *
	 * @param dbc	the database configuration to add.
	 * @return		<code>true</code> if removed, <code>false</code> otherwise.
	 */
	public boolean addDatabaseConfiguration(DatabaseConfig dbc) {
		return databaseConfigurations.add( dbc );
	}

	/**
	 * Remove a database configuration.
	 *
	 * @param dbc	the database configuration to remove.
	 * @return		<code>true</code> if removed, <code>false</code> otherwise.
	 */
	public boolean removeDatabaseConfiguration(DatabaseConfig dbc) {
		removeAllQueriesForDatabase( dbc );
		return databaseConfigurations.remove( dbc );
	}

	private void removeAllQueriesForDatabase(DatabaseConfig dbc) {
		Iterator< QueryConfig > it = queryConfigurations.iterator();
		while ( it.hasNext() ) {
			QueryConfig qc = it.next();
			if ( qc.getDatabaseConfig().equals( dbc ) ) {
				it.remove();
			}
		}
	}

	public DatabaseConfig databaseConfigurationAt(final int index) {
		return databaseConfigurations.get( index );
	}

	public DatabaseConfig databaseConfigurationWithId(final long id) {
		for ( DatabaseConfig dc : databaseConfigurations ) {
			if ( dc.getId() == id ) {
				return dc;
			}
		}
		return null;
	}

	public int numberOfDatabaseConfigurations() {
		return databaseConfigurations.size();
	}

	public int indexOfDatabaseConfiguration(DatabaseConfig o) {
		return databaseConfigurations.indexOf( o );
	}

	public List< QueryConfig > getQueryConfigurations() {
		return queryConfigurations;
	}

	public void setQueryConfigurations(List< QueryConfig > queryConfigurations) {
		this.queryConfigurations = queryConfigurations;
	}

	/**
	 * Add a query configuration.
	 * @param qc	the query configuration to add.
	 * @return		<code>true</code> if added, <code>false</code> otherwise.
	 */
	public boolean addQueryConfiguration(QueryConfig qc) {
		return queryConfigurations.add( qc );
	}

	/**
	 * Remove a query configuration.
	 * @param qc	the query configuration to remove.
	 * @return		<code>true</code> if added, <code>false</code> otherwise.
	 */
	public boolean removeQueryConfiguration(QueryConfig qc) {
		return queryConfigurations.remove( qc );
	}

	public QueryConfig queryConfigurationAt(final int index) {
		return queryConfigurations.get( index );
	}

	public int numberOfQueryConfigurations() {
		return queryConfigurations.size();
	}

	public int indexOfQueryConfiguration(QueryConfig o) {
		return queryConfigurations.indexOf( o );
	}

	/**
	 * Return a list with all the <code>QueryConfig</code>s that uses a given
	 * <code>DatabaseConfig</code>.
	 *
	 * @param dbc	the database configuration.
	 * @return		a list.
	 */
	public List< QueryConfig > queryConfigurationsForDatabase(
			final DatabaseConfig dbc
			) {
		List< QueryConfig > queries = new ArrayList< QueryConfig >();
		for ( QueryConfig qc : queryConfigurations ) {
			if ( qc.getDatabaseConfig().equals( dbc ) ) {
				queries.add( qc );
			}
		}
		return queries;
	}

	/**
	 * Return a query configuration for a certain <code>DatabaseConfig</code>,
	 * at a certain index.
	 *
	 * @param dbc	database configuration.
	 * @param index	query index.
	 * @return		the query configuration or <code>null</code> if not found.
	 */
	public QueryConfig queryConfigurationForDatabase(
			final DatabaseConfig dbc,
			final int index
			) {
		List< QueryConfig > queries = queryConfigurationsForDatabase( dbc );
		if ( index < 0 || index >= queries.size() ) {
			return null;
		}
		return queries.get( index );
	}

	public int numberOfQueryConfigurationsForDatabase(DatabaseConfig dbc) {
		return queryConfigurationsForDatabase( dbc ).size();
	}

	public List< UseCase > getUseCases() {
		return this.useCases;
	}

	public void setUseCases(List< UseCase > useCases) {
		if ( null == useCases ) {
			throw new IllegalArgumentException( "'useCases' should not be null." );
		}
		this.useCases = useCases;
	}

	/**
	 * Return the number of use cases.
	 *
	 * @return	the number of use cases.
	 */
	public int numberOfUseCases() {
		return useCases.size();
	}

	/**
	 * Return the use case at a given index.
	 *
	 * @param index	Position
	 * @return		A <code>UseCase</code> or <code>null</code> if not found.
	 */
	public UseCase useCaseAt(int index) {
		return useCases.get( index );
	}

	/**
	 * Return the index of a given use case.
	 *
	 * @param useCase	Use case to verify.
	 * @return			The index or -1 if not found.
	 */
	public int indexOfUseCase(UseCase useCase) {
		return useCases.indexOf( useCase );
	}

	/**
	 * Verifies if the software contains a use case.
	 *
	 * @param useCase	Use case to verify.
	 * @return			<code>true</code> if it contains.
	 */
	public boolean containsUseCase(UseCase useCase) {
		return useCases.contains( useCase );
	}

	/**
	 * Add a use case.
	 *
	 * @param useCase	Use case to be added.
	 * @return			<code>true</code> if successfully added,
	 */
	public boolean addUseCase(UseCase useCase) {
		if ( containsUseCase( useCase ) ) {
			return false;
		}
		return useCases.add( useCase );
	}

	/**
	 * Removes the use case.
	 *
	 * @param useCase	Use case to be removed.
	 * @return			<code>true</code> if successfully removed.
	 */
	public boolean removeUseCase(UseCase useCase) {
		return useCases.remove( useCase );
	}

	/**
	 * Return the use case with a given name or null if not found.
	 *
	 * @param aName	the name of the use case.
	 * @return		the use case or null if not found.
	 */
	public UseCase useCaseWithName(String aName) {
		for ( UseCase uc : useCases ) {
			if ( aName.equalsIgnoreCase( uc.getName() ) ) {
				return uc;
			}
		}
		return null;
	}

	/**
	 * Return the use case with a given id or null if not found.
	 *
	 * @param anId	the id of the use case.
	 * @return		the use case or null if not found.
	 */
	public UseCase useCaseWithId(final long anId) {
		for ( UseCase uc : useCases ) {
			if ( anId == uc.getId() ) {
				return uc;
			}
		}
		return null;
	}


	/*
	public int indexOfQueryConfigurationForDatabase(
			final DatabaseConfig dbc, final QueryConfig qc) {
		for ( QueryConfig q : queryConfigurations ) {

		}
		return queryConfigurations.indexOf( o );
	}	*/

	public Map< String, AtomicInteger > getLastIds() {
		return lastIds;
	}

	public void setLastIds(Map< String, AtomicInteger > lastIds) {
		this.lastIds = lastIds;
	}

	public AtomicInteger idFor(final String key) {
		AtomicInteger i = lastIds.get( key );
		if ( null == i ) {
			i = new AtomicInteger( 0 );
			lastIds.put( key, i );
		}
		return i;
	}

	public long generateIdFor(final String key) {
		return idFor( key ).incrementAndGet();
	}

	/** Return the vocabulary's profile or {@code null}. */
	public Profile profile() {
		return getVocabulary() != null ? getVocabulary().getProfile() : null;
	}

	@Override
	public Software copy(final Software that) {
		this.name = that.name;
		this.vocabulary = that.vocabulary; // Reference
		CopierUtil.copyCollection( that.useCases, this.useCases );
		CopierUtil.copyCollection( that.actors, this.actors );
		CopierUtil.copyCollection( that.regularExpressions, this.regularExpressions );
		CopierUtil.copyCollection( that.databaseConfigurations, this.databaseConfigurations );
		CopierUtil.copyCollection( that.queryConfigurations, this.queryConfigurations );
		this.lastIds.clear();
		this.lastIds.putAll( that.lastIds );

		return this;
	}

	@Override
	public Software newCopy() {
		return ( new Software() ).copy( this );
	}


	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			name,
			vocabulary,
			useCases,
			actors,
			regularExpressions,
			databaseConfigurations,
			queryConfigurations,
			lastIds
		} );
	}

	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof Software ) ) return false;
		Software that = (Software) obj;
		// Just compare the name
		return EqUtil.equalsIgnoreCase( this.name, that.name );
	}

}