package org.funtester.app.project;

import java.util.Arrays;

/**
 * Version
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class Version implements Comparable< Version > {
	
	private final String major;
	private final String minor;
	private final String release;

	public Version(
			final String major,
			final String minor,
			final String release
			) {
		this.major = major;
		this.minor = minor;
		this.release = release;
	}

	public String getMajor() {
		return major;
	}

	public String getMinor() {
		return minor;
	}

	public String getRelease() {
		return release;
	}
	
	/**
	 * Return {@code true} whether this version is greater than another one.
	 * 
	 * @param other	the other version to compare.
	 * @return
	 */
	public boolean after(final Version other) {
		return compareTo( other ) > 0;
	}
	
	@Override
	public int compareTo(final Version o) {
		
		int majorComparison = compare( major, o.major );
		if ( majorComparison != 0 ) { return majorComparison; }
		
		int minorComparison = compare( minor, o.minor );
		if ( minorComparison != 0 ) { return minorComparison; }
		
		return compare( release, o.release );
	}
	
	/**
	 * Compare two string version pieces.
	 * 
	 * @param first		the first version piece.
	 * @param second	the second version piece.
	 * @return
	 */
	private int compare(String first, String second) {
		if ( null == first && null == second ) { return 0; }
		if ( null == first ) { return -1; }
		if ( null == second ) { return 1; }
		return first.compareToIgnoreCase( second );
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( major != null && ! major.isEmpty() ? major : "0" );
		sb.append( minor != null && ! minor.isEmpty() ? "." + minor : ".0" );
		sb.append( release != null && ! release.isEmpty() ? "." + release : "" );
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] { major, minor, release } );
	}
	
	@Override
	public boolean equals(Object o) {
		if ( ! ( o instanceof Version ) ) { return false; }
		return ( ( Version ) o ).toString().equalsIgnoreCase( toString() );
	}
}
