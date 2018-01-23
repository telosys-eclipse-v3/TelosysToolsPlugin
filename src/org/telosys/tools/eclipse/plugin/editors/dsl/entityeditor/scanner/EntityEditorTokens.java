package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.scanner;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.graphics.RGB;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.EntityEditorColorManager;

public class EntityEditorTokens {
	
	private final static EntityEditorColorManager manager = new EntityEditorColorManager();

	private final static IToken createToken(RGB color) {
    	return new Token(new TextAttribute( manager.getColor(color)) );
    }
	
	public final static IToken DEFAULT_TOKEN = createToken(EntityEditorColorManager.DEFAULT_COLOR);
		//new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.DEFAULT_COLOR)) );
		
	protected final static IToken ENTITY_NAME_TOKEN = createToken(EntityEditorColorManager.ENTITY_COLOR);
		//new Token(new TextAttribute(manager.getColor(EntityEditorColorManager.ENTITY_COLOR)));
	
	protected final static IToken COMMENT_TOKEN = createToken(EntityEditorColorManager.COMMENT_COLOR);
    	//new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.COMMENT_COLOR)));
    
	protected final static IToken LITERAL_STRING_TOKEN = createToken(EntityEditorColorManager.STRING_COLOR);
    	// new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.STRING_COLOR)));
    
	protected final static IToken FIELD_TYPE_TOKEN = createToken(EntityEditorColorManager.FIELD_TYPE_COLOR);
    	// new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.FIELD_TYPE_COLOR)) );
    
	protected final static IToken FIELD_NAME_TOKEN = createToken(EntityEditorColorManager.FIELD_NAME_COLOR);
    	// new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.FIELD_NAME_COLOR)) );
    
	protected final static IToken ANNOTATION_TOKEN = createToken(EntityEditorColorManager.ANNOTATION_COLOR);
    	// new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.ANNOTATION_COLOR)));

}
