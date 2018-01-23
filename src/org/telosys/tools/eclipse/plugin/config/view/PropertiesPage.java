package org.telosys.tools.eclipse.plugin.config.view;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;
import org.telosys.tools.commons.DirUtil;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.env.EnvironmentManager;
import org.telosys.tools.commons.env.TelosysToolsEnv;
import org.telosys.tools.commons.github.GitHubClient;
import org.telosys.tools.commons.http.HttpUtil;
import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.eclipse.plugin.MyPlugin;
import org.telosys.tools.eclipse.plugin.PluginBuildInfo;
import org.telosys.tools.eclipse.plugin.commons.EclipseProjUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.commons.TelosysPluginException;
import org.telosys.tools.eclipse.plugin.commons.Util;
import org.telosys.tools.eclipse.plugin.commons.dialogbox.FolderSelectionDialogBox;
import org.telosys.tools.eclipse.plugin.config.ProjectConfigManager;
import org.telosys.tools.eclipse.plugin.settings.SettingsManager;
import org.telosys.tools.generator.GeneratorVersion;
import org.telosys.tools.generator.context.names.ContextNames;

/**
 * Project properties configuration page ( one configuration file for each project )
 * This page contains 5 tabs
 * 
 */
public class PropertiesPage extends PropertyPage {
	
	private final static boolean DEBUG_MODE = false ;

	private final static String WEB_CONTENT     = "WebContent" ;
	//private final static String DEFAULT_GITHUB_USER_NAME = "telosys-tools" ;
	private final static String DEFAULT_GITHUB_USER_NAME = "telosys-templates-v3" ; // v 3.0.0
	
	private Composite _pageComposite ;
	private Composite _panelNotInitialized ;
	
    //--- Tab "Info"
	
	//--- Tab "Folders"
	private Text _tSrcFolder = null ;
	private Text _tResFolder = null ;
	private Text _tWebFolder = null ;
	private Text _tTestSrcFolder = null ;
	private Text _tTestResFolder = null ;
	private Text _tDocFolder = null ;
	private Text _tTmpFolder = null ;
	
    //--- Tab "Packages"
	private Text _tEntityPackage = null ;
	private Text _tRootPackage = null ; 
	
	//--- Tab "Templates" ( "Download" )
	private Text    _tGitHubUserName = null;
	private List    _listGitHubRepositories = null ;
	private Button  _checkBoxUnzipDownload = null ;
	private Text    _tLogger = null ;
	
    //--- Tab "General"
	private Text _tProjectName = null ;
	private Text _tProjectLocation = null;
	private Text _tWorkspaceLocation = null ;
	private Text _tPluginConfigFile = null ;
	
	private Text _tModelsFolder = null ;
	private Text _tDownloadsFolder = null ;
	private Text _tLibrariesFolder = null ;
	private Text _tTemplatesFolder = null ;
	private Text _tDestinationFolder = null ;
	
	//--- Tab "Variables"
	private VariablesTable _variablesTable = null ;
	
	//--- Tab "Advanced"
	private Button _rbTemplatesFolderStandard ;
	private Button _rbTemplatesFolderWorkspace ;
	private Button _rbTemplatesFolderFilesystem ;
	private Link   _linkTemplatesFolderBrowse ;
	private Text   _tSpecificTemplatesFolder ;
	
	private Button _rbDestinationFolderStandard ;
	private Button _rbDestinationFolderFilesystem ;
	private Link   _linkDestinationFolderBrowse ;
	private Text   _tSpecificDestinationFolder ;
	
//	//--- Tab "Advanced" (OLD)
//	private Label checkClassDirLabel;
//	private Text checkClassDirText;
//	//private Label checkClassLabel;
//	private Text checkClassText;
//	private Group checkGroup;
//	private Button classDirPickerButton;
//	private Button defaultCheck;
//	private Label resultTest;
//	private Button specificCheck;
//	private Button testClassButton;

	/**
	 * Constructor
	 */
	public PropertiesPage() {
		super();
		//MsgBox.info("PropertiesPage constructor " );
		// NB : do not use getElement here ( no yet set ) 
	}

	private void log(String s) {
		PluginLogger.log(s);
	}
	
	private void createTabAdvancedTemplatesFolderGroup(Composite composite) {
		Group group = new Group(composite, SWT.NONE);
		group.setLayoutData(getColSpan(5));
		group.setText("Templates bundles folder location");
		group.setLayout(new GridLayout(2, false));

		_rbTemplatesFolderStandard = new Button(group, SWT.RADIO);
		_rbTemplatesFolderStandard.setText("Standard location : '" + TelosysToolsEnv.getInstance().getTemplatesFolder() + "' in the current project ");
		_rbTemplatesFolderStandard.setLayoutData(getColSpan(2));
		
		_rbTemplatesFolderWorkspace = new Button(group, SWT.RADIO);
		_rbTemplatesFolderWorkspace.setText("Specific location : this current workspace (each bundle is a project)");
		_rbTemplatesFolderWorkspace.setLayoutData(getColSpan(2));
		
		_rbTemplatesFolderFilesystem = new Button(group, SWT.RADIO);
		_rbTemplatesFolderFilesystem.setText("Specific location : a folder somewhere in the filesystem ");
		_rbTemplatesFolderFilesystem.setData("");

		_linkTemplatesFolderBrowse = new Link(group, SWT.NONE);
		_linkTemplatesFolderBrowse.setText("<A>Browse to select a folder...</A>");
		_linkTemplatesFolderBrowse.setEnabled(false);
		
		_tSpecificTemplatesFolder = new Text(group, SWT.BORDER);
		_tSpecificTemplatesFolder.setLayoutData(getColSpan(2));
		_tSpecificTemplatesFolder.setEditable(false);

//		browseLink.addSelectionListener( 
//				new FolderChooserSelectionListener(this.getShell(), "Select templates folder", _tSpecificTemplatesFolder) );
		_linkTemplatesFolderBrowse.addSelectionListener( new SelectionAdapter () {
			public void widgetSelected(SelectionEvent event) {
				String folder = FolderSelectionDialogBox.chooseFolder(_linkTemplatesFolderBrowse.getShell(), "Select templates folder") ;
				// Folder can be null if "Escape/Cancel" (no folder selected)
				if ( folder != null ) {
					_tSpecificTemplatesFolder.setText(folder);
					_rbTemplatesFolderFilesystem.setData(folder);
				}
			}
		} );
		
		_rbTemplatesFolderStandard.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent event) {
				_linkTemplatesFolderBrowse.setEnabled(false);
				_tSpecificTemplatesFolder.setText("");
			}
		} );
		_rbTemplatesFolderWorkspace.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent event) {
				_linkTemplatesFolderBrowse.setEnabled(false);
				_tSpecificTemplatesFolder.setText(getCurrentWorkspaceAbsolutePath());
			}
		} );
		_rbTemplatesFolderFilesystem.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent event) {
				_tSpecificTemplatesFolder.setText((String)_rbTemplatesFolderFilesystem.getData());
				_linkTemplatesFolderBrowse.setEnabled(true);
			}
		} );
	}
	
	private void createTabAdvancedDestinationFolderGroup(Composite composite) {
		Group group = new Group(composite, SWT.NONE);
		group.setLayoutData(getColSpan(5));
		group.setText("Destination folder location for code generation");
		group.setLayout(new GridLayout(2, false));

		_rbDestinationFolderStandard = new Button(group, SWT.RADIO);
		_rbDestinationFolderStandard.setText("Standard location : the current project ");
		_rbDestinationFolderStandard.setLayoutData(getColSpan(2));
		//_rbDestinationFolderStandard.setSelection(true); // Selected by default 
		
		_rbDestinationFolderFilesystem = new Button(group, SWT.RADIO);
		_rbDestinationFolderFilesystem.setText("Specific location : a folder somewhere in the filesystem ");
		_rbDestinationFolderFilesystem.setData("");
		
		_linkDestinationFolderBrowse = new Link(group, SWT.NONE);
		_linkDestinationFolderBrowse.setText("<A>Browse to select a folder...</A>");
		_linkDestinationFolderBrowse.setEnabled(false);
		
		_tSpecificDestinationFolder = new Text(group, SWT.BORDER);
		_tSpecificDestinationFolder.setLayoutData(getColSpan(2));
		_tSpecificDestinationFolder.setEditable(false);
		_tSpecificDestinationFolder.addModifyListener( new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				PluginLogger.log("Text modified");
				
			}
		});

		_linkDestinationFolderBrowse.addSelectionListener( new SelectionAdapter () {
			public void widgetSelected(SelectionEvent event) {
				String folder = FolderSelectionDialogBox.chooseFolder(_linkDestinationFolderBrowse.getShell(), 
						"Select generation destination folder") ;
				// Folder can be null if "Escape/Cancel" (no folder selected)
				if ( folder != null ) {
					_tSpecificDestinationFolder.setText(folder);
					_rbDestinationFolderFilesystem.setData(folder);
				}
			}
		} );
		_rbDestinationFolderStandard.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent event) {
				_linkDestinationFolderBrowse.setEnabled(false);
				_tSpecificDestinationFolder.setText("");
			}
		} );
		_rbDestinationFolderFilesystem.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent event) {
				_tSpecificDestinationFolder.setText((String)_rbDestinationFolderFilesystem.getData());
				_linkDestinationFolderBrowse.setEnabled(true);
			}
		} );
	}
	
//	private void createCheckGroup(Composite composite) {
//		checkGroup = new Group(composite, SWT.NONE);
//		checkGroup.setLayoutData(getColSpan(5));
//		checkGroup.setText("Init / Check");
//		checkGroup.setLayout(new GridLayout(3, false));
//
//		defaultCheck = new Button(checkGroup, SWT.RADIO);
//		defaultCheck.setText("Default");
//		defaultCheck.setLayoutData(getColSpan(3));
//		specificCheck = new Button(checkGroup, SWT.RADIO);
//		specificCheck.setText("Specific");
//		specificCheck.setLayoutData(getColSpan(3));
//		
//		Label checkClassLabel = new Label(checkGroup, SWT.NONE);
//		checkClassLabel.setText("Check class");
//		checkClassText = new Text(checkGroup, SWT.BORDER);
//		checkClassText.setLayoutData(getColSpan(2));
//		
//		checkClassDirLabel = new Label(checkGroup, SWT.NONE);
//		checkClassDirLabel.setText("Directory");
//		checkClassDirText = new Text(checkGroup, SWT.BORDER);
//		checkClassDirText.setLayoutData(getColSpan(1));
//
//		createCheckDirPicker(checkGroup, composite);
//		testClassButton = new Button(checkGroup, SWT.PUSH);
//		testClassButton.setText("Test class loading");
//		resultTest = new Label(checkGroup, SWT.BORDER);
//		resultTest.setLayoutData(getColSpan(2));
//	}
//	private void createCheckDirPicker(Composite group, final Composite cevent) {
//		classDirPickerButton = new Button(group, SWT.PUSH);
//		classDirPickerButton.setText("...");
//	}

	private boolean isProjectInitialized() {
		EnvironmentManager em = getEnvironmentManager();
		return em.isEnvironmentInitialized();
	}
	
	//------------------------------------------------------------------------------------------
	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		
		IProject project = getCurrentProject();
		String projectName = project != null ? project.getName() : "?" ;
		this.setTitle("Telosys Tools configuration - Project \"" + projectName + "\"");

		try {
			_pageComposite = new Composite(parent, SWT.NONE);
			_pageComposite.setLayout(new FillLayout());
	
			if ( isProjectInitialized() ) {
				//--- Standard case 
				createContentsStandardAndInitFields() ;
			}
			else {
				//--- First time : project not yet initialized
				createContentsNotInitialized();
			}
		} 
		catch ( Exception e ) {
			MsgBox.error("Error in createContents().", e);	
		}
		return _pageComposite ;
	}
	
	//------------------------------------------------------------------------------------------
	private void createContentsStandardAndInitFields() {
		
		//MsgBox.info("createContentsStandard()");
		TabFolder tabFolder = new TabFolder(_pageComposite, SWT.NONE);

		createTabInfo(tabFolder);
		createTabFolders(tabFolder);
		createTabPackages(tabFolder); 
		createTabVariables(tabFolder);
		createTabTemplates(tabFolder);
		createTabGeneral(tabFolder);
		createTabAdvanced(tabFolder);
		createTabAboutPlugin(tabFolder);
		
		//--- This tab is for DEBUG only 
		if ( DEBUG_MODE ) {
			createTabDebug(tabFolder) ;
		}
		
		//--- Populate fields values from configuration file
//		ProjectConfig projectConfig = loadProjectConfig();
////		configToFields( projectConfig );
//		configToFields( projectConfig.getTelosysToolsCfg() ); // v 3.0.0
		configToFields( loadProjectConfig() ) ; // v 3.0.0
		
		_pageComposite.layout(); // Mandatory when creating new controls dynamically 
	}	
	//------------------------------------------------------------------------------------------
	private void createContentsNotInitialized() {
		
		//--- Panel 
//		Composite panelNotInitialized = new Composite(_pageComposite, SWT.NONE);
//		// panelNotInitialized.setLayout( panelLayout );
//
//		final int nbCol = 1 ;
//		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
//		tabItem.setText(" Not initialized ");
		
		Composite panel = new Composite(_pageComposite, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		
		panel.setLayout(gridLayout);
		
		Label voidLabel = new Label(panel, SWT.NONE) ;
		voidLabel.setText("");
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.CENTER ;
		gd.verticalAlignment = SWT.TOP ;
		gd.minimumWidth = 600 ;
		gd.widthHint    = 600 ;
		//gd.minimumHeight = 20 ;
		voidLabel.setLayoutData(gd);
		//voidLabel.setSize(400, 30);
		
		
		new Label(panel, SWT.CENTER).setText("This project is not yet initialized for Telosys ");
		new Label(panel, SWT.CENTER).setText("Click on the following button to initialize the project :");

		
		Button initButton = new Button(panel, SWT.PUSH);
		initButton.setText("Initialize Telosys Tools");
		initButton.setToolTipText(" Creates the Telosys Tools folders \n"
				+ " and the databases configuration file \n"
				+ " if they don't exist");
		initButton.addSelectionListener(new InitButtonSelectionListener(this) ); 
		
		_panelNotInitialized = panel ;
	}

	//------------------------------------------------------------------------------------------
	/**
	 * Creates the "General" TabItem
	 * @param tabFolder
	 */
	private void createTabGeneral(TabFolder tabFolder) 
	{
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(" General ");
		
		Composite tabContent = new Composite(tabFolder, SWT.NONE);
		tabContent.setLayout(new GridLayout(3, false));
		tabItem.setControl(tabContent);
		
		//-------------------------------------------------------------------------------
		
		_tProjectName = createTextField(tabContent, "Project name : ") ;
		_tProjectName.setEditable(false);	

		_tWorkspaceLocation = createTextField(tabContent, "Workspace location :") ;
		_tWorkspaceLocation.setEditable(false);		

		_tProjectLocation = createTextField(tabContent, "Project location :") ;
		_tProjectLocation.setEditable(false);		

		_tPluginConfigFile = createTextField(tabContent, "Project config file : ") ;
		_tPluginConfigFile.setEditable(false);	
		
		//-------------------------------------------------------------------------------
		_tModelsFolder = createTextField(tabContent, "Models folder :") ;
		_tModelsFolder.setEditable(false);	

		_tDownloadsFolder = createTextField(tabContent, "Downloads folder :") ;
		_tDownloadsFolder.setEditable(false);	

		_tLibrariesFolder = createTextField(tabContent, "Libraries folder :") ;
		_tLibrariesFolder.setEditable(false);	

		_tTemplatesFolder = createTextField(tabContent, "Templates folder :") ;
		_tTemplatesFolder.setEditable(false);	
		_tTemplatesFolder.setToolTipText("Templates bundles folder");

		_tDestinationFolder = createTextField(tabContent, "Destination folder :") ;
		_tDestinationFolder.setEditable(false);	
		_tDestinationFolder.setToolTipText("Destination folder for code generation");
		
	}	
	//------------------------------------------------------------------------------------------
	/**
	 * Initialized the project with Telosys Tools files and folders <br>
	 * This method is called by the button listener
	 */
	protected void initializeProject() {
		//MsgBox.info("initializeProject()");
		
		initTelosysToolsEnv();
		
		//--- Remove the current panel
		_panelNotInitialized.dispose();
		
		//--- Creates the standard tab items
		createContentsStandardAndInitFields();
	}
	//------------------------------------------------------------------------------------------
	// V 3.0.0
	/**
	 * Initializes Telosys Tools environment files in the current project
	 */
	private void initTelosysToolsEnv() {
		IProject currentProject = getCurrentProject();
		EnvironmentManager em = getEnvironmentManager();
		StringBuffer sb = new StringBuffer();
		sb.append("Project initialization \n");
//		sb.append("Project folder : '" + projectFolderFullPath + "' \n");
		sb.append("\n");
				
		//-- Specific variables to be used in initialization
		String projectName = currentProject.getName() ;
		LinkedList<Variable> specificVariables = new LinkedList<Variable>();
		specificVariables.add( new Variable("MAVEN_ARTIFACT_ID", projectName            ) ) ; 
		//specificVariables.add( new Variable("MAVEN_GROUP_ID",    "group.to.be.defined"  ) ) ; // already set in the cfg file
		specificVariables.add( new Variable("PROJECT_NAME",      projectName            ) ) ; 
		//specificVariables.add( new Variable("PROJECT_VERSION",   "0.1"                  ) ) ; // already set in the cfg file
		
		//--- Initializes the environment and set the given specific variables
		em.initEnvironment(sb, specificVariables);
		
		TelosysToolsEnv telosysToolsEnv = TelosysToolsEnv.getInstance();
		// replace telosysToolsCfg by telosysToolsEnv
		
		// Refresh potential new files : 
		EclipseProjUtil.refreshFile(currentProject, telosysToolsEnv.getTelosysToolsConfigFileName() ); // file "telosys-tools.cfg"
		EclipseProjUtil.refreshFile(currentProject, telosysToolsEnv.getDatabasesDbCfgFilePath() ); // e.g. 'TelosysTools/databases.dbcfg'

		EclipseProjUtil.refreshFolder(currentProject, telosysToolsEnv.getTelosysToolsFolder() ); // folder "TelosysTools"
		//EclipseProjUtil.refreshFolder(currentProject, telosysToolsEnv.getModelsFolder() );    // e.g. "TelosysTools"
		EclipseProjUtil.refreshFolder(currentProject, telosysToolsEnv.getDownloadsFolder() ); // e.g. "TelosysTools/downloads"
		EclipseProjUtil.refreshFolder(currentProject, telosysToolsEnv.getLibrariesFolder() ); // e.g. "TelosysTools/libs"
		EclipseProjUtil.refreshFolder(currentProject, telosysToolsEnv.getTemplatesFolder() ); // e.g. "TelosysTools/templates"
		
		// initFieldsFromConfigurationFile() ;
		MsgBox.info(sb.toString());		
	}
	
	//------------------------------------------------------------------------------------------
	/**
	 * Creates the "Packages" TabItem
	 * @param tabFolder
	 */
	private void createTabPackages(TabFolder tabFolder) {
		final int nbCol = 3 ;
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(" Packages ");
		
		Composite tabContent = new Composite(tabFolder, SWT.NONE);
		tabContent.setLayout(new GridLayout(nbCol, false));
		tabItem.setControl(tabContent);

		//_tEntityPackage = createTextField(tabContent, "Entity classes package ") ;
		createSingleLabel(tabContent, "Define here the standard packages variables (usable in targets and templates)", nbCol);
		createSingleLabel(tabContent, "If you need more packages define them in \"Variables\" ", nbCol);
		_tRootPackage   = createTextField(tabContent, "Root package ",           "${ROOT_PKG}") ;
		_tEntityPackage = createTextField(tabContent, "Entity classes package ", "${ENTITY_PKG}") ;
		
//		createOneLabel(tabContent, "" ); 
//		createOneLabel(tabContent, "If you need more packages define them in the \"Variables\" " );
	}
	
	//------------------------------------------------------------------------------------------
	private void createTabInfo(TabFolder tabFolder) {
		final int nbCol = 2 ;
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(" Information ");
		
		Composite tabContent = new Composite(tabFolder, SWT.NONE);
		tabContent.setLayout(new GridLayout(nbCol, false));
		tabItem.setControl(tabContent);

		createSingleLabel(tabContent, " ", nbCol); // void line
		createSingleLabel(tabContent, "Telosys generator configuration main steps : ", nbCol);
		
		int index = 0 ;
		createTabInfoDescLink(tabContent, tabFolder, " - \"<A>Folders</A>\" : ", 
				"to configure the standard variables for project folders", ++index);
		createTabInfoDescLink(tabContent, tabFolder, " - \"<A>Packages</A>\" : ", 
				"to configure the standard variables for project packages", ++index);
		createTabInfoDescLink(tabContent, tabFolder, " - \"<A>Variables</A>\" : ", 
				"to configure the specific variables", ++index);
		createTabInfoDescLink(tabContent, tabFolder, " - \"<A>Templates</A>\" : ", 
				"to download and install templates bundles", ++index);
		
		createSingleLabel(tabContent, " ", nbCol); // void line
		createSingleLabel(tabContent, "Do not forget to click \"Apply\" to save the configuration ", nbCol);
		createSingleLabel(tabContent, " ", nbCol); // void line
		
		createTabInfoHelpLink(tabContent, nbCol);
		//tabContent.pack();
	}
	//------------------------------------------------------------------------------------------
	private void createTabInfoHelpLink(Composite composite, int colSpan ){
		createSingleLabel(composite, "For more information about Telosys : ", colSpan); // void line
		Link link = new Link(composite, SWT.NONE);
		link.setText(" - Telosys web site : <A>www.telosys.org</A>");
		link.setLayoutData(getColSpan(colSpan));
		link.addSelectionListener(
			new SelectionListener() {
	            public void widgetSelected(SelectionEvent arg0) {
//	            	final String helpResource = 
//	            		// "/org.telosys.tools.eclipse.plugin.help" ; // OK : global help root 
//	            		"/org.telosys.tools.eclipse.plugin.help/html/getting-started.html"; // OK : specific topic 
//	            		// PB : no topic for TOC
//	            		// "/org.telosys.tools.eclipse.plugin.help/html/toc.html"; // Topic not found
//	            		// "/org.telosys.tools.eclipse.plugin.help/html";  // Topic not found
//	            	
//	            	// Add a Table Of Content ? : 
//	            	// "/org.telosys.tools.eclipse.plugin.help/html/toc.html"
//	            	// in PluginHelp : 
//	            	//   create file html/toc.html 
//	            	//   change TelosysToolsHelpTOC.xml : <toc ... topic="html/toc.html" >
//	            	
//	            	IWorkbenchHelpSystem helpSystem = PlatformUI.getWorkbench().getHelpSystem() ;
//	            	//helpSystem.displayHelp(); // programmatically open the Help window
//	            	helpSystem.displayHelpResource(helpResource);
	            	Util.launchExternalBrowser("http://www.telosys.org/");
	            }
	            public void widgetDefaultSelected(SelectionEvent arg0) { }
        	});
		
		link = new Link(composite, SWT.NONE);
		link.setText(" - Telosys on Twitter : <A>@telosys</A>");
		link.setLayoutData(getColSpan(colSpan));
		link.addSelectionListener(
			new SelectionListener() {
	            public void widgetSelected(SelectionEvent arg0) {
	            	Util.launchExternalBrowser("https://twitter.com/telosys");
	            }
	            public void widgetDefaultSelected(SelectionEvent arg0) { }
        	});
	}
	
	/**
	 * Creates the "Folders" TabItem
	 * @param tabFolder
	 */
	private void createTabFolders(TabFolder tabFolder) {
		final int nbCol = 3 ;
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(" Folders ");
		
		Composite tabContent = new Composite(tabFolder, SWT.NONE);
		tabContent.setLayout(new GridLayout(nbCol, false));
		tabItem.setControl(tabContent);

		createSingleLabel(tabContent, "Define here the standard folders variables (usable in targets and templates)", nbCol);
		createSingleLabel(tabContent, "If you need more folders define them in \"Variables\" ", nbCol);
		_tSrcFolder = createTextField(tabContent, "Sources",      "${SRC}") ;
		_tResFolder = createTextField(tabContent, "Resources ",   "${RES}") ;
		_tWebFolder = createTextField(tabContent, "Web content ", "${WEB}" ) ;

		_tTestSrcFolder = createTextField(tabContent, "Tests sources  ",  "${TEST_SRC}") ;
		_tTestResFolder = createTextField(tabContent, "Tests resources ", "${TEST_RES}") ;
		
		_tDocFolder = createTextField(tabContent, "Documentation",   "${DOC}" ) ;
		_tTmpFolder = createTextField(tabContent, "Temporary files", "${TMP}" ) ;
		
		createTabFoldersButtons(tabContent);
		
//		createOneLabel(tabContent, "" ); 
//		createOneLabel(tabContent, "If you need more folders define them in the \"Variables\" " );
		 
	}
	
	//------------------------------------------------------------------------------------------
	/**
	 * Creates the "Variables" TabItem
	 * @param tabFolder
	 */
	private void createTabVariables(TabFolder tabFolder) 
	{
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(" Variables ");
		
		/*
		 +----------------+---------+
		 ! Label (SPAN 2)           !
		 +----------------+---------+
		 ! Table          ! Buttons !
		 +----------------+---------+		 
		 */
		Composite tabContent = new Composite(tabFolder, SWT.NONE);
		tabContent.setLayout( new GridLayout(2, false));
		tabItem.setControl(tabContent);
		
		//--- Label ( Col 1 and 2 in the GRID )
		Label label= new Label(tabContent, SWT.LEFT | SWT.WRAP );
		label.setFont(tabContent.getFont());
		label.setText("Define here your own specific variables (usable in targets and templates)");
		GridData gd = new GridData();
		//gd.heightHint = 300 ;
		//gd.horizontalAlignment = GridData.FILL;
		gd.horizontalAlignment = SWT.BEGINNING; 
		gd.grabExcessHorizontalSpace = false;
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);

		//--- Table ( Col 1 in the GRID )
		_variablesTable = new VariablesTable(tabContent);
		GridData gdTable = new GridData();
		//gd.heightHint = 300 ;
		gdTable.heightHint = 300 ; // prefered height (in pixels)
		gdTable.widthHint = 460 ; // prefered width (in pixels)
		//gd.horizontalAlignment = GridData.FILL;
		gdTable.horizontalAlignment = SWT.BEGINNING; 
		gdTable.grabExcessHorizontalSpace = false;
		//gdTable.horizontalSpan = 2;
		_variablesTable.setLayoutData(gdTable);
		
		//--- Buttons ( Col 2 in the GRID )
		createTableButtons(tabContent, _variablesTable) ;
		
		Button button = new Button(tabContent, SWT.PUSH);
		button.setText("Show reserved variable names");
		button.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent arg0) {
            	showReservedVariableNames();
            }
            public void widgetDefaultSelected(SelectionEvent arg0) { }
        });
	}	
	private String repeat(char c, int n) {
		char[] chars = new char[n];
		for ( int i = 0 ; i < n ; i++ ) {
			chars[i] = c ;
		}
		return new String(chars);
	}
	private String padRight(String s, int totalWidth, char paddingChar) {
		if ( s.length() >= totalWidth ) {
			return s ;
		}
		else {
			return s + repeat(' ', totalWidth - s.length());
		}
	}
	private boolean nameBreakNeeded(String previousName, String name) {
		if ( previousName.length() > 0 && name.length() > 0 ) {
			if ( Character.isUpperCase(previousName.charAt(0)) 
				&& Character.isLowerCase(name.charAt(0)) ) {
				return true ;
			}
		}
		return false ;
	}
	private void showReservedVariableNames() {
    	String[] reserverdNames = ContextNames.getSortedReservedNames() ;
    	StringBuffer sb = new StringBuffer();
    	sb.append("The following names are reserved : \n\n") ;
    	int i = 0 ;
    	String previousName = "";
    	for ( String name : reserverdNames ) {
    		if ( nameBreakNeeded(previousName, name) ) {
        		sb.append("\n\n");
        		i = 0 ;
    		}
    		i++ ;
    		if ( i%2 == 0 ) {
        		sb.append(name);
        		sb.append(" \n");
    		}
    		else {
        		sb.append(padRight(name, 18, ' '));
        		sb.append("\t\t");
    		}
    		previousName = name ;
    	}
    	MsgBox.info(sb.toString());
	}
	
	private Composite createTableButtons(Composite composite, VariablesTable table ) 
	{		
		RowLayout panelLayout = new RowLayout(); // Vertical row ( = column )
		panelLayout.type = SWT.VERTICAL ;
		panelLayout.fill = true ; // same width for all controls
		panelLayout.pack = false ; // no pack for all controls size
		
		//--- Panel for buttons
		Composite panelBouton = new Composite(composite, SWT.NONE);
		panelBouton.setLayout( panelLayout );
		
//		Color color = new Color( panelBouton.getDisplay(), 0xFF, 0, 0 );
//		panelBouton.setBackground(color);

		Button button = null;
		
		//--- "Add" button 
		button = new Button(panelBouton, SWT.PUSH);
		button.setText("Add");
		button.setToolTipText("Add an attribute at the end of the list");	
		button.addSelectionListener(table.getAddSelectionAdapter() ); 
		
		//--- "Insert" button 
		button = new Button(panelBouton, SWT.PUSH);
		button.setText("Insert");
		button.setToolTipText("Insert an attribute");	
		button.addSelectionListener(table.getInsertSelectionAdapter() ); 
		
		//--- "Delete" button 
		button = new Button(panelBouton, SWT.PUSH);
		button.setText("Delete");
		button.setToolTipText("Delete the selected attribute");	
		button.addSelectionListener(table.getDeleteSelectionAdapter() ); 
		
		return panelBouton ;
	}
	
	//------------------------------------------------------------------------------------------
	/**
	 * Creates the "Packages" TabItem
	 * @param tabFolder
	 */
	private void createTabTemplates(TabFolder tabFolder) {
		
		final int Col2With = 400 ;
		
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(" Templates ");
		
		//--- GRID with 2 columns
		Composite tabContent = new Composite(tabFolder, SWT.NONE);
		tabContent.setLayout(new GridLayout(2, false));
		tabItem.setControl(tabContent);
		//------------------------------------------------------------------------------------
		//--- Label  ( SPAN 2 )
		Label label = new Label(tabContent, SWT.NONE);
		label.setText("Download templates bundles from GitHub (a 'bundle' is a set of templates and resources)");
		label.setLayoutData(getColSpan(2));
		
		//------------------------------------------------------------------------------------
		//--- SEPARATOR  ( SPAN 2 )
		label = new Label(tabContent, SWT.NONE);
		label.setText("");
		label.setLayoutData(getColSpan(2));

		//------------------------------------------------------------------------------------
		//--- Label + Text field 
		label = new Label(tabContent, SWT.NONE);
		label.setText("GitHub user name : ");
		_tGitHubUserName = new Text(tabContent, SWT.BORDER);
		GridData gd = getCellGridData2();
		gd.widthHint   = Col2With ;
		_tGitHubUserName.setLayoutData(gd);
		_tGitHubUserName.setText(DEFAULT_GITHUB_USER_NAME);
		
		//------------------------------------------------------------------------------------
		//--- Void Label + Button  
		label = new Label(tabContent, SWT.NONE);
		label.setText("");
		
		Button b = new Button(tabContent, SWT.PUSH);
		b.setText("Get available bundles");
		b.setToolTipText(" Get available bundles \n from GitHub site ");
		b.addSelectionListener(new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent arg0)
            {
            	populateGitHubRepoList();
            }
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        }
		);

		//------------------------------------------------------------------------------------
		//--- Label + List of Repositories 
		label = new Label(tabContent, SWT.NONE);
		label.setText("GitHub bundles : ");	
		label.setLayoutData(getCellGridData1());
		
//		_tableGitHubRepositories = createGitHubRepositiriesTable(tabContent, 400);
//		GridData gd = getCellGridData2();
//		//gd.minimumHeight = 200 ;
//		gd.heightHint = 200 ;
//		gd.widthHint  = 400 ;
//		_tableGitHubRepositories.setLayoutData(gd);
//		_tableGitHubRepositories.setSize(400, 200);
//		//createTwoLabels(tabContent, "", "" ); // Separator
		_listGitHubRepositories = new List(tabContent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL );
		gd = getCellGridData2();
		gd.heightHint  = 120 ;
		gd.widthHint   = Col2With - 10 ;
		_listGitHubRepositories.setLayoutData(gd);
		
		//------------------------------------------------------------------------------------
// Removed in v 2.1.1
//		//--- Label + Text field 
//		label = new Label(tabContent, SWT.NONE);
//		label.setText("GitHub URL pattern : ");
//		
//		_tGitHubUrlPattern = new Text(tabContent, SWT.BORDER);
//		gd = getCellGridData2();
//		gd.widthHint   = Col2With ;
//		_tGitHubUrlPattern.setLayoutData(gd);
//		_tGitHubUrlPattern.setText("https://github.com/${USER}/${REPO}/archive/master.zip");
		
		//------------------------------------------------------------------------------------
		//--- Void Label + Composite [ Button + CheckBox ]  
		label = new Label(tabContent, SWT.NONE);
		label.setText("");
		label.setLayoutData(getCellGridData1());
		
		Composite composite1 = new Composite(tabContent, SWT.NONE);
		GridLayout gdComposite = new GridLayout(2, false);
		gdComposite.marginLeft = 0 ;
		gdComposite.horizontalSpacing = 80 ;
		gdComposite.marginWidth = 0 ;
		composite1.setLayout(gdComposite);

			b = new Button(composite1, SWT.PUSH);
			b.setText("Download selected bundles(s)");
			b.setToolTipText(" Download selected bundle(s) \n from GitHub site ");
			b.addSelectionListener(new SelectionListener() 
	    	{
	            public void widgetSelected(SelectionEvent arg0)
	            {
	            	if ( _listGitHubRepositories.getSelectionCount() > 0 ) {
	            		boolean bUnzip = _checkBoxUnzipDownload.getSelection() ;
	        			String[] selectedRepo = _listGitHubRepositories.getSelection();
	        			downloadSelectedFiles(selectedRepo, bUnzip);
	            	}
	            	else {
	            		MsgBox.warning("Select at least one file");
	            	}
	            }
	            public void widgetDefaultSelected(SelectionEvent arg0)
	            {
	            }
	        }
			);
	
			_checkBoxUnzipDownload = new Button(composite1, SWT.CHECK);
			_checkBoxUnzipDownload.setText("Install downloaded bundle(s)");
			_checkBoxUnzipDownload.setSelection(true);
		
		
		//------------------------------------------------------------------------------------
		//--- Label + Text field 
		label = new Label(tabContent, SWT.NONE);
		label.setText("Log : ");
		label.setLayoutData(getCellGridData1());
		
		//Text _tLogger = new Text (tabContent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL );
		_tLogger = new Text (tabContent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL );
		gd = getCellGridData2();
		gd.widthHint   = Col2With - 10 ;
		gd.heightHint  = 80 ;
		_tLogger.setLayoutData(gd);
		_tLogger.setEditable(false);


		//--- Label  ( SPAN 2 )
		label = new Label(tabContent, SWT.NONE);
		label.setText("If you experience download problems, check Eclipse proxy settings.");
		label.setLayoutData(getColSpan(2));
//		label = new Label(tabContent, SWT.NONE);
//		label.setText("");
//		label = new Label(tabContent, SWT.NONE);
//		label.setText("");
	}
	//------------------------------------------------------------------------------------------
	private GridData getCellGridData1() {
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.LEFT ;
		gd.verticalAlignment = SWT.TOP ;
		return gd;
	}
	private GridData getCellGridData2() {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = SWT.LEFT ;
		gd.verticalAlignment = SWT.TOP ;
		return gd;
	}
	//------------------------------------------------------------------------------------------
	private void populateGitHubRepoList() {
		
		String sGitHubUserName = getGitHubUserName();
		if ( sGitHubUserName != null ) {
			//--- Create the task
			PopulateListTaskWithProgress task = new PopulateListTaskWithProgress( 
					getTelosysToolsCfgFromFields(),
					sGitHubUserName, 
					_listGitHubRepositories );
			
			//--- Run the task with monitor
			ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog( Util.getActiveWindowShell() ) ;
			try {
				progressMonitorDialog.run(false, false, task);
			} catch (InvocationTargetException e) {
				MsgBox.error("Error during task", e.getCause() );
			} catch (InterruptedException e) {
				MsgBox.info("Task interrupted");
			}
			if ( _listGitHubRepositories.getItemCount() == 0 ) {
				MsgBox.info("No bundle available on GitHub '" + sGitHubUserName + "'");
			}
		}
	}
	//------------------------------------------------------------------------------------------
	private long downloadSelectedFiles(String[] repoNames, boolean bUnzip) {

//		String sDownloadFolder = getDownloadFolder();
//		if ( null == sDownloadFolder ) {
//			return 0 ;
//		}
//		String sGitHubUrlPattern = getGitHubUrlPattern() ;
//		if ( null == sGitHubUrlPattern ) {
//			return 0 ;
//		}
		if ( null == repoNames ) {
			MsgBox.error("Selection is null !");
			return 0 ;
		}
		
		TelosysToolsCfg telosysToolsCfg = getTelosysToolsCfgFromFields();
		
		if ( telosysToolsCfg.hasSpecificTemplatesFolders() ) {
			MsgBox.warning("A specific templates folder is currently defined. Donwload is not allowed");
			return 0 ;
		}
		
		try {
			DirUtil.checkIsValidDirectory(telosysToolsCfg.getTemplatesFolderAbsolutePath());
		} catch (TelosysToolsException e) {
			MsgBox.warning("Templates folder" + e.getMessage());
			return 0 ;
		}
		
		try {
			DirUtil.checkIsValidDirectory(telosysToolsCfg.getDownloadsFolderAbsolutePath());
		} catch (TelosysToolsException e) {
			MsgBox.warning("Download folder" + e.getMessage());
			return 0 ;
		}
		
		//--- Run the generation task via the progress monitor 
		DownloadTaskWithProgress task = null ;
		try {
			task = new DownloadTaskWithProgress(//this.getCurrentProject(), 
					telosysToolsCfg,
					getGitHubUserName(), 
					repoNames, 
//					sDownloadFolder, 
//					sGitHubUrlPattern, 
					bUnzip, // Unzip or not the downloaded file
//					sTemplatesFolder, // ie "TelosysTools/templates"
					_tLogger );
		} catch (TelosysPluginException e) {
    		MsgBox.error("Cannot create DownloadTaskWithProgress instance", e);
    		return 0 ;
		}

		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog( Util.getActiveWindowShell() ) ;
		try {
			progressMonitorDialog.run(false, false, task);
		} catch (InvocationTargetException e) {
			MsgBox.error("Error during download", e.getCause() );
		} catch (InterruptedException e) {
			MsgBox.info("Download interrupted");
		}
		
		return task.getResult();
	}
	
//	private String getDownloadFolder() {
//		String sFolder = _tDownloadsFolder.getText().trim();
//		if ( sFolder.length() == 0  ) {
//			MsgBox.warning("Download folder is not defined");
//			return null ;
//		}
//		if ( EclipseProjUtil.folderExists(getCurrentProject(), sFolder) ) {
//			return sFolder ;
//		}
//		else {
//			MsgBox.warning("Download folder '" + sFolder + "' does not exist !");
//			return null ;
//		}
//	}
	
//	private String getTemplatesFolder() {
//		String sFolder = _tTemplatesFolder.getText().trim();
//		if ( sFolder.length() == 0  ) {
//			MsgBox.warning("Templates folder is not defined");
//			return null ;
//		}
//		if ( EclipseProjUtil.folderExists(getCurrentProject(), sFolder) ) {
//			return sFolder ;
//		}
//		else {
//			MsgBox.warning("Templates folder '" + sFolder + "' does not exist !");
//			return null ;
//		}
//	}
	
	private String getGitHubUserName() {
		String user = _tGitHubUserName.getText().trim();
		if ( user.length() == 0  ) {
			MsgBox.warning("GitHub user name is void");
			return "" ;
		}
		return user ;
	}
	
	//------------------------------------------------------------------------------------------
	/**
	 * Creates the "Advanced" TabItem
	 * @param tabFolder
	 */
	private void createTabAdvanced(TabFolder tabFolder) {
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(" Advanced ");

		Composite tabContent = new Composite(tabFolder, SWT.NONE);
		tabContent.setLayout(new GridLayout(5, false));
		tabItem.setControl(tabContent);

		createTabAdvancedTemplatesFolderGroup(tabContent);
		createTabAdvancedDestinationFolderGroup(tabContent);
		//createCheckGroup(tabContent);
	}
	
	//------------------------------------------------------------------------------------------
	/**
	 * Creates the "About plugin" TabItem
	 * @param tabFolder
	 */
	private void createTabAboutPlugin(TabFolder tabFolder) 
	{
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(" About plugin ");
		
		Composite tabContent = new Composite(tabFolder, SWT.NONE);
		tabContent.setLayout(new GridLayout(3, false));
		tabItem.setControl(tabContent);
		
		Text t = null ; 
		t = createTextField(tabContent, "Name :") ;
		t.setEditable(false);
		t.setText( MyPlugin.getName() );
		
		t = createTextField(tabContent, "Version :") ;
		t.setEditable(false);
		t.setText( MyPlugin.getVersion() + " - " + PluginBuildInfo.BUILD_ID + "  ( " + PluginBuildInfo.BUILD_DATE + " ) ");
		
		t = createTextField(tabContent, "Id :") ;
		t.setEditable(false);
		t.setText( MyPlugin.getId() );
		
		t = createTextField(tabContent, "Directory URL :") ;
		t.setEditable(false);
		t.setText( MyPlugin.getBaseURLAsString() );
		
		t = createTextField(tabContent, "Directory :") ;
		t.setEditable(false);
		t.setText( MyPlugin.getDirectory() );
		
		t = createTextField(tabContent, "Resources dir :") ;
		t.setEditable(false);
		t.setText( MyPlugin.getResourcesDirectory() );
		
		t = createTextField(tabContent, "Generator version :") ;
		t.setEditable(false);
		t.setText( GeneratorVersion.GENERATOR_VERSION );

		t = createTextField(tabContent, "GitHub URL pattern :") ;
		t.setEditable(false);
		t.setText( GitHubClient.GIT_HUB_REPO_URL_PATTERN );
		
		t = createTextArea(tabContent, "Http proxy config :") ;
		t.setEditable(false);
		t.setText( HttpUtil.getSystemProxyPropertiesAsString("-----") );
	}	
	
	//------------------------------------------------------------------------------------------
	/**
	 * Creates the "About plugin" TabItem
	 * @param tabFolder
	 */
	private void createTabDebug(TabFolder tabFolder) 
	{
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(" Debug ");
		
		Composite tabContent = new Composite(tabFolder, SWT.NONE);
		tabContent.setLayout(new GridLayout(2, false));
		tabItem.setControl(tabContent);
		
		//-------------------------------------------------- ROW ------------------------------------
		//--- Creates the Label 
		Label label = new Label(tabContent, SWT.NONE);
		label.setText("Settings test 1 : ");

		new Label(tabContent, SWT.NONE).setText("xxx");
		
		//-------------------------------------------------- ROW ------------------------------------
		//--- Creates the Button 
		Button button = new Button(tabContent, SWT.PUSH);
		button.setText("isBundleStaticResourcesCopied");
//		button.setToolTipText(" Creates the Telosys Tools folders \n"
//				+ " and the databases configuration file \n"
//				+ " if they don't exist");
		button.addSelectionListener(new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent arg0)
            {
            	
            	SettingsManager settingsManager = new SettingsManager( getCurrentProject() ) ;
            	boolean r = settingsManager.readBundleStaticResourcesCopiedFlag("fakeBundle");
            	MsgBox.info("Result = " + r );
            }
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        }
		);
		
		button = new Button(tabContent, SWT.PUSH);
		button.setText("setBundleStaticResourcesCopied");
//		button.setToolTipText(" Creates the Telosys Tools folders \n"
//				+ " and the databases configuration file \n"
//				+ " if they don't exist");
		button.addSelectionListener(new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent arg0)
            {
            	
            	SettingsManager settingsManager = new SettingsManager( getCurrentProject() ) ;
            	settingsManager.updateBundleStaticResourcesCopiedFlag("fakeBundle", true);
            	MsgBox.info("Done (set to TRUE)");
            }
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        }
		);
	}	
	
	//------------------------------------------------------------------------------------------
	private void createTabFoldersButtons(Composite composite ){
		//--- Creates the Label 
		Label label = new Label(composite, SWT.NONE);
		label.setText("");
		Label label2 = new Label(composite, SWT.NONE);
		label2.setText("");
		
		//--- Creates the buttons
		Composite buttons = new Composite(composite, SWT.NONE);
		buttons.setLayout(new FillLayout());

		Button mavenButton = new Button(buttons, SWT.PUSH);
		mavenButton.setText("Maven project folders");
		mavenButton.addSelectionListener(new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent arg0)
            {
            	_tSrcFolder.setText("src/main/java");
            	_tResFolder.setText("src/main/resources");
            	_tWebFolder.setText("src/main/webapp");
            	_tTestSrcFolder.setText("src/test/java");
            	_tTestResFolder.setText("src/test/resources");
            }
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        }
		);

		Button projectButton = new Button(buttons, SWT.PUSH);
		projectButton.setText("Eclipse project folders");
		projectButton.addSelectionListener(new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent event)
            {
            	//Object source = event.getSource();
            	//MsgBox.info("source : " + source.getClass().getCanonicalName() );
            	String srcFolderName = getProjectSourceFolder() ;
            	if ( StrUtil.nullOrVoid(srcFolderName) ) {
            		srcFolderName = "src" ;
            	}
            	String srcWebContentFolderName = getProjectWebContentFolder(); 
            	if ( StrUtil.nullOrVoid(srcWebContentFolderName) ) {
            		srcWebContentFolderName = srcFolderName + "-webapp" ;
            	}
            	_tSrcFolder.setText( srcFolderName );
            	_tResFolder.setText( srcFolderName + "-resources" );
            	_tWebFolder.setText( srcWebContentFolderName );
            	_tTestSrcFolder.setText( srcFolderName + "-test-java" );
            	_tTestResFolder.setText( srcFolderName + "-test-resources" );
            }
            public void widgetDefaultSelected(SelectionEvent event)
            {
            }
        }
		);
	}
	//------------------------------------------------------------------------------------------
	/**
	 * Try to determine the project source folder  
	 * @return
	 */
	private String getProjectSourceFolder() {
		IProject project = getCurrentProject();
		if ( EclipseProjUtil.isJavaProject(project) != true ) {
			return "" ; // unknown
		}
		String[] srcFolders = EclipseProjUtil.getSrcFolders(project);
		String projectSourceFolder = null ;
		if ( srcFolders.length == 1 ) {
			projectSourceFolder = srcFolders[0] ;
		}
		else if ( srcFolders.length > 1 ) {
			String srcEquals = null ;
			String srcStartsWith = null ;
			String srcContains = null ;
    		for ( String srcFolder : srcFolders ) {
    			if ( "src".equals(srcFolder) ) { // "src" found 
    				srcEquals = srcFolder ; 
    			}
    			if ( srcFolder.startsWith("src") ) { // "src*" found 
    				if ( srcStartsWith == null ) {
        				srcStartsWith = srcFolder ; 
    				}
    			}
    			if ( srcFolder.contains("src") ) { // "*src*" found 
    				if ( srcContains == null ) {
    					srcContains = srcFolder ; 
    				}
    			}
    		}
    		if ( srcEquals != null ) { // 1rts strict equals "src"
    			projectSourceFolder = srcEquals ;
    		}
    		else if ( srcStartsWith != null ) { // 2nd starts with "src"
    			projectSourceFolder = srcStartsWith ;
    		}
    		else if ( srcContains != null ) {
    			projectSourceFolder = srcContains ;
    		}
    		else {
    			projectSourceFolder = srcFolders[0] ; // => use the first one 
    		}
//    		if ( null == projectSourceFolder ) { // "src" not found
//    			projectSourceFolder = srcFolders[0] ; // => use the first one 
//    		}
		}
		if ( null == projectSourceFolder ) { // still undefined 
			projectSourceFolder = "" ; // unknown
		}
		return projectSourceFolder ;
	}
	
	private String getProjectWebContentFolder() {
		IProject project = getCurrentProject();
		IFolder folder = project.getFolder(WEB_CONTENT);
		if ( folder.exists() ) {
			// Exists 
			return WEB_CONTENT ;
		}
		return "" ; // unknown
	}	
	//------------------------------------------------------------------------------------------
	private Text createTextField(Composite composite, String sLabel) {
		//--- Creates the Label 
		Label label = new Label(composite, SWT.NONE);
		label.setText(sLabel);
		//--- Creates the Text field 
		Text textField = new Text(composite, SWT.BORDER);
		textField.setLayoutData(getColSpan(2));
		return textField;
	}

	//------------------------------------------------------------------------------------------
	private Text createTextField(Composite composite, String label1, String label2) {
		//--- Creates the Label 1
		Label label = new Label(composite, SWT.NONE);
		label.setText(label1);
		//--- Creates the Label 2 
		label = new Label(composite, SWT.NONE);
		label.setText(label2);
		//--- Creates the Text field 
		Text textField = new Text(composite, SWT.BORDER);
		textField.setLayoutData(getColSpan(1));
		return textField;
	}

	//------------------------------------------------------------------------------------------
	private Text createTextArea(Composite composite, String sLabel) {
		//--- Creates the Label 
		Label label = new Label(composite, SWT.NONE);
		label.setText(sLabel);
		//--- Creates the Text area 
		Text textArea = new Text (composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL );
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.heightHint     = 100 ;
		textArea.setLayoutData(gd);
		return textArea;
	}

	//------------------------------------------------------------------------------------------
	private void createTabInfoDescLink(Composite composite, TabFolder tabFolder, String tabTitle, String tabDescription, int tabIndex) {
		
		//--- Col 1 : Link for tab selection  
		Link link = new Link(composite, SWT.NONE);
		link.setText(tabTitle);
		link.addSelectionListener( new LinkSelectionListener(tabFolder, tabIndex) );
		
		//--- Col 2 : Label for description
		new Label(composite, SWT.NONE).setText(tabDescription);
		
	}

	//------------------------------------------------------------------------------------------
	private Label createSingleLabel(Composite composite, String sLabel, int colSpan) {
		//--- Creates the Label 
		Label label = new Label(composite, SWT.NONE);
		label.setText(sLabel);
		label.setLayoutData(getColSpan(colSpan));
		return label;
	}

	//------------------------------------------------------------------------------------------
	private GridData getColSpan(int n) {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = n;
		return gd;
	}

	//------------------------------------------------------------------------------------------
	private IProject getCurrentProject() {
		IAdaptable adaptable = getElement() ;
		if ( adaptable == null ) {
			MsgBox.error("Cannot get Adaptable element " );
			return null ;
		}		
		return (IProject)adaptable;		
	}
	//------------------------------------------------------------------------------------------
	private String getCurrentWorkspaceAbsolutePath() {
		return EclipseProjUtil.getWorkspaceLocation(getCurrentProject()) ;
	}
	//------------------------------------------------------------------------------------------
	private EnvironmentManager getEnvironmentManager() 	{
		IProject currentProject = getCurrentProject();
		String projectFolderFullPath = EclipseProjUtil.getProjectDir( currentProject );
		return new EnvironmentManager( projectFolderFullPath );
	}
	//------------------------------------------------------------------------------------------
//	private ProjectConfig loadProjectConfig() 
	private TelosysToolsCfg loadProjectConfig() 
	{
		log("loadProjectConfig()...");
//		ProjectConfig projectConfig = ProjectConfigManager.loadProjectConfig( getCurrentProject() ) ;
//		return projectConfig ;
		return ProjectConfigManager.loadProjectConfig( getCurrentProject() ) ;
	}
	//------------------------------------------------------------------------------------------
//	private void saveProjectConfig(ProjectConfig projectConfig ) 
	private void saveProjectConfig(TelosysToolsCfg telosysToolsCfg ) // v 3.0.0
	{
		log("saveProjectConfig()...");
//		ProjectConfigManager.saveProjectConfig(getCurrentProject(), projectConfig);
		ProjectConfigManager.saveProjectConfig(getCurrentProject(), telosysToolsCfg); // v 3.0.0
	}
	
	//------------------------------------------------------------------------------------------
	/*
	 * overwritten method for "Apply" button
	 */
	@Override
	protected void performApply() 
	{
		try {
			//ProjectConfig projectConfig = new ProjectConfig(getCurrentProject());
//			ProjectConfig projectConfig = ProjectConfigManager.loadProjectConfig(getCurrentProject()); // v 3.0.0
			TelosysToolsCfg telosysToolsCfg = loadProjectConfig(); // v 3.0.0
			
//			fieldsToConfig( projectConfig.getTelosysToolsCfg() );
			fieldsToConfig( telosysToolsCfg ); // v 3.0.0
			
			//-- Save the Telosys-Tools configuration for the current project
//			saveProjectConfig( projectConfig.getTelosysToolsCfg() ); 
			saveProjectConfig( telosysToolsCfg );  // v 3.0.0
			
			configToFieldsInGeneralTab(telosysToolsCfg); // Refresh "General" Tab - v 3.0.0 
			
		} catch ( Exception e ) {
			MsgBox.error("Cannot save properties.", e );
		}
	}

	//------------------------------------------------------------------------------------------
	/*
	 * overwritten method for "Restore Defaults" button
	 */
	@Override
	protected void performDefaults() {
	}

	//------------------------------------------------------------------------------------------
	private void configToFieldsInGeneralTab( TelosysToolsCfg telosysToolsCfg  )  { // v 3.0.0
		IProject project = getCurrentProject();
//		_tProjectName.setText( projectConfig.getProjectName() );
		_tProjectName.setText( project.getName() ); // v 3.0.0
		
//		_tProjectLocation.setText(projectConfig.getProjectFolder() );
		_tProjectLocation.setText( telosysToolsCfg.getProjectAbsolutePath() ); // v 3.0.0
		
//		_tWorkspaceLocation.setText(projectConfig.getWorkspaceFolder() );
		String workspaceFolderFullPath = getCurrentWorkspaceAbsolutePath() ;
		_tWorkspaceLocation.setText( workspaceFolderFullPath ); // v 3.0.0
		
//		TelosysToolsEnv telosysToolsEnv = TelosysToolsEnv.getInstance();

		_tPluginConfigFile.setText( telosysToolsCfg.getCfgFileAbsolutePath() );

		// v 3.0.0 : "telosysToolsCfg" replaced by "telosysToolsEnv"
//		_tModelsFolder.setText( telosysToolsEnv.getModelsFolder() ) ; // telosysToolsCfg.getRepositoriesFolder() );
		_tModelsFolder.setText( telosysToolsCfg.getModelsFolderAbsolutePath() ) ;
//		_tDownloadsFolder.setText( telosysToolsEnv.getDownloadsFolder() );  // telosysToolsCfg.getDownloadsFolder() );
		_tDownloadsFolder.setText( telosysToolsCfg.getDownloadsFolderAbsolutePath() ); 
//		_tLibrariesFolder.setText( telosysToolsEnv.getLibrariesFolder() ) ; // telosysToolsCfg.getLibrariesFolder() );
		_tLibrariesFolder.setText( telosysToolsCfg.getLibrariesFolderAbsolutePath() ) ;

//		_tTemplatesFolder.setText( telosysToolsEnv.getTemplatesFolder() );  // telosysToolsCfg.getTemplatesFolder() );
		_tTemplatesFolder.setText( telosysToolsCfg.getTemplatesFolderAbsolutePath() );

		_tDestinationFolder.setText( telosysToolsCfg.getDestinationFolderAbsolutePath() ) ; 		
	}
	/**
	 * @param projectConfig
	 */
//	private void configToFields( ProjectConfig projectConfig  ) 
	private void configToFields( TelosysToolsCfg telosysToolsCfg  )  // v 3.0.0
	{
		log("propertiesToFields ...");
//		TelosysToolsCfg telosysToolsCfg = projectConfig.getTelosysToolsCfg();

		//--- Tab "Folders" ( considered as pre-defined variables )
		_tSrcFolder.setText( telosysToolsCfg.getSRC() ) ;
		_tResFolder.setText( telosysToolsCfg.getRES() ) ;
		_tWebFolder.setText( telosysToolsCfg.getWEB() ) ;
		_tTestSrcFolder.setText( telosysToolsCfg.getTEST_SRC() ) ;
		_tTestResFolder.setText( telosysToolsCfg.getTEST_RES() ) ;
		_tDocFolder.setText( telosysToolsCfg.getDOC() ) ;
		_tTmpFolder.setText( telosysToolsCfg.getTMP() ) ;

		//--- Tab "Packages"
		//_tEntityPackage.setText( projectConfig.getPackageForJavaBeans() );
		_tEntityPackage.setText( telosysToolsCfg.getEntityPackage() ); // v 2.0.6
		_tRootPackage.setText( telosysToolsCfg.getRootPackage() ); // v 2.0.6
		
		//--- Tab "Variables"
		Variable[] items = telosysToolsCfg.getSpecificVariables();
		if ( items != null ) {
			_variablesTable.initItems(items);
		}

		//--- Tab "General"
//		IProject project = getCurrentProject();
////		_tProjectName.setText( projectConfig.getProjectName() );
//		_tProjectName.setText( project.getName() ); // v 3.0.0
//		
////		_tProjectLocation.setText(projectConfig.getProjectFolder() );
//		_tProjectLocation.setText( telosysToolsCfg.getProjectAbsolutePath() ); // v 3.0.0
//		
////		_tWorkspaceLocation.setText(projectConfig.getWorkspaceFolder() );
//		String workspaceFolderFullPath = getCurrentWorkspaceAbsolutePath() ;
//		_tWorkspaceLocation.setText( workspaceFolderFullPath ); // v 3.0.0
//		
////		TelosysToolsEnv telosysToolsEnv = TelosysToolsEnv.getInstance();
//
//		_tPluginConfigFile.setText( telosysToolsCfg.getCfgFileAbsolutePath() );
//
//		// v 3.0.0 : "telosysToolsCfg" replaced by "telosysToolsEnv"
////		_tModelsFolder.setText( telosysToolsEnv.getModelsFolder() ) ; // telosysToolsCfg.getRepositoriesFolder() );
//		_tModelsFolder.setText( telosysToolsCfg.getModelsFolderAbsolutePath() ) ;
////		_tTemplatesFolder.setText( telosysToolsEnv.getTemplatesFolder() );  // telosysToolsCfg.getTemplatesFolder() );
//		_tTemplatesFolder.setText( telosysToolsCfg.getTemplatesFolderAbsolutePath() );
////		_tDownloadsFolder.setText( telosysToolsEnv.getDownloadsFolder() );  // telosysToolsCfg.getDownloadsFolder() );
//		_tDownloadsFolder.setText( telosysToolsCfg.getDownloadsFolderAbsolutePath() ); 
////		_tLibrariesFolder.setText( telosysToolsEnv.getLibrariesFolder() ) ; // telosysToolsCfg.getLibrariesFolder() );
//		_tLibrariesFolder.setText( telosysToolsCfg.getLibrariesFolderAbsolutePath() ) ;
//
//		_tDestinationFolder.setText( telosysToolsCfg.getDestinationFolderAbsolutePath() ) ; 
//		
		configToFieldsInGeneralTab(telosysToolsCfg); 
		
		//--- Tab "Advanced" ( v 3.0.0 )
		if ( telosysToolsCfg.hasSpecificTemplatesFolders() ) {
			String workspaceFolderFullPath = getCurrentWorkspaceAbsolutePath() ;
			String specificFolder = telosysToolsCfg.getSpecificTemplatesFolderAbsolutePath() ;
			_tSpecificTemplatesFolder.setText(specificFolder);
			if ( workspaceFolderFullPath.equals( specificFolder ) ) {
				_rbTemplatesFolderWorkspace.setSelection(true);
			}
			else {
				_rbTemplatesFolderFilesystem.setData(specificFolder);
				_rbTemplatesFolderFilesystem.setSelection(true);
				_linkTemplatesFolderBrowse.setEnabled(true);
			}
		}
		else {
			_rbTemplatesFolderStandard.setSelection(true);
			_tSpecificTemplatesFolder.setText("");
		}

		if ( telosysToolsCfg.hasSpecificDestinationFolder() ) {
			String specificFolder = telosysToolsCfg.getSpecificDestinationFolder() ;
			_tSpecificDestinationFolder.setText(specificFolder);
			_rbDestinationFolderFilesystem.setData(specificFolder);
			_rbDestinationFolderFilesystem.setSelection(true);
			_linkDestinationFolderBrowse.setEnabled(true);
		}
		else {
			_tSpecificDestinationFolder.setText("");
			_rbDestinationFolderStandard.setSelection(true);
		}
	}
	
	//------------------------------------------------------------------------------------------
	/**
	 * Populates the given properties with screen fields values
	 * @param props
	 */
//	private void fieldsToConfig( ProjectConfig projectConfig ) {
	private void fieldsToConfig( TelosysToolsCfg telosysToolsCfg ) { // v 3.0.0
		log("fieldsToConfig ...");
//		TelosysToolsCfg telosysToolsCfg = projectConfig.getTelosysToolsCfg();
		
		//--- Tab "Folders" ( considered as pre-defined variables )
		telosysToolsCfg.setSRC     ( _tSrcFolder.getText()     );
		telosysToolsCfg.setRES     ( _tResFolder.getText()     );
		telosysToolsCfg.setWEB     ( _tWebFolder.getText()     );
		telosysToolsCfg.setTEST_SRC( _tTestSrcFolder.getText() );
		telosysToolsCfg.setTEST_RES( _tTestResFolder.getText() );
		telosysToolsCfg.setDOC     ( _tDocFolder.getText()     );
		telosysToolsCfg.setTMP     ( _tTmpFolder.getText()     );
		
		//--- Tab "Packages"
		telosysToolsCfg.setRootPackage   (_tRootPackage.getText()   );
		telosysToolsCfg.setEntityPackage (_tEntityPackage.getText() );

		//--- Tab "Variables"		
		log("fieldsToConfig : variables ...");
		//Variable[] variables = getVariablesFromView();
		java.util.List<Variable> variables = getVariablesFromView();
		if ( checkVariablesNames(variables) ) {
			telosysToolsCfg.setSpecificVariables(variables);
			log("fieldsToConfig : " + variables.size() + " variable(s) set in TelosysToolsCfg");
		}

		//--- Tab "Advanced" ( v 3.0.0 )
		if ( _rbTemplatesFolderStandard.getSelection() == true ) {
			// "Standard location" selected => no specific location
			telosysToolsCfg.setSpecificTemplatesFolderAbsolutePath("");
		}
		else {
			// Workspace or specific folder in filesystem selected => keep specific location
			telosysToolsCfg.setSpecificTemplatesFolderAbsolutePath( _tSpecificTemplatesFolder.getText() );
		}
		if ( _rbDestinationFolderStandard.getSelection() == true ) {
			// "Standard location" selected => no specific location
			telosysToolsCfg.setSpecificDestinationFolder("");
		}
		else {
			// Specific folder in filesystem selected => keep specific location
			telosysToolsCfg.setSpecificDestinationFolder( _tSpecificDestinationFolder.getText() );
		}
		
		//--- Tab "General" & "About" : nothing to do

		log("fieldsToConfig : END");
	}
	//------------------------------------------------------------------------------------------
	private TelosysToolsCfg getTelosysToolsCfgFromFields() {
		//ProjectConfig projectConfig = new ProjectConfig(getCurrentProject());
		//ProjectConfig projectConfig = ProjectConfigManager.loadProjectConfig( getCurrentProject() ) ; // v 3.0.0
//		TelosysToolsCfg telosysToolsCfg = ProjectConfigManager.loadProjectConfig( getCurrentProject() ); // v 3.0.0		
		TelosysToolsCfg telosysToolsCfg = loadProjectConfig(); // v 3.0.0		

		//fieldsToConfig( projectConfig.getTelosysToolsCfg() );
		fieldsToConfig( telosysToolsCfg );
		//return projectConfig.getTelosysToolsCfg();
		return telosysToolsCfg;
	}	
	//------------------------------------------------------------------------------------------
	private java.util.List<Variable> getVariablesFromView() {
		Object[] items = _variablesTable.getItems();
		LinkedList<Variable> variables = new LinkedList<Variable>();
		for ( Object item : items ) {
			if ( item instanceof Variable ) {
				variables.add((Variable) item);
			}
			else {
				MsgBox.error("Item from variables list is not an instance of 'Variable'" );
			}
		}
		return variables ;
	}
	//------------------------------------------------------------------------------------------
	private boolean checkVariablesNames(java.util.List<Variable> variables) { // v 3.0.0
		//-- are there invalid names ? 
		java.util.List<String> invalidNames = ContextNames.getInvalidVariableNames(variables); // v 3.0.0
		if ( invalidNames != null ) {
			//--- Invalid names found => display all the invalid names
			StringBuffer sb = new StringBuffer();
			for ( String variableName : invalidNames ) { // v 3.0.0
				sb.append(" '"+variableName+"' ");
			}
			MsgBox.error("Invalid variable name(s) : " + sb.toString() 
					+ "\n These name(s) are reserved for 'standard variables'."
					+ "\n The current variables will not be saved !");
			return false ;
		}
		return true ;
	}
	
}