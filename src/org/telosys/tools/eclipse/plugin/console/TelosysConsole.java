package org.telosys.tools.eclipse.plugin.console;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

public class TelosysConsole {

	private final MessageConsole console ;
	private final MessageConsoleStream out ;
	//private boolean revealed = false ;
	
	public TelosysConsole(String consoleName) {
		super();
		this.console = getConsole(consoleName);
		this.out = console.newMessageStream();
		PluginLogger.log("TelosysConsole created");
	}

	private MessageConsole getConsole(String consoleName) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager consoleManager = plugin.getConsoleManager();
		IConsole[] existingConsoles = consoleManager.getConsoles();
		for (int i = 0; i < existingConsoles.length; i++) {
			if (consoleName.equals(existingConsoles[i].getName())) {
				return (MessageConsole) existingConsoles[i];
			}
		}
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(consoleName, null);
		consoleManager.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}

	public MessageConsoleStream getStream() {
		return out;
	}

	public void clear() {
		console.clearConsole();
	}
	
	public void print(String message) {
		//if ( ! revealed ) reveal();
		out.print(message);
	}
	
	public void println(String message) {
		//if ( ! revealed ) reveal();
		out.println(message);
	}
	
	public void reveal() {
		// obtain the active page
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
		
		try {
			IConsoleView view = (IConsoleView) workbenchPage.showView(IConsoleConstants.ID_CONSOLE_VIEW);
			view.display(console);
			//revealed = true ;
		} catch (PartInitException e) {
			MsgBox.error("Cannot reveal the console (PartInitException)");
		}
	}
}
