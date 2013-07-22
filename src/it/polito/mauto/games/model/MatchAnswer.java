package it.polito.mauto.games.model;

public class MatchAnswer {
	private int matchID;
	private String name;
	
	public MatchAnswer(int matchID, String name) {
		super();
		this.name = name;
		this.matchID = matchID;
	}

	public int getMatchID() {
		return matchID;
	}

	public String getName() {
		return name;
	}
	
}
