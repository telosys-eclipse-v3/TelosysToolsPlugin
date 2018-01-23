package org.telosys.tools.eclipse.plugin.editors.velocity.model;

public class VelocityKeyWord implements Comparable<VelocityKeyWord> {
	
	private final String value;
	private final String displayValue;
	private final String additionnalDocumentation;
	private final String completionIconName;
	
	
//	public VelocityKeyWord(String value, String displayValue,
//			String additionnalDocumentation) {
//		super();
//		this.value = value;
//		this.displayValue = displayValue;
//		this.additionnalDocumentation = additionnalDocumentation;
//		this.completionIconName = null;
//	}
	
	public VelocityKeyWord(String value, String displayValue,
			String additionnalDocumentation, String iconName) {
		super();
		this.value = value;
		this.displayValue = displayValue;
		this.additionnalDocumentation = additionnalDocumentation;
		this.completionIconName = iconName;
	}
	
	public String getValue() {
		return value;
	}
//	public void setValue(String value) {
//		this.value = value;
//	}
	public String getDisplayValue() {
		return displayValue;
	}
//	public void setDisplayValue(String displayValue) {
//		this.displayValue = displayValue;
//	}
	
	public String getAdditionnalDocumentation() {
		return additionnalDocumentation;
	}
//	public void setAdditionnalDocumentation(String additionnalDocumentation) {
//		this.additionnalDocumentation = additionnalDocumentation;
//	}
	
	public String getCompletionIconName() {
		return completionIconName;
	}
//	public void setCompletionIconName(String completionIconName) {
//		this.completionIconName = completionIconName;
//	}

	@Override
	public int compareTo(VelocityKeyWord o) {
		 return displayValue.compareTo(o.displayValue);
	}
	
}
