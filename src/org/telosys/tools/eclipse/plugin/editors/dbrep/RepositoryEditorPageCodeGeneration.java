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
 * Editor Page 3 : "Bulk Generation"
 * 
 */
///* package */ class RepositoryEditorPage3 extends RepositoryEditorPage {
/* package */ class RepositoryEditorPageCodeGeneration extends AbstractModelEditorPageForGeneration {

	/**
	 * Constructor
	 * @param editor
	 * @param id
	 * @param title
	 */
	public RepositoryEditorPageCodeGeneration(FormEditor editor, String id, String title ) {
		super(editor, id, title);
	}

	//----------------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
	}
    
	//----------------------------------------------------------------------------------------------	
//	/**
//	 * Populates the list of entities ( left side table )
//	 */
//	private void populateEntitiesTable()
//	{
//		log(this, "populateEntitiesTable()");
//		
//		RepositoryEditor repEditor = (RepositoryEditor) getEditor();
//		RepositoryModel dbRep = repEditor.getDatabaseRepository();
//		
//		//Entity[] entities = dbRep.getEntities();
////		EntityInDbModel[] entities = dbRep.getEntitiesArray(); // v 3.0.0
//		EntityInDbModel[] entities = dbRep.getEntitiesArraySortedByTableName(); // v 3.0.0
//		if ( entities != null )
//		{
////			for ( int i = 0 ; i < entities.length ; i++ ){
////				Entity entity = entities[i];
//			for ( EntityInDbModel entity : entities ) { // v 3.0.0
//				//String sTableName = entity.getName() ;
//				String tableName = entity.getDatabaseTable() ; // v 3.0.0
//				if ( entity.getWarnings() != null && entity.getWarnings().size() > 0 ) {
//					tableName = "(!) " + tableName;
//				}
//				//String sBeanClass = entity.getBeanJavaClass();
//				String entityClassName = entity.getClassName(); // v 3.0.0
//				
//				if ( entityClassName == null ) entityClassName = "???" ;
//				
//                //--- Create the row content 
//                String[] row = new String[] { tableName, entityClassName };
//				
//                //--- Create the TableItem and set the row content 
//            	TableItem tableItem = new TableItem(_tableEntities, SWT.NONE );
//                tableItem.setChecked(false);                
//                tableItem.setText(row);                
//                //tableItem.setData( sTableName );
//                tableItem.setData( entityClassName ); // v 3.0.0
//			}
//		}
//	}
	
	@Override
	public void createEntitiesTableColumns(Table table) {
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
		if ( entities != null )
		{
			for ( Entity entity : entities ) { 
				String tableName = entity.getDatabaseTable() ; // v 3.0.0
				String entityClassName = entity.getClassName(); // v 3.0.0
				
                //--- Create the row content 
                String[] row = new String[] { entityClassName, tableName };
				
                //--- Create the TableItem and set the row content 
            	TableItem tableItem = new TableItem(table, SWT.NONE );
                //tableItem.setChecked(false);
                tableItem.setChecked(true); // All entities checked by default
                tableItem.setText(row);
                
	            tableItem.setImage( getEntityWarningImage(entity) ) ; // v 3.0.0

                //tableItem.setData( sTableName );
                tableItem.setData( entityClassName ); // v 3.0.0
			}
		}
	}
}