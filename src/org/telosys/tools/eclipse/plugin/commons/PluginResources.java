package org.telosys.tools.eclipse.plugin.commons;

import java.net.MalformedURLException;
import java.net.URL;

import org.telosys.tools.eclipse.plugin.MyPlugin;

public class PluginResources {

	/*
	 * Platform URLs in Eclipse
	 * "platform:/plugin" : 
	 *  It is used to locate a resource available in a plug-in/bundle. One really cool thing about 
	 *  this one is that it doesn't really matter if this resource is available in a directory or in a jar file. 
	 *  It also doesn't matter if the bundle is installed in a link folder or in the default directory.
	 *  The path segment after "plugin" should be the identifier of the bundle, which can be followed by the path 
	 *  of the resource in the bundle
	 */
	/**
	 * @param resourceFile the resource file name (path in the pluging 'resources' folder)
	 */
	public static URL getResourceURL(String resourceFile) {
		
		String pluginId = MyPlugin.getId() ;
		String resourceURL = "platform:/plugin/" + pluginId + "/resources/" + resourceFile ;
		URL url;
		try {
			url = new URL( resourceURL );
			return url ;
		} catch (MalformedURLException e) {
			MsgBox.error("Cannot create URL : \n" + resourceURL, e ) ;
			return null ;
		}
	}
	
}
