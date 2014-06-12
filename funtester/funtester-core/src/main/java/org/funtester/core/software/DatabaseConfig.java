package org.funtester.core.software;

import java.io.Serializable;
import java.util.Arrays;

import org.funtester.common.util.Copier;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

/**
 * Database configuration used to connect to a database used by the software
 * and load its test values. 
 * 
 * @author Thiago Delgado Pinto
 *
 */
@JsonIdentityInfo(generator=PropertyGenerator.class, property="id", scope=DatabaseConfig.class)
public class DatabaseConfig
	implements Serializable, Copier< DatabaseConfig >,
	Comparable< DatabaseConfig > {

	private static final long serialVersionUID = -2276108570097796171L;

	public static final String JDBC_URL_FORMAT = "jdbc:%s://%s:%d/%s"; 	

	private long id = 0;
	private String name = "";		// i.e.: "My Database"

	private String driver = "";		// i.e.: "org.firebirdsql.jdbc.FBDriver"
	private String type = "";		// i.e.: "firebirdsql"
	
	private String dialect = "";	// i.e.: "org.hibernate.dialect.FirebirdDialect"
	private String host = "";		// i.e.: "127.0.0.1" 
	private int port = 0;			// i.e.: 3050
	private String path = "";		// i.e.: "C:/path/to/database.fdb"
	private String user = "";	
	private String password = "";
	
	public DatabaseConfig() {		
	}
	
	public DatabaseConfig(DatabaseConfig that) {
		copy( that );
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String toJdbcUrl() {		
		return String.format( JDBC_URL_FORMAT, getType(), getHost(), getPort(), getPath() );
	}

	@Override
	public DatabaseConfig copy(DatabaseConfig that) {
		this.id = that.id;
		this.name = that.name;
		this.driver = that.driver;
		this.dialect = that.dialect;
		this.type = that.type;
		this.host = that.host;
		this.port = that.port;
		this.path = that.path;
		this.user = that.user;
		this.password = that.password;
		return this;
	}

	@Override
	public DatabaseConfig newCopy() {
		return ( new DatabaseConfig() ).copy( this );
	}
	
	@Override
	public int compareTo(final DatabaseConfig o) {
		return name.compareTo( o.name );
	}	
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			id, name, driver, dialect, type, host, port, path, user, password
		} );
	}
	
	@Override
	public boolean equals(Object o) {
		if ( ! ( o instanceof DatabaseConfig ) ) {
			return false;
		}
		DatabaseConfig that = (DatabaseConfig) o;
		return this.driver.equalsIgnoreCase( that.driver )
			&& this.dialect.equalsIgnoreCase( that.dialect )
			&& this.type.equalsIgnoreCase( that.type )
			&& this.host.equalsIgnoreCase( that.host )
			&& this.port == that.port
			&& this.path.equalsIgnoreCase( that.path )
			&& this.user.equals( that.user )
			&& this.password.equals( that.password )
			;
	}
	
	@Override
	public String toString() {
		//return toJdbcUrl();
		return name;
	}
	
	// BUILDER METHODS
	
	public DatabaseConfig withName(final String value) {
		setName( value );
		return this;
	}

	public DatabaseConfig withDriver(final String value) {
		setDriver( value );
		return this;
	}
	
	public DatabaseConfig withDialect(final String value) {
		setDialect( value );
		return this;
	}
	
	public DatabaseConfig withType(final String value) {
		setType( value );
		return this;
	}
	
	public DatabaseConfig withHost(final String value) {
		setHost( value );
		return this;
	}
	
	public DatabaseConfig withPort(final int value) {
		setPort( value );
		return this;
	}
	
	public DatabaseConfig withPath(final String value) {
		setPath( value );
		return this;
	}
	
	public DatabaseConfig withUser(final String value) {
		setUser( value );
		return this;
	}
	
	public DatabaseConfig withPassword(final String value) {
		setPassword( value );
		return this;
	}	
}
