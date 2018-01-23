package org.telosys.tools.eclipse.plugin.wkschanges;

import java.io.File;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

public class RefreshJob extends WorkspaceJob  {

	private static void log(String msg) {
		if ( _PackageLoggerConfig.LOG ) {
			PluginLogger.log(RefreshJob.class, msg);
		}
	}

	private final File modelsFolder ;
	
	public RefreshJob(File modelsFolder) {
		super("RefreshJob");
		this.modelsFolder = modelsFolder ;
	}

	@Override
	public IStatus runInWorkspace(IProgressMonitor progressMonitor) throws CoreException {
		log("RefreshJob : runInWorkspace() : refresh " + modelsFolder.getName());
		if ( modelsFolder.exists() ) {
			log("RefreshJob : runInWorkspace() : the folder exists => launch refresh" );
			EclipseWksUtil.refresh(modelsFolder); // refresh the models folder ( = "TelosysTools" folder ) 
		}
		else {
			log("RefreshJob : runInWorkspace() : the folder doesn't exist => nothing to do" );			
		}
		return Status.OK_STATUS;
	}

}
