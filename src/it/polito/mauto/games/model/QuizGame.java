package it.polito.mauto.games.model;

import it.polito.mauto.classes.Game;

import java.util.ArrayList;

public class QuizGame extends Game {
	
	private String question;
	private ArrayList<QuizAnswer> answerList;
	
	public QuizGame(String carID, String gameID, String name, String category, String gameType, String question, ArrayList<QuizAnswer> answerList) {
		super(carID, gameID, name, category, gameType);
		this.question = question;
		this.answerList = answerList;
	}
	
	
	public String getQuestion() {
		return question;
	}
	
	
	public QuizAnswer getAnswer(int index) {
		return answerList.get(index);
	}
	
	public int getNumberOfAnswer() {
		return answerList.size();
	}
}
