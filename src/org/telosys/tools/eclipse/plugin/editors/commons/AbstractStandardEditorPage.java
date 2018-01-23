package org.telosys.tools.eclipse.plugin.editors.commons;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.logger.ConsoleLogger;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginColors;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

public abstract class AbstractStandardEditorPage extends FormPage {

	private final AbstractStandardEditor  _standardEditor ; // Ref on the Editor this page belongs to
	
	private final TelosysToolsLogger      _logger ;
	
	private final Color                   _backgroundColor = PluginColors.widgetBackground();

	//----------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param editor
	 * @param id
	 * @param title
	 */
	public AbstractStandardEditorPage(FormEditor editor, String id, String title ) {
		super(editor, id, title);
		PluginLogger.log(this, "constructor(.., '"+id+"', '"+ title +"')..." );
		//super(editor, id, null); // ERROR if title is null
		if ( editor == null ) {
			MsgBox.error("FormEditor is null");
		}
		if ( editor instanceof AbstractStandardEditor ) {
			_standardEditor = (AbstractStandardEditor) editor;
		}
		else {
			_standardEditor = null ;
			MsgBox.error("FormEditor is not an instance of StandardEditor");
		}
		
		//--- Init the logger
		TelosysToolsLogger editorLogger = _standardEditor.getLogger();		
		if ( editorLogger != null ) {
			_logger = editorLogger;
		}
		else {
			_logger = new ConsoleLogger();
		}
	}
	
	//----------------------------------------------------------------------------------------------
	@Override
	protected void createFormContent(IManagedForm managedForm) 
	{
		super.createFormContent(managedForm);
		log(this, "createFormContent(..)..." );
		Control pageControl = getPartControl();
		
		if ( pageControl == null ) {
			MsgBox.error("Page control is null ! Cannot create form content.");
			return;
		}
		
		setPageBackgroundColor(pageControl) ;
		
//		if ( pageControl instanceof Composite ) {
//			log(this, "- pageControl is a Composite, class = " + pageControl.getClass() );
//			Layout layout = ((Composite)pageControl).getLayout();
//			log(this, "- pageControl layout class = " + layout.getClass() );
//			// layout = org.eclipse.swt.custom.ScrolledCompositeLayout
//		}
//		else {
//			log(this, "- pageControl() is NOT a Composite !!! " );
//		}
	}
	
	//----------------------------------------------------------------------------------------------
	public void log(String s) {
		if ( _logger != null ) {_logger.log(s); };
	}
	
	//----------------------------------------------------------------------------------------------
	public void log(Object o, String s) {
		if ( _logger != null ) { _logger.log(o,s); } ;
	}
	//----------------------------------------------------------------------------------------------
	public TelosysToolsLogger getLogger() {
		return _logger ;
	}

	//----------------------------------------------------------------------------------------------
	public void setDirty() {
		_standardEditor.setDirty();
	}
	
	//----------------------------------------------------------------------------------------------
	protected AbstractStandardEditor getStandardEditor() {
		return _standardEditor;
	}
	//----------------------------------------------------------------------------------------------
	public IProject getProject() {
		return _standardEditor.getProject();
	}

	//----------------------------------------------------------------------------------------------
	private void setPageBackgroundColor(Control pageControl) {
		//Control pageControl = this.getPartControl();
		if ( pageControl != null ) {
			pageControl.setBackground(_backgroundColor) ;
		}
		else {
			MsgBox.error("setPageBackgroundColor() : pageControl is null");
		}		
	}
	//----------------------------------------------------------------------------------------------
//	protected Color getBackgroundColor() {
//		return _backgroundColor;
//	}
	
	public TelosysToolsCfg getProjectConfig () {
//		PluginLogger.log(this, "getProjectConfig()..." );
//		return ProjectConfigManager.loadProjectConfig( getProject() ); // v 3.0.0
		return _standardEditor.getProjectConfig();
	}
	//----------------------------------------------------------------------------------------------
	protected Composite initAndGetFormBody(IManagedForm managedForm, Layout layout) {
		ScrolledForm scrolledForm = managedForm.getForm();		
		Composite scrolledFormBody = scrolledForm.getBody(); 
		log(this, "- body class = " + scrolledFormBody.getClass() );
		// scrolledFormBody.getClass() --> org.eclipse.ui.forms.widgets.LayoutComposite
		
		Layout currentLayout = scrolledFormBody.getLayout();
		if ( currentLayout != null ) {
			log(this, "- body layout class = " + currentLayout.getClass() );
		}
		else {
			log(this, "- body layout class = NO LAYOUT ! ");
		}
		// No Layout for the body at this moment
		
		scrolledFormBody.setLayout( layout );
		
		return scrolledFormBody;
	}
}