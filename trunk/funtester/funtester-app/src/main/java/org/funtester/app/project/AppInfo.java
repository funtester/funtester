package org.funtester.app.project;

import java.util.Arrays;

import org.funtester.common.util.EqUtil;

/**
 * Application information.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class AppInfo {

	private final String name;
	private final Version version;
	private final String siteURL;
	private final String downloadSiteURL;

	public AppInfo(
			final String name,
			final Version version,
			final String siteURL,
			final String downloadSiteURL
			) {
		this.name = name;
		this.version = version;
		this.siteURL = siteURL;
		this.downloadSiteURL = downloadSiteURL;
	}

	public String getName() {
		return name;
	}
	
	public Version getVersion() {
		return version;
	}
	
	public String getSiteURL() {
		return siteURL;
	}
	
	public String getDownloadSiteURL() {
		return downloadSiteURL;
	}

	@Override
	public String toString() {
		return ( new StringBuffer() )
			.append( name != null ? name : "" ).append( " - " )
			.append( version != null ? version.toString() : "" ).append( " - " )
			.append( siteURL != null ? siteURL : "" ).append( " - " )
			.append( downloadSiteURL != null ? downloadSiteURL : "" )
			.toString()
			;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
				name, version, siteURL, downloadSiteURL } );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof AppInfo ) ) { return false; }
		final AppInfo that = (AppInfo) obj;
		return EqUtil.equalsIgnoreCase( this.name, that.name )
			&& EqUtil.equals( this.version, that.version )
			&& EqUtil.equalsIgnoreCase( this.siteURL, that.siteURL )
			&& EqUtil.equalsIgnoreCase( this.downloadSiteURL, that.downloadSiteURL )
			;
	}
}
