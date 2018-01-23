package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.scanner;

import java.util.ArrayList;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.EntityEditorColorManager;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.EntityEditorUtil;

/**
 * A generic scanner which can be "programmed" with a sequence of rules. 
 * The scanner is used to get the next token by evaluating its rule in sequence until one is successful. 
 * If a rule returns a token which is undefined, the scanner will proceed to the next rule. 
 * Otherwise the token provided by the rule will be returned by the scanner. 
 * If no rule returned a defined token, this scanner returns a token which returns true when calling isOther, 
 * unless the end of the file is reached. In this case the token returns true when calling isEOF.
 */
public class EntityEditorScanner extends RuleBasedScanner {

	private static void log(String msg) {
		if ( _PackageLoggerConfig.LOG ) {
			PluginLogger.log(EntityEditorScanner.class, msg);
		}
	}

    /**
     * Constructor
     * @param manager
     */
    public EntityEditorScanner(EntityEditorColorManager manager) { // throws EntityEditorException {
    	
    	log("Constructor()...");
    	
    	// rules are evaluated in sequence until one is successful
        ArrayList<IRule> rulesList = new ArrayList<IRule>();

        // Different kinds of rules :
        // . EndOfLineRule 
        // . MultiLineRule
        // . NumberRule
        // . PatternRule
        // . SingleLineRule
        // . WhitespaceRule
        // . WordPatternRule
        // . WordRule
        // . IPredicateRule
        
        //----- Rule for white spaces 
        // WhitespaceRule :
        //   An implementation of IRule capable of detecting whitespace. 
        //   A whitespace rule uses a whitespace detector in order to find out which characters are whitespace characters
        rulesList.add( new WhitespaceRule(new EntityWhitespaceDetector()) ); 

        //----- Rule for comment : // abcd 
        // "EndOfLineRule"
        rulesList.add( new EndOfLineRule("//", EntityEditorTokens.COMMENT_TOKEN) );
        
//        //----- Rule for literal string : starts with '"' and ends with '"', e.g. "abcd" 
//		// SingleLineRule : 
//		//   A specific configuration of pattern rule whereby the pattern 
//		//   begins with a specific sequence and may end with a specific sequence, 
//		//   but will not span more than a single line.
//        rulesList.add( new SingleLineRule("\"", "\"", EntityEditorTokens.LITERAL_STRING_TOKEN) );
        
		//----- Rule for annotation names : @Id @Max
        WordRule annotationRule = new WordRule(new FieldAnnotationWordDetector(), EntityEditorTokens.DEFAULT_TOKEN);
        for (String word : EntityEditorUtil.getAnnotations()) {
            annotationRule.addWord(word, EntityEditorTokens.ANNOTATION_TOKEN);
        }
		rulesList.add(annotationRule);
		
		//----- Rule for annotation parenthesis : (20) () 
		// SingleLineRule : 
		//   A specific configuration of pattern rule whereby the pattern 
		//   begins with a specific sequence and may end with a specific sequence, 
		//   but will not span more than a single line.
		rulesList.add( new SingleLineRule( "(",  ")", EntityEditorTokens.ANNOTATION_TOKEN) );

		//----- Rule for any word starting by an Upper Case : "Car", "Book", "Azz2xx", etc
        WordRule entityNameRule = new WordRule(new FirstCharUpperCaseWordDetector(), EntityEditorTokens.ENTITY_NAME_TOKEN); 
		rulesList.add(entityNameRule);

		//----- Rule for any word starting by a Lower Case : "code", "firstName", "string", "integer"
		// Used for field name and field type
		WordRule wordRuleFirstCharLowerCase = new WordRule(new FirstCharLowerCaseWordDetector(), EntityEditorTokens.DEFAULT_TOKEN) ;
        for (String str : EntityEditorUtil.getEntityFieldTypes() ) {
        	wordRuleFirstCharLowerCase.addWord(str, EntityEditorTokens.FIELD_TYPE_TOKEN); // specific color for predefined types "string", "date", etc
        }
		rulesList.add(wordRuleFirstCharLowerCase);

//        //----- Rule for LOWERCASE WORD 
//        WordRule fieldTypeRule = new WordRule(new FieldTypeWordDetector(), EntityEditorTokens.FIELD_NAME_TOKEN); // consider as field name 
//        // List of specific words : "integer", "string", "date", ...
//        for (String str : EntityEditorUtil.getEntityFieldTypes() ) {
//            fieldTypeRule.addWord(str, EntityEditorTokens.FIELD_TYPE_TOKEN); // specific color for predefined types "string", "date", etc
//        }
//		rulesList.add(fieldTypeRule);

//        //----- Rule for Entity Name : Book, Country, etc
//        // WordRule : 
//        //   An implementation of IRule capable of detecting words. 
//        //   A word rule also allows to associate a token to a word. 
//        //   That is, not only can the rule be used to provide tokens for exact matches, 
//        //   but also for the generalized notion of a word in the context in which it is used. 
//        //   A word rule uses a word detector to determine what a word is.
//        rulesList.add( new WordRule(new EntityNameWordDetector(), ENTITY_NAME_TOKEN) );
        
		//rulesList.add( new EntitySpecificRule() );
		
		// Set the RULES for this SCANNER
		IRule[] rulesArray = rulesList.toArray( new IRule[0] );
        this.setRules(rulesArray);
    }
}
