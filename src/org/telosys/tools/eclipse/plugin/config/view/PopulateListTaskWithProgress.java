package org.telosys.tools.eclipse.plugin.config.view;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.List;
import org.telosys.tools.commons.bundles.BundlesFromGitHub;
import org.telosys.tools.commons.bundles.BundlesManager;
import org.telosys.tools.commons.bundles.BundlesNames;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;

/**
 * Eclipse runnable task with a progress bar 
 *  
 * @author L. Guerin
 *
 */
public class PopulateListTaskWithProgress implements IRunnableWithProgress 
{
	private final String    userName ;
	private final List      swtListOfBundles ;
	private final TelosysToolsCfg telosysToolsCfg ;

	//--------------------------------------------------------------------------------------------------
	public PopulateListTaskWithProgress( TelosysToolsCfg telosysToolsCfg, String sGitHubUserName, List swtListToBePopulated )
	{
		super();
		this.telosysToolsCfg = telosysToolsCfg ;
		this.userName = sGitHubUserName ;
		this.swtListOfBundles = swtListToBePopulated ;
	}
	
	private BundlesFromGitHub getGitHubBundles() {
		BundlesManager bm = new BundlesManager( this.telosysToolsCfg );
		// Request GitHub 
		BundlesFromGitHub bundlesFromGitHub = null;
		try {
			bundlesFromGitHub = bm.getGitHubBundlesList(this.userName);
		} catch (Exception e) {
			bundlesFromGitHub = null ;
			MsgBox.error("Cannot get bundles from GitHub", e);
		}
		return bundlesFromGitHub;
	}

	//--------------------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void run(IProgressMonitor progressMonitor) throws InvocationTargetException, InterruptedException 
	{
		int totalWorkTasks = 3 ;
		progressMonitor.beginTask("Requesting GitHub server ...", totalWorkTasks ); 
		progressMonitor.worked(1);
		
		swtListOfBundles.removeAll();
		
//		BundlesManager bm = new BundlesManager( this.telosysToolsCfg );

//		java.util.List<String> bundles = null ;
//		try {
//			//bundles = bm.getBundlesList(userName) ;
//			bundles = bm.getGitHubBundlesList(userName) ;
//		} catch (Exception e1) {			
//			MsgBox.error("Cannot get bundles from GitHub", e1);
//		}

		// Get bundles from GitHub 
//		BundlesFromGitHub bundlesFromGitHub;
//		try {
//			bundlesFromGitHub = bm.getGitHubBundlesList(userName);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		BundlesFromGitHub bundlesFromGitHub = getGitHubBundles();
		progressMonitor.worked(2);

		// Use the result ( bundles or error )
		if ( bundlesFromGitHub != null ) {
			int httpStatusCode = bundlesFromGitHub.getHttpStatusCode();
			if ( httpStatusCode == 200 ) {
				BundlesNames bundleNames = bundlesFromGitHub.getBundlesNames();
				// Populate combo box 
				for ( String s : bundleNames.getAll() ) {
					swtListOfBundles.add( s );
				}
			}
			else if ( httpStatusCode == 403 ) {
				StringBuffer sb = new StringBuffer();
				sb.append("Cannot get bundles from GitHub." ) ;
				sb.append("\n" ) ;
				sb.append("\n GitHub API http status '" + httpStatusCode + "'" ) ;
				sb.append("\n GitHub API rate limit status : " ) ; 
				sb.append("\n  . remaining : " + bundlesFromGitHub.getRemaining() ) ; 
				sb.append("\n  . limit     : " + bundlesFromGitHub.getLimit() ) ; 
				sb.append("\n  . reset     : " + bundlesFromGitHub.getReset() ) ; 
				MsgBox.error(sb.toString());
			}
			else {
				StringBuffer sb = new StringBuffer();
				sb.append("Cannot get bundles from GitHub, unexpected http status code" ) ;
				sb.append("\n" ) ;
				sb.append("\n GitHub API http status '" + httpStatusCode + "'" ) ;
				MsgBox.error(sb.toString());
			}
			
		}

//		progressMonitor.worked(1);
//		if ( bundles != null ) {
//			for ( String s : bundles ) {
//				swtListOfBundles.add( s );
//			}
//		}
		//--- Notifies that the work is done; that is, either the main task is completed or the user canceled it.
		progressMonitor.done();		
	}	
}
