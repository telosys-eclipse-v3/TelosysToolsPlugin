package org.telosys.tools.eclipse.plugin.commons.dialogbox;

import org.eclipse.jface.dialogs.MessageDialog;
import org.telosys.tools.generator.task.ErrorReport;

/**
 * @author Laurent GUERIN
 *
 */
public class GenerationTaskMsgBox {
	
	private static final String NEW_LINE = "\n" ;
	
	/**
	 * Private constructor
	 */
	private GenerationTaskMsgBox() {
	}
	
	//----------------------------------------------------------------------------------
	// OLD
	//----------------------------------------------------------------------------------
//	public static boolean error(String title, String message, Throwable exception) 
//	{
//		MessageDialog dialog = new MessageDialog(
//				null,   // Shell parentShell
//				title,  // String dialogTitle, 
//				null,   // Image dialogTitleImage, 
//				buildMessage(message, exception),     // String dialogMessage
//				MessageDialog.ERROR,                  // int dialogImageType 
//				new String[]{ "Continue", "Cancel" }, // String[] dialogButtonLabels
//				0);                                   // int defaultIndex
//		int r = dialog.open();
//		return r == 0; // TRUE if "continue", else FALSE
//	}
//		
//	private static String buildMessage(String message, Throwable exception) {
//		StringBuffer sb = new StringBuffer();
//		sb.append(message);
//		sb.append("\n");
//		if ( exception != null ) {
//			if ( exception.getCause() != null ) {
//				Throwable cause = exception.getCause() ;
//				//sb.append("Exception : ") ;
//				sb.append(cause.getClass().getSimpleName() ) ;
//				sb.append(" : \n");
//				sb.append(cause.getMessage() ) ;
//				sb.append("\n");
//			}
//		}
//		return sb.toString();
//	}
	
	//----------------------------------------------------------------------------------
	// New in Ver 3.3.0
	//----------------------------------------------------------------------------------
	public static boolean error(ErrorReport errorReport) {
		String message = buildMessage(errorReport) ;
		MessageDialog dialog = new MessageDialog(
				null,               // Shell parentShell
				"Generation error", // String dialogTitle, 
				null,               // Image dialogTitleImage, 
				message,            // String dialogMessage
				MessageDialog.ERROR,                  // int dialogImageType 
				new String[]{ "Continue", "Cancel" }, // String[] dialogButtonLabels
				0);                                   // int defaultIndex
		int r = dialog.open();
		return r == 0; // TRUE if "continue", else FALSE
	}
	
	private static String buildMessage(ErrorReport errorReport) {
		StringBuilder sb = new StringBuilder();
		sb.append( errorReport.getErrorMessage() );
		sb.append(NEW_LINE);
		sb.append(NEW_LINE);
		for ( String s : errorReport.getErrorDetails() ) {
			sb.append(s);
			sb.append(NEW_LINE);
		}
		return sb.toString();
	}
}
