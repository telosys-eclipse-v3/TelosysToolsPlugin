package org.telosys.tools.eclipse.plugin.commons;

import org.eclipse.swt.graphics.Image;

public interface ITableItem {

	/**
	 * Returns the text for the given column index 
	 * @param columnIndex from 0 to N
	 * @return
	 */
	public String getColumnText(int columnIndex) ;

	/**
	 * Returns the image for the given column index 
	 * @param columnIndex
	 * @return
	 */
	public Image  getColumnImage(int columnIndex) ;
	
	
	public Object getValue(String property);
	
	public void setValue(String property, Object value);
	
	
}
