package org.telosys.tools.eclipse.plugin.wkschanges;

import java.io.File;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.telosys.tools.dsl.DslModelUtil;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.editors.dsl.model.ModelEditor;

/**
 * Processor for entity changes ( when ".entity" files has changed ) <br>
 * 
 * @author L. Guerin
 *
 */
public class ModelEditorFinder {

	private static void log(String msg) {
		if ( _PackageLoggerConfig.LOG ) {
			PluginLogger.log(ModelEditorFinder.class, msg);
		}
	}
	
    /**
     * Try to find a ModelEditor currently open for the Model file the given entity belongs to
     * @param entityFile
     * @return the editor found or null if none
     */
    public static ModelEditor findModelEditorForEntityFile(File entityFile) {
    	if ( entityFile != null ) {
        	File modelFile = DslModelUtil.getModelFileForEntityFile(entityFile);
        	return findModelEditorForModelFile(modelFile);
    	}
    	return null ;
    }
    
    /**
     * Try to find a ModelEditor currently open for the given Model file
     * @param modelFile
     * @return the editor found or null if none
     */
    public static ModelEditor findModelEditorForModelFile(File modelFile) {
    	if ( modelFile != null ) {
    		//IEditorReference[] editors = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
    		IEditorReference[] editors = getEditors() ;
    		if ( editors != null ) {
    			log("Editors ( count = " + editors.length + " )");
        		for ( IEditorReference editor : editors ) {
        			log(" editor.getId()   = " + editor.getId()   );
        			log(" editor.getName() = " + editor.getName() );
        			if ( isModelEditor(editor)) {
        				log(" this editor is a 'ModelEditor' ");
        				ModelEditor modelEditor = getModelEditor(editor);
        				if ( modelEditor != null ) {
            				log(" ModelEditor instance found : file name = " + modelEditor.getShortFileName() );
            				// modelEditor.getShortFileName() : eg "foo.model"
            				// Check if the file in the editor is the given model file
            				File modelEditorFile = modelEditor.getModelFile() ;
//            				IEditorInput editorInput = modelEditor.getEditorInput(); // eg "foo.model"     
//            				if ( editorInput instanceof FileEditorInput ) {
//            					log("instance of FileEditorInput");
//            					FileEditorInput fileEditorInput = (FileEditorInput) editorInput ;
//            					IPath iPath =fileEditorInput.getPath();
//            					log("IPath toFile       = " + iPath.toFile() );
//            					log("IPath toOSString() = " + iPath.toOSString());
//            				}
//            				log(" ModelEditor input = " + editorInput.getName() );
//            				//editorInput.getName();
            				log(" ModelEditor instance found : model file = " + modelEditorFile.getName() );
            				if ( modelFile.getAbsolutePath().equals(modelEditorFile.getAbsolutePath() ) ) {
                				log(" Same file => return ModelEditor ..." );
                				return modelEditor ;
            				}
        				}
        			}
        		}
    		}
    	}
		return null ; // No ModelEditor open for this file 
    }
        
    private static IEditorReference[] getEditors() {
		log("getEditors()...");
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow() ;
		if ( workbenchWindow != null ) {
			IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
			if ( workbenchPage != null ) {
				return workbenchPage.getEditorReferences();
			}
		}
		else {
			// Returns null if not in the same thread => use it in "new Runnable() { ... } "
			log("getEditors() : IWorkbenchWindow is null => no editor");
		}
		return null ;
    }

    private static boolean isModelEditor(IEditorReference editor) {
		if ( editor.getId().endsWith(".ModelEditor") ) {
			if ( editor.getName().endsWith(".model") ) {
    			return true ;
			}
		}
		return false ;
    }
    
    private static ModelEditor getModelEditor(IEditorReference editor) {
    	IEditorPart editorPart = editor.getEditor(true); 
    	if ( editorPart != null ) {
    		//Class<?> clazz = editorPart.getClass() ;
    		if ( editorPart instanceof ModelEditor ) {
    			return (ModelEditor) editorPart ;
    		}
    		else {
    			log("ERROR : editor is not an instance of ModelEditor !!!");
    		}
    	}
    	else {
    		log("ERROR : editorPart is null !!!");
    	}
    	return null ;
    }    
    
}
