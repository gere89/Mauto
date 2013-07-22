package it.polito.mauto.activity;

import java.io.IOException;

import it.polito.mauto.activity.R;
import it.polito.mauto.classes.Car;
import it.polito.mauto.classes.HorizontialListView;
import it.polito.mauto.classes.MautoFunc;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CarListActivity extends BaseFragmentActivity {
	
	private ImageButton listButton, galleryButton;
	private RelativeLayout carScroll;	
	private BaseAdapter baList, baGallery;
	private HorizontialListView hlv;
	private ListView lw;
	private ImageView lockImg, carImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkOriginFromIntent(R.layout.layout_carlist, R.id.layout_menu_btn_carlist);
		
		carScroll = (RelativeLayout) findViewById(R.id.layout_common_list_car);
		listButton = (ImageButton) findViewById(R.id.layout_carlist_btn_listview);
		galleryButton = (ImageButton) findViewById(R.id.layout_carlist_btn_galleryview);
		
		// Initialize list
		lw = new ListView(this);
		hlv = new HorizontialListView(this);
		
		// Crete the adapters
		baList = new BaseAdapter() {
			@Override
			public View getView(int position, View recycledView, ViewGroup parent) {
				if (recycledView == null) {
					recycledView = LayoutInflater.from(CarListActivity.this).inflate(R.layout.layout_carlist_list_item, null);
				}
					
				Car c = route.getCarByArrayIndex(position, isFromMauto);
				TextView carName = (TextView) recycledView.findViewById(R.id.layout_carlist_txt_name);
				lockImg = (ImageView) recycledView.findViewById(R.id.layout_carlist_list_img_Lock);
				
				// Set text view
				carName.setText(c.getModel());
				// Set lock image
				if (!c.isLooked()) lockImg.setVisibility(View.VISIBLE); 
				else lockImg.setVisibility(View.INVISIBLE);

				return recycledView;
			}
			
			@Override
			public long getItemId(int position) {
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				return route.getCarByArrayIndex(position, isFromMauto);
			}
			
			@Override
			public int getCount() {
				return route.getNumberOfCar(isFromMauto);
			}
		};
		
		baGallery = new BaseAdapter() {
			@Override
			public View getView(int position, View recycledView, ViewGroup parent) {
				if (recycledView == null) {
					recycledView = LayoutInflater.from(CarListActivity.this).inflate(R.layout.layout_carlist_gallery_item, null);
				}
					
				Car c = route.getCarByArrayIndex(position, isFromMauto);
				TextView carName = (TextView) recycledView.findViewById(R.id.layout_carlist_gallery_text);
				lockImg = (ImageView) recycledView.findViewById(R.id.layout_carlist_gallery_img_Lock);
				lockImg.setAlpha(250);
				carImg = (ImageView) recycledView.findViewById(R.id.layout_carlist_gallery_image);
				
				
				// Set text view
				carName.setText(c.getModel());
				
				// Set car image
				if (!c.isLooked()) lockImg.setVisibility(View.VISIBLE); 
				else lockImg.setVisibility(View.INVISIBLE);
				
				try {
					Bitmap myBitmap = BitmapFactory.decodeStream(getAssets().open(MautoFunc.CARS_PATH+c.getPath()+".jpg"));
					carImg.setImageBitmap(myBitmap);
				} catch (IOException e) {
					throw new RuntimeException("Car image not found");
				}

				return recycledView;
				
				
			}
			
			@Override
			public long getItemId(int position) {
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				return route.getCarByArrayIndex(position, isFromMauto);
			}
			
			@Override
			public int getCount() {
				return route.getNumberOfCar(isFromMauto);
			}
		};
		
		// Set the list with the base adapter
		lw.setAdapter(baList);
		lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> adView, View arg1, int position, long arg3) {
				Adapter a = adView.getAdapter();
				Car item = (Car) a.getItem(position);
				
				if(!item.isLooked()) {
					Intent technical = new Intent(CarListActivity.this, TechnicalActivity.class);
					technical.putExtra("carID", item.getID());
					startActivity(technical);
					finish();
				}
			}
		});
		
		hlv.setAdapter(baGallery);
		hlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> adView, View arg1, int position, long arg3) {
				Adapter a = adView.getAdapter();
				Car item = (Car) a.getItem(position);
				
				if(!item.isLooked()) {
					Intent technical = new Intent(CarListActivity.this, TechnicalActivity.class);
					technical.putExtra("carID", item.getID());
					startActivity(technical);
					finish();
				}
			}
		});
		
		// Set button action
		listButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				carScroll.removeAllViews();
				carScroll.addView(lw);
			}
		});
				
		galleryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				carScroll.removeAllViews();
				carScroll.addView(hlv);
			}
		});
		
		// Set the default view
		carScroll.addView(lw);
		
	}
	
}
