package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.scanner.tests;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.scanner.EntityEditorTokens;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.scanner.EntityWhitespaceDetector;

public class EntitySpecificRule implements IRule {
	
	private static void log(String msg) {
		if ( _PackageLoggerConfig.LOG ) {
			PluginLogger.log(EntitySpecificRule.class, msg);
		}
	}
	
//	/** Token to return for this rule */
//	private final IToken token;
//	
//	/**
//	 * Constructor
//	 * @param token
//	 */
//	public EntitySpecificRule (IToken token) {
//		log("Constructor");
//		this.token = token;
//	}
//	
	
	private final EntityWhitespaceDetector whitespaceDetector = new EntityWhitespaceDetector() ;
	
	public EntitySpecificRule () {
		log("Constructor");
	}
	
	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		log("evaluate(ICharacterScanner scanner)");

		//char c = (char) scanner.read();
		int c = 0 ; 
		do {
			c = scanner.read();
			if ( ICharacterScanner.EOF == c ) {
				log("evaluate : c = EOF");
				return EntityEditorTokens.DEFAULT_TOKEN;
			}
			log("evaluate : c = " + c);
		} while ( ! whitespaceDetector.isWhitespace(c) );
		
		if ( whitespaceDetector.isWhitespace(c) ) {
			scanner.unread();
		}
		return EntityEditorTokens.DEFAULT_TOKEN;
		
//		int character= scanner.read();
//		if (isBracket((char) character)) {
//			do {
//				character= scanner.read();
//			} while (isBracket((char) character));
//			scanner.unread();
//			return token;
//		} else {
//			scanner.unread();
//			return Token.UNDEFINED;
//		}
	}
	
//	public boolean isBracket(char character) {
//		for (int index= 0; index < JAVA_BRACKETS.length; index++) {
//			if (JAVA_BRACKETS[index] == character)
//				return true;
//		}
//		return false;
//	}
}
