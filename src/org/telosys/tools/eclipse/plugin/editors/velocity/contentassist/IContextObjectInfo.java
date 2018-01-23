package org.telosys.tools.eclipse.plugin.editors.velocity.contentassist;

import java.util.List;

import org.telosys.tools.eclipse.plugin.editors.velocity.model.VelocityKeyWord;

public interface IContextObjectInfo {

	List<VelocityKeyWord> getPredefineVariables();

	List<VelocityKeyWord> getContextBeans();

	List<VelocityKeyWord> getBeanInfo(String variableName);
	
}
