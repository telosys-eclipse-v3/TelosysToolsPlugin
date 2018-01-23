package org.telosys.tools.eclipse.plugin.editors.velocity.scanner.partition;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordPatternRule;
import org.telosys.tools.eclipse.plugin.editors.velocity.scanner.code.EmptyCommentDetector;

/**
 * Scanner for document partitioning 
 * 
 * @author Laurent Guerin
 *
 */
public class VelocityPartitionScanner extends RuleBasedPartitionScanner {
	
	public final static String VELOCITY_DOCUMENT_COMMENT    = "__velocity_document_comment";
	public final static String VELOCITY_MULTILINE_COMMENT   = "__velocity_multiline_comment";
	public final static String VELOCITY_SINGLELINE_COMMENT  = "__velocity_singleline_comment";

	public final static String[] TYPES = new String[] {
				VELOCITY_DOCUMENT_COMMENT,
				VELOCITY_MULTILINE_COMMENT, 
				VELOCITY_SINGLELINE_COMMENT };
	 
	public VelocityPartitionScanner() {

		List<IPredicateRule> rulesList = new ArrayList<IPredicateRule>();

		rulesList.add( new MultiLineRule("#**", "*#",  new Token(VELOCITY_DOCUMENT_COMMENT)) ) ;
		rulesList.add( new MultiLineRule("#*",  "*#",  new Token(VELOCITY_MULTILINE_COMMENT)) ) ;
		rulesList.add( new EndOfLineRule("##",         new Token(VELOCITY_SINGLELINE_COMMENT) ) ) ;

		// Add special empty comment word rules
		rulesList.add(new WordPatternRule(new EmptyCommentDetector(), "#***#", null, new Token(VELOCITY_DOCUMENT_COMMENT)) );
		rulesList.add(new WordPatternRule(new EmptyCommentDetector(), "#**#",  null, new Token(VELOCITY_MULTILINE_COMMENT)) );
		
		//-------------------------------------------------------------------------------
		// END OF RULES DEFINITION
		IPredicateRule[] rulesArray = new IPredicateRule[rulesList.size()];
		rulesList.toArray(rulesArray);
		setPredicateRules(rulesArray);
	}
}
