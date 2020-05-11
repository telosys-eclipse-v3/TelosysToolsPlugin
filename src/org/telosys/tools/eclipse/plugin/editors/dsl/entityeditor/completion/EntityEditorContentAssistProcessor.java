package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.completion;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

/**
 * IContentAssistProcessor implementation 
 * 
 * A content assist processor proposes completions and computes context information for a particular content type. 
 * A content assist processor is a IContentAssistant plug-in.
 * 
 * Completion : array of "ICompletionProposal"
 * Context information : array of "IContextInformation"
 *
 */
public class EntityEditorContentAssistProcessor implements IContentAssistProcessor {

    private final ProposalsManager proposalsManager;
    private String errorMessage;

    /**
     * Constructor
     */
    public EntityEditorContentAssistProcessor() {
    	this.proposalsManager = new ProposalsManager();
    }

    //-------------------------------------------------------------------------------------------------------
    // COMPLETION
    //-------------------------------------------------------------------------------------------------------
    
    /*
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#computeCompletionProposals
     *      (org.eclipse.jface.text.ITextViewer, int)
     * 
     * Returns a list of completion proposals based on the specified location within the document 
     * that corresponds to the current cursor position within the text viewer.
     * 
     * Parameters:
     * viewer - the viewer whose document is used to compute the proposals
     * offset - an offset within the document for which completions should be computed
     * 
     * Returns:
     * an array of completion proposals or null if no proposals are possible
     *      
     */
    @Override
    public ICompletionProposal[] computeCompletionProposals(ITextViewer textViewer, int documentOffset) {
    	return proposalsManager.getCompletionProposals(textViewer, documentOffset);
    }
    
    /*
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getCompletionProposalAutoActivationCharacters()
     * 
     * Returns the characters which when entered by the user should automatically trigger the presentation of possible completions.
     * 
     * Returns:
     * the auto activation characters for completion proposal or null if no auto activation is desired
     * 
     */
    @Override
    public char[] getCompletionProposalAutoActivationCharacters() {
    	// example :
    	// return new char[]{'$'} // make automatic proposals after '$' character
        return null; // no auto-activation
    }

    //-------------------------------------------------------------------------------------------------------
    // CONTEXT INFORMATION
    //-------------------------------------------------------------------------------------------------------
    /*
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#computeContextInformation
     *                                                                   (org.eclipse.jface.text.ITextViewer, int)
     * 
     * Returns information about possible contexts based on the specified location within the document 
     * that corresponds to the current cursor position within the text viewer.
     * 
     * Parameters:
     * viewer - the viewer whose document is used to compute the possible contexts
     * offset - an offset within the document for which context information should be computed
     * 
     * Returns:
     * an array of context information objects or null if no context could be found
     * 
     */
    @Override
    public IContextInformation[] computeContextInformation(ITextViewer itextviewer, int i) {
        errorMessage = "No Context Information available";
        return null;
    }

    /*
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getContextInformationAutoActivationCharacters()
     * 
     * Returns the characters which when entered by the user should automatically trigger the presentation of context information.
     * 
     * Returns:
     * the auto activation characters for presenting context information or null if no auto activation is desired
     * 
     */
    @Override
    public char[] getContextInformationAutoActivationCharacters() {
        return null;
    }

    /*
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getContextInformationValidator()
     * 
     * Returns the characters which when entered by the user should automatically trigger the presentation of context information.
     * 
     * Returns:
     * the auto activation characters for presenting context information or null if no auto activation is desired
     * 
     */
    @Override
    public IContextInformationValidator getContextInformationValidator() {
        return null;
    }

    //-------------------------------------------------------------------------------------------------------
    // METHODS FOR BOTH COMPLETION AND CONTEXT INFORMATION
    //-------------------------------------------------------------------------------------------------------
    /*
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getErrorMessage()
     * 
     * Returns the reason why this content assist processor was unable to produce any completion proposals or context information.
     * 
     * Returns:
     * an error message or null if no error occurred
     *
     */
    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

}