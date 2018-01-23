package org.telosys.tools.eclipse.plugin.editors.velocity.scanner.code;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * A Velocity directive aware whitespace detector.
 */
/* package */ class VelocityWhitespaceDetector implements IWhitespaceDetector {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IWhitespaceDetector#isWhitespace(char)
	 */
	public boolean isWhitespace(char aChar) {
		return (aChar == ' ' || aChar == '\t' || aChar == '\n' ||
				 aChar == '\r');
	}
}
