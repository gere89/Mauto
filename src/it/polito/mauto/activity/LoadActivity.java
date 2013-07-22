package it.polito.mauto.activity;

import it.polito.mauto.activity.R;
import it.polito.mauto.classes.Route;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ProgressBar;

public class LoadActivity extends BaseActivity {
	
	private ProgressBar progressBar;
	private int progressStatus = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);
		
		progressBar = (ProgressBar) findViewById(R.id.layout_load_progressbar);
		progressBar.setProgress(progressStatus);
	
		new InitializeApp().execute();
	}
	
	
	private class InitializeApp extends AsyncTask<Void, Integer, Void> {
      
		@Override
		protected Void doInBackground(Void... params) {
			publishProgress(10);
			
			// Initialize data structure
			route.initialize();
			publishProgress(20);
			
			// Load car DB
			try {
				route.loadFirstPartDB();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			publishProgress(30);
			
			// Load other DB
			try {
				route.loadSecondPartDB();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			publishProgress(40);
			
			// Restore preferences
			SharedPreferences settings = getSharedPreferences(Route.PREFKEY_PREFS_NAME, 0);
			//SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			String carsPreferences = settings.getString(Route.PREFKEY_CARS_LIST, null);
			publishProgress(50);
			String gamesPreferences = settings.getString(Route.PREFKEY_GAMES_LIST, null);
			publishProgress(60);
			String partsPreferences = settings.getString(Route.PREFKEY_PARTS_LIST, null);
			publishProgress(70);
			route.compareCarsAndPreference(carsPreferences);
			publishProgress(80);
			route.compareGamesAndPreference(gamesPreferences);
			publishProgress(90);
			route.comparePartsAndPreference(partsPreferences);
			publishProgress(100);
			// Add load of mycar parts added
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			progressBar.setProgress(progress[0]);
	     }
		
		@Override
        protected void onPostExecute(Void param)  {
        	// On finish start intent to MautoActivity
        	Intent intent = new Intent(LoadActivity.this, MautoActivity.class);
			startActivity(intent);
			finish();
        }        
	}
}
