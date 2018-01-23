package org.telosys.tools.eclipse.plugin.editors.dbrep;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class CompositesList {
	
	private ScrolledComposite scrolledComposite = null ; // main container
	
	private Composite composite = null;	
	
	private int size = 0 ;
	
	//----------------------------------------------------------------------------------------
	public CompositesList(Composite parent, Object layoutData)
	{
		//------ Level 1 : ScrolledComposite ( No Layout , Height=200 )
		scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		
		//--- Set the layout data if any
		if ( layoutData != null ) {
			scrolledComposite.setLayoutData(layoutData);
		}
		
		//------ Level 2 : Composite ( Grid Layout 3 cols )
		composite = new Composite(scrolledComposite, SWT.NONE);		
		RowLayout rowLayout = new RowLayout ();
		rowLayout.type = SWT.VERTICAL;
		composite.setLayout(rowLayout);
		
//		if ( items != null ) {
//			populate(items);
//		}
		
		scrolledComposite.setContent(composite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	//----------------------------------------------------------------------------------------
	// Abstract methods
	//----------------------------------------------------------------------------------------
	/**
	 * Creates and return a new row based on a "Composite"
	 * @param item
	 * @return
	 */
	protected abstract Composite addRow( Composite parent, Object item );
	
	//----------------------------------------------------------------------------------------
	/**
	 * Delete the given item in the model
	 * @param item
	 */
	protected abstract void deleteItemInModel (Object item);

	//----------------------------------------------------------------------------------------
	// Concrete methods
	//----------------------------------------------------------------------------------------
	public int getSize()
	{
		return size ;
	}
	//----------------------------------------------------------------------------------------
	public void refresh()
	{
		composite.pack();
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.redraw();
	}
	//----------------------------------------------------------------------------------------
	private void clearAll(boolean refresh)
	{
		//--- Remove children controls
		Control[] controls = composite.getChildren();
		for ( int i = 0 ; i < controls.length ; i++ ) {
			Control c = controls[i];
			if ( c != null ) {
				if ( c instanceof Composite )
				{
					deleteRow((Composite)c);
				}
			}
		}
		if ( refresh ) {
			refresh();
		}
		size=0;
	}
	//----------------------------------------------------------------------------------------
	public Composite[] getAllRows()
	{
		Control[] controls = composite.getChildren();
		Composite[] rows = new Composite[controls.length];
		for ( int i = 0 ; i < controls.length ; i++ ) {
			Control c = controls[i];
			if ( c instanceof Composite ) {
				rows[i] = (Composite)c ;
			}
		}
		return rows ;
	}
	//----------------------------------------------------------------------------------------
	public void clearAll()
	{
		clearAll(true);
	}
	//----------------------------------------------------------------------------------------
	protected void deleteRow(Composite row)
	{
		//--- Remove children controls
		Control[] controls = row.getChildren();
		for ( int i = 0 ; i < controls.length ; i++ ) {
			Control c = controls[i];
			if ( c != null ) {
				c.dispose();
			}
		}
		//--- Remove row itself
		row.dispose();
		size--;
	}
	
	//----------------------------------------------------------------------------------------
	private int populate (Collection<? extends Object> items, boolean refresh)
	{
		size = 0 ;
//		Iterator iter = items.iterator();
//		while ( iter.hasNext() ) {
//			Object item = iter.next() ;
		for ( Object item : items ) {
			addRow(composite, item);
			size++;
		}
		if ( refresh ) {
			refresh();
		}
		return size ;
	}
	//----------------------------------------------------------------------------------------
	public int populate (Collection<? extends Object> items)
	{
		return populate(items, true);
	}
	
	//----------------------------------------------------------------------------------------
	public int repopulate (Collection<? extends Object> items)
	{
		clearAll(false);
		return populate (items, true);
	}

	//----------------------------------------------------------------------------------------
}
