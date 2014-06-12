package org.funtester.common.at;

/**
 * Abstract database connection
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class AbstractTestDatabaseConnection {

	private String name;
	private String driver;
	private String jdbcURL;
	private String user;
	private String password;
	
	public AbstractTestDatabaseConnection() {
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

	public String getJdbcURL() {
		return jdbcURL;
	}

	public void setJdbcURL(String jdbcURL) {
		this.jdbcURL = jdbcURL;
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
	
}
