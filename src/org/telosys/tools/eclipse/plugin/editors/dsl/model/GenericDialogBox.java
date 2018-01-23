package org.telosys.tools.eclipse.plugin.editors.dsl.model;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public abstract class GenericDialogBox extends Dialog {
	
	private final String _title ;
	private Composite _container ;
	
	public GenericDialogBox(Shell parentShell, String title) {
		super(parentShell);
		_title = title ;
	}
	
	// overriding this methods allows to set the title of the custom dialog
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(_title);
	}
	  
	protected abstract void createContent() ;
		
	@Override
	protected Control createDialogArea(Composite parent) {
		_container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(2, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		_container.setLayout(layout);

		createContent();
		
		return _container;
	}

	protected Text createLabelAndField(String labelText, String initialValue) {
		return createLabelAndField(labelText, initialValue, SWT.BORDER) ;
	}
	
	protected Text createLabelAndField(String labelText, String initialValue, int style) {
		
		Label label = new Label(_container, SWT.NONE);
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		//gd_label.horizontalIndent = 1; 
		// horizontalIndent : specifies the number of pixels of indentation that will be placed along the left side of the cell.
		
		label.setLayoutData(gd_label);
		label.setText(labelText);

		Text txt = new Text(_container, style);
		txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt.setText(initialValue);		
//		txt.addModifyListener(new ModifyListener() {
//			@Override
//			public void modifyText(ModifyEvent e) {
//				Text textWidget = (Text) e.getSource();
//				String passwordText = textWidget.getText();
//				password = passwordText;
//			}
//		});
		
		return txt ;
	}
	
//	// override method to use "Login" as label for the OK button
//	@Override
//	protected void createButtonsForButtonBar(Composite parent) {
//		createButton(parent, IDialogConstants.OK_ID, "Login", true);
//		createButton(parent, IDialogConstants.CANCEL_ID,
//				IDialogConstants.CANCEL_LABEL, false);
//	}

	@Override
	protected Point getInitialSize() {
		// Default size : width, height
		return new Point(450, 200);
	}

//	@Override
//	protected void okPressed() {
//		user = txtUser.getText();
//		password = txtPassword.getText();
//		super.okPressed();
//	}
}