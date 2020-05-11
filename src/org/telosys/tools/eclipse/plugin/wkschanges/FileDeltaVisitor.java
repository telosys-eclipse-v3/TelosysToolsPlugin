package org.telosys.tools.eclipse.plugin.wkschanges;

import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.telosys.tools.eclipse.plugin.commons.Const;
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
		boolean result = false ;
		String fullPath = delta.getFullPath().toString();
		log("isModelFile(IResourceDelta delta) : fullPath = " + fullPath );
		if ( fullPath != null ) {
			if ( fullPath.endsWith(Const.DOT_MODEL) ) {
				result = true ;
			}
		}
		log("isModelFile(IResourceDelta delta) : " + result );
        return result; 
	}

	private boolean isEntityFile(IResourceDelta delta) {
		boolean result = false ;
		String fullPath = delta.getFullPath().toString();
		log("isEntityFile(IResourceDelta delta) : fullPath = " + fullPath );
		if ( fullPath != null ) {
			if ( fullPath.endsWith(Const.DOT_ENTITY) ) {
				result = true ;
			}
		}
		log("isEntityFile(IResourceDelta delta) : " + result );
        return result; 
	}

}
