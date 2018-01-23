package org.telosys.tools.eclipse.plugin.commons.unused;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;

/**
 * @author Laurent GUERIN
 *
 */
/* public */ class JModel {

	public final static IPackageFragment getPackageFragment( IJavaProject project, String sName )
	{
		IPackageFragment[] packages = null;
		try {
			packages = project.getPackageFragments() ;
		} catch (JavaModelException e) {
			MsgBox.error("ERROR in getPackageFragment(..,..) \n"
					+ "Cannot get project package fragments : JavaModelException", e );
			packages = null ;
		}
		if ( packages != null )
		{
			IPackageFragment pf = null ;
			for ( int i=0 ; i < packages.length ; i++ )
			{
				pf = packages[i];
				if ( sName.equals( pf.getElementName() ) )
				{
					//--- Found 
					return pf;
				}
			}
		}
		return null ;	
	}
	
	public final static IType getJavaType( IJavaElement javaElement )
	{
		if (javaElement != null) {
			if ( javaElement instanceof IType )
			{
				return (IType) javaElement ;
			}
			else if ( javaElement instanceof ICompilationUnit )
			{
				ICompilationUnit cu = (ICompilationUnit) javaElement ;
				return cu.findPrimaryType() ;
			}
		}
		return null ;
	}
	
	public final static String getSuperClass( IType type )
	{
		String sSuperClass = null; 
		if ( type != null )
		{
			try {
				sSuperClass = type.getSuperclassName();
			} catch (JavaModelException e) {
				MsgBox.error("ERROR in getSuperClass("+type+") \n"
						+ "JavaModelException", e );
				
				sSuperClass = null; 
			}
		}
		return sSuperClass ; 
	}
	
	public final static String getSuperClass( IJavaElement javaElement )
	{
		return getSuperClass ( getJavaType( javaElement ) ) ;
	}
	
	
	/**
	 * Returns the IJavaProject instance corresponding to the given IProject (Resource) instance
	 * @param project
	 * @return
	 */
	public final static IJavaProject toJavaProject( IProject project )
	{
		if ( project == null )
		{
			return null ;
		}
		
		IJavaProject javaProject = null ;
		try {
			IJavaElement javaElement = (IJavaElement) project.getAdapter(IJavaElement.class);
			javaProject = javaElement.getJavaProject();
		} catch (Exception e) {
			MsgBox.error("ERROR in toJavaProject("+project+").", e );
		}
		return javaProject ;
	}
	
	/**
	 * Returns the IProject (Resource) instance corresponding to the given IJavaProject instance
	 * @param javaProject
	 * @return
	 */
	public final static IProject toProject( IJavaProject javaProject )
	{
		if ( javaProject == null )
		{
			return null ;
		}
		IResource r = javaProject.getResource();
		if ( r instanceof IProject )
		{
			return (IProject) r ;
		}
		else
		{
			MsgBox.error("ERROR in toProject("+javaProject+") \n"
					+ "the parameter is not a Project resource" );
			return null ;
		}
	}
}
