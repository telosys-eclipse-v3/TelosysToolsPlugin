package org.telosys.tools.eclipse.plugin.wkschanges;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;

/*
 * Eclipse Workspace resources change listeners 
 * See :
 * https://eclipse.org/articles/Article-Resource-deltas/resource-deltas.html 
 * http://help.eclipse.org/mars/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Fguide%2FresAdv_events.htm 
 * http://stackoverflow.com/questions/27244316/how-to-add-iresourcechangelistener-in-eclipse-plugin
 * 
 */

public class WorkspaceChanges {

	public static void startTracking() {
		
//		IResourceChangeListener listener = new ResourceChangeListener();
		
//		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener,
//			  IResourceChangeEvent.PRE_CLOSE
//			| IResourceChangeEvent.PRE_DELETE
//			| IResourceChangeEvent.PRE_BUILD
//			| IResourceChangeEvent.POST_BUILD
//			| IResourceChangeEvent.POST_CHANGE);		

//		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener, IResourceChangeEvent.POST_CHANGE);
		
//		ResourcesPlugin.getWorkspace().addResourceChangeListener(new EntityChangeListener(), IResourceChangeEvent.POST_CHANGE);
//		ResourcesPlugin.getWorkspace().addResourceChangeListener(new ModelChangeListener(), IResourceChangeEvent.POST_CHANGE);
		
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new FileChangeListener(), IResourceChangeEvent.POST_CHANGE);
		
	}
}
