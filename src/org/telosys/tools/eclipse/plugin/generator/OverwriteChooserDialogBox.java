package org.telosys.tools.eclipse.plugin.generator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.telosys.tools.commons.io.OverwriteChooser;

public class OverwriteChooserDialogBox implements OverwriteChooser {
	
	private Integer globalChoice = null ;

	private final static String[] buttons = {"Yes",  "Yes To All",  "No",  "No To All",  "Cancel" };

	public OverwriteChooserDialogBox() {
		super();
	}

	public int choose(String fileName ) 
	{
		String message = "Overwrite file \n"
			+ "'" + fileName + "' ?" ;
		return openDialogBox( message) ;
	}

	public int choose(String file, String folder ) 
	{
		String message = "Overwrite file '" + file + "' \n"
			+ "in folder '" + folder + "'  ? ";
		
		return openDialogBox( message) ;
	}
	
	private int openDialogBox(String message) 
	{
		if ( globalChoice != null ) {
			return globalChoice ;
		}
		
		Shell shell = null ;
		MessageDialog dialog = new MessageDialog(
				shell, 
				"Question ", // Title
				null, // Image
				message, // Message in the dialog box body
			    MessageDialog.QUESTION, // Image type ( icon : ERROR, WARNING, ... )
			    buttons, // Buttons labels
			    0); // Default index
		int r = dialog.open();
		
		switch(r) {
		case 0 : // YES 
			return OverwriteChooser.YES ;
		case 1 : // YES FOR ALL
			globalChoice = OverwriteChooser.YES; 
			return globalChoice ;
		case 2 : // NO
			return OverwriteChooser.NO ;
		case 3 : // NO FOR ALL
			globalChoice = OverwriteChooser.NO; 
			return globalChoice ;
		case 4 : // CANCEL
			return OverwriteChooser.CANCEL ;
		default :
			throw new RuntimeException("Unexpected choice " + r);
		}
	}

}
