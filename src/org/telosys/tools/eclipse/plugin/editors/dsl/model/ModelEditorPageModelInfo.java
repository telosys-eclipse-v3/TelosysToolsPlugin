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
 * DSL Model Editor Page : Model Info  <br>
 *  
 */

/* package */  class ModelEditorPageModelInfo extends AbstractModelEditorPage 
{
	private boolean  _bPopulateInProgress = false ;
	
	private Text     _tFileName ;
//	private Text     _tName ; // removed in v 3.3.0
//	private Text     _tVersion ; // removed in v 3.3.0
	private Text     _tTitle ; // added in v 3.3.0
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
		log(this, "init(..,..) :  input name = '" + input.getName() + "'");
	}

	//----------------------------------------------------------------------------------------------
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		
		log(this, "createFormContent(..) : Model Info Page" );

		setBodyBackgroundColor(); // OK for Eclipse 4.X compatibility

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
		
//		_tName        = addLabelAndText(scrolledFormBody, "Name : ") ; // removed in v 3.3.0
//		_tVersion     = addLabelAndText(scrolledFormBody, "Version : ") ; // removed in v 3.3.0
		_tTitle       = addLabelAndText(scrolledFormBody, "Title : ") ; // added in v 3.3.0
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
		// TODO : CREATE A TEXT EDITOR FOR MODEL
		// v 3.3.0
//		modelInfo.setName(_tName.getText());
//		modelInfo.setVersion(_tVersion.getText());
		modelInfo.setTitle(_tTitle.getText()); // added in v 3.3.0
		modelInfo.setDescription(_tDescription.getText());
	}
	//----------------------------------------------------------------------------------------------
	private void populateFields() {
		log(this, "populateFields()");
		
		getStandardEditor().setPopulateInProgress(true);
		
		String modelFileAbsolutePath = this.getModelEditor().getFileAbsolutePath();
		_tFileName.setText( modelFileAbsolutePath );

		// TODO : CREATE A TEXT EDITOR FOR MODEL
		// v 3.3.0
		DomainModelInfo modelInfo = ((ModelEditor)this.getModelEditor()).getDomainModelInfo();
		if ( modelInfo != null ) {
//			_tName.setText(modelInfo.getName()); // removed in v 3.3.0
//			_tVersion.setText(modelInfo.getVersion()); // removed in v 3.3.0
			_tTitle.setText(modelInfo.getTitle()); // added in v 3.3.0
			_tDescription.setText(modelInfo.getDescription());
		}		
		getStandardEditor().setPopulateInProgress(false);
	}
	//----------------------------------------------------------------------------------------------
	private Text addLabelAndText(Composite c, String labelText ) {
		Label label1 = new Label( c, SWT.LEFT );
		label1.setText(labelText) ;

		// Standard text field 
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