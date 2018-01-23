package org.telosys.tools.eclipse.plugin.editors.commons;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.telosys.tools.eclipse.plugin.commons.PluginImages;

public class RefreshButton {

	private final static int  BUTTON_HEIGHT =  30 ; // 26 ;
	private final static int  BUTTON_WIDTH  = 114 ; // 90 ; 
	
	private final Button           _button ;
//	private final EditorWithCodeGeneration _editor ;
//	private final ModelEditor _editor ;
	private final AbstractModelEditorPageForGeneration _page ;

	/**
	 * Creates a "Refresh" button in the given Composite with a "GridData" LayoutData
	 * @param parent
	 * @param editor
	 */
//	public RefreshButton(Composite parent, EditorWithCodeGeneration editor) {
	public RefreshButton(Composite parent, AbstractModelEditorPageForGeneration page) {
		super();
		_page = page ;

		_button = new Button(parent, SWT.NONE);
		_button.setText("Refresh");
		_button.setImage( PluginImages.getImage(PluginImages.REFRESH ) );
		_button.setToolTipText("Reload bundles and templates from the filesystem");

		
		GridData gridData = new GridData (BUTTON_WIDTH, BUTTON_HEIGHT) ;
		// no effect
		//gridData.grabExcessVerticalSpace = false ; // default is false
		//gridData.minimumHeight = 2 ;
		//gridData.verticalIndent= 0 ;
//		gridData.verticalAlignment   = SWT.BEGINNING ;
//		gridData.horizontalAlignment = SWT.BEGINNING ;
		gridData.verticalAlignment   = GridData.BEGINNING ;
		gridData.horizontalAlignment = GridData.BEGINNING ;
		init(gridData);
	}
	
//	/**
//	 * Creates a "Refresh" button in the given Composite with a specific LayoutData
//	 * @param parent
//	 * @param layoutData
//	 * @param editor
//	 */
////	public RefreshButton(Composite parent, Object layoutData, EditorWithCodeGeneration editor) {
//	public RefreshButton(Composite parent, Object layoutData, AbstractModelEditorPageForGeneration page) {
//		super();
//
//		_page = page ;
//		//_editor = editor ; 
////		_editor = page.getModelEditor();
//		
//		_button = new Button(parent, SWT.NONE);
//		
//		init(layoutData);
//	}
	
	private void init(Object layoutData) {
		
		_button.setLayoutData ( layoutData );
		
		_button.addSelectionListener(new SelectionListener()  // v 2.0.7
		{
	        public void widgetSelected(SelectionEvent arg0)
	        {
	        	//--- Reload the bundles and targets list
//	        	_editor.refreshAllTargetsTablesFromConfigFile();
//	        	_page.refreshAllTargetsTablesFromConfigFile();
	        	_page.refreshBundlesAndTargets();
	        }
	        public void widgetDefaultSelected(SelectionEvent arg0)
	        {
	        }
	    });
		
	}
	
//	public Button getButton() {
//		return _button ;
//	}
	
	public void setEnabled(boolean enabled) {
		_button.setEnabled(enabled);
	}
	
//	public void addSelectionListener(SelectionListener listener) {
//		_button.addSelectionListener( listener ) ;
//	}
}
