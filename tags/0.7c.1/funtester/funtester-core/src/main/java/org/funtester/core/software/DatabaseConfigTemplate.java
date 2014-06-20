package org.funtester.core.software;

import java.util.HashMap;
import java.util.Map;

/**
 * Database configuration templates
 * 
 * @author Thiago Delgado Pinto
 * 
 * TODO: Add more configurations.
 * TODO: Specify additional classes.
 *
 */
public class DatabaseConfigTemplate {
	
	// Templates (alphabetically ordered)
	
	public static final DatabaseConfig FIREBIRD = new DatabaseConfig()
		.withType( "firebird" )
		.withDriver( "org.firebirdsql.Driver" )
		.withPort( 3050 )
		.withHost( "127.0.0.1" )
		.withUser( "SYSDBA" )
		.withPassword( "masterkey" )
		;	
	
	public static final DatabaseConfig MYSQL = new DatabaseConfig()
		.withType( "mysql" )
		.withDriver( "com.mysql.jdbc.Driver" )
		.withPort( 3306 )
		.withHost( "127.0.0.1" )
		.withUser( "root" )
		;
	
	public static final DatabaseConfig POSTGRESQL = new DatabaseConfig()
		.withType( "postgresql" )
		.withDriver( "org.postgresql.Driver" )
		.withPort( 5432 )
		.withHost( "127.0.0.1" )
		.withUser( "postgres" )
		;	
	
	// All configurations
	
	public static final DatabaseConfig ALL[] = {
		FIREBIRD, MYSQL, POSTGRESQL
	};
	
	public static final String ALL_NAMES[] = {
		"Firebird", "MySQL", "PostgreSQL"
	};	
	
	// Additional JDBC classes to load
	
	public static final String MYSQL_ADD_CLASSES[] = {
		/*
		"com.mysql.jdbc.ParameterBindings",
		*/
		
		/*
		"com.mysql.jdbc.StringUtils",
		"com.mysql.jdbc.Util",
		"com.mysql.jdbc.Messages",
		*/
	};
	
	public static final String FIREBIRD_ADD_CLASSES[] = { };
	
	public static final String POSTGRESQL_ADD_CLASSES[] = { };	
	
	// Additional classes map: type name -> classes
	public static final Map< String, String[] > ADDITIONAL_CLASSES_MAP;
	
	static {
		ADDITIONAL_CLASSES_MAP = new HashMap< String, String[] >();
		ADDITIONAL_CLASSES_MAP.put( FIREBIRD.getType(), FIREBIRD_ADD_CLASSES );
		ADDITIONAL_CLASSES_MAP.put( MYSQL.getType(), MYSQL_ADD_CLASSES );
		ADDITIONAL_CLASSES_MAP.put( POSTGRESQL.getType(), POSTGRESQL_ADD_CLASSES );
	}
}
