package org.telosys.tools.eclipse.plugin.editors.dbrep;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Table;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.repository.model.EntityInDbModel;

/**
 * This class is the "Content Provider" for the "Table Viewer"
 * It contains the current entity that provides the table rows to display
 * 
 * @author Laurent Guerin
 *
 */
class TableViewerContentProvider implements IStructuredContentProvider 
{
	private static void log(String msg) {
		if ( _PackageLoggerConfig.LOG ) {
			PluginLogger.log(TableViewerContentProvider.class, msg);
		}
	}

	//private Entity _currentTableViewerEntity = null ;
	private EntityInDbModel _currentTableViewerEntity = null ;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) 
	{
		// This method is implemented in order to 
		// . verify the type of the input ( old and new )
		// . keep a reference on the input
		
		log("inputChanged("
				+ "viewer : " + ( viewer != null ? viewer.getClass().getName() : "null" )
				+ ", oldInput : " + ( oldInput != null ? oldInput.getClass().getName() : "null" )
				+ ", newInput : " + ( newInput != null ? newInput.getClass().getName() : "null" )
				+ ")...");
		
		if (viewer != null) {
			if (viewer instanceof TableViewer != true) {
				MsgBox.error("inputChanged(..) : viewer is not a TableViewer");
				return;
			}
			TableViewer tableViewer = (TableViewer) viewer;
			Table table = tableViewer.getTable(); 
			
			log("inputChanged(..,..,..) table.getItemCount() : " + table.getItemCount() );
			// At this moment the table contains the OLD rows ( new input not yet set )
		} 
		else 
		{
			MsgBox.error("inputChanged(..) : viewer is NULL !");
			return;
		}
		
		if (oldInput != null) {
			//if (oldInput instanceof Entity != true) {
			if (oldInput instanceof EntityInDbModel != true) { // v 3.0.0
				String msg = "inputChanged(..,..,..) : oldInput is not an instance of Entity" ;
				log(msg);
				MsgBox.error(msg);
				return;
			}
		}
		
		if (newInput != null) {
			//if (newInput instanceof Entity != true) {
			if (newInput instanceof EntityInDbModel != true) { // v 3.0.0
				String msg = "inputChanged(..,..,..) : newInput is not an instance of Entity" ;
				log(msg);
				MsgBox.error(msg);
				return;
			}
			//_currentTableViewerEntity = (Entity) newInput;
			_currentTableViewerEntity = (EntityInDbModel) newInput; // v 3.0.0
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		log("dispose(..,..,..)...");
		_currentTableViewerEntity = null ;
	}

	//----------------------------------------------------------------------------------------------
	/* 
	 * IStructuredContentProvider implementation.
	 * 
	 * Returns the elements to display in the viewer when its input is set to the given element. 
	 * These elements can be presented as rows in a table, items in a list, etc. 
	 * The result is not modified by the viewer. 
	 * 
	 * Here returns the table rows 
	 */
	public Object[] getElements(Object parent) 
	{
		log("getElements(..)...");
		if ( _currentTableViewerEntity != null )
		{
			//return _currentTableViewerEntity.getColumns();
			return _currentTableViewerEntity.getAttributesArray(); // v 3.0.0
		}
		return new Object[0] ;
	}

	//		/* (non-Javadoc)
	//		 * @see ITaskListViewer#addTask(ExampleTask)
	//		 */
	//		public void addTask(ExampleTask task) {
	//			tableViewer.add(task);
	//		}
	//
	//		/* (non-Javadoc)
	//		 * @see ITaskListViewer#removeTask(ExampleTask)
	//		 */
	//		public void removeTask(ExampleTask task) {
	//			tableViewer.remove(task);
	//		}
	//
	//		/* (non-Javadoc)
	//		 * @see ITaskListViewer#updateTask(ExampleTask)
	//		 */
	//		public void updateTask(ExampleTask task) {
	//			tableViewer.update(task, null);
	//		}
	
}