package it.polito.mauto.games.view;

import it.polito.mauto.activity.R;
import it.polito.mauto.activity.TechnicalActivity;
import it.polito.mauto.games.model.QuizAnswer;
import it.polito.mauto.games.model.QuizGame;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QuizView extends RelativeLayout {
	
	private Context context;
	private QuizGame quiz = null;

	public QuizView(Context context, QuizGame quiz) {
		super(context);
		this.context = context;
		this.quiz = quiz;		
		initView();
	}
	
	private void initView() {		
		// Faccio l'inflate del layout XML
		View.inflate(context, R.layout.layout_quiz, this);
		// Setto tutti gli elementi
		this.setUpViewElements();
	}

	
	private void setUpViewElements() {
		Button[] btn_answer = null;
		TextView txt_question = null;
		
		try {
			// Prendo i bottoni dalla view
			btn_answer = new Button[quiz.getNumberOfAnswer()];
			btn_answer[0] = (Button) findViewById(R.id.btn_quiz_answer_1);
			btn_answer[1] = (Button) findViewById(R.id.btn_quiz_answer_2);
			btn_answer[2] = (Button) findViewById(R.id.btn_quiz_answer_3);
			btn_answer[3] = (Button) findViewById(R.id.btn_quiz_answer_4);
			
			for(int i=0; i<btn_answer.length; i++) {
				// Setto il testo di ogni bottone con la risposta
				btn_answer[i].setText(quiz.getAnswer(i).getAnswerText());
				// Setto il comportamento al click
				this.setAnswerButton(btn_answer[i], quiz.getAnswer(i));
			}
			
			// Setto la TextView della domanda
			txt_question = (TextView) findViewById(R.id.txt_quiz_question);
			txt_question.setText(quiz.getQuestion());			
			
		} catch (Exception e) {
			Log.w("QuizView", e.toString());
			throw new RuntimeException(e);
		}
	}
	
	
	private void setAnswerButton(final Button btn, final QuizAnswer answer) {
		btn.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		// Creo il toast per il messaggio
        		Toast message = null;
        		// Creo l'intent per mostrare la scheda tecnica dopo il gioco
        		Intent quizIntent = new Intent(context, TechnicalActivity.class);
        		quizIntent.putExtra("carID", quiz.getCarID());
        		quizIntent.putExtra("gameID", quiz.getGameID());
    			quizIntent.putExtra("gamePerformed", true);
        		
        		if(answer.isCorrect()) {
        			message = Toast.makeText(context, "Risposta corretta!", Toast.LENGTH_SHORT);
        			quizIntent.putExtra("gameWellDone", true);
        			
        		} else {
        			message = Toast.makeText(context, "Hai sbagliato!", Toast.LENGTH_SHORT);
        			quizIntent.putExtra("gameWellDone", false);
        		}
        		
        		message.show();
        		context.startActivity(quizIntent);
				((Activity) context).finish();
			}
		});
	}

}
