package org.telosys.tools.eclipse.plugin.editors.dbrep;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.generic.model.Cardinality;
import org.telosys.tools.generic.model.CascadeOption;
import org.telosys.tools.generic.model.CascadeOptions;
import org.telosys.tools.generic.model.FetchType;
import org.telosys.tools.generic.model.Optional;
import org.telosys.tools.repository.model.LinkInDbModel;

public class DialogBoxForLink extends TitleAreaDialog
{
	private static final String [] cardinalityAll = 
	{ 
		"One To One", 
		"Many To One",
		"One To Many",
		"Many To Many"
	};

	private static final String [] cardinalityToOne = 
	{ 
		"One To One", 
		"Many To One"
	};

	
	private static final int ONE_TO_ONE   = 0 ;
	private static final int MANY_TO_ONE  = 1 ;
	private static final int ONE_TO_MANY  = 2 ;
	private static final int MANY_TO_MANY = 3 ;
	
	private static final int OPTIONAL_UNDEFINED = 0 ;
	private static final int OPTIONAL_TRUE      = 1 ;
	private static final int OPTIONAL_FALSE     = 2 ;
	
	private static final int FETCH_DEFAULT = 0 ;
	private static final int FETCH_LAZY    = 1 ;
	private static final int FETCH_EAGER   = 2 ;
	
	//--- Widgets
	
	Combo _comboCardinality  = null ;
	Combo _comboFecthType    = null ;
	Text  _textJavaFieldName = null ;
	Combo _comboJavaFieldType = null ; // Unused since v 3.3.0 : TODO : remove it !
	Combo _comboOptional      = null ;
	
	Button _checkBoxCascadeAll     = null ; 
	Button _checkBoxCascadeMerge   = null ; 
	Button _checkBoxCascadePersist = null ; 
	Button _checkBoxCascadeRefresh = null ; 
	Button _checkBoxCascadeRemove  = null ; 

	//--- Current value
	LinkInDbModel _link = null ;

	public DialogBoxForLink( Shell parentShell, LinkInDbModel link ) {
		super(parentShell);
		PluginLogger.log(this, "DialogBox CONSTRUCTOR ..." );
		this.setHelpAvailable(false);	
		_link = link ;
	}

	protected void log(String s)
	{
		PluginLogger.log(this, s );
	}

	protected Control createContents(Composite parent) 
	{
		log( "createContents() ..." );
		Control result = super.createContents(parent);
		this.setTitle("Link from " + _link.getSourceTableName() + " to " + _link.getTargetTableName() );
		// setTitleImage(CustomwidgetPlugin.getCustomImage());
		
		String message ;
		if ( _link.isBasedOnForeignKey() ) {
			message = "Based on Foreign Key \"" + _link.getForeignKeyName() + "\"";
		}
		else if ( _link.isBasedOnJoinTable() ) {
			message = "Based on Join Table \"" + _link.getJoinTableName() + "\"";
		}
		else {
			message = "" ;
		}
		
		setMessage(	message, IMessageProvider.INFORMATION);
		
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

//		RowLayout rowLayout = new RowLayout ();
//		rowLayout.type = SWT.VERTICAL;
//		rowLayout.justify = true;
//		rowLayout.marginLeft  = 10;
//		rowLayout.marginRight = 10;
//		rowLayout.spacing = 10;
//		
//		content.setLayout(rowLayout);

		
		//--- Composite layout
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = false;
		
		content.setLayout(gridLayout);
		
		return content ;
	}

	protected Composite createCascadeOptions(Composite parent) 
	{
		log("createCascadeOptions() ..." );

		Composite content = new Composite(parent, SWT.NONE);

		//--- Composite layout
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.makeColumnsEqualWidth = true;
		
		content.setLayout(gridLayout);
		
		_checkBoxCascadeAll = new Button(content, SWT.CHECK);
		_checkBoxCascadeAll.setText("All");
		
		_checkBoxCascadeMerge   = new Button(content, SWT.CHECK);
		_checkBoxCascadeMerge.setText("Merge");
		
		_checkBoxCascadePersist = new Button(content, SWT.CHECK);
		_checkBoxCascadePersist.setText("Persist");
		
		new Label(content, SWT.NONE).setText("");
		
		_checkBoxCascadeRefresh = new Button(content, SWT.CHECK);
		_checkBoxCascadeRefresh.setText("Refresh");
		
		_checkBoxCascadeRemove  = new Button(content, SWT.CHECK);
		_checkBoxCascadeRemove.setText("Remove");
		 
		return content ;
	}
	
	protected Control createDialogArea(Composite parent) 
	{
		PluginLogger.log(this, "createDialogArea() ..." );

		
		Composite content = createDialogAreaContainer(parent);
		
		//--- Fix the Text widgets size
		GridData gd1 = new GridData();
		gd1.grabExcessHorizontalSpace=true;
		gd1.minimumWidth = 100 ; // in pixels 
		gd1.verticalAlignment = SWT.TOP ;
		gd1.minimumHeight = 24 ;
		gd1.grabExcessVerticalSpace = true ;

		GridData gd2 = new GridData();
		gd2.grabExcessHorizontalSpace=true;
		gd2.minimumWidth = 100 ; // in pixels 
		gd2.verticalAlignment = SWT.TOP ;

		GridData gd2_long = new GridData();
		gd2_long.grabExcessHorizontalSpace=true;
		gd2_long.minimumWidth = 240 ; // in pixels 
		gd2_long.verticalAlignment = SWT.TOP ;

		
		//--- Row ------------------------------
		if ( _link.isOwningSide() ) {
			Label label = new Label(content, SWT.NONE);
			label.setText("Owning side");
			label.setLayoutData (gd1); 
			
			label = new Label(content, SWT.NONE);
			label.setText("");
			label.setLayoutData (gd2_long); 
		} else {
			Label label = new Label(content, SWT.NONE);
			label.setText("Inverse side");
			label.setLayoutData (gd1); 
			
			label = new Label(content, SWT.NONE);
			label.setText("mapped by \"" + _link.getMappedBy() + "\"");
			label.setLayoutData (gd2_long); 
			
		}
		
		//--- Row ------------------------------
		{
			Label label = new Label(content, SWT.NONE);
			label.setText("Cardinality : ");
			label.setLayoutData (gd1); 
		}
		{
			_comboCardinality = new Combo(content, SWT.READ_ONLY );
			_comboCardinality.setItems(cardinalityAll);			
			_comboCardinality.setLayoutData (gd2); 			
		}
		
		//--- Row ------------------------------
		{
			Label label = new Label(content, SWT.NONE);
			label.setText("Target entity : ");
			label.setLayoutData (gd1); 
		}
		{
			Text text = new Text(content, SWT.BORDER | SWT.READ_ONLY );
			text.setText( _link.getTargetEntityClassName() ); // v 3.0.0			
			text.setLayoutData (gd2_long); 
		}
		
		//--- Row ------------------------------
		{
			Label label = new Label(content, SWT.NONE);
			label.setText("Java field type : ");
			label.setLayoutData (gd1); 
		}
		{
			_comboJavaFieldType = new Combo(content, SWT.DROP_DOWN );
			_comboJavaFieldType.setLayoutData (gd2_long); 
		}
		
		//--- Row ------------------------------
		{
			Label label = new Label(content, SWT.NONE);
			label.setText("Java field name : ");
			label.setLayoutData (gd1); 
		}
		{
			_textJavaFieldName  = new Text(content, SWT.BORDER);
			_textJavaFieldName.setLayoutData (gd2_long); 
		}
		

		//--- Row ------------------------------
		{
			Label label = new Label(content, SWT.NONE);
			label.setText("Optional : ");
			label.setLayoutData (gd1); 
		}
		{
			_comboOptional = new Combo(content, SWT.READ_ONLY );
			_comboOptional.add("Undefined");
			_comboOptional.add("True");
			_comboOptional.add("False");			
			_comboOptional.setLayoutData (gd2); 
		}
		
		//--- Row ------------------------------
		{
			Label label = new Label(content, SWT.NONE);
			label.setText("Fetch type : ");
			label.setLayoutData (gd1); 
		}
		{
			_comboFecthType = new Combo(content, SWT.READ_ONLY );
			_comboFecthType.add("Default");
			_comboFecthType.add("Lazy");			
			_comboFecthType.add("Eager");
			_comboFecthType.setLayoutData (gd2); 
		}
		
		//--- Row ------------------------------
		{
			Label label = new Label(content, SWT.NONE);
			label.setText("Cascade : ");
			label.setLayoutData (gd1); 
		}
		{
			createCascadeOptions(content); 
		}
		
		
		dataToView(_link);

		return content;
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
		//group.setLayoutData (_rowData);
		return group ;
	}
	
	protected void okPressed() 
	{
		PluginLogger.log(this, " ==== OK Pressed ");
		if ( checkViewValues() )
		{
			viewToData(_link) ;
			super.okPressed(); // proceed
		}
	}
	
	protected void cancelPressed() 
	{
		PluginLogger.log(this, " ==== CANCEL Pressed ");
		super.cancelPressed(); // proceed
	}
		
	private boolean checkViewValues() 
	{
		//if ( same values )  return false ;
		return true ;
	}
	
	private void viewToData(LinkInDbModel link) {
		log( "viewToData() ..." );

		//--- Cardinality 
		switch ( _comboCardinality.getSelectionIndex() ) 
		{
		case ONE_TO_ONE : 
			link.setCardinality( Cardinality.ONE_TO_ONE ) ; 
			break ;
		case MANY_TO_ONE : 
			link.setCardinality( Cardinality.MANY_TO_ONE ) ;
			break ;
		case ONE_TO_MANY : 
			link.setCardinality( Cardinality.ONE_TO_MANY ) ;
			break ;
		case MANY_TO_MANY : 
			link.setCardinality( Cardinality.MANY_TO_MANY ) ;
			break ;
		}
		
		//--- Java Field Name 
		link.setFieldName( _textJavaFieldName.getText() ); // v 3.0.0
		
		//--- Java Field Type 
		//link.setFieldType( _comboJavaFieldType.getText() ); // unused since v 3.3.0
		
		//--- Optional ( "Undefined"/"True"/"False" ) 
		switch ( _comboOptional.getSelectionIndex() )
		{
		case OPTIONAL_UNDEFINED : link.setOptional( Optional.UNDEFINED ) ; break ; // v 3.0.0
		case OPTIONAL_TRUE      : link.setOptional( Optional.TRUE ) ; break ; // v 3.0.0
		case OPTIONAL_FALSE     : link.setOptional( Optional.FALSE ) ; break ; // v 3.0.0
		}
		
		//--- Fetch Type ( "Default"/"Lazy"/"Eager" ) 
		switch ( _comboFecthType.getSelectionIndex() ) 
		{
		case FETCH_DEFAULT : link.setFetchType( FetchType.DEFAULT ) ; break ;
		case FETCH_LAZY    : link.setFetchType( FetchType.LAZY ) ; break ;
		case FETCH_EAGER   : link.setFetchType( FetchType.EAGER ) ; break ;
		}
		
		//--- Cascade
		if ( true == _checkBoxCascadeAll.getSelection() ) {
			// v 3.0.0
			CascadeOptions cascadeOptions = new CascadeOptions() ;
			cascadeOptions.add(CascadeOption.ALL);
			link.setCascadeOptions(cascadeOptions) ;
		}
		else {
			// v 3.0.0
			CascadeOptions cascadeOptions = new CascadeOptions() ;
			if ( _checkBoxCascadeMerge.getSelection() == true )   cascadeOptions.add(CascadeOption.MERGE);
			if ( _checkBoxCascadePersist.getSelection() == true ) cascadeOptions.add(CascadeOption.PERSIST);
			if ( _checkBoxCascadeRefresh.getSelection() == true ) cascadeOptions.add(CascadeOption.REFRESH);
			if ( _checkBoxCascadeRemove.getSelection() == true )  cascadeOptions.add(CascadeOption.REMOVE);
			link.setCascadeOptions(cascadeOptions) ;
		}
		
		log( "viewToData() : END " );
	}

	private void dataToView(LinkInDbModel link) {
		log( "dataToView() ..." );

		//--- Cardinality 
		if      ( link.getCardinality() == Cardinality.ONE_TO_ONE ) { // v 3.0.0
			_comboCardinality.setItems(cardinalityToOne);			
			_comboCardinality.select(ONE_TO_ONE) ;
			_comboCardinality.setEnabled(true); // can be changed 
		}
		else if ( link.getCardinality() == Cardinality.MANY_TO_ONE ) { // v 3.0.0
			_comboCardinality.setItems(cardinalityToOne);			
			_comboCardinality.select(MANY_TO_ONE) ;
			_comboCardinality.setEnabled(true); // can be changed 
		}
		else if ( link.getCardinality() == Cardinality.ONE_TO_MANY ) { // v 3.0.0
			_comboCardinality.setItems(cardinalityAll);			
			_comboCardinality.select(ONE_TO_MANY) ;
			_comboCardinality.setEnabled(false); // cannot be changed 
		}
		else if ( link.getCardinality() == Cardinality.MANY_TO_MANY ) { // v 3.0.0
			_comboCardinality.setItems(cardinalityAll);			
			_comboCardinality.select(MANY_TO_MANY) ;
			_comboCardinality.setEnabled(false); // cannot be changed 
		}
		
		if ( link.isInverseSide() ) {
			_comboCardinality.setEnabled(false); // cannot be changed if INVERSE SIDE 
		}
		
		//--- Java Field Name 
		_textJavaFieldName.setText( link.getFieldName() ); // v 3.0.0

		//--- Java Field Type 
		populateComboJavaFieldType(link);
		
		//--- Optional ( "Undefined"/"True"/"False" ) 
		if  ( link.isCardinalityOneToOne() || link.isCardinalityManyToOne() ) { // v 3.0.0
			if ( link.getOptional() == Optional.UNDEFINED ) _comboOptional.select( OPTIONAL_UNDEFINED ); // v 3.0.0
			if ( link.getOptional() == Optional.TRUE      ) _comboOptional.select( OPTIONAL_TRUE ); // v 3.0.0
			if ( link.getOptional() == Optional.FALSE     ) _comboOptional.select( OPTIONAL_FALSE ); // v 3.0.0
			_comboOptional.setEnabled(true);// can be changed
		}
		else {
			_comboOptional.select( OPTIONAL_UNDEFINED );
			_comboOptional.setEnabled(false);// cannot be changed
		}
		
		//--- Fetch Type ( "Default"/"Lazy"/"Eager" ) 
		if ( link.getFetchType() == FetchType.DEFAULT ) _comboFecthType.select( FETCH_DEFAULT ); // v 3.0.0
		if ( link.getFetchType() == FetchType.LAZY    ) _comboFecthType.select( FETCH_LAZY ); // v 3.0.0
		if ( link.getFetchType() == FetchType.EAGER   ) _comboFecthType.select( FETCH_EAGER ); // v 3.0.0
		
		//--- Cascade
		// v 3.0.0
		CascadeOptions cascadeOptions = link.getCascadeOptions() ; // never null
		_checkBoxCascadeAll.setSelection( cascadeOptions.isCascadeAll() ) ;
		_checkBoxCascadeMerge.setSelection ( cascadeOptions.isCascadeMerge() ) ;
		_checkBoxCascadePersist.setSelection ( cascadeOptions.isCascadePersist() ) ;
		_checkBoxCascadeRefresh.setSelection ( cascadeOptions.isCascadeRefresh() ) ;
		_checkBoxCascadeRemove.setSelection ( cascadeOptions.isCascadeRemove() ) ;
		
		log( "dataToView() : END" );
	}

	// Unused since v 3.3.0
//	private static final String [] collectionItems = 
//		{ 
//		"java.util.List", 
//		"java.util.Set",
//		"java.util.Collection",
//		"java.util.Map"
//		};
	
	private void populateComboJavaFieldType(LinkInDbModel link)
	{
//		Cardinality cardinality = link.getCardinality();
//		if ( cardinality != null ) {
//			if ( cardinality == Cardinality.MANY_TO_ONE || cardinality == Cardinality.ONE_TO_ONE ) { 
//				// ...ToOne : offers only 2 choices : void or Java Target
//				String [] entityItems = {
//						link.getTargetEntityClassName() 
//						};
//				_comboJavaFieldType.setItems(entityItems);
//				selectComboJavaFieldType( link.getFieldType() ); 
//			}
//			else if ( cardinality == Cardinality.ONE_TO_MANY || cardinality == Cardinality.MANY_TO_MANY ) { 
//				// ...ToMany 
//				_comboJavaFieldType.setItems(collectionItems);
//				selectComboJavaFieldType( link.getFieldType() );
//			}
//		}
//		else {
//			String [] nothing = { "" };
//			_comboJavaFieldType.setItems(nothing);
//			_comboJavaFieldType.select(0);
//		}
		// Unused since v 3.3.0
		String [] nothing = { "" };
		_comboJavaFieldType.setItems(nothing);
		_comboJavaFieldType.select(0);
	}

// Unused since v 3.3.0
//	private void selectComboJavaFieldType( String type )
//	{
//		if ( null == type ) {
//			_comboJavaFieldType.setText("");
//			return ;
//		}
//		else {
//			// Try to found the given type in the items list 
//			String [] items = _comboJavaFieldType.getItems();
//			for ( int i = 0 ; i < items.length ; i++ ) {
//				if ( type.equals(items[i]) ) {
//					_comboJavaFieldType.select(i);
//					return ;
//				}
//			}
//			// Not found 
//			_comboJavaFieldType.setText(type);
//		}
//	}
}
