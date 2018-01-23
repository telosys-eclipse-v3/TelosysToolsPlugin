package org.telosys.tools.eclipse.plugin.editors.dbrep;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginImages;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.generic.model.Cardinality;
import org.telosys.tools.repository.model.EntityInDbModel;
import org.telosys.tools.repository.model.ForeignKeyInDbModel;
import org.telosys.tools.repository.model.LinkInDbModel;
import org.telosys.tools.repository.model.RelationLinksInDbModel;
import org.telosys.tools.repository.model.RepositoryModel;
import org.telosys.tools.repository.persistence.util.RepositoryConst;

public class LinksList extends CompositesList 
{
//	private LinksManager           linksManager ;
	private final RepositoryModel        repositoryModel ; // v 3.0.0 replaces linksManager
	private final RepositoryEditorPageModelLinks  _editorPage;
	
	//----------------------------------------------------------------------------------------
//	public LinksList(Composite parent, Object layoutData, LinksManager linksManager, RepositoryEditorPage2 pluginPage) 
	public LinksList(Composite parent, Object layoutData, RepositoryEditorPageModelLinks editorPage) // v 3.0.0
	{
		super(parent, layoutData);
		if ( editorPage.getRepositoryModel() == null ) {
			MsgBox.error("Repository model is not yet set in the editor page");
		}
//		this.linksManager = linksManager ;
		this.repositoryModel = editorPage.getRepositoryModel() ; // v 3.0.0
		this._editorPage = editorPage ;
	}

	protected void log(String s)
	{
		PluginLogger.log(this, s );
	}

	//----------------------------------------------------------------------------------------
	protected Composite addRow( Composite parent, Object item )
	{
		int height =  22 ;
		int width0 =  22 ;
		int width1 = 220 ;
		int width2 = 100 ;
		int width3 = 220 ;
		int width4 = 100 ;
		int width_icon =  20 ;
		
		Device device = Display.getCurrent();
		Color white = device.getSystemColor(SWT.COLOR_WHITE);
		Color red   = device.getSystemColor(SWT.COLOR_RED);
		Color blue  = device.getSystemColor(SWT.COLOR_BLUE);
		//Color gray  = device.getSystemColor(SWT.COLOR_GRAY);
		Color gray = new Color(device, 240, 240, 240);

		GridLayout gridLayout = new GridLayout(7, false); 

		Composite row = new Composite(parent, SWT.NONE);		
		row.setLayout( gridLayout );
		row.setBackground(white);

		//if ( ! ( item instanceof Link) ) {
		if ( ! ( item instanceof LinkInDbModel) ) { // v 3.0.0
			MsgBox.error("Links list - addRow() : item is not an instance of Link");
			return row ;
		}
		
		//Link link = (Link)item;
		LinkInDbModel link = (LinkInDbModel)item; // v 3.0.0
		row.setData(link);

		String side = "owning side " ;
		if ( ! link.isOwningSide() ) {
			//side = "inverse side of \"" + link.getInverseSideOf() + "\" " ;
			side = "inverse side of \"" + link.getInverseSideLinkId() + "\" " ;
		}
		String toolTip = " Link id : \"" + link.getId() + "\" "
			+ "\n " + side
			+ "\n mapped by : " + link.getMappedBy() 
			+ "\n target entity : " + link.getTargetEntityClassName() // link.getTargetEntityJavaType() // v 3.0.0
			+ "\n fetch : " + link.getFetchType() // link.getFetch()  // v 3.0.0
			+ "\n cascade : " + link.getCascadeOptions() // link.getCascade() // v 3.0.0
			+ "\n optional : " + link.getOptional()
			;

		//row.setToolTipText(toolTip);
		
		//--- 1.0 & 2.0 ( Vertical Span )
		{
			//--- Check Box "Link Used or Not Used" 
			GridData gd = new GridData(width0, height) ;
			gd.verticalSpan = 2 ;
			Button b = new Button(row, SWT.CHECK | SWT.CENTER );
	        b.setData(row);
	        //b.setText(text);
	        b.setLayoutData( gd ) ;
//	        b.setSelection( link.isUsed() );
	        b.setSelection( link.isSelected() ); // v 3.0.0
	        b.setBackground(white);
	        b.addSelectionListener( new SelectionListener() 
	    	{
	            public void widgetSelected(SelectionEvent event)
	            {
	            	if ( event.widget instanceof Button ) {
	            		Button b = (Button)event.widget ;
	            		buttonChangeUseFlag(b);
	            	}
	            }
	            public void widgetDefaultSelected(SelectionEvent arg0)
	            {
	            }
	        });
	        
		}
			

		//--- 1.1
		{
			Label label1 = new Label( row, SWT.CENTER );
			label1.setText( link.getSourceTableName() ) ;
			label1.setLayoutData( new GridData(width1, height) ) ; // to define width
			label1.setBackground(gray);
			if ( link.isOwningSide() ) {
				label1.setToolTipText("Owning side");
			}
			else {
				label1.setToolTipText("Inverse side");
			}
		}

		//--- 1.2
		{
			Label label = new Label( row, SWT.LEFT );
			label.setLayoutData( new GridData(width_icon, height) ) ; 
			label.setBackground(white);
			label.setToolTipText(toolTip);
//			if ( link.isOwningSide() ) {
//				label.setImage( WizardImages.getImage(WizardImages.ARROW_RIGHT) );
//			}
//			else {
//				label.setText( "" );
//			}
//			label.setText( "" );
//			
			setForeignKeyOrJoinTable(link, label, true );
		}
		
		//--- 1.3
		{
			Label label2 = new Label( row, SWT.CENTER );
			String s = "" ;
//			if ( link.isTypeOneToMany() )  { s = "1  ----->  *"; } 
//			if ( link.isTypeManyToOne() )  { s = "*  ----->  1"; } 
//			if ( link.isTypeManyToMany() ) { s = "*  ----->  *"; } 
//			if ( link.isTypeOneToOne() )   { s = "1  ----->  1"; } 
			if ( link.getCardinality() == Cardinality.ONE_TO_MANY )  { s = "1  ----->  *"; } 
			if ( link.getCardinality() == Cardinality.MANY_TO_ONE )  { s = "*  ----->  1"; } 
			if ( link.getCardinality() == Cardinality.MANY_TO_MANY ) { s = "*  ----->  *"; } 
			if ( link.getCardinality() == Cardinality.ONE_TO_ONE )   { s = "1  ----->  1"; } 
			label2.setText(s) ;
			label2.setLayoutData( new GridData(width2, height) ) ; // to define Button width
			label2.setBackground(white);
			label2.setToolTipText(toolTip);
			if ( link.isOwningSide() ) {
				label2.setForeground(red);
			}
			else {
				label2.setForeground(blue);
			}
		}
	
		//--- 1.4
		{
			Label label = new Label( row, SWT.LEFT );
			label.setLayoutData( new GridData(width_icon, height) ) ; 
			label.setBackground(white);
			//label.setToolTipText(toolTip);
//			if ( link.isOwningSide() ) {
//				label.setImage( WizardImages.getImage(WizardImages.ARROW_RIGHT) );
//			}
			setForeignKeyOrJoinTable(link, label, false );
		}
		
		//--- 1.5 
		{
			Label label = new Label( row, SWT.CENTER  );
			label.setText( link.getTargetTableName() ) ;		
			label.setLayoutData( new GridData(width3, height) ) ; // to define width
			label.setBackground(gray);
			if ( link.isOwningSide() ) {
				label.setToolTipText("");
			}
			else {
				label.setToolTipText("Owning side");
			}
		}
		
		//--- 1.6 
		{
		Button b2 = new Button(row, SWT.NONE);
        b2.setText("Edit ... ");
        b2.setData(row);
        b2.setLayoutData( new GridData(width4, height) );
        b2.addSelectionListener( new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent event)
            {
            	if ( event.widget instanceof Button ) {
            		Button b = (Button)event.widget ;
            		buttonEditLink(b);
            	}
            }
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }        	
        });
		}
		
		//------------------------------------------------------------------------------------
		//--- 2.0
		// Nothing ( Vertical Span in 1.0 )
		
		//--- 2 col 1,2,3
//		{
//			GridData gd = new GridData(500, height) ; // to define width
//			gd.horizontalSpan = 3 ;
//			Label l = new Label( row, SWT.LEFT );
//			String s = link.getJavaType() + " " + link.getJavaName() + " - " + link.getType() 
//				+ " (fetch=" + link.getFetch() +")" 
//				+ " ( " + ( link.isOwningSide() ? "Owning Side" : "Inverse Side" ) + " )" ;
//			//l.setText("Department dept (fetch=LAZY, cascade=ALL, optional)") ;
//			l.setText(s) ;
//			l.setLayoutData( gd ) ; 
//			l.setBackground(white);
//			l.setToolTipText(toolTip);
//		}
		//--- 2.1
		{
			Label l = new Label( row, SWT.LEFT );
//			l.setText( link.getJavaFieldType() + "  " + link.getJavaFieldName() ) ;
			l.setText( link.getFieldType() + "  " + link.getFieldName() ) ; // v 3.0.0
			l.setLayoutData( new GridData(width1, height) ) ; 
			l.setBackground(white);
			l.setToolTipText(toolTip);
		}
		
		//--- 2.2
		{
			Label label = new Label( row, SWT.LEFT );
			label.setLayoutData( new GridData(width_icon, height) ) ; 
			label.setBackground(white);
			if ( link.isOwningSide() ) {
				label.setImage( PluginImages.getImage(PluginImages.ARROW_RIGHT) );
				//label.setToolTipText("Owning side");
			}
			else {
				label.setText( "" );
			}
		}
		
		//--- 2.3
		{
			Label l = new Label( row, SWT.CENTER );
//			l.setText(  link.getCardinality() ) ;
			l.setText(  link.getCardinality().getText() ) ; // v 3.0.0
			l.setLayoutData( new GridData(width2, height) ) ; 
			l.setBackground(white);
			l.setToolTipText(toolTip);
		}

		//--- 2.4
		{
			Label label = new Label( row, SWT.LEFT );
			label.setBackground(white);
			label.setLayoutData( new GridData(width_icon, height) ) ; 
			if ( ! link.isOwningSide() ) {
				label.setImage( PluginImages.getImage(PluginImages.ARROW_LEFT) );
				//label.setToolTipText("Inverse side");
			}
			else {
				label.setText( "" );
			}
		}
		
		//--- 2.5
		{
			Label label = new Label( row, SWT.LEFT );
			label.setBackground(white);
			label.setText( "" );
			if ( ! link.isOwningSide() ) {
				label.setText( link.getMappedBy() );
				label.setToolTipText("mapped by " + link.getMappedBy() );
			}
			else {
				label.setToolTipText("");
			}
			
//			Composite panel = new Composite(row, SWT.NONE);
//			panel.setLayoutData( new GridData(width3, height) ) ; 
//			panel.setBackground(white);
//			//panel.setToolTipText(toolTip);
//			
//			RowLayout rowLayout = new RowLayout ();
//			rowLayout.marginTop = 0;
//			rowLayout.marginLeft = 0;
//			rowLayout.marginRight = 0;
//			rowLayout.spacing = 5;
//			panel.setLayout( rowLayout );
//			
//			Label img = new Label( panel, SWT.LEFT );
//			img.setBackground(white);
//			img.setToolTipText(toolTip);
//			Label txt = new Label( panel, SWT.LEFT );
//			//l.setLayoutData( new GridData(width3, height) ) ; 
//			txt.setBackground(white);
//			txt.setToolTipText(toolTip);
//			if ( link.isOwningSide() ) {
//				img.setImage( WizardImages.getImage(WizardImages.ARROW_RIGHT) );
//				txt.setText( "Owning side" ) ;
//			}
//			else {
//				img.setImage( WizardImages.getImage(WizardImages.ARROW_LEFT) );
//				txt.setText( "Inverse side" ) ;
//			}
			
		}

		//--- 2.6 
		{
		Button b2 = new Button(row, SWT.NONE);
        b2.setText("Remove ");
        b2.setLayoutData( new GridData(width4, height) );
        b2.setData(row);
        b2.addSelectionListener( new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent event)
            {
            	if ( event.widget instanceof Button ) {
            		Button b = (Button)event.widget ;
            		buttonRemoveLink(b);
            	}
            }
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        });
		}

		return row ;
	}
	
	//----------------------------------------------------------------------------------------
	//private void setForeignKeyOrJoinTable(Link link, Label label, boolean bOwningSide )
	private void setForeignKeyOrJoinTable(LinkInDbModel link, Label label, boolean bOwningSide )
	{
		if ( bOwningSide == link.isOwningSide() ) 
		{
			if ( link.isBasedOnForeignKey() ) {
				String fkName = link.getForeignKeyName();
				// ForeignKey fk = linksManager.getForeignKey(fkName);
//				ForeignKeyInDbModel fk = linksManager.getForeignKey(fkName); // v 3.0.0
				ForeignKeyInDbModel fk = repositoryModel.getForeignKeyByName(fkName); // v 3.0.0
				if ( fk != null ) {
					label.setImage( PluginImages.getImage(PluginImages.FOREIGNKEY) );
					label.setToolTipText(" Foreign Key : \"" + fkName + "\" ");
				}
				else {
					label.setText( "FK" );					
					label.setToolTipText(" (!) Non existent Foreign Key : \"" + fkName + "\" ");
				}
			}
			else if ( link.isBasedOnJoinTable() ) {
				//JoinTable joinTable = link.getJoinTable();
				String joinTableName = link.getJoinTableName();
				//Entity entity = linksManager.getEntity(joinTableName);
//				EntityInDbModel entity = linksManager.getEntityByTableName(joinTableName);
				EntityInDbModel entity = repositoryModel.getEntityByTableName(joinTableName);// v 3.0.0
				if ( entity != null ) {
					label.setImage( PluginImages.getImage(PluginImages.JOINTABLE) );
					label.setToolTipText(" Join Table : \"" + joinTableName + "\"" );
				}
				else {
					label.setText( "JT" );
					label.setToolTipText(" (!) Non existent Join Table : \"" + joinTableName + "\" " );
				}
			}
		}
		else 
		{
			
		}
		
	}
	//----------------------------------------------------------------------------------------
	private Composite getRowFromWidget(Button button)
	{
		Object buttonData = button.getData();
		if ( null == buttonData ) {
			MsgBox.error("Button data is null !");
			return null ;
		}
		if ( ! ( buttonData instanceof Composite ) )
		{
			MsgBox.error("Button data is not an instance of Composite !");
			return null ;
		}		
		return (Composite) buttonData ;
	}
	//----------------------------------------------------------------------------------------
	//private Link getLinkFromButton(Button button)
	private LinkInDbModel getLinkFromButton(Button button) // v 3.0.0
	{
		Composite row = getRowFromWidget(button);
		//Link link = getLinkFromRow(row);
		LinkInDbModel link = getLinkFromRow(row); // v 3.0.0
		if ( null == link ) {
			MsgBox.error("Row data is null (no link for this row) !");
			return null;
		}
		return link ;
	}
	//----------------------------------------------------------------------------------------
	private void buttonChangeUseFlag(Button button)
	{
//		Link link = getLinkFromButton(button);
		LinkInDbModel link = getLinkFromButton(button); // v 3.0.0
		if ( null == link ) return ;

//		if ( button.getSelection() != link.isUsed() ) {
//			link.setUsed( button.getSelection() );
//			pluginPage.setDirty();
//		}
		if ( button.getSelection() != link.isSelected() ) { // v 3.0.0
			link.setSelected( button.getSelection() );
			_editorPage.setDirty();
		}
	}
	//----------------------------------------------------------------------------------------
	private void buttonEditLink(Button b)
	{
		log("EDIT link ...");
		//Link link = getLinkFromButton(b);
		LinkInDbModel link = getLinkFromButton(b); // v 3.0.0
		if ( null == link ) return ;
		
		String linkBefore = link.getComparableString();
		log("EDIT link : link before = " + linkBefore );
		
		TitleAreaDialog dialog = new DialogBoxForLink( b.getDisplay().getActiveShell(), link ); 
		//--- Open the Dialog Box Window ( Modal Window )
		int iRet = dialog.open();
		//Object oRet = dialog.getValue() ;
		if ( Window.OK == iRet )
		{
			String linkAfter = link.getComparableString();
			log("EDIT link : link after = " + linkAfter );
			if ( ! linkBefore.equals(linkAfter) ) {
				log("EDIT link : change detected => reprint the list..."  );
				if ( link.isOwningSide() ) 
				{
					//--- Owning side => update inverse side if any 
//					RelationLinks relation = linksManager.getRelationByLinkId( link.getId() );
//					RelationLinksInDbModel relation = linksManager.getRelationByLinkId( link.getId() ); // v 3.0.0
					RelationLinksInDbModel relation = repositoryModel.getRelationByLinkId( link.getId() ); // v 3.0.0
//					Link inverseSideLink = relation.getInverseSideLink();
					LinkInDbModel inverseSideLink = relation.getInverseSideLink(); // v 3.0.0
					if ( inverseSideLink != null ) {
						//--- Set reverse cardinality
//						String sOwningSideCardinality = link.getCardinality();
						Cardinality sOwningSideCardinality = link.getCardinality(); // v 3.0.0
//						String sOldInverseSideCardinality = inverseSideLink.getCardinality() ;
						Cardinality sOldInverseSideCardinality = inverseSideLink.getCardinality() ; // v 3.0.0
//						String sNewInverseSideCardinality = null ;
						Cardinality sNewInverseSideCardinality = Cardinality.UNDEFINED ; // v 3.0.0
//						if ( RepositoryConst.MAPPING_ONE_TO_ONE.equals( sOwningSideCardinality ) )
						if ( sOwningSideCardinality == Cardinality.ONE_TO_ONE )
						{
//							sNewInverseSideCardinality = RepositoryConst.MAPPING_ONE_TO_ONE ;
							sNewInverseSideCardinality = Cardinality.ONE_TO_ONE ;
						}
//						else if ( RepositoryConst.MAPPING_ONE_TO_MANY.equals( sOwningSideCardinality ) )
						else if ( sOwningSideCardinality == Cardinality.ONE_TO_MANY )
						{
//							sNewInverseSideCardinality = RepositoryConst.MAPPING_MANY_TO_ONE ;
							sNewInverseSideCardinality = Cardinality.MANY_TO_ONE ;
						}
//						else if ( RepositoryConst.MAPPING_MANY_TO_ONE.equals( sOwningSideCardinality ) )
						else if ( sOwningSideCardinality == Cardinality.MANY_TO_ONE )
						{
//							sNewInverseSideCardinality = RepositoryConst.MAPPING_ONE_TO_MANY ;
							sNewInverseSideCardinality = Cardinality.ONE_TO_MANY;
						}
//						else if ( RepositoryConst.MAPPING_MANY_TO_MANY.equals( sOwningSideCardinality ) )
						else if ( sOwningSideCardinality == Cardinality.MANY_TO_MANY )
						{
//							sNewInverseSideCardinality = RepositoryConst.MAPPING_MANY_TO_MANY ;
							sNewInverseSideCardinality = Cardinality.MANY_TO_MANY  ;
						}
						if ( sNewInverseSideCardinality != null ) 
						{
							if ( sNewInverseSideCardinality != inverseSideLink.getCardinality() ) 
							{
								// Change the cardinality 
								inverseSideLink.setCardinality( sNewInverseSideCardinality );
								
								// Change the cardinality => change the type if necessary
//								if ( sNewInverseSideCardinality.endsWith( RepositoryConst.MAPPING_TO_ONE )
//										&& ( ! sOldInverseSideCardinality.endsWith( RepositoryConst.MAPPING_TO_ONE ) ) ) 
								if ( sNewInverseSideCardinality.isToOne() && ( ! sOldInverseSideCardinality.isToOne() ) )
								{
									// Change to "TO ONE" => change the Java Type in order to hold a single value
//									inverseSideLink.setJavaFieldType( inverseSideLink.getTargetEntityJavaType() );
									inverseSideLink.setFieldType( inverseSideLink.getTargetEntityClassName() );
								}
//								if ( sNewInverseSideCardinality.endsWith( RepositoryConst.MAPPING_TO_MANY )
//										&& ( ! sOldInverseSideCardinality.endsWith( RepositoryConst.MAPPING_TO_MANY ) ) ) 
								if ( sNewInverseSideCardinality.isToMany() && ( ! sOldInverseSideCardinality.isToMany() ) )
								{
									// Change to "TO MANY" => change the Java Type in order to hold a single value
//									inverseSideLink.setJavaFieldType( RepositoryConst.COLLECTION_JAVA_TYPE );
									inverseSideLink.setFieldType( RepositoryConst.COLLECTION_JAVA_TYPE );
								}								
							}
						}
						
						//--- Set "mapped by" field name
//						inverseSideLink.setMappedBy( link.getJavaFieldName() );
						inverseSideLink.setMappedBy( link.getFieldName() );
					}
				}
				
				// Something has changed 
				_editorPage.applyFilterCriteria(); // To rebuild all the list of links ( = "REFRESH" )
				_editorPage.setDirty();
			}
		}
		
	}
	//----------------------------------------------------------------------------------------
	private void buttonRemoveLink(Button b)
	{
		log("REMOVE link ...");
		Composite row = getRowFromWidget(b);
//		Link link = getLinkFromRow(row);
		LinkInDbModel link = getLinkFromRow(row); // v 3.0.0
		if ( null == link ) {
			MsgBox.error("Row data is null (no link for this row) !");
			return ;
		}
		
		if ( link.isOwningSide() ) 
		{
			//--- Owning side => remove inverse side if any 
//			RelationLinks relation = linksManager.getRelationByLinkId( link.getId() );
//			RelationLinksInDbModel relation = linksManager.getRelationByLinkId( link.getId() ); // v 3.0.0
			RelationLinksInDbModel relation = repositoryModel.getRelationByLinkId( link.getId() ); // v 3.0.0
			String s = "\n ( no inverse side )" ;
//			Link inverseSideLink = relation.getInverseSideLink();
			LinkInDbModel inverseSideLink = relation.getInverseSideLink(); // v 3.0.0
			Composite rowInvSide = null ;
			if ( inverseSideLink != null ) {
				s = "\n and its inverse side " ;
				rowInvSide = getRowByLinkId(inverseSideLink.getId());
			}
			if ( MsgBox.confirm("Do you realy want to remove this link (owning side)"
					+ s 
					+ "\n from the repository ? ") ) {
//				linksManager.removeRelation(relation); // Remove the 2 links
				repositoryModel.removeRelation(relation); // Remove the 2 links // v 3.0.0
				deleteRow(row);
				if ( rowInvSide != null ) {
					deleteRow(rowInvSide);
				}
				refresh();
				_editorPage.setDirty();
				_editorPage.refreshCount();
			}
		}
		else 
		{
			//--- Inverse side => remove only the current link
			if ( MsgBox.confirm("Do you realy want to remove this link (inverse side)"
					+ "\n from the repository ? ") ) {
//				linksManager.removeLink( link.getId() );
//				linksManager.removeLink( link.getId() );
				repositoryModel.removeLinkById( link.getId() ); // v 3.0.0
				deleteRow(row);
				refresh();
				_editorPage.setDirty();
				_editorPage.refreshCount();
			}
		}
	}
	//----------------------------------------------------------------------------------------
	private Composite getRowByLinkId(String id)
	{
		if ( id != null )
		{
			Composite[] rows = getAllRows();
			for ( int i = 0 ; i < rows.length ; i++ ) {
				Composite row = rows[i];
//				Link link = getLinkFromRow(row);
				LinkInDbModel link = getLinkFromRow(row); // v 3.0.0
				if ( id.equals( link.getId() ) ) {
					return row;
				}
			}
		}
		else
		{
			MsgBox.error("getRowByLinkId() : id is null !");
		}
		return null ;
	}
	
	//----------------------------------------------------------------------------------------
//	private Link getLinkFromRow(Composite row)
	private LinkInDbModel getLinkFromRow(Composite row) // v 3.0.0
	{
		Object data = row.getData();
		if ( data != null ) {
//			if ( data instanceof Link ) {
			if ( data instanceof LinkInDbModel ) { // v 3.0.0
//				return (Link) data ;
				return (LinkInDbModel) data ; // v 3.0.0
			}
			else {
				MsgBox.error("Row data is not an instance of Link !");
			}			
		}
		else {
			MsgBox.error("Row data is null !");
		}
		return null ;
	}
	//----------------------------------------------------------------------------------------
	protected void deleteItemInModel (Object item)
	{
		if ( item != null ) {
				
//			if ( item instanceof Link ) {
			if ( item instanceof LinkInDbModel ) { // v 3.0.0
//				Link link = (Link)item ;
				LinkInDbModel link = (LinkInDbModel)item ; // v 3.0.0
//				linksManager.removeLink( link.getId() );
				repositoryModel.removeLinkById( link.getId() );// v 3.0.0
			}
			else
			{
				MsgBox.error("deleteItemInModel : item is not an instance of Link");
			}
		}
		else {
			MsgBox.error("deleteItemInModel : item is null");
		}
	}
	//----------------------------------------------------------------------------------------
}
