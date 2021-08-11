package org.telosys.tools.eclipse.plugin.editors.velocity.model;

public class VelocityKeyWord implements Comparable<VelocityKeyWord> {
	
	private final String value;
	private final String displayValue;
	private final String additionnalDocumentation;
	private final String completionIconName;
	
	/**
	 * Constructor
	 * @param value
	 * @param displayValue
	 * @param additionnalDocumentation
	 * @param iconName
	 */
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

	public String getDisplayValue() {
		return displayValue;
	}
	
	public String getAdditionnalDocumentation() {
		return additionnalDocumentation;
	}
	
	public String getCompletionIconName() {
		return completionIconName;
	}

	@Override
	public int compareTo(VelocityKeyWord o) {
		 return displayValue.compareTo(o.displayValue);
	}
	
}
