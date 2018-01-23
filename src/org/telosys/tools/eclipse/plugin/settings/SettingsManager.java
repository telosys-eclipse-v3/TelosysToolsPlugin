package org.telosys.tools.eclipse.plugin.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.telosys.tools.eclipse.plugin.commons.EclipseProjUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;

/**
 * @author Laurent GUERIN
 * 
 * Class to load and save properties 
 */

public class SettingsManager {

	private final static String ERR_CANNOT_LOAD = "Cannot load properties.\n" ;
	
	private final static String ERR_CANNOT_SAVE = "Cannot save properties.\n" ;

	private final static String SETTINGS_FILE = ".settings/org.telosys.tools.settings" ;
	private final static String BUNDLE = "bundle" ;
	
	private final IProject _project ;
	
	/**
	 * Constructor
	 * @param project
	 */
	public SettingsManager(IProject project) 
	{
		super();
		if ( project == null ) {
			MsgBox.error("SettingsManager constructor error : IProject is null !");
		}
		_project = project ;
	}
	
	//=======================================================================================================
	// Bundle Static Resources Copied or Not 
	//=======================================================================================================
	
	private String getBundleStaticResourcesCopiedKey(String bundleName){
		return BUNDLE + "." + bundleName + ".staticResourcesCopied";
	}
	
	/**
	 * Updates the flag about static resources (copied or not) for the given bundle name 
	 * @param bundleName
	 * @param value
	 */
	public void updateBundleStaticResourcesCopiedFlag(String bundleName, boolean value){
		updateProperty(getBundleStaticResourcesCopiedKey(bundleName), ""+value);
	}

	/**
	 * Reads the flag about static resources (copied or not) for the given bundle name 
	 * @param bundleName
	 * @return
	 */
	public boolean readBundleStaticResourcesCopiedFlag(String bundleName ){
		String value = loadProperty(getBundleStaticResourcesCopiedKey(bundleName) );
		if ( value != null ) {
			return value.equalsIgnoreCase("true");
		}
		return false ;
	}

	//=======================================================================================================
	/**
	 * Updates the given property value in the file
	 * @param key
	 * @param value
	 */
	private void updateProperty(String key, String value){
		Properties properties = load() ; 
		properties.setProperty(key, value);
		save(properties);
	}

	/**
	 * Reads the given property from the file
	 * @param key
	 * @return
	 */
	private String loadProperty(String key){
		Properties properties = load() ; 
		return properties.getProperty(key);
	}
	
	/**
	 * Loads all the properties from the 'settings' file
	 * @return
	 */
	private Properties load() 
	{
		String filePath = EclipseProjUtil.getAbsolutePathInFileSystem(_project, SETTINGS_FILE);
		File file = new File(filePath);
		return load(file);
	}
	
	/**
	 * Save the given properties in the 'settings' file
	 * @param properties
	 */
	private void save(Properties properties) 
	{
		String filePath = EclipseProjUtil.getAbsolutePathInFileSystem(_project, SETTINGS_FILE);
		File file = new File(filePath);
		save(file, properties);
	}
	
	/**
	 * Loads the properties from the given file 
	 * @param file
	 * @return
	 */
	private Properties load( File file ) 
	{
		Properties properties = new Properties();
		
		//--- If the file doesn't exist ... return null (it's not an error)
		if ( file.exists() != true ) {
			return properties ;
		}

		//--- The file exists => load it !  
		FileInputStream fis = null ;
		try {
			fis = new FileInputStream(file);
			properties.load(fis);
		} catch (IOException ioe) {
			MsgBox.error(ERR_CANNOT_LOAD + "IOException : \n", ioe );
		}
		finally
		{
			try {
				if ( fis != null )
				{
					fis.close();
				}
			} catch (IOException e) {
				// NOTHING TO DO
			}
		}
		return properties;
	}

		
	/**
	 * Saves the given properties in the given file
	 * @param file
	 * @param properties
	 */
	private void save(File file, Properties properties) 
	{
		
		if ( file.exists() != true ) {
			File dir = file.getParentFile() ;
			if ( dir.exists() != true ) {
				boolean r = dir.mkdir(); // create only the first level ( ".settings" )
				if ( r != true ) {
					MsgBox.error("Cannot create directory '" + dir.getPath() + "'");
				}
			}
		}
			
		FileOutputStream fos = null ;
		try {
			fos = new FileOutputStream(file);
			properties.store(fos, "Telosys plugin properties");
		} catch (IOException ioe) {
			MsgBox.error(ERR_CANNOT_SAVE + "IOException : \n" + ioe.getMessage() );
		}
		finally
		{
			try {
				if ( fos != null )
				{
					fos.close();
				}
			} catch (IOException e) {
				// NOTHING TO DO 
			}
		}
	}

}