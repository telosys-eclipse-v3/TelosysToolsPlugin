package org.telosys.tools.eclipse.plugin.commons;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.telosys.tools.commons.ExceptionUtil;

/**
 * @author Laurent GUERIN
 *
 */
public class MsgBox
{
	private final static boolean DEBUG = true ;
	
	//----------------------------------------------------------------------------------
	// ERROR
	//----------------------------------------------------------------------------------
	public static void error(String title, String message) {
		MessageDialog dialog = new MessageDialog(null, title, null, 
				message, MessageDialog.ERROR, new String[]{IDialogConstants.OK_LABEL}, 0); 
		dialog.open();
		return;
	}
	//----------------------------------------------------------------------------------
	public static void error(String message) {
	    error(" Error ", message);
	}
	//----------------------------------------------------------------------------------
	public static void error(String title, String message, Throwable e) {
	    error( title, message + "\n\n" + ExceptionUtil.getExceptionSummary(e) );
	}
	//----------------------------------------------------------------------------------
	public static void error(String message, Throwable e) {
	    error(" Error ", message + "\n\n" + ExceptionUtil.getExceptionSummary(e) );
	}
	
	//----------------------------------------------------------------------------------
	// INFO
	//----------------------------------------------------------------------------------
	public static void info(String title,String message) {
		MessageDialog dialog = new MessageDialog(null, title, null, 
				message, MessageDialog.INFORMATION, new String[]{IDialogConstants.OK_LABEL}, 0);
		dialog.open();
		return;
	}
	//----------------------------------------------------------------------------------
	public static void info(String message) {
	    info(" Information ", message);
	}

	//----------------------------------------------------------------------------------
	// WARNING
	//----------------------------------------------------------------------------------
	public static void warning(String title,String message) {
		MessageDialog dialog = new MessageDialog(null, title, null, 
				message, MessageDialog.WARNING, new String[]{IDialogConstants.OK_LABEL}, 0);
		dialog.open();
		return;
	}
	//----------------------------------------------------------------------------------
	public static void warning(String message) {
	    warning(" Warning ", message);
	}
	
	//----------------------------------------------------------------------------------
	// CONFIRM
	//----------------------------------------------------------------------------------
	public static boolean confirm(String title, String message) {
		return MessageDialog.openConfirm(null, title, message);
	}
	public static boolean confirm(String message) {
		return confirm( "Confirm", message);
	}

	//----------------------------------------------------------------------------------
	// DEBUG
	//----------------------------------------------------------------------------------
	public static void debug(String message) {
	    if ( DEBUG ) info(" DEBUG ", message);
	}
}
