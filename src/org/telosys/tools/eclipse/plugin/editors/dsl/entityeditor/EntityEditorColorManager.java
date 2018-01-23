package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Provides a color for every content.
 * 
 */
public class EntityEditorColorManager {

    public static final RGB ENTITY_COLOR = new RGB(255, 0, 0);
    public static final RGB ANNOTATION_COLOR = new RGB(0, 50, 200);
    public static final RGB COMMENT_COLOR = new RGB(50, 200, 50);
    public static final RGB FIELD_NAME_COLOR = new RGB(153, 51, 0);
    public static final RGB FIELD_TYPE_COLOR = new RGB(153, 0, 153);
    public static final RGB STRING_COLOR = new RGB(0, 50, 200);
    public static final RGB DEFAULT_COLOR = new RGB(0, 0, 0);

    private Map<RGB, Color> fColorTable = new HashMap<RGB, Color>();

    public void dispose() {
        for (Color color : fColorTable.values()) {
            color.dispose();
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
