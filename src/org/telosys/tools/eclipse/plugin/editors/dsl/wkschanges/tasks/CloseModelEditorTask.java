package org.telosys.tools.eclipse.plugin.editors.dsl.wkschanges.tasks;

import java.io.File;

import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.editors.dsl.model.ModelEditor;
import org.telosys.tools.eclipse.plugin.editors.dsl.wkschanges.ModelEditorFinder;

public class CloseModelEditorTask implements Runnable {

	private static void log(String msg) {
		PluginLogger.debug("CloseModelEditorTask : " +  msg);
	}

	private final File modelFile ;
	
	public CloseModelEditorTask(File modelFile) {
		super();
		this.modelFile = modelFile;
		log("TASK created");
	}

	@Override
	public void run() {
		log("TASK run()...");
		// NB : Must be called in the task "run"
		ModelEditor modelEditor = ModelEditorFinder.findModelEditorForModelFile(modelFile);
    	if ( modelEditor != null ) {
    		log(" ModelEditor instance found for file : " + modelFile.getName()  );
    		log(" Calling the ModelEditor close() method ..." );
    		//modelEditor.close(false);
    		modelEditor.closeEditor(false);
    	}
    	else {
    		log(" No ModelEditor for this file : " + modelFile.getName() );
    	}
	}

}
