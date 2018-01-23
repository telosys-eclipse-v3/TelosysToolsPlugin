package org.telosys.tools.eclipse.plugin.commons;

import org.telosys.tools.generic.model.Model;
import org.telosys.tools.repository.model.RepositoryModel;

public class ModelUtil {
	
	/**
	 * Converts the given generic model to a "database model"
	 * @param model
	 * @return
	 */
	public final static RepositoryModel toRepositoryModel(Model model) {
		if ( model != null ) {
			if ( model instanceof RepositoryModel ) {
				return (RepositoryModel) model ;
			}
			else {
				throw new RuntimeException("Cannot convert Model to RepositoryModel (not an instance of RepositoryModel)");
			}
		}
		else {
			throw new IllegalArgumentException("Model is null");
		}
	}

}
