package org.telosys.tools.eclipse.plugin.editors.dsl.model;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DialogBoxForRenameEntity extends GenericDialogBox {
	
	private Text _tOldName;
	private Text _tNewName;
	private final String _oldName;
	private String _newName;

	public DialogBoxForRenameEntity(Shell parentShell, String currentName) {
		super(parentShell, "Rename entity");
		_oldName = currentName ;
	}
	
	public String getNewEntityName() {
		return _newName ;
	}

	@Override
	protected void createContent() {
		
		_tOldName = createLabelAndField("Current name : ", _oldName);
//		_tOldName.setEditable(false);
		_tOldName.setEnabled(false);
		
		_tNewName = createLabelAndField("New name : ", "");
	}

	// override method for specific buttons labels
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Rename", true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() {
		_newName = _tNewName.getText();
		super.okPressed();
	}

}