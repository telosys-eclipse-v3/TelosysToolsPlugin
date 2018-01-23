package org.telosys.tools.eclipse.plugin.editors.velocity;

import java.util.ResourceBundle;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.telosys.tools.eclipse.plugin.ContextualHelp;
import org.telosys.tools.eclipse.plugin.editors.velocity.contentassist.ContentAssistConfiguration;

public class VelocityEditor extends TextEditor {

	private ColorManager colorManager;

	public VelocityEditor() {
		super();
		
		colorManager = new ColorManager();
		
		//setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		setSourceViewerConfiguration(new VelocityEditorConfiguration(colorManager));
		
		setDocumentProvider(new VelocityDocumentProvider());
		
		//--- Set contextual help to be called when F1 is pressed in the editor
		//setHelpContextId(getHelpContextId(VELOCITY_EDITOR_HELP_CONTEXT_ID));
		setHelpContextId( ContextualHelp.getVelocityEditorHelpContextId() ) ;

	}
	
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}
	
	@Override
	protected void createActions() {
		super.createActions();
		ResourceBundle bundle = ContentAssistConfiguration.getDocumentationResourceBundle();
		
		// Content assist action
		IAction action = new TextOperationAction(bundle, "ContentAssistProposal.", this, ISourceViewer.CONTENTASSIST_PROPOSALS); //$NON-NLS-1$
        action.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
        // setAction("ContentAssistProposal", action); //$NON-NLS-1$
        setAction("ContentAssist", action);
        
        
	}

}
