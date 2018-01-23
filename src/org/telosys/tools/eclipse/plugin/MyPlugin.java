package org.telosys.tools.eclipse.plugin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

/**
 * Utility methods for the current Eclipse Plugin 
 * 
 * @author Laurent GUERIN
 *  
 */
public class MyPlugin
{
    //------------------------------------------------------------------------------------------------
//	//--- Templates folder embedded in the Eclipse Plugin
//	private static final String TEMPLATES_PLUGIN_FOLDER  = "templates";

	//--- Icons folder embedded in the Eclipse Plugin
	private static final String IMAGES_PLUGIN_FOLDER     = "icons";

	//--- Resources folder embedded in the Eclipse Plugin
	private static final String RESOURCES_PLUGIN_FOLDER  = "resources";

    //------------------------------------------------------------------------------------------------
    private static String $sPluginId = "(no-id)" ;

    private static String $sPluginName = "(no-name)" ;
    
    private static String $sPluginVersion = "(no-version)" ;

    private static String $sPluginProvider = "(no-provider)" ;
    
    private static Bundle $bundle = null ;

    //------------------------------------------------------------------------------------------------
    /**
     * Initialized the plugin environment.
     * 
     * @param bundle
     */
    protected static void init( Bundle bundle )
    {
    	if ( bundle == null )
    	{
    		MsgBox.error("Plugin.init() : bundle argument is null !");
    		return;
    	}
    	PluginLogger.log("Plugin.init() ... " );
    	//logBundle(bundle);
    	
    	//--- Keep the bundle instance
    	$bundle = bundle ;
    	
    	//--- The bundle symbolic name is the "id" from the "plugin.xml" file  
    	$sPluginId = bundle.getSymbolicName();
    	PluginLogger.log(" Plugin ID = " + $sPluginId );
    	
		Dictionary<?,?> dict = bundle.getHeaders(); // Bundle's Manifest headers and values
		if ( dict != null )
		{
			Object o = null ; 
			//--- Plugin Name
			o = dict.get("Bundle-Name");
			if ( o instanceof String )
			{
				$sPluginName = (String) o;
			}
	    	PluginLogger.log(" Plugin name = " + $sPluginName );
			//--- Plugin Version
			o = dict.get("Bundle-Version");
			if ( o instanceof String )
			{
				$sPluginVersion = (String) o;
			}
	    	PluginLogger.log(" Plugin version = " + $sPluginVersion );
			//--- Plugin Provider
			o = dict.get("Bundle-Vendor");
			if ( o instanceof String )
			{
				$sPluginProvider = (String) o;
			}				
	    	PluginLogger.log(" Plugin provider = " + $sPluginProvider );
		}
		else
		{
			PluginLogger.log(" No bundle headers ( return is null )" );    
		}

		PluginLogger.log(" Plugin base URL = " + getBaseURL() );
		PluginLogger.log(" Plugin directory URL = " + getBaseURLAsString() );
		
		PluginLogger.log(" Plugin directory = " + getDirectory() );
		PluginLogger.log(" Plugin icons directory     = " + getImagesDirectory() );
//		PluginLogger.log(" Plugin templates directory = " + getTemplatesDirectory() );
		PluginLogger.log(" Plugin resources directory = " + getResourcesDirectory() );
		
    }
    
    //------------------------------------------------------------------------------------------------
    protected static void logBundle(Bundle bundle)
    {
    	PluginLogger.log(" bundle.getBundleId() : " + bundle.getBundleId() );
    	PluginLogger.log(" bundle.getLocation() : " + bundle.getLocation() );
    	PluginLogger.log(" bundle.getSymbolicName() : " + bundle.getSymbolicName() );    
    	// Returns this bundle's Manifest headers and values
    	try {
			Dictionary<?,?> dict = bundle.getHeaders();
	    	PluginLogger.log(" after bundle.getHeaders() " );    
			if ( dict != null )
			{
				Enumeration<?> en = dict.keys();
		    	PluginLogger.log(" after dict.keys() " );    
				if ( en != null )
				{					
					Object oKey = null ;
					Object oVal = null ;    	
					while ( en.hasMoreElements() )
					{
						oKey = en.nextElement();
						oVal = dict.get(oKey);
				    	PluginLogger.log("  . " + oKey + " : " + oVal );    
					}
				}
				else
				{
					PluginLogger.log(" No enumeration ( return is null )" );    
				}
			}
			else
			{
				PluginLogger.log(" No bundle headers ( return is null )" );    
			}
		} catch (Throwable e) {
			PluginLogger.log(" Exception : " + e.toString() + " " + e.getMessage()  );    
		}		
    }

    //------------------------------------------------------------------------------------------------
    /**
     * Returns the plugin ID ( id retrieve from the plugin.xml file )
     * @return
     */
    public static String getId()
    {
        return $sPluginId;
    }

    //------------------------------------------------------------------------------------------------
    /**
     * Returns the plugin NAME ( retrieved from the plugin.xml file )
     * @return
     */
    public static String getName()
    {
        return $sPluginName;
    }

    //------------------------------------------------------------------------------------------------
    /**
     * Returns the plugin VERSION ( retrieved from the plugin.xml file )
     * @return
     */
    public static String getVersion()
    {
        return $sPluginVersion;
    }

    //------------------------------------------------------------------------------------------------
    /**
     * Returns the plugin PROVIDER ( retrieved from the plugin.xml file )
     * @return
     */
    public static String getProvider()
    {
        return $sPluginProvider;
    }

    //------------------------------------------------------------------------------------------------
    /**
     * Returns the plugin OSGI bundle
     * @return
     */
    public static Bundle getBundle()
    {
        //return Platform.getBundle($sPluginId);
        return $bundle ;
    }

//    //------------------------------------------------------------------------------------------------
//    /**
//     * Returns the UI workbench (set of windows). Fails if the workbench has not been created yet.
//     * Based on "PlatformUI.getWorkbench()"
//     * @return
//     */
//    public static IWorkbench getWorkbench()
//    {
//    	return PlatformUI.getWorkbench();
//    }
    
    //------------------------------------------------------------------------------------------------
    /**
     * Returns the plugin base URL ( ie "file:/c:/xxx/xxx/eclipse/plugins/myplugin" ) 
     * @return
     */
    private static URL getBaseURL()
    {
        URL resolvedUrl = null;
        if ( $bundle != null )
        {
            // URL pluginRelativeUrl = Platform.find($bundle, new Path(""));
            URL pluginRelativeUrl = FileLocator.find($bundle, new Path(""), null);
            if (pluginRelativeUrl != null)
            {
                try
                {
                    //resolvedUrl = Platform.resolve(pluginRelativeUrl);
                    resolvedUrl = FileLocator.resolve(pluginRelativeUrl);
                } catch (IOException e)
                {
                    MsgBox.error("Cannot resolve relative URL '" + pluginRelativeUrl +"' !", e ) ; 
                }
            }
            else
            {
            	MsgBox.error("Cannot get plugin directory : Platform.find() return null !");
            }
        }
        else
        {
        	MsgBox.error("Cannot get plugin directory ( bundle is null ) !");
        }
        return resolvedUrl;
    }

    //------------------------------------------------------------------------------------------------
    /**
     * Returns the plugin base URL as a String ( ie "file:/c:/xxx/xxx/eclipse/plugins/myplugin" ) 
     * @return
     */
    public static String getBaseURLAsString()
    {
    	URL url = getBaseURL();
    	if ( url != null )
    	{
    		return url.toString();
    	}
    	return null ;
    	
    }

    //------------------------------------------------------------------------------------------------
    /**
     * @return Ecplise plugin directory ( ie : "c:/xxx/xxx/eclipse/plugins/myplugin" )
     */
    public static String getDirectory()
    {
        String sPluginDirectoryURL = getBaseURLAsString();
        if ( sPluginDirectoryURL != null )        	
        {
        	if ( sPluginDirectoryURL.startsWith("file:/") )
        	{
                return sPluginDirectoryURL.substring(6); // without "file:/"
        	}
        	return sPluginDirectoryURL ; // Keep it AS IS 
        }
        return null ;
    }

//    //------------------------------------------------------------------------------------------------
//    /**
//     * @return Ecplise plugin template directory ( ie :
//     *         "c:/xxx/xxx/eclipse/plugins/myplugin/templates" )
//     */
//    public static String getTemplatesDirectory()
//    {
//        String sPluginDir = getDirectory();
//        if ( sPluginDir != null )
//        {
//        	return sPluginDir + TEMPLATES_PLUGIN_FOLDER;
//        }
//        return null ;
//    }

    //------------------------------------------------------------------------------------------------
    /**
     * @return Ecplise plugin icons directory ( ie :
     *         "c:/xxx/xxx/eclipse/plugins/myplugin/icons" )
     */
    public static String getImagesDirectory()
    {
        String sPluginDir = getDirectory();
        if ( sPluginDir != null )
        {
        	return sPluginDir + IMAGES_PLUGIN_FOLDER;
        }
        return null ;
    }

    //------------------------------------------------------------------------------------------------
    /**
     * @return Ecplise plugin icons directory ( ie :
     *         "c:/xxx/xxx/eclipse/plugins/myplugin/resources" )
     */
    public static String getResourcesDirectory()
    {
        String sPluginDir = getDirectory();
        if ( sPluginDir != null )
        {
        	return sPluginDir + RESOURCES_PLUGIN_FOLDER ;
        }
        return null ;
    }

    //------------------------------------------------------------------------------------------------
    /**
     * Returns the plugin image file URL for the given file name 
     * @param sImageFile the image file
     * @return
     */
    public static URL getImageURL(String sImageFile)
    {
		URL imageURL = null ;
		URL baseURL = getBaseURL();
		if ( baseURL != null )
		{
			try {
				imageURL = new URL(baseURL, IMAGES_PLUGIN_FOLDER + "/" + sImageFile );
			} catch (MalformedURLException e) {
				MsgBox.error("Cannot get image URL for '" + sImageFile + "'.", e );
			}
		}
		return imageURL ;
    }
    
}