package org.telosys.tools.eclipse.plugin.editors.dbrep;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.eclipse.plugin.commons.listeners.AbstractListenerForTableToolTip;

public class ToolTipListenerForTargetsTable extends AbstractListenerForTableToolTip {

	public ToolTipListenerForTargetsTable(Table table) {
		super(table);
	}

	protected String getToolTipInfo(TableItem tableItem) {
		//--- Info to be displayed
		String info = "( no target information )" ;
		Object data = tableItem.getData();
		if ( data != null ) {
			if ( data instanceof TargetDefinition ) {
				TargetDefinition targetDefinition = (TargetDefinition) data ;
				info = 
					targetDefinition.getFolder() +
					"   |   " +
					targetDefinition.getFile() ;
			}
		}
		return info ;		
	}

}
