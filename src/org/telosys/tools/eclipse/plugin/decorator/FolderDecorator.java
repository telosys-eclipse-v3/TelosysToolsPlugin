package org.telosys.tools.eclipse.plugin.decorator;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.telosys.tools.eclipse.plugin.commons.PluginImages;

@SuppressWarnings("restriction")
public class FolderDecorator extends LabelProvider implements ILabelDecorator  {

	@Override
	public Image decorateImage(Image image, Object element) {
		/*
		 * image   : the input image to decorate, or null if the element has no image
		 *           Here never null ( 1rts time contains the standard "folder" image
		 * element : the element whose image is being decorated
		 */
		//Image telosysFolderImage = PluginImages.getImage(PluginImages.TELOSYS_FOLDER);
		if (element instanceof Folder) {

			Folder folder = (Folder) element;

			// Apply decorator for "TelosysTools" (ignore case) or any "telosys*tools"
			final String folderNameLowerCase = folder.getName().toLowerCase() ;
			if ( "telosystools".equalsIgnoreCase( folderNameLowerCase )  ) {
				return getTelosysToolsFolderImage() ;
			}
			else {
				if ( folderNameLowerCase.startsWith("telosys") 
						&& folderNameLowerCase.endsWith("tools") ) {
					return getTelosysToolsFolderImage() ;
				}
			}
		}
		return null; // null means "no decoration is to be applied"
	}
	
	public Image getTelosysToolsFolderImage() {
		return PluginImages.getImage(PluginImages.TELOSYS_FOLDER);
		
	}
	
	@Override
	public String decorateText(String text, Object element) {
		// NOTHING TO DECORATE
		return null;
	}
}
