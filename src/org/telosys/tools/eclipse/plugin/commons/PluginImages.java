package org.telosys.tools.eclipse.plugin.commons;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.telosys.tools.eclipse.plugin.MyPlugin;

public class PluginImages {

	// images keys
	public static final String TELOSYS_LOGO  = "TELOSYS_LOGO" ; 
	public static final String SAMPLE        = "SAMPLE" ; 
	
	public static final String PRIMARYKEY    = "PRIMARYKEY" ; 

	public static final String PRIMARYKEY_AUTOINCR  = "PRIMARYKEY_AUTOINCR" ;  // v 1.0
	public static final String PRIMARYKEY_FK        = "PRIMARYKEY_FK" ;  // v 1.0
	
	public static final String FOREIGNKEY    = "FOREIGNKEY" ; // v 1.0 
	public static final String JOINTABLE     = "JOINTABLE" ; // v 1.0 
	
	public static final String ARROW_RIGHT   = "ARROW_RIGHT" ; // v 1.0 
	public static final String ARROW_LEFT    = "ARROW_LEFT" ; // v 1.0 
	
	public static final String NOTNULL_ON    = "NOTNULL_ON" ; 
	public static final String NOTNULL_OFF   = "NOTNULL_OFF" ; 
	
	public static final String LINK_MANY_TO_ONE   = "LINK_MANY_TO_ONE" ; 
	
	public static final String NEW_DATABASE    = "NEW_DATABASE" ; // v 2.0.6
	public static final String DELETE_DATABASE = "DELETE_DATABASE" ; // v 2.0.6
	public static final String GENERATE_REPO   = "GENERATE_REPO" ; // v 2.0.6
	public static final String UPDATE_REPO     = "UPDATE_REPO" ; // v 2.0.6
	
	public static final String EDIT_ICON      = "EDIT_ICON" ; // v 2.0
	public static final String GENERATE       = "GENERATE" ; // v 2.0
	public static final String REFRESH        = "REFRESH" ; // v 2.0
	public static final String TARGETS        = "TARGETS" ; // v 2.0
	public static final String SWITCH_BUNDLE  = "SWITCH_BUNDLE" ; // v 2.0.7

	public static final String CHECKED_ON     = "CHECKED_ON" ; // v 2.0
	public static final String CHECKED_OFF    = "CHECKED_OFF" ; // v 2.0

	public static final String FILE1    = "FILE1" ; // v 2.0.7
	public static final String FILES    = "FILES" ; // v 2.0.7
	
	public static final String TELOSYS_FOLDER  = "TELOSYS_FOLDER" ; // v 2.0.7 (folder decorator)
	public static final String ENTITY_FILE     = "ENTITY_FILE" ; 

	public static final String ERROR     = "ERROR" ; 
	public static final String WARNING   = "WARNING" ; // v 3.0.0 (warning in entities list)

	// Images for Velocity Templates Editor  ( v 2.0.5 )
	public static final String VELOCITY_BEAN_GENERATOR = "VELOCITY_BEAN_GENERATOR";
	public static final String VELOCITY_BEAN_METHOD = "VELOCITY_BEAN_METHOD";
	public static final String VELOCITY_BEAN_PROPERTY = "VELOCITY_BEAN_PROPERTY";
	public static final String VELOCITY_DIRECTIVE = "VELOCITY_DIRECTIVE";
	public static final String VELOCITY_PREDEF_VARIABLE = "VELOCITY_PREDEF_VARIABLE";
	public static final String VELOCITY_PREDEFNAMES = "VELOCITY_PREDEFNAMES";
	
	// images registry
	private static ImageRegistry $imageRegistry = new ImageRegistry() ;

    //------------------------------------------------------------------------------------------------
	/**
	 * Register the given image with the given key
	 * @param sImgKey
	 * @param sImgFile
	 * @return
	 */
	private static ImageDescriptor registerImage(String sImgKey, String sImgFile) 
	{
		URL url = MyPlugin.getImageURL(sImgFile);
		if ( url != null )
		{
			$imageRegistry.put(sImgKey, ImageDescriptor.createFromURL(url) );			
		}
		return ImageDescriptor.createFromURL(url);
	}
	
    //------------------------------------------------------------------------------------------------
	// Init the registry
	static {
		registerImage(TELOSYS_LOGO, Const.TELOSYS_IMAGE ) ;
		//registerImage(SAMPLE, Const.SAMPLE_IMAGE ) ;
		registerImage(PRIMARYKEY,  Const.PRIMARYKEY_IMAGE ) ;
		
		registerImage(PRIMARYKEY_AUTOINCR,  Const.PRIMARYKEY_AUTOINCR_IMAGE ) ;  // v 1.0
		registerImage(PRIMARYKEY_FK,        Const.PRIMARYKEY_FK_IMAGE ) ;  // v 1.0

		registerImage(FOREIGNKEY,  Const.FOREIGNKEY_IMAGE ) ;  // v 1.0
		registerImage(JOINTABLE,   Const.JOINTABLE_IMAGE ) ;  // v 1.0

		registerImage(ARROW_RIGHT,  Const.ARROW_RIGHT_IMAGE ) ;  // v 1.0
		registerImage(ARROW_LEFT,   Const.ARROW_LEFT_IMAGE ) ;  // v 1.0
		
		registerImage(NOTNULL_ON,  Const.NOTNULL_ON_IMAGE ) ;
		registerImage(NOTNULL_OFF, Const.NOTNULL_OFF_IMAGE ) ;
		
		registerImage(LINK_MANY_TO_ONE, Const.LINK_MANY_TO_ONE_IMAGE );

		registerImage(NEW_DATABASE,    Const.NEW_DATABASE );  // v 2.0.6 
		registerImage(DELETE_DATABASE, Const.DELETE_DATABASE ); // v 2.0.6
		registerImage(GENERATE_REPO,   Const.GENERATE_REPO );  // v 2.0.6 
		registerImage(UPDATE_REPO,     Const.UPDATE_REPO );  // v 2.0.6 
		
		registerImage(EDIT_ICON,     Const.EDIT_ICON );
		registerImage(GENERATE,      Const.GENERATE );
		registerImage(REFRESH,       Const.REFRESH );
		registerImage(TARGETS,       Const.TARGETS );
		registerImage(SWITCH_BUNDLE, Const.SWITCH_BUNDLE );
		registerImage(CHECKED_OFF,   Const.CHECKED_OFF );
		registerImage(CHECKED_ON,    Const.CHECKED_ON );

		registerImage(FILE1,    Const.FILE1 );
		registerImage(FILES,    Const.FILES );
		
		// Velocity Templates Editor ( v 2.0.5 )
		registerImage(VELOCITY_BEAN_GENERATOR,   Const.VELOCITY_BEAN_GENERATOR_IMAGE );
		registerImage(VELOCITY_BEAN_METHOD,      Const.VELOCITY_BEAN_METHOD_IMAGE );
		registerImage(VELOCITY_BEAN_PROPERTY,    Const.VELOCITY_BEAN_PROPERTY_IMAGE );
		registerImage(VELOCITY_DIRECTIVE,        Const.VELOCITY_DIRECTIVE_IMAGE );
		registerImage(VELOCITY_PREDEF_VARIABLE,  Const.VELOCITY_PREDEF_VARIABLE_IMAGE);
		registerImage(VELOCITY_PREDEFNAMES,      Const.VELOCITY_PREDEFNAMES_IMAGE);
		
		registerImage(TELOSYS_FOLDER, Const.TELOSYS_FOLDER ) ;

		registerImage(ENTITY_FILE, "entity_16pix.png" ) ; // v 3.0.0
		registerImage(ERROR,       "error_16pix.png" ) ; // v 3.0.0
		registerImage(WARNING,     "warning_16pix.png" ) ; // v 3.0.0
	}

//    //------------------------------------------------------------------------------------------------
//	/**
//	 * Returns the image descriptor registered with the given key
//	 * @param sImgKey
//	 * @return
//	 */
//	public static ImageDescriptor getImageDescriptor(String sImgKey) 
//	{
//		return $imageRegistry.getDescriptor(sImgKey);
//	}

    //------------------------------------------------------------------------------------------------
	/**
	 * Returns the image registered with the given key
	 * @param sImgKey
	 * @return
	 */
	public static Image getImage(String sImgKey) 
	{
		/*
		 * There are two ways to get an Image from an ImageDescriptor. 
		 * The method createImage will always return a new Image which must be disposed by the caller. 
		 * Alternatively, createResource() returns a shared Image. When the caller is done with an image obtained 
		 * from createResource, they must call destroyResource() rather than disposing the Image directly. 
		 * The result of createResource() can be safely cast to an Image.
		 */
//		PluginLogger.log("--- GET IMAGE --- " + sImgKey );
		// The image is created by the registry if necessary 
		Image image = $imageRegistry.get(sImgKey);
		
//		if ( image == null ) {
//			//--- Image  not yet defined => creation 
//			ImageDescriptor imgDesc = getImageDescriptor(sImgKey) ;
//			if ( imgDesc != null )
//			{
//				PluginLogger.log("--- CREATE IMAGE --- " + sImgKey );
//				image = imgDesc.createImage();
//				// Keep the image for next time
//				$imageRegistry.put(sImgKey, image);
//			}
//			else
//			{
//				MsgBox.error("Cannot get image descriptor for key '" + sImgKey + "'");
//				image = null ;
//			}
//		}
		return image ;
	}

}
