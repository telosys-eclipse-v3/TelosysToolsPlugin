package org.telosys.tools.eclipse.plugin.editors.dbrep;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.telosys.tools.commons.javatypes.JavaTypes;
import org.telosys.tools.commons.javatypes.JavaTypesManager;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginImages;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.repository.model.AttributeInDbModel;


/**
 * Label provider for the TableViewer
 *  
 * Provides the cell text or image for each column of each "row element" 
 * the return is set in the TableItem cell of the SWT table 
 * 
 * For each column :
 *  1. call getColumnText(Object element, int columnIndex)
 *  2. call getColumnImage(Object element, int columnIndex)
 * 
 * @see org.eclipse.jface.viewers.LabelProvider 
 */
class TableViewerLabelProvider 	extends LabelProvider implements ITableLabelProvider
{
//	/**
//	 * Note: An image registry owns all of the image objects registered with it,
//	 * and automatically disposes of them the SWT Display is disposed.
//	 */ 
//	static {
//		String iconPath = "icons/"; 
//		imageRegistry.put(CHECKED_IMAGE, ImageDescriptor.createFromFile(
//				TableViewerExample.class, 
//				iconPath + CHECKED_IMAGE + ".gif"
//				)
//			);
//		imageRegistry.put(UNCHECKED_IMAGE, ImageDescriptor.createFromFile(
//				TableViewerExample.class, 
//				iconPath + UNCHECKED_IMAGE + ".gif"
//				)
//			);	
//	}
//	
//	/**
//	 * Returns the image with the given key, or <code>null</code> if not found.
//	 */
//	private Image getImage(boolean isSelected) {
//		String key = isSelected ? CHECKED_IMAGE : UNCHECKED_IMAGE;
//		return  imageRegistry.get(key);
//	}
	
	private static void log(String msg) {
		if ( _PackageLoggerConfig.LOG ) {
			PluginLogger.log(TableViewerLabelProvider.class, msg);
		}
	}

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 * 
	 * Returns the label text for the given column of the given element.
	 * ( method called by the TableViewer )
	 * 
	 * Parameters: 
	 *  element - the object representing the entire row, 
	 *            or null indicating that no input object is set in the viewer 
	 *  columnIndex - the zero-based index of the column in which the label appears
	 *   
	 */
	public String getColumnText(Object element, int columnIndex) 
	{
		log("getColumnText(element, " + columnIndex + ")..." );
//		if ( element instanceof Column != true )
//		{
//			MsgBox.error ( "getColumnText() : element is not an instance of AttributeInDbModel !" );
//			return "??";
//		}
//		Column modelColumn = (Column) element ;
		AttributeInDbModel modelColumn = getAttributeInDbModel("getColumnText", element); // v 3.0.0
		if ( modelColumn == null ) return "??" ;
		switch (columnIndex) {
			case ColumnNames.SELECTED_INDEX :  
				return null ;
				
			case ColumnNames.PRIMARY_KEY_INDEX :  
				return null ;
				
			case ColumnNames.DB_NAME_INDEX :
				return modelColumn.getDatabaseName();
				
			case ColumnNames.DB_TYPE_INDEX : // SMALLINT, VARCHAR(n), DATE, ...
				return modelColumn.getDatabaseTypeNameWithSize(); 
				
			case ColumnNames.JDBC_TYPE_INDEX :
				return "" + modelColumn.getJdbcTypeCodeWithText();
				
			case ColumnNames.JAVA_NAME_INDEX :
				//return modelColumn.getJavaName();
				return modelColumn.getName(); // v 3.0.0
				
			case ColumnNames.JAVA_TYPE_INDEX :
				//String sType = modelColumn.getJavaType(); // Java Type stored in the model 
				//String sType = modelColumn.getFullType(); // Java Type stored in the model  // v 3.0.0
				String sType = modelColumn.getModelFullType(); // Java Type stored in the model  // v 3.0.0
				JavaTypes types = JavaTypesManager.getJavaTypes();
				String sText = types.getTextForType(sType);
				if ( sText != null ) {
					return sText ; // The text to show in the table
				}
				MsgBox.error("Cannot found text for type " + sType );
				return "????";

			case ColumnNames.SPECIAL_INDEX :
				return modelColumn.getSpecialTypeInfo();
		}
		return "?";
	}

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 * 
	 * Returns the label image for the given column of the given element.
	 * The image is put at the left of the text in the same cell.
	 * ( method called by the TableViewer )
	 * 
	 * Parameters: 
	 *  element - the object representing the entire row, 
	 *            or null indicating that no input object is set in the viewer
	 *  columnIndex - the zero-based index of the column in which the label appears
	 *  
	 * Returns:
	 *  Image or null if there is no image for the given object at columnIndex
	 * 
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		log("getColumnImage(element, " + columnIndex + ")..." );
//		if ( element instanceof Column != true )
//		{
//			MsgBox.error ( "getColumnImage() : element is not a TableRow !" );
//			return null ;
//		}
//		Column column = (Column) element ;
		AttributeInDbModel column = getAttributeInDbModel("getColumnImage", element); // v 3.0.0
		if ( column == null ) return null ;
		switch (columnIndex) 
		{
		case ColumnNames.PRIMARY_KEY_INDEX : // Column for PK / FK images
			// if ( column.isPrimaryKey() ) 
			if ( column.isKeyElement() )  // v 3.0.0
			{
				if ( column.isAutoIncremented() ) {
					//--- Primary Key and Auto-incremented
					return PluginImages.getImage(PluginImages.PRIMARYKEY_AUTOINCR);
				}
				//if ( column.isForeignKey() ) {
				//if ( column.isUsedInForeignKey() ) { // v 3.0.0
				if ( column.isFK() ) { // v 3.0.0
					//--- Primary Key and Foreign Key
					return PluginImages.getImage(PluginImages.PRIMARYKEY_FK);
				}
				//--- Simple Primary Key
				return PluginImages.getImage(PluginImages.PRIMARYKEY);
			}
			//else if ( column.isForeignKey() )
			//else if ( column.isUsedInForeignKey() ) // v 3.0.0
			else if ( column.isFK() ) // v 3.0.0
			{
				//--- Simple Foreign Key
				return PluginImages.getImage(PluginImages.FOREIGNKEY);
			}
			break;
		case ColumnNames.DB_TYPE_INDEX : // Column for "Database type" with "not null" image
			if ( column.isDatabaseNotNull() ) {
				return PluginImages.getImage(PluginImages.NOTNULL_ON);
			}
			else {
				return PluginImages.getImage(PluginImages.NOTNULL_OFF);
			}
		case ColumnNames.JAVA_TYPE_INDEX :
			return null ; // No image
			
		case ColumnNames.SPECIAL_INDEX :
			return TableUtil.getJavaNotNullImage(column) ;
		}
		return null ; // No image
	}

	private AttributeInDbModel getAttributeInDbModel(String methodName, Object element) { // v 3.0.0
		if ( element instanceof AttributeInDbModel != true ) 
		{
			MsgBox.error ( "Class " + this.getClass().getSimpleName(), 
					"Method '" + methodName + "' : element is not an instance of AttributeInDbModel !" );
			return null ;
		}
		return (AttributeInDbModel) element ;
	}
}
