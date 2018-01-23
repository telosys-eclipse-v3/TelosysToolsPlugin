package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.completion.EntityEditorContentAssistProcessor;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.scanner.EntityEditorScanner;

/**
 * Entity text editor configuration <br>
 * Methods called in the following order : <br>
 * 1) getPresentationReconciler <br>
 * 2) getContentAssistant <br>
 * 
 * @author Laurent Guerin
 *
 */
public class EntityEditorConfiguration extends TextSourceViewerConfiguration {

	private static void log(String msg) { 
		if ( _PackageLoggerConfig.LOG ) {
			PluginLogger.log(EntityEditorConfiguration.class, msg);
		}
	}

    private ITokenScanner scanner = null;

    @Override
    // 1rst call
    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
    	log("getPresentationReconciler()...");
    	
        PresentationReconciler reconciler = new PresentationReconciler();
        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getScanner());

        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
        return reconciler;
    }

    private ContentAssistant assistant = null;

    @Override
    // 2nd call
    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
    	log("getContentAssistant()...");
        if (assistant == null) {
            assistant = new ContentAssistant();
            assistant.setContentAssistProcessor(
                    new EntityEditorContentAssistProcessor(),
                    IDocument.DEFAULT_CONTENT_TYPE);
            assistant
                    .setInformationControlCreator(getInformationControlCreator(sourceViewer));
        }
        return assistant;
    }

    private ITokenScanner getScanner() {
    	log("getScanner()...");
        if (scanner == null) {
            EntityEditorColorManager manager = new EntityEditorColorManager();
            scanner = new EntityEditorScanner(manager);
        }
        return scanner;
    }
}