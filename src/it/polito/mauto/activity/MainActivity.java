package it.polito.mauto.activity;

import it.polito.mauto.activity.R;
import it.polito.mauto.classes.MautoFunc;
import it.polito.mauto.classes.Route;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private Route route;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Alert Dialog iniziale
	    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			// set title
			alertDialogBuilder.setTitle("SOCIAL NETWORK");
			// set dialog message
			alertDialogBuilder
				.setMessage("Prima di iniziare, vorresti condividere le novità con i tuoi amici?")
				.setCancelable(false)
				.setPositiveButton("Si",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
		        		Intent intent = new Intent(MainActivity.this, FacebookActivity.class);
		        		intent.putExtra("post", "view_all");
						startActivity(intent);
						dialog.cancel();
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked
		        		Intent intent = new Intent(MainActivity.this, FacebookActivity.class);
		        		intent.putExtra("post", "only_post");
						startActivity(intent);
						dialog.cancel();
					}
				});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
				alertDialog.show();
		
		
		
		
		route = (Route) getApplicationContext();
		
		// Set up the route button
		this.setButtonFunction("Gare e sfide", route.getCounterBySlug("race"), "race");
		this.setButtonFunction("Tecnologia e design", route.getCounterBySlug("tech"), "tech");
		this.setButtonFunction("Personaggi famosi e lusso", route.getCounterBySlug("fams"), "fams");
		this.setButtonFunction("Primati e storia", route.getCounterBySlug("hist"), "hist");
	}
	
	
	/**
	 * Set bottom action
	 * @param btn
	 * @param routeCategory
	 */
	private void setButtonFunction(final String routeCategory, int[] counter, String slugCategory) {
		
		ImageButton routeButton = (ImageButton) findViewById(MautoFunc.getLayoutResourceIDbyName(this, "layout_route_btn_"+slugCategory));
		TextView counterLabel = (TextView) findViewById(MautoFunc.getLayoutResourceIDbyName(this, "layout_route_txt_"+slugCategory));
		//counterLabel.setText(counter[1]+" / "+counter[0]);
		// Show the textview
		
		routeButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		// Reset lists and variables
        		route.clearLists();
        		// Load cars of route selected
        		route.loadCarsOfRoute(routeCategory);
        		
        		// Start route activity with the specified category
        		Intent intent = new Intent(MainActivity.this, RouteActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_struttura, menu);
		return true;
	}
	
	*/
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MautoActivity.class);
		startActivity(intent);
		finish();
	}
	/*
	private AlertDialog setExitDialog(AlertDialog dialog) {
		dialog.setTitle("Conferma uscita");
	    dialog.setMessage("Vuoi davvero uscire dall'applicazione?");
	    
	    // Add the buttons
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "SI", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				finish();
			}
		});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		return dialog;
	}*/

}
