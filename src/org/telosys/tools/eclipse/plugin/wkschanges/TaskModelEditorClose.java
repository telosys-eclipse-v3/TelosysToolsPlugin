package org.telosys.tools.eclipse.plugin.wkschanges;

import java.io.File;

import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.editors.dsl.model.ModelEditor;

public class TaskModelEditorClose implements Runnable {

	private static void log(String msg) {
		if ( _PackageLoggerConfig.LOG ) {
			PluginLogger.log(TaskModelEditorClose.class, msg);
		}
	}

	private final File modelFile ;
	
	public TaskModelEditorClose(File modelFile) {
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
