package org.telosys.tools.eclipse.plugin.editors.dbrep;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.commons.Util;
import org.telosys.tools.eclipse.plugin.editors.commons.AbstractModelEditorPage;
import org.telosys.tools.repository.LinksManager;
import org.telosys.tools.repository.model.LinkInDbModel;
import org.telosys.tools.repository.model.LinksCriteria;
import org.telosys.tools.repository.model.RepositoryModel;
import org.telosys.tools.repository.rules.RepositoryRulesProvider;

/**
 * Page 2 of the editor : Links management  <br>
 */
///* package */ class RepositoryEditorPage2 extends RepositoryEditorPage 
/* package */ class RepositoryEditorPageModelLinks extends AbstractModelEditorPage 
{

	private Button ckFilterOwningSide  ;
	private Button ckFilterInverseSide ;

	private Button ckFilterManyToOne  ;
	private Button ckFilterOneToMany  ;
	private Button ckFilterManyToMany ;
	private Button ckFilterOneToOne   ;
	
	private LinksList    linksView ;
//	private LinksManager linksManager ; // removed in v 3.0.0
	
	private Label     linksCount ;
	
	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public RepositoryEditorPageModelLinks(FormEditor editor, String id, String title) {
		super(editor, id, title);
//		linksManager = new LinksManager( getRepositoryModel() ); // v 3.0.0
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
				
		// What do we have here ?
		// * pageControl (Composite)
		//  . class  : org.eclipse.ui.forms.widgets.ScrolledForm ( see API JavaDoc )
		//  . layout : org.eclipse.swt.custom.ScrolledCompositeLayout
		// * body 
		//  . class  : org.eclipse.ui.forms.widgets.LayoutComposite ( no API doc ! )
		//  . layout : none
		//
		/* Example from API doc :
		  ScrolledForm form = toolkit.createScrolledForm(parent);
		  form.setText("Sample form");
		  form.getBody().setLayout(new GridLayout());
		  toolkit.createButton(form.getBody(), "Checkbox", SWT.CHECK);
		*/
		try {
			
			ScrolledForm form = managedForm.getForm();
			
			Composite scrolledFormBody = form.getBody(); 
			// No Layout for the body at this moment
			
			//--- Set the Form Body Layout = Grid with only 1 column
			GridLayout bodyLayout = new GridLayout(1, false); 
			//GridData gd = new GridData(GridData.);
			
			// marginWidth specifies the number of pixels of horizontal margin 
			// that will be placed along the left and right edges of the layout. The default value is 5.
			//bodyLayout.marginWidth = 20 ;
//			bodyLayout.marginWidth = RepositoryEditor.LAYOUT_MARGIN_WIDTH ;
			bodyLayout.marginWidth = LAYOUT_MARGIN_WIDTH ;
			
			scrolledFormBody.setLayout( bodyLayout );
			//scrolledFormBody.setSize(200,200); // Sans effet
			
			//---------------------------------------------------------------
			// Line 1 - Column 1 : The page title
			//---------------------------------------------------------------
			//Util.setPageTitle(scrolledFormBody, "Links between entities"  ) ;
			Util.setPageTitle(scrolledFormBody, this.getTitle() ) ; // Title defined in the constructor
			
			//---------------------------------------------------------------
			// Line 2 - Column 1 : The filter criteria 
			//---------------------------------------------------------------
			createFilter(scrolledFormBody);
			
			//---------------------------------------------------------------
			// Line 3 - Column 1 : The list of links
			//---------------------------------------------------------------
			//--- Set the composite height
			GridData gd = new GridData (GridData.FILL_HORIZONTAL);
			gd.heightHint = 400 ;
			gd.widthHint  = 800;

//			linksView = new LinksList(scrolledFormBody, gd, linksManager, this );
			linksView = new LinksList(scrolledFormBody, gd, this ); // v 3.0.0
	
			//---------------------------------------------------------------
			// Line 4 - Column 1 : Number of links
			//---------------------------------------------------------------
			linksCount = new Label(scrolledFormBody, SWT.LEFT);
			linksCount.setLayoutData( new GridData(200, 26) ) ;
	
			//---------------------------------------------------------------
			// Line 5 - Column 1 : Push button
			//---------------------------------------------------------------
			createButtons(scrolledFormBody);
		
			//---------------------------------------------------------------
			// Populate the links view
			//---------------------------------------------------------------
			//LinkedList<Link> links = linksManager.getAllLinks();
//			LinkedList<LinkInDbModel> links = linksManager.getAllLinks(); // v 3.0.0
//			List<LinkInDbModel> links = linksManager.getAllLinks(); // v 3.0.0
			List<LinkInDbModel> links = getRepositoryModel().getAllLinks(); // v 3.0.0
			linksView.populate( links );
			
			refreshCount();
			
		}
		catch ( Throwable tex ) {
			MsgBox.error("Exception in createFormContent()", tex);
		}
	}

//	private void createLinks(Composite scrolledFormBody)
//	{
//		LinkedList links = getAllLinks();
//		//--- Set the composite height
//		GridData gd = new GridData (GridData.FILL_HORIZONTAL);
//		gd.heightHint = 400 ;
//		gd.widthHint  = 800;
//
//		linksView = new LinksList(scrolledFormBody, gd, null );
//		int n = linksView.populate( links );
//		linksCount.setText( "" + n + "/" + links.size() );
//	}
	
	private Button createCheckBox(Composite parent, String text, GridData gd ) {
		Button b = new Button(parent, SWT.CHECK);
        b.setText(text);
        b.setLayoutData( gd ) ;
        b.setSelection(true);
        return b;
	}
	
	protected void refreshCount() {
//		linksCount.setText( "" + linksView.getSize() + "/" + linksManager.countAllLinks() );
		linksCount.setText( "" + linksView.getSize() + "/" + getRepositoryModel().getNumberOfLinks() );
	}
	
	protected void applyFilterCriteria() {
		log("applyFilterCriteria()");

		LinksCriteria criteria = new LinksCriteria();
		criteria.setOwningSide ( ckFilterOwningSide.getSelection() );
		criteria.setInverseSide( ckFilterInverseSide.getSelection() );
		criteria.setTypeManyToMany( ckFilterManyToMany.getSelection() );
		criteria.setTypeManyToOne( ckFilterManyToOne.getSelection() );
		criteria.setTypeOneToMany( ckFilterOneToMany.getSelection() );
		criteria.setTypeOneToOne( ckFilterOneToOne.getSelection() );
		
		log("applyFilterCriteria() : " + criteria );
		
		//LinkedList<Link> links = linksManager.getLinks(criteria);
//		LinkedList<LinkInDbModel> links = linksManager.getLinks(criteria); // v 3.0.0
		LinkedList<LinkInDbModel> links = getRepositoryModel().getLinks(criteria); // v 3.0.0
		linksView.repopulate( links );
		refreshCount();
	}
	
	private void setAllFilterChoices(boolean b) {
    	ckFilterOwningSide.setSelection(b);
    	ckFilterInverseSide.setSelection(b);
    	ckFilterManyToOne.setSelection(b);
    	ckFilterOneToMany.setSelection(b);
    	ckFilterManyToMany.setSelection(b);
    	ckFilterOneToOne.setSelection(b);
	}
	
	private void selectAllFilterChoices() {
		setAllFilterChoices(true);
	}
	private void deselectAllFilterChoices()	{
		setAllFilterChoices(false);
	}
	
	private void createFilter(Composite scrolledFormBody) {
		GridLayout gridLayout = new GridLayout(6, false); 
		gridLayout.verticalSpacing = 0;
		//Composite composite = new Composite(scrolledFormBody, SWT.NONE | SWT.BORDER );	
		Composite composite = new Composite(scrolledFormBody, SWT.NONE );	
		composite.setLayout( gridLayout ) ; 
		
		GridData gd = new GridData(140, 26);
		GridData gdButton = new GridData(100, 26);

		//----- Line 1
		{
			Label label = new Label(composite, SWT.LEFT);
			label.setText("Filter : ");
			label.setLayoutData( gd ) ;
		}
		
		ckFilterOwningSide  = createCheckBox(composite, "Owning side", gd);
		ckFilterInverseSide = createCheckBox(composite, "Inverse side", gd);

		ckFilterOwningSide.addSelectionListener(new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent arg0)
            {
//            	if ( ckFilterOwningSide.getSelection() == false ) {
//            		ckFilterInverseSide.setSelection(true);
//            	}
            }
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        });
		ckFilterInverseSide.addSelectionListener(new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent arg0)
            {
//            	if ( ckFilterInverseSide.getSelection() == false ) {
//            		ckFilterOwningSide.setSelection(true);
//            	}
            }
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        });
		
		{
			Label l = new Label(composite, SWT.LEFT);
	        l.setLayoutData( gd ) ;
		}
		{
			Label l = new Label(composite, SWT.LEFT);
	        l.setLayoutData( gd ) ;
		}

		{
			Button b = new Button(composite, SWT.PUSH);
	        b.setText("Select All");
	        b.setLayoutData( gdButton ) ;
	        b.addSelectionListener( new SelectionListener() 
		    	{
		            public void widgetSelected(SelectionEvent arg0)
		            {
		            	selectAllFilterChoices();
		            }
		            public void widgetDefaultSelected(SelectionEvent arg0)
		            {
		            }
		        });
		}
		
		//----- Line 2
//		{
//			Label label = new Label(composite, SWT.LEFT);
//			label.setLayoutData( gd ) ;
//			
//		}
		{
			Button b = new Button(composite, SWT.PUSH);
	        b.setText("Apply");
	        b.setLayoutData( gdButton ) ;
	        b.addSelectionListener( new SelectionListener() 
		    	{
		            public void widgetSelected(SelectionEvent arg0)
		            {
		            	applyFilterCriteria();
		            }
		            public void widgetDefaultSelected(SelectionEvent arg0)
		            {
		            }
		        });
		}

		ckFilterManyToOne  = createCheckBox(composite, "Many To One", gd);
		ckFilterOneToMany  = createCheckBox(composite, "One To Many", gd);
		ckFilterManyToMany = createCheckBox(composite, "Many To Many", gd);
		ckFilterOneToOne   = createCheckBox(composite, "One To One", gd);
		
		{
			Button b = new Button(composite, SWT.PUSH);
	        b.setText("Deselect All");
	        b.setLayoutData( gdButton ) ;
	        b.addSelectionListener( new SelectionListener() 
		    	{
		            public void widgetSelected(SelectionEvent arg0)
		            {
		            	deselectAllFilterChoices();
		            }
		            public void widgetDefaultSelected(SelectionEvent arg0)
		            {
		            }
		        });
		}
	}
	
	private void createButtons(Composite scrolledFormBody) {
		Composite composite = new Composite(scrolledFormBody, SWT.NONE);	
		RowLayout rowLayout = new RowLayout () ;
		rowLayout.type = SWT.HORIZONTAL ;
		rowLayout.spacing = 10 ;
		rowLayout.marginLeft = 0 ;
		composite.setLayout( rowLayout ) ; // Horizontal row layout

		Button buttonRemoveLinks = new Button(composite, SWT.NONE);
		buttonRemoveLinks.setText("  Remove all links  ");
		buttonRemoveLinks.addSelectionListener( new SelectionListener() 
	    	{
	            public void widgetSelected(SelectionEvent arg0)
	            {
	            	String msg = 
          		  		 "Do you realy want to remove all the links ?" 
          		  		;
    				if ( MsgBox.confirm(msg) ) 
    				{
    					removeAllLinks();
    					setDirty();
    					selectAllFilterChoices();
    					applyFilterCriteria();
    					refreshCount();
    					
    					//MsgBox.info("" + n + " link(s) generated.");
    				}
	            }
	            public void widgetDefaultSelected(SelectionEvent arg0)
	            {
	            }
	        });

        Button buttonGenerate = new Button(composite, SWT.NONE);
        buttonGenerate.setText("  Generate links from foreign keys  ");
        buttonGenerate.addSelectionListener( new SelectionListener() 
	    	{
	            public void widgetSelected(SelectionEvent arg0)
	            {
	            	String msg = 
          		  		 "\n Do you realy want to generate links for each Foreign Key ?" 
						+ "\n"
						+ "\n Generated links will replace existing links if any." 
						+ "\n Both sides (owning side and inverse side) will be generated." 
          		  		;
    				if ( MsgBox.confirm(msg) ) 
    				{
    					int n = generateLinks();
    					setDirty();
    					selectAllFilterChoices();
    					applyFilterCriteria();
    					refreshCount();
    					
    					MsgBox.info("" + n + " link(s) generated.");
    				}
	            }
	            public void widgetDefaultSelected(SelectionEvent arg0)
	            {
	            }
	        });
		
		
		Button buttonAddLink = new Button(composite, SWT.NONE);
		buttonAddLink.setText("  Add new link  ");
		buttonAddLink.addSelectionListener( new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent arg0)
            {
				MsgBox.info("Not yet implemented.");
//            	if ( confirmBulkGeneration() )
//        		{
//        	    	Shell shell = Util.cursorWait();
//        	    	launchBulkGeneration();
//        			Util.cursorArrow(shell);
//        		}                
            }
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        });
	}
	
//	//----------------------------------------------------------------------------------------------
//	private void addListRow(Composite c, String s1, String s2, String s3)
//	{
//			
//		Label label1 = new Label( c, SWT.LEFT );
//		label1.setText(s1) ;
//
//		Label label2 = new Label( c, SWT.LEFT );
//		label2.setImage(PluginImages.getImage(PluginImages.LINK_MANY_TO_ONE));
////		label2.setText(s2) ;
//
//		Label label3 = new Label( c, SWT.LEFT );
//		label3.setText(s3) ;
//		
////		CLabel label3 = new CLabel(c, SWT.LEFT);  
////		label3.setText(s3);
////		label3.setT
////		label2.setImage( WizardImages.getImage(WizardImages.FOREIGNKEY) );
//
//
//		Button b1 = new Button(c, SWT.NONE);
//        b1.setText("Edit...");
//		
//		Button b2 = new Button(c, SWT.NONE);
//        b2.setText("Remove");
//		
//	}
	//----------------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
		PluginLogger.log(this, "init(..,..) : site id = '" + site.getId() + "'" );
		PluginLogger.log(this, "init(..,..) : input name = '" + input.getName() + "'" );
	}

	//----------------------------------------------------------------------------------------------
	private void removeAllLinks() {
		RepositoryModel repositoryModel = getRepositoryModel();
		repositoryModel.removeAllLinks();
	}
	//----------------------------------------------------------------------------------------------
	private int generateLinks() {
		int n = 0 ;
		RepositoryModel repositoryModel = getRepositoryModel();
		
		//LinksGenerator linksGenerator = new LinksGenerator(getLogger());
		//LinksGenerator linksGenerator = new LinksGenerator(RepositoryRulesProvider.getRepositoryRules(), getLogger()); // v 2.1.1
		LinksManager linksGenerator = new LinksManager(RepositoryRulesProvider.getRepositoryRules() ); // v 3.0.0

		try {
			
			n = linksGenerator.generateAllLinks(repositoryModel);
			
		} catch (TelosysToolsException e) {
			MsgBox.error("Error /links generation - TelosysToolsException", e);
		}
		return n ;
	}
}