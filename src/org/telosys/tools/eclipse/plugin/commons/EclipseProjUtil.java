package org.telosys.tools.eclipse.plugin.commons;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.telosys.tools.commons.StrUtil;


/**
 * Utility class to manage Eclipse PROJECT RESOURCES
 * 
 * 
 * @author Laurent Guerin
 *
 */
public class EclipseProjUtil {

	/**
	 * Static class : no constructor 
	 */
	private EclipseProjUtil() {
	}
	
	private static void log(String s) 
	{
		//PluginLogger.log( EclipseProjUtil.class, s );
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * Returns the "full path" of the given project ( i.e. "C:/aaa/bbb/workspace/project" )
	 * 
	 * @param project
	 * @return
	 */
	public static String getProjectDir( IProject project ) 
	{
		if ( project != null )
		{
			IPath path = project.getLocation();
			if ( path != null )
			{
				// The standard toString method creates a platform-neutral encoding of the path as a String.		
				//return path.toString() ;	
				
				// The toOSString method creates a platform-specific encoding suitable for passing 
				// to java.io.File or other API that deals directly with the file system
				return path.toOSString() ;
			}
			else
			{
				MsgBox.error("getProjectDir() : Project location is null " );
				return null ;
			}
		}
		else
		{
			MsgBox.error("getProjectDir() : Project is null " );
			return null ;
		}
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * Returns the project resource (File or Folder) for the given relative path <br>
	 * or null if the resource doesn't exist
	 * @param project
	 * @param sPath ( '/aaa/bbb' or 'aaa/bbb' in the project ) 
	 * @return the resource ( or null if the resource doesn't exists )
	 */
	public static IResource getResource(IProject project, String sPath) 
	{
		log("getResource(String : '" + sPath + "')");	
		IResource resource = getResource( project, new Path(sPath) );
		log("getResource(String : '" + sPath + "') : return " + resource );
		return resource ;
	}
	
	/**
	 * Returns the project resource (File or Folder) for the given relative path <br>
	 * or null if the resource doesn't exist
	 * @param project
	 * @param path ( '/aaa/bbb' or 'aaa/bbb' in the project ) 
	 * @return the resource ( or null if the resource doesn't exists )
	 */
	public static IResource getResource(IProject project, Path path) 
	{
		log("getResource(Path : '" + path + "')");	
		// Path implements IPath
		IResource resource = project.getFile(path) ; // try to get a file
		if ( ! resource.exists() )
		{
			resource = project.getFolder(path); // try to get a folder
			if ( ! resource.exists() )
			{
				resource = null ;
			}
		}
		log("getResource(Path : '" + path + "') : return " + resource );
		return resource ;
	}

	//----------------------------------------------------------------------------------
	public static String getWorkspaceLocation ( IProject project ) {
    	IWorkspace wks = project.getWorkspace();
    	IWorkspaceRoot wksRoot = wks.getRoot();
    	IPath wksLocation = wksRoot.getLocation();
    	if ( wksLocation != null ) {
    		return wksLocation.toOSString() ;
    	}
    	else {
    		return "unknown" ; 
    	}
	}

	//----------------------------------------------------------------------------------
	/**
	 * Returns the project absolute path relative to the containing workspace <br>
	 * Example : returns "/myproject/aa/bb/file" for "aa/bb/file"
	 * @param project
	 * @param sPath the path in the project 
	 * @return
	 */
	public static String getAbsolutePathInWorkspace(IProject project, String sPath) 
	{
		IFile file = project.getFile( new Path(sPath) ) ; 
		IPath p = file.getFullPath();
		return p.toString() ;
	}

	/**
	 * Returns the filesystem absolute path <br>
	 * Example : returns "D:/aaa/bbbb/workspace/myproject/aa/bb/file" for "aa/bb/file"
	 * @param project
	 * @param sPath
	 * @return
	 */
	public static String getAbsolutePathInFileSystem(IProject project, String sPath) 
	{
		IResource resource = project.getFile( new Path(sPath) ) ;
		IPath ipath = resource.getLocation(); // OS Path ( D:/aaa/bbbb/workspace/myproject/aa/bb/file )
		File file = ipath.toFile();
		return file.getAbsolutePath();
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * Returns a standard file instance for the given relative path in the project <br>
	 * or null if the file doesn't exist
	 * @param project
	 * @param sPath ( '/aaa/bbb' or 'aaa/bbb' ) 
	 * @return the file, or null 
	 */
	public static File getResourceAsFile(IProject project, String sPath) 
	{
		log("getResourceAsFile(String : '" + sPath + "')");	
		File file = getResourceAsFile( project, new Path(sPath) );
		log("getResourceAsFile(String : '" + sPath + "') : return " + file );
		return file ;
	}
	
	/**
	 * Returns a standard file instance for the given relative path in the project <br>
	 * or null if the file doesn't exist
	 * @param project
	 * @param path ( '/aaa/bbb' or 'aaa/bbb' ) 
	 * @return 
	 */
	private static File getResourceAsFile(IProject project, Path path) 
	{
		File file = null ;
		log("getResourceAsFile(Path : '" + path + "')");	
		IResource resource = getResource(project, path);
		if ( resource != null )
		{
			log("getResourceAsFile() : resource exists ? " + resource.exists() );	
			log("getResourceAsFile() : resource getLocation() : " + resource.getLocation() );	
			log("getResourceAsFile() : resource getFullPath() : " + resource.getFullPath() );	
			IPath ipath = resource.getLocation(); // OS Path ( D:/aaa/bbbb/workspace/myproject/aa/bb/file )
			//IPath ipath = resource.getFullPath();
			file = ipath.toFile();
			if ( ! file.exists() ) // never happends
			{
				file = null ;
			}
		}
		log("getResourceAsFile(Path : '" + path + "') : return " + file );
		return file ;
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * Refresh the given project file 
	 * @param project
	 * @param path file path ( '/aaa/bbb.txt' or 'aaa/bbb.txt' ) 
	 */
	public static void refreshFile(IProject project, String path) 
	{
		refreshFile( project, new Path(path) );
	}
	
	/**
	 * Refresh the given project folder 
	 * @param project
	 * @param path resource path ( '/aaa/bbb' or 'aaa/bbb' ) 
	 */
	public static void refreshFolder(IProject project, String path) 
	{
		refreshFolder( project, new Path(path) );
	}
	
	/**
	 * Refresh the given project file 
	 * @param project
	 * @param path file path ( '/aaa/bbb.txt' or 'aaa/bbb.txt' ) 
	 */
	public static void refreshFile(IProject project, Path path) 
	{
		IFile iFile = project.getFile( path ) ;
		//if ( iFile.exists() ) { 
		// Do not test existence here 
		// Unknown file while not refreshed => considered as non existent !
			try {
				iFile.refreshLocal(IResource.DEPTH_ZERO, null);
			} catch (CoreException e) {
				MsgBox.error("Cannot refresh file '" + path + "' !", e );
			}
//		}
//		else {
//			MsgBox.error("Cannot refresh file '" + path + "' !\n" + "This file doesn't exist" );
//		}
	}
	
	/**
	 * Refresh the given project folder
	 * @param project
	 * @param path folder path ( '/aaa/bbb' or 'aaa/bbb' ) 
	 */
	public static void refreshFolder(IProject project, Path path) 
	{
		IFolder iFolder = project.getFolder( path ) ;
//		if ( iFolder.exists() ) {
			try {
				iFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e) {
				MsgBox.error("Cannot refresh folder '" + path + "' !", e);
			}
//		}
//		else {
//			MsgBox.error("Cannot refresh folder '" + path + "' !\n" + "This folder doesn't exist" );
//		}
	}
	
	/**
	 * Refresh the given project resource (file or folder)
	 * @param project
	 * @param path resource path ( '/aaa/bbb' or 'aaa/bbb' ) 
	 */
	public static void refreshResource(IProject project, Path path) 
	{
		IResource iResource = project.getFile( path ) ;
		if ( iResource.exists() ) {
			try {
				if ( iResource instanceof IFile ) {
					IFile iFile = (IFile) iResource ;
					iFile.refreshLocal(IResource.DEPTH_ZERO, null);
				}
				else if ( iResource instanceof IFolder ) {
					IFolder iFolder = (IFolder) iResource ;
					iFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
				}
			} catch (CoreException e) {
				MsgBox.error("Cannot refresh '" + path + "' !" , e );
			}
		}
		else {
			MsgBox.error("Cannot refresh '" + path + "' !\n" + "This resource doesn't exist" );
		}
	}
	
    //------------------------------------------------------------------------------------------------
    /**
     * Return the "Eclipse Java Model" for the "Eclipse Workspace"
     * @return
     */
    public static IJavaModel getJavaModel()
    {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        if ( workspace != null )
        {
            IJavaModel javaModel = JavaCore.create(workspace.getRoot());
            return javaModel;
        }
        else
        {
        	MsgBox.error("Cannot get JavaModel because workspace is null !");
        	return null ;
        }
    }
    
    //------------------------------------------------------------------------------------------------
    /**
     * Return the "Eclipse Java Project" (interface) for a given project name
     * @param sProjectName
     * @return IJavaProject object 
     */
    public static IJavaProject getJavaProject(String sProjectName)
    {
        IJavaModel javaModel = getJavaModel() ;
        if ( javaModel != null )
        {
            return javaModel.getJavaProject(sProjectName);
        }
        else
        {
        	MsgBox.error("Cannot get JavaProject because JavaModel is null !");
        	return null ;
        }
    }

    //------------------------------------------------------------------------------------------------
    /**
     * Returns true if the given project is a Java project
     * @param project
     * @return
     */
    public static boolean isJavaProject(IProject project)
    {
    	try {
    		return project.hasNature(JavaCore.NATURE_ID) ;
		} catch (CoreException ce) {
        	MsgBox.error("Cannot get project nature (project is null or not open) \n\n isJavaProject(project) ", ce);
        	return false ;
		} catch (Exception e) {
	    	MsgBox.error("Cannot get project nature\n\n isJavaProject(project) ", e);
	    	return false ;
		}
    }
    //------------------------------------------------------------------------------------------------
    public static IJavaProject getJavaProject(IProject project)
    {
    	if ( isJavaProject(project) ) {
    		return JavaCore.create(project);
    	}
    	else {
        	MsgBox.error("This project is not a Java project \n\n getJavaProject(project) ");
        	return null ;
    	}
    }
	
    //------------------------------------------------------------------------------------------------
    /**
     * Returns the list of JAR files defined in the Java Project PATH ( "Java Build Path" )
     * 
     * @return
     */
    public static String[] getClassPathLibraries( IProject project )
    {
    	IJavaProject javaProject = getJavaProject(project);
    	if ( javaProject != null ) {
    		List<String> libraries = getProjectClassPathLibraries(javaProject);
    		return libraries.toArray(new String[0]);
    	}
    	else {
    		return new String[0] ;
    	}
    }

    //------------------------------------------------------------------------------------------------
    private static List<String> getProjectClassPathLibraries(IJavaProject project) //throws JavaModelException
    {
        List<String> libraries = new ArrayList<String>();
    
        IClasspathEntry classpathEntries[];
		try {
			classpathEntries = project.getResolvedClasspath(false);
		} catch (JavaModelException e) {
			MsgBox.error("Cannot get Java libraries for this project.\n\nCheck the 'build path' ");
			e.printStackTrace();
			return libraries ;
		}
		
        for (int i = 0; i < classpathEntries.length; i++) 
        {
            IClasspathEntry entry = classpathEntries[i];
            //----- New 
            IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(entry.getPath());
            if (resource != null) 
            {
            	//--- Project's resource 
            	PluginLogger.log("Project resource : '"+entry.getPath()+"'");
            	java.io.File file = new java.io.File( resource.getLocation().toPortableString() ) ;
            	libraries.add( file.getAbsolutePath() );
            } 
            else 
            {
            	//--- External resource : Java JRE, JAR added with "Add external JAR"
            	PluginLogger.log("External resource : '"+entry.getPath()+"'");
            	java.io.File file = entry.getPath().toFile() ;
            	libraries.add( file.getAbsolutePath() );
            }
        }
        return libraries;
    }
    
    /**
     * Returns all the source folders for the given project 
     * @param project
     * @return
     */
    public static String[] getSrcFolders(IProject project) {
    	LinkedList<String> srcFolders = new LinkedList<String>();
        IJavaProject javaProject = JavaCore.create(project);
        IClasspathEntry[] entries;
		try {
			entries = javaProject.getRawClasspath();
		} catch (Exception e) {
			MsgBox.error("Cannot get JavaProject raw class path !");
			return new String[0];
		}
        for (IClasspathEntry classPathEntry : entries ) {
            if (classPathEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                IPath path = classPathEntry.getPath();
                IFolder srcFolder = project.getWorkspace().getRoot().getFolder(path);
    			IPath relativePath = srcFolder.getProjectRelativePath() ; // "src", "src/main/java", ... 
    			srcFolders.add(relativePath.toString());
            }
        }        
        return srcFolders.toArray(new String[0]);
    }	
    
    /**
     * Returns true if the folder exists
     * @param project
     * @param folderName
     * @return
     */
    public static boolean folderExists(IProject project, String folderName ) {
        IFolder folder = project.getFolder(folderName);
        return folder.exists() ;
    }
    
    /**
     * Tries to create the given folder ( with all sub-folders if any )
     * @param project
     * @param folderName
     * @return true if created, false if already exists (or if an error occurs )
     */
    public static boolean createFolder(IProject project, String folderName ) {
        if ( project.getFolder(folderName).exists() )  {
        	// Already exists
        	return false ;
        }
        else {
        	StringBuffer sb = new StringBuffer();
        	String[] parts = StrUtil.split(folderName, '/') ;
        	for ( int i = 0 ; i < parts.length ; i++ ) {
        		if ( i > 0 ) {
        			sb.append("/");
        		}
        		sb.append(parts[i]);
                IFolder folder = project.getFolder( sb.toString() );
                if (!folder.exists())  {
            		try {
                        folder.create(IResource.NONE, true, null);
            		} catch (CoreException e) {
            			MsgBox.error("Cannot create folder '" + folder.getName() + "' !\n", e);
            			return false ;
            		}
                }
        	}
            return true ;
        }
    }
    
	// The VIEW ID in "plugin.xml"
	final static String PROPERTIES_PAGE_ID = "org.telosys.tools.eclipse.plugin.config.view.PropertiesPage" ; 

	/**
	 * Opens the Telosys project properties page programmatically 
	 * @param project
	 */
	public static void openTelosysPropertiesView(IProject project) {
		if ( project != null ) {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			PreferenceDialog dialog = PreferencesUtil.createPropertyDialogOn(shell, 
					project, 
					PROPERTIES_PAGE_ID, 
					new String[] { PROPERTIES_PAGE_ID }, 
					null);
			dialog.open();
		}
		else {
			MsgBox.error("Cannot open properties view : the given project is null ");
		}
	}

}
