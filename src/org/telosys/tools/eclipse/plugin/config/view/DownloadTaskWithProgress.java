package org.telosys.tools.eclipse.plugin.config.view;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Text;
import org.telosys.tools.commons.bundles.BundleStatus;
import org.telosys.tools.commons.bundles.BundlesManager;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;
import org.telosys.tools.eclipse.plugin.commons.TelosysPluginException;

/**
 * Eclipse runnable task with a progress bar 
 *  
 * @author L. Guerin
 *
 */
public class DownloadTaskWithProgress implements IRunnableWithProgress 
{
//	private final IProject  project ;
	private final String    user ;
	private final String[]  repoNames ;
//	private final String    sDownloadFolder ;
//	private final String    sBundlesFolder ;
//	private final String    sGitHubUrlPattern ;
	private final Text      loggerTextArea ;
	private final boolean   bInstall ;
	private final TelosysToolsCfg telosysToolsCfg ;
	
	private int   _result = 0 ;
	
	//--------------------------------------------------------------------------------------------------
	public DownloadTaskWithProgress(// IProject project, 
			TelosysToolsCfg telosysToolsCfg,
			String   user, 
			String[] repoNames, 
//			String   sDownloadFolder, 
//			String   sGitHubUrlPattern,
			boolean  bInstall,
//			String   sBundlesFolder, 
			Text     loggerTextArea 
			) throws TelosysPluginException
	{
		super();
		
		this.telosysToolsCfg = telosysToolsCfg ;
		
//		this.project = project ;
		this.user = user ;
		this.repoNames = repoNames ;
//		this.sDownloadFolder = sDownloadFolder ;
//		this.sGitHubUrlPattern = sGitHubUrlPattern ;
		this.bInstall = bInstall ;
//		this.sBundlesFolder = sBundlesFolder ;

		this.loggerTextArea = loggerTextArea ;
	}
	
	//--------------------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void run(IProgressMonitor progressMonitor) throws InvocationTargetException, InterruptedException 
	{
		int count = 0 ;
		if ( repoNames.length > 0 ) {
			
			BundlesManager bm = new BundlesManager( this.telosysToolsCfg );
			
			// count = total number of work units into which the main task is been subdivided
			int totalWorkTasks = repoNames.length ;
			progressMonitor.beginTask("Download in progress", totalWorkTasks + 1); 
			progressMonitor.worked(1);

			loggerTextArea.setText("");
			for ( String githubRepoName : repoNames ) {
				
				count++;
				
				progressMonitor.subTask("Download #" + count + " '" + githubRepoName + "'");

				BundleStatus status ;
				if ( bInstall ) {
					loggerTextArea.append("-> #" + count + " Download & Install '" + githubRepoName + "' ... \n");
					status = bm.downloadAndInstallBundle(this.user, githubRepoName);
				}
				else {
					loggerTextArea.append("-> #" + count + " Download '" + githubRepoName + "' ... \n");
					status = bm.downloadBundle(this.user, githubRepoName);
				}
				if ( status.isDone() ) {
					loggerTextArea.append("OK, done. \n");
				}
				else {
					loggerTextArea.append(status.getMessage() + "\n");
					if ( status.getException() != null ) {
						loggerTextArea.append("Exception : " + status.getException() + "\n" );
					}
				}
				EclipseWksUtil.refresh( new File(bm.getDownloadsFolderFullPath()) );
				if ( bInstall ) {
//					EclipseWksUtil.refresh( new File(bm.getBundlesFolderFullPath()) );
					EclipseWksUtil.refresh( new File(telosysToolsCfg.getTemplatesFolderAbsolutePath()) ); // v 3.0.0
				}
				progressMonitor.worked(1); // One unit done (not cumulative)
				loggerTextArea.append("\n");
			}
		}
		
		//--- Notifies that the work is done; that is, either the main task is completed or the user canceled it.
		progressMonitor.done();
		
		if (progressMonitor.isCanceled()) // Returns whether cancellation of current operation has been requested
		{
			throw new InterruptedException("The download was cancelled");
		}
		_result = count ;
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Returns the operation result : number of files generated
	 * @return
	 */
	public int getResult()
	{
		return _result ;
	}
}
