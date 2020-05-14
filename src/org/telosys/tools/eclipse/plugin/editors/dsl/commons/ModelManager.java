package org.telosys.tools.eclipse.plugin.editors.dsl.commons;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.telosys.tools.api.GenericModelLoader;
import org.telosys.tools.api.TelosysModelException;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.dsl.DslModelManager;
import org.telosys.tools.dsl.DslModelUtil;
import org.telosys.tools.dsl.parser.model.DomainModelInfo;
import org.telosys.tools.eclipse.plugin.commons.Const;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.generic.model.Model;

public class ModelManager {

	private ModelManager() {}
	
	/**
	 * Loads the DSL model defined by the given Eclipse IFile 
	 * @param eclipseModelFile the model file ( eg "xxx/xxx.model")
	 * @return
	 */
	public static ModelLoadingResult load(IFile eclipseModelFile) {
		File modelFile = EclipseWksUtil.toFile(eclipseModelFile);
		return load(modelFile);
	}
	
	/**
	 * Loads the DSL model defined by the given standard Java File 
	 * @param modelFile the model file ( eg "xxx/xxx.model")
	 * @return
	 */
	public static ModelLoadingResult load(File modelFile) {
		ModelLoadingResult r = loadModel(modelFile);
		updateErrorMarkers(r.getEntitiesFileNames(), r.getEntitiesErrors());
		return r;
	}
	
	private static ModelLoadingResult loadModel(File modelFile) {

		// --- 1) Load entities absolute file names
		List<String> entitiesFileNames = DslModelUtil
				.getEntitiesAbsoluteFileNames(modelFile);

		// --- 2) Load model information from ".model" file
		DslModelManager modelManager = new DslModelManager();
		DomainModelInfo modelInfo = modelManager
				.loadModelInformation(modelFile);

		// --- 3) Prepare the model loader and try to load the model
		GenericModelLoader genericModelLoader = new GenericModelLoader();

		Model model;
		Map<String,List<String>> voidMap = new HashMap<>();
		try {
			// --- 3.1) Try to load/parse the model
			model = genericModelLoader.loadModel(modelFile);
			// --- 3.2) Model OK : no parsing error
			return new ModelLoadingResult(model, modelInfo,
					entitiesFileNames, voidMap);
		} catch (TelosysModelException modelException) {
			// --- 3.2) Invalid Model : keep parsing errors
			return new ModelLoadingResult(null, modelInfo,
					entitiesFileNames, modelException.getParsingErrors());
		} catch (TelosysToolsException e) {
			MsgBox.error("Cannot load model !\n Unexpected exception", e);
			return new ModelLoadingResult(null, modelInfo,
					entitiesFileNames, voidMap);
		}
	}

	private static void updateErrorMarkers(List<String> entitiesFileNames, Map<String,List<String>> entitiesErrors) {
		for (String entityFile : entitiesFileNames) {
			// Entity file name ( eg "Person.entity" )
			String entityFileName = (new File(entityFile)).getName();
			// Entity name without ".entity" extension
			String entityName = StrUtil.removeEnd(entityFileName, Const.DOT_ENTITY);

			// Update errors markers
			IFile iFile = EclipseWksUtil.toIFile(entityFile);
			deleteErrorMarkers(iFile);
			if (entitiesErrors != null) {
				addErrorMarkers(iFile, entitiesErrors.get(entityName));
			}
		}
	}
	
	private static void deleteErrorMarkers(IFile iFile) {
		try {
			iFile.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			MsgBox.error("Cannot delete markers", e);
		}
	}
	
	private static void addErrorMarkers(IFile iFile, List<String> entityErrorsList) {
		if ( entityErrorsList != null ) {
			for ( String msg : entityErrorsList ) {
				addErrorMarker(iFile, msg); 
			}
		}
	}
	private static void addErrorMarker(IFile iFile, String message) {
		try {
			// add new marker
			IMarker marker = iFile.createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			marker.setAttribute(IMarker.LINE_NUMBER, 1 );
			marker.setAttribute(IMarker.MESSAGE, message );
		} catch (CoreException e) {
			MsgBox.error("Cannot create marker", e);
		}
	}

}
