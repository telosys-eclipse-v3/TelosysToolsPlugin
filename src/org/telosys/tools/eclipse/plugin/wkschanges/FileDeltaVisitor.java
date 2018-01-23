package org.telosys.tools.eclipse.plugin.wkschanges;

import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

public class FileDeltaVisitor implements IResourceDeltaVisitor {

	private static void log(String msg) {
		if ( _PackageLoggerConfig.LOG ) {
			PluginLogger.log(FileDeltaVisitor.class, msg);
		}
	}
	
	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		log("visit(delta)");
		if ( isModelFile(delta) ) {
	        ModelChangesProcessor.processModelChange(delta);
	        return false; // IFile => no children
		}
		else if ( isEntityFile(delta) ) {
	        EntityChangesProcessor.processEntityChange(delta);
	        return false; // IFile => no children
		}
        return true; 
	}
	
	private boolean isModelFile(IResourceDelta delta) {
//		log("isModelFile(IResourceDelta delta) : delta = " + delta.getFullPath() );
//		File file = getFile(delta);
//        if ( file != null ) {
//    		// If this file match the standard model name and location, eg : "TelosysTools/xxxx.model"
//    		if ( DslModelUtil.isValidModelFile(file, true) ) {
//                return true ; // It's a model file 
//    		}
//        }
//        return false; // Not a model file 
		boolean result = false ;
		String fullPath = delta.getFullPath().toString();
		log("isModelFile(IResourceDelta delta) : fullPath = " + fullPath );
		if ( fullPath != null ) {
			if ( fullPath.endsWith(".model") ) {
				result = true ;
			}
		}
		log("isModelFile(IResourceDelta delta) : " + result );
        return result; 
	}

	private boolean isEntityFile(IResourceDelta delta) {
		boolean result = false ;
//		File file = getFile(delta);
//        if ( file != null ) {
//    		// If this file match the standard name and location, eg : "TelosysTools/xxxx_model/xxx.entity"
//    		if ( DslModelUtil.isValidEntityFile(file, true) ) {
//                return true ; // It's a model file 
//    		}
//        }
		String fullPath = delta.getFullPath().toString();
		log("isEntityFile(IResourceDelta delta) : fullPath = " + fullPath );
		if ( fullPath != null ) {
			if ( fullPath.endsWith(".entity") ) {
				result = true ;
			}
		}
		log("isEntityFile(IResourceDelta delta) : " + result );
        return result; 
	}

//	private File getFile(IResourceDelta delta) {
//		IResource resource = delta.getResource() ;
//        if ( resource instanceof IFile ) {
//    		IFile iFile = (IFile) resource ;
//    		if ( iFile.getLocationURI() != null ) {
//    			// NB : "null pointer" if getLocationURI() is null 
//	    		return EclipseWksUtil.toFile(iFile);
//    		}
//    		else {
//    			// No location URI for this resource occurs when a project is deleted ( "Delete project" )
//    			return null ;
//    		}
//        }	
//        return null; // Not a file 
//	}
}
