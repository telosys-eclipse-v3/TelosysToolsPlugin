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
import org.telosys.tools.commons.DateUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;

public class DialogBoxForDate extends DialogBox
{
	private boolean bJavaUtilDate = false ;
	private boolean bJavaSqlDate = false ;
	private boolean bJavaSqlTime = false ;
	private boolean bJavaSqlTimestamp = false ;
	
	//--- Widgets
	private Button _rbDateOnly = null;

	private Button _rbTimeOnly = null;

	private Button _rbDateAndTime = null;
	
	private Button _cbNotNull = null ;
	
	//private Button _rbNone = null ;
	
	private Button _cbPast   = null  ;
    
	private Button _cbFuture = null ;
    
	private Button _cbBefore = null ;
	private Text   _textBefore = null ;
	
	private Button _cbAfter   = null ;
	private Text   _textAfter = null ;
	
	private Label  _labelInputFormat = null ;
	
	//--- Current value
	private SpecialValueForDate _value = null ;

	public DialogBoxForDate(Shell parentShell, SpecialValueForDate value) 
	{
		super(parentShell, value);
//		PluginLogger.log(this, "CONSTRUCTOR ..." );
//		this.setHelpAvailable(false);

		_value = value ;

		String sJavaType = _value.getJavaType();
		log("CONSTRUCTOR : java type = " + sJavaType );
		if ( "java.sql.Time".equals(sJavaType) ) {
			bJavaSqlTime = true ;
		}
		if ( "java.sql.Timestamp".equals(sJavaType) ) {
			bJavaSqlTimestamp = true ;
		}
		if ( "java.sql.Date".equals(sJavaType) ) {
			bJavaSqlDate = true ;
		}
		if ( "java.util.Date".equals(sJavaType) ) {
			bJavaUtilDate = true ;
		}
	}

	protected Control createContents(Composite parent) 
	{
		log( "createContents() ..." );
		
		Control result = super.createContents(parent);
		// setTitleImage(CustomwidgetPlugin.getCustomImage());

//		setTitle("Further informations for '" + _value.getJavaType() + "' type");
		setMessage( "Date type and validation rules",
				IMessageProvider.INFORMATION);
		
		this.setHelpAvailable(false);
		
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
//		//content.setBackground(new Color());
		
		Composite content = createDialogAreaContainer(parent);

//		//--- Create the widgets
//		_rbDateAndTime = new Button(content, SWT.RADIO);
//		_rbDateAndTime.setText("Date and Time");
//		
//		_rbDateOnly = new Button(content, SWT.RADIO);
//		_rbDateOnly.setText("Date only");
//		
//		_rbTimeOnly = new Button(content, SWT.RADIO);
//		_rbTimeOnly.setText("Time only");

		createDateTypeGroup(content);
		
		createValidationGroup(content);
		
//		//--- 
//		Label label = new Label(content, SWT.NONE);
//		GridData gd = new GridData();
//		gd.verticalAlignment = GridData.FILL;
//		gd.grabExcessVerticalSpace = true;
//		label.setLayoutData(gd);
		
//		//--- Listeners (to update the value in real-time)
//		_rbDateAndTime.addSelectionListener( new org.eclipse.swt.events.SelectionAdapter() { 
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {   
//				if ( _rbDateAndTime.getSelection() ) {
//					_value.setDateAndTime() ; 
//				}
//			}
//		});
//
//		_rbDateOnly.addSelectionListener( new org.eclipse.swt.events.SelectionAdapter() { 
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {   
//				if ( _rbDateOnly.getSelection() ) {
//					_value.setDateOnly() ; 
//				}
//			}
//		});
//		
//		_rbTimeOnly.addSelectionListener( new org.eclipse.swt.events.SelectionAdapter() { 
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {   
//				manageEnableDisable( _rbTimeOnly.getSelection() );
////				if ( _rbTimeOnly.getSelection() ) {
//////					_value.setTimeOnly() ; 
////					_rbPast.setEnabled(false);
////					_rbFuture.setEnabled(false);
////				} else {
////					_rbPast.setEnabled(true);
////					_rbFuture.setEnabled(true);
////				}
//			}
//		});
		
		dataToView();
		
		return content;
	}
	
	private Group createDateTypeGroup(Composite container)
	{
		log( "createDatabaseValuesGroup() ..." );
		
//		Group group = new Group(container, SWT.NONE);
//		group.setText(" Database values ");
//		group.setLayoutData (rowData);
//		
        Group group = createDialogAreaGroup(container, " Date type " );
        
		//--- Group layout
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.makeColumnsEqualWidth = false;
        group.setLayout(gridLayout);

        // Same event listener for the 3 Radio Buttons
        org.eclipse.swt.events.SelectionAdapter listener = 
	        new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {  
					Button button = (Button) e.widget ;
					if ( button.getSelection() ) {
						refreshHelpInputFormat();
					}
					if ( button == _rbTimeOnly ) {
						manageEnableDisable( _rbTimeOnly.getSelection() );
					}
				}
	        };
        
        _rbDateAndTime = new Button(group, SWT.RADIO);
		_rbDateAndTime.setText("Date and Time");
		_rbDateAndTime.addSelectionListener( listener ); 
		
		_rbDateOnly = new Button(group, SWT.RADIO);
		_rbDateOnly.setText("Date only");
		_rbDateOnly.addSelectionListener( listener ); 
		
		_rbTimeOnly = new Button(group, SWT.RADIO);
		_rbTimeOnly.setText("Time only");
		_rbTimeOnly.addSelectionListener( listener ); 
//		_rbTimeOnly.addSelectionListener( new org.eclipse.swt.events.SelectionAdapter() { 
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {  
//				refreshHelpInputFormat();
//				manageEnableDisable( _rbTimeOnly.getSelection() );
//			}
//		});
		
		return group ;

	}
	
	private Group createValidationGroup(Composite container)
	{

//		Group group = new Group(container, SWT.NONE);
//		group.setText(" Validation ");
		Group group = createDialogAreaGroup(container, " Validation rules " );
		
		//--- Group layout
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = false;
        group.setLayout(gridLayout);

		//-----
        _cbNotNull = new Button(group, SWT.CHECK);
        _cbNotNull.setText("Not null ");
        _cbNotNull.setSelection(false);       
        
		GridData gdSpan = new GridData();
		gdSpan.verticalAlignment = GridData.FILL;
		gdSpan.grabExcessVerticalSpace = true;
		gdSpan.horizontalSpan = 2;
		gdSpan.widthHint    = 400 ;
		gdSpan.minimumWidth = 400 ;

		GridData gdText = new GridData();
		gdText.widthHint    = 200 ;
		gdText.minimumWidth = 200 ;
        
//        _rbNone = new Button(group, SWT.RADIO);
//        _rbNone.setText("No Past/Future constraint");
//        _rbNone.setSelection(false);    
//        _rbNone.setLayoutData(gdSpan);
//		//--- Listeners (to update the value in real-time)
//        _rbNone.addSelectionListener( new org.eclipse.swt.events.SelectionAdapter() { 
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {   
//				if ( _rbNone.getSelection() ) {
//					_value.setDateValidationNone() ; 
//				}
//			}
//		});
        
        
        //_cbPast = new Button(group, SWT.RADIO);
        _cbPast = new Button(group, SWT.CHECK);
        _cbPast.setText("In the Past");
        _cbPast.setSelection(false);    
        _cbPast.setLayoutData(gdSpan);
        _cbPast.addSelectionListener( new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {   
				// If PAST is selected => unselect FUTURE
				if ( _cbPast.getSelection() ) {
					_cbFuture.setSelection( false ); 
				}
			}
		});
        
        //_cbFuture = new Button(group, SWT.RADIO);
        _cbFuture = new Button(group, SWT.CHECK);
        _cbFuture.setText("In the Future");
        _cbFuture.setSelection(false);       
        _cbFuture.setLayoutData(gdSpan);
        _cbFuture.addSelectionListener( new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {   
				// If FUTURE is selected => unselect PAST
				if ( _cbFuture.getSelection() ) {
					_cbPast.setSelection( false ) ; 
				}
			}
		});
        
        //--- After YYYY-MM-DD
        _cbAfter  = new Button(group, SWT.CHECK);
        _cbAfter.setText("After");
        _cbAfter.setSelection(false);       

        _textAfter = new Text(group, SWT.BORDER);
        _textAfter.setText("");
		//t.setSize(200, 20);
        _textAfter.setLayoutData(gdText);
        
        //--- Before YYYY-MM-DD
        _cbBefore = new Button(group, SWT.CHECK);
        _cbBefore.setText("Before");
        _cbBefore.setSelection(false);   
        
		_textBefore = new Text(group, SWT.BORDER);
		_textBefore.setText("");
		//t.setSize(200, 20);
		_textBefore.setLayoutData(gdText);

		_labelInputFormat = new Label(group, SWT.NONE);
		_labelInputFormat.setText("Use ISO format");
		_labelInputFormat.setLayoutData(gdSpan);
        
        return group ;
	}
	
	private void refreshHelpInputFormat() 
	{
		if ( _rbDateOnly.getSelection() ) {
			_labelInputFormat.setText("Enter DATE in ISO format ( 'YYYY-MM-DD' )");
		}
		else if ( _rbTimeOnly.getSelection() ) {
			_labelInputFormat.setText("Enter TIME in ISO format ( 'HH:MM:SS' )");
		}
		else if ( _rbDateAndTime.getSelection() ) {
			_labelInputFormat.setText("Enter DATE with TIME in ISO format ( 'YYYY-MM-DD HH:MM:SS' )");
		}
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
		log("dataToView : past   : " + _value.isDateValidationPast() );
		log("dataToView : future : " + _value.isDateValidationFuture() );
		
		_cbNotNull.setSelection(_value.isDateValidationNotNull()) ;
		
		//--- Validation rules 
//		if ( _value.isDateValidationPast() == false && _value.isDateValidationFuture() == false )
//		{
//			_rbNone.setSelection(true);
//		}
//		else
//		{
//			_rbNone.setSelection(false);
//			_cbPast.setSelection  (_value.isDateValidationPast());
//			_rbFuture.setSelection(_value.isDateValidationFuture());
//		}

		_cbPast.setSelection  (_value.isDateValidationPast());
		_cbFuture.setSelection(_value.isDateValidationFuture());
		
		
		if ( _value.isDateValidationBefore() )  {
			_cbBefore.setSelection(true);
			_textBefore.setText(_value.getDateValidationBefore() );
		}
		if ( _value.isDateValidationAfter()  ) { 
			_cbAfter.setSelection(true);
			_textAfter.setText(_value.getDateValidationAfter() );
		}
		
		//--- Disable and force widgets depending on the Java DATE type
		if ( bJavaUtilDate )
		{
			log("java.util.date");
			_rbDateAndTime.setSelection( _value.isDateAndTime() );
			_rbDateOnly.setSelection( _value.isDateOnly() );
			_rbTimeOnly.setSelection( _value.isTimeOnly() );
		}
		else 
		{
			log("Not java.util.date");
			_rbDateOnly.setEnabled(false);
			_rbTimeOnly.setEnabled(false);
			_rbDateAndTime.setEnabled(false);

			//--- Select the Radio Button corresponding to the primary type
			if ( bJavaSqlDate ) {
				_rbDateOnly.setSelection(true);
			}
			else if ( bJavaSqlTime ) {
				_rbTimeOnly.setSelection(true);
			}
			else if ( bJavaSqlTimestamp ) {
				_rbDateAndTime.setSelection(true);
			}
		}
		refreshHelpInputFormat();
		manageEnableDisable( _rbTimeOnly.getSelection() );
	}
	
	private void manageEnableDisable(boolean bTimeOnly)
	{
		if ( bTimeOnly ) {
//			_rbNone.setEnabled(false);
//			_rbNone.setSelection(true);

			_cbPast.setEnabled(false);
			_cbPast.setSelection(false);
			
			_cbFuture.setEnabled(false);
			_cbFuture.setSelection(false);
		} else {
//			_rbNone.setEnabled(true);
			_cbPast.setEnabled(true);
			_cbFuture.setEnabled(true);
		}
	}
	
	private boolean checkViewValues() 
	{
		String m1 = "Invalid 'before' limit" ;
		String m2 = "Invalid 'after' limit" ;
		
		if ( _cbBefore.getSelection() )
		{
			String s = _textBefore.getText() ;
			if ( null == s ) { MsgBox.error(m1); return false ; }
			if ( s.length() == 0 ) { MsgBox.error(m1); return false ; }
			if ( ! checkDate(s) ) { MsgBox.error(m1); return false ; }
		}
		
		if ( _cbAfter.getSelection() )
		{
			String s = _textAfter.getText() ;
			if ( null == s ) { MsgBox.error(m2); return false ; }
			if ( s.length() == 0 ) { MsgBox.error(m2); return false ; }
			if ( ! checkDate(s) ) { MsgBox.error(m2); return false ; }
		}
		return true ;
	}
	
	private boolean checkDate(String s) 
	{
		try {
			if ( _rbDateOnly.getSelection() ) {
				DateUtil.parseDate(s);
				return true ;
			}
			if ( _rbTimeOnly.getSelection() ) {
				DateUtil.parseTime(s);
				return true ;
			}
			if ( _rbDateAndTime.getSelection() ) {
				DateUtil.parseDateTime(s);
				return true ;
			}
		} catch ( Throwable t ) {
			return false ;
		}
		MsgBox.error("Unknown date type : Date only, Time only, Date and Time ?");
		return false ;
	}
	
	private void viewToData() 
	{
		if ( _rbDateAndTime.getSelection() ) _value.setDateAndTime() ;
		if ( _rbDateOnly.getSelection() ) _value.setDateOnly() ;
		if ( _rbTimeOnly.getSelection() ) _value.setTimeOnly() ;
		
		_value.setDateValidationNotNull( _cbNotNull.getSelection() );
		
		//if ( _rbNone.getSelection() ) _value.setDateValidationNone() ; 
		_value.setDateValidationPast(_cbPast.getSelection() ) ; 
		_value.setDateValidationFuture(_cbFuture.getSelection())  ;
		
		if ( _cbBefore.getSelection() ) _value.setDateValidationBefore(true, _textBefore.getText() );
		else _value.setDateValidationBefore(false, null );

		if ( _cbAfter.getSelection() ) _value.setDateValidationAfter(true, _textAfter.getText() );
		else _value.setDateValidationAfter(false, null );
	}

	public Object getValue() 
	{
		log( "getValue() ... return " + _value );
//		if ( _value != null )
//		{
////			//--- Update the value and return it 
////			if ( date_and_time.getSelection() ) _value.setDateAndTime() ; 
////			if ( date_only.getSelection() ) _value.setDateOnly() ; 
////			if ( time_only.getSelection() ) _value.setTimeOnly() ; 
////			
//			PluginLogger.log(this, "getValue() ... return " + _value );
//		}
//		else
//		{
//			MsgBox.error("DiallogBox1.getValue() : _value is NULL !");
//		}
		return _value ;
	}

}
