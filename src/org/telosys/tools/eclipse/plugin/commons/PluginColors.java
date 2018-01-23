package org.telosys.tools.eclipse.plugin.commons;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Display;

public class PluginColors {

	private static Color getSystemColor(int colorId) {
		Device device = Display.getCurrent();
		return device.getSystemColor(colorId);
	}
	
	public static Color rgb(int r, int g, int b) {
		return new Color(Display.getCurrent(), r, g, b );
	}
	
	public static Color blue() {
		return getSystemColor(SWT.COLOR_BLUE);
	}

	public static Color red() {
		return getSystemColor(SWT.COLOR_RED);
	}

	public static Color gray() {
		return getSystemColor(SWT.COLOR_GRAY);
	}

	public static Color widgetBackground() {
		return getSystemColor(SWT.COLOR_WIDGET_BACKGROUND );
	}
}
