package org.telosys.tools.eclipse.plugin.editors.commons;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.eclipse.plugin.commons.FileEditorUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;

public class OpenTemplateFileInEditor implements Listener {

//	private final EditorWithCodeGeneration editor ;
	private final AbstractModelEditor editor ;
	private final IProject project ;
	private final Table    table ;
	private final int      columnClick ;
	
	
//	public OpenTemplateFileInEditor(EditorWithCodeGeneration editor, IProject project, Table table, int columnClick) {
	public OpenTemplateFileInEditor(AbstractModelEditor editor, IProject project, Table table, int columnClick) {
		super();
		this.editor = editor ;
		this.project = project;
		this.table = table;
		this.columnClick = columnClick;
	}


	@Override
	public void handleEvent(Event event) {
        Point pt = new Point(event.x, event.y);
        TableItem item = table.getItem(pt);
        if (item == null)
          return;
        else {
        	int column = 0 ;
	        int columnCount = table.getColumnCount();
	        for (int i = 0; i < columnCount; i++) {
	          Rectangle rect = item.getBounds(i);
	          if (rect.contains(pt)) {
	        	  //column = table.indexOf(item);
	        	  column = i ;
	          }
	        }
	        if ( column == columnClick ) {
	        	Object data = item.getData();
	        	if ( data != null ) {
	        		if ( data instanceof TargetDefinition ) {
	        			TargetDefinition target = (TargetDefinition) item.getData();
//			        	MsgBox.info("Edit template : " + target.getName() + " - " + target.getTemplate() + 
//			        			"\n Column = " + column );
	        			
			        	FileEditorUtil.openTemplateFileInEditor(project, editor.getCurrentBundleName(), target);
	        		}
	        		else {
	        			MsgBox.error("Edit event : this row is not a target instance");
	        		}
	        	}
	        	else {
        			MsgBox.error("Edit event : no data (null)");
	        	}
	        }
        }
	}
}
