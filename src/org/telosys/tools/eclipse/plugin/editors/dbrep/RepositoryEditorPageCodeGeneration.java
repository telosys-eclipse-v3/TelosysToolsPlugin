package org.telosys.tools.eclipse.plugin.editors.dbrep;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.editor.FormEditor;
import org.telosys.tools.eclipse.plugin.editors.commons.AbstractModelEditorPageForGeneration;
import org.telosys.tools.generic.model.Entity;

/**
 * Editor Page : "Code Generation"
 * 
 */
/* package */ class RepositoryEditorPageCodeGeneration extends AbstractModelEditorPageForGeneration {

	/**
	 * Constructor
	 * @param editor
	 * @param id
	 * @param title
	 */
	public RepositoryEditorPageCodeGeneration(FormEditor editor, String id, String title ) {
		super(editor, id, title);
		log(this, "constructor(.., '"+id+"', '"+ title +"')..." );		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
		log(this, "init(..,..) : input name = '" + input.getName() + "'" );
	}
    
	@Override
	public void createEntitiesTableColumns(Table table) {
		log(this, "createEntitiesTableColumns(Table)" );
		TableColumn col = null ;
		int iColumnIndex = 0 ;

		//--- First column : "Entity class name"
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Entity class name");
		col.setWidth(220);

		//--- Second column : "Database table name"
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Database table");
		col.setWidth(200);
	}
	
	@Override
	public void populateEntitiesTable(Table table, List<Entity> entities) {
		log(this, "populateEntitiesTable(Table,List)" );
		if ( entities != null ) {
			for ( Entity entity : entities ) { 
				String tableName = entity.getDatabaseTable() ; // v 3.0.0
				String entityClassName = entity.getClassName(); // v 3.0.0
				
                //--- Create the row content 
                String[] row = new String[] { entityClassName, tableName };
				
                //--- Create the TableItem and set the row content 
            	TableItem tableItem = new TableItem(table, SWT.NONE );
                tableItem.setChecked(true); // All entities checked by default
                tableItem.setText(row);
	            tableItem.setImage( getEntityWarningImage(entity) ) ; // v 3.0.0
                tableItem.setData( entityClassName ); // v 3.0.0
			}
		}
	}
}