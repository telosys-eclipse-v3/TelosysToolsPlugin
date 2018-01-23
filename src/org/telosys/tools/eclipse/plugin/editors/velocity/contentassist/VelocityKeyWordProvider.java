package org.telosys.tools.eclipse.plugin.editors.velocity.contentassist;

import java.util.ArrayList;
import java.util.List;

import org.telosys.tools.eclipse.plugin.editors.velocity.model.VelocityKeyWord;
import org.telosys.tools.eclipse.plugin.editors.velocity.model.VelocityKeyWords;

/**
 * The VelocityKeyWordProvider class has a single method that returns all key-words starting with the string 
 * passed as a parameter
 * @author bewilcox
 *
 */
public class VelocityKeyWordProvider {
	
	private IContextObjectInfo contextObjectInfo;
	
	
	public VelocityKeyWordProvider(IContextObjectInfo contextObjectInfo) {
		super();
		this.contextObjectInfo = contextObjectInfo;
	}
 
	/**
	 * Return all key words starting by the string passed as a parameter.
	 * @param word
	 * @return all the corresponding key-words
	 */
	public List<VelocityKeyWord> suggest(String word) {
		
		ArrayList<VelocityKeyWord> wordBuffer = new ArrayList<VelocityKeyWord>();
		
		// provide the directives
		wordBuffer.addAll(this.filterSuggestions(VelocityKeyWords.getKeyWords(), word));
		
		// provide the predefines variables
		wordBuffer.addAll(this.filterSuggestions(this.contextObjectInfo.getPredefineVariables(), word));
		
		// provide the generator variables
		wordBuffer.addAll(this.filterSuggestions(this.contextObjectInfo.getContextBeans(), word));

		return wordBuffer;
	}
	
	/**
	 * Return all the attributes and methods from a the code context variable passed as a parameter.
	 * @param variable name
	 * @return all the corresponding key-words
	 */
	public List<VelocityKeyWord> suggestContextVariableOutline(String variableName, String filter) {
		
		ArrayList<VelocityKeyWord> wordBuffer = new ArrayList<VelocityKeyWord>();
		
		// provide attributs and method of a variable generator
		List<VelocityKeyWord> completions = this.filterSuggestions(this.contextObjectInfo.getBeanInfo(variableName), filter);
		if (completions.isEmpty()) {
			// Some variables must match a pattern to have completions. This feature allows you to avail 
			// the completion for custom variables. For a predefine list of pattern, if the variable start by this pattern
			// the completion provide a methods list.
			// For ex : There is the $target varibale wich have methods completion. If, there a variable with the
			// name $targetCustom, this variable will have the completion too.
			String customzableVariableNames = ContentAssistConfiguration.getConfigurationValue(
															ContentAssistConfiguration.CONF_BEAN_VARIABLES_CUSTOMIZABLE);
			
			String[] allowedPrefixes = customzableVariableNames.split(";");
			for (int i = 0; i < allowedPrefixes.length; i++) {
				if(variableName.startsWith(allowedPrefixes[i])) {
					completions = this.filterSuggestions(this.contextObjectInfo.getBeanInfo(allowedPrefixes[i]), filter);
					wordBuffer.addAll(completions);
					break;
				}
			}
		} else {
			wordBuffer.addAll(completions);
		}

		return wordBuffer;
	}
	
	
	/**
	 * Filter completion suggestions with a filter passed as paramter
	 * @param allSuggestions
	 * @param filter
	 * @return Filtered List
	 */
	private List<VelocityKeyWord> filterSuggestions(List<VelocityKeyWord> allSuggestions,String filter) {
		List<VelocityKeyWord> listFiltered = new ArrayList<VelocityKeyWord>();
		
		if (filter ==  null) {
			filter = "";
		}
		
		if (filter.isEmpty()) {
			listFiltered = allSuggestions;
		} else {
			for (VelocityKeyWord keyword : allSuggestions) {
				if (keyword.getDisplayValue().startsWith(filter.replaceAll("^\\$\\{", "\\$"))) {
					listFiltered.add(keyword);
				}
			}
		}
		
		return listFiltered;
	}

}
