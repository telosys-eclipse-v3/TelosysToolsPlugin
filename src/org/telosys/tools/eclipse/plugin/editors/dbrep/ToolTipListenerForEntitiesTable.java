package org.telosys.tools.eclipse.plugin.editors.dbrep;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.telosys.tools.eclipse.plugin.commons.listeners.AbstractListenerForTableToolTip;

public class ToolTipListenerForEntitiesTable extends AbstractListenerForTableToolTip {

	public ToolTipListenerForEntitiesTable(Table table) {
		super(table);
	}

	protected String getToolTipInfo(TableItem tableItem) {
		//--- Info to be displayed
		String info = "( no information )" ;
		Object data = tableItem.getData();
		if ( data != null ) {
			if ( data instanceof String ) {
				String entityName = (String) data ;
				info = entityName ;
			}
		}
		return info ;		
	}

}
