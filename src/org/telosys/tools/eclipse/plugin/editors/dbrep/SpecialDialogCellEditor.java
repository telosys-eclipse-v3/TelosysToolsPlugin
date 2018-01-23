package org.telosys.tools.eclipse.plugin.editors.dbrep;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

public class SpecialDialogCellEditor extends DialogCellEditor {

//	private Table _table = null ;
//	/**
//	 * 
//	 */
//	public SpecialDialogCellEditor() {
//		super();
//		// Auto-generated constructor stub
//		PluginLogger.log(this, "SpecialDialogCellEditor() : Constructor" );		
//	}

//	/**
//	 * @param parent
//	 * @param style
//	 */
//	public SpecialDialogCellEditor(Composite parent, int style) {
//		super(parent, style);
//		// Auto-generated constructor stub
//	}

	/**
	 * @param parent
	 */
	//public SpecialDialogCellEditor(Composite parent) 
	public SpecialDialogCellEditor(Table table) 
	{
//		super(parent);
		super(table);
//		_table = table ;
		// Auto-generated constructor stub
		PluginLogger.log(this, "SpecialDialogCellEditor(Composite parent) : Constructor" );		
	}

	private void log(String s)
	{
		PluginLogger.log(this, s );
	}

	//-----------------------------------------------------------------------------------------------------
	// Methods call sequence :
	// . get the TableViewer value : TableViewerCellModifier.getValue(..,..) 
	// . updateContents(Object value) : init the DialogCellEditor value 
	// . openDialogBox() : the user change the value (or not)
	// . 'OK' or 'Cancel -->  updateContents(Object value) : set the value returned by openDialogBox()
	// . set the TableViewer value : TableViewerCellModifier.modify(..,..) 
	//-----------------------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
	 */
	// opens the dialog box when the end user presses the button 
	protected Object openDialogBox(Control cellEditorWindow) {
		log( "openDialogBox(Control cellEditorWindow)..." + cellEditorWindow.getClass().getName());		
		
//		if ( cellEditorWindow instanceof Table )
//		{
//			PluginLogger.log(this, "openDialogBox(Table )...");		
//		}
		
		Display display = cellEditorWindow.getDisplay();
		
//		Object oData = cellEditorWindow.getData();
//		log("getData() : " + ( oData != null ? oData.getClass().getName() : "(null)" ) );
		
		//TitleAreaDialog dialog = null ;
		DialogBox dialog = null ;
		
		//--- Retrieve the original value 
		Object val = getValue();
		PluginLogger.log(this, "openDialogBox(Control cellEditorWindow)... value = " + val );
		if ( val instanceof SpecialValueForNumber )
		{
			log( "openDialogBox(Control cellEditorWindow)... SpecialValueForNumber retrieved "  );
			dialog = new DialogBoxForNumber(display.getActiveShell(), (SpecialValueForNumber)val);
		}
		else if ( val instanceof SpecialValueForDate )
		{
			log( "openDialogBox(Control cellEditorWindow)... SpecialValueForDate retrieved "  );
			dialog = new DialogBoxForDate(display.getActiveShell(), (SpecialValueForDate)val);
		}
		else if ( val instanceof SpecialValueForString )
		{
			log( "openDialogBox(Control cellEditorWindow)... SpecialValueForString retrieved "  );
			dialog = new DialogBoxForString(display.getActiveShell(), (SpecialValueForString)val);
		}
		else if ( val instanceof SpecialValueForBoolean )
		{
			log( "openDialogBox(Control cellEditorWindow)... SpecialValueForBoolean retrieved "  );
			dialog = new DialogBoxForBoolean(display.getActiveShell(), (SpecialValueForBoolean)val);
		}
		else
		{
//			MsgBox.error("openDialogBox() : value is not a SpecialValue ");
//			return "Error";
			MsgBox.info("No further information for this type");
			return "" ;
		}
		
		//--- Display the dialog box
		
//		TitleAreaDialog dialog = new TitleAreaDialog(display.getActiveShell()) 
//		{
//			DialogBox1Content content;
//
//			protected Control createContents(Composite parent) {
//				Control result = super.createContents(parent);
//				// setTitleImage(CustomwidgetPlugin.getCustomImage());
//				setTitle("Prompter's text property editor");
//				setMessage(
//						"Enter the text property, or select a default one by checking the Hello or GoodBye",
//						IMessageProvider.INFORMATION);
//				return result;
//			}
//
//			protected Control createDialogArea(Composite parent) {
//				content = new DialogBox1Content(parent, SWT.NONE);
//				// content.setString(stringValue);
//				return content;
//			}
//
//			public String toString() {
//				return content.getString();
//			}
//
//		};

		//--- Open the Dialog Box Window ( Modal Window )
		log( "openDialogBox() : before dialog.open() ----- " );		
		int iRet = dialog.open();
		log( "openDialogBox() : after dialog.open() ----- " );		
		
		//--- OK or Cancel ?
//		Object oRet = null ;
//		if ( iRet == Window.OK )
//		{
//			oRet = dialog.getValue() ;
//
//			//oRet = "New value"; // OK : return the new value
//		}
//		else
//		{
//			//oRet = "Old value" ; // CANCEL : keep the old value
//			//oRet = null ; // CANCEL : keep the old value
//			oRet = dialog.getValue() ;
//			oRet.
//		}

		Object oRet = dialog.getValue() ;
		if ( iRet != Window.OK )
		{
			//--- Cancel button => Cancel all changes in the value
			if ( oRet instanceof SpecialValue )
			{
				SpecialValue specialValue = (SpecialValue) oRet ;
				specialValue.cancelChanges() ;
			}
			else
			{
				MsgBox.error("openDialogBox() : value is not a SpecialValue ");
				return null ;
			}
		}
		
		//--- The value contains
		log( "openDialogBox() : after dialog.open() : return " + oRet  );		
		return oRet ; 
	}

	//----------------------------------------------------------------------------------------------

}
