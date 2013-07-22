package it.polito.mauto.activity;

import it.polito.mauto.activity.R;
import it.polito.mauto.classes.MautoFunc;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MautoActivity extends BaseActivity {
	
	private static String NELMUSEO_NAME = "nelmuseo";
	private static String GARAGE_NAME = "garage";
	private static String COLLEZIONE_NAME = "collezione";
	private static String BONUS_NAME = "bonus";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mauto);

		this.setButtonFunction(NELMUSEO_NAME, MainActivity.class);
		this.setButtonFunction(GARAGE_NAME, GarageActivity.class);
		this.setButtonFunction(COLLEZIONE_NAME, CarListActivity.class);
		this.setButtonFunction(BONUS_NAME, BonusActivity.class);
	}
	

	/**
	 * Set button functionality
	 * @param buttonName
	 * @param activity
	 */
	private void setButtonFunction(final String buttonName, final Class<?> activity) {
		
		ImageButton button = (ImageButton) findViewById(MautoFunc.getLayoutResourceIDbyName(this, "layout_mauto_btn_"+buttonName));
		
		button.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MautoActivity.this, activity);
				intent.putExtra("from_mauto", true);
				startActivity(intent);
				
				if(buttonName.compareToIgnoreCase(NELMUSEO_NAME)==0) {
					finish();
				}
			}
		});
	}	

}
