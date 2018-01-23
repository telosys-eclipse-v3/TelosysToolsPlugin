package org.telosys.tools.eclipse.plugin.toolbar;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;
import org.telosys.tools.eclipse.plugin.commons.EclipseProjUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

public class ActionOpenProperties_OLD extends ActionDelegate implements
		IWorkbenchWindowActionDelegate {

	/**
	 * @see ActionDelegate#run(IAction)
	 */
	@Override
	public void run(IAction action) {

		//ISelection sel = HandlerUtil.getCurrentSelection();
		//test1();
		//test2();
		test3();
		//test4();
		
		//IHandlerService handlerService = getSite().getService(IHandlerService.class);
		//MsgBox.info("ActionOpenProperties Execution ");
	}
	
	private void log(String msg) { // ERROR : "Could not create view"
		PluginLogger.log(msg);
	}
	
	private void test1() { // ERROR : "Could not create view"
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		
		//page.activate(workbenchPartToActivate);
		try {
			page.showView("org.telosys.tools.eclipse.plugin.properties.PropertiesPage");
			
		} catch (PartInitException e) {
			MsgBox.error("ActionOpenProperties error ", "PartInitException : " + e.getMessage() );
		}
	}
	
	private void test2() { // ERROR : "Could not create view"
//		IViewPart propSheet =
//			getWorkbenchPart().getSite().getPage().findView(IPageLayout. ID_PROP_SHEET);
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IViewPart viewPart = page.findView(IPageLayout.ID_PROP_SHEET);
		if ( viewPart != null) {
//			getWorkbenchPart().getSite().getPage().bringToTop(viewPart) ;
			page.bringToTop(viewPart);
		}
		else {
			MsgBox.error("ActionOpenProperties error ", "Cannot get IViewPart" );
		}
	}
	
	private void test3() {
		log("test3...");
		IProject iProject = getCurrentProject(false);
		if ( iProject != null ) {
			log("test3 : project found : " + iProject.getName() );
//			//String propertyPageId = "org.telosys.tools.eclipse.plugin.properties.PropertiesPage";
//			String propertyPageId = "org.telosys.tools.eclipse.plugin.config.view.PropertiesPage" ; // The VIEW
//			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
//			//MessageDialog.openInformation(shell, "Project Proeperties", "Properties window will open next");
//
//			//PreferenceDialog dialog = PreferencesUtil.createPropertyDialogOn(shell, iProject, propertyPageId, null, null);
////			PreferenceDialog dialog = PreferencesUtil.createPropertyDialogOn(shell, iProject, 
////					propertyPageId, new String[] { propertyPageId }, null);
//			PreferenceDialog dialog = PreferencesUtil.createPropertyDialogOn(shell, iProject, 
//					propertyPageId, new String[] { propertyPageId }, null);
			
			//openTelosysPropertiesView(iProject);
			EclipseProjUtil.openTelosysPropertiesView(iProject);
		}
//		else {
//			log("test3 : project not found !" );
//			MsgBox.warning("No current project");
//		}
	}

//	private void test4() {
//		log("test4...");
//		IAdaptable selectedElement = getCurrentSelection() ;
//		if ( selectedElement != null ) {
//			log("test3 : selectedElement found : " + selectedElement );
//			openTelosysPropertiesView(selectedElement);
//		}
//		else {
//			log("test3 : project not found !" );
//			MsgBox.warning("No current project");
//		}
//	}


	private IProject getCurrentProjectOLD() {
		// ISelection sel = HandlerUtil.getCurrentSelection(event);
		//PlatformUI.getWorkbench().getActiveWorkbenchWindow().
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		return null ;
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
		        	message(silent, "Cannot identify current project from current selection.\n" +
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
//	        if (firstElement instanceof IAdaptable) {
//	        	log("IAdaptable instance ");
//	            //IProject project = (IProject)((IAdaptable)firstElement).getAdapter(IProject.class);
//	        	IAdaptable adaptable = (IAdaptable)firstElement ;
//	            IProject project = getProject(adaptable);
//	            //System.out.println(path);
//	            MsgBox.info("Current project : " + ( project != null ? project.getFullPath() : "null" ) );
//	            return project ;
//	        }
//	        else {
//	        	log("Not a IAdaptable ! ");
//	        }
            IProject project = getProject(selectedElement);
            //System.out.println(path);
            //MsgBox.info("Current project : " + ( project != null ? project.getFullPath() : "null" ) );
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
			//adaptable.ge
    		return null ;
		}
	}
	
	//==========================================================================================

	private IAdaptable getCurrentSelection() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	    if (window != null)
	    {
	        IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection();
	        log("selection size = " + selection.size() ) ;
	        if ( selection.isEmpty() ) {
	        	MsgBox.info("No project selected "  );
	        	return null ;
	        }
	        else if (selection.size() > 1 ){
	        	MsgBox.info("Multiple items selected "  );
	        	// TODO
	        	// if same project or not 
	        	return null ;
	        }
	        else {
		        Object firstElement = selection.getFirstElement();
	        	log("Only one item selected : " + firstElement );
		        if (firstElement instanceof IAdaptable) {
		        	log("IAdaptable instance ");
		            return (IAdaptable) firstElement ;
		        }
		        else {
		        	log("Not a IAdaptable ! ");
		        	MsgBox.error("Cannot get selection", "Not a IAdaptable");
		        }
	        }
	    }
	    else {
            MsgBox.info("No active WorkbenchWindow ! "  );
	    }
	    return null ;
	}

	IProject getProjectOLD(IAdaptable adaptable) {
    	log("getProject( " + adaptable + ")" );
		if ( adaptable instanceof IResource ) {
			IResource resource = (IResource) adaptable ;
			IProject project = resource.getProject();
			return project ;
		}
		else {
			//adaptable.ge
	    	log("getProject( " + adaptable + ") : not a IResource !" );
		}
		return null ;
	}
	
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