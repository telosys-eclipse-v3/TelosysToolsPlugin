package org.telosys.tools.eclipse.plugin.editors.velocity.contentassist;

import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension3;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

public class HtmlCompletionProposal implements ICompletionProposal,
		ICompletionProposalExtension3 {

	private String replacementString;
	private int replacementOffset;
	private int replacementLength;
	private int cursorPosition;
	private Image image;
	private String displayString;
	private IContextInformation contextInformation;
	private String additionalProposalInfo;
	
	private final static String FONT_SIZE = "2.5";

	public HtmlCompletionProposal(String replacementString,
			int replacementOffset, int replacementLength, int cursorPosition,
			Image image, String displayString,
			IContextInformation contextInformation,
			String additionalProposalInfo) {
		super();
		this.replacementString = replacementString;
		this.replacementOffset = replacementOffset;
		this.replacementLength = replacementLength;
		this.cursorPosition = cursorPosition;
		this.image = image;
		this.displayString = displayString;
		this.contextInformation = contextInformation;
		this.additionalProposalInfo = this.formatDocumentationHTML(additionalProposalInfo);
	}

	@Override
	public IInformationControlCreator getInformationControlCreator() {

		return new IInformationControlCreator() {
			@Override
			public IInformationControl createInformationControl(Shell parent) {

				// DefaultInformationControl infoControl = new
				// DefaultInformationControl(parent, new
				// HTMLTextPresenter(true));
				// TODO Revoir la génération du conteneur HTML.
				BrowserInformationControl browser = new BrowserInformationControl(
						parent, null, false);

				return browser;
			}
		};
	}

	@Override
	public int getPrefixCompletionStart(IDocument arg0, int arg1) {
		return 0;
	}

	@Override
	public CharSequence getPrefixCompletionText(IDocument arg0, int arg1) {
		return null;
	}

	@Override
	public void apply(IDocument document) {
		try {
			document.replace(this.replacementOffset, this.replacementLength,
					this.replacementString);
		} catch (BadLocationException x) {
			// ignore
		}
	}

	@Override
	public String getAdditionalProposalInfo() {
		return this.additionalProposalInfo;
	}

	@Override
	public IContextInformation getContextInformation() {
		return this.contextInformation;
	}

	@Override
	public String getDisplayString() {
		if (this.displayString != null) {
			return this.displayString;
		} else {
			return this.replacementString;
		}
	}

	@Override
	public Image getImage() {
		return this.image;
	}

	@Override
	public Point getSelection(IDocument arg0) {
		return new Point(this.replacementOffset + this.cursorPosition, 0);
	}
	
	/**
	 * Apply some presentation like font size.
	 * @param doc
	 * @return formatted doc
	 */
	private String formatDocumentationHTML(String doc) {
		String formattedDoc = "";
		
		if (doc != null) {
			// Font size
			formattedDoc = "<font size=\""+ FONT_SIZE + "\">" + doc + "</font>";
		}
		
		return formattedDoc;
	}

}
