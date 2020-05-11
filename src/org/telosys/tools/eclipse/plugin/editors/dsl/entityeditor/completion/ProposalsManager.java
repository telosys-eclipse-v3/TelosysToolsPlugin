package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.completion;

import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

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
public class ProposalsManager {

	private void debug(String msg) { 
		if ( _PackageLoggerConfig.DEBUG ) {
			PluginLogger.debug(msg);
		}
	}
	
    public ICompletionProposal[] getCompletionProposals(ITextViewer textViewer, int documentOffset) {
    	IDocument document = textViewer.getDocument();
    	debug("getCompletionProposals()...");
    	// Step #1 : determine proposal context
    	ProposalContext ctx = getProposalContext(document, documentOffset);
    	debug("getCompletionProposals() : ProposalContext : " + ctx);
    	
    	// Step #2 : get suggestions for the current context
    	List<String> suggestions = null;
    	switch ( ctx.getProposalType() ) {
    	case ENTITY_ANNOTATION : 
    		// For future use
    		return null;
    	case ATTRIBUTE_ANNOTATION : // @Id, @DefaultValue(xx), etc
    		suggestions = SuggestionsForAttributeAnnotation.getSuggestions(ctx.getWord());
    		break;
    	case ATTRIBUTE_TYPE : // int, string, Employee, etc
    		suggestions = SuggestionsForAttributeType.getSuggestions(ctx.getWord());
    		break;
    	default :
    		return null;
    	}

    	// Step #3 : build and return Completion Proposals if any
    	if ( suggestions != null && suggestions.size() > 0 ) {
    		String originalWord = ctx.getWord();
    		int originalWordOffset = documentOffset - originalWord.length();
			return buildProposals(suggestions, originalWord, originalWordOffset);
    	}
    	return null ;
    }
    
    //----------------------------------------------------------------------------------------
    // STEP #1 : DETERMINE PROPOSAL CONTEXT
    //----------------------------------------------------------------------------------------
    /**
     * @param document
     * @param documentOffset
     * @return
     */
    private ProposalContext getProposalContext(IDocument document, int documentOffset) {
		char c ;
		boolean wordCompleted = false ;
		boolean colonFound = false ;
		boolean openBraceFound = false ;
		StringBuffer sb = new StringBuffer() ;
    	int pos = documentOffset ; 
    	// rewind (from current position to beginning)
    	while ( ( c = getChar(document, --pos) ) > 0 ) {
    		if ( isWordChar(c) ) {
    			if ( ! wordCompleted ) {
        			sb.append(c);
    			}
    		} else {
    			wordCompleted = true ;
    		}
    		if ( c == '{' ) {
    			openBraceFound = true ;
    		}
    		if ( c == ':' ) {
    			colonFound = true ;
    			break; // end of search 
    		}
    		if ( c == '}' || c == ';' ) {
    			break; // end of search 
    		}
    	}
    	String word = sb.reverse().toString();
    	return new ProposalContext(word, colonFound, openBraceFound);
	}
	
    private char getChar(IDocument document, int pos) {
    	char c = 0 ;
    	if ( pos >= 0 ) {
            try {
				c = document.getChar(pos);
			} catch (BadLocationException e) {
				c = 0;
			}
    	}
    	return c;
    }
    
	private boolean isWordChar(char c) {
		if ( c >= 'A' && c <= 'Z') {
			return true ;
		}
		if ( c >= 'a' && c <= 'z') {
			return true ;
		}
		if ( c >= '0' && c <= '9') {
			return true ;
		}
		if ( c == '@' ) {
			return true ;
		}
		return false ;
	}
	
    //----------------------------------------------------------------------------------------
    // STEP #3 : BUILD PROPOSALS
    //----------------------------------------------------------------------------------------
    /**
     * @param suggestions
     * @param replacedWord
     * @param offset
     * @return
     */
    private ICompletionProposal[] buildProposals(List<String> suggestions, String replacedWord, int offset) {
    	
        ICompletionProposal[] proposals = new ICompletionProposal[suggestions.size()];
        int index = 0;
        for ( String suggestion : suggestions ) {
        	// build the proposal instance for the current suggestion 
        	String replacementString = suggestion ;
        	int replacementOffset = offset ;
        	int replacementLength = replacedWord.length();
        	int cursorPosition = suggestion.length();
//        	if ( replacementString.endsWith(")") ) {
//        		cursorPosition--;
//        	}
        	if ( replacementString.endsWith(") ") ) {
        		cursorPosition = cursorPosition-2;
        	}
            CompletionProposal cp = new CompletionProposal(
            		replacementString, // the actual string to be inserted into the document
            		replacementOffset, // the offset of the text to be replaced
            		replacementLength, // the length of the text to be replaced
                    cursorPosition     // the position of the cursor following the insert relative to replacementOffset
                    ); 
            proposals[index++] = cp;
        }
        return proposals;
    }

}