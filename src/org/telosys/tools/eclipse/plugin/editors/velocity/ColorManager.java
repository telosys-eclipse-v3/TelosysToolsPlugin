package org.telosys.tools.eclipse.plugin.editors.velocity;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ColorManager {
	
	//--- VELOCITY EDITOR TEXT COLORS 
	public final static RGB RGB_VELOCITY_DEFAULT   = new RGB(0, 0, 0);
	public final static RGB RGB_VELOCITY_COMMENT   = new RGB(63, 127, 95);
	public final static RGB RGB_VELOCITY_DIRECTIVE = new RGB(127, 0, 85);
	public static final RGB RGB_VELOCITY_REFERENCE = new RGB(39, 121, 243);

	protected Map<RGB,Color> fColorTable = new HashMap<RGB,Color>(10);

	public void dispose() {
		for ( Color color : fColorTable.values() ) {
			color.dispose() ;
		}
	}
	
	public Color getColor(RGB rgb) {
		Color color = (Color) fColorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}
		return color;
	}
}
