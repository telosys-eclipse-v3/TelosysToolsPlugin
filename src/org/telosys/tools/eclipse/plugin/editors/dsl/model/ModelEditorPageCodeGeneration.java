package org.telosys.tools.eclipse.plugin.editors.dsl.model;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.editor.FormEditor;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.editors.commons.AbstractModelEditorPageForGeneration;
import org.telosys.tools.generic.model.Entity;

/**
 * Editor Page 3 : "Code Generation"
 * 
 */
/* package */ class ModelEditorPageCodeGeneration extends AbstractModelEditorPageForGeneration {

	//----------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param editor
	 * @param id
	 * @param title
	 */
	public ModelEditorPageCodeGeneration(FormEditor editor, String id, String title ) {
		super(editor, id, title);
	}

	//----------------------------------------------------------------------------------------------
	@Override
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
	}
	
	//----------------------------------------------------------------------------------------------
	@Override
	protected void createEntitiesTableColumns(Table table) {
		//--- Columns
		TableColumn col = null ;
		int iColumnIndex = 0 ;

		//--- A single column for "ENtity class name"
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Entity class name");
		col.setWidth(400);
	}
	
	//----------------------------------------------------------------------------------------------	
	/**
	 * Populates the list of entities ( left side table )
	 */
	@Override
	protected void populateEntitiesTable(Table table, List<Entity> entities) {
		if ( table != null && entities != null ) {
			for ( Entity entity : entities ) { 
				String entityClassName = entity.getClassName(); 
				
	            //--- Create the row content : a single column for "entity class name"
	            String[] row = new String[] { entityClassName };
				
	            //--- Create the TableItem and set the row content 
	        	TableItem tableItem = new TableItem(table, SWT.NONE );
	            //tableItem.setChecked(false);
	            tableItem.setChecked(true); // All entities checked by default
	            tableItem.setText(row);      
	            
	            // ROW HEIGHT TESTS 
	            // tableItem.setImage( PluginImages.getImage(PluginImages.FILE1 ) ); // JUST FOR TESTS
	            // The row height is fixed from the image height ( here 16 px ) !
	            
	            tableItem.setImage( getEntityWarningImage(entity) ) ;
	            		
	            tableItem.setData( entityClassName ); 
			}
		}
		else {
			if ( table == null ) {
				MsgBox.error("table is null !");
			}
			if ( entities == null ) {
				MsgBox.error("entities list is null !");
			}
		}
	}

}