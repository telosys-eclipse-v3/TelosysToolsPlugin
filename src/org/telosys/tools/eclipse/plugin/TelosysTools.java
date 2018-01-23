package org.telosys.tools.eclipse.plugin;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.wkschanges.WorkspaceChanges;

/**
 * The activator class for the plug-in life cycle
 */
public class TelosysTools extends AbstractUIPlugin 
{
//	//--- Bundle Context
//	private BundleContext _bundleContext = null ;

    //------------------------------------------------------------------------------------------------
	/**
	 * The plugin constructor
	 */
	public TelosysTools() {
		super();
		PluginLogger.log("Plugin constructor...");
	}

    //------------------------------------------------------------------------------------------------
	/**
	 * Launch the plugin
	 */
	public void start(BundleContext bundleContext) throws Exception {
		PluginLogger.log("Plugin start()...");
		super.start(bundleContext);

//		//--- Keep the bundle context
//		_bundleContext = bundleContext;
//		Bundle bundle = _bundleContext.getBundle();
//		
//		//--- Init the plugin static informations
//		MyPlugin.init(bundle);
		
//		File file = _bundleContext.getDataFile("testfile.txt");
//		PluginLogger.log( "Data file : " + file.getPath() );
		
		MyPlugin.init( bundleContext.getBundle() );
		
		WorkspaceChanges.startTracking();
	}

    //------------------------------------------------------------------------------------------------
	/**
	 * Stop the plugin
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		PluginLogger.log("Plugin stop()...");
		super.stop(bundleContext);
	}

}
