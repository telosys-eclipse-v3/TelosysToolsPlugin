package org.telosys.tools.eclipse.plugin.config.view;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class InitButtonSelectionListener implements SelectionListener {

	private final PropertiesPage propertiesPage ;
	
	public InitButtonSelectionListener(PropertiesPage propertiesPage) {
		super();
		this.propertiesPage = propertiesPage;
	}

	@Override
	public void widgetSelected(SelectionEvent event) {
    	//MsgBox.info("Init"); 

		//PluginLogger.log("LinkSelectionListener event : widgetSelected : tabindex = " + tabIndex );
		propertiesPage.initializeProject();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent event) {
		// Nothing 
	}
}
