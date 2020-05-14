package org.telosys.tools.eclipse.plugin.editors.dsl.wkschanges.tasks;

import java.io.File;

import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.editors.dsl.commons.ModelLoadingResult;
import org.telosys.tools.eclipse.plugin.editors.dsl.commons.ModelManager;
import org.telosys.tools.eclipse.plugin.editors.dsl.model.ModelEditor;
import org.telosys.tools.eclipse.plugin.editors.dsl.wkschanges.ModelEditorFinder;

/**
 * Asynchronous task to reload a DSL model and to update the model editor if active
 * 
 * @author Laurent Guerin
 *
 */
public class ReloadModelTask implements Runnable {

	private static void log(String msg) {
		PluginLogger.debug("ReloadModelTask : " + msg);
	}

	private final File modelFile ;
	
	public ReloadModelTask(File modelFile) {
		super();
		this.modelFile = modelFile;
		log("TASK created");
	}

	@Override
	public void run() {
		log("TASK run()...");
		// NB : Must be called in the task "run"
    	if ( modelFile.exists() ) {
    		log(" The model file exists : " + modelFile.getName() );
    		
    		// Reload the model and update markers
    		ModelLoadingResult r = ModelManager.load(modelFile);
    		
    		// If the model editor is currently open refresh it  
    		ModelEditor modelEditor = ModelEditorFinder.findModelEditorForModelFile(modelFile);
        	if ( modelEditor != null ) {
        		log(" ModelEditor instance found for file : " + modelFile.getName()  );
        		log(" Calling the ModelEditor refresh() method ..." );
        		// Update Model Editor with the result
        		modelEditor.updateEditor(r);
        	}
        	else {
        		log(" No ModelEditor for this file : " + modelFile.getName() );
        	}
    	}
    	else {
    		log(" The model file doesn't exist (deleted) : " + modelFile.getName() );
    	}
	}

}
