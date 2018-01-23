package org.telosys.tools.eclipse.plugin.editors.velocity;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.telosys.tools.eclipse.plugin.editors.velocity.contentassist.VelocityContentAssistProcessor;
import org.telosys.tools.eclipse.plugin.editors.velocity.scanner.code.VelocityCodeScanner;
import org.telosys.tools.eclipse.plugin.editors.velocity.scanner.partition.VelocityPartitionScanner;

public class VelocityEditorConfiguration extends SourceViewerConfiguration {
//	private XMLDoubleClickStrategy doubleClickStrategy;
	private VelocityDoubleClickStrategy doubleClickStrategy;
	
//	private XMLTagScanner tagScanner;
	private VelocityCodeScanner codeScanner;
	private ColorManager colorManager;
	private ContentAssistant assistant;

	public VelocityEditorConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredContentTypes(org.eclipse.jface.text.source.ISourceViewer)
	 */
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return VelocityPartitionScanner.TYPES;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getDoubleClickStrategy(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
	 */
	@Override
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new VelocityDoubleClickStrategy();
		return doubleClickStrategy;
	}

	private VelocityCodeScanner getVelocityCodeScanner() {
		if (codeScanner == null) {
			codeScanner = new VelocityCodeScanner(colorManager);
			codeScanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(ColorManager.RGB_VELOCITY_DEFAULT))));
		}
		return codeScanner;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getPresentationReconciler(org.eclipse.jface.text.source.ISourceViewer)
	 */
	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

//		DefaultDamagerRepairer dr =
//			new DefaultDamagerRepairer(getXMLTagScanner());
//		reconciler.setDamager(dr, VelocityPartitionScanner.XML_TAG);
//		reconciler.setRepairer(dr, VelocityPartitionScanner.XML_TAG);

		//-----------------------------------------------------------------------------
		// DEFAULT
		//-----------------------------------------------------------------------------
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getVelocityCodeScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		//-----------------------------------------------------------------------------
		// COMMENTS
		//-----------------------------------------------------------------------------
		VelocityDamagerRepairer ndr ;
		
		ndr = new VelocityDamagerRepairer(new TextAttribute(colorManager.getColor(ColorManager.RGB_VELOCITY_COMMENT)));
		reconciler.setDamager(ndr, VelocityPartitionScanner.VELOCITY_MULTILINE_COMMENT);
		reconciler.setRepairer(ndr, VelocityPartitionScanner.VELOCITY_MULTILINE_COMMENT);

		ndr = new VelocityDamagerRepairer(new TextAttribute(colorManager.getColor(ColorManager.RGB_VELOCITY_COMMENT)));
		reconciler.setDamager(ndr, VelocityPartitionScanner.VELOCITY_DOCUMENT_COMMENT);
		reconciler.setRepairer(ndr, VelocityPartitionScanner.VELOCITY_DOCUMENT_COMMENT);

		ndr = new VelocityDamagerRepairer(new TextAttribute(colorManager.getColor(ColorManager.RGB_VELOCITY_COMMENT)));
		reconciler.setDamager(ndr, VelocityPartitionScanner.VELOCITY_SINGLELINE_COMMENT);
		reconciler.setRepairer(ndr, VelocityPartitionScanner.VELOCITY_SINGLELINE_COMMENT);

		return reconciler;
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		if(assistant == null){
			assistant = new ContentAssistant();
			assistant.setContentAssistProcessor(new VelocityContentAssistProcessor(), IDocument.DEFAULT_CONTENT_TYPE);
			assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
			assistant.enableAutoActivation(true);
			assistant.setAutoActivationDelay(0);
		}
		return assistant;
	}
	
	

}