package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.completion;

import java.util.ArrayList;
import java.util.List;

import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.EntityEditorUtil;

public class SuggestionsForAttributeAnnotation {

    /**
     * Returns all the suggestions for the given beginning of annotation
     * 
     * @param beginningOfWord
     * @return
     */
    protected static List<String> getSuggestions(String beginningOfWord) {
        ArrayList<String> suggestions = new ArrayList<String>();
        
    	for ( String str : EntityEditorUtil.getAnnotationsWithParenthesis() ) {
            if (str.startsWith(beginningOfWord)) {
            	suggestions.add(str + " ");
            }
        }
    	return suggestions;
    }
    
}
