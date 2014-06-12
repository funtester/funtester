package org.funtester.app.project;

import java.util.Arrays;

import org.funtester.common.util.Copier;
import org.funtester.common.util.EqUtil;

/**
 * Application configuration.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class AppConfiguration implements Copier< AppConfiguration >{
	
	public final static AppConfiguration DEFAULT = AppConfiguration.createWith(
			"en",
			"US",
			"Windows",
			"vocabulary",
			"profile",
			"jdbc",
			"plugin",
			"FunTesterSuite"
			);
	
	private String localeLanguage = "";
	private String localeCountry = "";	
	private String lookAndFeelName = "";
	private String vocabularyDirectory = "";
	private String profileDirectory = "";
	private String databaseDriverDirectory = "";
	private String pluginDirectory = "";
	private String defaultTestSuiteName = "";
	
	public AppConfiguration() {
	}
	
	public static AppConfiguration createWith(
			final String localeLanguage,
			final String localeCountry,
			final String lookAndFeelName,
			final String vocabularyDirectory,
			final String profileDirectory,
			final String databaseDriverDirectory,
			final String plugInDirectory,
			final String defaultTestSuiteName
			) {
		AppConfiguration o = new AppConfiguration();
		o.localeLanguage = localeLanguage;
		o.localeCountry = localeCountry;
		o.lookAndFeelName = lookAndFeelName;
		o.vocabularyDirectory = vocabularyDirectory;
		o.profileDirectory = profileDirectory;
		o.databaseDriverDirectory = databaseDriverDirectory;
		o.pluginDirectory = plugInDirectory;
		o.defaultTestSuiteName = defaultTestSuiteName;
		return o;
	}
	

	public String getLocaleLanguage() {
		return localeLanguage;
	}
	
	public void setLocaleLanguage(String localeLanguage) {
		this.localeLanguage = localeLanguage;
	}

	public String getLocaleCountry() {
		return localeCountry;
	}
	
	public void setLocaleCountry(String localeCountry) {
		this.localeCountry = localeCountry;
	}

	public String getLookAndFeelName() {
		return lookAndFeelName;
	}
	
	public void setLookAndFeelName(String lookAndFeelName) {
		this.lookAndFeelName = lookAndFeelName;
	}	
	
	public String getVocabularyDirectory() {
		return vocabularyDirectory;
	}
	
	public void setVocabularyDirectory(String vocabularyDirectory) {
		this.vocabularyDirectory = vocabularyDirectory;
	}

	public String getProfileDirectory() {
		return profileDirectory;
	}
	
	public void setProfileDirectory(String profileDirectory) {
		this.profileDirectory = profileDirectory;
	}
	
	public String getDatabaseDriverDirectory() {
		return databaseDriverDirectory;
	}
	
	public void setDatabaseDriverDirectory(String directory) {
		this.databaseDriverDirectory = directory;
	}
	
	public String getPluginDirectory() {
		return pluginDirectory;
	}

	public void setPluginDirectory(String plugInDirectory) {
		this.pluginDirectory = plugInDirectory;
	}

	public String getDefaultTestSuiteName() {
		return defaultTestSuiteName;
	}

	public void setDefaultTestSuiteName(String defaultTestSuiteName) {
		this.defaultTestSuiteName = defaultTestSuiteName;
	}

	@Override
	public AppConfiguration copy(final AppConfiguration that) {
		this.localeLanguage = that.localeLanguage;
		this.localeCountry = that.localeCountry;
		this.lookAndFeelName = that.lookAndFeelName;
		this.vocabularyDirectory = that.vocabularyDirectory;
		this.profileDirectory = that.profileDirectory;
		this.databaseDriverDirectory = that.databaseDriverDirectory;
		this.pluginDirectory = that.pluginDirectory;
		this.defaultTestSuiteName = that.defaultTestSuiteName;
		return this;
	}
	
	@Override
	public AppConfiguration newCopy() {
		return ( new AppConfiguration() ).copy( this );
	}

	@Override
	public String toString() {
		return ( new StringBuffer() )
			.append( " localeLanguage: " ).append( localeLanguage )
			.append( " localeCountry: " ).append( localeCountry )
			.append( " lookAndFeelName: " ).append( lookAndFeelName )
			.append( " vocabularyDirectory: " ).append( vocabularyDirectory )
			.append( " profileDirectory: " ).append( profileDirectory )
			.append( " databaseDriverDirectory: " ).append( databaseDriverDirectory )
			.append( " pluginDirectory: " ).append( pluginDirectory )
			.append( " defaultTestSuiteName: " ).append( defaultTestSuiteName )
			.toString();
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			localeLanguage,
			localeCountry,
			lookAndFeelName,
			vocabularyDirectory,
			profileDirectory,
			databaseDriverDirectory,
			pluginDirectory,
			defaultTestSuiteName
		} );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof AppConfiguration ) ) { return false; }
		final AppConfiguration that = (AppConfiguration) obj;
		return EqUtil.equalsIgnoreCase( this.localeLanguage, that.localeLanguage )
			&& EqUtil.equalsIgnoreCase( this.localeCountry, that.localeCountry )
			&& EqUtil.equalsIgnoreCase( this.lookAndFeelName, that.lookAndFeelName )
			&& EqUtil.equalsIgnoreCase( this.vocabularyDirectory, that.vocabularyDirectory )
			&& EqUtil.equalsIgnoreCase( this.profileDirectory, that.profileDirectory )
			&& EqUtil.equalsIgnoreCase( this.databaseDriverDirectory, that.databaseDriverDirectory )
			&& EqUtil.equalsIgnoreCase( this.pluginDirectory, that.pluginDirectory )
			&& EqUtil.equalsIgnoreCase( this.defaultTestSuiteName, that.defaultTestSuiteName )
			;
	}
}
