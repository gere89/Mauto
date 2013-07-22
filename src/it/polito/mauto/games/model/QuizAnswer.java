/**-------------LOG-------------
 * VERSIONE: 0.1
 * ULTIMA MODIFICA: Samuele
 * -----------------------------*/

package it.polito.mauto.games.model;

public class QuizAnswer {

	private String answerText;
	private boolean isCorrect;
	
	public QuizAnswer(String answerText, boolean isCorrect) {
		super();
		this.answerText = answerText;
		this.isCorrect = isCorrect;
	}

	public String getAnswerText() {
		return answerText;
	}

	public boolean isCorrect() {
		return isCorrect;
	}	

}
