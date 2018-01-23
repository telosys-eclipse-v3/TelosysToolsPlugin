package org.telosys.tools.eclipse.plugin.commons.dialogbox;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;

public class FolderSelectionDialogBox {

	private static String getDefaultRoot() {
		File[] roots = File.listRoots();;
		if ( roots != null && roots.length > 0 ) {
			return roots[0].getAbsolutePath() ;
		}
		
		File home = FileSystemView.getFileSystemView().getHomeDirectory();
		if ( home != null ) {
			return home.getAbsolutePath() ;
		}
		
		throw new RuntimeException("Cannot get filesystem root ");
	}
	
	public static String chooseFolder(Shell shell, String title) {
//		Shell shell = Util.getActiveWindowShell();
		return chooseFolder(shell, title, getDefaultRoot());
	}
	
//	public static String chooseFolder(String title, String filterPath) {
//		Shell shell = Util.getActiveWindowShell();
//		return chooseFolder(shell, title, filterPath);
//	}
	
	public static String chooseFolder(Shell shell, String title, String filterPath) {
		DirectoryDialog dialog = new DirectoryDialog(shell);
		
		// Initial filter path
		dialog.setFilterPath(filterPath); 
		
		// Change the title bar text
		//dialog.setText("SWT's DirectoryDialog");
		dialog.setText(title);
        
		// Customizable message displayed in the dialog
		// dialog.setMessage("Select a directory");
        
		String selectedFolder = dialog.open();
		
		return selectedFolder ;
	}
	
}
