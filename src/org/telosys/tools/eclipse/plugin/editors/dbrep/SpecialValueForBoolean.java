package org.telosys.tools.eclipse.plugin.editors.dbrep;

import org.telosys.tools.repository.model.AttributeInDbModel;



/**
 * Value for "SpecialDialogCellEditor" ( for column "Special" ) 
 *  
 * @author Laurent GUERIN
 *
 */
public class SpecialValueForBoolean extends SpecialValue 
{

	private String _initialTrueValue  = "" ;
	
	private String _initialFalseValue = "" ;
	
	//public SpecialValueForBoolean( Column modelColumn ) 
	public SpecialValueForBoolean( AttributeInDbModel modelColumn )  // v 3.0.0
	{
		super(modelColumn) ;
		_initialTrueValue  = _modelColumn.getBooleanTrueValue(); // never null 
		_initialFalseValue = _modelColumn.getBooleanFalseValue(); // never null 
	}

//	public int getJdbcTypeCode()
//	{
//		return _modelColumn.getJdbcTypeCode() ;
//	}
	
//	public String getDatabaseType()
//	{
//		//return _tableRow.getDatabaseType() ;
//		return _modelColumn.getDatabaseTypeName() ;
//	}
	

	public String getTrueValue()
	{
		return _modelColumn.getBooleanTrueValue();
	}
	
	public String getFalseValue()
	{
		return _modelColumn.getBooleanFalseValue();
	}
	

	public void setTrueValue(String v)
	{
		_modelColumn.setBooleanTrueValue(v);
	}

	public void setFalseValue(String v)
	{
		_modelColumn.setBooleanFalseValue(v);
	}

	
	public boolean hasChanged()
	{
		if ( super.hasChanged() ) return true ;
		
//		if ( ! _initialTrueValue.equals (_modelColumn.getBooleanTrueValue())  ) return true ; // changed
//		if ( ! _initialFalseValue.equals(_modelColumn.getBooleanFalseValue()) ) return true ; // changed
		if ( ! sameValue ( _initialTrueValue,  _modelColumn.getBooleanTrueValue()  ) ) return true ; // changed
		if ( ! sameValue ( _initialFalseValue, _modelColumn.getBooleanFalseValue() ) ) return true ; // changed

		return false ;
	}

	public void cancelChanges()
	{
		super.cancelChanges();
		
		_modelColumn.setBooleanTrueValue (_initialTrueValue);
		_modelColumn.setBooleanFalseValue(_initialFalseValue);
	}
}
