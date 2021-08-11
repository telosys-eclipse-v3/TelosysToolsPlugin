package org.telosys.tools.eclipse.plugin.editors.velocity.contentassist;

import java.util.List;

import org.telosys.tools.eclipse.plugin.editors.velocity.model.VelocityKeyWord;

public interface IContextObjectInfo {

	/**
	 * Returns all the predefine variables ($AMP, $SRC ...) for Velocity completion.
	 */
	List<VelocityKeyWord> getPredefineVariables();

	/**
	 * Returns all the Telosys context beans ($entity, $attribute, $fn, ...) for Velocity completion
	 */
	List<VelocityKeyWord> getContextBeans();

	/**
	 * Returns keywords to suggest attributes and methods 
	 * @param variableName object name ( eg 'entity', 'fn', 'attribute', etc )
	 * @return
	 */
	List<VelocityKeyWord> getBeanInfo(String variableName);
	
}
