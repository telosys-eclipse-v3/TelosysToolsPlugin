package org.telosys.tools.eclipse.plugin.config.view;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TabFolder;

public class LinkSelectionListener implements SelectionListener {

	private final TabFolder tabFolder ;
	private final int tabIndex ;
	
	public LinkSelectionListener(TabFolder tabFolder, int tabIndex) {
		super();
		this.tabFolder = tabFolder;
		this.tabIndex = tabIndex;
		//PluginLogger.log("LinkSelectionListener created : tabindex = " + tabIndex + ", tabFolder items = " + tabFolder.getItemCount() );
	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		//PluginLogger.log("LinkSelectionListener event : widgetSelected : tabindex = " + tabIndex );
		tabFolder.setSelection(tabIndex);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent event) {
		// Nothing 
	}
}
