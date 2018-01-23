package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor;

import org.eclipse.ui.editors.text.TextEditor;

/**
 * Main class for the Entity Editor.
 * 
 */
//@SuppressWarnings("deprecation")
public class EntityEditor extends TextEditor {

    public EntityEditor() {
        super();
        // Set this editor's source viewer configuration used to configure its internal source viewer.
        // This method must be called before the editor's control is created.  
        // AbstractTextEditor.setSourceViewerConfiguration
        setSourceViewerConfiguration(new EntityEditorConfiguration()); 
    }

//    @Override
//    protected void createActions() throws EditorsException {
//        super.createActions();
//        ResourceBundle resourceBundle = null;
//        try {
//            resourceBundle = new PropertyResourceBundle(
//                    new StringBufferInputStream(
//                            "ContentAssistProposal.label=Content assist\nContentAssistProposal.tooltip=Content assist\nContentAssistProposal.description=Provides Content Assistance"));
//        } catch (IOException e) {
//            throw new EditorsException(
//                    "Error while creating the autocompletion : " + e);
//        }
//        ContentAssistAction action = new ContentAssistAction(resourceBundle,
//                "ContentAssistProposal.", this);
//        action.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
//        setAction("ContentAssist", action);
//    }

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
