package it.polito.mauto.classes;

public class Car {
	
	private int year;
	private String id, model, carmaker, description, category, carPosition, path;
	
	private boolean isLooked;
	
	public Car(String id, int year, String model, String carmaker, String description, String category, String carPosition, String path) {
		super();
		this.id = id;
		this.year = year;
		this.model = model;
		this.carmaker = carmaker;
		this.description = description;
		this.category = category;
		this.carPosition = carPosition;
		this.path = path;
		
		isLooked = false;
	}

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getCarmaker() {
		return carmaker;
	}

	public void setCarmaker(String carmaker) {
		this.carmaker = carmaker;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCarPosition() {
		return carPosition;
	}

	public void setCarPosition(String carPosition) {
		this.carPosition = carPosition;
	}
		
	public boolean isLooked() {
		return isLooked;
	}

	public void setAsLooked() {
		isLooked = true;;
	}

}