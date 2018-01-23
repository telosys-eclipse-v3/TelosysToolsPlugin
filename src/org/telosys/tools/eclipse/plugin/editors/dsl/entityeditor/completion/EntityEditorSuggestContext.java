package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.completion;


public class EntityEditorSuggestContext {

	private final String word ;
	private final EntityEditorContext context ;
	
	public EntityEditorSuggestContext(String word, EntityEditorContext context) {
		super();
		this.word = word;
		this.context = context;
	}

	public String getWord() {
		return word;
	}

	public EntityEditorContext getContext() {
		return context;
	}
	
}
