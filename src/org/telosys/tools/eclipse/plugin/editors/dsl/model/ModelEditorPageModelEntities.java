package org.telosys.tools.eclipse.plugin.editors.dsl.model;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.dsl.DslModelUtil;
import org.telosys.tools.eclipse.plugin.commons.Const;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;
import org.telosys.tools.eclipse.plugin.commons.FileEditorUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginImages;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.commons.Util;
import org.telosys.tools.eclipse.plugin.editors.commons.AbstractModelEditorPage;

/**
 * First page of the editor : Model attributes mapping and foreign keys <br>
 *  
 */
/* package */  class ModelEditorPageModelEntities extends AbstractModelEditorPage 
{

	private Table _entitiesTable = null ;
	
	/**
	 * Constructor
	 * @param editor
	 * @param id
	 * @param title 
	 */
	public ModelEditorPageModelEntities(FormEditor editor, String id, String title ) {
		super(editor, id, title);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		
		log(this, "createFormContent(..) : Entities Page" );

		//--- Set a GRID LAYOUT to the BODY
		GridLayout bodyLayout = new GridLayout();	
		bodyLayout.numColumns = 1 ;
		bodyLayout.makeColumnsEqualWidth = false ;
		
		Composite scrolledFormBody = initAndGetFormBody(managedForm, bodyLayout);
		
		//---------------------------------------------------------------
		// Line 1 : Page title
		//---------------------------------------------------------------
		Composite titlePanel = createTitleAndButtons(scrolledFormBody);		
		titlePanel.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );
		
		//---------------------------------------------------------------
		// Line 2 : Entities table
		//---------------------------------------------------------------
		_entitiesTable = createEntitiesTable(scrolledFormBody);

		// Fills available horizontal and vertical space, 
		// grabs horizontal space, grabs vertical space
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		//gd.heightHint = 360 ;
		_entitiesTable.setLayoutData(gd);
		
		setBodyBackgroundColor(); // KEEP IT AT THE END OF THE METHOD (for Eclipse 4.X compatibility )

		refresh();
	}
	
	//----------------------------------------------------------------------------------------------
	private Shell getShell() {
		return _entitiesTable.getDisplay().getActiveShell();
	}
	//----------------------------------------------------------------------------------------------
	protected void doOpenEntityInEditor(String entityAbsoluteFilePath) {
		FileEditorUtil.openEntityFileInEditor( entityAbsoluteFilePath );
	}
	//----------------------------------------------------------------------------------------------
	protected void doNewEntity() {
		DialogBoxForNewEntity dialogBox = new DialogBoxForNewEntity( getShell() );
		if ( dialogBox.open() == Window.OK ) {
			String entityName = dialogBox.getEntityName();
			try {
				File entityFile = DslModelUtil.createNewEntity(getModelFile(), entityName);
				// refresh the Eclipse workspace (add the file in workspace view)
				EclipseWksUtil.refresh(entityFile); 
				// re-parse the model and refresh the entities in "entities table"
				// the model is automatically refreshed ( by file change listener )
				//refresh();
				// open the new entity in the editor
				doOpenEntityInEditor(entityFile.getAbsolutePath());
			} catch (Exception e) {
				MsgBox.error("Cannot create entity '" + entityName + "'", e);
			}
		}
	}
	//----------------------------------------------------------------------------------------------
	protected void doDeleteEntity(String entityAbsoluteFilePath) {
		File entityFile = new File ( entityAbsoluteFilePath );
		String entityName = DslModelUtil.getEntityName( entityFile );
		if ( MsgBox.confirm("Confirm delete", 
				"Do you realy want to delete entity '" + entityName + "' ?" ) ) {
			boolean deleted = entityFile.delete() ;
			if ( deleted ) {
				// refresh the Eclipse workspace (removes the file in workspace view and close editor if any)
				EclipseWksUtil.refresh(entityFile); 
				// re-parse the model and refresh the entities in "entities table"
				// the model is automatically refreshed ( by file change listener )
				//refresh();
			}
			else {
				MsgBox.error( "Cannot delete entity '" + entityName + "' !");
			}
		}
	}
	//----------------------------------------------------------------------------------------------
	protected void doRenameEntity(String entityAbsoluteFilePath) {
		String currentEntityName = DslModelUtil.getEntityName( new File(entityAbsoluteFilePath) );
		DialogBoxForRenameEntity dialogBox = new DialogBoxForRenameEntity( getShell(), currentEntityName );
		if ( dialogBox.open() == Window.OK ) {
			String newEntityName = dialogBox.getNewEntityName();
			// rename the entity file
			DslModelUtil.renameEntity(new File(entityAbsoluteFilePath), newEntityName);
			// refresh the Eclipse workspace (removes the file in workspace view and close editor if any)
			File modelFolder = DslModelUtil.getModelFolder(getModelFile());
			EclipseWksUtil.refresh(modelFolder); 
			// re-parse the model and refresh the entities in "entities table"
			
			//refresh();
		}
	}
	
	
	//----------------------------------------------------------------------------------------------
	/**
	 * Return the current selected TableItem in the entities table <br>
	 * Return null if no entity is selected or if more than one entity are selected
	 * @return
	 */
	protected TableItem getSelectedTableItem() {
		TableItem selectedItems[] = _entitiesTable.getSelection();
		if ( selectedItems != null && selectedItems.length == 1 ) {
			return selectedItems[0] ;
		}
		return null ;
	}
	//----------------------------------------------------------------------------------------------
	private final static int BUTTON_WIDTH = 120 ;
	
	private Composite createTitleAndButtons(Composite formBodyComposite) {
		Composite panel = new Composite(formBodyComposite, SWT.NONE );
		
		//RowLayout rowLayout = new RowLayout();
		panel.setLayout(new RowLayout());
		
		Util.setPageTitle(panel, this.getTitle() ) ; // Title defined in the constructor
		
		Label filler = new Label(panel, SWT.NULL);
		filler.setText("");
		filler.setLayoutData( new RowData(80, SWT.DEFAULT) );
		
		//--- [ Refresh ]
		Button refreshButton = new Button(panel, SWT.NONE);
		refreshButton.setText(" Refresh");
		refreshButton.setLayoutData( new RowData(BUTTON_WIDTH, SWT.DEFAULT) );
		refreshButton.setImage( PluginImages.getImage(PluginImages.REFRESH ) );
		refreshButton.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		    	log("button click : [ Refresh ]");
		        refresh();
		    }
		}); 
		
		//--- [ New entity ]
		Button newButton = new Button(panel, SWT.NONE);
		newButton.setText(" New entity");
		newButton.setLayoutData( new RowData(BUTTON_WIDTH, SWT.DEFAULT) );
		newButton.setImage( PluginImages.getImage(PluginImages.ENTITY_FILE ) );
		newButton.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		        doNewEntity();
		    }
		}); 

		return panel;
	}
	//----------------------------------------------------------------------------------------------
	private Table createEntitiesTable(Composite formBodyComposite) {
		// Table style
		// SWT.CHECK : check box in the first column of each row
//		int iTableStyle = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL 
//						| SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.CHECK ;
		int iTableStyle = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL 
		| SWT.FULL_SELECTION | SWT.HIDE_SELECTION ;
		
		final Table table = new Table(formBodyComposite, iTableStyle);
		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		//--- Table columns : call the specialized method implemented in the descendant class
		//--- Columns
		TableColumn col = null ;
		int iColumnIndex = 0 ;

		//--- Column for "Entity class name"
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Entity file name");
		col.setWidth(200);
		
		//--- Column for "Entity error"
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Entity parsing error");
		col.setWidth(500);
		
		createMenu(table);
		
		table.addMouseListener(new MouseListener() {
			// Double-click => Open the entity file in the "Entity editor"
            public void mouseDoubleClick(MouseEvent mouseEvent) {
                if (table.getSelectionCount() > 0) {
                	TableItem[] tableItemsSelected = table.getSelection();
                	String entityAbsoluteFilePath = "?";
                	if ( tableItemsSelected != null && tableItemsSelected.length == 1 ) {
                		entityAbsoluteFilePath = (String) tableItemsSelected[0].getData() ;
                		doOpenEntityInEditor(entityAbsoluteFilePath);
                	}
                }
            }
            public void mouseDown(MouseEvent mouseEvent) {
            }
            public void mouseUp(MouseEvent mouseEvent) {
            }
        });

		table.addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// Event widget is "Table"
				//MsgBox.info( "Event widget class : " + event.widget.getClass());
                TableItem tableItem = getTableItemClicked(table, event);
                if ( tableItem == null ) {
                	event.doit = false ; // Don't do it !
                }
			}
		});
		
		return table;
	}

	//----------------------------------------------------------------------------------------------
	private Menu createMenu(Table table) {
		Menu menu = new Menu(table);
		table.setMenu(menu);
		menu.setData(table);
		
//		createMenuItem(menu, null, "New", 
//				new TableContextMenuListener(this, TableContextMenuListener.NEW) );
//		
//		new MenuItem(menu, SWT.SEPARATOR);
		
		createMenuItem(menu, null, "Open", 
				new TableContextMenuListener(this, TableContextMenuListener.OPEN) );

		createMenuItem(menu, null, "Rename", 
				new TableContextMenuListener(this, TableContextMenuListener.RENAME) );
		
//		createMenuItem(menu, PluginImages.getImage(PluginImages.ERROR), "Delete", 
		createMenuItem(menu, null, "Delete", 
				new TableContextMenuListener(this, TableContextMenuListener.DELETE) );
		
		return menu ;
	}

	//----------------------------------------------------------------------------------------------
	private MenuItem createMenuItem(Menu menu, Image image, String text, Listener listener) {
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText(text);
		if ( image != null ) {
			menuItem.setImage(image);
		}
		menuItem.addListener(SWT.Selection, listener);
		menuItem.setData(menu.getData());
		return menuItem ;
	}
	//----------------------------------------------------------------------------------------------
	private TableItem getTableItemClicked(Table table, Event event) {
		// calculate click offset within table area  
		Point point = Display.getDefault().map(null, table, new Point(event.x, event.y)); 
		TableItem tableItem = table.getItem(point);
		//log("getTableItem : x = " + point.x + " / y = " + point.y + " / table item = " + tableItem );
		return tableItem ;
	}
	//----------------------------------------------------------------------------------------------
	private void refresh() {
		log ( "refresh()..." ) ;
		ModelEditor modelEditor = (ModelEditor) getModelEditor();
		modelEditor.refresh();
	}
	//----------------------------------------------------------------------------------------------
	/**
	 * Returns entity error message for the given entity file name
	 * @param entityFileName
	 * @param entitiesErrors
	 * @return
	 */
	protected String getEntityError(String entityFileName, Map<String,List<String>> entitiesErrors) {
		// Entity name without ".entity" extension 
		String entityName = StrUtil.removeEnd(entityFileName, Const.DOT_ENTITY);
		// Check if this entity has 1 or more error(s)
		if ( entitiesErrors != null ) {
			List<String> entityErrorsList = entitiesErrors.get(entityName);
			if ( entityErrorsList != null ) {
				if ( ! entityErrorsList.isEmpty() ) {
					if ( entityErrorsList.size() == 1 ) {
						// get unique error 
						return entityErrorsList.get(0);
					}
					else {
						// just report number of errors  
						return entityErrorsList.size() + " errors" ;
					}
				}
			}
		}
		return null ; // No error found 
	}
	
	/**
	 * Populates the list of entities <br>
	 * 
	 */
	protected int populateEntities() {
		log(this, "populateEntities(table)");
		
		int errorsCount = 0 ;
		_entitiesTable.removeAll();
		
		ModelEditor modelEditor = (ModelEditor) getModelEditor();
		
    	List<String> entitiesFileNames = modelEditor.getEntitiesAbsoluteFileNames();
		PluginLogger.debug("populateEntities() : entitiesFileNames = " + entitiesFileNames.size() ) ;

    	Map<String,List<String>> entitiesErrors = modelEditor.getEntitiesErrors();
//		// #TMP
//		MsgBox.debug("modelEditor.getEntitiesErrors() : " + 
//				" \n errors size : " + entitiesErrors.size() ) ;

		for ( String entityFile : entitiesFileNames ) {
			// Entity file name ( eg "Person.entity" )
			String entityFileName = (new File(entityFile)).getName() ;
			// Entity name without ".entity" extension 
			String entityName = StrUtil.removeEnd(entityFileName, Const.DOT_ENTITY);

			// Update errors markers
			IFile iFile = EclipseWksUtil.toIFile(entityFile);
			deleteErrorMarkers(iFile);
			if ( entitiesErrors != null ) {
				addErrorMarkers(iFile, entitiesErrors.get(entityName));
			}
			
			// Update entities list in model editor
			String imageId = PluginImages.ENTITY_FILE ;
			String entityErrorMessage = getEntityError(entityFileName, entitiesErrors);
			if ( entityErrorMessage != null ) {
				imageId = PluginImages.ERROR ;
				errorsCount++;
			} else {
				entityErrorMessage = "" ;
			}

			//--- Create the TableItem and set the row content 
            String[] row = new String[] { entityFileName, entityErrorMessage };

        	TableItem tableItem = new TableItem( _entitiesTable, SWT.NONE );
            tableItem.setText( row );
            tableItem.setChecked(false);
            tableItem.setData( entityFile );
            
            tableItem.setImage( PluginImages.getImage(imageId) );
            //tableItem.addListener(eventType, listener)
            //tableItem.addListener(eventType, listener)
		}		
		return errorsCount ;
	}
	
	// The platform standard markers : task, problem, and bookmark
//	private void updateErrorMarkers(String absoluteFilePath, List<String> entityErrorsList) {
//		IFile iFile = EclipseWksUtil.toIFile(absoluteFilePath);
//		deleteErrorMarkers(iFile);
//		addErrorMarkers(iFile, entityErrorsList);
//	}
	
	private void deleteErrorMarkers(IFile iFile) {
		try {
			iFile.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			MsgBox.error("Cannot delete markers", e);
		}
	}
	
	private void addErrorMarkers(IFile iFile, List<String> entityErrorsList) {
		if ( entityErrorsList != null ) {
			for ( String msg : entityErrorsList ) {
				addErrorMarker(iFile, msg); 
			}
		}
	}
	private void addErrorMarker(IFile iFile, String message) {
		try {
			// add new marker
			IMarker marker = iFile.createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			marker.setAttribute(IMarker.LINE_NUMBER, 1 );
			marker.setAttribute(IMarker.MESSAGE, message );
		} catch (CoreException e) {
			MsgBox.error("Cannot create marker", e);
		}
	}
}