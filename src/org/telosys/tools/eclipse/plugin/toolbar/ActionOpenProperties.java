package org.telosys.tools.eclipse.plugin.toolbar;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;
import org.telosys.tools.eclipse.plugin.commons.EclipseProjUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

public class ActionOpenProperties extends ActionDelegate implements
		IWorkbenchWindowActionDelegate {

	/**
	 * @see ActionDelegate#run(IAction)
	 */
	@Override
	public void run(IAction action) {

		IProject iProject = getCurrentProject(true);
		if ( iProject != null ) {
			log("project found : " + iProject.getName() );
			EclipseProjUtil.openTelosysPropertiesView(iProject);
		}
		else {
			MsgBox.info("No project selected.\n\n" +
					"Please select a project or one of its files.");
		}
	}
	
	private void log(String msg) { // ERROR : "Could not create view"
		PluginLogger.log(msg);
	}
	
	//==========================================================================================
	private void message(boolean silent, String message) {
		if ( ! silent ) {
        	MsgBox.info(message);
		}		
	}
	private IProject getCurrentProject(boolean silent) {
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	    if (workbenchWindow != null)
	    {
	    	ISelection selection = workbenchWindow.getSelectionService().getSelection();
	    	if ( selection != null ) {
	    		if ( selection instanceof IStructuredSelection ) {
	    			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
	    			return getProjectFromStructuredSelection(structuredSelection, silent);
	    		}
	    		else {
		        	message(silent, 
		        			"Cannot identify current project from current selection.\n" +
		        			"The selection is not a 'IStructuredSelection' \n\n" + 
	            			"Class : " + selection.getClass().getCanonicalName() + "\n" +
	            			selection );
	    		}
	        }
	    	else {
	        	message(silent, "Cannot identify current project : no active selection.\n");
	    	}
	    }
	    else {
        	message(silent, "Cannot identify current project : no active WorkbenchWindow.\n");
	    }
	    return null ;
	}
	
	private IProject getProjectFromStructuredSelection(IStructuredSelection selection, boolean silent) {
        log("selection size = " + selection.size() ) ;
        if ( selection.isEmpty() ) {
        	message(silent, "Cannot identify current project (nothing selected)" );
        	return null ;
        }
        else if (selection.size() > 1 ){
        	message(silent, "Cannot identify current project (multiple items selected)" );
        	// TODO
        	// if same project or not 
        	return null ;
        }
        else {
        	// Only one element selected
	        Object selectedElement = selection.getFirstElement();
        	log("Only one item selected : " + selectedElement );
            IProject project = getProject(selectedElement);
            if ( project == null ) {
            	message(silent, "Cannot get current project from selected element.\n\n" + 
            			"Class : " + selectedElement.getClass().getCanonicalName() + "\n" +
            			selectedElement);
            }
            return project ;		
        }
	}
        
	private IProject getProject(Object selected) {
    	log("getProject( " + selected + ")" );
    	IResource resource = (IResource)Platform.getAdapterManager().getAdapter(selected, IResource.class);
    	if ( resource != null ) {
			IProject project = resource.getProject();
			return project ;
		}
		else {
    		return null ;
		}
	}
	
	//==========================================================================================

	/**
	 * @see IWorkbenchWindowActionDelegate#dispose()
	 */
	@Override
	public void dispose() {
	}

	/**
	 * @see IWorkbenchWindowActionDelegate#init(IWorkbenchWindow)
	 */
	@Override
	public void init(IWorkbenchWindow window) {
	}
}