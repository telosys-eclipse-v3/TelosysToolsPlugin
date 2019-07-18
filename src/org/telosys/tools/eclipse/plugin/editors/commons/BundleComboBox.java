package org.telosys.tools.eclipse.plugin.editors.commons;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.bundles.BundlesNames;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;

public class BundleComboBox {

	public final static int COMBO_WIDTH  = 300 ; // 260 ;
	public final static int COMBO_HEIGHT =  28 ; // 24 ;
	private final static int COMBO_VISIBLE_ITEMS =  16 ;
	
	private final AbstractModelEditor _editor ;
	private final Combo            _combo ;
	private final AbstractModelEditorPageForGeneration _page ;
	
	public BundleComboBox(Composite parent, AbstractModelEditorPageForGeneration page) {
		super();

		_page = page ;
		_editor = page.getModelEditor();
		
    	_combo = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
		GridData gridData = new GridData(COMBO_WIDTH, COMBO_HEIGHT);
		_combo.setLayoutData( gridData );

		_combo.setVisibleItemCount(COMBO_VISIBLE_ITEMS); // Show a list of N items 

        _combo.addSelectionListener( new SelectionAdapter() 
        {
            public void widgetSelected(SelectionEvent event)
            {
            	//--- Update selected item
        		String[] items = _combo.getItems();
        		String selectedBundle = items[ _combo.getSelectionIndex() ] ;
        		
				if ( StrUtil.different( selectedBundle, _editor.getCurrentBundleName() )) {
					// only if the bundle name has changed : to avoid refresh (visual list effect) if unchanged
					_editor.setCurrentBundleName(selectedBundle);
					_page.refreshBundlesAndTargets();
				}
            }
        });
	}
	
	/**
	 * Populates the combo box from the project's bundles (workspace folders) <br>
	 * and select the editor's current bundle if any 
	 */
	public void refresh() {
		
		//--- Populate combo items 
        IProject eclipseProject = _editor.getProject() ;
        List<String> bundles = getBundlesFromTemplatesFolder(eclipseProject);
        _combo.removeAll();
		for ( String s : bundles ) {
			_combo.add(s); 
		}
		
		//--- Select the bundle currently selected in the editor
		String currentBundle = _editor.getCurrentBundleName() ;
		if ( currentBundle != null ) {
			currentBundle = currentBundle.trim() ;
			for ( int index = 0 ; index < _combo.getItemCount() ; index++ ) {
				if ( currentBundle.equals( _combo.getItem(index) ) ) {
					_combo.select(index);
					break;
				}
			}
		}
	}
	
	private List<String> getBundlesFromTemplatesFolder( IProject eclipseProject ) {
		
//		TelosysToolsCfg telosysToolsCfg = ProjectConfigManager.loadProjectConfig( eclipseProject ); // v 3.0.0	
//		BundlesManager bm = new BundlesManager(telosysToolsCfg);
//		try {
//			return bm.getBundlesList();
//		} catch (TelosysToolsException e) {
//			MsgBox.error(e.getMessage());
//			return new LinkedList<String>();
//		} 

		TelosysProject telosysProject = _page.getTelosysProject();
		
		MsgBox.debug("getBundlesFromTemplatesFolder("+eclipseProject.getName() +") " 
					+ "\n Eclipse IProject : "+ eclipseProject.getLocation().toString()
					+ "\n Telosys Project  : "+ telosysProject.getProjectFolder() );
		
		// get all installed bundles
		BundlesNames bundlesNames;
		try {
			bundlesNames = telosysProject.getInstalledBundles();
			return bundlesNames.getAll();
		} catch (TelosysToolsException e) {
			MsgBox.error("Cannot get installed bundles", e);
			return new LinkedList<String>();
		}
	}
	
}
