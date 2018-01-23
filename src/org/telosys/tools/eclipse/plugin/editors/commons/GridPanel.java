package org.telosys.tools.eclipse.plugin.editors.commons;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class GridPanel {

	private final Composite panel ;
	
	public GridPanel(Composite parent, int numberOfColumns) {

		this.panel = new Composite(parent, SWT.NONE );

		//buttonsPanel.setBackground(PluginColors.red()); // to see the panel size 

		//GridLayout layout = new GridLayout(numberOfColumns, true); // 2 columns & equal width
		GridLayout layout = new GridLayout(); 
		layout.numColumns = numberOfColumns ;
		
		//layout.makeColumnsEqualWidth = true ;
		
		layout.marginLeft = 0 ;
		layout.marginRight = 0 ;
		layout.marginTop = 0 ;
		layout.marginBottom = 0 ;

		layout.verticalSpacing = 0 ;
		//layout.horizontalSpacing = 0 ;
		
		layout.marginHeight = 0 ; // To avoid top and bottom spaces in the panel 
		layout.marginWidth = 0 ; // To avoid left and right spaces in the panel 
		
		panel.setLayout(layout);
	}
	
	public Composite getPanel()
	{
		return panel ;
	}

	public void addFiller(int width) {
		Label label = new Label(panel, SWT.PUSH);
		label.setLayoutData( new GridData (width, 2 ));
		label.setText("");
	}
}
