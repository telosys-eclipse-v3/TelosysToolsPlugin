package org.telosys.tools.eclipse.plugin.editors.velocity.contentassist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.telosys.tools.eclipse.plugin.commons.PluginImages;
import org.telosys.tools.eclipse.plugin.editors.velocity.model.VelocityKeyWord;

public class VelocityContentAssistProcessor implements IContentAssistProcessor {

	private static final IContextInformation[] NO_CONTEXTS = {};
	private static final ICompletionProposal[] NO_COMPLETIONS = {};

	private VelocityKeyWordProvider wordProvider;
	private String lastError;

	public VelocityContentAssistProcessor() {
		this.wordProvider = new VelocityKeyWordProvider(new TelosysGeneratorObjectInfo());
	}

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer textViewer, int documentOffset) {
		ICompletionProposal[] proposals = NO_COMPLETIONS;
//		try {
			String context = this.extractPrefix(textViewer, documentOffset);
			List<VelocityKeyWord> suggestions = new ArrayList<>();
			
			if (!context.isEmpty() && context.contains(".")) {
				if (context.charAt(context.length() - 1) == '.' && context.charAt(0) != '$') {
					// It's a point but no for a generator variable
					proposals = NO_COMPLETIONS;
				} else {
					if (context.charAt(0) == '$' && context.contains(".")) {
						String variableName = this.extractBeanName(context);
						if (context.charAt(context.length() - 1) == '.') {
							// Point for display the bean's method
							suggestions = this.wordProvider.suggestContextVariableOutline(variableName, null);
						} else {
							// The begin of the method name is already write
							String filter = this.extractMethodNameFilter(context);
							suggestions = this.wordProvider.suggestContextVariableOutline(variableName, filter);
						}
					}
				}
			} else {
				suggestions = this.wordProvider.suggest(context);
			}

			proposals = buildResult(suggestions, context,
					documentOffset - context.length());
//		} catch (Exception e) {
//		} catch (BadLocationException e) {
//			e.printStackTrace();
//			this.lastError = e.getMessage();
//		}
		return proposals;
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer arg0,
			int arg1) {
		lastError = "No Context Information available";
		
		return NO_CONTEXTS;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		// return new char[] { '.', '"', '#', '$' };
		return new char[] { '.'};
	}

	@Override
	public String getErrorMessage() {
		return this.lastError;
	}

	private ICompletionProposal[] buildResult(
			List<VelocityKeyWord> suggestions, String replacedWord, int offset)
//			throws Exception {
		{
			
		ICompletionProposal[] proposals = new ICompletionProposal[suggestions.size()];

		int index = 0;
		for (VelocityKeyWord suggestion : suggestions) {
						
			// In a completion for methods, we must keep the bean name
			StringBuilder replacementText = new StringBuilder();
			if (replacedWord.contains(".")) {
				// Ignore the beginning input of the method
				String preserveInput = replacedWord.substring(0,replacedWord.indexOf(".") + 1);
				// method completion of a bean
				replacementText.append(preserveInput).append(suggestion.getDisplayValue());
			} else {
				// Preserve the character "{"	
				if (replacedWord.startsWith("${")) {
					replacementText.append(suggestion.getDisplayValue().charAt(0));
					replacementText.append("{");
					replacementText.append(suggestion.getDisplayValue().substring(1, suggestion.getDisplayValue().length()));
				} else {
					replacementText.append(suggestion.getDisplayValue());
				}
			}
			
			// Compute cursor position, between the brackets if needed
			int cursorPosition = replacementText.length();
			if (suggestion.getDisplayValue().endsWith(")")) {
				cursorPosition = replacementText.length() - 1;
			}
			
			// Create the completion entry
			ICompletionProposal cp = new HtmlCompletionProposal(
					replacementText.toString(), offset,
					replacedWord.length(), cursorPosition ,
					PluginImages.getImage(suggestion.getCompletionIconName()) , suggestion.getDisplayValue(),
					null, suggestion.getAdditionnalDocumentation());
			proposals[index] = cp;
			index++;
		}
		return proposals;
	}

	private String extractPrefix(ITextViewer textViewer, int documentOffset) {
//			throws BadLocationException {
		IDocument document = textViewer.getDocument();
		int currOffset = documentOffset - 1;

		String currWord = "";
		char currChar;
		try {
			// go back to left
			while ( currOffset >= 0
					&& !( Character.isWhitespace(currChar = document.getChar(currOffset)) || currChar == ';')
					) {
				currWord = currChar + currWord;
				currOffset--;
			}
		} catch (BadLocationException e) {
			// document.getChar(x) error : nothing to do
		}
		
		// identify the situation if the cursor is just after a bracket
		if (currWord.endsWith("(")) {
			currWord = "";
		} else {
			// isolating the bean or directive input
			// for example "normalform${myBean}input or #set($myBean --> The content assistant is for $myBean
			int startDirectiveIndex = currWord.lastIndexOf("#");
			int startBeanIndex = currWord.lastIndexOf("$");
			int startCurrentWord;
			if (startBeanIndex != -1 || startDirectiveIndex != -1) {
				if (startBeanIndex > startDirectiveIndex) {
					// context of a bean input
					startCurrentWord = startBeanIndex;
				} else {
					// context of a directive input
					startCurrentWord = startDirectiveIndex;
				}
				currWord = currWord.substring(startCurrentWord,
						currWord.length());
			}
		}
		return currWord;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		// Auto-generated method stub
		return null;
	}
	
	/**
	 * Extract the bean name from a context of the completion
	 * ex : $beanName.method --> beanName.
	 * @param context
	 * @return The bean name
	 */
	private String extractBeanName(String context) {
		String formattedName;
		
		// $ and dot delete
		int pointPosition = context.indexOf(".");
		formattedName = context.substring(1, pointPosition);
		
		// and optionnaly {
		if (formattedName.startsWith("{")) {
			formattedName = formattedName.substring(1, formattedName.length());
		}
		
		return formattedName;
	}

	/**
	 * Extract the beginning of a method input in the editor
	 * Ex $beanName.myMeth -> myMeth.
	 * @param context
	 * @return The beginning of the method input
	 */
	private String extractMethodNameFilter(String context) {
		// The beginning of the method name is after the point
		int pointPosition = context.indexOf(".");
		
		return context.substring(pointPosition + 1, context.length());
	}
	
}
