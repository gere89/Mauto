package it.polito.mauto.activity;

import java.io.IOException;

import it.polito.mauto.activity.R;
import it.polito.mauto.classes.Car;
import it.polito.mauto.classes.MautoFunc;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class RouteActivity extends BaseFragmentActivity {
	
	private Car carStep;
	private String routeCategory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLayoutInflater().inflate(R.layout.layout_route, viewContainer);
		disableButton(R.id.layout_menu_btn_route);
		
		// Get the step to show
		carStep = null;
		carStep = route.getRouteStep();
		if(carStep == null) {
			// The route is finished
		} else {
			// Show the step
			TextView txt_route_carPosition = (TextView) findViewById(R.id.txt_route_carPosition);
			TextView txt_route_carModel = (TextView) findViewById(R.id.txt_route_carModel);
			ImageView imgCar = (ImageView) findViewById(R.id.img_route_carPhoto);

			
			// Set elements
			txt_route_carModel.setText(carStep.getModel());
			txt_route_carPosition.setText(carStep.getCarPosition());
			try {
				Bitmap myBitmap = BitmapFactory.decodeStream(getAssets().open(MautoFunc.CARS_PATH+carStep.getPath()+".jpg"));
				imgCar.setImageBitmap(myBitmap);
			} catch (IOException e) {
				throw new RuntimeException("Car image not found");
			}
		}
	}
}
