package org.telosys.tools.eclipse.plugin.editors.dsl.model;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.telosys.tools.dsl.parser.model.DomainModelInfo;
import org.telosys.tools.eclipse.plugin.commons.Util;
import org.telosys.tools.eclipse.plugin.editors.commons.AbstractModelEditorPage;

/**
 * First page of the editor : Model attributes mapping and foreign keys <br>
 *  
 */
/* package */  class ModelEditorPageModelInfo extends AbstractModelEditorPage 
{
	private boolean  _bPopulateInProgress = false ;
	
	private Text     _tFileName ;
	private Text     _tName ;
	private Text     _tVersion ;
	private Text     _tDescription ;

	//----------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param editor
	 * @param id
	 * @param title 
	 */
	public ModelEditorPageModelInfo(FormEditor editor, String id, String title ) {
		super(editor, id, title);
		log(this, "constructor(.., '"+id+"', '"+ title +"')..." );
	}
	
	//----------------------------------------------------------------------------------------------
	@Override
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
		log(this, "init(..,..)..." );
		log(this, "init(..,..) : site id = '" + site.getId() 
				+ "'  input name = '" + input.getName() + "'");
	}	
	//----------------------------------------------------------------------------------------------
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		
		log(this, "createFormContent(..) : Model Info Page" );
		
		//--- Set a LAYOUT to the BODY
		GridLayout bodyLayout = new GridLayout();	
		bodyLayout.numColumns = 2 ;
		bodyLayout.makeColumnsEqualWidth = false ;
		
		Composite scrolledFormBody = initAndGetFormBody(managedForm, bodyLayout);
		
		//---------------------------------------------------------------
		// The page title
		//---------------------------------------------------------------
		Label labelTitle = Util.setPageTitle(scrolledFormBody, this.getTitle() ) ; // Title defined in the constructor
		GridData gdTitle = new GridData(GridData.FILL_HORIZONTAL);
		gdTitle.horizontalSpan = 2;		
		labelTitle.setLayoutData(gdTitle);
		
		//---------------------------------------------------------------
		// Lines : label + text field 
		//---------------------------------------------------------------
		addRow(scrolledFormBody, "", "") ;
		_tFileName    = addLabelAndText(scrolledFormBody, "File : ") ;
//		_tFileName.setEnabled(false);
		_tFileName.setEditable(false);
		
		_tName        = addLabelAndText(scrolledFormBody, "Name : ") ;
		_tVersion     = addLabelAndText(scrolledFormBody, "Version : ") ;
		_tDescription = addLabelAndTextMulti(scrolledFormBody, "Description : ") ;
		
		populateFields() ;
	}
	//----------------------------------------------------------------------------------------------
	protected boolean isPopulateInProgress() {
		return _bPopulateInProgress ;
	}
	
	//----------------------------------------------------------------------------------------------
	/**
	 * Update the given ModelInformation from the text fields
	 * @param modelInfo
	 */
	protected void updateModelInformation( DomainModelInfo modelInfo ) {
		modelInfo.setName(_tName.getText());
		modelInfo.setVersion(_tVersion.getText());
		modelInfo.setDescription(_tDescription.getText());
	}
	//----------------------------------------------------------------------------------------------
	private void populateFields() {
		log(this, "populateFields()");
		
		getStandardEditor().setPopulateInProgress(true);
		
		String modelFileAbsolutePath = this.getModelEditor().getFileAbsolutePath();
		_tFileName.setText( modelFileAbsolutePath );

//		File modelFile = new File(modelFileAbsolutePath);
//		ModelLoader modelLoader = new ModelLoader();
//		DomainModelInfo modelInfo = modelLoader.loadModelInformation(modelFile);
		DomainModelInfo modelInfo = ((ModelEditor)this.getModelEditor()).getDomainModelInfo();
		if ( modelInfo != null ) {
			_tName.setText(modelInfo.getName());
			_tVersion.setText(modelInfo.getVersion());
			_tDescription.setText(modelInfo.getDescription());
		}
		
		getStandardEditor().setPopulateInProgress(false);
	}
	//----------------------------------------------------------------------------------------------
	private Text addLabelAndText(Composite c, String labelText ) {
		Label label1 = new Label( c, SWT.LEFT );
		label1.setText(labelText) ;

		// Standard text field 
		//Text text = new Text( c, SWT.LEFT );SWT.BORDER
		Text text = new Text( c, SWT.BORDER );
		text.addModifyListener( new TextFieldModifyListener(getStandardEditor()) );

		GridData gd = new GridData();
		gd.widthHint = 400;
		text.setLayoutData ( gd );
		return text ;
	}
	//----------------------------------------------------------------------------------------------
	private Text addLabelAndTextMulti(Composite c, String labelText ) {
		Label label = new Label( c, SWT.LEFT );
		label.setText(labelText) ;
		GridData labelGridData = new GridData(); 
		labelGridData.verticalAlignment = SWT.BEGINNING; 
		labelGridData.grabExcessVerticalSpace = true; 
		label.setLayoutData(labelGridData);
		
		// Multi-line text field 
		Text text = new Text (c, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL );
		
		text.addModifyListener( new TextFieldModifyListener(getStandardEditor()) );
		
		new TextFieldModifyListener(getStandardEditor());
		GridData textGridData = new GridData(400, 200); 
		textGridData.verticalAlignment = SWT.BEGINNING; 
		textGridData.grabExcessVerticalSpace = true; 
		text.setLayoutData ( textGridData );
		return text ;
	}
	//----------------------------------------------------------------------------------------------
	private void addRow(Composite c, String labelText1, String labelText2) {
		Label label = new Label( c, SWT.LEFT );
		label.setText(labelText1) ;
		
		label = new Label( c, SWT.LEFT );
		label.setText(labelText2) ;
	}
	//----------------------------------------------------------------------------------------------
}