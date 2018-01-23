package org.telosys.tools.eclipse.plugin.editors.dbrep;

import java.math.BigDecimal;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.repository.model.AttributeInDbModel;


/**
 * Value for "SpecialDialogCellEditor" ( for column "Special" ) 
 *  
 * @author Laurent GUERIN
 *
 */
public abstract class SpecialValue 
{

	//protected Column _modelColumn = null ;
	protected AttributeInDbModel _modelColumn = null ; // V 3.0.0
	
	//--- Initial values 
	private final boolean  _initialNotNull ;
	
	private final String   _initialDefaultValue ;

	private final String   _initialLabel ;

	private final String   _initialInputType ;


	//---------------------------------------------------------------------
	/**
	 * Constructor
	 * @param modelColumn
	 */
//	protected SpecialValue( Column modelColumn ) 
	protected SpecialValue( AttributeInDbModel modelColumn ) // V 3.0.0
	{
		_modelColumn    = modelColumn ;
		//--- Keep initial values 
		//_initialNotNull      = _modelColumn.getJavaNotNull();
		_initialNotNull      = _modelColumn.isNotNull(); // V 3.0.0
		//_initialDefaultValue = _modelColumn.getJavaDefaultValue(); 
		_initialDefaultValue = _modelColumn.getDefaultValue();  // V 3.0.0
		_initialLabel        = _modelColumn.getLabel() ;
		_initialInputType    = _modelColumn.getInputType() ;
		
	}
	
	//---------------------------------------------------------------------
	protected void log(String s) {
		PluginLogger.log( this, s );
	}

	//---------------------------------------------------------------------
	public boolean isPrimitiveType()
	{
		return _modelColumn.isJavaPrimitiveType() ;
		//return JavaTypeUtil.isPrimitiveType( _modelColumn.getJavaType() );
	}

	//---------------------------------------------------------------------
	public String getJavaType() 
	{
		//return _modelColumn.getJavaType() ;
		//return _modelColumn.getFullType() ; // v 3.0.0
		return _modelColumn.getModelFullType() ; // v 3.0.0
	}
	
	//---------------------------------------------------------------------
	// NOT NULL
	//---------------------------------------------------------------------
	public boolean isNotNull()
	{
		//return _modelColumn.getJavaNotNull() ;
		return _modelColumn.isNotNull() ; // v 3.0.0
	}
	public void setNotNull(boolean b)
	{
		//_modelColumn.setJavaNotNull(b);
		_modelColumn.setNotNull(b);  // v 3.0.0
	}
	
	//---------------------------------------------------------------------
	// DEFAULT VALUE
	//---------------------------------------------------------------------
	public String getDefaultValue()
	{
		//return emptyIfNull( _modelColumn.getJavaDefaultValue() ) ;
		return emptyIfNull( _modelColumn.getDefaultValue() ) ; // v 3.0.0
	}
	public void setDefaultValue(String v)
	{
		//_modelColumn.setJavaDefaultValue(v);
		_modelColumn.setDefaultValue(v); // v 3.0.0
	}

	//---------------------------------------------------------------------
	// LABEL
	//---------------------------------------------------------------------
	public String getLabel()
	{
		return emptyIfNull( _modelColumn.getLabel() ) ;
	}
	public void setLabel(String v)
	{
		_modelColumn.setLabel(v);
	}

	//---------------------------------------------------------------------
	// INPUT TYPE
	//---------------------------------------------------------------------
	public String getInputType()
	{
		return emptyIfNull( _modelColumn.getInputType() ) ;
	}
	public void setInputType(String v)
	{
		_modelColumn.setInputType(v);
	}

	//---------------------------------------------------------------------
	/**
	 * Returns true if at least one of the values hold by this class has changed
	 * @return
	 */
	protected boolean hasChanged()
	{
		//if ( _initialNotNull      != _modelColumn.getJavaNotNull() ) return true ;
		if ( _initialNotNull      != _modelColumn.isNotNull() ) return true ; // v 3.0.0
		//if ( StrUtil.different(_initialDefaultValue, _modelColumn.getJavaDefaultValue() ) ) return true ;
		if ( StrUtil.different(_initialDefaultValue, _modelColumn.getDefaultValue() ) ) return true ; // v 3.0.0
		if ( StrUtil.different(_initialLabel,        _modelColumn.getLabel()            ) ) return true ;
		if ( StrUtil.different(_initialInputType,    _modelColumn.getInputType()        ) ) return true ;
		return false ;
	}
	
	//---------------------------------------------------------------------
	/**
	 * Cancel the changes : restore the initial values hold by this class
	 */
	protected void cancelChanges()
	{
		//_modelColumn.setJavaNotNull(_initialNotNull);
		_modelColumn.setNotNull(_initialNotNull); // v 3.0.0
		//_modelColumn.setJavaDefaultValue(_initialDefaultValue);
		_modelColumn.setDefaultValue(_initialDefaultValue); // v 3.0.0
		_modelColumn.setLabel(_initialLabel );
		_modelColumn.setInputType(_initialInputType);
	}
	
	//---------------------------------------------------------------------
	/**
	 * Returns the given string or "" if the string is null
	 * @param s
	 * @return
	 */
	protected String emptyIfNull(String s)
	{
		return s != null ? s : "" ;
	}
	protected String emptyIfNull(Integer value)
	{
		return value != null ? value.toString() : "" ;
	}
	protected String emptyIfNull(BigDecimal value)
	{
		return value != null ? value.toString() : "" ;
	}
	
	//---------------------------------------------------------------------
	/**
	 * Return true if the significant values are identical <br>
	 * A null string is considered as a void string
	 * @param s1
	 * @param s2
	 * @return
	 */
	protected boolean sameValue(String s1, String s2 )
	{
		String s1bis = emptyIfNull(s1);
		String s2bis = emptyIfNull(s2);
		return s1bis.equals(s2bis);
	}
	

	//---------------------------------------------------------------------
	public String toString() 
	{
		return _modelColumn.getSpecialTypeInfo();
	}	

}
