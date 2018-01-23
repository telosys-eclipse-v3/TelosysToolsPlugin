package org.telosys.tools.eclipse.plugin.editors.dbconfig;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.commons.TextWidgetLogger;


/**
 * 
 */
/* package */ class DbConfigEditorPage2 extends DbConfigEditorPage 
{

    //-----------------------------------------------------------
//	private DbConfigEditor _editor  = null ;
	
	private Text           _tLogger = null ; 
	
	//private Color  _backgroundColor = null ;
	//-----------------------------------------------------------
	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public DbConfigEditorPage2(FormEditor editor, String id, String title) {
		super(editor, id, title);
		PluginLogger.log(this, "constructor(.., '"+id+"', '"+ title +"')..." );
//		_editor = (DbConfigEditor) editor;
	}
	
	protected Text getLoggerText() 
	{
		PluginLogger.log(this, "getLoggerText(..)..." );
		return _tLogger ;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	// NB : called only when the page is open with clicking on the tab
	protected void createFormContent(IManagedForm managedForm) 
	{
		super.createFormContent(managedForm);
		PluginLogger.log(this, "createFormContent(..)..." );
		
//		Control pageControl = getPartControl();
		
//		Display display = pageControl.getDisplay();
		
//		_backgroundColor = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);				
//		pageControl.setBackground(_backgroundColor ) ;

//		pageControl.setBackground( getBackgroundColor() ) ;
		
		
		ScrolledForm form = managedForm.getForm();
		// Page title 
		// form.setText( _repEditor.getDatabaseTitle() );
		
		Composite body = form.getBody();
		
		RowLayout rowLayout = new RowLayout ();
		rowLayout.type = SWT.VERTICAL;
		body.setLayout (rowLayout);

		
		Composite panel = new Composite(body, SWT.NONE);
		GridLayout gridLayout = new GridLayout ();
		gridLayout.numColumns = 3;
		panel.setLayout (gridLayout);

		GridData gdLabel = new GridData();
		gdLabel.widthHint = 300;
		Label label0 = new Label (panel, SWT.NONE);
		label0.setText ("Telosys Tools log viewer : ");
		label0.setLayoutData(gdLabel);

		GridData gd = new GridData();
		gd.widthHint = 140;
		//gd.verticalAlignment = SWT.BEGINNING ;

		Button bRefresh = new Button (panel, SWT.PUSH);
		bRefresh.setText ("Refresh");
		bRefresh.setLayoutData(gd);
		bRefresh.addSelectionListener( new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent arg0)
            {
            	refreshLog();
            }
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        });
		
		
		Button bClear = new Button (panel, SWT.PUSH);
		bClear.setText ("Clear");
		bClear.setLayoutData(gd);
		bClear.addSelectionListener( new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent arg0)
            {
            	clearLog();
            }
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        });

		
		_tLogger = new Text (body, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL );
		RowData data = new RowData (800, 500);
		_tLogger.setLayoutData (data);
		
		refreshLog();
//		TextWidgetLogger logger = _editor.getTextWidgetLogger();
//		if ( logger != null ) {
//			_tLogger.setText( logger.getContent() );
//		}
//		else {
//			_tLogger.setText( "logger is null ! " );
//		}
		
//		//--------------------------------------------------------------
//		button = new Button(group1, SWT.NONE);
//		button.setText("Update repository");
//		button.setBounds(420, 25, 180, 25);
//
//    	button.addSelectionListener( new SelectionListener() 
//    	{
//            public void widgetSelected(SelectionEvent arg0)
//            {
//                actionUpdateRepository();
//            }
//            public void widgetDefaultSelected(SelectionEvent arg0)
//            {
//            }
//        });
//		
		//--------------------------------------------------------------
	}
	
	//----------------------------------------------------------------------------------------------
	private void refreshLog()
	{
		DbConfigEditor editor = getDbConfigEditor();
		if ( editor != null ) {
			TextWidgetLogger logger = editor.getTextWidgetLogger();
			if ( logger != null ) {
				_tLogger.setText( logger.getContent() );
			}
			else {
				_tLogger.setText( "logger is null ! " );
			}
		}
		else
		{
			MsgBox.error("Cannot get editor instance !");
		}
	}
	//----------------------------------------------------------------------------------------------
	private void clearLog()
	{
		DbConfigEditor editor = getDbConfigEditor();
		if ( editor != null ) {
			TextWidgetLogger logger = editor.getTextWidgetLogger();
			if ( logger != null ) {
				logger.clear();
			}
			_tLogger.setText( "" );
		}
		else
		{
			MsgBox.error("Cannot get editor instance !");
		}
	}
	//----------------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
		PluginLogger.log(this, "init(..,..)..." );
		PluginLogger.log(this, "init(..,..) : site id = '" + site.getId() + "'" );
		PluginLogger.log(this, "init(..,..) : input name = '" + input.getName() + "'" );
	}
	
}