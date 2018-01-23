package org.telosys.tools.eclipse.plugin.commons.unused;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;

/**
 * 
 */
/* public */ class ClassChooser 
{
	Shell  _parentShell = null ;
	Shell  _windowShell = null ;
	Button _buttonOk = null ;
	IJavaProject _javaProject = null ;
	ICompilationUnit _selectedClass = null ;
	
	public ClassChooser(Shell parentShell, IJavaProject javaProject)
	{
		_parentShell = parentShell ;
		_javaProject = javaProject ;
	}
	
	public void open( )
	{
		if ( _windowShell == null )
		{
			createWindow();
		}
		show();
		_selectedClass = null;
	}
	
	public void show( )
	{
		if ( _windowShell != null )
		_windowShell.setVisible(true);		
		_windowShell.setActive();
	}
	
	public void hide( )
	{
		if ( _windowShell != null )
		_windowShell.setVisible(false);		
	}
	
	public void close( )
	{
		if ( _windowShell != null )
		_windowShell.dispose();		
		_windowShell = null ;
	}
	
	public ICompilationUnit getSelected( )
	{
		return _selectedClass ;
	}
	
	private void createButtonOK( Composite panel )
	{
		_buttonOk = new Button(panel, SWT.PUSH);
		_buttonOk.setText("Ok");
		_buttonOk.setEnabled(false);
		_buttonOk.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		_buttonOk.addSelectionListener( getButtonOkEvents() );
	}
	
	private void createButtonCancel( Composite panel )
	{
		Button button = new Button(panel, SWT.PUSH);
		button.setText("Cancel");
		button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		button.addSelectionListener( getButtonCancelEvents() );
	}

	private Tree createTreePanel( Shell shell )
	{
		//--- Panel composite containing the "tree"
		Composite panel = new Composite(shell, SWT.BORDER);
		panel.setLayout(new GridLayout());
		panel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL));
		
		//--- The tree itself 
		Tree tree = new Tree(panel, SWT.NONE);
		tree.setToolTipText("Select a class");
		tree.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL));
		
		
		//--- Tree EVENTS
		tree.addSelectionListener(getTreeEvents());
		return tree ;
	}
	
	private void createButtonsPanel( Shell shell )
	{
		Composite panel = new Composite(shell, SWT.NONE);
		panel.setLayout(new GridLayout(2, false));
		panel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		//--- Button "Ok" => class selected
		createButtonOK( panel );
		//--- Button "Cancel" 
		createButtonCancel( panel );

	}
	
	private void createWindow()
	{
		// ---Creation de la fenetre
		//final Shell shell = new Shell(getShell(), SWT.BORDER | SWT.CLOSE);
		Shell shell = new Shell(_parentShell, SWT.BORDER | SWT.CLOSE | SWT.APPLICATION_MODAL );
		shell.setText("Class browser");
		shell.setLocation(new Point(450, 200));
		shell.setSize(400, 400);
		shell.setLayout(new GridLayout());
		shell.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		_windowShell = shell ;
		
		Tree tree = createTreePanel(shell);
		
		createButtonsPanel(shell);
		

		try {
			//printPackagesFragRoots();  // for DEBUG 
			populateTree(tree);
		} catch (JavaModelException e) {
			MsgBox.error("Cannot populate tree from JavaProject ! \n JavaModelException : " + e );
		}
	}
	
	private void populateTree( Tree tree ) throws JavaModelException
	{
		//--- Get all the "PackageFragmentRoots" ( SRC dir, JAR files, ZIP files, ... )
		IPackageFragmentRoot[] packages = _javaProject.getPackageFragmentRoots();
		boolean bUseIt = false ;
		for ( int i = 0 ; i < packages.length ; i++)
		{
			//--- Do not use Archive and External files
			IPackageFragmentRoot pfr = packages[i];
			bUseIt = true ;
			if ( pfr.isArchive() ) bUseIt = false ;
			if ( pfr.isExternal() ) bUseIt = false ;
			if ( bUseIt ) 
			{
				addPackageFragmentRoot(tree, pfr );
			}
		}
	}
	
	private void addPackageFragmentRoot( Tree tree, IPackageFragmentRoot pfr  ) throws JavaModelException
	{
		String sName = pfr.getElementName();
		//MsgBox.info("addPackageFragment : " + sPackageName );
		if ( sName != null )
		{
			// NB: Package fragment root name can be void ( "default project dir" )  
			if ( sName.trim().length() == 0 ) 
			{
				sName = "(default src dir)";
			}
		}
		else
		{
			MsgBox.error("PackageFragmentRoot name is null !");
			return;			
		}
		
		//MsgBox.info("addPackageFragmentRoot : " + pfr.getElementName() );
		TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setText(sName);

		//--- Packages of the given "PackageFragmentRoot"
		IJavaElement[] javaElements = pfr.getChildren();
		for ( int i = 0 ; i < javaElements.length ; i++) 
		{
			if ( javaElements[i] instanceof IPackageFragment )
			{
				IPackageFragment pkg = (IPackageFragment) javaElements[i];
				addPackageFragment( item, pkg );				
			}
			else
			{
				MsgBox.error("IPackageFragmentRoot child [" + i + "] is not an instance of IPackageFragment !");
				return;
			}
		}
		item.setExpanded(true);
	}
	
	private void addPackageFragment( TreeItem treeItem, IPackageFragment pkg ) throws JavaModelException
	{
		if ( pkg.hasChildren() != true )
		{
			// The given package is void => do not show in the tree 
			return ; 
		}
		String sPackageName = pkg.getElementName();
		//MsgBox.info("addPackageFragment : " + sPackageName );
		if ( sPackageName != null )
		{
			// NB: Package fragment root children collection 
			// always contains a void package name : the "default package"
			if ( sPackageName.trim().length() == 0 ) 
			{
				sPackageName = "(default package)";
			}
		}
		else
		{
			MsgBox.error("PackageFragment name is null !");
			return;			
		}
		
		
		TreeItem item = new TreeItem(treeItem, SWT.NONE);
		item.setText(sPackageName);

		//--- Classes of the given "PackageFragment"
		IJavaElement[] javaElements = pkg.getChildren();
		for ( int i = 0 ; i < javaElements.length ; i++) 
		{
			if ( javaElements[i] instanceof ICompilationUnit )
			{
				ICompilationUnit compilationUnit = (ICompilationUnit) javaElements[i];
				addClass( item, compilationUnit );
			}
			else
			{
				MsgBox.error("IPackageFragment child [" + i + "] is not an instance of ICompilationUnit !");
				return;
			}
		}
		//item.setExpanded(true);
	}
	
	private void addClass( TreeItem treeItem, ICompilationUnit compilationUnit ) throws JavaModelException
	{
		//MsgBox.info("addClass : " + compilationUnit.getElementName() );
		TreeItem item = new TreeItem(treeItem, SWT.NONE);
		IType type = compilationUnit.findPrimaryType();
		if ( type != null )
		{
			item.setText(type.getElementName());			
		}
		else
		{
			item.setText(compilationUnit.getElementName() + " ( no primary type )");
		}
		item.setData(compilationUnit);
	}
	
	
	protected void printPackagesFragRoots() throws JavaModelException
	{
		IPackageFragmentRoot[] packages = _javaProject.getPackageFragmentRoots();
		for ( int i = 0 ; i < packages.length ; i++)
		{
			String s = "";
			IPackageFragmentRoot p = packages[i];
			if ( p.isArchive() ) s += "ARCHIVE " ;
			if ( p.isExternal() ) s += "EXTERNAL " ;
			if ( p.isReadOnly() ) s += "READ-ONLY " ;
			MsgBox.info("Package root [" + i + "] : " + p.getElementName() + " : " + s );
		}
	}
	
	//----------------------------------------------------------------------------------
	// EVENTS 
	//----------------------------------------------------------------------------------
	private final SelectionListener getButtonOkEvents()
	{
		return new SelectionListener() 
		{
			public void widgetDefaultSelected(SelectionEvent e) 
			{
				//MsgBox.info("button OK : widgetDefaultSelected" );
			}
			public void widgetSelected(SelectionEvent e) 
			{
				//MsgBox.info("button OK : widgetSelected" );				
				close();
			}
		};
	}
	//----------------------------------------------------------------------------------
	private final SelectionListener getButtonCancelEvents()
	{
		return new SelectionListener() 
		{
			public void widgetDefaultSelected(SelectionEvent e) 
			{
				//MsgBox.info("button CANCEL : widgetDefaultSelected" );
			}
			public void widgetSelected(SelectionEvent e) 
			{
				//MsgBox.info("button CANCEL : widgetSelected" );
				close();
			}
		};
	}
	//----------------------------------------------------------------------------------
	private final SelectionListener getTreeEvents()
	{
		return new SelectionListener() 
		{
			//--- Double-Click on a Tree Item
			public void widgetDefaultSelected(SelectionEvent e) 
			{
				MsgBox.info("TREE : widgetDefaultSelected" );
			}

			//--- Tree Item selection 
			public void widgetSelected(SelectionEvent e) 
			{
				//MsgBox.info("TREE : widgetSelected " );
				
				Widget widget = e.item;
				if ( ( widget instanceof TreeItem ) != true )
				{
					MsgBox.error("Event widget is not a TreeItem");
					return;
				}
				TreeItem item = (TreeItem) widget ;
				
				//--- Retrieve the DATA (object) associated with the selected item ( IJavaElement )
				Object oData = item.getData();
				if ( oData == null )
				{
					//MsgBox.error("TreeItem data is null ! ICompilationUnit expected !");
					_buttonOk.setEnabled(false);
					return;
				}
				if ( ( oData instanceof ICompilationUnit ) != true )
				{
					MsgBox.error("TreeItem data is not a ICompilationUnit");
					_buttonOk.setEnabled(false);
					return;
				}
				
				//--- Ok the selected item is a valid Java Class ( CompilationUnit )
				ICompilationUnit compilationUnit = (ICompilationUnit) oData;
				//MsgBox.info("ICompilationUnit name : " + compilationUnit.getElementName() );
				_selectedClass = compilationUnit ;
				_buttonOk.setEnabled(true);
				
			}
		};
	}
}
