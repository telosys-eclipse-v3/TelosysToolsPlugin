package org.telosys.tools.eclipse.plugin.editors.dsl.model;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DialogBoxForNewEntity extends GenericDialogBox {
	
	private Text   _tEntityName;
	private String _entityName;

	public DialogBoxForNewEntity(Shell parentShell) {
		super(parentShell, "New entity");
	}

	public String getEntityName() {
		return _entityName ;
	}
	
	@Override
	protected void createContent() {
		_tEntityName = createLabelAndField("Entity name", "");
	}

	// override method to specific labels for the buttons
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Create", true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

//	@Override
//	protected Point getInitialSize() {
//		return new Point(450, 300);
//	}

	@Override
	protected void okPressed() {
		_entityName = _tEntityName.getText();
		super.okPressed();
	}
}