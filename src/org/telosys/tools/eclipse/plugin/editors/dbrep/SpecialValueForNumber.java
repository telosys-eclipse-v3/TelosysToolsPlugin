package org.telosys.tools.eclipse.plugin.editors.dbrep;

import java.math.BigDecimal;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.repository.model.AttributeInDbModel;


/**
 * Value for "SpecialDialogCellEditor" ( for column "Special" ) 
 *  
 * @author Laurent GUERIN
 *
 */
public class SpecialValueForNumber extends SpecialValue 
{
//	private String  _initialMinValue ;
//	private String  _initialMaxValue ;
	private BigDecimal  _initialMinValue ; // v 3.0.0
	private BigDecimal  _initialMaxValue ; // v 3.0.0
	
	private String  _initialFormat  ;
	
	//public SpecialValueForNumber( Column modelColumn ) 
	public SpecialValueForNumber( AttributeInDbModel modelColumn ) // v 3.0.0
	{
		super(modelColumn) ;
		
		//--- Keep initial values to know if something has changed, and to cancel changes
		_initialMinValue  =  _modelColumn.getMinValue()  ;
		_initialMaxValue  =  _modelColumn.getMaxValue()  ;
		
		_initialFormat    =  _modelColumn.getFormat()  ;
	}

	//---------------------------------------------------------------------
	public String getMinValue()
	{
		return emptyIfNull( _modelColumn.getMinValue() )  ;
	}
	public void setMinValue(String s)
	{
		log("setMin(" + s +")");
		//_modelColumn.setMinValue(s);
		//_modelColumn.setMinValue(StrUtil.getIntegerObject(s)); // v 3.0.0
		_modelColumn.setMinValue(StrUtil.getBigDecimalObject(s)); // v 3.0.0
	}
	
	//---------------------------------------------------------------------
	public String getMaxValue()
	{
		return emptyIfNull( _modelColumn.getMaxValue() ) ;
	}
	public void setMaxValue(String s)
	{
		log("setMax(" + s +")");
		//_modelColumn.setMaxValue(s);
		//_modelColumn.setMaxValue(StrUtil.getIntegerObject(s)); // v 3.0.0
		_modelColumn.setMaxValue(StrUtil.getBigDecimalObject(s)); // v 3.0.0
	}
	
	//---------------------------------------------------------------------
	public String getFormat()
	{
		return emptyIfNull ( _modelColumn.getFormat() ) ;
	}
	public void setFormat(String s)
	{
		log("setFormat(" + s +")");
		_modelColumn.setFormat(s);
	}
	
	//---------------------------------------------------------------------

	public boolean hasChanged() {
		if ( super.hasChanged() ) return true ;
		
//		if ( ! sameValue ( _initialMinValue, _modelColumn.getMinValue() ) ) return true ;
//		if ( ! sameValue ( _initialMaxValue, _modelColumn.getMaxValue() ) ) return true ;
		if ( _initialMinValue != _modelColumn.getMinValue() ) return true ; // v 3.0.0
		if ( _initialMaxValue != _modelColumn.getMaxValue() ) return true ; // v 3.0.0
		if ( ! sameValue ( _initialFormat,   _modelColumn.getFormat() ) ) return true ;

		return false ; // No change
	}

	public void cancelChanges() {
		super.cancelChanges();
		_modelColumn.setMinValue(_initialMinValue);
		_modelColumn.setMaxValue(_initialMaxValue);
		_modelColumn.setFormat (_initialFormat );
	}
}
