package it.polito.mauto.activity;

import it.polito.mauto.classes.Game;
import it.polito.mauto.classes.Route;
import it.polito.mauto.games.model.MatchGame;
import it.polito.mauto.games.model.QuizGame;
import it.polito.mauto.games.view.MatchView;
import it.polito.mauto.games.view.PowerPlugView;
import it.polito.mauto.games.view.QuizView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class GameActivity extends Activity {
	
	private Route route;
	private Game gameToPlay;
	private String carID, gameType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		route = (Route) getApplicationContext();
		
		// Check extras
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// Retrive game to play
			carID = extras.getString("carID");
			gameToPlay = route.getCarGame(carID);
			
			if(gameToPlay.isPerformed()) {
				// Redirect to technical information page
				Intent alreadyPlayed = new Intent(GameActivity.this, TechnicalActivity.class);
				alreadyPlayed.putExtra("carID", carID);
				startActivity(alreadyPlayed);
				finish();
			} else {
				gameType = gameToPlay.getGameType();
				
				// Switch game by type
				if(gameType.compareToIgnoreCase("quiz")==0) {
					// QUIZ GAME
					this.performQuizGame();
				} else if (gameType.compareToIgnoreCase("match")==0) {
					// MATCH GAME
					this.performMatchGame();
				} else if (gameType.compareToIgnoreCase("powerplug")==0) {
					// POWERPLUG GAME
					PowerPlugView ppv = new PowerPlugView(this);
					setContentView(ppv);
				} else {
					Intent leave = new Intent(GameActivity.this, RouteActivity.class);
					startActivity(leave);
					finish();
				}
			}
		} else {
			throw new RuntimeException("There are some errors in carID retrieval");
		}
	}
	

	private void performMatchGame() {
		try {
			MatchGame m = route.getMatch(carID);
			MatchView mv = new MatchView(this, m);
			setContentView(mv);
			
		} catch (Exception e) {
			Log.d("GameActivity - MatchGame", e.toString());
			throw new RuntimeException(e);
		}
	}
	
	private void performQuizGame() {
		try {
			QuizGame q = route.getQuiz(carID);
			QuizView qv = new QuizView(this, q);
			setContentView(qv);
			
		} catch (Exception e) {
			Log.d("GameActivity - QuizGame", e.toString());
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void onBackPressed() {
		Intent leave = new Intent(GameActivity.this, RouteActivity.class);
		startActivity(leave);
		finish();		
	}

}
