package org.telosys.tools.eclipse.plugin.editors.dbrep;



import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.telosys.tools.commons.JavaValue;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;

public class DialogBoxForNumber extends DialogBox
{
	//--- Widgets
	
	private Button _cbNotNull    = null ;
	
	private Text   _textMinValue = null ;
	
	private Text   _textMaxValue = null ;
	
	private Text   _textDefaultValue = null ;
	
	//--- Current value
	private SpecialValueForNumber _value = null ;

	public DialogBoxForNumber(Shell parentShell, SpecialValueForNumber value) 
	{
		super(parentShell, value);
		_value = value ;
	}

	protected Control createContents(Composite parent) 
	{
		log( "createContents() ..." );
		
		Control result = super.createContents(parent);
		// setTitleImage(CustomwidgetPlugin.getCustomImage());

//		setTitle("Further informations for '" + _value.getJavaType() + "' type");
		setMessage( "Number validation rules", IMessageProvider.INFORMATION);
		
		return result;
	}

	protected Control createDialogArea(Composite parent) 
	{
		log( "createDialogArea() ..." );

//		Composite content = new Composite(parent, SWT.NONE);
//
//		//--- Set the layout
//		GridLayout gridLayout = new GridLayout();
//		gridLayout.numColumns = 1;
//		gridLayout.makeColumnsEqualWidth = true;
//		content.setLayout(gridLayout);

		Composite content = createDialogAreaContainer(parent);
		
		createDefaultValueGroup(content);
		
		createValidationGroup(content);
		
//		//--- 
//		Label label = new Label(content, SWT.NONE);
//		GridData gd = new GridData();
//		gd.verticalAlignment = GridData.FILL;
//		gd.grabExcessVerticalSpace = true;
//		label.setLayoutData(gd);
		
		dataToView();
		
		return content;
	}
	
	private Group createDefaultValueGroup(Composite container)
	{
		log( "createDefaultValueGroup() ..." );
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
		//--- 
		label = new Label(group, SWT.NONE);
		label.setText("Default value : ");
		label.setLayoutData(gdLabel);

		_textDefaultValue = new Text(group, SWT.BORDER);
		_textDefaultValue.setText("");
		_textDefaultValue.setLayoutData(gdText);
		
        return group ;

	}
	
	private Group createValidationGroup(Composite container)
	{
		log( "createValidationGroup() ..." );

//		Group group = new Group(container, SWT.NONE);
//		group.setText(" Validation ");
		
		Group group = createDialogAreaGroup(container, " Validation rules " );
		
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
		
		//-----
        _cbNotNull = new Button(group, SWT.CHECK);
        _cbNotNull.setText("Not null ");
        _cbNotNull.setSelection(false);       
        _cbNotNull.setLayoutData(gdSpan);
        
//		Label label = new Label(group, SWT.NONE);
//		label.setText("");
//		label.setLayoutData(gdLabel);

        //--- Min Value
		label = new Label(group, SWT.NONE);
		label.setText("Minimum : ");
		label.setLayoutData(gdLabel);

		_textMinValue = new Text(group, SWT.BORDER);
		_textMinValue.setText("");
		//t.setSize(200, 20);
		_textMinValue.setLayoutData(gdText);

        //--- Max Value
		label = new Label(group, SWT.NONE);
		label.setText("Maximum : ");
		label.setLayoutData(gdLabel);

        _textMaxValue = new Text(group, SWT.BORDER);
        _textMaxValue.setText("");
		//t.setSize(200, 20);
        _textMaxValue.setLayoutData(gdText);
        
        
//		Label label = new Label(group, SWT.NONE);
//		label.setText("Enter Date or Time in ISO format ( 'YYYY-MM-DD', 'hh:mm:ss' )");
//		label.setLayoutData(gdSpan);
        
        return group ;
	}

	public boolean onOK() 
	{
		log("onOK");
		if ( checkViewValues() )
		{
			viewToData() ;
			return true ;
		}
		return false ;
	}
	
	private void dataToView()
	{
		log( "dataToView() ..." );

		_cbNotNull.setSelection(_value.isNotNull()) ;		
		_textDefaultValue.setText(_value.getDefaultValue() );
		
		_textMinValue.setText(_value.getMinValue() );
		_textMaxValue.setText(_value.getMaxValue() );
		
		if ( _value.isPrimitiveType() ) {
			_cbNotNull.setSelection(false) ;
			_cbNotNull.setEnabled(false);
		}
		else {
			_cbNotNull.setEnabled(true);
		}
		log( "dataToView() : END" );
	}
	
	private boolean checkViewValues() 
	{
		if ( ! checkDefaultValue( _textDefaultValue.getText(), "Invalid default value" ) )  return false ;
		
		if ( ! checkNumericValue( _textMinValue.getText(), "Invalid minimum value" ) )  return false ;
		if ( ! checkNumericValue( _textMaxValue.getText(), "Invalid maximum value" ) )  return false ;
		if ( ! isLowerThan(_textMinValue.getText(), _textMaxValue.getText(), "Min/Max inverted") ) return false ;
		return true ;
	}
	
	private void viewToData() 
	{
		log( "viewToData() ..." );
		_value.setNotNull ( _cbNotNull.getSelection() );
		_value.setDefaultValue ( trim( _textDefaultValue.getText() ) );
		
		_value.setMinValue( trim( _textMinValue.getText() ) ) ;
		_value.setMaxValue( trim( _textMaxValue.getText() ) ) ;
	}

	private boolean checkDefaultValue(String s, String msg) 
	{
		if ( StrUtil.nullOrVoid(s) ) return true ; 
		
		String type = _value.getJavaType() ;
		String value = s.trim();
		
		if ( JavaValue.isValidForNumberType(value, type) ) {
			return true ;
		} 
		else {
			MsgBox.error(msg);
			return false ;
		}
		
//		if ( "byte".equals(type) || "java.lang.Byte".equals(type) ) {
//			try {
//				Byte.parseByte(s.trim());
//				return true;
//			} catch (Throwable e) {
//				MsgBox.error(msg);
//				return false ;
//			}
//		}
//		else if ( "short".equals(type) || "java.lang.Short".equals(type) ) {
//			try {
//				Short.parseShort(s.trim());
//				return true;
//			} catch (Throwable e) {
//				MsgBox.error(msg);
//				return false ;
//			}
//		}
//		else if ( "int".equals(type) || "java.lang.Integer".equals(type) ) {
//			try {
//				Integer.parseInt(s.trim());
//				return true;
//			} catch (Throwable e) {
//				MsgBox.error(msg);
//				return false ;
//			}
//		}
//		else if ( "long".equals(type) || "java.lang.Long".equals(type) ) {
//			try {
//				Long.parseLong(s.trim());
//				return true;
//			} catch (Throwable e) {
//				MsgBox.error(msg);
//				return false ;
//			}
//		}
//		else // "float", "double", "BigDecimal"
//		{
//			try {
//				new BigDecimal(s.trim());
//				return true;
//			} catch (Throwable e) {
//				MsgBox.error(msg);
//				return false ;
//			}
//		}
	}
	
}
