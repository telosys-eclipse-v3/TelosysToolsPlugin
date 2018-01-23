package org.telosys.tools.eclipse.plugin.editors.dbrep;

import java.math.BigDecimal;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

public abstract class DialogBox extends TitleAreaDialog 
{
	private final static int ROW_WIDTH = 500 ;
	
	private RowData _rowData = new RowData(ROW_WIDTH, SWT.DEFAULT);
	
	//--- Current value
	private SpecialValue _value = null ;

	//--- Standard Fields 
	private Text   _textFieldLabel     = null ;
	private Text   _textFieldInputType = null ;
	
	public abstract boolean onOK();
	
	public DialogBox(Shell parentShell, SpecialValue value) 
	{
		super(parentShell);
		PluginLogger.log(this, "DialogBox CONSTRUCTOR ..." );
		this.setHelpAvailable(false);	
		_value = value ;
	}
	
	protected void log(String s)
	{
		PluginLogger.log(this, s );
	}
	
	public Object getValue()
	{
		return _value ;
	}
	
	protected Control createContents(Composite parent) 
	{
		log( "createContents() ..." );		
		Control result = super.createContents(parent);
		this.setTitle("Further information for '" + _value.getJavaType() + "' type");
		// setTitleImage(CustomwidgetPlugin.getCustomImage());
		return result;
	}

	/**
	 * Creates a Composite with a "RowLayout"
	 * @param parent
	 * @return
	 */
	protected Composite createDialogAreaContainer(Composite parent) 
	{
		log("createDialogAreaContainer() ..." );

		Composite content = new Composite(parent, SWT.NONE);

		RowLayout rowLayout = new RowLayout ();
		rowLayout.type = SWT.VERTICAL;
		rowLayout.justify = true;
		rowLayout.marginLeft  = 10;
		rowLayout.marginRight = 10;
		rowLayout.spacing = 10;
		
		content.setLayout(rowLayout);
		
		createStandardGroup(content); // New in v 2.0.3
		
		//--- Data To View 
		_textFieldLabel.setText( _value.getLabel() ) ;
		_textFieldInputType.setText( _value.getInputType() ) ;

		return content ;
	}
	
	/**
	 * Create a standard group for all types of fields with <br>
	 *  . field label <br>
	 *   
	 * @param container
	 * @return
	 */
	private Group createStandardGroup(Composite container)
	{
		log( "createFirstGroup() ..." );
		Group group = createDialogAreaGroup(container, null );

		//--- Group layout
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = false;
        group.setLayout(gridLayout);

		GridData gdSpan = new GridData();
		gdSpan.verticalAlignment = GridData.FILL;
		gdSpan.grabExcessVerticalSpace = true;
		gdSpan.horizontalSpan = 2;

		GridData gdLabel = new GridData();
		//gdLabel.widthHint    = 100 ;
		//gdLabel.minimumWidth = 100 ;
        
		GridData gdText = new GridData();
		gdText.widthHint = 200 ;
		gdText.minimumWidth = 200 ;

		Label label ;

		//--- Field label
		label = new Label(group, SWT.NONE);
		label.setText("Label : ");
		label.setLayoutData(gdLabel);

		_textFieldLabel = new Text(group, SWT.BORDER);
		_textFieldLabel.setText("");
		_textFieldLabel.setLayoutData(gdText);
		
		//--- Field input type
		label = new Label(group, SWT.NONE);
		label.setText("Input type : ");
		label.setLayoutData(gdLabel);

		_textFieldInputType = new Text(group, SWT.BORDER);
		_textFieldInputType.setText("");
		_textFieldInputType.setLayoutData(gdText);
		
        return group ;

	}
	
	/**
	 * Creates a Group with the given title text and the standard LayoutData ( "RowData" )
	 * @param container
	 * @param text
	 * @return
	 */
	protected Group createDialogAreaGroup(Composite container, String text )
	{
		Group group = new Group(container, SWT.NONE);
		if ( text != null ) {
			group.setText(text);
		}
		group.setLayoutData (_rowData);
		return group ;
	}
	
	protected void dataToNotNullCheckBox(Button cbNotNull) 
	{
		if ( _value.isPrimitiveType() ) {
			cbNotNull.setSelection(false) ;
			cbNotNull.setEnabled(false);
		}
		else {
			cbNotNull.setSelection(_value.isNotNull()) ;
			cbNotNull.setEnabled(true);
		}
	}

	protected void okPressed() 
	{
		PluginLogger.log(this, " ==== OK Pressed ");
		if ( onOK() )
		{
			_value.setLabel(     _textFieldLabel.getText() ) ;
			_value.setInputType( _textFieldInputType.getText() ) ;

			super.okPressed();
		}
	}
	
	protected void cancelPressed() 
	{
		PluginLogger.log(this, " ==== CANCEL Pressed ");
		super.cancelPressed();
	}
	
	/**
	 * Check the given string is a valid numeric value
	 * @param s
	 * @param msg
	 * @return
	 */
	protected boolean checkNumericValue(String s, String msg) 
	{
		if ( StrUtil.nullOrVoid(s) ) return true ; 
		try {
			new BigDecimal(s.trim());
			return true;
		} catch (Throwable e) {
			MsgBox.error(msg);
			return false ;
		}
	}
	
	/**
	 * Check s1 <= s2
	 * @param s1
	 * @param s2
	 * @param msg
	 * @return
	 */
	protected boolean isLowerThan(String s1, String s2, String msg) 
	{
		//--- If one of the field is void => nothing to compare
		if ( StrUtil.nullOrVoid(s1) ) return true ; 
		if ( StrUtil.nullOrVoid(s2) ) return true ; 
		try {
			BigDecimal d1 = new BigDecimal(s1.trim());
			BigDecimal d2 = new BigDecimal(s2.trim());
			if ( d1.compareTo(d2) > 0 ) {
				MsgBox.error(msg);
				return false ;
			}
			return true;
		} catch (Throwable e) {
			MsgBox.error("Invalid numeric value");
			return false ;
		}
	}
	
	protected String trim(String s) 
	{
		if ( s != null ) return s.trim() ;
		else return s ;
	}
}
