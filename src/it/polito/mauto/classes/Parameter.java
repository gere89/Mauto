package it.polito.mauto.classes;

public class Parameter {
	private String name;
	private int value;
	
	public Parameter(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
