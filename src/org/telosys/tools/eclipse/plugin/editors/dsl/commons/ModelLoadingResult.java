package org.telosys.tools.eclipse.plugin.editors.dsl.commons;

import java.util.List;
import java.util.Map;

import org.telosys.tools.dsl.parser.model.DomainModelInfo;
import org.telosys.tools.generic.model.Model;

public class ModelLoadingResult {

	private final Model model;
	private final DomainModelInfo modelInfo;
	private final List<String> entitiesFileNames;
	private final Map<String, List<String>> entitiesErrors;

	public ModelLoadingResult(Model model, DomainModelInfo modelInfo,
			List<String> entitiesFileNames,
			Map<String, List<String>> entitiesErrors) {
		super();
		this.model = model;
		this.modelInfo = modelInfo;
		this.entitiesFileNames = entitiesFileNames;
		this.entitiesErrors = entitiesErrors;
	}

	/**
	 * Returns the model loaded. <br>
	 * NB can be null in case of errors and model not loaded
	 * @return
	 */
	public Model getModel() {
		return model;
	}

	public DomainModelInfo getModelInfo() {
		return modelInfo;
	}

	public List<String> getEntitiesFileNames() {
		return entitiesFileNames;
	}

	public Map<String, List<String>> getEntitiesErrors() {
		return entitiesErrors;
	}

}
