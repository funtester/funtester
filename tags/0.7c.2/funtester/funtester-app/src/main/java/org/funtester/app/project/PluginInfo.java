package org.funtester.app.project;

import java.util.ArrayList;
import java.util.List;

/**
 * Plug-in information.
 * <p>Some notes about a plug-in in FunTester:<ul>
 * <li>A plug-in file should be named with the <code>.plg</code> extension.</li>
 * <li>A plug-in icon file should have the same name as the plugin file, but
 * should have the <code>.ico</code> extension. 
 * </ul>
 * </p>
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class PluginInfo {

	private String id = ""; // unique id as string. Example: org.funtester.funtester-plug-fest
	private String name = "";
	private String version = "";
	private String description = "";
	/** Supported testing frameworks */
	private List< FrameworkInfo > frameworks = new ArrayList< FrameworkInfo >();
	private boolean canRunTestsInternally = false;
	private String authors = "";
	private String website = "";
	private String commandToRun = "";
	
	public PluginInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List< FrameworkInfo > getFrameworks() {
		return frameworks;
	}

	public void setFrameworks(List< FrameworkInfo > frameworks) {
		this.frameworks = frameworks;
	}

	public boolean getCanRunTestsInternally() {
		return canRunTestsInternally;
	}

	public void setCanRunTestsInternally(boolean canRunTestsInternally) {
		this.canRunTestsInternally = canRunTestsInternally;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getCommandToRun() {
		return commandToRun;
	}

	public void setCommandToRun(String commandToRun) {
		this.commandToRun = commandToRun;
	}
	
}
