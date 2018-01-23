package org.telosys.tools.eclipse.plugin.editors.velocity.contentassist;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class ContentAssistConfiguration {
	private static final String DOC_BUNDLE_NAME= "org.telosys.tools.eclipse.plugin.editors.velocity.contentassist.ContentAssistMessages"; //$NON-NLS-1$
    private static final ResourceBundle RESOURCE_BUNDLE_DOC= ResourceBundle.getBundle(DOC_BUNDLE_NAME);
	private static final String CONF_BUNDLE_NAME= "org.telosys.tools.eclipse.plugin.editors.velocity.contentassist.VelocityEditorConfiguration"; //$NON-NLS-1$
    private static final ResourceBundle RESOURCE_BUNDLE_CONF = ResourceBundle.getBundle(CONF_BUNDLE_NAME);
    
    // Standard Velocity directives
    public static final String DIRECTIVE_SET_DOC      = "directive.set.doc";
    public static final String DIRECTIVE_IF_DOC       = "directive.if.doc";
    public static final String DIRECTIVE_FOREACH_DOC  = "directive.foreach.doc";
    public static final String DIRECTIVE_INCLUDE_DOC  = "directive.include.doc";
    public static final String DIRECTIVE_PARSE_DOC    = "directive.parse.doc";
    public static final String DIRECTIVE_STOP_DOC     = "directive.stop.doc";
    public static final String DIRECTIVE_BREAK_DOC    = "directive.break.doc";
    public static final String DIRECTIVE_EVALUATE_DOC = "directive.evaluate.doc";
    public static final String DIRECTIVE_DEFINE_DOC   = "directive.define.doc";
    public static final String DIRECTIVE_MACRO_DOC    = "directive.macro.doc";
    
    // Specific Telosys directives (added in ver 2.0.7)
    public static final String DIRECTIVE_ASSERTFALSE_DOC = "directive.assertfalse.doc";
    public static final String DIRECTIVE_ASSERTTRUE_DOC  = "directive.asserttrue.doc";
    public static final String DIRECTIVE_ERROR_DOC       = "directive.error.doc";
    public static final String DIRECTIVE_USING_DOC       = "directive.using.doc";

    // Specific Telosys directives (added in ver 3.0.0)
    public static final String DIRECTIVE_CHECKID_DOC = "directive.checkid.doc";
    
    // Customizable bean variable name (ex : $entityFound must have the same completion than $entity)
    public static final String  CONF_BEAN_VARIABLES_CUSTOMIZABLE = "velocity.editor.beanVariableSuffixable";
    
    private ContentAssistConfiguration() {
        // no instance
    }

    /**
     * Returns the resource string associated with the given key in the resource bundle. If there isn't 
     * any value under the given key, the key is returned.
     *
     * @param key the resource key
     * @return the string
     */ 
    public static String getMessage(String key) {
        try {
            return RESOURCE_BUNDLE_DOC.getString(key);
        } catch (Exception e) {
            return '!' + key + '!';
        }
    }
    
    /**
     * Returns the resource string associated with the given key in the resource bundle. If there isn't 
     * any value under the given key, the key is returned.
     *
     * @param key the resource key
     * @return the string
     */ 
    public static String getConfigurationValue(String key) {
        try {
            return RESOURCE_BUNDLE_CONF.getString(key);
        } catch (Exception e) {
            return '!' + key + '!';
        }
    }
    
    /**
     * Returns the resource bundle managed by the receiver.
     * 
     * @return the resource bundle
     */
    public static ResourceBundle getDocumentationResourceBundle() {
        return RESOURCE_BUNDLE_DOC;
    }
    
    /**
     * Returns the resource bundle managed by the receiver.
     * 
     * @return the resource bundle
     */
    public static ResourceBundle getConfigurationResourceBundle() {
        return RESOURCE_BUNDLE_CONF;
    }
    
    /**
     * Returns the formatted resource string associated with the given key in the resource bundle. 
     * <code>MessageFormat</code> is used to format the message. If there isn't  any value 
     * under the given key, the key is returned.
     *
     * @param key the resource key
     * @param arg the message argument
     * @return the string
     */ 
    public static String getFormattedMessage(String key, Object arg) {
        return getFormattedMessage(key, new Object[] { arg });
    }
    
    /**
     * Returns the formatted resource string associated with the given key in the resource bundle. 
     * <code>MessageFormat</code> is used to format the message. If there isn't  any value 
     * under the given key, the key is returned.
     *
     * @param key the resource key
     * @param args the message arguments
     * @return the string
     */ 
    public static String getFormattedMessage(String key, Object[] args) {
        return MessageFormat.format(getMessage(key), args);  
    }   
}
