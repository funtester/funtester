package org.funtester.core.software;

import java.util.Arrays;

import org.funtester.common.util.Copier;
import org.funtester.common.util.EqUtil;

/**
 * Driver template.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DatabaseDriverConfig implements Copier< DatabaseDriverConfig >{
	
	private String name = "";
	private String type = "";
	private String driverFile = "";
	private String driverClass = "";
	
	private int defaultPort = 0;
	private String defaultUser = "";
	private String defaultPassword = "";

	public DatabaseDriverConfig() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDriverFile() {
		return driverFile;
	}

	public void setDriverFile(String driverFile) {
		this.driverFile = driverFile;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public int getDefaultPort() {
		return defaultPort;
	}

	public void setDefaultPort(int port) {
		this.defaultPort = port;
	}
	
	public String getDefaultUser() {
		return defaultUser;
	}

	public void setDefaultUser(String defaultUser) {
		this.defaultUser = defaultUser;
	}

	public String getDefaultPassword() {
		return defaultPassword;
	}

	public void setDefaultPassword(String defaultPassword) {
		this.defaultPassword = defaultPassword;
	}

	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[]{
			name, type, driverFile, driverClass, defaultPort, defaultUser, defaultPassword } );
	}
	
	@Override
	public boolean equals(Object o) {
		if ( ! ( o instanceof DatabaseDriverConfig ) ) { return false; }
		DatabaseDriverConfig that = (DatabaseDriverConfig) o;
		return EqUtil.equalsIgnoreCase( this.driverFile, that.driverFile );
	}

	@Override
	public DatabaseDriverConfig copy(final DatabaseDriverConfig that) {
		this.name = that.name;
		this.type = that.type;
		this.driverFile = that.driverFile;
		this.driverClass = that.driverClass;
		this.defaultPort = that.defaultPort;
		this.defaultUser = that.defaultUser;
		this.defaultPassword = that.defaultPassword;
		return this;
	}

	@Override
	public DatabaseDriverConfig newCopy() {
		return ( new DatabaseDriverConfig() ).copy( this );
	}
}
