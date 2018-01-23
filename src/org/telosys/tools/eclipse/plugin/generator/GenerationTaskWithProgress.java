package org.telosys.tools.eclipse.plugin.generator;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.io.CopyHandler;
import org.telosys.tools.commons.io.OverwriteChooser;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.Util;
import org.telosys.tools.eclipse.plugin.commons.dialogbox.GenerationTaskMsgBox;
import org.telosys.tools.generator.context.Target;
import org.telosys.tools.generator.task.AbstractGenerationTask;
import org.telosys.tools.generator.task.ErrorReport;
import org.telosys.tools.generator.task.GenerationTask;
import org.telosys.tools.generator.task.GenerationTaskResult;
import org.telosys.tools.generator.task.ITaskMonitor;
import org.telosys.tools.generic.model.Model;


/**
 * Eclipse runnable task with a progress bar 
 * for code generation 
 *  
 * @author Laurent Guerin
 *
 */
public class GenerationTaskWithProgress extends AbstractGenerationTask implements GenerationTask, IRunnableWithProgress 
{
	
	/**
	 * Constructor
	 * @param model
	 * @param selectedEntities
	 * @param bundleName
	 * @param selectedTargets
	 * @param resourcesTargets
	 * @param telosysToolsCfg
	 * @param logger
	 * @throws TelosysToolsException
	 */
	public GenerationTaskWithProgress(
//			RepositoryModel model,
			Model model,
			LinkedList<String> selectedEntities,
			String bundleName,
			LinkedList<TargetDefinition> selectedTargets,
			List<TargetDefinition> resourcesTargets,
			//GeneratorConfig generatorConfig, 
			TelosysToolsCfg telosysToolsCfg,
			TelosysToolsLogger logger)
			throws TelosysToolsException 
	{
		// Just call the super class constructor
		super(model, selectedEntities, 
				bundleName, selectedTargets, resourcesTargets, 
				telosysToolsCfg, logger);
	}
	
	//--------------------------------------------------------------------------------------
	// Methods implementation for super class 'AbstractGenerationTask'
	//--------------------------------------------------------------------------------------
	@Override  // Implementation for AbstractGenerationTask
	protected boolean onError(ErrorReport errorReport) {
		boolean r = GenerationTaskMsgBox.error(errorReport.getErrorType(),
					errorReport.getMessage(), 
					errorReport.getException() );
//		MsgBox.info("Continue ? : " + r );
		return r ; // continue the task or cancel
	}
	
	@Override  // Implementation for AbstractGenerationTask
	protected void afterFileGeneration(Target target, String fullFileName) {
		log("afterFileGeneration(" + fullFileName + ")");
		// Refresh the generated file in the Eclipse Workspace 
		EclipseWksUtil.refresh( new File(fullFileName) );	
	}

	@Override  // Implementation for GenerationTask
	public GenerationTaskResult launch() { 
		
		//-----------------------------------------------------------------------------------
		// BULK GENERATION ENTRY POINT 
		// Creates a 'ProgressMonitor (Eclipse object)' and use it to run this task instance
		//-----------------------------------------------------------------------------------

		//--- 1) De-activate "Build Automatically"
		boolean originalFlag = EclipseWksUtil.setBuildAutomatically(false);
		
		//--- 2) Run the generation task via the progress monitor 
		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog( Util.getActiveWindowShell() ) ;
		try {
			log("Run generation task ..."  );
			//--- RUN THE ECLIPSE TASK ( 'this' task ) ....
			progressMonitorDialog.run(false, false, this); // call the 'run' method
			log("End of generation task."  );
			
			GenerationTaskResult generationTaskResult = super.getResult() ;
			MsgBox.info("End of generation task." 
					+ "\n\n" + generationTaskResult.getNumberOfResourcesCopied() + " resources(s) copied."
					+ "\n\n" + generationTaskResult.getNumberOfFilesGenerated() + " file(s) generated."
					+ "\n\n" + generationTaskResult.getNumberOfGenerationErrors() + " generation error(s).");
			
		} catch (InvocationTargetException invocationTargetException) {
			onError( buildErrorReport(invocationTargetException) ) ;
			
		} catch (InterruptedException e) {
			GenerationTaskResult generationTaskResult = super.getResult() ;
			MsgBox.warning("GENERATION CANCELED !" 
					+ "\n\n"
					+ "\n\n" + generationTaskResult.getNumberOfResourcesCopied() + " resources(s) copied."
					+ "\n\n" + generationTaskResult.getNumberOfFilesGenerated() + " file(s) generated."
					+ "\n\n" + generationTaskResult.getNumberOfGenerationErrors() + " generation error(s).");
		}
		
		//--- 3) Re-activate "Build Automatically" 
		EclipseWksUtil.setBuildAutomatically(originalFlag);
		
    	return super.getResult();
	}

	//--------------------------------------------------------------------------------------
	// Methods implementation for Eclipse interface 'IRunnableWithProgress'
	//--------------------------------------------------------------------------------------
	@Override
	public void run(IProgressMonitor progressMonitor) throws InvocationTargetException,
			InterruptedException {
		log("run");

		//---------------------------------------------------------------------------
		// BULK GENERATION STEPS ( called by the Eclipse 'ProgressMonitorDialog' )
		// It copies the required resources and generates the selected targets 
		// by calling the super class standard methods
		//---------------------------------------------------------------------------

		OverwriteChooser overwriteChooser = new OverwriteChooserDialogBox() ; 
		CopyHandler copyHandler = new CopyHandlerForRefresh() ;
		ITaskMonitor taskMonitor = new TaskMonitor(progressMonitor);
		
		super.runTask(taskMonitor, overwriteChooser, copyHandler);
	}
	
}
