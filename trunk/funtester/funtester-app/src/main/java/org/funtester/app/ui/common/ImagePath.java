package org.funtester.app.ui.common;


/**
 * Paths of images and icons used in the application UI.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class ImagePath {
	
	private static final String RESOURCE_PATH = "images/";
	private static final String FLAGS_PATH = RESOURCE_PATH + "flags/";
	
	private ImagePath() {}
	
	/**
	 * Generate a path for an image file.
	 * 
	 * @param fileName	the name of the image file.
	 * @return
	 */
	private static String path(final String fileName) {
		return RESOURCE_PATH + fileName;
	}
	
	/**
	 * Generate a path for a flag image file.
	 * 
	 * @param fileName	the name of the image file.
	 * @return
	 */
	private static String flagPath(final String fileName) {
		return FLAGS_PATH + fileName;
	}
	
	// Splash
	
	public static String splashImage() {
		return path( "funtester.png" );
	}
	
	// Logo
	
	public static String logoImage() {
		return path( "logo.png" );
	}
	
	// Author
	
	public static String authorImage() {
		return path( "author.jpg" );
	}
	
	// LinkedIn 
	
	public static String linkedinIcon() {
		return path( "linkedin.png" );
	}	
	
	// Email
	
	public static String emailIcon() {
		return path( "email.png" );
	}
	
	// Licence Logo
	
	public static String licenceLogoImage() {
		return path( "gplv3.png" );
	}	
	
	// Dialog
	
	public static String cancelIcon() {
		return path( "cancel.png" );
	}
	
	public static String okIcon() {
		return path( "ok.png" );
	}
	
	// Arrows
	
	public static String dropDownIcon() {
		return path( "dropdown.png" );
	}
	
	// File
	
	public static String newIcon() {
		return path( "new.png" );
	}	
	
	public static String openIcon() {
		return path( "open.png" );
	}
	
	public static String saveIcon() {
		return path( "save.png" );
	}
	
	public static String saveAsIcon() {
		return path( "saveAs.png" );
	}
	
	public static String closeIcon() {
		return path( "close.png" );
	}
	
	// Flags

	/**
	 * Generate a path for a flag image with a given country.
	 * 
	 * @param country	the country of the image (e.g. "br", "ar")
	 * @return
	 */
	public static String countryFlagIcon(final String country) {
		if ( null == country ) { return null; }
		return flagPath( country + ".png" );
	}
	
	// Main
	
	public static String mainIcon() {
		return path( "main.png" );
	}
	
	// Configuration
	
	public static String configurationIcon() {
		return path( "configuration.png" );
	}
	
	public static String localeIcon() {
		return path( "locale.png" );
	}
	
	public static String themeIcon() {
		return path( "theme.png" );
	}
	
	// Console
	
	public static String consoleIcon() {
		return path( "console.png" );
	}
	
	// Copy, Paste, Cut
	
	public static String copyIcon() {
		return path( "copy.png" );
	}
	
	// Font size
	
	public static String fontSizeUp() {
		return path( "font_size_up.png" );
	}
	
	public static String fontSizeDown() {
		return path( "font_size_down.png" );
	}
	
	// About
	
	public static String aboutIcon() {
		return path( "about.png" );
	}
	
	// Tip
	
	public static String tipIcon() {
		return path( "tip.png" );
	}	
	
	// Test
	
	public static String abstractTestGenerationIcon() {
		return path( "abstract_test_generate.png" );
	}	

	public static String testRunIcon() {
		return path( "run.png" );
	}
	
	public static String testRunOnlyIcon() {
		return path( "runOnly.png" );
	}
	
	// Plug-in
	
	public static String pluginIcon() {
		return path( "plugin.png" );
	}	
	
	// Software
	
	public static String softwareIcon() {
		return path( "software.png" );
	}
	
	// Use case
	
	public static String useCaseIcon() {
		return path( "use_case.png" );
	}

	public static String useCaseNewIcon() {
		return path( "use_case_add.png" );
	}
	
	public static String useCaseEditIcon() {
		return path( "use_case_edit.png" );
	}
	
	public static String useCaseRemoveIcon() {
		return path( "use_case_remove.png" );
	}
	
	public static String useCaseCloneIcon() {
		return path( "use_case_clone.png" );
	}	
	
	// Actor
	
	public static String actorIcon() {
		return path( "actor.png" );
	}
	
	public static String actorNewIcon() {
		return path( "actor_add.png" );
	}
	
	public static String actorEditIcon() {
		return path( "actor_edit.png" );
	}
	
	public static String actorRemoveIcon() {
		return path( "actor_remove.png" );
	}
	
	// Database
	
	public static String databaseIcon() {
		return path( "database.png" );
	}
	
	public static String databaseNewIcon() {
		return path( "database_add.png" );
	}
	
	public static String databaseEditIcon() {
		return path( "database_edit.png" );
	}
	
	public static String databaseRemoveIcon() {
		return path( "database_remove.png" );
	}
	
	public static String databaseConnectIcon() {
		return path( "database_connect.png" );
	}	
	
	// Query
	
	public static String queryIcon() {
		return path( "script.png" );
	}
	
	public static String queryNewIcon() {
		return path( "script_add.png" );
	}
	
	public static String queryEditIcon() {
		return path( "script_edit.png" );
	}
	
	public static String queryRemoveIcon() {
		return path( "script_remove.png" );
	}
	
	// Script
	
	public static String scriptIcon() {
		return path( "script.png" );
	}
	
	public static String scriptNewIcon() {
		return path( "script_add.png" );
	}
	
	public static String scriptEditIcon() {
		return path( "script_edit.png" );
	}
	
	public static String scriptRemoveIcon() {
		return path( "script_remove.png" );
	}
	
	public static String scriptTestIcon() {
		return path( "script_test.png" );
	}
	
	// RegEx
	
	public static String regExIcon() {
		return path( "regex.png" );
	}
	
	public static String regExNewIcon() {
		return path( "regex_add.png" );
	}	
	
	public static String regExEditIcon() {
		return path( "regex_edit.png" );
	}	
	
	public static String regExRemoveIcon() {
		return path( "regex_remove.png" );
	}	
	
	// Flow
	
	public static String flowIcon() {
		return path( "flow.png" );
	}
	
	public static String flowNewIcon() {
		return path( "flow_add.png" );
	}
	
	public static String flowEditIcon() {
		return path( "flow_edit.png" );
	}
	
	public static String flowRemoveIcon() {
		return path( "flow_remove.png" );
	}
	
	// Step
	
	public static String stepIcon() {
		return path( "step.png" );
	}
	
	public static String stepNewIcon() {
		return path( "step_add.png" );
	}
	
	public static String stepCloneIcon() {
		return path( "step_clone.png" );
	}
	
	public static String stepEditIcon() {
		return path( "step_edit.png" );
	}
	
	public static String stepRemoveIcon() {
		return path( "step_remove.png" );
	}
	
	public static String stepUpIcon() {
		return path( "step_up.png" );
	}
	
	public static String stepDownIcon() {
		return path( "step_down.png" );
	}
	
	// Postcondition
	
	public static String postconditionIcon() {
		return path( "postcondition.png" );
	}

	public static String postconditionNewIcon() {
		return path( "postcondition_add.png" );
	}
	
	public static String postconditionEditIcon() {
		return path( "postcondition_edit.png" );
	}
	
	public static String postconditionRemoveIcon() {
		return path( "postcondition_remove.png" );
	}
	
	// Element
	
	public static String elementIcon() {
		return path( "element.png" );
	}	
	
	public static String elementNewIcon() {
		return path( "element_add.png" );
	}
	
	public static String elementEditIcon() {
		return path( "element_edit.png" );
	}
	
	public static String elementRemoveIcon() {
		return path( "element_remove.png" );
	}
	
	// Include File
	
	public static String includeFileIcon() {
		return path( "includefile.png" );
	}	

	public static String includeFileNewIcon() {
		return path( "includefile_add.png" );
	}
	
	public static String includeFileEditIcon() {
		return path( "includefile_edit.png" );
	}
	
	public static String includeFileRemoveIcon() {
		return path( "includefile_remove.png" );
	}
}
