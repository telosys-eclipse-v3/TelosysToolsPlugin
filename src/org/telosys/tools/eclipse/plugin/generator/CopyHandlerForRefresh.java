package org.telosys.tools.eclipse.plugin.generator;

import java.io.File;

import org.telosys.tools.commons.io.CopyHandler;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;

public class CopyHandlerForRefresh implements CopyHandler {

	@Override
	public void beforeCopy(File origin, File destination) {
		// Nothing to do here
	}

	@Override
	public void afterCopy(File origin, File destination) {
		// Refresh the Eclipse Workspace 
		EclipseWksUtil.refresh( destination );		
	}

}
