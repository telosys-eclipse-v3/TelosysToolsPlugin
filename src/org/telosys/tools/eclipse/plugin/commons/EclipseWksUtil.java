package org.telosys.tools.eclipse.plugin.commons;

import java.io.File;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.telosys.tools.commons.FileUtil;


/**
 * Utility class to manage WORKSPACE RESOURCES
 * 
 * Conversion to standard Java classes :
 * . IPath     : File file = path.toFile()
 * . IResource : File file = res.getLocation().toFile()
 * . IFolder   : File file = folder.getLocation().toFile() 
 * 
 * @author Laurent Guerin
 *
 */
public class EclipseWksUtil {

	/**
	 * Static class : no constructor 
	 */
	private EclipseWksUtil() {
	}
	
	private static void log(String s) {
		//PluginLogger.log( EclipseWksUtil.class, s );
	}
	
	/**
	 * Returns the Eclipse workspace (set of resources : projects, files, ...)
     * Based on "ResourcesPlugin.getWorkspace()"
	 * 
	 * @return the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}
	
	/**
	 * @return the current workspace root
	 */
	public static IWorkspaceRoot getWorkspaceRoot() {
		return getWorkspace().getRoot();
	}
	
	/**
	 * @return the projects defined in the eclipse workspace
	 */
	public static IProject[] getWorkspaceProjects() {
		IProject[] projects = getWorkspace().getRoot().getProjects();
		return projects;
	}
	
	/**
	 * Return the workspace location in the Operating System ( filesystem absolute path ) <br>
	 * e.g. 'D:/foo/bar/myworkspace'
	 * 
	 * @return
	 */
	public static String getWorkspaceLocation() {
		IWorkspace wks = getWorkspace();
		IWorkspaceRoot wksRoot = wks.getRoot();
		IPath wksLocation = wksRoot.getLocation();
		if ( wksLocation != null ) {
			return wksLocation.toString() ;
		}
		else {			
			throw new IllegalStateException("Cannot get workspace location");
		}
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * Returns the full path for the given resource path in the workspace <br>
	 * 
	 * @param sPath ( 'myproject/folder/xxx' )
	 * @return the full path if given resource exists, else null ( '/myproject/folder/xxx' )
	 */
	public static IPath getIPath(String sPath) 
	{
		log("getFullPath(String : '" + sPath + "')");	
		IPath p = getIPath( new Path(sPath) );
		log("getFullPath(String : '" + sPath + "') : return '" + p + "'");	
		return p ;
	}
	
	/**
	 * Returns the full path for the given resource path in the workspace <br>
	 * 
	 * @param path the resource path in the workspace ( 'myproject/folder/xxx' )
	 * @return the full path if given resource exists, else null ( '/myproject/folder/xxx' )
	 */
	private static IPath getIPath(Path path) 
	{
		IPath p = null ;
		log("getFullPath(Path : '" + path + "')");	
		IResource resource = getWorkspaceRoot().findMember(path);
		if ( resource == null )
		{
			MsgBox.error("Cannot find '" + path + "' in the workspace !");
			p = null ;
		}
		else
		{
			if ( resource.exists() )
			{
				log("Resource '" + path + "' exists ");	
				p = resource.getFullPath();
			}
			else
			{
				log("Resource '" + path + "' doesn't exist ");						
				p = null ;
			}
		}
		log("getFullPath(Path : '" + path + "') : return '" + p + "'");	
		return p ;
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * Returns the workspace resource for the given workspace path <br>
	 * or null if the resource doesn't exist
	 * @param sPath ( 'myproject/aaa/bbb' or '/myproject/aaa/bbb' ) 
	 * @return
	 */
	public static IResource getResource(String sPath) 
	{
		log("getResource(String : '" + sPath + "')");	
		IResource resource = getResource( new Path(sPath) );
		log("getResource(String : '" + sPath + "') : return " + resource );
		return resource ;
	}
	
	/**
	 * Returns the workspace resource for the given workspace path <br>
	 * or null if the resource doesn't exist
	 * @param path ( 'myproject/aaa/bbb' or '/myproject/aaa/bbb' ) 
	 * @return
	 */
	public static IResource getResource(Path path) 
	{
		log("getResource(Path : '" + path + "')");	
		IResource resource = getWorkspaceRoot().findMember(path);
		log("getResource(Path : '" + path + "') : return " + resource );
		return resource ;
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * Returns a standard file instance for the given workspace path <br>
	 * or null if the file doesn't exist
	 * @param sPath ( 'myproject/aaa/bbb' or '/myproject/aaa/bbb' ) 
	 * @return
	 */
	public static File getResourceAsFile(String sPath) 
	{
		log("getResourceAsFile(String : '" + sPath + "')");	
		File file = getResourceAsFile( new Path(sPath) );
		log("getResourceAsFile(String : '" + sPath + "') : return " + file );
		return file ;
	}
	
	/**
	 * Returns a standard file instance for the given workspace path <br>
	 * or null if the file doesn't exist
	 * @param path ( 'myproject/aaa/bbb' or '/myproject/aaa/bbb' ) 
	 * @return 
	 */
	public static File getResourceAsFile(Path path) 
	{
		File file = null ;
		log("getResourceAsFile(Path : '" + path + "')");	
		IResource resource = getResource(path);
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
		else {
			log("Cannot get resource : getResource('" + path + "') return null ");	
		}
		log("getResourceAsFile(Path : '" + path + "') : return " + file );
		return file ;
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * Creates the given folder in the current workspace root
	 * @param sFolderPath
	 * @return
	 */
	public static IFolder createFolder(String sFolderPath)
	{
		return createFolder( new Path(sFolderPath) );
	}

	/**
	 * Creates the given folder in the current workspace root
	 * @param path : absolute path in the WORKSPACE including the project folder ( "myproject/src", "myproject/doc/foo" ) 
	 * @return
	 */
	public static IFolder createFolder(IPath path)
	{
		log("createFolder( IPath ) : path = " + path );

		IFolder folder = getWorkspaceRoot().getFolder(path);
		IContainer parent = folder.getParent();
		if ( parent != null )
		{
			log("createFolder( IPath ) : parent path = " + parent.getFullPath() );
			//--- Create parent folder first if necessary (recursivly)
			if ( parent instanceof IFolder && !parent.exists()) 
			{
				createFolder(parent.getFullPath());
			} 
			//--- Create the folder itself
			try {
				folder.create(true, true, null);
			} catch (CoreException e) {
				MsgBox.error("Cannot create folder '" + folder.getFullPath() + "'", e );
				return null ;
			}
			return folder ;
		}
		else
		{
			MsgBox.error("Folder '" + folder.getFullPath() + "' parent is null " );
			return null ;
		}
	}
	
	//----------------------------------------------------------------------------------
	public static IFolder toIFolder(File file)
	{
		log("toIFolder( File ) : file = " + file.getAbsolutePath() );
		String sAbsolutePath = file.getAbsolutePath();
		IPath path = new Path( sAbsolutePath );
//		IFile iFile = root.getFileForLocation(path);
//		IFolder iFolder = root.getFolder(path);
		
		return toIFolder(path);
	}
	//----------------------------------------------------------------------------------
	public static IFolder toIFolder(IPath path)
	{
		log("toIFolder( IPath ) : path = " + path );
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		if ( root != null )
		{
			/*
			 * IWorkspaceRoot - getContainerForLocation(path)
			 * Returns a handle to the workspace root, project or folder which is mapped to the given path 
			 * in the local file system, or null if none. If the path maps to the platform working location, 
			 * the returned object will be of type ROOT. If the path maps to a project, 
			 * the resulting object will be of type PROJECT; otherwise the resulting object will be a folder (type FOLDER). 
			 * The path should be absolute; a relative path will be treated as absolute. 
			 * The path segments need not be valid names; a trailing separator is ignored. 
			 * The resulting resource may not currently exist.
			 * 
			 * This method returns null when the given file system location is not equal to or under the location 
			 * of any existing project in the workspace, or equal to the location of the platform working location.
			 * The result will also omit resources that are explicitly excluded from the workspace according to existing resource filters.
			 * 
			 * Warning: This method ignores linked resources and their children. Since linked resources may overlap other resources, 
			 * a unique mapping from a file system location to a single resource is not guaranteed. 
			 * To find all resources for a given location, including linked resources, use the method findContainersForLocation.
			 */
			IContainer iContainer = root.getContainerForLocation(path);
			if ( iContainer != null )
			{
				if ( iContainer instanceof IFolder ) {
					return (IFolder) iContainer;
				}
				else {
					MsgBox.error("toIFolder(file) : the IContainer is not a FOLDER" );
					return null ;
				}
			}
			else
			{
				MsgBox.error("toIFolder(file) : root.getContainerForLocation('" + path + "') return NULL" );
				return null ;
			}
		}
		else
		{
			MsgBox.error("toIFolder(file) : Cannot get workspace root !" );
			return null ;
		}
	}
	//----------------------------------------------------------------------------------
	/**
	 * Converts the given filesystem absolute path to an Eclipse workspace "IFile" object <br>
	 * Returns null if the given file is not under the location of the workspace
	 * @param file
	 * @return 
	 */
	public static IFile toIFile(String absoluteFilePath) {
		return toIFile( new File(absoluteFilePath) );
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * Converts the given filesystem "File" to an Eclipse workspace "IFile" object <br>
	 * Returns null if the given file is not under the location of the workspace
	 * @param file
	 * @return 
	 */
	public static IFile toIFile(File file) {
		log("toIFile( File ) : file = " + file.getAbsolutePath() );
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		if ( root != null ) {
			String sAbsolutePath = file.getAbsolutePath();
			IPath path = new Path( sAbsolutePath );
			/*
			 * getFileForLocation(path) :
			 *  The path should be absolute; a relative path will be treated as absolute. 
			 *  The path segments need not be valid names. 
			 *  The resulting file need not exist in the workspace.
			 *  This method returns null when the given file system location 
			 *  is not under the location of any existing project in the workspace.
			 */
			IFile iFile = root.getFileForLocation(path);
			if ( iFile != null ) {
				return iFile ;
			}
			else {
				//MsgBox.error("toIFile(file) : getFileForLocation('" + sAbsolutePath + "') return NULL" );
				return null ;
			}
		}
		else {
			//MsgBox.error("toIFile(file) : Cannot get workspace root !" );
			return null ;
		}
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * Returns the filesystem absolute path for the given Eclipse "IFile" 
	 * @param iFile
	 * @return
	 */
	public static String getAbsolutePath(IFile iFile) {
    	File file = toFile(iFile);
    	return file.getAbsolutePath();
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * Converts the given Eclipse "IFile" to a standard "File" instance<br>
	 * If the IFile has been removed returns a File that doesn't exist
	 * @param iFile
	 * @return
	 */
	public static File toFile(IFile iFile) {
		
//		log("toFile( IFile iFile ) : iFile.getLocationURI() = " + iFile.getLocationURI() ); // NB : can be null if iFile has been deleted 
//		log("toFile( IFile iFile ) : iFile.getLocation() = " + iFile.getLocation() ); // NB : can be null if iFile has been deleted 
//		log("toFile( IFile iFile ) : iFile.getFullPath() = " + iFile.getFullPath() ); // Full path in workspace never null
		
/***
		IWorkspace wks = getWorkspace();
//		log("toFile( IFile iFile ) : workspace root getLocation() = " + wks.getRoot().getLocation() ); // 
//		log("toFile( IFile iFile ) : workspace root getFullPath() = " + wks.getRoot().getFullPath() ); // 
		
//    	URI uri = iFile.getLocationURI(); // NB : can be null if iFile has been deleted 
//    	File file = new File(uri); // Throws "NullPointerException" if uri is null
    	
		// ver 3.0.0
		
//    	IPath iPath = iFile.getFullPath() ; 
//		log("toFile( IFile iFile ) : IFile getFullPath toOSString = " + iPath.toOSString() );
//    	//File file = iPath.toFile() ; // returns 'D:\project\TelosysTools\zzz_model' !!!
//    	File file = new File(iPath.toOSString());
//		log("toFile( IFile iFile ) : return : file.getAbsolutePath() = " + file.getAbsolutePath() );

//    	IPath iPath = iFile.getLocation() ; // Absolute filesystem path : 'D:/aaa/wks/project/TelosysTools/zzz_model'
//    	File file = iPath.toFile() ; // returns 'D:\project\TelosysTools\zzz_model' !!!
***/
		
		String workspaceLocation = getWorkspaceLocation();
//		log(" workspace location = '" + workspaceLocation + "'");
		
		//String fileFullPathInworkspace = iFile.getFullPath().toOSString();
		String fileFullPathInworkspace = iFile.getFullPath().toString();
//		log(" file path in workspace = '" + fileFullPathInworkspace + "'");
		
		String fileAbsolutePath = FileUtil.buildFilePath(workspaceLocation, fileFullPathInworkspace);
//		log("file absolute path = '" + fileAbsolutePath + "'");
		
		return new File(fileAbsolutePath) ;
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * Refresh the given O.S. file in the Eclipse workspace
	 * @param file the file to be refreshed
	 */
	public static void refresh(File file) {
		log("refresh( File ) " );
		if ( null == file ) {
			MsgBox.error("refresh(File) : parameter is null !" );
			return ;
		}
		
		if ( file.isDirectory() ) {
			//--- FOLDER
			IFolder iFolder = toIFolder(file);
			if ( null != iFolder )
			{
				log("refreshing IFolder = " + iFolder );				
				try {
					iFolder.refreshLocal(IResource.DEPTH_INFINITE , null);
				} catch (CoreException e) {
					// MsgBox.error("Cannot refresh FOLDER resource '" + iFolder + "'", e );
					// The dialog should be created in UI thread => No MsgBox.error()
					throw new RuntimeException("Cannot refresh folder", e);
				}
			}
		}
		else {
			//--- FILE
//			IFile iFile = toIFile(file);
//			if ( null != iFile )
//			{
//				log("refreshing IFile = " + iFile );				
//				try {
//					iFile.refreshLocal(IResource.DEPTH_ZERO, null);
//				} catch (CoreException e) {
//					// MsgBox.error("Cannot refresh FILE resource '" + iFile + "'", e );
//					// The dialog should be created in UI thread => No MsgBox.error()
//					throw new RuntimeException("Cannot refresh file", e);
//				}
//			}
			refresh( toIFile(file) );
		}
	}
	
	public static void refresh(IFile iFile) {
		if ( iFile != null ) {
			log("refreshing IFile = " + iFile );				
			try {
				iFile.refreshLocal(IResource.DEPTH_ZERO, null);
			} catch (CoreException e) {
				// MsgBox.error("Cannot refresh FILE resource '" + iFile + "'", e );
				// The dialog should be created in UI thread => No MsgBox.error()
				throw new RuntimeException("Cannot refresh file", e);
			}
		}
	}	
	//----------------------------------------------------------------------------------
	public static boolean setBuildAutomatically(boolean flag)
	{
		IWorkspace workspace = getWorkspace() ;
		final IWorkspaceDescription desc = workspace.getDescription();
		boolean originalFlag = desc.isAutoBuilding();
		desc.setAutoBuilding(flag);
		try {
			workspace.setDescription(desc);
		} catch (CoreException e) {
			MsgBox.error("setBuildAutomatically : Cannot set workspace description ", e );
		}
		return originalFlag ;
	}	
//	//----------------------------------------------------------------------------------
//	/**
//	 * Refresh the given resource 
//	 * @param resource the resource to be refreshed ( FILE or FOLDER )
//	 */
//	private static void refresh(IResource resource)
//	{
//		log("refresh( IResource )..."  );
//		if ( resource == null )
//		{
//			MsgBox.error("refresh(resource) : parameter is null !" );
//		}
//		log("refresh( IResource ) : resource = " + resource.getFullPath() );
//		try {
//			int type = resource.getType();
//			switch ( type ) {
//			case IResource.FILE :
//				log("refresh for type FILE : resource = " + resource.getFullPath() );
//				resource.refreshLocal(IResource.DEPTH_ZERO, null);
//				break ;
//			case IResource.FOLDER :
//				log("refresh for type FOLDER : resource = " + resource.getFullPath() );
//				resource.refreshLocal(IResource.DEPTH_INFINITE, null);
//				break ;
//			default :
//				MsgBox.error("Cannot refresh resource. Not a file or folder" );
//				break;
//			}
//		} catch (CoreException e) {
//			MsgBox.error("Cannot refresh resource '" + resource, e );
//		}
//	}
}
