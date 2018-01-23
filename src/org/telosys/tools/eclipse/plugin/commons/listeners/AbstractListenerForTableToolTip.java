package org.telosys.tools.eclipse.plugin.commons.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public abstract class AbstractListenerForTableToolTip implements Listener { 

	private final Table table;

	private Shell tip = null;

	private TableItem currentTableItem = null;
	private Point     currentPoint = null ;

	//---------------------------------------------------------------------------------------------
	public AbstractListenerForTableToolTip(Table table) {
		super();
		this.table = table;
	}
	
	//---------------------------------------------------------------------------------------------
	protected abstract String getToolTipInfo(TableItem tableItem) ;

	//---------------------------------------------------------------------------------------------
	private void log(String msg) {
		//PluginLogger.log(this, msg);
	}

	//---------------------------------------------------------------------------------------------
	private void logEvent(Event event) {
		String msg = "???" ;
		switch(event.type) {
		case SWT.Dispose: msg = "Dispose" ; break ;
		case SWT.MouseMove: msg = "MouseMove" ; break ;
		case SWT.MouseHover: msg = "MouseHover" ; break ;
		case SWT.MouseDown: msg = "MouseDown" ; break ;
		case SWT.MouseUp: msg = "MouseUp" ; break ;
		}
		log("Event '" + msg + "' : " + event );
	}

	//---------------------------------------------------------------------------------------------
	@Override
	public void handleEvent(Event event) {

		logEvent(event);
		TableItem tableItem = getTableItem(event);
		if ( event.type == SWT.MouseDown ) {
			if ( tableItem != null ) {
//				if ( tableItem != currentTableItem ) {
//					// Not on the same item
//					showToolTip(event, tableItem);
//				}
//				else if ( tip == null ) {
//					// not yet shown
//					showToolTip(event, tableItem);
//				}
				log("table location     : " + table.getLocation()  );
				log("table size         : " + table.getSize()  );
				log("table header hight : " + table.getHeaderHeight()  );
				if (   event.x > 30 
					&& event.x < ( table.getSize().x - 80 ) 
					&& event.y > ( table.getHeaderHeight() + 3) ) {
					showToolTip(event, tableItem);
				}
			}
		}
		else if ( event.type == SWT.MouseUp ) {
			disposeToolTip(); 
		}
		else if ( event.type == SWT.MouseMove ) {
			if ( tableItem == null || tableItem != currentTableItem ) {
				disposeToolTip(); 
			}
			else {
				if ( deltaXY(event, 2) ){
					disposeToolTip(); 
				}
			}
		}
	}
	private boolean deltaXY(Event event, int deltaMax) {
		int delta ;
		delta = currentPoint.x - event.x ;
		if ( delta > deltaMax || delta < -deltaMax ) return true; 
		delta = currentPoint.y - event.y ;
		if ( delta > deltaMax || delta < -deltaMax ) return true; 
		return false ;
	}
	
	//---------------------------------------------------------------------------------------------
	/**
	 * Returns the TableItem for the current mouse position <br>
	 * or null if the mouse is not on a TableItem
	 * @param event
	 * @return
	 */
	private TableItem getTableItem(Event event) {
		TableItem tableItem = table.getItem(new Point(event.x, event.y));
		log("getTableItem : " + tableItem );
		return tableItem ;
	}
	
	//---------------------------------------------------------------------------------------------
	/**
	 * Shows the ToolTip for the given TableItem
	 * @param tableItem
	 */
	private void showToolTip(Event event, TableItem tableItem) {
		log("showToolTip()");

		if ( tableItem == null ) {
			return ;
		}
		
		if (tip != null && !tip.isDisposed()) {
			tip.dispose();
		}
		
		currentTableItem = tableItem ;
		currentPoint = new Point(event.x, event.y);
		
		//--- Info to be displayed
		String info = getToolTipInfo(tableItem) ;
		if ( info == null ) {
			info = "( no tool tip information )" ;
		}
//		String info = "( no target information )" ;
//		Object data = tableItem.getData();
//		if ( data != null ) {
//			if ( data instanceof TargetDefinition ) {
//				TargetDefinition targetDefinition = (TargetDefinition) data ;
//				info = 
//					targetDefinition.getFolder() +
//					"   |   " +
//					targetDefinition.getFile() ;
//			}
//		}

		//--- Tool-tip colors
		final Shell shell = table.getShell();
		final Display display = shell.getDisplay();
		Color foregroundColor = display.getSystemColor(SWT.COLOR_INFO_FOREGROUND) ;
		Color backgroundColor = display.getSystemColor(SWT.COLOR_INFO_BACKGROUND) ;
		
		//--- Tool-tip creation
		tip = new Shell(shell, SWT.ON_TOP | SWT.TOOL);
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginWidth = 8; // left/right
		fillLayout.marginHeight = 3; // top/bottom
		tip.setLayout(fillLayout);
		tip.setForeground(foregroundColor);
		tip.setBackground(backgroundColor);
		
		Label label = new Label(tip, SWT.NONE);
		label.setForeground(foregroundColor);
		label.setBackground(backgroundColor);		
		label.setText(info);

		//--- Tool-tip position
		Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Rectangle rect = tableItem.getBounds(0);
		Point pt = table.toDisplay(rect.x, rect.y);
		tip.setBounds(pt.x+20, pt.y+20, size.x, size.y);
		tip.setVisible(true);
	}
	
	//---------------------------------------------------------------------------------------------
	private void disposeToolTip() {
		log("disposeToolTip()");
		if ( tip != null ) {
			tip.dispose();
		}
		tip = null;
	}

}
