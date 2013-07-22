package it.polito.mauto.activity;

import it.polito.mauto.classes.Bonus;
import it.polito.mauto.classes.GPSTracker;
import it.polito.mauto.classes.Route;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MapsActivity extends FragmentActivity implements OnMyLocationChangeListener, OnInfoWindowClickListener{

private GPSTracker gps;
private GoogleMap googleMap;
private Route route;
private Bonus b;
private int position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		
		route = (Route) getApplicationContext();
		
		Intent bonusIntent = getIntent();
		position = Integer.parseInt(bonusIntent.getStringExtra("IDplace"));
		
		b = route.getBonusByArrayIndex(position);
		
		// Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
 
        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
 
        }else {    // Google Play Services are available
 
            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fgt_maps_map_container);
 
            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();
 
            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);
            
            // Setting event handler for location change
            googleMap.setOnMyLocationChangeListener(this);
            
          	 
   	        // Zoom in the Google Map
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(b.getLat(), b.getLon())));
	        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            
            
	        Marker marker = googleMap.addMarker(new MarkerOptions()
	            .position(new LatLng(b.getLat(), b.getLon()))
	            .title(b.getPlace())
	            .snippet(b.getAddress())
	            .draggable(false)
	            .icon(BitmapDescriptorFactory
	                    .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            
            marker.showInfoWindow();
            
            googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

                public void onInfoWindowClick(Marker marker) {
                       Intent intent = new Intent(MapsActivity.this, SchedaActivity.class);
                       intent.putExtra("IDplace", "" + position);
                       startActivity(intent);
                }
        });

            
            
            googleMap.setOnMapClickListener(new OnMapClickListener() {
            	//quando clicco sulla mappa, lui va al punto di destinazione
                @Override
                public void onMapClick(LatLng point) {
                	googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(b.getLat(), b.getLon())));
        	        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                }
            });
            
            Btn_Check();
            
            Btn_Naviga();
            
        }
	}
	
	
	//avvicina la telecamera alla posizione corrente
	 @Override
	    public void onMyLocationChange(Location location) {
	 
//	        // Getting latitude of the current location
//	        double latitude = location.getLatitude();
//	 
//	        // Getting longitude of the current location
//	        double longitude = location.getLongitude();
//	 
//	        // Creating a LatLng object for the current location
//	        LatLng latLng = new LatLng(latitude, longitude);
//	 
//	        // Showing the current location in Google Map
//	        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//	 
//	        // Zoom in the Google Map
//	        googleMap.animateCamera(CameraUpdateFactory.zoomTo(25));

	    }
	 
	 
	 public void Btn_Check(){
		 Button btn_check = (Button) findViewById(R.id.btn_maps_check);
		 btn_check.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View v) {
	        		
	        		gps = new GPSTracker(MapsActivity.this);
	        		// check if GPS enabled		
			        if(gps.canGetLocation() && gps.isGPSEnabled){
//		        		Intent intent = new Intent(MapsActivity.this, MapsActivity.class);
//						startActivity(intent);
//			
						int index = check_position(gps.getLatitude(), gps.getLongitude());
						
						if(index != -1){	
					    	Toast.makeText(getApplicationContext(), "Congratulazioni! Sei arrivato a " + route.getBonusByArrayIndex(index).getPlace(), Toast.LENGTH_LONG).show();
					    	//sblocca pezzo
						}else{
							Toast.makeText(getApplicationContext(), "Mi dispiace! Non hai ancora raggiunto un luogo prescelto", Toast.LENGTH_LONG).show();
						}
						
						//finish();
			        }else{
			        	// can't get location
			        	// GPS or Network is not enabled
			        	// Ask user to enable GPS/network in settings
			        	gps.showSettingsAlert();
			        }
	        	}
			});
	 }
	 
	 
	 public void Sblocca_Pezzo(){
		 String ID_pezzo = b.getpartID();
		 
		 
	 }
	 
	 
	 //--------------------------------------------------------------------------------------------
	 
	 public void Btn_Naviga(){
		 final Context context = this;
	 Button navigation = (Button) findViewById(R.id.btn_maps_navigator);
			navigation.setOnClickListener(new View.OnClickListener() {
	     	public void onClick(View v) {
	     		
	     		//Dialog di conferma
	     		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
	    			// set title
	    			alertDialogBuilder.setTitle("Accesso al Navigatore");
	     
	    			// set dialog message
	    			alertDialogBuilder
	    				.setMessage("Vuoi che ti conduca fino a " + b.getPlace() + "?")
	    				.setCancelable(false)
	    				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
	    					public void onClick(DialogInterface dialog,int id) {
	    						Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+ b.getLat() +","+ b.getLon())); 
	    			     		startActivity(i);
	    					}
	    				  })
	    				.setNegativeButton("No",new DialogInterface.OnClickListener() {
	    					public void onClick(DialogInterface dialog,int id) {
	    						// if this button is clicked, just close
	    						// the dialog box and do nothing
	    						dialog.cancel();
	    					}
	    				});
	     
	    				// create alert dialog
	    				AlertDialog alertDialog = alertDialogBuilder.create();
	     
	    				// show it
	    				alertDialog.show();
	    			}
		});
	 }
		
	//-------------------------------------------------------------------------------------------------
	 
	 public int check_position(double user_lat, double user_lon){
			//La latitudine deve essere compresa tra lat4 e lat1--> 	lat4 < user_lat < lat1
			//la longitudine deve essere compresa tra lat3 e lat2--> 	lon3 < user_lon < lon2
				
			for(int i=0; i<route.getNumberOfBonus(); i++){
				if((user_lat > route.getBonusByArrayIndex(i).getLat4() && user_lat < route.getBonusByArrayIndex(i).getLat1()) && (user_lon > route.getBonusByArrayIndex(i).getLon3() && user_lat < route.getBonusByArrayIndex(i).getLon2())) {
					return i;
				}
			}
			
			return -1;
		}


	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub
		
	}
	 



}
