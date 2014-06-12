package org.funtester.core.software;

import java.io.Serializable;
import java.util.Arrays;

import org.funtester.common.util.Copier;
import org.funtester.common.util.EqUtil;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

/**
 * A query configuration allows to define a way to get the values from a
 * database to use as a business rule's value.
 * 
 * @author Thiago Delgado Pinto
 *
 */
@JsonIdentityInfo(generator=PropertyGenerator.class, property="id", scope=QueryConfig.class)
public final class QueryConfig
	implements Serializable, Copier< QueryConfig >{

	private static final long serialVersionUID = 4009095739571582351L;
	
	private long id = 0;
	@JsonIdentityReference(alwaysAsId=true)
	private DatabaseConfig databaseConfig = null;
	private String name = "";
	private String command = "";
	
	public QueryConfig() {
	}
	
	public QueryConfig(DatabaseConfig dbConfig, String name, String command) {
		this();
		setDatabaseConfig( dbConfig );
		setName( name );
		setCommand( command );
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(final long id) {
		this.id = id;
	}
	
	public DatabaseConfig getDatabaseConfig() {
		return databaseConfig;
	}
	
	public void setDatabaseConfig(DatabaseConfig databaseConfig) {
		this.databaseConfig = databaseConfig;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCommand() {
		return command;
	}
	
	public void setCommand(final String command) {
		this.command = command;
	}

	@Override
	public QueryConfig copy(final QueryConfig that) {
		this.id = that.id;
		this.name = that.name;
		this.databaseConfig = that.databaseConfig; // Reference
		this.command = that.command;
		return this;
	}

	@Override
	public QueryConfig newCopy() {
		return ( new QueryConfig() ).copy( this );
	}
	
	@Override
	public String toString() {
		//return command;
		return name;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			id, command, name
		} );
	}

	@Override
	public boolean equals(Object o) {
		if ( ! ( o instanceof QueryConfig ) ) return false;
		QueryConfig that = (QueryConfig) o;
		return // Do not compare the ids
			EqUtil.equalsAdresses( this.databaseConfig, that.databaseConfig ) // Compare address
			&& ( EqUtil.equalsIgnoreCase( this.command, that.command )
				|| EqUtil.equalsIgnoreCase( this.name, that.name ) )
			;
	}
}
