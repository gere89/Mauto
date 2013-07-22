package it.polito.mauto.classes;

import java.util.ArrayList;

public class Part {
	private String partID, name, category, path, description;
	private ArrayList<Parameter> performance;
	private boolean locked;
	
	public Part(String partID, String name, String category, String path, ArrayList<Parameter> performance) {
		this.partID = partID;
		this.name = name;
		this.category = category;
		this.performance = performance;
		this.path = path;
		locked = true;
	}

	public String getPartID() {
		return partID;
	}
	
	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getDescription() {
		return description;
	}

	public ArrayList<Parameter> getPerformance() {
		return performance;
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public void unlock() {
		locked = false;
	}
	
}
