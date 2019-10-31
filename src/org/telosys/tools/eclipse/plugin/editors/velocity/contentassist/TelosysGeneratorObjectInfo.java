package org.telosys.tools.eclipse.plugin.editors.velocity.contentassist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.telosys.tools.eclipse.plugin.commons.PluginImages;
import org.telosys.tools.eclipse.plugin.editors.velocity.model.VelocityKeyWord;
import org.telosys.tools.generator.context.doc.tooling.MethodInfo;

public class TelosysGeneratorObjectInfo implements IContextObjectInfo {
	
	private ContextInfo contextInfo;
	private List<VelocityKeyWord> predefVariables;
	private List<VelocityKeyWord> objectNames;
	private Map<String,List<VelocityKeyWord>> beansInfos;
	
	public TelosysGeneratorObjectInfo() {
		super();
		this.contextInfo = new ContextInfo();
		this.beansInfos = new HashMap<String, List<VelocityKeyWord>>();
	}

	
	/**
	 * Get all the predefine variables ($AMP, $SRC ...) in the Telosys context for the Velocity completion.
	 * @return Velocity keywords completion
	 */
	@Override
	public List<VelocityKeyWord> getPredefineVariables() {
		List<VelocityKeyWord> variables = new ArrayList<VelocityKeyWord>();
		
		if (this.predefVariables == null) {
			String[] names = contextInfo.getVariableNames() ;
			if (names != null) {
				for (int i = 0; i < names.length; i++) {
					// Display value
					String displayValue = "$" + names[i];						
					variables.add(new VelocityKeyWord(names[i], displayValue,
							null, PluginImages.VELOCITY_PREDEF_VARIABLE));
				}
				this.predefVariables = variables;
			}
		} else {
			variables = this.predefVariables;
		}

		return variables;
	}

	/**
	 * Get all the Telosys context beans  ($beanClass, $attribute ...) for Velocity completion
	 * @return Velocity keywords completion
	 */
	@Override
	public List<VelocityKeyWord> getContextBeans() {
		List<VelocityKeyWord> objects = new ArrayList<VelocityKeyWord>();
		if (this.objectNames == null) {
			// Context Beans
			objects.addAll(this.initializeBeansKeywords(contextInfo.getObjectNames(), PluginImages.VELOCITY_BEAN_GENERATOR));

			// Predefines names
			objects.addAll(this.initializeBeansKeywords(contextInfo.getPredefinedNames(), PluginImages.VELOCITY_PREDEFNAMES));
			
			// Sort list
			Collections.sort(objects);
			
			this.objectNames = objects;
		} else {
			objects = this.objectNames;
		}		
		return objects;
	}

	/**
	 * Get all the methods for a Telosys context bean.
	 * @return Velocity keywords completion
	 */
	@Override
	public List<VelocityKeyWord> getBeanInfo(String objectName) {
		List<VelocityKeyWord> infos = new ArrayList<VelocityKeyWord>();
		
		if (this.beansInfos.containsKey(objectName)) {
			infos = this.beansInfos.get(objectName);
		} else {
			MethodInfo[] methodsInfo = contextInfo.getAllMethodsInfo(objectName);
			if (methodsInfo != null) {
					
				for (int i = 0; i < methodsInfo.length; i++) {
					
					// Display Name
					String imageName;
					StringBuilder displayName = new StringBuilder();
					displayName.append(methodsInfo[i].getVelocityName());
					if (methodsInfo[i].isAttributeLike()) {
						imageName = PluginImages.VELOCITY_BEAN_PROPERTY;
					} else {
						displayName.append("()");
						imageName = PluginImages.VELOCITY_BEAN_METHOD;
					}
					
					// The doc
					String doc = contextInfo.getMethodDocumentation(objectName, methodsInfo[i].getSignature());
					
					infos.add(new VelocityKeyWord(methodsInfo[i].getVelocityName(), 
													displayName.toString(), 
													doc,
													imageName));
				}
				this.beansInfos.put(objectName, infos);
			}
		}
		
		return infos;
	}
	
	/**
	 * Initialize VelocityKeywords for completion for all Telosys context beans and predefines names
	 * @param predefinedNames
	 * @param velocityBeanGenerator
	 * @return Velocity keywords
	 */
	private List<VelocityKeyWord> initializeBeansKeywords(String[] objects, String iconKeyName) {
		List<VelocityKeyWord> keywords = new ArrayList<VelocityKeyWord>();

		if (objects != null) {
			for (int i = 0; i < objects.length; i++) {
				// Display value
				String displayValue = "$" + objects[i];

				// Documentation
				String doc = contextInfo.getClassDocumentation(objects[i]);			
				keywords.add(new VelocityKeyWord(objects[i], displayValue,
						doc, iconKeyName));
			}			
		}
		return keywords;
	}

}
