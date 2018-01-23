package org.telosys.tools.eclipse.plugin.editors.dbrep;

import java.util.LinkedList;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;

public class TargetsUtil {

	public static void addSelectedItemsToList(LinkedList<TargetDefinition> list, Table table)
	{
		int n = table.getItemCount() ;
		for ( int i = 0 ; i < n ; i++ )
		{
			TableItem tableItem = table.getItem(i);
			if ( tableItem.getChecked() == true )
			{
				// Retrieve the Target associated with the table item
		    	Object oData = tableItem.getData();
		    	if ( oData != null )
		    	{
			    	if ( oData instanceof TargetDefinition )
			    	{
			    		TargetDefinition target = (TargetDefinition) oData ;
			    		list.addLast(target);
			    	}
			    	else
			    	{
			    		MsgBox.error("Table item " + i + " : invalid Data type (not a GenericTarget)");
			    		return ;
			    	}
		    	}
		    	else
		    	{
		    		MsgBox.error("Table item " + i + " : data is null (GenericTarget expected)");
		    		return ;
		    	}
			}
		}		
	}
}
