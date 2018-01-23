package org.telosys.tools.eclipse.plugin.config.view;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.telosys.tools.eclipse.plugin.commons.GenericEditableTable;

public class VariablesTable extends GenericEditableTable 
{
	//----------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param parent
	 */
	public VariablesTable(Composite parent) 
	{
		super(parent, SWT.BORDER);
		
//		//--- Set the buttons selection adapter ( for "Add", "Insert" and "Delete" )
//		this.addSelectionAdapter    = new AddSelectionAdapter(this); 
//		this.insertSelectionAdapter = new InsertSelectionAdapter(this); 
//		this.deleteSelectionAdapter = new DeleteSelectionAdapter(this); 
//		
////		//--- Set the providers 
////		this.tableLabelProvider  = new TableLabelProviderImpl();
////		this.cellModifier = new CellModifierImpl();
//		
//		//buildControls();
//		FillLayout compositeLayout = new FillLayout();
//		setLayout(compositeLayout);
//
//		final Table table = new Table(this, SWT.FULL_SELECTION);
//		
//		
//		initTable(table);
//		
//		//tableViewer = initTable(table);
//		tableViewer = new TableViewer(table); 
//		
//		//attachContentProvider(tableViewer);
//		tableViewer.setContentProvider( new StructuredContentProvider() ) ;
//		
//		//attachLabelProvider(viewer);		
//		// tableViewer.setLabelProvider( this.tableLabelProvider ) ;
//		tableViewer.setLabelProvider( new TableLabelProviderImpl() ) ;
//		
//
//		//attachCellEditors(tableViewer, table);
//		//tableViewer.setCellModifier( this.cellModifier );
//		tableViewer.setCellModifier(  new CellModifierImpl(tableViewer) );
//		
		
	}
	
	//----------------------------------------------------------------------------------
	protected void initTable(final Table table) 
	{
		//--- Define the layout
		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnPixelData(20, false)); 
		layout.addColumnData(new ColumnPixelData(200, true)); // Name
		layout.addColumnData(new ColumnPixelData(200, true)); // Type
		table.setLayout(layout);

		//--- Define all the columns
		TableColumn column ;
		
		column = new TableColumn(table, SWT.LEFT);
		column.setText(" ");
		column.setResizable(false);

		column = new TableColumn(table, SWT.LEFT);
		column.setText("Name");

		column = new TableColumn(table, SWT.LEFT);
		column.setText("Value");


		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}
	
	//----------------------------------------------------------------------------------
	protected void initTableViewer( TableViewer tableViewer ) 
	{
		Table table = tableViewer.getTable() ;
		
		tableViewer.setCellEditors( 
				new CellEditor[] { 
						null,
						new TextCellEditor(table, SWT.NONE),
						new TextCellEditor(table, SWT.NONE),
				} );

		tableViewer.setColumnProperties( VariableItem.getColumnProperties() ) ;
	}

	//----------------------------------------------------------------------------------
	public Object getNewItem()
	{
		return new VariableItem("","") ;
	}
	
	//----------------------------------------------------------------------------------
//	public void addVariableItem ( VariableItem[] item )
//	{
//		initItems( Object[] items )
//		addItem ( item ) ;
//	}
}