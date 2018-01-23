package org.telosys.tools.eclipse.plugin.editors.dbconfig;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.telosys.tools.commons.dbcfg.DatabaseType;
import org.telosys.tools.commons.dbcfg.DatabaseTypeProvider;
import org.telosys.tools.commons.dbcfg.DatabasesConfigurations;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

public class DialogBoxNewDatabase extends TitleAreaDialog 
{
	private final static int ROW_WIDTH = 500 ;
	
//	private RowData _rowData = new RowData(ROW_WIDTH, SWT.DEFAULT);
	
	private final DatabasesConfigurations databasesConfigurations ;

	//--- Widgets 
	private Combo comboDatabaseId = null ;
	private Combo comboDatabaseType = null ;

	//--- Data selected in this Dialog Box 
	private int          selectedDatabaseId   = -1 ;
	private DatabaseType selectedDatabaseType = null ;
	
	public DialogBoxNewDatabase(Shell parentShell, DatabasesConfigurations databasesConfigurations) 
	{
		super(parentShell);
		PluginLogger.log(this, "DialogBox CONSTRUCTOR ..." );
		this.setHelpAvailable(false);	
		this.databasesConfigurations = databasesConfigurations ;
	}
	
	private void log(String s)
	{
		PluginLogger.log(this, s );
	}
	
	protected Control createContents(Composite parent) 
	{
		log( "createContents() ..." );		
		Control result = super.createContents(parent);
		this.setTitle("New database configuration");
		// setTitleImage(CustomwidgetPlugin.getCustomImage());
		return result;
	}
	
	protected Control createDialogArea(Composite parent) 
	{
		log( "createDialogArea() ..." );
		Composite content = createDialogAreaContainer(parent);
		
		populateDatabaseIdCombo();
		populateDatabaseTypeCombo();
		
		return content ;
	}
	
	/**
	 * Creates a Composite with a "RowLayout"
	 * @param parent
	 * @return
	 */
	private Composite createDialogAreaContainer(Composite parent) 
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
		
		createDialogAreaGroup(content); 
		
		return content ;
	}
	
	/**
	 * Create a standard group for all types of fields with <br>
	 *  . field label <br>
	 *   
	 * @param container
	 * @return
	 */
	private Group createDialogAreaGroup(Composite container)
	{
		log( "createDialogAreaGroup() ..." );
		Group group = new Group(container, SWT.NONE);
		//group.setText(text);
		group.setLayoutData ( new RowData(ROW_WIDTH, SWT.DEFAULT) );
		
		//--- Group layout
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.verticalSpacing = 20 ;
		gridLayout.marginTop = 6 ;
		gridLayout.marginBottom = 12 ;
		gridLayout.makeColumnsEqualWidth = false;
        group.setLayout(gridLayout);

		GridData gdLabel = new GridData();
		Label label ;

		//-------------------------------------------------------------------
		//--- Database ID 
		//-------------------------------------------------------------------
		//--- Label 
		label = new Label(group, SWT.NONE);
		label.setText("Database id : ");
		label.setLayoutData(gdLabel);

		//--- Combo Box 
		GridData gdComboDatabaseId = new GridData();
		gdComboDatabaseId.widthHint    = 40 ;
		gdComboDatabaseId.minimumWidth = 40 ;
		comboDatabaseId = new Combo(group, SWT.BORDER | SWT.READ_ONLY);
		comboDatabaseId.setVisibleItemCount(8);
		comboDatabaseId.setLayoutData(gdComboDatabaseId);
		
		//-------------------------------------------------------------------
		//--- Database type
		//-------------------------------------------------------------------
		//--- Label 
		label = new Label(group, SWT.NONE);
		label.setText("Database type : ");
		label.setLayoutData(gdLabel);

		//--- Combo Box 
		GridData gdComboDatabaseType = new GridData();
		gdComboDatabaseType.widthHint    = 260 ;
		gdComboDatabaseType.minimumWidth = 260 ;
		comboDatabaseType = new Combo(group, SWT.BORDER | SWT.READ_ONLY);
		comboDatabaseType.setVisibleItemCount(12);
		comboDatabaseType.setLayoutData(gdComboDatabaseType);

		//-------------------------------------------------------------------
		return group ;

	}
	
//	/**
//	 * Creates a Group with the given title text and the standard LayoutData ( "RowData" )
//	 * @param container
//	 * @param text
//	 * @return
//	 */
//	private Group createDialogAreaGroup(Composite container, String text )
//	{
//		Group group = new Group(container, SWT.NONE);
//		if ( text != null ) {
//			group.setText(text);
//		}
//		group.setLayoutData (_rowData);
//		return group ;
//	}
	
	protected void okPressed() 
	{
		PluginLogger.log(this, " ==== OK Pressed ");
		
		//--- Keep the value before widgets are disposed
		int index = comboDatabaseId.getSelectionIndex() ; // 
		if ( index >= 0 ) {
			String s = comboDatabaseId.getItem(index) ;			
			try {
				this.selectedDatabaseId = Integer.parseInt(s);
			} catch (NumberFormatException e) {
				this.selectedDatabaseId = -1 ;
			}
		}
		else {
			this.selectedDatabaseId = -1 ;
		}

		index = comboDatabaseType.getSelectionIndex() ; // 
		if ( index >= 0 ) {
			String dbTypeName = comboDatabaseType.getItem(index) ;
			this.selectedDatabaseType = DatabaseTypeProvider.getDatabaseTypeByName(dbTypeName);
		}
		else {
			this.selectedDatabaseType = null ;
		}
		
		super.okPressed();
	}
	
	protected void cancelPressed() 
	{
		PluginLogger.log(this, " ==== CANCEL Pressed ");
		super.cancelPressed();
	}
	
	private void populateDatabaseIdCombo()
	{
		int n = databasesConfigurations.getNumberOfDatabases();
		int max = 10 + n ;
		for ( int i = 0 ; i < max ; i++ ) {
			if ( null == databasesConfigurations.getDatabaseConfiguration(i) ) {
				// this id is not used 
				comboDatabaseId.add(""+i);
			}
		}
		//comboDatabaseId.getItemCount()
		comboDatabaseId.select(0);
	}
	
	private void populateDatabaseTypeCombo()
	{
		PluginLogger.log(this, "populateDatabaseTypeCombo" );
		for ( DatabaseType t : DatabaseTypeProvider.getDbTypesList() ) {
			comboDatabaseType.add( t.getName() );
		}
	}
	
	/**
	 * Returns the selected database id (or -1 if none)
	 * @return
	 */
	public int getDatabaseId()
	{
		// NB : cannot use SWT widgets here  ( SWTException: Widget is disposed )
		return selectedDatabaseId ;
	}
	
	/**
	 * Returns the selected database type (or null if none)
	 * @return
	 */
	public DatabaseType getDatabaseType()
	{
		// NB : cannot use SWT widgets here  ( SWTException: Widget is disposed )
		return selectedDatabaseType ;
	}
	
	
}
