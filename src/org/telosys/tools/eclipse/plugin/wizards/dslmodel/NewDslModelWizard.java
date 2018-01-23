package org.telosys.tools.eclipse.plugin.wizards.dslmodel;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.dsl.DslModelUtil;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;
import org.telosys.tools.eclipse.plugin.commons.FileEditorUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

/**
 * New DSL model Wizard 
 * 
 * @author L. Guerin
 *
 */
public class NewDslModelWizard extends Wizard implements INewWizard  {

	private IProject projectSelected = null ;
	private NewDslModelWizardPage page1 ;
	
	private void log(String msg) {
		PluginLogger.log(this, msg);
	}
	
	/*
	 * Wizard lifecycle :
	 * - constructor
	 * - init()
	 * - addPages()
	 * - getWindowTitle()
	 * User input
	 * User "Finish" or "Cancel" => performFinish() or performCancel()
	 * 
	 */
	
	public NewDslModelWizard() {
		super();
        log("Constructor" );
		setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		log("init()" );
		// The selection parameter will hold IResource objects that were selected 
		// in the current navigator when launching the wizard. 
		// From IResource, you can get the project.
		if ( ! selection.isEmpty() ) {
			Object firstElement = selection.getFirstElement();
	        if ( firstElement instanceof IResource ) {
	        	projectSelected = ((IResource) firstElement).getProject();
	        	log("init : Current project " + projectSelected.getName() );
	        }
		}
		else {
			// yes it can happen (when launched from the menu without selection in the view)
			// The user must choose a project !
			projectSelected = null ;
			log("init : No current project " );
		}
	}
	
	@Override
	public void addPages() {
		log("addPages()" );
		page1 = new NewDslModelWizardPage(projectSelected);
		addPage(page1);
	}

	@Override
	public String getWindowTitle() {
		log("getWindowTitle()" );
		return "New DSL model";
	}

	@Override
	public boolean performFinish() {  // abstract method => must be implemented
		log("performFinish() : project path = " + page1.getProjectPath() );
		log("performFinish() : model name = " + page1.getModelName() );
		createNewModel();
		return true;
	}
	
	public boolean performCancel() { // not abstract method => not mandatory
		log("performCancel() " );
		return super.performCancel();
	}
	
	private void createNewModel() {
		String projectAbsolutePath = page1.getProjectPath() ;
		String modelName = page1.getModelName() ;
		log("createNewModel() : " + projectAbsolutePath + " " + modelName );
		TelosysProject telosysProject = new TelosysProject(projectAbsolutePath);
		
		File modelFile;
		
		try {
			modelFile = telosysProject.getDslModelFile(modelName);
			if ( modelFile.exists() ) {
				MsgBox.error("Model '" + modelName + "' already exists" );
				return;
			}
		} catch (TelosysToolsException e) {
			MsgBox.error("Unexpected error", e );
			return;
		}
		
		try {
			modelFile = telosysProject.createNewDslModel(modelName);
			// Refresh "_model" folder
			EclipseWksUtil.refresh(DslModelUtil.getModelFolder(modelFile));
			// Refresh ".model" file 
			//EclipseWksUtil.refresh(modelFile.getParentFile());
			EclipseWksUtil.refresh(modelFile);
			// Open ".model" file in the model editor
			FileEditorUtil.openModelFileInEditor( modelFile.getAbsolutePath());
		} catch (Exception e) {
			MsgBox.error("Cannot create model", e);
		}
	}
}
