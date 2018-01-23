package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.scanner;

import org.eclipse.jface.text.rules.IWordDetector;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

/**
 * 'Word Detector' for a word starting by an Upper Case <br>
 * Used to determine if a word is an Entity Name <br>
 * eg : 'Book', 'Country', etc
 * 
 */
public class FirstCharUpperCaseWordDetector implements IWordDetector {

	private static void log(String msg) {
		if ( _PackageLoggerConfig.LOG ) {
			PluginLogger.log(FirstCharUpperCaseWordDetector.class, msg);
		}
	}
	
    /**
     * Constructor
     */
    public FirstCharUpperCaseWordDetector() {
    	log("Constructor");
    }

	@Override
    public boolean isWordStart(char c) {
		boolean r = Character.isUpperCase(c);
    	//log("isWordStart("+ c +") -> " + r);
    	return r;
    }

    @Override
    public boolean isWordPart(char c) {
    	boolean r = Character.isLetterOrDigit(c) ;
    	//log("isWordPart("+ c +") -> " + r);
    	return r ;
    }

}
