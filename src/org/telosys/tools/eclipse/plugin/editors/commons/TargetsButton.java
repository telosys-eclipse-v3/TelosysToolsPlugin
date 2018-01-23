package org.telosys.tools.eclipse.plugin.editors.commons;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.telosys.tools.eclipse.plugin.commons.FileEditorUtil;
import org.telosys.tools.eclipse.plugin.commons.PluginImages;

public class TargetsButton {

	public final static int  BUTTON_HEIGHT =  26 ;
	public final static int  BUTTON_WIDTH  =  90 ;
	
	private final Button           _button ;
//	private final EditorWithCodeGeneration _editor ;
	private final AbstractModelEditor  _editor ;
	private final IProject         _project ;

//	public TargetsButton(Composite parent, EditorWithCodeGeneration editor, IProject project ) {
	public TargetsButton(Composite parent, AbstractModelEditor editor, IProject project ) {
		super();

		_editor = editor ;
		_project = project ;
		
		_button = new Button(parent, SWT.NONE);
		//_button.setText("Configure");
		_button.setToolTipText("Edit templates and resources\ndefined in 'templates.cfg' file");

		_button.setImage( PluginImages.getImage(PluginImages.TARGETS ) );
		
		// ver 3.0.0 : no layout data ( no GridData )
		// _button.setLayoutData ( new GridData (26, 26) );
		
		_button.addSelectionListener( new SelectionListener() 
	    	{
	            public void widgetSelected(SelectionEvent arg0)
	            {
	            	//--- Standard behavior : open the targets/templates configuration file in text editor
	            	FileEditorUtil.openTargetsConfigFileInEditor(_project, _editor.getCurrentBundleName());
	            }
	            public void widgetDefaultSelected(SelectionEvent arg0)
	            {
	            }
	        });
	}
	
	public Button getButton() {
		return _button ;
	}
	
	public void setEnabled(boolean enabled) {
		_button.setEnabled(enabled);
	}
	
	public void addSelectionListener(SelectionListener listener) {
		_button.addSelectionListener( listener ) ;
	}
}
