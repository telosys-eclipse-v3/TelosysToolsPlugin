package org.telosys.tools.eclipse.plugin.editors.velocity.scanner.code;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordPatternRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.telosys.tools.eclipse.plugin.editors.velocity.ColorManager;
import org.telosys.tools.eclipse.plugin.editors.velocity.contentassist.IContextObjectInfo;
import org.telosys.tools.eclipse.plugin.editors.velocity.contentassist.TelosysGeneratorObjectInfo;
import org.telosys.tools.eclipse.plugin.editors.velocity.model.VelocityKeyWord;
import org.telosys.tools.eclipse.plugin.editors.velocity.model.VelocityKeyWords;

public class VelocityCodeScanner extends RuleBasedScanner {

	public VelocityCodeScanner(ColorManager manager) {
		
		List<IRule> rulesList = new ArrayList<IRule>();
		
		//-------------------------------------------------------------------------------
		//--- RULE for generic whitespace rule
		rulesList.add(new WhitespaceRule(new VelocityWhitespaceDetector()));
		
		//-------------------------------------------------------------------------------
		//--- RULE for "directives"	
		rulesList.add( getDirectiveRule(manager) );
		
		//-------------------------------------------------------------------------------
		//--- RULES for "references"
		rulesList.addAll(this.getReferenceRule(manager));
		
//		Token token = new Token(new TextAttribute( manager.getColor(ColorManager.RGB_VELOCITY_REFERENCE) ) );
//		// Add pattern rule for formal references
//		rulesList.add(new PatternRule("$!{", "}", token, (char)0, true));
//		rulesList.add(new PatternRule("${",  "}", token, (char)0, true));
//		// Add pattern rule for shorthand references
//		rulesList.add(new WordPatternRule(new IdentifierDetector(), "$!", null, token));
//		rulesList.add(new WordPatternRule(new IdentifierDetector(), "$",  null, token));
		
		//-------------------------------------------------------------------------------
		// END OF RULES DEFINITION
		IRule[] rulesArray = new IRule[rulesList.size()];
		rulesList.toArray(rulesArray);
		setRules(rulesArray);
	}
	
	/**
	 * Returns a WordRule for Velocity References ( ie : $something ) <br>
	 * BOLD if is a reference reconize by the system or italic if not.
	 * @param colorManager
	 * @return
	 */
	private List<IRule> getReferenceRule(ColorManager manager) {
		List<IRule> rules = new ArrayList<IRule>();
		Color referenceColor = manager.getColor(ColorManager.RGB_VELOCITY_REFERENCE) ;
				
		// References known by the systems
		Token token = new Token(new TextAttribute(referenceColor,null,SWT.BOLD ) );
		// Add pattern rule for known references
		IContextObjectInfo context = new TelosysGeneratorObjectInfo();
		List<VelocityKeyWord> words = context.getPredefineVariables();
		words.addAll(context.getContextBeans());
		for ( VelocityKeyWord reference : words ) {
			for (String formatName : this.getAllFormalFormattedNameFor(reference.getValue())) {
				rules.add(new PatternRule(formatName, "}", token, (char)0, true));
			}
			for (String formatName : this.getAllShortHandFormattedNameFor(reference.getValue())) {
				//rules.add(new PatternRule(formatName, " ", token, (char)0, true, true));
				// 2013-06-12 / Bug Fix : ")" coloring
				rules.add(new WordPatternRule(new IdentifierDetector(), formatName, null, token)); 
			}			
		}

		token = new Token(new TextAttribute(referenceColor,null,SWT.ITALIC ) );
		// Add pattern rule for formal references
		rules.add(new PatternRule("$!{", "}", token, (char)0, true));
		rules.add(new PatternRule("${",  "}", token, (char)0, true));	
		// Add pattern rule for shorthand references
		rules.add(new WordPatternRule(new IdentifierDetector(), "$!", null, token));
		rules.add(new WordPatternRule(new IdentifierDetector(), "$",  null, token));
		
		return rules;
	}

	//-----------------------------------------------------------------------------------------------
	/**
	 * Returns a WordRule for Velocity DIRECTIVE ( ie : #something ) <br>
	 * 
	 * @param colorManager
	 * @return
	 */
	private WordRule getDirectiveRule(ColorManager colorManager) {
		Color directiveColor = colorManager.getColor(ColorManager.RGB_VELOCITY_DIRECTIVE) ;
		//--- All word starting with "#"
		Token token = new Token(new TextAttribute(directiveColor));
		WordRule wordRule = new WordRule( new VelocityDirectiveDetector(), token );
		//--- System directives ( list of predefined keywords : #set, #if, #end, ... )
		token = new Token(new TextAttribute(directiveColor, null, SWT.BOLD));
		String[] directives = VelocityKeyWords.getAllDirectives();
		for ( String directive : directives ) {
			wordRule.addWord(directive, token);
		}
		//--- User directives ( TODO ? )
		return wordRule ;
	}
	
	/**
	 * Return all possibilities formats for a reference in a Shorthand way.
	 * ex : $bean, $!bean
	 * @return all formats
	 */
	private List<String> getAllShortHandFormattedNameFor(String name) {
		List<String> formats = new ArrayList<String>();
		
		formats.add("$" + name);
		formats.add("$" + name + ".");
		formats.add("$!" + name);
		formats.add("$!" + name + ".");		
		
		return formats;
	}

	/**
	 * Return all possibilities formats for a reference in a formal way.
	 * ex : ${bean}, $!{bean} ...
	 * @return all formats
	 */
	private List<String> getAllFormalFormattedNameFor(String name) {
		List<String> formats = new ArrayList<String>();
			
		formats.add("${" + name);
		formats.add("${" + name + ".");
		formats.add("$!{" + name);
		formats.add("$!{" + name + ".");
		
		return formats;
	}
}
