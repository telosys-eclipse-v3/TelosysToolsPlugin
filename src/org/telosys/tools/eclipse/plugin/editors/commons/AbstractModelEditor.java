package org.telosys.tools.eclipse.plugin.editors.commons;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;
import org.telosys.tools.eclipse.plugin.commons.ModelUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.generic.model.Model;
import org.telosys.tools.repository.model.RepositoryModel;

/**
 * Abstract ancestor class for a model editor (e.g. database model or DSL model) <br>
 * This kind of editor is designed to edit a model and to generate code from this model <br>
 * 
 * @author Laurent Guerin
 * 
 */
public abstract class AbstractModelEditor extends AbstractStandardEditor
{
	private Model     _model    = null;
	
	private String    _currentBundle = null ; 
	
	private AbstractModelEditorPageForGeneration _codeGenerationPage = null ;
		
	//----------------------------------------------------------------------------------------
	// Abstract method(s)
	//----------------------------------------------------------------------------------------
	protected abstract Model loadModel(File modelFile) ;
	
	protected abstract void saveModel( Model model, File file );
	
	//----------------------------------------------------------------------------------------
    /**
     * Constructor
     */
    public AbstractModelEditor() {
    	super();
    }
    
    protected void setCodeGenerationPage(AbstractModelEditorPageForGeneration codeGenerationPage ) {
    	_codeGenerationPage = codeGenerationPage ;
    }
	//----------------------------------------------------------------------------------------
	/**
	 * Returns the current model managed by the model editor
	 * @return
	 */
	public Model getModel() {
//		if ( _model == null ) {
//			MsgBox.error("Cannot get model, current model is null");
//		}
		return _model ;
	}
	
	//----------------------------------------------------------------------------------------
	private void setModel(Model model) {
		log(this, "setModel(Model)..." );
		_model = model ;
		if ( _codeGenerationPage != null ) {
			// if the page is ready
			_codeGenerationPage.newModelNotification() ;
		}
		else {
			// if page not yet constructed : not an error
			//MsgBox.error("Code generation page is null !");
		}
	}	
	
	//----------------------------------------------------------------------------------------
	/**
	 * Returns the current 'RepositoryModel' managed by the model editor <br>
	 * ( the current model converted to 'RepositoryModel' )
	 * @return
	 */
	public RepositoryModel getRepositoryModel() {
		if ( _model != null ) {
			try {
				return ModelUtil.toRepositoryModel( _model );
			} catch (Exception e) {
				MsgBox.error("Cannot convert current model to Database Model", e);
			}
		}
		else {
			MsgBox.error("Cannot get Database Model, current model is null");
		}
		return null ;
	}
	
	//----------------------------------------------------------------------------------------
	public void setCurrentBundleName(String bundleName) {
		_currentBundle = bundleName ;
	}
	
	//----------------------------------------------------------------------------------------
	public String getCurrentBundleName() {
		return _currentBundle ;
	}

	//----------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
    @Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException 
	{
		log(this, "init(..,..)..." );
		super.init(site, input);
//		_model = loadModel( super.getFile() );
//		if ( _model == null ) {
//			MsgBox.error("Model loaded is null !");
//		}
	}
    
	//----------------------------------------------------------------------------------------
//	private Model loadModel( IFile iFile ) {
//		File modelFile = EclipseWksUtil.toFile(iFile);
//		//log("loadModel(" + modelFile + ")");
//		GenericModelLoader genericModelLoader = new GenericModelLoader( getProjectConfig() ) ;
//		try {
//			Model model = genericModelLoader.loadModel(modelFile);
//			//log("loadModel() : done. Model name = " + model.getName() + " - " + model.getEntities().size() + " entities");
//			return model;
//		} catch (Exception ex) {
//			MsgBox.error("Cannot load model ", ex);
//			return null ;
//		}
//	}

	public File getModelFile() {
		return EclipseWksUtil.toFile( getFile() );
	}

	//----------------------------------------------------------------------------------------
    public void loadModel() {
		log(this, "loadModel()..." );
    	File file = getModelFile();
		Model model = loadModel(file);
		setModel(model);
    }

	//----------------------------------------------------------------------------------------
	@Override
	public void doSave(IProgressMonitor monitor) {
		log("doSave()..." );

		monitor.beginTask( "Saving the model...", IProgressMonitor.UNKNOWN );
		
		File modelFile = getModelFile();
		
		// Call the specialized method implemented in descendant class
		saveModel(getModel(), modelFile);
		
		super.setDirty(false);
		
		super.refreshFile(monitor);
		
		monitor.done();
	}

	//----------------------------------------------------------------------------------------
	@Override
	public void doSaveAs() {
		MsgBox.error("'Save as' is not allowed");
	}

	//----------------------------------------------------------------------------------------
	@Override
	public boolean isSaveAsAllowed() { // Allow the "Save as" ?
		log("isSaveAsAllowed()..." );
		return false ; // "Save as" not allowed
	}
}