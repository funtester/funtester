package org.funtester.core.software;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.funtester.common.util.CopierUtil;
import org.funtester.common.util.EqUtil;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

/**
 * Value configuration based on a database query.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class QueryBasedVC extends ValueConfiguration {
	
	@JsonIdentityReference(alwaysAsId=true)
	private QueryConfig queryConfig = null;
	private String targetColumn = "";
	private ValueType targetColumnType = ValueType.STRING;
	private List< ParameterConfig > parameters = new ArrayList< ParameterConfig >();
	
	public QueryBasedVC() {
	}
	
	@Override
	public ValueConfigurationKind kind() {
		return ValueConfigurationKind.QUERY_BASED;
	}
		
	public QueryConfig getQueryConfig() {
		return queryConfig;
	}

	public void setQueryConfig(QueryConfig queryConfig) {
		this.queryConfig = queryConfig;
	}
	
	public String getTargetColumn() {
		return targetColumn;
	}

	public void setTargetColumn(String targetColumn) {
		this.targetColumn = targetColumn;
	}

	public ValueType getTargetColumnType() {
		return targetColumnType;
	}

	public void setTargetColumnType(ValueType targetColumnType) {
		this.targetColumnType = targetColumnType;
	}
	
	public List< ParameterConfig > getParameters() {
		return parameters;
	}

	public void setParameters(List< ParameterConfig > parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Adds a parameter configuration.
	 * 
	 * @param parameterConfig	the parameter configuration to add.
	 * @return					true if added, false otherwise.
	 */
	public boolean addParameter(ParameterConfig parameterConfig) {
		return this.parameters.add( parameterConfig );
	}
	

	public void clearParameters() {
		parameters.clear();
	}	
	

/*	
	public Object getValue() throws Exception {
		if ( null == queryConfig ) {
			return null;		
		}
		// TODO: Refactor
		DatabaseConfig dbC = queryConfig.getDatabaseConfig();
		Connection connection = DriverManager.getConnection(
				dbC.getAsJdbcUrl(),
				dbC.getUser(),
				dbC.getPassword()
				);
		PreparedStatement stmt = connection.prepareStatement( queryConfig.getCommand() );
		
		int count = queryConfig.getParametersCount();
		int valuesCount = parameterValues.size();
		if ( count != valuesCount ) {
			throw new RuntimeException( String.format( "%d parameters needed. %d found.", count, valuesCount ) );
		}
		for ( int i = 0; ( i < count ); ++i ) {
			ValueType type = queryConfig.getParameterType( i );
			Object value = parameterValues.get( i ).getValue();
			System.out.println( "Values is " + value );
			int paramIndex = i + 1;
			switch ( type ) {
				case INTEGER	: stmt.setInt( paramIndex, (Integer) value ); break;
				case DOUBLE		: stmt.setDouble( paramIndex, (Double) value ); break;
				case BOOLEAN	: stmt.setBoolean( paramIndex, (Boolean) value ); break;
				case DATE		: stmt.setDate( paramIndex, (java.sql.Date) value ); break;
				case TIME		: stmt.setTime( paramIndex, (java.sql.Time) value ); break;
				case DATETIME	: stmt.setTimestamp( paramIndex, (java.sql.Timestamp) value ); break;
				default			: stmt.setString( paramIndex, (String) value );
			}			
		}
		
		// stmt.getParameterMetaData(). <<< TO GET PARAMETER DETAILS
		System.out.println( "Parameters expected: "
				+ stmt.getParameterMetaData().getParameterCount()
				+ " Type: "
				+ stmt.getParameterMetaData().getParameterTypeName( 1 )
				);
		
		ResultSet rs = stmt.executeQuery();
		if ( ! rs.next() ) {
			throw new RuntimeException( "No data returned from the query." );
		}		
		if ( rs.findColumn( targetColumn ) < 0 ) {
			throw new RuntimeException( "targetColumn not found: " + targetColumn );
		}
		
		// Add values to a list to make a random choice			
		List< Object > values = new ArrayList< Object >();
		do {
			values.add( getTargetColumnValue( rs ) );			
		} while ( rs.next() );
		int randomIndex = ( new Random() ).nextInt( values.size() );
		return values.get( randomIndex );
	}
	
	private Object getTargetColumnValue(ResultSet rs) throws SQLException {
		return rs.getObject( targetColumn );
		/*
		switch ( getTargetColumnType() ) {
			case INTEGER	: return rs.getInt( targetColumn );
			case DOUBLE		: return rs.getDouble( targetColumn );
			case BOOLEAN	: return rs.getBoolean( targetColumn );
			case DATE		: return rs.getDate( targetColumn );
			case TIME		: return rs.getTime( targetColumn );
			case DATETIME	: return rs.getTimestamp( targetColumn );
			default			: return rs.getString( targetColumn );
		}
		*/
		/*
	}
		 */


	@Override
	public QueryBasedVC copy(final ValueConfiguration obj) {
		super.copy( obj );
		if ( obj instanceof QueryBasedVC ) {
			QueryBasedVC that = (QueryBasedVC) obj;
			this.queryConfig = that.queryConfig; // Reference
			CopierUtil.copyCollection( that.parameters, this.parameters );
			this.targetColumn = that.targetColumn;
			this.targetColumnType = that.targetColumnType;
		}
		return this;
	}

	@Override
	public QueryBasedVC newCopy() {
		return ( new QueryBasedVC() ).copy( this );
	}
	
	@Override
	public String toString() {
		return ( new StringBuffer() )
			.append( " queryConfig: " ).append( queryConfig != null ? queryConfig : "null" )
			.append( " targetColumn: " ).append( targetColumn != null ? targetColumn : "null" )
			.append( " targetColumnType: " ).append( targetColumnType != null ? targetColumnType : "null" )
			.append( " parameters: " ).append( parameters != null ? parameters : "null" )
			.toString();
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			queryConfig, targetColumn, targetColumnType, parameters
		} );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof QueryBasedVC ) ) return false;
		final QueryBasedVC that = (QueryBasedVC) obj;
		return EqUtil.equalsAdresses( this.queryConfig, that.queryConfig ) // Compare address
			&& EqUtil.equalsIgnoreCase( this.targetColumn, that.targetColumn )
			&& EqUtil.equals( this.targetColumnType, that.targetColumnType )
			&& EqUtil.equals( this.parameters, that.parameters )
			;
	}
}
