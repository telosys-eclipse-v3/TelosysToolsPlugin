package org.telosys.tools.eclipse.plugin.commons;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.eclipse.plugin.config.view.VariableItem;

public abstract class GenericEditableTable extends Composite 
{

	private TableViewer tableViewer;
	
	private SelectionAdapter addSelectionAdapter ;
	private SelectionAdapter insertSelectionAdapter ;
	private SelectionAdapter deleteSelectionAdapter ;
	
	//----------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param parent
	 */
	public GenericEditableTable(Composite parent, int swtCompositeFlags ) 
	{
		super(parent, swtCompositeFlags);
		
		//--- Set the buttons selection adapter ( for "Add", "Insert" and "Delete" )
		this.addSelectionAdapter    = new AddSelectionAdapter(this); 
		this.insertSelectionAdapter = new InsertSelectionAdapter(this); 
		this.deleteSelectionAdapter = new DeleteSelectionAdapter(this); 
		
//		//--- Set the providers 
//		this.tableLabelProvider  = new TableLabelProviderImpl();
//		this.cellModifier = new CellModifierImpl();
		
		//buildControls();
		FillLayout compositeLayout = new FillLayout();
		setLayout(compositeLayout);

		final Table table = new Table(this, SWT.FULL_SELECTION);
		
		
		initTable(table);
		
		//tableViewer = initTable(table);
		tableViewer = new TableViewer(table); 
		
		//attachContentProvider(tableViewer);
		tableViewer.setContentProvider( new StructuredContentProvider() ) ;
		
		//attachLabelProvider(viewer);		
		// tableViewer.setLabelProvider( this.tableLabelProvider ) ;
		tableViewer.setLabelProvider( new TableLabelProviderImpl() ) ;
		

		//attachCellEditors(tableViewer, table);
		//tableViewer.setCellModifier( this.cellModifier );
		tableViewer.setCellModifier(  new CellModifierImpl(tableViewer) );
		
		initTableViewer(tableViewer) ;
		
//		tableViewer.setCellEditors( 
//				new CellEditor[] { 
//						null,
//						new TextCellEditor(table, SWT.NONE),
//						new TextCellEditor(table, SWT.NONE),
//				} );
//
//		tableViewer.setColumnProperties( VariableItem.getColumnProperties() ) ;
//		
		
	}
	
	//----------------------------------------------------------------------------------
	// Abstract methods 
	//----------------------------------------------------------------------------------
	protected abstract void initTable( Table table ) ;

	protected abstract void initTableViewer( TableViewer tableViewer ) ;
	
	public abstract Object getNewItem() ;
	

	//----------------------------------------------------------------------------------
	public SelectionAdapter getAddSelectionAdapter()
	{
		return this.addSelectionAdapter ;
	}
	//----------------------------------------------------------------------------------
	public SelectionAdapter getInsertSelectionAdapter()
	{
		return this.insertSelectionAdapter ;
	}
	//----------------------------------------------------------------------------------
	public SelectionAdapter getDeleteSelectionAdapter()
	{
		return this.deleteSelectionAdapter ;
	}

	//----------------------------------------------------------------------------------
	public void initItems( Object[] items ) 
	{
		if ( items != null )
		{
			for ( int i = 0 ; i < items.length ; i++ )
			{
				// tableViewer.add(items[i]);
				Object o = items[i] ;
				if ( o instanceof Variable )
				{
					VariableItem item = new VariableItem((Variable)o);
					tableViewer.add(item);
				}
				else
				{
					MsgBox.error("initItems : item " + i + " is not an instance of Variable !");
				}
			}
		}
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * Adds a new item at the end of the list
	 */
	public void addItem() 
	{
		Object item = getNewItem();
		tableViewer.add(item);
		//--- Select and set focus on the row added
		int lastRow = tableViewer.getTable().getItemCount() - 1 ;
		tableViewer.getTable().select(lastRow);
		tableViewer.getTable().setFocus();
	}

	//----------------------------------------------------------------------------------
	/**
	 * Removes the selected item from the table
	 */
	public void removeItem() 
	{
		int selectedRow = tableViewer.getTable().getSelectionIndex();
		if ( selectedRow >= 0 )
		{
			tableViewer.remove( tableViewer.getElementAt(selectedRow) );
			int nbRows = tableViewer.getTable().getItemCount() ;
			//MsgBox.info("REMOVE : selectedRow = " + selectedRow + " nbRows = " + nbRows );
			if ( nbRows > 0 )
			{
				if ( selectedRow < nbRows )
				{
					tableViewer.getTable().select(selectedRow);
				}
				else
				{
					tableViewer.getTable().select(nbRows-1);
				}
			}
			tableViewer.getTable().setFocus();
		}
		// else ( -1 ) no row selected 
	}

	//----------------------------------------------------------------------------------
	/**
	 * Inserts a new item at the current position
	 */
	public void insertItem() 
	{
		int selectedRow = tableViewer.getTable().getSelectionIndex() ;
		if ( selectedRow >= 0 )
		{
			Object item = getNewItem();
			tableViewer.insert(item, selectedRow );
			//MsgBox.info("INSERT : selectedRow = " + selectedRow  );
			//--- Select and set focus on the row inserted
			tableViewer.getTable().select(selectedRow);
			tableViewer.getTable().setFocus();
		}
		// else ( -1 ) no row selected 
	}

	//----------------------------------------------------------------------------------
	/**
	 * Returns an array containing all the items of the table
	 * @return
	 */
	public Object[] getItems() 
	{
		int nbItem = tableViewer.getTable().getItemCount();
		Object[] items = new Object[nbItem];
		for ( int i = 0 ; i < nbItem ; i++ ) 
		{
			items[i] = tableViewer.getElementAt(i);
		}
		return items;
	}

	//==========================================================================
	// Private inner classes 
	//==========================================================================
	private class TableLabelProviderImpl implements ITableLabelProvider
	{
		public Image getColumnImage(Object item, int columnIndex) {
			return null;
		}

		public String getColumnText(Object item, int columnIndex) {
			if ( item instanceof ITableItem )
			{
				return ((ITableItem)item).getColumnText(columnIndex);
			}
			else
			{
				MsgBox.error("ITableLabelProvider : item does not implement ITableItem ");
			}
			return "";
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener lpl) {
		}
	}
	
	//--------------------------------------------------------------------------
	private class CellModifierImpl implements ICellModifier
	{
		private TableViewer tableViewer ;
		
		/**
		 * Constructor
		 * @param tableViewer
		 */
		public CellModifierImpl(TableViewer tableViewer) {
			super();
			this.tableViewer = tableViewer;
		}

		public boolean canModify(Object item, String property) 
		{
			return true; // All the collumns can be modified 
		}

		public Object getValue(Object item, String property) 
		{
			if ( item instanceof ITableItem )
			{
				return ((ITableItem)item).getValue(property);
			}
			else
			{
				MsgBox.error("CellModifierImpl - getValue : "
						+ "\n item data does not implements ITableItem "
						+ "\n data class : " + item.getClass().getName() );
			}
			return null;
		}

		public void modify(Object item, String property, Object value) 
		{
			// NB : SWT doc :
			// Note that it is possible for an SWT Item to be passed instead of the model element
			if ( item instanceof TableItem )
			{
				Object data = ((TableItem)item).getData();
				if ( data instanceof ITableItem )
				{
					((ITableItem)data).setValue(property, value);
					//viewer.refresh(data);
					tableViewer.refresh(data);
				}
				else
				{
					MsgBox.error("CellModifierImpl - modify : "
							+ "\n item data does not implements ITableItem "
							+ "\n data class : " + data.getClass().getName() );
				}
			}
			else if ( item instanceof ITableItem )
			{
				((ITableItem)item).setValue(property, value);
				//viewer.refresh(data);
				tableViewer.refresh(item);
			}
			else 
			{
				MsgBox.error("CellModifierImpl - modify : "
						+ "\n item is not a TableItem "
						+ "\n item class : " + item.getClass().getName() );
			}
		}
	}
	
	//--------------------------------------------------------------------------
	private class StructuredContentProvider implements IStructuredContentProvider
	{
		public Object[] getElements(Object inputElement) {
			return (Object[]) inputElement;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput,
				Object newInput) {
		}
	}
	
	//==========================================================================
	// Private inner classes for buttons actions 
	//==========================================================================
	private class AddSelectionAdapter extends SelectionAdapter
	{
		GenericEditableTable table = null ;
		AddSelectionAdapter(GenericEditableTable table)
		{
			this.table = table ;
		}
		public void widgetSelected(SelectionEvent e) 
		{
			// OnClick => Action
			//this.table.addItem(new VariableItem("","") );
			this.table.addItem();
		}
	}
	
	//--------------------------------------------------------------------------
	private class InsertSelectionAdapter extends SelectionAdapter
	{
		GenericEditableTable table = null ;
		InsertSelectionAdapter(GenericEditableTable table)
		{
			this.table = table ;
		}
		public void widgetSelected(SelectionEvent e) 
		{
			// OnClick => Action
			//this.table.insertItem(new VariableItem("","") );
			this.table.insertItem();
		}
	}
	
	//--------------------------------------------------------------------------
	private class DeleteSelectionAdapter extends SelectionAdapter
	{
		GenericEditableTable table = null ;
		DeleteSelectionAdapter(GenericEditableTable table)
		{
			this.table = table ;
		}
		public void widgetSelected(SelectionEvent e) 
		{
			// OnClick => Action
			this.table.removeItem();
		}
	}
}