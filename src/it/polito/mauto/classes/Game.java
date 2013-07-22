package it.polito.mauto.classes;

public class Game {

	private String name, category, carID, gameID, gameType;
	private boolean performed, wellDone;
	
	public Game(String carID, String gameID, String name, String category, String gameType) {
		super();
		this.carID = carID;
		this.gameID = gameID;
		this.name = name;
		this.category = category;
		this.gameType = gameType;
		this.performed = false;
		this.wellDone = false;
	}

	public String getCarID() {
		return carID;
	}
	
	public String getGameID() {
		return gameID;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public String getGameType() {
		return gameType;
	}

	public boolean isPerformed() {
		return performed;
	}

	public void setPerformed() {
		this.performed = true;
	}

	public boolean isWellDone() {
		return wellDone;
	}

	public void setWellDone() {
		this.wellDone = true;
	}
		
}
