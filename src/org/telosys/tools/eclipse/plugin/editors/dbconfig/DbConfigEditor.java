package org.telosys.tools.eclipse.plugin.editors.dbconfig;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.dbcfg.DatabasesConfigurations;
import org.telosys.tools.commons.dbcfg.DbConfigManager;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.commons.TextWidgetLogger;

/**
 * Main entry point for the editor <br>
 * This editor contains x pages : <br>
 * . 1 : the table view with the mapping table<br>
 * . 2 : the log view <br>
 * 
 */
public class DbConfigEditor extends FormEditor 
{
	/** The dirty flag : see isDirty() */
    private boolean           _dirty = false;
//	private String            _fileName = "???" ;
	private IFile             _file = null ;
	private DatabasesConfigurations   databasesConfigurations = new DatabasesConfigurations(); // Void configuration
		
	private TextWidgetLogger  _logger = new TextWidgetLogger() ;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
	 */
	protected void addPages() {
		PluginLogger.log(this, "addPages()..." );
		DbConfigEditorPage1 page1 = new DbConfigEditorPage1(this, "DbConfigEditorPage1", " Database ");
		DbConfigEditorPage2 page2 = new DbConfigEditorPage2(this, "DbConfigEditorPage2", " Log viewer");
		try {
			addPage(page1);
			addPage(page2);
		} catch ( Exception e ) {
			MsgBox.error("addPage(page) Exception ", e);
		}
	}

	public boolean isDirty()
	{
		return _dirty;
	}

	public void setDirty()
	{
		setDirty(true);
	}
	
	private void setDirty(boolean flag)
	{
		_dirty = flag ;
		editorDirtyStateChanged(); // Notify the editor 
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
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
//		IEditorInput input = getEditorInput();
//		IPersistableElement e = input.getPersistable();
		
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
	public void doSaveAs() {
		PluginLogger.log(this, "doSaveAs()..." );
		// Never called because isSaveAsAllowed() returns false
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		//PluginLogger.log(this, "isSaveAsAllowed()..." );
		return false ; // "Save as..." not allowed for this file
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);

		// MsgBox.debug("DbConfig editor : init ");

		PluginLogger.log(this, "--- INIT ---" );
		PluginLogger.log(this, "init(..,..) : site id = '" + site.getId() + "'" );
		PluginLogger.log(this, "init(..,..) : input name = '" + input.getName() + "'" );
		setPartName(input.getName());
		
//		_fileName = input.getName() ;

		if ( input instanceof IFileEditorInput )
		{
			IFileEditorInput fileInput = (IFileEditorInput) input;
			_file = fileInput.getFile();

			PluginLogger.log(this, "init(..,..) : file name = '" + _file.getName() + "'" );
			try {
				DbConfigManager dbDonfigManager = new DbConfigManager( EclipseWksUtil.toFile(_file) );
				this.databasesConfigurations = dbDonfigManager.load() ;
				PluginLogger.log(this, "init(..,..) : file loaded, " 
						+ this.databasesConfigurations.getNumberOfDatabases() + " db configuartion(s)." );

			} catch (TelosysToolsException e) {
				MsgBox.error("Cannot load databases configurations.", e );
			}		
		}
		else // never happen 
		{
			MsgBox.error("The editor input '" + input.getName() + "' is not a File ! ");
		}
	}
	
	public TelosysToolsLogger getLogger ()
	{		
		return _logger ;
	}

//	public String getFileName ()
//	{
//		return _fileName ;
//	}
	
	public IFile getFile ()
	{
		return _file ;
	}
	
	public IProject getProject ()
	{		
		return _file.getProject() ;
	}
	
	public DatabasesConfigurations getDatabasesConfigurations() {
		return this.databasesConfigurations ;
	}
	
	public TextWidgetLogger getTextWidgetLogger()
	{
		return _logger ;
	}

//	public void addPageChangedListener(IPageChangedListener listener) {
//		super.addPageChangedListener(listener);
//	}

}