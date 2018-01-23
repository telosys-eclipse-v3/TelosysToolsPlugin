package org.telosys.tools.eclipse.plugin;

/**
 * Contextual Help IDs provider 
 * 
 * @author Laurent Guerin
 *
 */
public class ContextualHelp {

	/**
	 * The OSGI bundle name of the plugin providing help
	 */
	private final static String TELOSYS_TOOLS_HELP_PLUGIN_ID = "org.telosys.tools.eclipse.plugin.help" ;

	/**
	 * The "Help Context ID" defined in the "contexts.xml" file for Velocity Editor
	 */
	private final static String VELOCITY_EDITOR_HELP_CONTEXT_ID = "VelocityEditorHelp" ;

	/**
	 * Returns the Help Context ID for the Velocity Template Editor <br>
	 * The ID is made of "PluginHelpID" + "." + "HelpContextID"
	 * @return
	 */
	public final static String getVelocityEditorHelpContextId () {
		return TELOSYS_TOOLS_HELP_PLUGIN_ID + "." + VELOCITY_EDITOR_HELP_CONTEXT_ID ;
	}

}
