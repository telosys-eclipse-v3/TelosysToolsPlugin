package org.telosys.tools.eclipse.plugin.editors.dbrep;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.telosys.tools.commons.DateUtil;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.commons.Util;
import org.telosys.tools.eclipse.plugin.editors.commons.AbstractModelEditorPage;
import org.telosys.tools.repository.model.RepositoryModel;

/**
 * Page 3 of the editor <br>
 * 
 * Shows the project configuration 
 * 
 */
///* package */ class RepositoryEditorPage4 extends RepositoryEditorPage 
/* package */ class RepositoryEditorPageModelInfo extends AbstractModelEditorPage 
{

	//--------------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param editor
	 * @param id
	 * @param title
	 */
	public RepositoryEditorPageModelInfo(FormEditor editor, String id, String title) {
		super(editor, id, title);
		PluginLogger.log(this, "constructor(.., '"+id+"', '"+ title +"')..." );		
	}

	//----------------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
		PluginLogger.log(this, "init(..,..) : site id = '" + site.getId() + "'" );
		PluginLogger.log(this, "init(..,..) : input name = '" + input.getName() + "'" );
	}

	//--------------------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		
//		TelosysToolsCfg telosysToolsCfg = getProjectConfig(); // v 3.0.0
		
		ScrolledForm form = managedForm.getForm();
		
		Composite body = form.getBody(); 
		// body.getClass() --> org.eclipse.ui.forms.widgets.LayoutComposite
		// No Layout for the body at this moment
		
		//--- Set a LAYOUT to the BODY
		GridLayout bodyLayout = new GridLayout();	
		bodyLayout.numColumns = 2 ;
		bodyLayout.makeColumnsEqualWidth = false ;
		
		body.setLayout( bodyLayout );
		
		//---------------------------------------------------------------
		// Line 0 - Columns 1 & 2 (span) : The page title
		//---------------------------------------------------------------
		GridData gdTitle = new GridData(GridData.FILL_HORIZONTAL);
		gdTitle.horizontalSpan = 2;		
		Label labelTitle = Util.setPageTitle(body, this.getTitle() ) ; // Title defined in the constructor
		labelTitle.setLayoutData(gdTitle);
		
		
		//TelosysToolsCfg telosysToolsCfg = config.getTelosysToolsCfg();
		
		addConfigRow(body, "", "" );
			
		RepositoryModel repositoryModel = getRepositoryModel();
		addConfigRow(body, "Model name :", repositoryModel.getName() );
		addConfigRow(body, "Model version :", repositoryModel.getVersion() );
		//repositoryModel.getDescription();

		addConfigRow(body, "", "" );
		
		addConfigRow(body, "Model generation :",  formatDate(repositoryModel.getGenerationDate()) );
		addConfigRow(body, "Model last update :", formatDate(repositoryModel.getLastUpdateDate()) );
		
		addConfigRow(body, "", "" );

		addConfigRow(body, "Database used to generate the model", "" );
		addConfigRow(body, ". database ID :", ""+repositoryModel.getDatabaseId() );
		addConfigRow(body, ". database name :", ""+repositoryModel.getDatabaseName() );
		addConfigRow(body, ". database product name :", ""+repositoryModel.getDatabaseProductName() );
	}
	
	//----------------------------------------------------------------------------------------------
	final static String DATE_FORMAT = "MMMM dd yyyy - HH:mm:ss";
	private String formatDate (java.util.Date date) {
		if ( date != null ) {
			return DateUtil.format(date, DATE_FORMAT );
		}
		else {
			return "" ;
		}
	}
	//----------------------------------------------------------------------------------------------
	private void addConfigRow(Composite c, String s1, String s2)
	{
		Label label1 = new Label( c, SWT.LEFT );
		label1.setText(s1) ;

		Label label2 = new Label( c, SWT.LEFT );
		label2.setText(s2) ;
	}

}