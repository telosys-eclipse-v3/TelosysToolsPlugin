package org.telosys.tools.eclipse.plugin.editors.dbrep;

import java.util.Arrays;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.javatypes.JavaTypes;
import org.telosys.tools.commons.javatypes.JavaTypesManager;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.Util;
import org.telosys.tools.eclipse.plugin.editors.commons.AbstractModelEditorPage;
import org.telosys.tools.repository.model.AttributeInDbModel;
import org.telosys.tools.repository.model.EntityInDbModel;
import org.telosys.tools.repository.model.ForeignKeyColumnInDbModel;
import org.telosys.tools.repository.model.ForeignKeyInDbModel;
import org.telosys.tools.repository.model.RepositoryModel;


/**
 * First page of the editor : Model attributes mapping and foreign keys <br>
 *  
 */
///* package */  class RepositoryEditorPage1 extends RepositoryEditorPage 
/* package */  class RepositoryEditorPageModelEntities extends AbstractModelEditorPage
{

//	private final static int GROUP_X = 12 ;
//	private final static int GROUP_WIDTH = 600 ;
	
//	private final static int TEXT_X = 10 ;
//	private final static int TEXT_WIDTH = 180 ;

	private final static int TEXT_HEIGHT = 24 ;
	
	// Style for basic tables 
	private final static int BASIC_TABLE_STYLE = SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL 
		| SWT.SINGLE | SWT.HIDE_SELECTION | SWT.FULL_SELECTION ;
	// SWT.SINGLE : Create a single-select table
	// SWT.MULTI  : Create a multiselect table
	// SWT.FULL_SELECTION : The selection is expanded to fill an entire row
	// SWT.HIDE_SELECTION : The selection is hidden when focus is lost

	//-----------------------------------------------------------------

	private boolean  _bPopulateInProgress = false ;
	
	//-----------------------------------------------------------------
	//private Entity _currentEntity = null ; 
	private EntityInDbModel _currentEntity = null ; // v 3.0.0
	
	//-----------------------------------------------------------------
	
	//--- ComboBox "Tables/Entities"
	private Combo       _comboTables  = null ;
	
	private Label       _labelCatalog = null ;
	private Label       _labelSchema  = null ;
	
	//--- Tab "Mapping" 
	private TableViewer _tableViewer = null ;
	
	private Text   _textJavaBeanClass  = null ;

	//--- Tab 2 : "Foreign Keys" 
	private Table  _tableForeignKeys     = null ;

//	//--- Tab 3 : "Generation Targets" 
//	private Table  _tableSpecificTargets = null ;
//	
//	private BundleComboBox  _comboBundles = null ;

	/**
	 * @param editor
	 * @param id
	 * @param title 
	 */
	//public RepositoryEditorPage1(FormEditor editor, String id, String title, List<TargetDefinition> targetsList) {
	public RepositoryEditorPageModelEntities(FormEditor editor, String id, String title ) {
		super(editor, id, title);
		//super(editor, id, null); // ERROR if title is null
		log(this, "constructor(.., '"+id+"', '"+ title +"')..." );
		//this.initialTargetsList = targetsList ;
	}

	protected boolean isPopulateInProgress()
	{
		return _bPopulateInProgress ;
	}
	
	private final static int BEAN_JAVA_CLASS      = 1 ;
	
	protected String getModelValue( int id )
	{
		if ( _currentEntity != null )
		{
			switch (id) 
			{
			//case BEAN_JAVA_CLASS :      return _currentEntity.getBeanJavaClass() ;
			case BEAN_JAVA_CLASS :      return _currentEntity.getClassName() ;
			}
			MsgBox.error("getModelValue("+id+") : unknown id !");
		}
		else
		{
			MsgBox.error("getModelValue("+id+") : current entity is null !");
		}
		return "" ;
	}
	
	protected void setModelValue( int id, String v )
	{		
		if ( _currentEntity != null )
		{
			switch (id) 
			{
			case BEAN_JAVA_CLASS :      
				//_currentEntity.setBeanJavaClass(v) ; 
				_currentEntity.setClassName(v) ; 
				break ;
			default : 
				MsgBox.error("getModelValue("+id+") : unknown id !"); 
				return ;
			}
		}
		else
		{
			MsgBox.error("getModelValue("+id+") : current entity is null !");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) 
	{
		super.createFormContent(managedForm);
		
//		log(this, "createFormContent(..)..." );
//		Control pageControl = getPartControl();
//		
//		if ( pageControl != null )
//		{
//			log(this, "createFormContent(..) : getPartControl() != null " );
//		}
//		else
//		{
//			log(this, "createFormContent(..) : getPartControl() is null !!! " );
//			return ;
//		}
		
//		if ( pageControl instanceof Composite )
//		{
//			log(this, "- pageControl is a Composite  " );
//			// Yes pageControl is a Composite
//
//			log(this, "- pageControl class = " + pageControl.getClass() );
//			// class = org.eclipse.ui.forms.widgets.ScrolledForm
//			
//			Composite pageComposite = (Composite) pageControl ;
//			Layout layout = pageComposite.getLayout();			
//			log(this, "- pageControl layout class = " + layout.getClass() );
//			// layout = org.eclipse.swt.custom.ScrolledCompositeLayout
//		}
//		else
//		{
//			log(this, "- pageControl() is NOT a Composite !!! " );
//		}

		// What do we have here ?
		// * pageControl (Composite)
		//  . class  : org.eclipse.ui.forms.widgets.ScrolledForm ( see API JavaDoc )
		//  . layout : org.eclipse.swt.custom.ScrolledCompositeLayout
		// * body 
		//  . class  : org.eclipse.ui.forms.widgets.LayoutComposite ( no API doc ! )
		//  . layout : none
		//
		/* Example from API doc :
		  ScrolledForm form = toolkit.createScrolledForm(parent);
		  form.setText("Sample form");
		  form.getBody().setLayout(new GridLayout());
		  toolkit.createButton(form.getBody(), "Checkbox", SWT.CHECK);
		*/
		
		ScrolledForm scrolledForm = managedForm.getForm();
		
		//--- Page title 
		//scrolledForm.setText( _repEditor.getDatabaseTitle() );
		
		scrolledForm.setExpandHorizontal(true);
		
//		Specify the minimum width at which the ScrolledComposite will begin scrolling 
//		the content with the horizontal scroll bar. 
//		This value is only relevant if setExpandHorizontal(true) has been set. 
//		scrolledForm.setMinWidth(700);

//		form.setExpandHorizontal(false); // tout bout à bout !
//		form.setExpandVertical(false);
		
		//scrolledForm.setMinWidth(700);
		
		Composite scrolledFormBody = scrolledForm.getBody();
		log(this, "- body class = " + scrolledFormBody.getClass() );
		
		Layout layout = scrolledFormBody.getLayout();			
		if ( layout != null )
		{
			log(this, "- body layout class = " + layout.getClass() );
		}
		else
		{
			log(this, "- body layout class = NO LAYOUT ! ");
		}
		
		//Layout bodyLayout = new RowLayout(SWT.VERTICAL);
		//--- New layout in v 1.0 : Grid with only 1 column
		GridLayout bodyLayout = new GridLayout(1, false); 
		
		// marginWidth specifies the number of pixels of horizontal margin 
		// that will be placed along the left and right edges of the layout. The default value is 5.
		//bodyLayout.marginWidth = 20 ;
//		bodyLayout.marginWidth = RepositoryEditor.LAYOUT_MARGIN_WIDTH ;
		bodyLayout.marginWidth = LAYOUT_MARGIN_WIDTH ;
		
		scrolledFormBody.setLayout( bodyLayout );
		
		//---------------------------------------------------------------
		// Line 0 - Column 1 : The page title
		//---------------------------------------------------------------
		//Util.setPageTitle(scrolledFormBody, "Entities mapping and generation" ) ;
		Util.setPageTitle(scrolledFormBody, this.getTitle() ) ; // Title defined in the constructor
		
		//---------------------------------------------------------------
		// Line 1 - Column 1 in the "body layout"
		//---------------------------------------------------------------
		createPageHeaderGroup(scrolledFormBody);
		
		//---------------------------------------------------------------
		// Line 2 - Column 1 in the "body layout"
		//---------------------------------------------------------------
		//--- Tab Folder 
		Composite composite = null ;

		composite = new Composite(scrolledFormBody, SWT.NONE); // Add it in the form body
		composite.setLayout(new FillLayout());
		
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL)); // new #LGU
		//composite.setLocation(GROUP_X, 100);
		
		// #LGU changed for tests
		//composite.setSize(760, 200); 
		
		//composite.setBackground( getBackgroundColor() );
		

		TabFolder tabFolder = new TabFolder(composite, SWT.NONE);
		
		//tabFolder.setLocation(GROUP_X, 100);
		//tabFolder.setSize(400, 200);
		//tabFolder.setBackground( getBackgroundColor() ); // No effect : cannot change the TabFolder color 

		//--- Creates the 3 tabs 
		createTabFolderMapping(tabFolder);
		createTabFolderForeignKeys(tabFolder);
		// createTabFolderGeneration(tabFolder);

		//--------------------------------------------------------------
		
		populateComboEntities(_comboTables) ;
	}
	
	//----------------------------------------------------------------------------------------------
	private Composite createPageHeaderGroup(Composite scrolledFormBody) 
	{
		//--- The group "Database table"
		//Group group1 = new Group(scrolledFormBody, SWT.NONE); // Add it in the form body
		//Composite group1 = new Composite(scrolledFormBody, SWT.NONE | SWT.BORDER );
		Composite group1 = new Composite(scrolledFormBody, SWT.NONE );
		
		//group1.setText("Database");
		
	    GridData gd = new GridData (GridData.FILL_HORIZONTAL);
//	    gd.heightHint    = 100 ;
	    gd.heightHint    = 50 ;
	    
	    group1.setLayoutData(gd);
		
		//group1.setSize(900, 120); // No effect !
		
//        group1.setBackground( getBackgroundColor() );

		int width = 200;
//		int x0 =  10;
//		int x1 = 220;
//		int x2 = 420;
		int y1 = 2 ; //20 ;
		int y2 = 22 ; //40 ;
		int fillerWidth = 20 ;
		
    	Label label ;
    	
    	int x = 0 ;

    	//--- The Combo Box for "table/entity" selection
    	label = new Label(group1, SWT.NULL);
    	//label = new Label(group1, SWT.BORDER );
    	label.setText("Database table name : ");
    	label.setSize(width, 20);
    	label.setLocation(x, y1);

    	_comboTables = new Combo(group1, SWT.BORDER | SWT.READ_ONLY);
		//_comboTables.setBounds(TEXT_X, 25, TEXT_WIDTH, TEXT_HEIGHT);
		_comboTables.setSize(width, TEXT_HEIGHT);
		_comboTables.setLocation(x, y2);
		_comboTables.setVisibleItemCount(20); // Show a list of N items 
		setTablesComboAction(_comboTables);
		
    	x = x + width + fillerWidth ;
    	
    	//--- The "Catalog"
    	label = new Label(group1, SWT.NULL);
    	//label = new Label(group1, SWT.BORDER );
    	label.setText("Catalog : ");
    	label.setSize(width, 20);
    	label.setLocation(x, y1);

    	_labelCatalog = new Label(group1, SWT.BORDER );
    	_labelCatalog.setText("");
    	_labelCatalog.setSize(width, TEXT_HEIGHT);
    	_labelCatalog.setLocation(x, y2);

    	x = x + width + fillerWidth ;

    	//--- The "Schema"
    	label = new Label(group1, SWT.NULL);
    	//label = new Label(group1, SWT.BORDER );
    	label.setText("Schema : ");
    	label.setSize(width, 20);
    	label.setLocation(x, y1);

    	_labelSchema = new Label(group1, SWT.BORDER );
    	_labelSchema.setText("");
    	_labelSchema.setSize(width, TEXT_HEIGHT);
    	_labelSchema.setLocation(x, y2);

    	
    	//--------------------------------------------------------------------------
    	
    	x = x + width + fillerWidth ;
		
		//-----  Entity Class name ( Java Bean class )
    	label = new Label(group1, SWT.NULL);
    	label.setText("Entity class name : ");
    	label.setSize(width, 20);
    	label.setLocation(x, y1);

		_textJavaBeanClass = new Text(group1, SWT.BORDER);
		_textJavaBeanClass.setText("");
		_textJavaBeanClass.setSize(width, TEXT_HEIGHT);
		_textJavaBeanClass.setLocation(x, y2);
		//setModifyListener(_TextVOBeanClass, DatabaseRepository.TABLE_VO_FILE_GENERATE_ATTRIBUTE );
		_textJavaBeanClass.addModifyListener( new GenericModifyListener(this, BEAN_JAVA_CLASS) );
		   	
    	return group1;
	}
	//----------------------------------------------------------------------------------------------
	private void populateComboEntities(Combo combo) 
	{
		log(this, "Populate combo Tables/Entities..." );

		RepositoryModel repositoryModel = getRepositoryModel();
		
		combo.removeAll(); 
		String[] tableNames = repositoryModel.getEntitiesNames();
		
		Arrays.sort(tableNames); // Sort by alphabetical order
		
		if ( tableNames != null )
		{
			for ( int i = 0 ; i < tableNames.length ; i++ )
			{
				//combo.add(tableNames[i]);
				// ver 2.0.7
				String entityName = tableNames[i];
				//Entity entity = repositoryModel.getEntityByName(entityName);
				EntityInDbModel entity = repositoryModel.getEntityByTableName(entityName);
				if ( ! StrUtil.nullOrVoid( entity.getDatabaseType() ) ) {
					if ( ! entity.getDatabaseType().equalsIgnoreCase("TABLE") ) {
						entityName = entityName + "   (" + entity.getDatabaseType() + ")" ;
					}
				}
				combo.add(entityName);
			}
		}
		combo.setData(tableNames); // ver 2.0.7
		
		log(this, "Populate TABLES combo : done." );
	}
	//----------------------------------------------------------------------------------------------
	private Composite createTabFolderContainer(TabFolder tabFolder) 
	{
		log(this, "createTabFolderContainer() ..." );
		Composite tabContent = new Composite(tabFolder, SWT.NONE);
	/***
		//FillLayout layout = new FillLayout();
		RowLayout layout = new RowLayout();
		
		layout.type = SWT.VERTICAL; // for RowLayout
		
		layout.marginHeight = 5;
		layout.marginWidth  = 5;
		tabContent.setLayout(layout);
	***/
	    GridLayout gridLayout = new GridLayout();
	    gridLayout.numColumns = 1 ;
	    gridLayout.horizontalSpacing = 30 ;
		
	    tabContent.setLayout(gridLayout); 
	    
		return tabContent ;
	}
	//----------------------------------------------------------------------------------------------
	private GridData createStandardTableGridData() {
	    GridData gd = new GridData (GridData.FILL_HORIZONTAL);
	    gd.minimumHeight = 400 ;
	    gd.minimumWidth  = 500 ;
	    gd.heightHint    = 400 ;
	    return gd ;
	}
	//----------------------------------------------------------------------------------------------
	private void createTabFolderMapping(TabFolder tabFolder) {
		log(this, "createTabFolder1() ..." );

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(" Mapping table - object ");
		//tabItem.setBackground( _backgroundColor );

		Composite tabContent = createTabFolderContainer(tabFolder);
		
		//----- Table for mapping Database columns - Java fields
		
//		//--- Create the standard "SWT Table"
//	    GridData gd1 = new GridData (GridData.FILL_HORIZONTAL);
//	    gd1.minimumHeight = 400 ;
//	    gd1.minimumWidth  = 500 ;
//	    gd1.heightHint    = 400 ;
	    
		
		Table table = createTableMapping(tabContent);
//		table.setLayoutData (gd1);
		table.setLayoutData ( createStandardTableGridData() );
		
		tabItem.setControl(tabContent);
	}
	
	//----------------------------------------------------------------------------------------------
	private void createTabFolderForeignKeys(TabFolder tabFolder) 
	{
		log(this, "createTabFolderForeignKeys() ..." );
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(" Foreign keys ");
		
		Composite tabContent = createTabFolderContainer(tabFolder);

//	    GridData gd1 = new GridData (GridData.FILL_HORIZONTAL);
//	    gd1.minimumHeight = 400 ;
//	    gd1.minimumWidth  = 500 ;
//	    gd1.heightHint    = 400 ;
	    		
		_tableForeignKeys = createTableForeignKeys( tabContent );
		//_tableForeignKeys.setLocation(20, 20);
//		_tableForeignKeys.setLayoutData (gd1);
		_tableForeignKeys.setLayoutData ( createStandardTableGridData() );
		
		tabItem.setControl(tabContent);
		
		//tabItem.setControl(_tableForeignKeys);
	}
	
	//----------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------
//	/**
//	 * Refresh the targets table from the current configuration supposed to be up to date 
//	 */
//	//protected void refreshTargetsTable()
//	protected void refreshTargetsTable(List<TargetDefinition> targetslist)	
//	{
//		log("refreshTargetsTable");
//		_comboBundles.refresh();
//		//--- Re-populate the SWT table
//		populateTableGenerationTargets(_tableSpecificTargets, targetslist);
//	}
	//----------------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
		log(this, "init(..,..)..." );
		log(this, "init(..,..) : site id = '" + site.getId() 
				+ "'  input name = '" + input.getName() + "'");
	}
	
	
	private Table createTableMapping(Composite composite)
	{
		log(this, "createTable(..)..." );
		
		// Table style
		// SWT.CHECK : check box in the first column of each row
//		int iTableStyle = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK ;
		
//		int iTableStyle = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | 
//		SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		
		int iTableStyle = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL 
						| SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.CHECK ;
		Table table = new Table(composite, iTableStyle);
		
		
		//table.setBounds(10, 20, 600, 400);
		//table.setSize(650, 400);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		//--- Columns
		TableColumn col = null ;
		int iColumnIndex = 0 ;

		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("");
		col.setWidth(22);
		
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("");
		col.setWidth(18);
		
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Database Name");
		col.setWidth(150);
		
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Database Type");
		col.setWidth(150);
		
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("JDBC Type");
		col.setWidth(120);
		
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Attribute Name");
		col.setWidth(150);
		
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Attribute Type");
		col.setWidth(120);
		
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Further info");
		col.setWidth(160);
		
		//--- Create the "jFace Table Viewer"
		_tableViewer = createTableViewerMapping(table);
		
		setSelectionListener(table);
		
		return table;
	}
	
	private TableViewer createTableViewerMapping(Table table) {

		log(this, "createTableViewer(..)..." );
		TableViewer tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		
		//--- Set the columns names ( used to identify the column in a cell modifier )
		tableViewer.setColumnProperties(ColumnNames.NAMES);

		// Create the cell editors
		CellEditor[] editors = new CellEditor[ColumnNames.NAMES.length];

		// Column 0 : "Selection" => Checkbox
		editors[0] = new TextCellEditor(table);

		// Column 1 : "Primary Key" => Image
		editors[1] = new TextCellEditor(table);

		// Database Column Name
		editors[2] = new TextCellEditor(table);
		
		// Database Column Type
		editors[3] = new TextCellEditor(table);
		
		// Column 4 : "JDBC Type"
		editors[4] = new TextCellEditor(table);
		
		// Column 5 : "Attribute Name" => Free text editor
		TextCellEditor textEditor = new TextCellEditor(table);
		((Text) textEditor.getControl()).setTextLimit(60);
		editors[5] = textEditor;

		// Column 6 : "Attribute Type" => Combo Box editor
		//editors[6] = new ComboBoxCellEditor(table, ComboBoxJavaTypeItems.JAVA_TYPE, SWT.READ_ONLY);
		JavaTypes types = JavaTypesManager.getJavaTypes();		
		ComboBoxCellEditor cbEditor = new ComboBoxCellEditor(table, types.getTexts(), SWT.READ_ONLY);
		Control control = cbEditor.getControl();
		if ( control instanceof CCombo )
		{
			CCombo combo = (CCombo) control ;
			combo.setVisibleItemCount(10);
		}
		//control.setSize(100, 200);
		editors[6] = cbEditor ;

		// Column 7 : "Special" => no editor
		editors[7] = new SpecialDialogCellEditor(table);
		//editors[7] = new ColorCellEditor(table);
		//editors[7] = new TextCellEditor(table);
		
		//--- Set the "Cell Editors" for this table viewer
		tableViewer.setCellEditors(editors);
		
		//--- Set the "Cell Modifier" for this table viewer
		tableViewer.setCellModifier(new TableViewerCellModifier((RepositoryEditor) this.getEditor()));

		// Set the default sorter for the viewer 
		//tableViewer.setSorter(new ExampleTaskSorter(ExampleTaskSorter.DESCRIPTION));
		
		//--- Set the "Content Provider" for this table viewer
		tableViewer.setContentProvider(new TableViewerContentProvider());
		
		//--- Set the "Label Provider" for this table viewer
		tableViewer.setLabelProvider(new TableViewerLabelProvider());
		
		return tableViewer ;
	}
	
	private void loadTableEntity( String sTableName )
	{
		log(this, "loadTableEntity('" + sTableName + "')");
		
//		RepositoryModel repositoryModel = getRepositoryModel();
		RepositoryModel repositoryModel = getRepositoryModel();

		//_currentEntity = repositoryModel.getEntityByName(sTableName);
		_currentEntity = repositoryModel.getEntityByTableName(sTableName);

		//--- Tab 1
		populateClassesFields(_currentEntity);
		populateTableMapping(_currentEntity);
		
		//--- Tab 2
		populateTableForeignKeys(_currentEntity, _tableForeignKeys);

	}
	
	//private void populateTableMapping(Entity entity)
	private void populateTableMapping(EntityInDbModel entity)
	{
		log(this, "populateTableMapping()...");
		
		//--- Init the "jFace Table Viewer" with the table rows
		
//		_labelCatalog.setText( blankIfNull( entity.getCatalog() ) );
//		_labelSchema.setText(  blankIfNull( entity.getSchema()  ) );
		_labelCatalog.setText( blankIfNull( entity.getDatabaseCatalog() ) );
		_labelSchema.setText(  blankIfNull( entity.getDatabaseSchema()  ) );
			
		log(this, "populateTableWidgets() : before setInput() ---");
		_tableViewer.setInput(entity); 
		//----------
		// This call to "setInput" is propagated to 
		// 1) TableViewerContentProvider.inputChanged()
		// 2) TableViewerContentProvider.getElements()
		// 3) for each row : TableViewerLabelProvider.getColumnText() and TableViewerLabelProvider.getColumnImage()
		//----------
		log(this, "populateTableWidgets() : after setInput() ---");
		
		//--- Check selected columns 
		Table table = _tableViewer.getTable();
		TableItem[] tableItems = table.getItems();
		for ( int i = 0 ; i < tableItems.length ; i++ )
		{
			TableItem item = tableItems[i];
			//Column column = TableUtil.getTableColumn( item ) ;
			AttributeInDbModel column = TableUtil.getTableColumn( item ) ;
			//item.setChecked(column.getSelected());
			item.setChecked(column.isSelected());
		}
	}
	
	//private void populateClassesFields(Entity entity) {
	private void populateClassesFields(EntityInDbModel entity) { // v 3.0.0
		log(this, "populateFields()...");		
		_bPopulateInProgress = true ;
		
		//_textJavaBeanClass.setText( blankIfNull( entity.getBeanJavaClass() ) );
		_textJavaBeanClass.setText( blankIfNull( entity.getClassName() ) ); // v 3.0.0
		
		_bPopulateInProgress = false ;
	}
	
	private String blankIfNull( String s ) {
		return ( s != null ? s : "" ) ;
	}
	
	//private void populateTableForeignKeys(Entity entity, Table table) {
	private void populateTableForeignKeys(EntityInDbModel entity, Table table) { // v 3.0.0
		log(this, "populateForeignKeysTable()...");
		
		//ForeignKey[] foreignKeys = entity.getForeignKeys();
		ForeignKeyInDbModel[] foreignKeys = entity.getForeignKeys(); // v 3.0.0
		
		if ( foreignKeys != null )
		{
			table.removeAll();
	        TableItem tableItem ; 
			int n = foreignKeys.length ;
			for ( int i = 0 ; i < n ; i++ ) // For each Foreign Key 
			{
				//ForeignKey fk = foreignKeys[i];
				ForeignKeyInDbModel fk = foreignKeys[i]; // v 3.0.0
				
				//ForeignKeyColumn fkColumns[] = fk.getForeignKeyColumns();
				ForeignKeyColumnInDbModel fkColumns[] = fk.getForeignKeyColumns(); // v 3.0.0

				for ( int c = 0 ; c < fkColumns.length ; c++ )  // For each column of the Foreign Key 
				{
					//ForeignKeyColumn fkCol = fkColumns[c];
					ForeignKeyColumnInDbModel fkCol = fkColumns[c];
					
					//--- Create a new row in the table
					tableItem = new TableItem(table, SWT.NONE );
					int col = 0 ;
					String name = ( c == 0 ? fk.getName() : "" ) ;

					tableItem.setText(col++, name );

					tableItem.setText(col++, fkCol.getColumnName() );			
			        
					tableItem.setText(col++, fkCol.getTableRef() );
			        
					//tableItem.setText(col++, fkCol.getColumnRef());
					tableItem.setText(col++, fkCol.getReferencedColumnName()); // v 3.0.0
	
					tableItem.setText(col++, fkCol.getUpdateRuleCode() + " : " + fkCol.getUpdateRuleText() );
			        
					tableItem.setText(col++, fkCol.getDeleteRuleCode() + " : " + fkCol.getDeleteRuleText() );
			        
					tableItem.setText(col++, fkCol.getDeferrableCode() + " : " + fkCol.getDeferrableText() );
			        
				}
			}
		}
		
	}
	
	private Table createTableForeignKeys(Composite composite)
	{
		log(this, "createForeignKeysTable(..)..." );

		Table table = new Table(composite, BASIC_TABLE_STYLE);
		
		//table.setSize(650, 400);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		//--- Columns
		TableColumn col = null ;
		int iColumnIndex = 0 ;
		int columnWidth = 160 ;

		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("FK name");
		col.setWidth(columnWidth);
		
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Column");
		col.setWidth(columnWidth);
		
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Ref table");
		col.setWidth(columnWidth);
		
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Ref column");
		col.setWidth(columnWidth);
		
		columnWidth = 84 ;
		
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Update rule");
		col.setWidth(columnWidth);
		
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Delete rule");
		col.setWidth(columnWidth);
		
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Deferrability");
		col.setWidth(columnWidth);
		
		return table;
	}
	
	
//	private void populateTableGenerationTargets(Table table, List<TargetDefinition> list) // v 2.0.7
//	{
////		ProjectConfig projectConfig = getProjectConfig();
////		if ( projectConfig == null )
////		{
////			return ;
////		}
////		List<TargetDefinition> list = projectConfig.getTemplates(); // NB : the list can be null 
//		if ( list != null )
//		{
//			table.removeAll();
//	        TableItem tableItem ; 
//	        for ( TargetDefinition t : list ) {
//	        	// Do not add "once" target ( not entity dependent )
//	        	if ( t.isOnce() != true ) {
//	        		
//					tableItem = new TableItem(table, SWT.NONE );
//			        tableItem.setChecked(false);
//			        tableItem.setText(0, t.getName() );
//			        tableItem.setText(1, t.getFile() ) ; //  st.getTargetFile() );			
//			        tableItem.setText(2, t.getFolder() ) ; // st.getTargetFolder() );
//			        tableItem.setText(3, t.getTemplate() );
//			        tableItem.setImage(4, PluginImages.getImage(PluginImages.EDIT_ICON ) );
//			        
//			        // Keep the target definition associated with the table item
//			        tableItem.setData(t);		        
//	        	}
//			}
//		}
//	}
	
    /**
     * Listener for DATABASE TABLE COMBO BOX 
     * Event fired when a table is selected
     * @param combo
     */
    private void setTablesComboAction(Combo combo)
    {
        combo.addSelectionListener( new SelectionAdapter() 
        {
            public void widgetSelected(SelectionEvent event)
            {
        		//log(this, "Tables combo listener : widgetSelected()" );
                Combo combo = (Combo) event.widget ;
                
                // ver 2.0.7 : table names array is attached with the combo box
                String sTableName = "" ;
        		int index = combo.getSelectionIndex();
        		String[] tableNames = (String[]) combo.getData();
        		if ( tableNames != null ) {
            		if ( index >= 0 && index < tableNames.length ) {
            			sTableName = tableNames[index] ;
            		}
            		else {
            			MsgBox.error("Invalid index " + index);
            		}
        		}
        		else {
        			MsgBox.error("No table names for this combo ( getData returns null )" );
        		}

        		log(this, "Tables combo listener : widgetSelected() : " + sTableName );
                
        		//--- Load the new current table entity and populate the table and the fields
        		loadTableEntity(sTableName);
        		
            }
        });
    }
    
    /**
     * Listener for TableItem CheckBox (row selection in the table) 
     * @param table
     */
    private void setSelectionListener(Table table)
    {
    	table.addSelectionListener(new SelectionListener()
    	{
			public void widgetDefaultSelected(SelectionEvent e) {
        		log(this, "*** TABLE : widgetDefaultSelected" );				
			}

			public void widgetSelected(SelectionEvent e) {

        		TableItem tableItem = (TableItem) e.item ;
        		
        		log(this, "*** TABLE : widgetSelected : checked ? " + tableItem.getChecked()  );				
        		// Column row = TableUtil.getTableColumn(tableItem);
        		AttributeInDbModel attribute = TableUtil.getTableColumn(tableItem); // v 3.0.0
        		log(this, "*** TABLE : widgetSelected : row : " + attribute );
        		//if ( row.isPrimaryKey() ) {
            	if ( attribute.isKeyElement() ) { // v 3.0.0
    				// Must be always checked : unselect not allowed
            		tableItem.setChecked(true);
            		return ;
        		}
        		else {
    				// Can be checked or not
        			//if ( row.getSelected() != tableItem.getChecked() ) {
            		if ( attribute.isSelected() != tableItem.getChecked() ) {
                		attribute.setSelected( tableItem.getChecked() );
        				setDirty();
        				
        			}
        		}
			}
    	});
    }
    
}