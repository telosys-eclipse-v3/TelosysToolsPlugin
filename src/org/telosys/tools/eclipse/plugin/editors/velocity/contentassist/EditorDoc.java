package org.telosys.tools.eclipse.plugin.editors.velocity.contentassist;

import java.util.HashMap;
import java.util.Map;

import org.telosys.tools.generator.context.doc.tooling.ClassInfo;
import org.telosys.tools.generator.context.doc.tooling.MethodInfo;
import org.telosys.tools.generator.context.doc.tooling.MethodParameter;

public class EditorDoc {

	Map<String,String> classDoc   = new HashMap<>();
	Map<String,String> methodsDoc = new HashMap<>();
	
	private String buildKey( ClassInfo classInfo, MethodInfo methodInfo ) {
		return classInfo.getContextName() + "." + methodInfo.getSignature() ;
	}
	
	public String getClassDoc( ClassInfo classInfo ) {
		String name = classInfo.getContextName() ;
		String doc = classDoc.get( name ) ;
		if ( null == doc ) {
			doc = buildHtmlClassDoc( classInfo ) ;
			classDoc.put(name, doc);
		}
		return doc ;
	}
	
	public String getMethodDoc( ClassInfo classInfo, MethodInfo methodInfo) {
		String key = buildKey( classInfo, methodInfo);
		String doc = methodsDoc.get(key) ;
		if ( null == doc ) {
			doc = buildHtmlMethodDoc(methodInfo ) ;
			classDoc.put(key, doc);
		}
		return doc ;
	}

	private String buildHtmlClassDoc( ClassInfo classInfo ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "<p>");
		
		for ( String s : classInfo.getDocText()  ) {
			sb.append( s + "<br>" );
		}
		sb.append( "<br>" );
		if ( classInfo.getSince() != null ) {
			if ( classInfo.getSince().trim().length() > 0 ) {
				sb.append( "Since : " + classInfo.getSince() + "<br>" );
			}
		}
		if ( classInfo.isDeprecated()  ) {
			sb.append( "<b>DEPRECATED (!) <b><br>" );
		}
		sb.append( "</p>");

		return sb.toString() ;
	}
	
	private String buildHtmlMethodDoc(MethodInfo methodInfo) {
		StringBuilder sb = new StringBuilder();
		
		sb.append( "<CODE> <B>." + methodInfo.getSimpleDescription() + "</B> </CODE>" );
		sb.append( "<p>" );
		if ( methodInfo.isDeprecated() ) {
			sb.append( "<b>Deprecated.</b><br>" );
			sb.append( "<br>" );
		}
		for ( String s : methodInfo.getDocText()  ) {
			sb.append( s + "<br>" );
		}
		if ( methodInfo.hasParameters() ) {
			sb.append( "<br>" );
			sb.append( "<b>Parameters : </b><br>" );
			for ( MethodParameter p : methodInfo.getParameters() ) {
				sb.append("&nbsp;&nbsp;&nbsp;<b>" + p.getName() + "</b> : " + p.getDescription() + "<br>");
			}			
		}
		if ( methodInfo.hasExampleText() ) {
			sb.append( "<br>" );
			sb.append( "<b>Example : </b><br>" );
			sb.append( "<code>" );
			for ( String s : methodInfo.getExampleText() ) {
				sb.append( "&nbsp;&nbsp;&nbsp;" + s + "<br>" );
			}
			sb.append( "</code>" );
		}
		if ( methodInfo.hasSince() ) {
			sb.append( "<br>" );
			sb.append( "<b>Since : </b>" + methodInfo.getSince() + "<br>" );
		}
		sb.append( "</p>" );

		return sb.toString() ;
	}
	
}
