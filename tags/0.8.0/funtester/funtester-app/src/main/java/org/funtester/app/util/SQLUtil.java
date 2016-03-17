package org.funtester.app.util;

/**
 * SQL utilities
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class SQLUtil {
	
	private SQLUtil() {}
	
	// Just SQL, not DML or DDL
	public static final String SQL_WORDS[] = {
		"SELECT", 
		"FROM", 
		"WHERE", 
		"ORDER BY", 
		"GROUP BY", 
		"HAVING",
		"DISTINCT", 
		"AND", 
		"OR", 
		"NOT", 
		"LIKE", 
		"BETWEEN", 
		"INNER", 
		"OUTER", 
		"RIGHT", 
		"LEFT", 
		"JOIN" ,
		"IN", 
		"AS",
		"ON",
		"MAX",
		"MIN",
		"AVG",
		"SUM",
		"COUNT",
		"ASC",
		"ASCENDING",
		"DESC",
		"DESCENDING",
	};

}
