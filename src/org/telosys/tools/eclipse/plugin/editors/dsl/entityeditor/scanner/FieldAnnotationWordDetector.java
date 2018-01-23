package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.scanner;

import org.eclipse.jface.text.rules.IWordDetector;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

/**
 * 'Word Detector' for 'annotation' <br>
 * ( used to determine what a word is )<br>
 * eg : '@Id', '@Max', etc
 * 
 */
public class FieldAnnotationWordDetector implements IWordDetector {
	
	private static void log(String msg) {
		if ( _PackageLoggerConfig.LOG ) {
			PluginLogger.log(FieldAnnotationWordDetector.class, msg);
		}
	}
	
    /**
     * Constructor
     */
    public FieldAnnotationWordDetector() {
    	log("Constructor");
    }

    /**
     * Determines if the specified character is permissible as the first
     * character of an annotation 
     */
    public boolean isWordStart(char c) {
    	boolean r = c == '@' ;
    	//log("isWordStart("+ c +") -> " + r );
    	return r ;
    }

    /**
     * Determines if the specified character may be part of an annotation
     * Ok if and only if it is a letter (a..z, A..Z)
     */
    public boolean isWordPart(char c) {
    	boolean r = false ;
    	r =  Character.isLetter(c) ;
    	
//    	if ( c == '(' ) return true ;
//    	if ( Character.isDigit(c) ) return true ;
//    	if ( c == ')' ) return true ;

    	//log("isWordPart("+ c +") -> " + r );
    	return r ;
    }
}