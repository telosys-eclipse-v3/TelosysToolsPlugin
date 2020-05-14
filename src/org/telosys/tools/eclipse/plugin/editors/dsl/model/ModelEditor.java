package org.telosys.tools.eclipse.plugin.editors.dsl.model;

import java.io.File;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.telosys.tools.dsl.DslModelManager;
import org.telosys.tools.dsl.parser.model.DomainModelInfo;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginImages;
import org.telosys.tools.eclipse.plugin.editors.commons.AbstractModelEditor;
import org.telosys.tools.eclipse.plugin.editors.dsl.commons.ModelLoadingResult;
import org.telosys.tools.eclipse.plugin.editors.dsl.commons.ModelManager;
import org.telosys.tools.generic.model.Model;

/**
 * Main entry point for the DSL model editor (for code generation) <br>
 * 
 */
public class ModelEditor extends AbstractModelEditor {
	
	private Image _imageWithoutError = null ;
	
	// Editor pages :
    private ModelEditorPageModelEntities  _modelEntitiesPage ;
	private ModelEditorPageModelInfo      _modelInformationPage ;
	private ModelEditorPageCodeGeneration _codeGenerationPage ;
	
	private ModelLoadingResult _modelLoadingResult = null;
	
	//----------------------------------------------------------------------------------------
    /**
     * Constructor
     */
    public ModelEditor() {
    	super();
    }
    
	public ModelLoadingResult getModelLoadingResult() {
		return _modelLoadingResult ;
	}

    public DomainModelInfo getDomainModelInfo() {
		//return _modelInfo ;
		if ( _modelLoadingResult != null ) {
			return _modelLoadingResult.getModelInfo();
		}
		return null ;
	}
	
    
	//========================================================================================
	// Editor plugin startup ( for each file to edit ) :
	// Step 1 : init()
	// Step 2 : addPages()
	//========================================================================================
	public void closeEditor(boolean save) {
		getSite().getPage().closeEditor(ModelEditor.this, save);
	}
	
	//----------------------------------------------------------------------------------------
    @Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		log(this, "--- DSL MODEL EDITOR INIT " );
		log(this, "init(..,..) : site id = '" + site.getId() + "'" );
		log(this, "init(..,..) : input name = '" + input.getName() + "'" );
		
		ImageDescriptor imageDescriptor = input.getImageDescriptor();
		if ( imageDescriptor != null ) {
			_imageWithoutError = imageDescriptor.createImage();
		}
		loadModel();
	}

    @Override
    public void dispose() {
        super.dispose();
        _imageWithoutError.dispose();
    }
    
	//----------------------------------------------------------------------------------------
    @Override
	protected void addPages() {
		log(this, "addPages()..." );

		_modelEntitiesPage = 
			new ModelEditorPageModelEntities(this, "ModelEditorPage1", " Model entities " );
		
		_modelInformationPage = 
			new ModelEditorPageModelInfo(this, "ModelEditorPage2", " Model information " );
		
		_codeGenerationPage = 
			new ModelEditorPageCodeGeneration(this, "ModelEditorPage3", " Code generation " );
		
		try {
			addPage(_modelEntitiesPage);
			addPage(_modelInformationPage);
			addPage(_codeGenerationPage);
		} catch (PartInitException e) {
			MsgBox.error("RepositoryEditor : addPage(page) throws PartInitException ", e);
		}		
		setCodeGenerationPage(_codeGenerationPage);
		
		log(this, "addPages() : DONE" );
	}

    //----------------------------------------------------------------------------------------
    @Override // implements super class abstract method
    protected Model loadModel(File modelFile) { // called by super.loadModel()
    	// Try to load the DSL model
    	_modelLoadingResult = ModelManager.load(modelFile);
    	return _modelLoadingResult.getModel(); 
    }
    
    public void updateEditor(ModelLoadingResult r) {

    	// Set current model loading result
    	_modelLoadingResult = r ;
    	
    	// Set editor title image : errors or not
		if ( r.getEntitiesErrors() != null && ! r.getEntitiesErrors().isEmpty() ) {
			Image errorImage = PluginImages.getImage(PluginImages.ERROR);
			log(this, "refresh() : setTitleImage(errorImage) " );
			setTitleImage(errorImage);
		} else {
			setTitleImage(_imageWithoutError);
		}

		// Refresh entities list in first page
		_modelEntitiesPage.populateEntities(); 

		// set the new model (automatically refresh the entities in "Code generation" page)
		setModel(r.getModel());
    }
    
    //----------------------------------------------------------------------------------------
    @Override // implements super class abstract method
    protected void saveModel( Model model, File modelFile ) {
    	if ( _modelInformationPage != null ) {
    		DomainModelInfo _modelInfo = getDomainModelInfo();
        	if  ( _modelInfo != null ) {
            	_modelInformationPage.updateModelInformation(_modelInfo);
        		DslModelManager modelManager = new DslModelManager();
        		modelManager.saveModelInformation(modelFile, _modelInfo);
        	}
        	else {
        		MsgBox.error("ModelInfo is null in the editor !");
        	}
    	}
    	else {
    		MsgBox.error("_modelInformationPage is null in the editor !");
    	}
    }
    //----------------------------------------------------------------------------------------
    /**
     * Refresh the model status in the model editor <br>
     * - reload the model <br>
     * - populate the entities lists (with errors messages if any ) <br>
     * - update the editor image according with the current status (errors or not) <br>
     * 
     */
    public void refresh() {
		log(this, "refresh()..." );
		this.loadModel(); // call super.loadModel() --> call this.loadModel(File) 
    	// Update editor views with the result (model OK or errors)
    	updateEditor(_modelLoadingResult);
    }
}