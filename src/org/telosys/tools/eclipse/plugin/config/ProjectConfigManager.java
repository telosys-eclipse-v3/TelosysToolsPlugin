package org.telosys.tools.eclipse.plugin.config;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.cfg.TelosysToolsCfgManager;
import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.eclipse.plugin.commons.EclipseProjUtil;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

/**
 * Projects configuration manager <br>
 * . save and load the projects configuration<br>
 * . hold each project configuration in a cache<br>
 */
public class ProjectConfigManager {

//	//-------------------------------------------------------------------------------------------------
	// Removed in v 3.0.0 ( same as loadProjectConfig )
//	/**
//	 * Returns the project configuration for the given Eclipse project<br>
//	 * @param project
//	 * @return the project configuration or null if the configuration file doesn't exist
//	 */
//	public static ProjectConfig getProjectConfig( IProject project ) 
//	{
//		PluginLogger.log("ProjectConfigManager.getProjectConfig(project)..." );
//		if ( project == null )
//		{
//			MsgBox.error("getProjectConfig : project parameter is null ");
//			return null ;
//		}
//		
//		PluginLogger.log("ProjectConfigManager.getProjectConfig(project) : Load config..." );
//		ProjectConfig projectConfig = loadProjectConfig(project) ;
//		return projectConfig ;
//	}

	//-------------------------------------------------------------------------------------------------
	/**
	 * Loads the project's configuration from the properties file  
	 * @param project
	 * @return
	 */
//	public static ProjectConfig loadProjectConfig(IProject project) 
	public static TelosysToolsCfg loadProjectConfig(IProject project)  // v 3.0.0
	{
		if ( project == null ) {
			MsgBox.error("ProjectConfigManager : loadProjectConfig : project is null");
		}

		String projectName = getProjectName(project); // v 3.0.0
//		String projectLocation = getProjectLocation(project); // v 3.0.0
		//String workspaceLocation = getWorkspaceLocation(project); // v 3.0.0
//		String workspaceLocation = EclipseProjUtil.getWorkspaceLocation(project); // v 3.0.0
		
		String projectFolder = EclipseProjUtil.getProjectDir( project );
		PluginLogger.log("ProjectConfigManager.loadProjectConfig("+projectFolder+")..." );
		TelosysToolsCfgManager telosysToolsCfgManager = new TelosysToolsCfgManager(projectFolder);
		TelosysToolsCfg telosysToolsCfg = null ;
		try {
			//telosysToolsCfg = telosysToolsCfgManager.loadProjectConfig(); // Never null
			telosysToolsCfg = telosysToolsCfgManager.loadTelosysToolsCfg(); // v 3.0.0
		} catch (TelosysToolsException e) {
			MsgBox.error( "Cannot load TelosysTools configuration", e);
			//--- Create a default configuration
			//telosysToolsCfg = new TelosysToolsCfg(telosysToolsCfgManager.getProjectAbsolutePath());
			telosysToolsCfg = telosysToolsCfgManager.createDefaultTelosysToolsCfg(); // v 3.0.0
		}
		if ( ! telosysToolsCfg.hasBeenInitializedFromFile() ) {
			//--- No specific variables => initialize
			//String projectName = project.getName() ;
			List<Variable> vars = new LinkedList<Variable>();
			//-- Keep alphabetic order
			vars.add( new Variable("MAVEN_ARTIFACT_ID", projectName            ) ) ; // for pom.xml artifactId
			vars.add( new Variable("MAVEN_GROUP_ID",    "group.to.be.defined"  ) ) ; // for pom.xml artifactId
			vars.add( new Variable("PROJECT_NAME",      projectName            ) ) ; 
			vars.add( new Variable("PROJECT_VERSION",   "0.1"                  ) ) ; 
			//-- Set variables in current configuration
			telosysToolsCfg.setSpecificVariables(vars);
		}
		
		// return new ProjectConfig(project, telosysToolsCfg);
		//return new ProjectConfig(projectName, projectLocation, workspaceLocation, telosysToolsCfg); // v 3.0.0
		return telosysToolsCfg ;
	}
	
	//-------------------------------------------------------------------------------------------------
	/**
	 * Saves the given configuration in the "telosys-tools.cfg" file in the given project
	 * @param project
	 * @param projectConfig
	 */
//	public static void saveProjectConfig( IProject project, ProjectConfig projectConfig ) 
	public static void saveProjectConfig( IProject project, TelosysToolsCfg telosysToolsCfg ) 
	{
		String projectFolder = EclipseProjUtil.getProjectDir( project );
		PluginLogger.log("ProjectConfigManager.saveProjectConfig("+projectFolder+", properties)..." );
		
		TelosysToolsCfgManager cfgManager = new TelosysToolsCfgManager(projectFolder);
		try {
			//cfgManager.saveProjectConfig( projectConfig.getTelosysToolsCfg() ) ;
			//cfgManager.saveTelosysToolsCfg( projectConfig.getTelosysToolsCfg() ) ; // v 3.0.0
			cfgManager.saveTelosysToolsCfg( telosysToolsCfg ) ; // v 3.0.0
		} catch (TelosysToolsException e) {
			MsgBox.error( "Cannot save configuration", e);
		}

		//--- Refresh the file in the Eclipse Workspace
		String cfgFileAbsolutePath = cfgManager.getCfgFileAbsolutePath();
		File file = new File(cfgFileAbsolutePath);
		if ( file.exists() ) {
			EclipseWksUtil.refresh(file);
		}
		else {
			MsgBox.error( "Cannot refresh file \n"
					+ "'" + cfgFileAbsolutePath + "' \n"
					+ "This file doesn't exist");
		}
	}

	private static String getProjectName ( IProject project ) {
    	if ( project != null ) {
    		return project.getName();
    	}
    	else {
    		return "unknown" ; 
    	}
	}
	
//	private static String getProjectLocation ( IProject project ) {
//    	if ( project != null ) {
//    		return EclipseProjUtil.getProjectDir(project);
//    	}
//    	else {
//    		return "unknown" ; 
//    	}
//	}

	// v 3.0.0 : moved in EclipseProjUtil
//	private static String getWorkspaceLocation ( IProject project ) {
//    	IWorkspace wks = project.getWorkspace();
//    	IWorkspaceRoot wksRoot = wks.getRoot();
//    	IPath wksLocation = wksRoot.getLocation();
//    	if ( wksLocation != null ) {
//    		return wksLocation.toOSString() ;
//    	}
//    	else {
//    		return "unknown" ; 
//    	}
//	}
	
}
