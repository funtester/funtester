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
			Directories.DEFAULT,
			"FunTesterSuite"
			);	
	
	private String localeLanguage = "";
	private String localeCountry = "";	
	private String lookAndFeelName = "";
	private Directories directories = new Directories(); 
	private String defaultTestSuiteName = "";
	
	public AppConfiguration() {
	}
	
	public static AppConfiguration createWith(
			final String localeLanguage,
			final String localeCountry,
			final String lookAndFeelName,
			final Directories directories,
			final String defaultTestSuiteName
			) {
		AppConfiguration o = new AppConfiguration();
		o.localeLanguage = localeLanguage;
		o.localeCountry = localeCountry;
		o.lookAndFeelName = lookAndFeelName;
		o.directories = directories;
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


	public Directories getDirectories() {
		return directories;
	}

	public void setDirectories(Directories directories) {
		this.directories = directories;
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
		this.directories = that.directories;
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
			.append( " localeLanguage : " ).append( localeLanguage )
			.append( " localeCountry : " ).append( localeCountry )
			.append( " lookAndFeelName : " ).append( lookAndFeelName )
			.append( " directories : {" ).append( directories.toString() ).append( "}" )
			.append( " defaultTestSuiteName : " ).append( defaultTestSuiteName )
			.toString();
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			localeLanguage,
			localeCountry,
			lookAndFeelName,
			directories,
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
			&& EqUtil.equals( this.directories, that.directories )
			&& EqUtil.equalsIgnoreCase( this.defaultTestSuiteName, that.defaultTestSuiteName )
			;
	}
}
