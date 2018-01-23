package org.telosys.tools.eclipse.plugin.config.view;

import org.eclipse.swt.graphics.Image;
import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.eclipse.plugin.commons.ITableItem;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;

public class VariableItem extends Variable implements ITableItem
{
	public static final String FIRST_COL = "firstcol";

	public static final String NAME  = "name";

	public static final String VALUE = "value";

	private String sFirstCol = "¤";
//	private String sVariableName;
//	private String sVariableValue;
	
	/**
	 * Constructor
	 * @param name
	 * @param value
	 */
	public VariableItem( String name, String value ) {
		super(name, value );
//		sVariableName  = ( name  != null ? name : "" ) ;
//		sVariableValue = ( value != null ? value : "" );
	}

	public VariableItem( Variable var ) 
	{
		super(var.getName(), var.getValue() );
	}

	//------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.objectweb.telosys.plugin.commons.ITableItem#getColumnText(int)
	 */
	public String getColumnText(int columnIndex) {
		String ret = "" ;
		switch ( columnIndex ) 
		{
		case 0 :
			ret = "¤" ;
			break;
		case 1 :
			//ret = this.sVariableName ;
			ret = getName() ;
			break;
		case 2 :
			//ret = this.sVariableValue ;
			ret = getValue() ;
			break;
		}
		return ret ;
	}
	
	//------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.objectweb.telosys.plugin.commons.ITableItem#getColumnImage(int)
	 */
	public Image getColumnImage(int columnIndex) {
		return null; // No image for this type of item
	}
	
	//------------------------------------------------------------
	//------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.objectweb.telosys.plugin.commons.ITableItem#getValue(java.lang.String)
	 */
	public Object getValue(String property) {
		if (NAME.equals(property)) {
			// return this.sVariableName;
			return getName() ;
		}
		if (VALUE.equals(property)) {
			// return this.sVariableValue ;
			return getValue() ;
		}
		return null;
	}
	//------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.objectweb.telosys.plugin.commons.ITableItem#setValue(java.lang.String, java.lang.Object)
	 */
	public void setValue(String property, Object value) {
		if ( value instanceof String )
		{
			String sValue = (String) value ;
			if (NAME.equals(property)) {
				//this.sVariableName = sValue.trim() ;
				setName(sValue);
			}
			if (VALUE.equals(property)) {
				//this.sVariableValue = sValue ;
				setValue(sValue);
			}
		}
		else
		{
			MsgBox.error("VariableItem : the value is not a String ");
		}
	}

	//------------------------------------------------------------
	public static String[] getColumnProperties() {
		return new String[] { FIRST_COL, NAME, VALUE };
	}
	
	//------------------------------------------------------------
	public String getFirstCol() {
		return sFirstCol;
	}
	
	//------------------------------------------------------------

//	public String getName() {
//		return sVariableName;
//	}
//
////	public void setName(String v) {
////		sVariableName = v;
////	}

//	/**
//	 * Returns the variable name between "${ and "}"
//	 * @return
//	 */
//	public String getSymbolicName() {
//		return "${"+sVariableName+"}" ;
//	}

//	//------------------------------------------------------------
//	
//	public String getValue() {
//		return sVariableValue;
//	}
//
////	public void setValue(String v) {
////		sVariableValue = v;
////	}

//	//------------------------------------------------------------
//	/* (non-Javadoc)
//	 * @see java.lang.Comparable#compareTo(java.lang.Object)
//	 */
//	public int compareTo( Object o) {
//		if ( o instanceof VariableItem )
//		{
//			return this.getName().compareTo( ((VariableItem)o).getName() );
//		}
//		else
//		{
//			return 0 ;
//		}
//	}

}