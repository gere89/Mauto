package it.polito.mauto.activity;

import it.polito.mauto.classes.MautoFunc;
import it.polito.mauto.classes.Messages;
import it.polito.mauto.classes.Route;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.RelativeLayout;

public class BaseFragmentActivity extends FragmentActivity {

	protected Context context;
	protected Route route;
	protected RelativeLayout viewContainer;
	protected boolean isFromMauto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);
		
		isFromMauto = false;
		context = this;
		route = (Route) context.getApplicationContext();
		viewContainer = (RelativeLayout) findViewById(R.id.viewContainer);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.route_menu, menu);
		return true;
	}
	
	
	@Override
	public void onBackPressed() {
		if (!isFromMauto) {
			setDialog(Messages.getString("DialogBox.TITLE_BACKTOMENU"), Messages.getString("DialogBox.MESSAGE_BACKTOMENU")).show();
		} else {
			super.onBackPressed();
		}
	}
	
	/**
	 * Set up the reset preferences dialog alert
	 * @param title
	 * @param message
	 * @return {@link AlertDialog}
	 */
	private AlertDialog setDialog(String title, String message) {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		
		// Set up dialog alert
		dialog.setTitle(title);
	    dialog.setMessage(message);
	    
	    // Add the buttons
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, Messages.getString("DialogBox.BUTTON_TEXT_POSITIVE"), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				
				// Back to main menu
				Intent backTo = new Intent(BaseFragmentActivity.this, MautoActivity.class);
				startActivity(backTo);
				finish();
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
	 * Check origin of intent called
	 * @param layoutToInflate
	 */
	protected void checkOriginFromIntent(int layoutToInflate, int buttonToDeactivate) {
		Bundle extras = getIntent().getExtras();
		
		
		if(extras != null && extras.containsKey("from_mauto") && extras.getBoolean("from_mauto") == true) {
			isFromMauto = true;
			Log.w("isFromMauto - BaseFragment", ""+isFromMauto);
			extras.remove("from_mauto");
		}
		
		// Select the content view
		if(isFromMauto) {
			setContentView(layoutToInflate);
		} else {
			getLayoutInflater().inflate(layoutToInflate, viewContainer);
			disableButton(buttonToDeactivate);
		}
	}
	
	/**
	 * Disables the button depending on the menu current page
	 * @param buttonID
	 */
	protected void disableButton(int buttonID) {
		Button button = (Button) findViewById(buttonID);
		button.setBackgroundColor(Color.TRANSPARENT);
		button.setEnabled(false);
	}

}
