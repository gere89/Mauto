package it.polito.mauto.classes;

import it.polito.mauto.activity.CarListActivity;
import it.polito.mauto.activity.GarageActivity;
import it.polito.mauto.activity.QRScannerActivity;
import it.polito.mauto.activity.R;
import it.polito.mauto.activity.RouteActivity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuFragment extends Fragment {
	
	private Route route;
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		route = (Route) getActivity().getApplicationContext();
		view = new View(getActivity().getBaseContext());
		
		// Select menu color by route category choosed
		if(route.getRouteCategory() != null) {
			if (route.getRouteCategory().compareToIgnoreCase("Gare e sfide") == 0) {
				view = inflater.inflate(R.layout.menu_fragment_red, container, false);
			}
			else if (route.getRouteCategory().compareToIgnoreCase("Tecnologia e design") == 0) {
				Log.d("Test", "menu blu");
				view = inflater.inflate(R.layout.menu_fragment_blu, container, false);
			}
			else if (route.getRouteCategory().compareToIgnoreCase("Personaggi famosi e lusso") == 0){
				view = inflater.inflate(R.layout.menu_fragment_yellow, container, false);
			}
			else if (route.getRouteCategory().compareToIgnoreCase("Primati e storia") == 0){
				view = inflater.inflate(R.layout.menu_fragment_green, container, false);
			}
				
			// Set up button onclick action
			setButtonFunction("route", RouteActivity.class);
			setButtonFunction("qrcode", QRScannerActivity.class);
			setButtonFunction("carlist", CarListActivity.class);
			setButtonFunction("garage", GarageActivity.class);
			//setButtonFunction("bonus", GarageActivity.class);
		}
		return view;
	}
	
	/**
	 * Set up the menu button
	 * @param buttonName
	 * @param activity
	 */
	private void setButtonFunction(String buttonName, final Class<?> activity) {

		Button button = (Button) view.findViewById(MautoFunc.getLayoutResourceIDbyName(view.getContext(), "layout_menu_btn_"+buttonName));
		
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(view.getContext(), activity);
//				intent.putExtra("from_mauto", true);
				startActivity(intent);
			}
		});
	}

}
