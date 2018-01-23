package org.telosys.tools.eclipse.plugin.editors.dbrep;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.repository.model.AttributeInDbModel;

/**
 * Value for "SpecialDialogCellEditor" ( for column "Special" ) 
 *  
 * @author Laurent GUERIN
 *
 */
public class SpecialValueForString extends SpecialValue 
{
	
	private boolean _initialLongText = false ;

	private boolean _initialNotEmpty = false ;
	private boolean _initialNotBlank = false ;
//	private String  _initialMinLength ;
//	private String  _initialMaxLength ;
	private Integer _initialMinLength ; // v 3.0.0
	private Integer _initialMaxLength ; // v 3.0.0
	private String  _initialPattern  ;
	
	//public SpecialValueForString( Column modelColumn ) 
	public SpecialValueForString( AttributeInDbModel modelColumn ) // v 3.0.0
	{
		super(modelColumn) ;
		//_initialLongText = _modelColumn.getLongText();
		_initialLongText = _modelColumn.isLongText(); // v 3.0.0
		//_initialNotEmpty = _modelColumn.getNotEmpty();
		_initialNotEmpty = _modelColumn.isNotEmpty(); // v 3.0.0
		//_initialNotBlank = _modelColumn.getNotBlank();
		_initialNotBlank = _modelColumn.isNotBlank(); // v 3.0.0
		_initialMinLength = _modelColumn.getMinLength();
		_initialMaxLength = _modelColumn.getMaxLength();
		_initialPattern   = _modelColumn.getPattern();
	}
	
	//-------------------------------------------------------------------------------
	public boolean isLongText() {
		//return _modelColumn.getLongText() ;
		return _modelColumn.isLongText() ; // v 3.0.0
	}
	public void setLongText(boolean b) {
		_modelColumn.setLongText(b) ;
	}
	
	//-------------------------------------------------------------------------------
	public boolean isNotEmpty() {
		//return _modelColumn.getNotEmpty() ;
		return _modelColumn.isNotEmpty() ; // v 3.0.0
	}
	public void setNotEmpty(boolean b) {
		_modelColumn.setNotEmpty(b) ;
	}
	
	//-------------------------------------------------------------------------------
	public boolean isNotBlank() {
		//return _modelColumn.getNotBlank() ;
		return _modelColumn.isNotBlank() ; // v 3.0.0
	}
	public void setNotBlank(boolean b) {
		_modelColumn.setNotBlank(b) ;
	}
	
	//-------------------------------------------------------------------------------
	public String getMinLength() {
		return emptyIfNull( _modelColumn.getMinLength() )  ;
	}
	public void setMinLength(String s) {
		//_modelColumn.setMinLength(s);
		_modelColumn.setMinLength(StrUtil.getIntegerObject(s)); // v 3.0.0
	}
	
	//-------------------------------------------------------------------------------
	public String getMaxLength() {
		return emptyIfNull( _modelColumn.getMaxLength() ) ;
	}
	public void setMaxLength(String s) {
		// _modelColumn.setMaxLength(s);
		_modelColumn.setMaxLength(StrUtil.getIntegerObject(s)); // v 3.0.0
	}
	
	//-------------------------------------------------------------------------------
	public String getPattern() {
		return emptyIfNull( _modelColumn.getPattern() ) ;
	}
	public void setPattern(String s) {
		_modelColumn.setPattern(s);
	}
	
	//-------------------------------------------------------------------------------
	public boolean hasChanged() {
		if ( super.hasChanged() ) return true ;
		
//		if ( _modelColumn.getLongText() != _initialLongText ) return true ;
//		if ( _modelColumn.getNotEmpty() != _initialNotEmpty ) return true ;
//		if ( _modelColumn.getNotBlank() != _initialNotBlank ) return true ;
		if ( _modelColumn.isLongText() != _initialLongText ) return true ; // v 3.0.0
		if ( _modelColumn.isNotEmpty() != _initialNotEmpty ) return true ; // v 3.0.0
		if ( _modelColumn.isNotBlank() != _initialNotBlank ) return true ; // v 3.0.0
		
//		if ( ! sameValue( _modelColumn.getMinLength() , _initialMinLength ) ) return true ;
		if ( _modelColumn.getMinLength() != _initialMinLength ) return true ; // v 3.0.0
//		if ( ! sameValue( _modelColumn.getMaxLength() , _initialMaxLength ) ) return true ;
		if ( _modelColumn.getMaxLength() != _initialMaxLength ) return true ; // v 3.0.0
		if ( ! sameValue( _modelColumn.getPattern() ,   _initialPattern ) ) return true ;
		
		return false ; // No change

	}
	
	//-------------------------------------------------------------------------------
	public void cancelChanges() {
		super.cancelChanges();
		
		_modelColumn.setLongText(_initialLongText);
		_modelColumn.setNotEmpty(_initialNotEmpty);
		_modelColumn.setNotBlank(_initialNotBlank);

		_modelColumn.setMinLength(_initialMinLength);
		_modelColumn.setMaxLength(_initialMaxLength);
		_modelColumn.setPattern(_initialPattern);
	}

}
