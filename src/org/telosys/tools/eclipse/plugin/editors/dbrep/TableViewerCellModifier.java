package org.telosys.tools.eclipse.plugin.editors.dbrep;

import java.sql.Types;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableItem;
import org.telosys.tools.commons.JavaTypeUtil;
import org.telosys.tools.commons.javatypes.JavaTypes;
import org.telosys.tools.commons.javatypes.JavaTypesManager;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.generic.model.DateType;
import org.telosys.tools.repository.model.AttributeInDbModel;

/**
 *
 */
class TableViewerCellModifier implements ICellModifier 
{
//	private void log(Object o, String s)
//	{
//		//PluginLogger.log(o,s);
//	}
	private static void log(String msg) {
		if ( _PackageLoggerConfig.LOG ) {
			PluginLogger.log(TableViewerCellModifier.class, msg);
		}
	}

	RepositoryEditor _editor = null ;
	
	/**
	 * @param viewer
	 */
	public TableViewerCellModifier( RepositoryEditor editor ) {
		super();
		_editor = editor;
	}
	
	private void setDirty()
	{
		_editor.setDirty();
	}

	//private Column getTableRow( Object element )
	private AttributeInDbModel getTableRow( Object element )
	{
		//if ( element instanceof Column )
		if ( element instanceof AttributeInDbModel ) // v 3.0.0
		{
			//return (Column) element ;
			return (AttributeInDbModel) element ; // v 3.0.0
		}
		else
		{
			//MsgBox.error("TableViewerCellModifier.getTableRow(element) : the element is not an instance of Column !");
			MsgBox.error("TableViewerCellModifier.getTableRow(element) : the element is not an instance of AttributeInDbModel !");
			return null ;
		}
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
	 */
	public boolean canModify(Object element, String property) {
		log("canModify(..," + property + ")..." );
		if ( ColumnNames.JAVA_NAME.equals(property) ) return true;
		if ( ColumnNames.JAVA_TYPE.equals(property) ) return true;
		if ( ColumnNames.SPECIAL.equals(property) ) {
			//Column row = getTableRow( element );
			AttributeInDbModel row = getTableRow( element ); // v 3.0.0
			if ( row != null ) {
				return true ;
			}
		}
		return false ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
	 */
	public Object getValue(Object element, String property) 
	{
		log("getValue(element,'" + property + "')..." );
		
		//Column column = getTableRow( element );
		AttributeInDbModel column = getTableRow( element ); // v 3.0.0

		if ( column != null ) {
			//--- "Java Name" property --> String ( Text field value )
			if ( ColumnNames.JAVA_NAME.equals(property) ) {
				//return column.getJavaName();
				return column.getName(); // v 3.0.0
			}
			
			//--- "Java Type" property --> Integer ( ComboBox index ) 
			if ( ColumnNames.JAVA_TYPE.equals(property) ) {
				//String sJavaType = column.getJavaType();
				//String sJavaType = column.getFullType();  // v 3.0.0
				String sJavaType = column.getModelFullType();  // v 3.0.0
				if ( sJavaType != null ) {
					JavaTypes types = JavaTypesManager.getJavaTypes();
					int index = types.getTypeIndex( sJavaType );
					if ( index < 0 ) {
						MsgBox.error("getValue(element, '" + property + "') : "
								+ "\n Cannot found index for type '" + sJavaType + "' !");
						return (new Integer(0));
					}
					else {
						return new Integer(index); // OK
					}
				}
				else {
					MsgBox.error("getValue(element, '" + property + "') : "
							+ "\n 'Java Type' value is null !");
				}
				return (new Integer(0));
				
			}

			//--- "Special" property --> String ( Text field value )
			if ( ColumnNames.SPECIAL.equals(property) ) {
				//--- There's 4 cases of types with special further informations
				if ( column.isJavaTypeString() ) {
					log("getValue(..," + property + ") : return SpecialValueForString" );
					return new SpecialValueForString(column);
				}
				else if ( column.isJavaTypeDateOrTime() ) {
					log("getValue(..," + property + ") : return SpecialValueForDate" );
					return new SpecialValueForDate(column);
				}
				else if ( column.isJavaTypeBoolean() ) {
					log("getValue(..," + property + ") : return SpecialValueForBoolean" );
					return new SpecialValueForBoolean(column);					
				}
				else if ( column.isJavaTypeNumber() ) {
					log("getValue(..," + property + ") : return SpecialValueForNumber" );
					return new SpecialValueForNumber(column);					
				}
				PluginLogger.log(this, "getValue(..," + property + ") : return 'NULL'" );
				return null ;
			}			
		}
		return "?" ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void modify(Object element, String property, Object value) {
		log("modify(element, '" + property + "', value)..." );

		if ( element instanceof TableItem != true ) {
			MsgBox.error("modify(element, property, val) : the element is not an instance of TableItem !");
			return ;
		}

		TableItem tableItem = (TableItem) element ;
		//Column modelColumn = TableUtil.getTableColumn(tableItem);
		AttributeInDbModel modelColumn = TableUtil.getTableColumn(tableItem);
		
		//--- "Java Name" column
		if ( ColumnNames.JAVA_NAME.equals(property) ) 
		{
			log("modify(..," + property + ", " + value + ") : 'Java Name' column " );
			String sNewValue = getString( value );
			
			//--- Has it realy change ?
			//String sOldValue = modelColumn.getJavaName() ;
			String sOldValue = modelColumn.getName() ; // v 3.0.0
			if ( ! sNewValue.equals( sOldValue ) )
			{
				//--- Set the new value in the "model"  
				//modelColumn.setJavaName(sNewValue);
				modelColumn.setName(sNewValue); // v 3.0.0
				//--- Set the new value in the "view" ( TableItem cell )  
				tableItem.setText(ColumnNames.JAVA_NAME_INDEX, sNewValue); 
				//tableItem.setImage( WizardImages.getImage(WizardImages.SAMPLE) );
				setDirty();
			}
		}
		
		//--- "Java Type" column
		if ( ColumnNames.JAVA_TYPE.equals(property) ) {
			log("modify(..," + property + ", " + value 
					+ ") : 'Java Type' column ( original value = '" 
//					+ modelColumn.getFullType() + "' )");
					+ modelColumn.getModelFullType() + "' )");
			String sNewInput = "???";
			int iChoice = getInt( value );

			JavaTypes types = JavaTypesManager.getJavaTypes();
			
			// The text to print in the "UI view"
			sNewInput = types.getText(iChoice);
			// The full Java type 
			String sNewFullType = types.getType(iChoice);
			log("modify(..," + property + ", " + value + ") : Full type = " + sNewFullType );
			
			//--- Has it realy change ?
			//String sOldFullType = modelColumn.getJavaType() ;
			//String sOldFullType = modelColumn.getFullType() ; // v 3.0.0
			String sOldFullType = modelColumn.getModelFullType() ; // v 3.0.0
			if ( sNewFullType.equals( sOldFullType ) ) {
				log("modify(..," + property + ", " + value + ") : New value = Original value (no change) ");
				return ;
			}
			else {
				log("modify(..," + property + ", " + value + ") : has changed '" + sOldFullType 
						+ "' -> '" + sNewFullType + "' ");
			}

			updateJavaTypeInModel( modelColumn, sNewFullType );
			
			//--- Set the new value in the "view" ( TableItem cell )  
			tableItem.setText(ColumnNames.JAVA_TYPE_INDEX, sNewInput); 
			
			//tableItem.setText(ColumnNames.SPECIAL_INDEX, modelColumn.getSpecialTypeInfo() );
			refreshSpecialColumn( tableItem );

			setDirty();
		}
		
		//--- "Special" property --> String ( Text field value )
		if ( ColumnNames.SPECIAL.equals(property) ) 
		{
			log("modify(..," + property + ", " + value + ") : 'Special' column " );
			if ( value != null )
			{
				if ( value instanceof SpecialValue )
				{
					SpecialValue specialValue = (SpecialValue) value ;
					if ( specialValue.hasChanged() )
					{
						//--- Set the new value in the "view" ( TableItem cell )  
						//tableItem.setText(ColumnNames.SPECIAL_INDEX, specialValue.toString() ); 
						refreshSpecialColumn( tableItem );
						setDirty();
					}
				}
				else
				{
					// Happends for types without special values : byte[], Blob, Clob
					// Its not an error
					// MsgBox.error("TableViewerCellModifier.modify() : value is not an instance of SpecialValue");
				}
			}
			else
			{
				log("modify(..," + property + ", " + value + ") : 'Special' column : value is NULL => no change" );
				//--- Set the same text
				String s = tableItem.getText(ColumnNames.SPECIAL_INDEX ); 
				tableItem.setText(ColumnNames.SPECIAL_INDEX, s ); 
			}
		}
	}
	
	private void refreshSpecialColumn( TableItem tableItem )
	{
		//Column modelColumn = TableUtil.getTableColumn(tableItem);
		AttributeInDbModel modelColumn = TableUtil.getTableColumn(tableItem);
		if ( modelColumn != null ) 
		{
			tableItem.setText(ColumnNames.SPECIAL_INDEX, modelColumn.getSpecialTypeInfo() );
			Image image = TableUtil.getJavaNotNullImage(modelColumn) ;
			tableItem.setImage(ColumnNames.SPECIAL_INDEX, image );
		}
	}
	
	private String getString( Object oValue )
	{
		if ( oValue instanceof String ) {
			return (String) oValue ;
		}
		else {
			MsgBox.error("ERROR in modify(element, property, value) : "
					+ "\n The value is not an instance of String !"
					+ "\n Class is " + oValue.getClass().getName() );
			return "?" ;
		}
	}

	private int getInt( Object oValue )
	{
		log( "getInt(" + oValue + ")");
		if ( oValue instanceof Integer )
		{
			Integer integer = (Integer) oValue ;
			int v = integer.intValue();
			log("getInt(" + oValue + ") : " + v );
			return v ;
		}
		else
		{
			MsgBox.error("ERROR in modify(element, property, value) : "
					+ "\n The value is not an instance of Integer !"
					+ "\n Class is " + oValue.getClass().getName() );
			return 0 ;
		}
	}
	
	/**
	 * Update the model with the new Java Type after a user change
	 * ( update the javaType and special info if necessary )
	 * @param modelColumn
	 * @param sNewFullType
	 */
	//private void updateJavaTypeInModel( Column modelColumn, String sNewFullType )
	private void updateJavaTypeInModel( AttributeInDbModel modelColumn, String sNewFullType ) // v 3.0.0
	{
		//--- Set the original value in the "model"  
		//String sOldFullType = modelColumn.getJavaType() ;
		//String sOldFullType = modelColumn.getFullType() ;  // v 3.0.0
		String sOldFullType = modelColumn.getModelFullType() ;  // v 3.0.0

		//--- Set the new value in the "model"  
		//modelColumn.setJavaType(sNewFullType);
		//modelColumn.setFullType(sNewFullType);  // v 3.0.0
		modelColumn.setModelFullType(sNewFullType);  // v 3.0.0
		
		if ( JavaTypeUtil.getCategory(sOldFullType) != JavaTypeUtil.getCategory(sNewFullType) ) 
		{
			//--- Not the same type category => Reset the values
			resetFurtherInfo( modelColumn );
		}
		else if ( JavaTypeUtil.isCategoryDateOrTime(sNewFullType) ) 
		{
			//--- Same type category but DATE or TIME or DATE+TIME => Reset the values
			resetFurtherInfo( modelColumn );
		}
		// ELSE : keep the same values 		
	}
	
	//private void resetFurtherInfo( Column modelColumn )
	private void resetFurtherInfo( AttributeInDbModel modelColumn ) // v 3.0.0
	{
		modelColumn.clearSpecialTypeInfo();

		//String sFullJavaType = modelColumn.getJavaType() ;
		//String sFullJavaType = modelColumn.getFullType() ;
		String sFullJavaType = modelColumn.getModelFullType() ;
		int jdbcCode = modelColumn.getJdbcTypeCode();
		
		//--- Init with the default values depending on the Java Type and the JDBC Code
		if ( "java.util.Date".equals(sFullJavaType) ) {
			switch ( jdbcCode )
			{
				case Types.DATE :
					//modelColumn.setDateType( Column.SPECIAL_DATE_ONLY );
					modelColumn.setDateType( DateType.DATE_ONLY );
					break;
				case Types.TIME :
					//modelColumn.setDateType( Column.SPECIAL_TIME_ONLY );
					modelColumn.setDateType( DateType.TIME_ONLY );
					break;
				case Types.TIMESTAMP :
					//modelColumn.setDateType( Column.SPECIAL_DATE_AND_TIME );
					modelColumn.setDateType( DateType.DATE_AND_TIME );
					break;
				default :
					//modelColumn.setDateType( Column.SPECIAL_DATE_ONLY );
					modelColumn.setDateType( DateType.DATE_ONLY );
					break;
			}
		}
		else if ( modelColumn.isJavaTypeString() ) {
			if ( jdbcCode == Types.LONGVARCHAR || jdbcCode == Types.CLOB )
			{
				modelColumn.setLongText(true);
			}
		}
		else if ( modelColumn.isJavaTypeBoolean() ) {
			if ( jdbcCode != Types.BOOLEAN )
			{
				modelColumn.setBooleanTrueValue("1");
				modelColumn.setBooleanFalseValue("0");
			}
		}
	}
}
