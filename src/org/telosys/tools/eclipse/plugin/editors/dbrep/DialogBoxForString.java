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

public class DialogBoxForString extends DialogBox 
{
	//--- Widgets
	private Button _cbLongText    = null ;
	
	private Button _cbNotNull     = null ;
	private Button _cbNotBlank    = null ;
	private Button _cbNotEmpty    = null ;
	private Text   _textMinLength = null ;
	private Text   _textMaxLength = null ;
	private Text   _textPattern   = null ;
	

	//--- Current value
	private SpecialValueForString _value = null ;
	
	public DialogBoxForString(Shell parentShell, SpecialValueForString value) 
	{
		super(parentShell, value);
		log( "DialogBoxForString(Shell parentShell, SpecialValueForString value) CONSTRUCTOR" );
		_value = value ;
	}

	/* (non-Javadoc)
	 * @see org.telosys.tools.eclipse.plugin.editors.dbrep.DialogBox#createContents(org.eclipse.swt.widgets.Composite)
	 * === STEP 1
	 */
	protected Control createContents(Composite parent) 
	{
		log( "createContents(Composite parent) ..." );
		Control result = super.createContents(parent);
		setMessage( "String type and validation rules", IMessageProvider.INFORMATION);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 * === STEP 2
	 */
	protected Control createDialogArea(Composite parent) 
	{
		log( "createDialogArea(Composite parent) ..." );
		Composite content = createDialogAreaContainer(parent);
		createStringTypeGroup(content);
		createValidationGroup(content);
		dataToView();
		return content;
	}
	
	private Group createStringTypeGroup(Composite container)
	{
		log( "createStringTypeGroup() ..." );
		
        Group group = createDialogAreaGroup(container, " String type " );
        
		//--- Group layout
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.makeColumnsEqualWidth = false;
        group.setLayout(gridLayout);

		//--- Fix the Text widgets size
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace=true;
		gd.minimumWidth = 100 ; // in pixels 

		//--- Create the widgets
		_cbLongText = new Button(group, SWT.CHECK);
		_cbLongText.setText("Long text (e.g. text requiring a textarea)");
		
//		//--- Init with value 
//		_cbLongText.setSelection( _value.isLongText() );

//		//--- Listeners (to update the value in real-time)
//		_cbLongText.addSelectionListener( new org.eclipse.swt.events.SelectionAdapter() { 
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {   
//				_value.setLongText( _cbLongText.getSelection() ) ; 
//			}
//		});
		
		return group ;
	}
	
	private Group createValidationGroup(Composite container)
	{
		log( "createValidationGroup() ..." );

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
		gdSpan.widthHint    = 300 ;
		gdSpan.minimumWidth = 300 ;

		GridData gdLabel = new GridData();
		gdLabel.widthHint    = 120 ;
		gdLabel.minimumWidth = 120 ;
        
		GridData gdTextLength = new GridData();
		gdTextLength.widthHint    = 80 ;
		gdTextLength.minimumWidth = 80 ;
		
		GridData gdTextPattern = new GridData();
		gdTextPattern.widthHint    = 300 ;
		gdTextPattern.minimumWidth = 300 ;
		
		Label label ;
		
		//--- Not Null
        _cbNotNull = new Button(group, SWT.CHECK);
        _cbNotNull.setText("Not null ");
        _cbNotNull.setSelection(false);       
        _cbNotNull.setLayoutData(gdSpan);
        
		//--- Not Empty
        _cbNotEmpty = new Button(group, SWT.CHECK);
        _cbNotEmpty.setText("Not empty ");
        _cbNotEmpty.setSelection(false);       
        _cbNotEmpty.setLayoutData(gdSpan);
        
		//--- Not Blank
        _cbNotBlank = new Button(group, SWT.CHECK);
        _cbNotBlank.setText("Not blank ");
        _cbNotBlank.setSelection(false);       
        _cbNotBlank.setLayoutData(gdSpan);
        
        //--- Min Length
		label = new Label(group, SWT.NONE);
		label.setText("Minimum length : ");
		label.setLayoutData(gdLabel);

		_textMinLength = new Text(group, SWT.BORDER);
		_textMinLength.setText("");
		_textMinLength.setLayoutData(gdTextLength);

        //--- Max Length
		label = new Label(group, SWT.NONE);
		label.setText("Maximum length : ");
		label.setLayoutData(gdLabel);

        _textMaxLength = new Text(group, SWT.BORDER);
        _textMaxLength.setText("");
        _textMaxLength.setLayoutData(gdTextLength);
        
        //--- Pattern
		label = new Label(group, SWT.NONE);
		label.setText("Pattern (RegExp) : ");
		label.setLayoutData(gdLabel);

		_textPattern = new Text(group, SWT.BORDER);
		_textPattern.setText("");
		_textPattern.setLayoutData(gdTextPattern);
        
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

	private boolean checkViewValues() 
	{
		if ( ! checkNumericValue( _textMinLength.getText(), "Invalid minimum length" ) )  return false ;
		if ( ! checkNumericValue( _textMaxLength.getText(), "Invalid maximum length" ) )  return false ;
		if ( ! isLowerThan(_textMinLength.getText(), _textMaxLength.getText(), "Min length / Max length inverted") ) return false ;
		
		return true ;
	}
	
	private void dataToView()
	{
		log( "dataToView() ..." );

		_cbLongText.setSelection( _value.isLongText() );
		_cbNotNull.setSelection ( _value.isNotNull()  ) ;
		_cbNotEmpty.setSelection( _value.isNotEmpty() );
		_cbNotBlank.setSelection( _value.isNotBlank() );

		_textMinLength.setText( _value.getMinLength());
		_textMaxLength.setText( _value.getMaxLength());
		_textPattern.setText( _value.getPattern());
		
		log( "dataToView() : END" );
	}
	
	private void viewToData() 
	{
		log( "viewToData() ..." );
		_value.setLongText( _cbLongText.getSelection() ) ;
		_value.setNotNull ( _cbNotNull.getSelection() ) ;
		_value.setNotEmpty( _cbNotEmpty.getSelection() ) ;
		_value.setNotBlank( _cbNotBlank.getSelection() ) ;

		_value.setMinLength( trim ( _textMinLength.getText() ) );
		_value.setMaxLength( trim ( _textMaxLength.getText() ) );
		_value.setPattern( _textPattern.getText() ) ;
	}

}
