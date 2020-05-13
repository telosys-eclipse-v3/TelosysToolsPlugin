package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

/**
 * Main class for the Entity Editor.
 * 
 */
//@SuppressWarnings("deprecation")
public class EntityEditor extends TextEditor {

	private IFile  _file = null ;

    public EntityEditor() {
        super();
        // Set this editor's source viewer configuration used to configure its internal source viewer.
        // This method must be called before the editor's control is created.  
        // AbstractTextEditor.setSourceViewerConfiguration
        setSourceViewerConfiguration(new EntityEditorConfiguration()); 
    }
    
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);

		PluginLogger.log(this, "--- ENTITY EDITOR INIT " );
		PluginLogger.log(this, "init(..,..) : site id = '" + site.getId() + "'" );
		PluginLogger.log(this, "init(..,..) : input name = '" + input.getName() + "'" );
		// setPartName(input.getName()); // An editor can update its title using EditorPart.setPartName()
		
		if ( input instanceof IFileEditorInput ) {
			IFileEditorInput fileInput = (IFileEditorInput) input;
			_file = fileInput.getFile();
			PluginLogger.log(this, "init(..,..) : file name = '" + _file.getName() + "'" );
		}
		else {
			// never happen 
			MsgBox.error("The editor input '" + input.getName() + "' is not a File ! ");
		}
	}
	
	public IFile getFile() {
		return _file ;
	}
	

    @Override
    protected void createActions() {
        super.createActions();
        
        // Content Assist ( Ctrl-Space ) works even without "setAction"
//		ResourceBundle resourceBundle = getResourceBundle() ;
//        
//		ContentAssistAction action = new ContentAssistAction(resourceBundle,
//                "ContentAssistProposal.", this);
//        action.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
//        setAction("ContentAssist", action);
    }
    
//    /**
//     * Returns the resource bundle managed by the receiver.
//     * 
//     * @return the resource bundle
//     */
//    private ResourceBundle getResourceBundle() {
//    	ResourceBundle resourceBundle ;
//    	String properties = 
//    		  "ContentAssistProposal.label=Content assist"  + "\n" 
//    		+ "ContentAssistProposal.tooltip=Content assist" + "\n" 
//    		+ "ContentAssistProposal.description=Provides Content Assistance" ;
//    	
//    	InputStream is = new ByteArrayInputStream(properties.getBytes());
//    	try {
//			resourceBundle = new PropertyResourceBundle(is);
//		} catch (IOException e) {
//			throw new RuntimeException("Cannot init ResourceBundle : IOException / new PropertyResourceBundle(is)");
//		}
//		return resourceBundle ;
//    }

}
