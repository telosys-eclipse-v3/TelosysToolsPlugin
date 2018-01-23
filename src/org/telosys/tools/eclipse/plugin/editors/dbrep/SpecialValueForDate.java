package org.telosys.tools.eclipse.plugin.editors.dbrep;

import org.telosys.tools.generic.model.DateType;
import org.telosys.tools.repository.model.AttributeInDbModel;


/**
 * Value for "SpecialDialogCellEditor" ( for column "Special" ) 
 *  
 * @author Laurent GUERIN
 *
 */
public class SpecialValueForDate extends SpecialValue 
{
	//private String _initialDateType = null ;
	private DateType _initialDateType = null ; // v 3.0.0
	

//	private boolean _initialNotNull ;
	
	private boolean _initialDatePast  ;
	private boolean _initialDateFuture  ;
	private boolean _initialDateBefore  ;
	private boolean _initialDateAfter  ;

	private String _initialDateBeforeValue = null ;
	private String _initialDateAfterValue = null ;
	
//	public SpecialValueForDate( Column modelColumn ) 
	public SpecialValueForDate( AttributeInDbModel modelColumn ) // v 3.0.0
	{
		super(modelColumn) ;
		//--- Keep initial values to know if something has changed, and to cancel changes
		//_initialDateType        = _modelColumn.getDateType();
		_initialDateType        = _modelColumn.getDateType();
		
//		_initialNotNull         = _modelColumn.getJavaNotNull();
		
		_initialDatePast        = _modelColumn.isDatePast()  ;
		_initialDateFuture      = _modelColumn.isDateFuture()  ;
		
		_initialDateBefore      = _modelColumn.isDateBefore()  ;
		_initialDateBeforeValue = _modelColumn.getDateBeforeValue()  ;
		
		_initialDateAfter       = _modelColumn.isDateAfter()  ;
		_initialDateAfterValue  = _modelColumn.getDateAfterValue()  ;
	}

//	private void log(String s)
//	{
//		PluginLogger.log( this, s );
//	}
	
	//---------------------------------------------------------------------

	public boolean isDateOnly() {
		//return Column.SPECIAL_DATE_ONLY.equals( _modelColumn.getDateType() ) ;
		return _modelColumn.getDateType() == DateType.DATE_ONLY ; // v 3.0.0
	}
	public void setDateOnly() {
		//_modelColumn.setDateType(Column.SPECIAL_DATE_ONLY);
		_modelColumn.setDateType(DateType.DATE_ONLY); // v 3.0.0
	}

	public boolean isTimeOnly() {
		//return Column.SPECIAL_TIME_ONLY.equals( _modelColumn.getDateType() ) ;
		return _modelColumn.getDateType() == DateType.TIME_ONLY ; // v 3.0.0
	}
	public void setTimeOnly() {
		//_modelColumn.setDateType(Column.SPECIAL_TIME_ONLY);
		_modelColumn.setDateType(DateType.TIME_ONLY); // v 3.0.0
	}

	public boolean isDateAndTime() {
		//return Column.SPECIAL_DATE_AND_TIME.equals( _modelColumn.getDateType() ) ;
		return _modelColumn.getDateType() == DateType.DATE_AND_TIME ; // v 3.0.0
	}
	public void setDateAndTime() {
		//_modelColumn.setDateType(Column.SPECIAL_DATE_AND_TIME);
		_modelColumn.setDateType(DateType.DATE_AND_TIME); // v 3.0.0
	}

	//---------------------------------------------------------------------
	public void setDateValidationNotNull(boolean b)
	{
		log("setDateValidationNotNull(" + b +")");
		//_modelColumn.setJavaNotNull(b);
		_modelColumn.setNotNull(b); // v 3.0.0
	}

//	public void setDateValidationNone()
//	{
//		_modelColumn.setDatePast(false);
//		_modelColumn.setDateFuture(false);
//	}
	public void setDateValidationPast(boolean b)
	{
		log("setDateValidationPast(" + b +")");
		_modelColumn.setDatePast(b);
	}
	public void setDateValidationFuture(boolean b)
	{
		log("setDateValidationFuture(" + b +")");
		_modelColumn.setDateFuture(b);
	}

	public void setDateValidationBefore(boolean b, String s)
	{
		log("setDateValidationBefore(" + b + "," + s +")");
		_modelColumn.setDateBefore(b);
		_modelColumn.setDateBeforeValue(s);
	}
	
	public void setDateValidationAfter(boolean b, String s)
	{
		log("setDateValidationAfter(" + b + "," + s +")");
		_modelColumn.setDateAfter(b);
		_modelColumn.setDateAfterValue(s);
	}
	
	//---------------------------------------------------------------------
	
	public boolean isDateValidationNotNull()
	{
		//return _modelColumn.getJavaNotNull() ;
		return _modelColumn.isNotNull() ;
	}
	public boolean isDateValidationPast()
	{
		log("isDateValidationPast() : " + _modelColumn.isDatePast() );
		return _modelColumn.isDatePast()  ;
	}
	public boolean isDateValidationFuture()
	{
		log("isDateValidationFuture() : " + _modelColumn.isDateFuture() );
		return _modelColumn.isDateFuture()  ;
	}
	public boolean isDateValidationBefore()
	{
		return _modelColumn.isDateBefore()  ;
	}
	public boolean isDateValidationAfter()
	{
		return _modelColumn.isDateAfter()  ;
	}
	public String getDateValidationBefore()
	{
		return _modelColumn.getDateBeforeValue()  ;
	}
	public String getDateValidationAfter()
	{
		return _modelColumn.getDateAfterValue() ;
	}
	
	//---------------------------------------------------------------------

	public boolean hasChanged()
	{
		if ( super.hasChanged() ) return true ;
		
		//if ( ! StrUtil.identical ( _initialDateType , _modelColumn.getDateType() ) ) return true ;
		if ( _initialDateType != _modelColumn.getDateType() ) return true ; // v 3.0.0
			
		if ( _initialDatePast   != _modelColumn.isDatePast()  ) return true ;
		if ( _initialDateFuture != _modelColumn.isDateFuture()  ) return true ;
		if ( _initialDateBefore != _modelColumn.isDateBefore() ) return true ;
		if ( ! sameValue ( _initialDateBeforeValue, _modelColumn.getDateBeforeValue() ) ) return true ;
		if ( _initialDateAfter  != _modelColumn.isDateAfter()  ) return true ;
		if ( ! sameValue (_initialDateAfterValue, _modelColumn.getDateAfterValue() ) ) return true ;

		return false ; // No change
	}

	public void cancelChanges()
	{
		super.cancelChanges();
		
		_modelColumn.setDateType(_initialDateType);
		
//		_modelColumn.setJavaNotNull(_initialNotNull);
		
		_modelColumn.setDatePast(_initialDatePast);
		_modelColumn.setDateFuture(_initialDateFuture);
		_modelColumn.setDateBefore(_initialDateBefore);
		_modelColumn.setDateBeforeValue(_initialDateBeforeValue);
		_modelColumn.setDateAfter(_initialDateAfter);
		_modelColumn.setDateAfterValue(_initialDateAfterValue);
	}
}
