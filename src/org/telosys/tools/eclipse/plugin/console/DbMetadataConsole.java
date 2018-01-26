package org.telosys.tools.eclipse.plugin.console;

public class DbMetadataConsole {

	private final static String  CONSOLE_NAME = "Telosys Database Metadata" ;
	private final static TelosysConsole console = new TelosysConsole(CONSOLE_NAME) ;
	
	/**
	 * Returns the console instance
	 * @return
	 */
	public static final TelosysConsole getConsole() {
		return console ;
	}
	
	/**
	 * Reset the console (reveal, clear and print the given message)
	 * @param message
	 */
	public static final void reset(String message) {
		console.reveal();
		console.clear();
		console.println(message);	
	}

	/**
	 * Reset the console (reveal and clear )
	 * @param message
	 */
	public static final void reset() {
		console.reveal();
		console.clear();
	}
}
