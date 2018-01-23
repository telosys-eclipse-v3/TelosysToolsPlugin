package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.scanner;

import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

/**
 * 'IWhitespaceDetector' implementation <br>
 * Defines the interface by which WhitespaceRule determines whether <br>
 * a given character is to be considered whitespace in the current context. <br>
 *
 */
public class EntityWhitespaceDetector implements IWhitespaceDetector {
	
	private static void log(String msg) {
		if ( _PackageLoggerConfig.LOG ) {
			PluginLogger.log(EntityWhitespaceDetector.class, msg);
		}
	}

    private static final String WHITE_SPACE_CHARACTERS = " \t\n\r:,{}[];";
    
    /**
     * Constructor
     */
    public EntityWhitespaceDetector() {
    	log("Constructor");
    }
    
    @Override
    public boolean isWhitespace(char c) {
        boolean r = WHITE_SPACE_CHARACTERS.indexOf(c) != -1;
    	//log("isWhitespace("+c+") -> " + r );
        return r;
    }
    public boolean isWhitespace(int i) {
        return isWhitespace((char) i);
    }
}
