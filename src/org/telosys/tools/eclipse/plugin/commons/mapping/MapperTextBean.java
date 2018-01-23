package org.telosys.tools.eclipse.plugin.commons.mapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Text;

public class MapperTextBean<T> {
	
	private final static String ERROR_SEARCH_SETTER = "Cannot get setter method " ;
	private final static String ERROR_INVOKE_SETTER = "Cannot invoke setter " ;
	//private final static String INVALID_INTEGER = "Invalid integer (cannot parse) " ;

	private final Text     text ;
	private final Class<T> beanClass ;
	private final String   methodName ;
	private final Class<?> propertyType ;
	
	private Method   method ;
	
	public MapperTextBean(Text text, Class<T> beanClass, String methodName, Class<?> propertyType ) {
		super();
		if ( null == text ) throw new IllegalArgumentException("Text is null");
		if ( null == beanClass ) throw new IllegalArgumentException("Class is null");
		if ( null == methodName ) throw new IllegalArgumentException("Method name is null");
		this.text = text;
		this.beanClass = beanClass;
		this.methodName = methodName.trim();
		this.propertyType = propertyType;
		
		//--- Search the setter method
		Class<?>[] arguments = new Class<?>[1] ;
		arguments[0] = propertyType ;
		try {
			this.method = beanClass.getMethod(this.methodName, arguments ); 
		} catch (SecurityException e) {
			throw new RuntimeException(ERROR_SEARCH_SETTER + methodName + "(SecurityException)", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(ERROR_SEARCH_SETTER + methodName + "(NoSuchMethodException)", e);
		}		
	}

	public Class<T> getBeanClass() {
		return beanClass;
	}

	public String getMethodName() {
		return methodName;
	}

	public Class<?> getPropertyType() {
		return propertyType;
	}

	public void updateBean(T bean) throws Exception {
		
		//--- Text value to be set in the bean
		Object params[] = new Object[1];		
		if ( String.class.equals(propertyType) ) {
			//--- String 
			params[0] = text.getText() ;			
		}
		else if ( int.class.equals(propertyType) ) {
			//--- int 
			params[0] = toInt( text.getText() ) ;						
		}
		else {
			//--- Not supported 
			throw new Exception("Type '" + propertyType.getName() + "' not supported");
		}
		
		
		try {
			this.method.invoke(bean, params);
		} catch (IllegalArgumentException e) {
			throw new Exception(ERROR_INVOKE_SETTER + methodName, e);
		} catch (IllegalAccessException e) {
			throw new Exception(ERROR_INVOKE_SETTER + methodName, e);
		} catch (InvocationTargetException e) {
			throw new Exception(ERROR_INVOKE_SETTER + methodName, e);
		}
		
	}

	private int toInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return 0 ;
		}
	}
}
