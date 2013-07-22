package it.polito.mauto.activity;

import java.io.IOException;

import it.polito.mauto.activity.R;
import it.polito.mauto.classes.Car;
import it.polito.mauto.classes.MautoFunc;
import it.polito.mauto.classes.Route;
import it.polito.mauto.classes.ScrollTextView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TechnicalActivity extends BaseFragmentActivity {
	
	private Route route;
	private Car carToShow;
	private boolean gamePerformed = false,
					gameWellDone = false;
	private String gameID = null,
				   carID = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Inserisco la vista della scheda tecnica all'interno container
		getLayoutInflater().inflate(R.layout.layout_technical, viewContainer);
		
		// Prendo l'application (può esistere una sola application per ogni app)
		route = (Route) getApplicationContext();
		
		// Controllo gli extras
		this.checkExstras();
		// Salvo le informazione del gioco terminato
		this.saveGameInfo();
		// Carico la vettura da mostrare in scheda tecnica
		this.loadCarToShow();
		// Setto tutti gli elementi grafici
		this.setViewElements();
	}
	
	
	private void setViewElements() {
		// Sistemo tutti gli elementi della vista
		if(carToShow == null) {
		}
		else{
			TextView txt_technical_carModel = (TextView) findViewById(R.id.txt_technical_carModel);
			TextView txt_technical_carMaker = (TextView) findViewById(R.id.txt_technical_carMaker);
			TextView txt_technical_carYear = (TextView) findViewById(R.id.txt_technical_carYear);
			TextView txt_technical_carDescription = (TextView) findViewById(R.id.txt_technical_carDescription);
			//ScrollTextView txt_technical_carDescription=(ScrollTextView) findViewById(R.id.txt_technical_carDescription);
			TextView txt_technical_carPosition = (TextView) findViewById(R.id.txt_technical_carPosition);
			ImageView img_technical_carPhoto = (ImageView) findViewById(R.id.img_technical_carPhoto);
			
			// Setto gli elementi con le informazioni della vettura
			txt_technical_carModel.setText(carToShow.getModel());
			txt_technical_carMaker.setText(carToShow.getCarmaker());
			txt_technical_carYear.setText(""+carToShow.getYear());
			txt_technical_carPosition.setText(carToShow.getCarPosition());
			txt_technical_carDescription.setText(""+carToShow.getDescription());
			//txt_technical_carDescription.startScroll();
			txt_technical_carDescription.setMovementMethod(new ScrollingMovementMethod());
			
			try {
				Bitmap myBitmap = BitmapFactory.decodeStream(getAssets().open(MautoFunc.CARS_PATH+carToShow.getPath()+".jpg"));
				img_technical_carPhoto.setImageBitmap(myBitmap);
			} catch (IOException e) {
				throw new RuntimeException("Car image not found");
			}
		}
		
//		// Setto il bottone nextStep
//		Button btn_technical_nextStep = (Button) findViewById(R.id.btn_technical_nextStep);
//		btn_technical_nextStep.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent nextStep = new Intent(TechnicalActivity.this, RouteActivity.class);
//				startActivity(nextStep);
//				finish();
//			}
//		});
	}

	
	private void checkExstras() {
		// Controllo se vi sono extras
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
			// Scheda tecnica dopo un gioco
			gamePerformed 	= extras.getBoolean("gamePerformed");
			gameWellDone 	= extras.getBoolean("gameWellDone");
			gameID			= extras.getString("gameID");
			// Solo scheda tecnica
			carID 			= extras.getString("carID");
		}
	}
	
	
	private void loadCarToShow() {
		if(carID != null) {
			try {
				carToShow = route.getCarByID(carID);
			} catch (Exception e) {
				Log.w("TechnicalActivity", e.toString());
				throw new RuntimeException(e);
			}
		}
	}
	
	
	private void saveGameInfo() {
		if(gamePerformed) {
			// Setto il gioco come performed
			route.setGamePerformed(gameID);
			
			if(gameWellDone) {
				// Setto il gioco come WellDone (vinto)
				route.setGameWellDone(gameID);
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_technical, menu);
		return true;
	}

}
