package org.funtester.app.project;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.profile.Profile;
import org.funtester.core.software.DatabaseDriverConfig;
import org.funtester.core.software.Software;
import org.funtester.core.vocabulary.Vocabulary;

/**
 * Application state.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class AppState {
	
	public static final String DEFAULT_TRANSLATION_DIRECTORY = "i18n";

	/** Informations about the application (like name, version, etc.) */
	private AppInfo appInfo = new AppInfo(
			"FunTester",
			new Version( "0", "0", "0" ),
			"http://funtester.org",
			"http://funtester.org/download"
			);
	
	/** Execution directory */
	private String executionDirectory = "";
	
	/** File that contains the application configuration */
	private String configurationFile = "";
	
	/** The application configuration */
	private AppConfiguration configuration = new AppConfiguration();
	
	/** The directory with the translation files (i18n) */
	private String translationsDirectory = DEFAULT_TRANSLATION_DIRECTORY;
	
	/** Map the available locales to their respective files */ 
	private Map< Locale, String > localesMap = new LinkedHashMap< Locale, String >();
	
	/** Map the available look and feels to theirs respective class names */ 
	private Map< String, String > lookAndFeelMap = new LinkedHashMap< String, String >();
	
	/** Current look and feel name */
	private String lookAndFeelName = "";
	
	/** Map the available profiles to their respective file names */
	private Map< Profile, String > profileMap = new LinkedHashMap< Profile, String >();
	
	/** Map the available vocabularies to their respective file names */
	private Map< Vocabulary, String > vocabularyMap = new LinkedHashMap< Vocabulary, String >();
	
	/** Map the available database driver templates to their respective template file names */
	private Map< DatabaseDriverConfig, String > driverTemplateFileMap =
			new LinkedHashMap< DatabaseDriverConfig, String >();
	
	/** Driver cache */
	private DriverCache driverCache = new DriverCache();

	/** Map the available plug-ins to their respective files */
	private Map< PluginInfo, String > pluginMap = new LinkedHashMap< PluginInfo, String >();

	/** Project file state */
	private FileState projectFileState = FileState.CLOSED;
	/** Project file name */
	private String projectFileName = "";
	/** Project object */
	private Project project = null;
	
	
	public AppState() {
	}
	
	public AppInfo getAppInfo() {
		return appInfo;
	}

	public void setAppInfo(AppInfo appInfo) {
		this.appInfo = appInfo;
	}
	
	public String getExecutionDirectory() {
		return executionDirectory;
	}
	
	public void setExecutionDirectory(String executionDirectory) {
		this.executionDirectory = executionDirectory;
	}

	public String getConfigurationFile() {
		return configurationFile;
	}

	public void setConfigurationFile(final String fileName) {
		this.configurationFile = fileName;
	}	

	public AppConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(AppConfiguration obj) {
		this.configuration = obj;
	}

	public String getTranslationsDirectory() {
		return translationsDirectory;
	}

	public void setTranslationsDirectory(String translationsDirectory) {
		this.translationsDirectory = translationsDirectory;
	}

	public Map< Locale, String > getLocalesMap() {
		return localesMap;
	}

	public void setLocalesMap(Map< Locale, String > map) {
		this.localesMap = map;
	}

	public Map< String, String > getLookAndFeelMap() {
		return lookAndFeelMap;
	}

	public void setLookAndFeelMap(Map< String, String > map) {
		this.lookAndFeelMap = map;
	}
	
	/**
	 * Put a look and feel in the map.
	 * @param name		the name of the L&F
	 * @param className	the class name of the L&F
	 */
	public void putLookAndFeel(String name, String className) {
		lookAndFeelMap.put( name, className );
	}
	
	/**
	 * Return {@code true} whether contains the given look and feel name.
	 * @param name
	 * @return
	 */
	public boolean containsLookAndFeel(String name) {
		return lookAndFeelMap.containsKey( name );
	}

	public String getLookAndFeelName() {
		return lookAndFeelName;
	}

	public void setLookAndFeelName(String lookAndFeelName) {
		this.lookAndFeelName = lookAndFeelName;
	}

	public Map< Profile, String > getProfileMap() {
		return profileMap;
	}

	public void setProfileMap(Map< Profile, String > profileMap) {
		this.profileMap = profileMap;
	}

	public Map< Vocabulary, String > getVocabularyMap() {
		return vocabularyMap;
	}

	public void setVocabularyMap(Map< Vocabulary, String > map) {
		this.vocabularyMap = map;
	}

	public Map< DatabaseDriverConfig, String > getDriverTemplateMap() {
		return driverTemplateFileMap;
	}

	public void setDriverTemplateMap(Map< DatabaseDriverConfig, String > driverTemplateMap) {
		this.driverTemplateFileMap = driverTemplateMap;
	}

	public DriverCache getDriverCache() {
		return driverCache;
	}

	public void setDriverCache(DriverCache driverCache) {
		this.driverCache = driverCache;
	}

	public Map< PluginInfo, String > getPluginMap() {
		return pluginMap;
	}

	public void setPluginMap(Map< PluginInfo, String > pluginMap) {
		this.pluginMap = pluginMap;
	}
	
	/**
	 * Return the plug-in with the given id.
	 * @param id	the id to find.
	 * @return
	 */
	public PluginInfo pluginWithId(final String id) {
		for ( PluginInfo p : pluginMap.keySet() ) {
			if ( p.getId().equalsIgnoreCase( id ) ) {
				return p;
			}
		}
		return null;
	}

	public FileState getProjectFileState() {
		return projectFileState;
	}

	public void setProjectFileState(FileState fileState) {
		this.projectFileState = fileState;
	}

	public String getProjectFileName() {
		return projectFileName;
	}

	public void setProjectFileName(String fileName) {
		this.projectFileName = fileName;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	public void closeProject() {
		setProject( null );
		setProjectFileName( "" );
		setProjectFileState( FileState.CLOSED );
	}

	public Software getCurrentSoftware() {
		return getProject() != null ? getProject().getSoftware() : null;
	}
	
}
