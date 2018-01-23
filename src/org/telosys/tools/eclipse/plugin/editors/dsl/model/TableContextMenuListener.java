package org.telosys.tools.eclipse.plugin.editors.dsl.model;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;

public class TableContextMenuListener implements Listener {

	public final static int NEW    = 1 ;
	public final static int OPEN   = 2 ;
	public final static int RENAME = 3 ;
	public final static int DELETE = 4 ;
	
	private final int                          action ;
	private final ModelEditorPageModelEntities entitiesPage ;
	
	public TableContextMenuListener( ModelEditorPageModelEntities entitiesPage, int action) {
		super();
		this.action = action;
		this.entitiesPage = entitiesPage;
	}

	@Override
	public void handleEvent(Event event) {

		// MenuItem menuItem = (MenuItem) event.widget ;
		/*
		MsgBox.info("Listener : handleEvent(Event event) \n" 
				+ "\n widget : " + event.widget.getClass()  // class is MenuItem 
				+ "\n item : " + ( event.item != null ? "not null" : "null" ) // null
				+ "\n data : " + ( event.data != null ? "not null" : "null" ) // null
				+ "\n"
				+ "\n menuItem.getText() : " + menuItem.getText() 
				+ "\n menuItem.getData() : " + ( menuItem.getData() != null ? "not null" : "null" ) // null
				);
		*/
		
		TableItem selectedItem = entitiesPage.getSelectedTableItem() ;
		String entityAbsoluteFilePath = (String) selectedItem.getData() ;
		switch ( action ) {
		case NEW :
			doNew(entityAbsoluteFilePath);
			break;
		case OPEN :
			doOpen(entityAbsoluteFilePath);
			break;
		case RENAME :
			doRename(entityAbsoluteFilePath);
			break;
		case DELETE :
			doDelete(entityAbsoluteFilePath);
			break;
		}
	}

	private void doNew(String entityAbsoluteFilePath) {
		entitiesPage.doNewEntity();
	}
	
	private void doOpen(String entityAbsoluteFilePath) {
		entitiesPage.doOpenEntityInEditor(entityAbsoluteFilePath);
	}
	
//	private void doRename(TableItem selectedItem) {
////		Shell shell = table.getDisplay().getActiveShell() ;
//		Shell shell = entitiesPage.getShell() ;
//		String currentName = "EntityCurrentName" ;
//		DialogBoxForRenameEntity dialogBox = new DialogBoxForRenameEntity( shell, currentName );
//		if ( dialogBox.open() == Window.OK ) {
//			String newEntityName = dialogBox.getNewEntityName();
//			MsgBox.info("OK : Rename to " + newEntityName ) ;
//		}
//		else {
//			MsgBox.info("Not OK " ) ;
//		}
//		entitiesPage.doOpenEntityInEditor(entityAbsoluteFilePath);
//	}
	private void doRename(String entityAbsoluteFilePath) {
		entitiesPage.doRenameEntity(entityAbsoluteFilePath);
	}
	
	private void doDelete(String entityAbsoluteFilePath) {
		entitiesPage.doDeleteEntity(entityAbsoluteFilePath);
	}
}
