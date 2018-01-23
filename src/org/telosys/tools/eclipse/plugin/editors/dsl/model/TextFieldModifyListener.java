package org.telosys.tools.eclipse.plugin.editors.dsl.model;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.telosys.tools.eclipse.plugin.editors.commons.AbstractStandardEditor;

public class TextFieldModifyListener implements ModifyListener {

	private final AbstractStandardEditor editor ;
	
	public TextFieldModifyListener(AbstractStandardEditor editor) {
		super();
		this.editor = editor;
	}

	@Override
	public void modifyText(ModifyEvent event) {
		if ( ! editor.isPopulateInProgress() ) {
			//--- Set "dirty" flag ON for this editor
			editor.setDirty();
		}
	}
}
