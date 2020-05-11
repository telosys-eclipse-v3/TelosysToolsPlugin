package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.completion;

public class ProposalContext {

	private final String word ;
	private final boolean afterColon  ;
	private final boolean afterOpenBrace ;
	
	/**
	 * Constructor
	 * @param word
	 * @param afterColon
	 * @param afterOpenBrace
	 */
	public ProposalContext(String word, boolean afterColon, boolean afterOpenBrace ) {
		super();
		this.word = word;
		this.afterColon = afterColon;
		this.afterOpenBrace = afterOpenBrace;
	}

	/**
	 * Returns the word requesting a suggestion : "@Ini" "@", "str", "da", "", etc
	 * @return
	 */
	public String getWord() {
		return word;
	}

	/**
	 * Returns true if the word is located after a colon character ':'
	 * @return
	 */
	public boolean isAfterColon() {
		return afterColon;
	}

	/**
	 * Returns true if the word is located after an open brace character '{' <br>
	 * itself located after a colon character ':'
	 * @return
	 */
	public boolean isAfterOpenBrace() {
		return afterOpenBrace;
	}
	
	/**
	 * Returns the type of proposal according to the word and its position
	 * @return
	 */
	public ProposalType getProposalType() {
		if ( word.startsWith("@") ) {
			if ( afterColon && afterOpenBrace ) {
				// foo : int { @Def 
				return ProposalType.ATTRIBUTE_ANNOTATION ;
			} else {
				if ( afterColon == false && afterOpenBrace == false) {
					// nothing found ( no ':' and no '{' )
					return ProposalType.ENTITY_ANNOTATION ;
				}
			}
		} else {
			if ( afterColon && ! afterOpenBrace ) {
				// foo : str
				return ProposalType.ATTRIBUTE_TYPE ;
			}
		}
		return ProposalType.DEFAULT;
	}

	@Override
	public String toString() {
		return "ProposalContext [word='" + word + "', afterColon=" + afterColon
				+ ", afterOpenBrace=" + afterOpenBrace 
				+ ", proposalType=" + getProposalType() 
				+ "]";
	}
	
}