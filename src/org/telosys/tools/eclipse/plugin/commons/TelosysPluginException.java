package org.telosys.tools.eclipse.plugin.commons;

/**
 * @author Laurent GUERIN
 * 
 */
public class TelosysPluginException extends Exception 
{
    //private static final TelosysClassLogger log = new TelosysClassLogger(TelosysException.class);

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @param message
     */
    public TelosysPluginException(String message)
    {
        super(message);
//        Telosys.error("TelosysException : " + message);
//        Telosys.error(this);
    }

    /**
     * @param message
     * @param cause
     */
    public TelosysPluginException(String message, Throwable cause)
    {
        super(message, cause);
//        Telosys.error("TelosysException : " + message + " ( cause : " + cause.toString() + ")");
//        Telosys.error(this);

    }
}