package org.telosys.tools.eclipse.plugin.commons;


/**
 * Basic utility class for plugin logging (only for DEBUG) 
 * 
 * @author Laurent GUERIN
 *
 */
public class PluginLogger
{
//	private final static TelosysToolsLogger logger = new ConsoleLogger();

	private static void print(String logLevel, String msg) {
		System.out.println("[" + logLevel + "] " + msg );
	}

	//----------------------------------------------------------------------------------
	// INFO
	//----------------------------------------------------------------------------------
//	public static void info(String s) {
//		logger.info(s);
//	}

	//----------------------------------------------------------------------------------
	// ERROR
	//----------------------------------------------------------------------------------
//	public static void error(String s) {
//		logger.error(s);
//	}
	
	//----------------------------------------------------------------------------------
	// LOG
	//----------------------------------------------------------------------------------
	public static void log(Object object, String msg) {
		if ( PluginLoggerConfig.LOG ) {
			if ( object != null ) {
				log(object.getClass(), msg);
			}
			else {
				log(msg);
			}
		}
	}
	public static void log(Class<?> clazz, String msg) {
		if ( PluginLoggerConfig.LOG ) {
			String msg2 = msg ;
			if ( clazz != null ) {
				msg2 = clazz.getSimpleName() + " : " + msg ;
			}
			print("LOG", msg2);
		}
	}
	//----------------------------------------------------------------------------------
	public static void log(String msg) {
		if ( PluginLoggerConfig.LOG ) {
			print("LOG", msg);
		}
	}

	//----------------------------------------------------------------------------------
	// DEBUG
	//----------------------------------------------------------------------------------
	public static void debug(String msg) {
		if ( PluginLoggerConfig.DEBUG ) {
			print("DEBUG", msg);
		}
	}
	
}
