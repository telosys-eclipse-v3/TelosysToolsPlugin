package org.telosys.tools.eclipse.plugin.wkschanges.deco;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

public class FileMarker {

	private final static String MARKER_ID = "com.ibm.mymarkers.mymarker" ;
	
	public final static void setErrorMarker(IResource resource) {
		
		PluginLogger.debug("setErrorMarker " + resource.getName() );
		if ( findErrorMarker(resource) != null ) {
			// The resource is already marked
			PluginLogger.debug("setErrorMarker " + resource.getName() + " already marked");
		}
		else {
			PluginLogger.debug("setErrorMarker " + resource.getName() + " not yet marked => create marker");
			IMarker marker = null;
			try {
				//note: you use the id that is defined in your plugin.xml
				marker = resource.createMarker(MARKER_ID);
				
				//res.createMarker(IMarker.SEVERITY_ERROR);
				
				marker.setAttribute("description", "this is one of my markers");
				
				//note: you can also use attributes from your supertype
				marker.setAttribute(IMarker.MESSAGE, "Error");
				
				PluginLogger.debug("setErrorMarker " + resource.getName() + " marked / ERROR");
				
			} catch (CoreException e) {
				// The dialog should be created in UI thread
				// MsgBox.error("ERROR / createErrorMarker ", "File " + res.getName() + "\n" + e.getMessage());
				throw new RuntimeException("ERROR : setErrorMarker CoreException", e);
			}
//			return marker;		
		}
	}
	
	public final static void removeErrorMarker(IResource resource) {
		PluginLogger.debug("removeErrorMarker " + resource.getName() );

		IMarker[] markers = null ;
		try {
			markers = resource.findMarkers(MARKER_ID, true, IResource.DEPTH_ZERO);
		} catch (CoreException e) {
			throw new RuntimeException("ERROR : removeErrorMarker / resource.findMarkers", e);
		}
		if ( markers != null ) {
			for ( IMarker m : markers ) {
				try {
					m.delete();
				} catch (CoreException e) {
					throw new RuntimeException("ERROR : removeErrorMarker / marker.delete", e);
				}
			}
		}
	}
	
	public final static IMarker findErrorMarker(IResource resource) {
		
		PluginLogger.debug("findErrorMarker " + resource.getName() );

		IMarker[] markers = null ;
		try {
			markers = resource.findMarkers(MARKER_ID, true, IResource.DEPTH_ZERO);
		} catch (CoreException e) {
			throw new RuntimeException("ERROR : findErrorMarker / resource.findMarkers", e);
		}
		if ( markers == null ) {
			PluginLogger.debug("findErrorMarker markers == null " );
			return null ;
		}
		else if ( markers.length == 0 ) {
			PluginLogger.debug("findErrorMarker markers length == 0 " );
			return null ;
		}
		else if ( markers.length == 1 ) {
			PluginLogger.debug("findErrorMarker markers length == 1 " );
			return markers[0] ;
		}
		else {
			// Not supposed to happen 
			PluginLogger.debug("findErrorMarker markers length > 1 !!! (?)" );
			// The dialog should be created in UI thread
			// MsgBox.error("ERROR / findErrorMarker", markers.length + " markers found !" ) ;
			return markers[0] ; // Returns the 1rst marker found 
		}
	}	
}
