package org.telosys.tools.eclipse.plugin.console;

import org.telosys.tools.commons.observer.TaskObserver2;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

public class DbModelObserver implements TaskObserver2<Integer, String> {

	private final TelosysConsole console  ;
	
	public DbModelObserver() {
		super();
		console = DbModelConsole.getConsole();
		PluginLogger.log("DbModelObserver created");
	}
	
	@Override
	public void notify(Integer level, String msg) {
		int n = level ;
		if ( n < 0 ) n = 0 ;
		if ( n > 10 ) n = 10 ;
		print( repeat(' ', n) + msg);
		
	}
	
	private String repeat(char c, int n) {
		char[] chars = new char[n];
		for ( int i=0 ; i < n ; i++) {
			chars[i] = c ;
		}
		return new String(chars);
	}
	
	private void print(String msg) {
		console.println(msg );
	}

}
