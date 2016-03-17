package org.funtester.core.software;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.funtester.common.util.Copier;
import org.funtester.common.util.EqUtil;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

/**
 * Database script to be executed in tests before each use case test method.
 * 
 * @author Thiago Delgado Pinto
 *
 */
@JsonIdentityInfo(generator=PropertyGenerator.class, property="id", scope=DatabaseScript.class)
public class DatabaseScript implements Copier< DatabaseScript > {

	private long id = 0;
	private String description = "";
	private List< String > commands = new ArrayList< String >();
	@JsonIdentityReference(alwaysAsId=true)
	private DatabaseConfig databaseConfig;
	
	public DatabaseScript() {
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List< String > getCommands() {
		return commands;
	}

	public void setCommands(List< String > commands) {
		this.commands = commands;
	}
	
	public boolean addCommand(String command) {
		return commands.add( command );
	}

	public DatabaseConfig getDatabaseConfig() {
		return databaseConfig;
	}

	public void setDatabaseConfig(DatabaseConfig databaseConfig) {
		this.databaseConfig = databaseConfig;
	}

	@Override
	public DatabaseScript copy(final DatabaseScript that) {
		this.description = that.description;
		this.commands.clear();
		this.commands.addAll( that.commands );		
		this.databaseConfig = that.databaseConfig; // Reference
		return this;
	}

	@Override
	public DatabaseScript newCopy() {
		return ( new DatabaseScript() ).copy( this );
	}
	
	@Override
	public String toString() {
		return description;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			id, description, commands, databaseConfig
		} );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof DatabaseScript ) ) return false;
		final DatabaseScript that = (DatabaseScript) obj;
		return
			EqUtil.equalsIgnoreCase( this.description, that.description )
			|| ( EqUtil.equals( this.commands, that.commands )
				&& EqUtil.equals( this.databaseConfig, that.databaseConfig ) )
			;
	}
}
