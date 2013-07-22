package it.polito.mauto.activity;

import it.polito.mauto.classes.Bonus;
import it.polito.mauto.classes.Route;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SchedaActivity extends Activity {

	private Route route;
	private Bonus b;
	int position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scheda);
		
		route = (Route) getApplicationContext();
		
		Intent bonusIntent = getIntent();
		position = Integer.parseInt(bonusIntent.getStringExtra("IDplace"));
		
		
		b = route.getBonusByArrayIndex(position);
		
		TextView txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText(b.getPlace());
		
		
		TextView txt_address = (TextView) findViewById(R.id.txt_address);
		txt_address.setText(b.getAddress());
		
		
		TextView txt_description = (TextView) findViewById(R.id.txt_description);
		txt_description.setText(b.getDescription());

	}
}
