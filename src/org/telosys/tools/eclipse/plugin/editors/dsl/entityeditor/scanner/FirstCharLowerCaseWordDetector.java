package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.scanner;

import org.eclipse.jface.text.rules.IWordDetector;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

/**
 * 'Word Detector' for a word starting by a Lower Case <br>
 * Used to detect  field types : "string", "integer", etc <br>
 * and field names : "code", "name", "firstName", "lastName", etc <br>
 * 
 */
public class FirstCharLowerCaseWordDetector implements IWordDetector {
	
	private static void log(String msg) {
		if ( _PackageLoggerConfig.LOG ) {
			PluginLogger.log(FirstCharLowerCaseWordDetector.class, msg);
		}
	}
	
    /**
     * Constructor
     */
    public FirstCharLowerCaseWordDetector() {
    	log("Constructor");
    }

    @Override
    // Returns whether the specified character is valid as the first character in a word.
    public boolean isWordStart(char c) {
    	boolean r = Character.isLowerCase(c);
    	//log("isWordStart("+ c +") -> " + r);
    	return r;
    }

    @Override
    // Returns whether the specified character is valid as a subsequent character in a word.
    public boolean isWordPart(char c) {
    	boolean r = Character.isLetterOrDigit(c);
    	//log("isWordPart("+ c +") -> " + r);
    	return r;
    }
    
}
