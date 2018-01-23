package org.telosys.tools.eclipse.plugin.editors.commons;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginImages;

public class SelectDeselectButtons {
	
//	private final static int  BUTTON_HEIGHT =  26 ; // 24
//	private final static int  BUTTON_WIDTH  =  26 ; // 80 ; // 74

//	public final static boolean CREATE_PANEL = true ;
//	public final static boolean DO_NOT_CREATE_PANEL = false ;
	
	//--- Buttons size
//	private GridData  gridData = new GridData(BUTTON_WIDTH, BUTTON_HEIGHT); // to define Button width and height
//	private Composite buttonsPanel ;
	private Button    selectAll ;
	private Button    deselectAll ;
	
	/**
	 * Create the 2 buttons in the same composite 
	 * @param parent
	 */
	public SelectDeselectButtons(Composite parent) {
//		this(parent, DO_NOT_CREATE_PANEL);
		createButtonSelectAll(parent);
		createButtonDeselectAll(parent);
	}

//	/**
//	 * Create the 2 buttons in 2 separated composites 
//	 * @param parentSelectAll
//	 * @param parentDeselectAll
//	 */
//	public SelectDeselectButtons(Composite parentSelectAll, Composite parentDeselectAll) {
//		createButtonSelectAll(parentSelectAll);
//		createButtonDeselectAll(parentDeselectAll);
//	}
	
/***
	public SelectDeselectButtons(Composite parent, boolean createPanel) {
		
		//--- Panel for buttons
		if ( createPanel ) {
			buttonsPanel = new Composite(parent, SWT.NONE );

			//buttonsPanel.setBackground(PluginColors.red()); // to see the panel size 

			GridLayout layout = new GridLayout(2, true); // 2 columns & equal width
			//layout.makeColumnsEqualWidth = true ;
			layout.marginLeft = 0 ;
			layout.marginRight = 0 ;
			layout.marginTop = 0 ;
			layout.marginBottom = 0 ;
			layout.verticalSpacing = 0 ;
			layout.marginHeight = 0 ; // To avoid top and bottom spaces in the panel 
			layout.marginWidth = 0 ; // To avoid left and right spaces in the panel 
			
			buttonsPanel.setLayout(layout);
		}
		else {
			buttonsPanel = parent ;
		}		

//		//--- Buttons size
//		GridData gd = new GridData(BUTTON_WIDTH, BUTTON_HEIGHT); // to define Button width and height
		
		//--- 1rst button 
//		selectAll = new Button(buttonsPanel, SWT.PUSH);
//		//selectAll.setText("Select All");
//		selectAll.setImage( PluginImages.getImage(PluginImages.CHECKED_ON ) );
//		selectAll.setToolTipText("Select All");
//
//		selectAll.setLayoutData(gd);
//		selectAll.addSelectionListener(new SelectionAdapter() 
//		{
//			public void widgetSelected(SelectionEvent event) 
//			{
//				Button button = (Button) event.getSource();
//				setCheckedStateForAll((Table)button.getData(), true );
//			}
//		});
		createButtonSelectAll(buttonsPanel);
		
		//--- 2nd button 
//		deselectAll = new Button(buttonsPanel, SWT.PUSH);
//		//deselectAll.setText("Deselect All");
//		deselectAll.setImage( PluginImages.getImage(PluginImages.CHECKED_OFF ) );
//		deselectAll.setToolTipText("Deselect All");
//
//		deselectAll.setLayoutData(gd);
//		deselectAll.addSelectionListener(new SelectionAdapter() 
//		{
//			public void widgetSelected(SelectionEvent event) 
//			{
//				//setCheckedForAll(listTable, false );
//				Button button = (Button) event.getSource();
//				setCheckedStateForAll((Table)button.getData(), false );
//			}
//		});
		createButtonDeselectAll(buttonsPanel);
	}
***/
	
	private void createButtonSelectAll(Composite panel) {
		selectAll = new Button(panel, SWT.PUSH);
		//selectAll.setText("Select All"); // No text for this button (image only)
		selectAll.setImage( PluginImages.getImage(PluginImages.CHECKED_ON ) );
		selectAll.setToolTipText("Select All");

//		selectAll.setLayoutData(gridData);
		
		selectAll.addSelectionListener(new SelectionAdapter() 
		{
			public void widgetSelected(SelectionEvent event) 
			{
				Button button = (Button) event.getSource();
				setCheckedStateForAll((Table)button.getData(), true );
			}
		});
	}
	private void createButtonDeselectAll(Composite panel) {
		deselectAll = new Button(panel, SWT.PUSH);
		//deselectAll.setText("Deselect All"); // No text for this button (image only)
		deselectAll.setImage( PluginImages.getImage(PluginImages.CHECKED_OFF ) );
		deselectAll.setToolTipText("Deselect All");

//		deselectAll.setLayoutData(gridData);
		
		deselectAll.addSelectionListener(new SelectionAdapter() 
		{
			public void widgetSelected(SelectionEvent event) 
			{
				Button button = (Button) event.getSource();
				setCheckedStateForAll((Table)button.getData(), false );
			}
		});
	}
//	/**
//	 * Returns the panel containing the buttons
//	 * @return
//	 */
//	public Composite getPanel() {
//		return buttonsPanel ;		
//	}
	
	public void setTable(Table managedTable) {
		//this.table = managedTable ;
		selectAll.setData(managedTable);
		deselectAll.setData(managedTable);
	}
	
	/**
	 * Set the "checked state" for all the table's items
	 * @param table
	 * @param flag
	 */
	private void setCheckedStateForAll(Table table, boolean flag )
	{
		if ( null == table ) {
			MsgBox.error("setCheckedForAll : managed table is null " );
			return ;
		}
		try {
			int n = table.getItemCount() ;
			for ( int i = 0 ; i < n ; i++ )
			{
				TableItem item = table.getItem(i);
				item.setChecked(flag);
			}
		} catch (RuntimeException e1) {
			MsgBox.error("setCheckedForAll : " + e1.getMessage() );
		}
	}
	
}
