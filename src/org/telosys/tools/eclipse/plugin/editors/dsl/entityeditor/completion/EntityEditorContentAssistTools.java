package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.completion;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

public class EntityEditorContentAssistTools {
	
	public static String getDocumentSubPart(IDocument document, int documentOffset) {
		StringBuffer sb = new StringBuffer() ;
		char lastChar = 0 ;
        int offset = documentOffset > 0 ? documentOffset - 1 : documentOffset;
        // build the string from right to left (reversed string)
        try {
            char c = document.getChar(offset);
            while ( offset > 0 ) {
            	sb.append(c);
            	lastChar = c ;
            	if ( c == ':') {
            		break; // this is the end
            	}
                offset--;
                c = document.getChar(offset);
            }
        } catch (BadLocationException e1) {
            throw new RuntimeException("Error in document parsing for suggest (BadLocationException)");
        }
        if ( lastChar == ':') {
            return sb.reverse().toString();
        }
        else {
        	return null ;
        }
	}	
	
	private final static char[] VOID_CHARACTERS    = { ' ', '\t', '\n', '\r' };
	private final static char[] SPECIAL_CHARACTERS = { ':', ';', ',', '{', '}', '[', ']' };
	
	public static EntityEditorSuggestContext getSuggestContext(String s) {
		int bracesFound = 0 ;
//		int commasFound = 0 ;
		char lastChar = 0 ;

		boolean endOfWord = false ;
		char firstLeftSeparator = 0 ;
		char firstLeftChar = 0 ;
		StringBuffer sbReversedWord = new StringBuffer();
		// read from end to beginning (reversed order of characters)
		for ( int i = s.length() -1 ; i >= 0 ; i-- ) {
			char c = s.charAt(i);
			lastChar = c ;
			if ( isVoidChar(c) ) {
				endOfWord = true ;
			}
			else {
				if ( isSpecialChar(c) ) { // separator and not void
					endOfWord = true ;
					if ( firstLeftSeparator == 0 ) { // not yet set 
						firstLeftSeparator = c ; // set only once
					}
				}
				if ( endOfWord && firstLeftChar == 0 ) { // not yet set 
					firstLeftChar = c ; // set only once
				}
			}
			if ( ! endOfWord ) {
				sbReversedWord.append(c);
			}
			else {
				if ( c == ':' ) {
					break; // this is the end
				}
//				else if ( c == ',' ) {
//					commasFound++ ;
//				}
				else if ( c == '{' ) {
					bracesFound++ ;
				}
			}
		}
		String word = sbReversedWord.reverse().toString();
		if (lastChar != ':') {
			// no suggestion if not after ':'
			return new EntityEditorSuggestContext(word, EntityEditorContext.DEFAULT ) ;
		}
		else if ( firstLeftChar == '{' ) {
			return new EntityEditorSuggestContext(word, EntityEditorContext.ANNOTATION ) ;
		}
		else if ( firstLeftChar == ',' && bracesFound > 0 ) {
			return new EntityEditorSuggestContext(word, EntityEditorContext.ANNOTATION ) ;
		}
		else if ( firstLeftChar == ':' ) {
			return new EntityEditorSuggestContext(word, EntityEditorContext.TYPE ) ;
		}
		else {
			return new EntityEditorSuggestContext(word, EntityEditorContext.DEFAULT ) ;
		}
	}

	private static boolean isVoidChar(char c) {
		for ( char voidChar : VOID_CHARACTERS ) {
			if ( voidChar == c ) return true ;
		}
		return false ;
	}
	private static boolean isSpecialChar(char c) {
		for ( char specialChar : SPECIAL_CHARACTERS ) {
			if ( specialChar == c ) return true ;
		}
		return false ;
	}
}
