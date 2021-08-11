package org.telosys.tools.eclipse.plugin.editors.velocity;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;

/**
 * Implementation for ITextDoubleClickStrategy <br>
 * <br>
 * A text double click strategy defines the reaction of a text viewer to mouse double click events. <br>
 * The strategy must be installed on an ITextViewer. <br>
 * <br>
 * Clients may implement this interface or use the standard implementation DefaultTextDoubleClickStrategy.
 * 
 * @author Laurent GUERIN
 *
 */
public class VelocityDoubleClickStrategy implements ITextDoubleClickStrategy {
	protected ITextViewer fText;

	/**
	 * @see org.eclipse.jface.text.ITextDoubleClickStrategy#doubleClicked(org.eclipse.jface.text.ITextViewer)
	 */
	// The mouse has been double clicked on the given text viewer.
	@Override
	public void doubleClicked(ITextViewer part) {
		int pos = part.getSelectedRange().x;

		if (pos < 0)
			return;

		fText = part;

//		if (!selectComment(pos)) {
//			selectWord(pos);
//		}
		selectWord(pos);
	}

	private boolean selectComment(int caretPos) {
		IDocument doc = fText.getDocument();
		int startPos;
		int endPos;

		try {
			int pos = caretPos;
			char c = ' ';

			while (pos >= 0) {
				c = doc.getChar(pos);
				if (c == '\\') {
					pos -= 2;
					continue;
				}
				if (c == Character.LINE_SEPARATOR || c == '\"')
					break;
				--pos;
			}

			if (c != '\"')
				return false;

			startPos = pos;

			pos = caretPos;
			int length = doc.getLength();
			c = ' ';

			while (pos < length) {
				c = doc.getChar(pos); // throws BadLocationException
				if (c == Character.LINE_SEPARATOR || c == '\"')
					break;
				++pos;
			}
			if (c != '\"')
				return false;

			endPos = pos;

			int offset = startPos + 1;
			int len = endPos - offset;
			fText.setSelectedRange(offset, len);
			return true;
		} catch (BadLocationException ex) {
			return false;		
		}

//		return false;
	}

	/**
	 * Select the word at the caret position
	 * @param caretPos
	 * @return
	 */
	private boolean selectWord(int caretPos) {

		IDocument doc = fText.getDocument();
		int startPos;
		int endPos;

		try {

			int pos = caretPos;
			char c;
			// go back (to the left)
			while (pos >= 0) {
				c = doc.getChar(pos); // throws BadLocationException
				if (!isIdentifierPart(c))
					break;
				--pos; // continue (one step to the left)
			}

			startPos = pos;

			// go forward (to the right)
			pos = caretPos;
			int length = doc.getLength();

			while (pos < length) {
				c = doc.getChar(pos); // throws BadLocationException
				if (!isIdentifierPart(c))
					break;
				++pos; // continue (one step to the right)
			}

			endPos = pos;
			selectRange(startPos, endPos);
			return true;

		} catch (BadLocationException ex) {
			return false;
		}

		//return false;
	}

	private void selectRange(int startPos, int stopPos) {
		int offset = startPos + 1;
		int length = stopPos - offset;
		fText.setSelectedRange(offset, length);
	}

	/**
     * Determines if the specified character may be part of a Velocity
     * identifier as other than the first character.
     * A character may be part of a Velocity identifier if and only if
     * it is one of the following:
     * <ul>
     * <li>a letter (a..z, A..Z)
     * <li>a digit (0..9)
     * <li>a hyphen ("-")
     * <li>a underscore("_")
     * </ul>
     * 
     * @param aChar  the character to be tested.
     * @return true if the character may be part of a Velocity identifier; 
     *          false otherwise.
     * @see java.lang.Character#isLetterOrDigit(char)
	 */
	private boolean isIdentifierPart(char aChar) {
		return (Character.isLetterOrDigit(aChar) || aChar == '-' || aChar == '_');
	}
}