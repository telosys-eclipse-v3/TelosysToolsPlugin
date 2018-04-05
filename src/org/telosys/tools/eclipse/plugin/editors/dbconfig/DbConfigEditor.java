package org.telosys.tools.eclipse.plugin.editors.dbconfig;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.dbcfg.DatabasesConfigurations;
import org.telosys.tools.commons.dbcfg.DbConfigManager;
import org.telosys.tools.db.observer.DatabaseObserverProvider;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.console.DbMetadataObserver;
import org.telosys.tools.eclipse.plugin.console.DbModelObserver;

/**
 * Main entry point for the editor <br>
 * This editor contains only one page (since ver 3.0.0) <br>
 * 
 */
public class DbConfigEditor extends FormEditor 
{
    private boolean           _dirty = false; // The dirty flag : see isDirty()
	private IFile             _file = null ;
	private DatabasesConfigurations   databasesConfigurations = new DatabasesConfigurations(); // Void configuration
		
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
	 */
	protected void addPages() {
		PluginLogger.log(this, "addPages()..." );
		DbConfigEditorPage1 page1 = new DbConfigEditorPage1(this, "DbConfigEditorPage1", " Database ");
		//DbConfigEditorPage2 page2 = new DbConfigEditorPage2(this, "DbConfigEditorPage2", " Log viewer");
		try {
			PluginLogger.log(this, "addPages() : addPage(page1)" );
			addPage(page1);
			//addPage(page2);
		} catch ( Exception e ) {
			MsgBox.error("addPage(page) Exception ", e);
		}
		PluginLogger.log(this, "addPages() : all pages added " );
		
		// Only ONE PAGE => remove the single tab at the bottom of the editor
		// see : https://www.modumind.com/2008/01/04/using-formeditor-when-you-only-have-a-single-page/
		// int n = getPageCount(); // one page
		Composite container = getContainer();
		if ( container instanceof CTabFolder ) {
			((CTabFolder) container).setTabHeight(0);
		}
		else {
			MsgBox.error("addPages() : the container is not an instance of CTabFolder !");
		}
		PluginLogger.log(this, "addPages() : end of method " );
	}

	@Override
	public boolean isDirty() {
		return _dirty;
	}

	public void setDirty() {
		setDirty(true);
	}
	
	private void setDirty(boolean flag) {
		_dirty = flag ;
		editorDirtyStateChanged(); // Notify the editor 
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		PluginLogger.log(this, "doSave()..." );

		monitor.beginTask( "Saving the repository...", IProgressMonitor.UNKNOWN );

		//--- Save the file
		DbConfigManager dbDonfigManager = new DbConfigManager( EclipseWksUtil.toFile(_file) );
		try {
			dbDonfigManager.save(this.databasesConfigurations) ;
			setDirty(false);
		} catch (TelosysToolsException e) {
			MsgBox.error("Cannot save file.", e);
		}
		
		//--- Refresh the file in the Eclipse workspace
		try {
			_file.refreshLocal(IResource.DEPTH_ZERO, monitor);
		} catch (CoreException e) {
			MsgBox.error("Cannot refresh the file after save." + e );
		}
		
		monitor.done();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		PluginLogger.log(this, "doSaveAs()..." );
		// Never called because isSaveAsAllowed() returns false
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false ; // "Save as..." not allowed for this file
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);

		// MsgBox.debug("DbConfig editor : init ");
		
		// Set the observers for console reporting
		DatabaseObserverProvider.setModelObserverClass(DbModelObserver.class);
		DatabaseObserverProvider.setMetadataObserverClass(DbMetadataObserver.class);

		PluginLogger.log(this, "--- INIT ---" );
		PluginLogger.log(this, "init(..,..) : site id = '" + site.getId() + "'" );
		PluginLogger.log(this, "init(..,..) : input name = '" + input.getName() + "'" );
		setPartName(input.getName());
		
		if ( input instanceof IFileEditorInput ) {
			IFileEditorInput fileInput = (IFileEditorInput) input;
			_file = fileInput.getFile();

			PluginLogger.log(this, "init(..,..) : file name = '" + _file.getName() + "'" );
			try {
				DbConfigManager dbDonfigManager = new DbConfigManager( EclipseWksUtil.toFile(_file) );
				this.databasesConfigurations = dbDonfigManager.load() ;
				PluginLogger.log(this, "init(..,..) : file loaded, " 
						+ this.databasesConfigurations.getNumberOfDatabases() + " db configuration(s)." );

			} catch (TelosysToolsException e) {
				MsgBox.error("Cannot load databases configurations.", e );
			}		
		}
		else {
			// never happen 
			MsgBox.error("The editor input '" + input.getName() + "' is not a File ! ");
		}
		
//		FormToolkit toolkit = this.getToolkit() ; // NPE : toolkit is null !
//		FormColors formColors = toolkit.getColors();
//		formColors.setBackground(PluginColors.red());

	}
	
	public IFile getFile() {
		return _file ;
	}
	
	public IProject getProject() {		
		return _file.getProject() ;
	}
	
	public DatabasesConfigurations getDatabasesConfigurations() {
		return this.databasesConfigurations ;
	}
	
//	public void addPageChangedListener(IPageChangedListener listener) {
//		super.addPageChangedListener(listener);
//	}

}