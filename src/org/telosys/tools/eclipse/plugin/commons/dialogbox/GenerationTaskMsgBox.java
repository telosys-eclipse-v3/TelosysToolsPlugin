package org.telosys.tools.eclipse.plugin.commons.dialogbox;

import org.eclipse.jface.dialogs.MessageDialog;

/**
 * @author Laurent GUERIN
 *
 */
public class GenerationTaskMsgBox
{
	//----------------------------------------------------------------------------------
	public static boolean error(String title, String message, Throwable exception) 
	{
		MessageDialog dialog = new MessageDialog(
				null, 
				title, 
				null, 
				buildMessage(message, exception),
				MessageDialog.ERROR, 
				new String[]{ "Continue", "Cancel" }, 
				0); 
		int r = dialog.open();
		return r == 0; // TRUE if "continue", else FALSE
	}
	
	private static String buildMessage(String message, Throwable exception) {
		StringBuffer sb = new StringBuffer();
		sb.append(message);
		sb.append("\n");
		if ( exception != null ) {
			if ( exception.getCause() != null ) {
				Throwable cause = exception.getCause() ;
				//sb.append("Exception : ") ;
				sb.append(cause.getClass().getSimpleName() ) ;
				sb.append(" : \n");
				sb.append(cause.getMessage() ) ;
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}
