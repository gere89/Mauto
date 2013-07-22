package it.polito.mauto.activity;

import java.io.IOException;

import it.polito.mauto.activity.R;
import it.polito.mauto.classes.Bonus;
import it.polito.mauto.classes.GPSTracker;
import it.polito.mauto.classes.MautoFunc;
import it.polito.mauto.classes.Part;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BonusActivity extends BaseFragmentActivity {

	private BaseAdapter baList;
	private RelativeLayout carScroll;	
	
	// GPSTracker class

	private ListView lw;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkOriginFromIntent(R.layout.layout_bonus, 0);
		
		carScroll = (RelativeLayout) findViewById(R.id.layout_common_list_car);
		lw = new ListView(this);
		baList = new BaseAdapter() {
			@Override
			public View getView(int position, View recycledView, ViewGroup parent) {
				
				Bonus b = route.getBonusByArrayIndex(position);
				Part p = route.getPartByID(b.getpartID());
				if (recycledView == null) {
					recycledView = LayoutInflater.from(BonusActivity.this).inflate(R.layout.layout_common_list_item, null);
				}
				
				ImageView iconItem = (ImageView) recycledView.findViewById(R.id.img_common_item_icon);
				ImageView lockItem = (ImageView) recycledView.findViewById(R.id.img_common_item_lock);
				TextView labelItem = (TextView) recycledView.findViewById(R.id.txt_common_item_label);
				
				Bitmap myBitmap;
				try {
					myBitmap = BitmapFactory.decodeStream(getBaseContext().getAssets().open(MautoFunc.PARTS_PATH+p.getPath()));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				iconItem.setImageBitmap(myBitmap);
				
				lockItem.setVisibility(View.INVISIBLE);
				labelItem.setText(p.getName());
				


				return recycledView;
			}
			
			@Override
			public long getItemId(int position) {
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				return route.getBonusByArrayIndex(position);
			}
			
			@Override
			public int getCount() {
				return route.getNumberOfBonus();
			}
		};
		
		lw.setAdapter(baList);
		
		lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long id) {
				//Log.w("POSITION", "" + position);
				Intent bonusIntent = new Intent(BonusActivity.this, MapsActivity.class);
				bonusIntent.putExtra("IDplace", ""+position);
				startActivity(bonusIntent);
			}
		});

		carScroll.addView(lw);
//		
//		
//		
//		TextView txt_lingotto = (TextView) findViewById(R.id.txt_lingotto);
//		txt_lingotto.setOnClickListener(new View.OnClickListener() {
//        	public void onClick(View v) {
//        		Intent intent = new Intent(BonusActivity.this, SchedaActivity.class);
//				startActivity(intent);
//				finish();
//        	}
//		});
		
	}
}
