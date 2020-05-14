package org.telosys.tools.eclipse.plugin.editors.dsl.wkschanges;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.swt.widgets.Display;
import org.telosys.tools.dsl.DslModelUtil;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.editors.dsl.wkschanges.tasks.CloseModelEditorTask;

/**
 * Processor for model changes 
 * 
 * @author L. Guerin
 *
 */
public class ModelChangesProcessor {

	private static void log(String msg) {
		if ( _PackageLoggerConfig.LOG ) {
			PluginLogger.log(ModelChangesProcessor.class, msg);
		}
	}
	
	//-----------------------------------------------------------------------------
	// Process model change event 
	//-----------------------------------------------------------------------------
    public static void processModelChange(IResourceDelta delta) { 
    	
    	log("processModelChange(delta)");

        switch (delta.getKind()) {
        
           case IResourceDelta.ADDED:
        	  modelFileAdded(delta);
              break;
              
           case IResourceDelta.REMOVED:
              modelFileRemoved(delta);              
              break;

           case IResourceDelta.CHANGED:
        	   modelFileChanged(delta);
               break;
        }
	}
    
    /**
     * A new model has been created : <br>
     * - noting to do <br>
     * @param delta
     */
    private static void modelFileAdded(IResourceDelta delta) {
        log("MODEL ADDED (nothing to do) : " + delta.getResource().getFullPath() );
    }

    /**
     * The model file has changed <br>
     * - nothing to do  <br>
     * @param delta
     */
    private static void modelFileChanged(IResourceDelta delta) {
        log("MODEL CHANGED (nothing to do) : " + delta.getResource().getFullPath() );
    }
    
    /**
     * The model file has been removed <br>
     * - delete all entities belonging to the model in the workspace <br>
     * - refresh the workspace (after entities deletion) <br>
     * - close the model editor if open <br>
     * @param delta
     */
    private static void modelFileRemoved(IResourceDelta delta) {
    	IResource res = delta.getResource();
        log("MODEL REMOVED : " + res.getFullPath() );              
        /*
         * ISSUE : 
         * Cannot refresh folder. The resource tree is locked for modifications.
         * 
         * RESPONSE :
         * You can't modify the resource tree from a resource delta event handler 
         * (imagine the potential for complete chaos if you could). 
         * The most common approach that I know of is to schedule a Job and make the 
         * modifications within the run() method of the Job. This means you need to 
         * remember the modifications that you want to make so that they can be done within the Job. 
         * It also means you can't make too many assumptions about the state of the resource 
         * tree because theoretically some other Job might run before yours that makes changes to the tree
         * 
         */
        
    	File modelFile = EclipseWksUtil.toFile( (IFile) delta.getResource() );    	
		log("modelFile.getAbsolutePath() = " + modelFile.getAbsolutePath() );

		// Delete all the model files and model folder (if any)
		DslModelUtil.deleteModel(modelFile);
        log("Model file and folder deleted.");

        // Differed WORKSPACE REFRESH with a JOB ( based on a scheduled "Job" )        
        RefreshWorkspaceFolderJob refreshWorkspaceJob = new RefreshWorkspaceFolderJob(modelFile.getParentFile());
        refreshWorkspaceJob.schedule();
        log("refreshWorkspaceJob.schedule() done.");

        // Close the Model Editor (not closed automatically by Eclipse because it's not a TextEditor ?)
        closeModelEditor(modelFile);
    }
    
    private static void closeModelEditor(File modelFile) {
        log("closeModelEditor()");
    	Display.getDefault().asyncExec( new CloseModelEditorTask(modelFile) );
    }

}
