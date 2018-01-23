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
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

public class DialogBoxForBoolean extends DialogBox
{
	//--- Widgets
	private Button _cbNotNull    = null ;
		
	private Text   _textTrueValue  = null;

	private Text   _textFalseValue = null;

	//--- Current value
	private SpecialValueForBoolean _value = null ;

	public DialogBoxForBoolean(Shell parentShell, SpecialValueForBoolean value) 
	{
		super(parentShell, value);		
		_value = value ;
	}

	protected Control createContents(Composite parent) 
	{
		PluginLogger.log(this, "createContents(Composite parent) ..." );
		
		Control result = super.createContents(parent);
		
		setMessage(	"Database values and validation rules", 
				IMessageProvider.INFORMATION);
		
		return result;
	}

	protected Control createDialogArea(Composite parent) 
	{
		PluginLogger.log(this, "createDialogArea(Composite parent) ..." );

//		Composite content = new Composite(parent, SWT.NONE);
//
//		RowLayout rowLayout = new RowLayout ();
//		rowLayout.type = SWT.VERTICAL;
//		rowLayout.justify = true;
//		rowLayout.marginLeft  = 10;
//		rowLayout.marginRight = 10;
//		rowLayout.spacing = 10;
//		
//		int width = 500 ;
//		RowData rowData = new RowData(width, SWT.DEFAULT);
		
		Composite content = createDialogAreaContainer(parent);
		
//		//--- Set the layout
//		GridLayout gridLayout = new GridLayout();
//		gridLayout.numColumns = 2;
//		//gridLayout.makeColumnsEqualWidth = true;
		
//		content.setLayout(rowLayout);
/***
		//--- Fix the Text widgets size
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace=true;
		gd.minimumWidth = 100 ; // in pixels 

		//--- Create the widgets
		
		Label label1 = new Label(content, SWT.NONE);
		label1.setText("True value : ");
		
		_textTrueValue  = new Text(content, SWT.BORDER);
		_textTrueValue.setLayoutData (gd); 
		
		Label label2 = new Label(content, SWT.NONE);
		label2.setText("False value : ");
		
		_textFalseValue = new Text(content, SWT.BORDER);
		_textFalseValue.setLayoutData (gd); 
		
//		//--- Init with value 
//		_textTrueValue.setText ( _value.getTrueValue()  );
//		_textFalseValue.setText( _value.getFalseValue() );
		
//		//--- Listeners (to update the value in real-time)
//		_textTrueValue.addModifyListener(
//			new org.eclipse.swt.events.ModifyListener() { 
//				public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
//					_value.setTrueValue( _textTrueValue.getText() );
//				}
//			});		
//		_textFalseValue.addModifyListener(
//			new org.eclipse.swt.events.ModifyListener() { 
//				public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
//					_value.setFalseValue( _textFalseValue.getText() );
//				}
//			});
***/
		createDatabaseValuesGroup(content);
		
		createValidationGroup(content);
		//group.setLayoutData(gd);
		
		dataToView();

		return content;
	}
	
	private Group createDatabaseValuesGroup(Composite container)
	{
		log( "createDatabaseValuesGroup() ..." );
		
//		Group group = new Group(container, SWT.NONE);
//		group.setText(" Database values ");
//		group.setLayoutData (rowData);
//		
        Group group = createDialogAreaGroup(container, " Database values " );
        
		//--- Group layout
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = false;
        group.setLayout(gridLayout);


		//--- Fix the Text widgets size
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace=true;
		gd.minimumWidth = 100 ; // in pixels 

		//--- Create the widgets
		
		Label label1 = new Label(group, SWT.NONE);
		label1.setText("True value : ");
		
		_textTrueValue  = new Text(group, SWT.BORDER);
		_textTrueValue.setLayoutData (gd); 
		
		Label label2 = new Label(group, SWT.NONE);
		label2.setText("False value : ");
		
		_textFalseValue = new Text(group, SWT.BORDER);
		_textFalseValue.setLayoutData (gd);
		
		return group ;
	}
	
	private Group createValidationGroup(Composite container)
	{
		log( "createValidationGroup() ..." );

//		Group group = new Group(container, SWT.NONE);
//		group.setText(" Validation rules ");
//		group.setLayoutData (rowData);
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
		gdSpan.grabExcessHorizontalSpace=true;

//		GridData gdLabel = new GridData();
//		//gdLabel.widthHint    = 100 ;
//		//gdLabel.minimumWidth = 100 ;
        
		GridData gdText = new GridData();
		gdText.widthHint = 200 ;
		gdText.minimumWidth = 200 ;
				
		//-----
        _cbNotNull = new Button(group, SWT.CHECK);
        _cbNotNull.setText("Not null ");
        _cbNotNull.setSelection(false);       
        _cbNotNull.setLayoutData(gdSpan);
        
//		Label label = new Label(group, SWT.NONE);
//		label.setText("");
//		label.setLayoutData(gdLabel);

        return group ;
	}
	
	public boolean onOK() 
	{
		if ( checkViewValues() )
		{
			viewToData() ;
			return true ;
		}
		return false ;
	}
	
	private boolean checkViewValues() 
	{
		//if ( same values )  return false ;
		return true ;
	}
	
	private void viewToData() 
	{
		log( "viewToData() ..." );
		_value.setNotNull ( _cbNotNull.getSelection() );
		_value.setTrueValue ( _textTrueValue.getText() ) ;
		_value.setFalseValue( _textFalseValue.getText() ) ;
	}

	private void dataToView()
	{
		log( "dataToView() ..." );

		dataToNotNullCheckBox(_cbNotNull) ;

		_textTrueValue.setText(_value.getTrueValue() );
		_textFalseValue.setText(_value.getFalseValue() );
		
		log( "dataToView() : END" );
	}

}
