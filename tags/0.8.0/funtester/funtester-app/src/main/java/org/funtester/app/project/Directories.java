package org.funtester.app.project;

import java.util.Arrays;

import org.funtester.common.util.Copier;
import org.funtester.common.util.EqUtil;

/**
 * Application directories.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class Directories implements Copier< Directories > {
	
	public final static Directories DEFAULT = Directories.createWith(
			"vocabulary",
			"profile",
			"jdbc",
			"plugin",
			"manual"
			);

	private String vocabulary = "";
	private String profile = "";
	private String databaseDriver = "";
	private String plugin = "";
	private String manual = "";
	
	public Directories() {
		copy( DEFAULT );
	}
	
	/**
	 * Create a {@link Directories} object with the given values.
	 * 
	 * @param vocabulary
	 * @param profile
	 * @param database
	 * @param plugin
	 * @param manual
	 * @return
	 */
	public static Directories createWith(
			final String vocabulary,
			final String profile,
			final String database,
			final String plugin,
			final String manual
			) {
		Directories obj = new Directories();
		obj.vocabulary = vocabulary;
		obj.profile = profile;
		obj.databaseDriver = database;
		obj.plugin = plugin;
		obj.manual = manual;
		return obj;
	}

	public String getVocabulary() {
		return vocabulary;
	}

	public void setVocabulary(String vocabulary) {
		this.vocabulary = vocabulary;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getDatabaseDriver() {
		return databaseDriver;
	}

	public void setDatabaseDriver(String database) {
		this.databaseDriver = database;
	}

	public String getPlugin() {
		return plugin;
	}

	public void setPlugin(String plugin) {
		this.plugin = plugin;
	}

	public String getManual() {
		return manual;
	}

	public void setManual(String manual) {
		this.manual = manual;
	}

	@Override
	public Directories copy(final Directories that) {
		if ( this == that || null == that ) { return this; }
		this.vocabulary = that.vocabulary;
		this.profile = that.profile;
		this.databaseDriver = that.databaseDriver;
		this.plugin = that.plugin;
		this.manual = that.manual;
		return this;
	}

	@Override
	public Directories newCopy() {
		return ( new Directories() ).copy( this );
	}

	@Override
	public String toString() {
		return ( new StringBuffer() )
			.append( " vocabulary: " ).append( vocabulary )
			.append( " profile: " ).append( profile )
			.append( " databaseDriver: " ).append( databaseDriver )
			.append( " plugin: " ).append( plugin )
			.append( " manual: " ).append( plugin )
			.toString();
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			vocabulary,
			profile,
			databaseDriver,
			plugin,
			manual
		} );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof Directories ) ) { return false; }
		final Directories that = (Directories) obj;
		return 
			EqUtil.equalsIgnoreCase( this.vocabulary, that.vocabulary )
			&& EqUtil.equalsIgnoreCase( this.profile, that.profile )
			&& EqUtil.equalsIgnoreCase( this.databaseDriver, that.databaseDriver )
			&& EqUtil.equalsIgnoreCase( this.plugin, that.plugin )
			&& EqUtil.equalsIgnoreCase( this.manual, that.manual )
			;
	}
}
