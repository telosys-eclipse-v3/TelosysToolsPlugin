package org.telosys.tools.eclipse.plugin.editors.commons;

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
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.cfg.TelosysToolsCfgManager;
import org.telosys.tools.commons.logger.ConsoleLogger;
import org.telosys.tools.eclipse.plugin.commons.EclipseProjUtil;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

/**
 * Abstract ancestor class for an editor (any kind of file) <br>
 * 
 * @author Laurent Guerin
 * 
 */
public abstract class AbstractStandardEditor extends FormEditor {

    private TelosysToolsLogger _logger = new ConsoleLogger() ;

	private IFile           _iFile     = null ; //the current file in the editor
	
    /** The dirty flag : see isDirty() */
    private boolean         _dirty = false;
    
	private boolean         _bPopulateInProgress = false ;

	//----------------------------------------------------------------------------------------
    /**
     * Constructor
     */
    public AbstractStandardEditor() {
    	super();
    }
    
	//----------------------------------------------------------------------------------------
	public TelosysToolsLogger getLogger () {
		return _logger ;
	}
	protected void log(String msg) {
		_logger.log(msg) ;
	}
	protected void log(Object object, String msg) {
		_logger.log(object, msg) ;
	}

	//----------------------------------------------------------------------------------------
    @Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException 
	{
		super.init(site, input);
		PluginLogger.log(this, "init(..,..) : site id = '" + site.getId() + "'" );
		PluginLogger.log(this, "init(..,..) : input name = '" + input.getName() + "'" );
		setPartName(input.getName());
		
		if ( input instanceof IFileEditorInput ) {
			IFileEditorInput fileInput = (IFileEditorInput) input;
			_iFile = fileInput.getFile();
		}
		else { // never happens
			MsgBox.error("The editor input '" + input.getName() + "' is not a File ! ");
		}
	}
    
	//----------------------------------------------------------------------------------------
	public IProject getProject() {
		return _iFile.getProject() ;
	}
	
	//----------------------------------------------------------------------------------------
	public String getProjectFolderAbsolutePath() {
		return EclipseProjUtil.getProjectDir(getProject());
	}
	
	//----------------------------------------------------------------------------------------
	public TelosysToolsCfg getProjectConfig() {
		TelosysToolsCfgManager cfgManager = new TelosysToolsCfgManager( getProjectFolderAbsolutePath() );
		try {
			return cfgManager.loadTelosysToolsCfg() ;
		} catch (TelosysToolsException e) {
			MsgBox.error("Cannot load Telosys Tools configuration", e);
			return null;
		}
	}
	
	//----------------------------------------------------------------------------------------
	public String getShortFileName() {
		return _iFile.getName();
		//return _fileName ;
	}
	
	//----------------------------------------------------------------------------------------
	/**
	 * Returns the filesystem absolute path of the current file in the editor
	 * @return
	 */
	public String getFileAbsolutePath() {
		return EclipseWksUtil.getAbsolutePath(_iFile);
	}
	//----------------------------------------------------------------------------------------
	protected IFile getFile() {
		return _iFile ;
	}
	
	//----------------------------------------------------------------------------------------
	protected void refreshFile(IProgressMonitor monitor) {
		try {
			_iFile.refreshLocal(IResource.DEPTH_ZERO, monitor);
		} catch (CoreException e) {
			MsgBox.error("Cannot refresh file after save", e );
		}
	}
	
	//----------------------------------------------------------------------------------------
	public void setPopulateInProgress(boolean value) {
		_bPopulateInProgress = value ;
	}
	//----------------------------------------------------------------------------------------
	public boolean isPopulateInProgress() {
		return _bPopulateInProgress ;
	}
	
	//----------------------------------------------------------------------------------------
	@Override
	public boolean isDirty() {
		return _dirty;
	}

	//----------------------------------------------------------------------------------------
	public void setDirty() {
		setDirty(true);
	}
	
	//----------------------------------------------------------------------------------------
	protected void setDirty(boolean flag) {
		if ( _dirty != flag ) {
			_dirty = flag ;
			editorDirtyStateChanged(); // Notify the editor 
		}
	}
}
