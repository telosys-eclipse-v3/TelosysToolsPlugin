package org.telosys.tools.eclipse.plugin.editors.commons;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

public abstract class AbstractStandardEditorPage extends FormPage {

	private final AbstractStandardEditor  _standardEditor ; // Ref on the Editor this page belongs to
	
	/**
	 * Constructor
	 * @param editor
	 * @param id
	 * @param title
	 */
	public AbstractStandardEditorPage(FormEditor editor, String id, String title ) {
		super(editor, id, title);
		PluginLogger.log(this, "constructor(.., '"+id+"', '"+ title +"')..." );
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
	}
	
	//----------------------------------------------------------------------------------------------
	@Override
	protected void createFormContent(IManagedForm managedForm) 
	{
		super.createFormContent(managedForm);
		log(this, "createFormContent(..) [ Abstract class ] ..." );
		Control pageControl = getPartControl();
		if ( pageControl == null ) {
			MsgBox.error("Page control is null ! Cannot create form content.");
			return;
		}
	}
	
	//----------------------------------------------------------------------------------------------
	public void log(String s) {
		PluginLogger.log(s);
	}
	
	//----------------------------------------------------------------------------------------------
	public void log(Object o, String s) {
		PluginLogger.log(o,s);
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
//	private void setPageBackgroundColor(Control pageControl) {
//		if ( pageControl != null ) {
//			pageControl.setBackground(_backgroundColor) ;
//		}
//		else {
//			MsgBox.error("setPageBackgroundColor() : pageControl is null");
//		}		
//	}
	//----------------------------------------------------------------------------------------------
	public TelosysToolsCfg getProjectConfig () {
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