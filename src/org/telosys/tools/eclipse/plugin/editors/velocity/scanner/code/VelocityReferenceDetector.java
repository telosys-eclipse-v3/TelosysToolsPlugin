package org.telosys.tools.eclipse.plugin.editors.velocity.scanner.code;

import org.eclipse.jface.text.rules.IWordDetector;

public class VelocityReferenceDetector implements IWordDetector {
	
	/**
     * Determines if the specified character may be part of a Velocity
     * reference as other than the first character.
     * A character may be part of a Velocity reference if and only if
     * it is one of the following:
     * <ul>
     * <li>a letter (a..z, A..Z)
     * <li>a digit (0..9)
     * <li>a hyphen ("-")
     * <li>a connecting punctuation character ("_")
     * </ul>
     * 
     * @param aChar  the character to be tested.
     * @return true if the character may be part of a Velocity directive; 
     *          false otherwise.
     * @see java.lang.Character#isLetterOrDigit(char)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart
	 */
	@Override
	public boolean isWordPart(char aChar) {
		return Character.isLetterOrDigit(aChar) || aChar == '-' ||
		aChar == '_' || aChar =='{' || aChar =='}';
	}

	@Override
	public boolean isWordStart(char aChar) {
		return aChar == '$';
	}
	

}
