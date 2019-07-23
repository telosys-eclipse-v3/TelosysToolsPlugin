package org.telosys.tools.eclipse.plugin.config.view;

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
import org.telosys.tools.api.TelosysGlobalEnv;

public class DialogBoxGitHubCredentials extends Dialog  {

	private Text tUser = null ;
	private Text tPassword = null ;

    /**
     * Constructor
     * @param parentShell
     */
    protected DialogBoxGitHubCredentials(Shell parentShell) {
		super(parentShell);
	}

	@Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        
		GridLayout gridLayout = new GridLayout(2, false);
		container.setLayout(gridLayout);

		//--- Void line
		createLabelSpan2(container, "");

		Label label ;

		//--- Line 1 : Label + Input text 
		label = new Label(container, SWT.NONE);
		label.setText("  User :  ");
		tUser = new Text(container, SWT.BORDER);
		tUser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL) );
		
		//--- Line 2 : Label + Input text 
		label = new Label(container, SWT.NONE);
		label.setText("  Password :  ");
		tPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		tPassword.setLayoutData(new GridData(GridData.FILL_HORIZONTAL) );
		
		//--- Void line
		createLabelSpan2(container, "");

		//--- Message		
		createLabelSpan2(container, "Use your GitHub account to increase API rate limit.");
		createLabelSpan2(container, "These credentials will not be stored");
		createLabelSpan2(container, "(only kept in memory)");
		
		return container;
    }

	private void createLabelSpan2(Composite container, String msg) {
		GridData gdMsg = new GridData(SWT.CENTER) ;
		gdMsg.horizontalSpan = 2 ;
		gdMsg.horizontalAlignment = SWT.CENTER;
		Label label = new Label(container, SWT.NONE);
		label.setText(msg);
		label.setLayoutData(gdMsg);
	}
	
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("GitHub credentials");
    }

    @Override
    protected Point getInitialSize() {
        return new Point(380, 300);
    }
    
    @Override
	protected void okPressed() {
//		PluginLogger.log(this, " ==== OK Pressed ");
		TelosysGlobalEnv.setGitHubUser(tUser.getText(), tPassword.getText());
		super.okPressed();
	}

    @Override
	protected void cancelPressed() {
//		PluginLogger.log(this, " ==== CANCEL Pressed ");
		super.cancelPressed();
	}

}
