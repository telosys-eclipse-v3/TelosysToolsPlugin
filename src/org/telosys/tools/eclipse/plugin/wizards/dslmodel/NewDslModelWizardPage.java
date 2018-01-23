package org.telosys.tools.eclipse.plugin.wizards.dslmodel;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.telosys.tools.eclipse.plugin.commons.EclipseProjUtil;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;

public class NewDslModelWizardPage extends WizardPage {

	private IProject _projectSelected ;
	private Text     _tModelName;

	/**
	 * Page constructor 
	 * @param project the current project selected (can be null if none)
	 */
	protected NewDslModelWizardPage( IProject project ) {
		super("NewDslModelWizardPage");
		setTitle("New DSL Model");
		setDescription("Create a new DSL model");
		// setControl(text1);
		_projectSelected = project ; // can be null
	}
	
	protected String getProjectPath() {
		if ( _projectSelected != null ) {
			return EclipseProjUtil.getProjectDir( _projectSelected );
		}
		else {
			MsgBox.error("Invalid state : no project selected");
			return null ;
		}
	}
	
	protected String getModelName() {
		return _tModelName.getText();
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite container;

		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		
		createProjectLine(container) ;
		
		//--- Line 1
		Label label1 = new Label(container, SWT.NONE);
		label1.setText("Model name");

		_tModelName = new Text(container, SWT.BORDER | SWT.SINGLE);
		_tModelName.setText("");
		_tModelName.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void keyReleased(KeyEvent e) {
				checkPageComplete();
			}
		});
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		_tModelName.setLayoutData(gd);
		
//		//--- Line 2
//		Label labelCheck = new Label(container, SWT.NONE);
//		labelCheck.setText("This is a check");
//		
//		Button check = new Button(container, SWT.CHECK);
//		check.setSelection(true);
		
		//--- End
		// required to avoid an error in the system
		setControl(container);
		setPageComplete(false);
	}

	private void createProjectLine(Composite container) {
		
		Label label = new Label(container, SWT.NONE);
		label.setText("Project :");

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		
		if ( this._projectSelected != null ) {
			label = new Label(container, SWT.NONE);
			label.setText( this._projectSelected.getName() );
			label.setLayoutData(gd);
		}
		else {
			//--- Combo Box for project selection
			Combo combo = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
			//combo.setBounds(TEXT_X, 25, 260, TEXT_HEIGHT);
			combo.setVisibleItemCount(10);
			//setDatabasesComboAction(_ComboDatabases);
			combo.setLayoutData(gd);
			populateProjectsComboBox(combo);
	        combo.addSelectionListener( new SelectionAdapter() {
	        	@Override
	            public void widgetSelected(SelectionEvent event) {
	                Combo combo = (Combo) event.widget ;
	            	if ( combo.getSelectionIndex() >= 0 ) {
	                    //String s = combo.getText();
	                    int index = combo.getSelectionIndex();
	                    //MsgBox.info("getSelectionIndex = " + index );
	                    @SuppressWarnings("unchecked")
	                    List<IProject> projectsList = (List<IProject>) combo.getData();
	                    _projectSelected = projectsList.get(index);
	                    checkPageComplete();
	            	}
	            }
	        });
		}
	}
	
	private void populateProjectsComboBox(Combo combo) {
		combo.removeAll(); 
		List<IProject> projectsList = new LinkedList<IProject>();
        for ( IProject project : EclipseWksUtil.getWorkspaceProjects() ) {
        	combo.add(project.getName());
        	projectsList.add(project);
        }
        combo.setData(projectsList);
	}
	
	/**
	 * Set the 'page complete' status to 'true' if all the required data 
	 * are available in the page ( project and model name )
	 */
	private void checkPageComplete() {
		if ( ( _tModelName.getText().isEmpty() == false ) && _projectSelected != null ) {
			setPageComplete(true);
		}
		else {
			setPageComplete(false);
		}
	}
}
