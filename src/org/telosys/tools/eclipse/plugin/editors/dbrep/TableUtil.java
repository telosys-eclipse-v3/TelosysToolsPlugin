package org.telosys.tools.eclipse.plugin.editors.dbrep;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableItem;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginImages;
import org.telosys.tools.repository.model.AttributeInDbModel;

/**
 * 
 *  
 * @author Laurent GUERIN
 *
 */
public class TableUtil {

	//-----------------------------------------------------------------------------

	//public static Column getTableColumn( TableItem tableItem ) 
	public static AttributeInDbModel getTableColumn( TableItem tableItem )  // v 3.0.0
	{
		if ( tableItem != null )
		{
			Object oData = tableItem.getData();
			if ( oData == null ) {
				MsgBox.error("TableUtil.getTableRow() : no data in the TableItem !");
				return null ;
			}
			//if ( oData instanceof Column != true ) {
			if ( oData instanceof AttributeInDbModel != true ) { // v 3.0.0
				MsgBox.error("TableUtil.getTableRow() : TableItem data is not a Column !");
				return null ;
			}
			//return (Column) oData ;
			return (AttributeInDbModel) oData ; // v 3.0.0
		}
		else
		{
			MsgBox.error("TableUtil.getTableRow() : TableItem param is null !");
			return null ;
		}
	}
	
	/**
	 * Returns the image showing the "null" or "not null" property for the given attribute
	 * @param attribute
	 * @return
	 */
	//public static Image getJavaNotNullImage( Column modelColumn ) 
	public static Image getJavaNotNullImage( AttributeInDbModel attribute )  // v 3.0.0
	{
		if ( null == attribute ) {
			MsgBox.error("TableUtil.getJavaNotNullImage() : Column param is null !");
			return null ;
		}
		
		Image image = null ;
		if ( attribute.isJavaPrimitiveType() ) {
			image = PluginImages.getImage(PluginImages.NOTNULL_ON);
		}
		else {
			//if ( modelColumn.getJavaNotNull() ) {
			if ( attribute.isNotNull() ) { // v 3.0.0
				image = PluginImages.getImage(PluginImages.NOTNULL_ON);
			}
			else {
				image = PluginImages.getImage(PluginImages.NOTNULL_OFF);
			}
		}
		return image ;
	}
}
