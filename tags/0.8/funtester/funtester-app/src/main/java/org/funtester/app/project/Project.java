package org.funtester.app.project;

import java.util.Arrays;

import org.funtester.common.generation.TestGenerationConfiguration;
import org.funtester.common.util.Copier;
import org.funtester.common.util.EqUtil;
import org.funtester.core.software.Software;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Project
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class Project implements Copier< Project > {
	
	/** Used to detect the model version */
	private int modelVersion = 1;
	
	private String vocabularyFile = "";
	
	private String softwareFile = "";
	
	private String lastConfigurationFile = "";
	
	@JsonIgnore
	private Software software = null;
	
	@JsonIgnore
	private TestGenerationConfiguration lastConfiguration = null;
	
	
	public Project() {
	}
	
	public int getModelVersion() {
		return modelVersion;
	}

	public void setModelVersion(int modelVersion) {
		this.modelVersion = modelVersion;
	}

	public String getVocabularyFile() {
		return vocabularyFile;
	}
	
	public void setVocabularyFile(String vocabularyFile) {
		this.vocabularyFile = vocabularyFile;
	}

	public String getSoftwareFile() {
		return softwareFile;
	}

	public void setSoftwareFile(String softwareFile) {
		this.softwareFile = softwareFile;
	}

	public String getLastConfigurationFile() {
		return lastConfigurationFile;
	}

	public void setLastConfigurationFile(String lastConfigurationFile) {
		this.lastConfigurationFile = lastConfigurationFile;
	}

	public Software getSoftware() {
		return software;
	}

	public void setSoftware(Software software) {
		this.software = software;
	}

	public TestGenerationConfiguration getLastConfiguration() {
		return lastConfiguration;
	}

	public void setLastConfiguration(TestGenerationConfiguration lastConfiguration) {
		this.lastConfiguration = lastConfiguration;
	}

	@Override
	public Project copy(final Project that) {
		
		this.modelVersion = that.modelVersion;
		this.vocabularyFile = that.vocabularyFile;
		this.softwareFile = that.softwareFile;
		this.lastConfigurationFile = that.lastConfigurationFile;
		
		if ( that.software != null ) {
			if ( this.software != null ) {
				this.software.copy( that.software );
			} else {
				this.software = that.software.newCopy();
			}
		} else {
			this.software = null;
		}
		
		if ( that.lastConfiguration != null ) {
			if ( this.lastConfiguration != null ) {
				this.lastConfiguration.copy( that.lastConfiguration );
			} else {
				this.lastConfiguration = that.lastConfiguration.newCopy();	
			}
		} else {
			this.lastConfiguration = null;
		}
		
		return this;
	}

	@Override
	public Project newCopy() {
		return ( new Project() ).copy( this );
	}
	
	@Override
	public String toString() {
		return 
				fmt( "modelVersion", modelVersion ) +
				fmt( "vocabularyFile", ( vocabularyFile != null ) ? vocabularyFile : "" ) +
				fmt( "softwareFile", ( softwareFile != null ) ? softwareFile : "" ) +
				fmt( "lastConfigurationFile", ( lastConfigurationFile != null ) ? lastConfigurationFile : "" ) +
				fmt( "software", ( software != null ) ? software.toString() : "" ) +
				fmt( "lastConfiguration", ( lastConfiguration != null ) ? lastConfiguration.toString() : "" )
				;
	}
	
	private String fmt(String key, Object value) {
		return String.format( "\"%s\" : \"%s\"", key, value );
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			modelVersion,
			vocabularyFile,
			softwareFile,
			lastConfigurationFile,
			software,
			lastConfiguration
		} );
	}
	
	@Override
	public boolean equals(final Object obj) {
		if ( ! ( obj instanceof Project ) ) { return false; }
		Project that = (Project) obj;
		return
			EqUtil.equalsIgnoreCase( this.vocabularyFile, that.vocabularyFile )
			&& EqUtil.equalsIgnoreCase( this.softwareFile, that.softwareFile )
			;
	}

}
