package it.polito.mauto.activity;

import it.polito.mauto.classes.MautoFunc;
import it.polito.mauto.classes.Messages;
import it.polito.mauto.classes.Route;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseActivity extends Activity {
	
	protected Context context;
	protected Route route;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		route = (Route) context.getApplicationContext();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mauto_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_mauto_reset:
	        	setDialog(MautoFunc.DIALOG_TITLE_RESET, Messages.getString("DialogBox.MESSAGE_RESET"), "reset").show();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onBackPressed() {
		setDialog(MautoFunc.DIALOG_TITLE_EXIT, Messages.getString("DialogBox.MESSAGE_EXIT"), "finish").show();
	}
	
	/**
	 * Set up the reset preferences dialog alert
	 * @param title
	 * @param message
	 * @return {@link AlertDialog}
	 */
	private AlertDialog setDialog(String title, String message, final String action) {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		
		// Set up dialog alert
		dialog.setTitle(title);
	    dialog.setMessage(message);
	    
	    // Add the buttons
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, Messages.getString("DialogBox.BUTTON_TEXT_POSITIVE"), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				positiveAction(action);
			}
		});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, Messages.getString("DialogBox.BUTTON_TEXT_NEGATIVE"), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		return dialog;
	}
	
	/**
	 * Reset the shared preferences
	 */
	private void resetPreferences() {
		// Reset the shared preferences
		SharedPreferences settings = getSharedPreferences(Route.PREFKEY_PREFS_NAME, 0);
		//SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(Route.PREFKEY_CARS_LIST);
        // Commit the edits!
        editor.commit();
        // Give a feedback
        Toast.makeText(context, "App resettata!", Toast.LENGTH_LONG).show();
	}
	
	/**
	 * Performe positive action of dialog alert
	 * @param action
	 */
	private void positiveAction(String action) {
		if(action.compareToIgnoreCase("reset")==0) {
			resetPreferences();
		} else if(action.compareToIgnoreCase("finish")==0) {
			finish();
		}
		
	}
	
}
