package org.telosys.tools.eclipse.plugin.wkschanges.deco;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.telosys.tools.eclipse.plugin.commons.PluginImages;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

//@SuppressWarnings("restriction")
//public class FileDecorator extends LabelProvider implements ILightweightLabelDecorator  {
public class FileDecorator extends LabelProvider implements ILabelDecorator  {

	
	public Image decorateImage(Image image, Object element) {
		/*
		 * image   : the input image to decorate, or null if the element has no image
		 *           Here never null ( 1rts time contains the standard "folder" image
		 * element : the element whose image is being decorated
		 */
		PluginLogger.debug("decorateImage(image, element) : element class " + element.getClass() ) ;
		if ( hasErrorMarker(element) ) {
			return getTelosysToolsFolderImage() ;
		}
//		//Image telosysFolderImage = PluginImages.getImage(PluginImages.TELOSYS_FOLDER);
//		if (element instanceof Folder) {
//
//			Folder folder = (Folder) element;
//
//			// Apply decorator for "TelosysTools" (ignore case) or any "telosys*tools"
//			final String folderNameLowerCase = folder.getName().toLowerCase() ;
//			if ( "telosystools".equalsIgnoreCase( folderNameLowerCase )  ) {
//				return getTelosysToolsFolderImage() ;
//			}
//			else {
//				if ( folderNameLowerCase.startsWith("telosys") 
//						&& folderNameLowerCase.endsWith("tools") ) {
//					return getTelosysToolsFolderImage() ;
//				}
//			}
//		}
		return null; // null means "no decoration is to be applied"
	}
	
	public Image getTelosysToolsFolderImage() {
		return PluginImages.getImage(PluginImages.TELOSYS_FOLDER);
		
	}
	
//	Returns a text label that is based on the given text label, 
//	but decorated with additional information relating to the state of the provided element. 
//	Text and image decoration updates can occur as a result of other updates within the workbench 
//	including deferred decoration by background processes. 
//	Clients should handle labelProviderChangedEvents for the given element to get the complete decoration.
	@Override
	public String decorateText(String text, Object element) {
		PluginLogger.debug("decorateText('" + text + "', element) : element class " + element.getClass() ) ;
		if ( hasErrorMarker(element) ) {
			
			return "( ! )" + text ;
		}
		return text ;
	}
	
//	public void decorate(Object object, IDecoration decoration) {
//		PluginLogger.debug("decorate()... " );
//
//		if ( object instanceof IResource ) {
//			IResource resource = (IResource) object ;
//			PluginLogger.debug("decorate() : resource = " + resource.getName() );
//			if ( resource.exists() ) {
//				IMarker errorMarker = FileMarker.findErrorMarker(resource);
//				if ( errorMarker != null ) {
//					// Error marker found => set the error decoration
//					PluginLogger.debug("decorate() : error marker found" );
////					decoration.addOverlay(ImageDescriptor.createFromFile(FileDecorator.class, ICON), IDecoration.TOP_RIGHT);
//					decoration.addPrefix("<!> ");
//					decoration.addSuffix(" (error)");
////					decoration.setFont(font);
////					decoration.setForegroundColor(color);
//				}
//			}
//			else {
//				PluginLogger.debug("decorate() : the resource doesn't exist" );
//			}
//		}
//	}
	
	private boolean hasErrorMarker(Object object) {
		PluginLogger.debug("hasErrorMarker(Object object) " ) ;
		if ( object != null ) {
			PluginLogger.debug("hasErrorMarker(Object object) : object is not null" ) ;
			if ( object instanceof IResource ) {
				IResource resource = (IResource) object ;
				PluginLogger.debug("hasErrorMarker(Object object) : object is a IResource, name = " + resource.getName()) ;
				if ( resource.exists() ) {
					IMarker errorMarker = FileMarker.findErrorMarker(resource);
					if ( errorMarker != null ) {
						PluginLogger.debug("hasErrorMarker(Object object) : IResource has an error marker" ) ;	
						return true;
					}
					else {
						PluginLogger.debug("hasErrorMarker(Object object) : IResource doesn't have an error marker" ) ;											
					}
				}
				else {
					PluginLogger.debug("hasErrorMarker(Object object) : IResource doesn't exist" ) ;					
				}
			}
			else {
				PluginLogger.debug("hasErrorMarker(Object object) : object is NOT a IResource" ) ;
			}
		}
		else {
			PluginLogger.debug("hasErrorMarker(Object object) : object is NULL" ) ;
		}
		return false;
	}
}
