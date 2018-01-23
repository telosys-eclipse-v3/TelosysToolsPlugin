package org.telosys.tools.eclipse.plugin.commons;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class Util {
	
//    private final static int RED   = 194;
//    private final static int GREEN = 208;
//    private final static int BLUE  = 245;
//        
//    public static Color getColor(Display display){
//        
//        return new Color(display, RED, GREEN, BLUE);
//    }

    //-----------------------------------------------------------------------------------------
    
    private final static Cursor CURSOR_WAIT  = new Cursor(null, SWT.CURSOR_WAIT);
    private final static Cursor CURSOR_ARROW = new Cursor(null, SWT.CURSOR_ARROW);
    
    public static Shell getActiveWindowShell()
    {
    	IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    	if ( window != null )
    	{
    		return window.getShell();
    	}
    	return null ;
    }
    
    public static Shell cursorWait() 
    {
		Shell shell = getActiveWindowShell();
		cursorWait(shell);
		return shell ;
    }
    public static void cursorWait(Shell shell) 
    {
    	if ( shell != null )
    	{
        	shell.setCursor(CURSOR_WAIT);
    	}
    }

    public static Shell cursorArrow() 
    {
		Shell shell = getActiveWindowShell();
		cursorArrow(shell);
		return shell ;
    }
    public static void cursorArrow(Shell shell) 
    {
    	if ( shell != null )
    	{
        	shell.setCursor(CURSOR_ARROW);
    	}
    }

    
    /**
     * Returns the standard font for a page title
     * @param device
     * @return
     */
    private static Font getPageTitleFont(Device device) 
    {
		FontData fontData = new FontData(); 
		fontData.setName("Arial");
		fontData.setHeight(12);
		// fontData.setStyle(0); // use constants BOLD, ...

		Font font = new Font(device, fontData);
		return font ;
    }
    
    private static Color getPageTitleColor(Device device) 
    {
		Color color = new Color(device, 10, 36, 106); // device, R, G, B
		return color ;
    }
    
    /**
     * Set the title of the page  
     * @param composite the composite where to put the label
     * @param text
     * @return the label control of the title
     */
    public static Label setPageTitle(Composite composite, String text) 
    {
    	Device device = composite.getDisplay();
    	
    	Font font = getPageTitleFont(device); 		
    	Color color = getPageTitleColor(device) ;

    	Label label = new Label(composite, SWT.NULL);
		label.setText(text);
		label.setForeground(color);		
		label.setFont(font);
    	
		return label ;
    }
    
    /**
     * Launch the external browser on the given URL
     * @param url
     */
    public static void launchExternalBrowser(String url) {
    	org.eclipse.swt.program.Program.launch(url);
    	// or 
    	// PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(url);
    }
}
