package org.telosys.tools.eclipse.plugin.commons;

import org.telosys.tools.commons.logger.GenericLogger;

/**
 *  
 * 
 * @author Laurent GUERIN
 *
 */
public class TextWidgetLogger extends GenericLogger {
	
	private StringBuilder  stringBuilder = new StringBuilder() ;
	
	//----------------------------------------------------------------------------------
	public TextWidgetLogger() {
		super();
	}
	//----------------------------------------------------------------------------------
	protected void print(String s) {
		stringBuilder.append( s + "\n");
	}
	//----------------------------------------------------------------------------------
	public String getContent() {
		return stringBuilder.toString();
	}
	//----------------------------------------------------------------------------------
	public void clear() {
		stringBuilder = new StringBuilder() ;
	}

}
