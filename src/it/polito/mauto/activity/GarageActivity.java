package it.polito.mauto.activity;

import it.polito.mauto.activity.R;
import it.polito.mauto.classes.FeatureBaseAdapter;
import it.polito.mauto.classes.GarageBaseAdapter;
import it.polito.mauto.classes.Messages;
import it.polito.mauto.classes.MyCustomCar;
import it.polito.mauto.classes.Parameter;
import it.polito.mauto.classes.Part;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GarageActivity extends BaseFragmentActivity {

	private MyCustomCar mycar;
	private RelativeLayout workshopContainer;
	private ImageView imgMyCar, imgGarageGate;
	private int lastExpandedGroupPosition = -1, level = -1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);	
        checkOriginFromIntent(R.layout.layout_garage, R.id.layout_menu_btn_garage);
        
        // Get mycar
        this.mycar = route.getMyCustomCar();
        
        // Generate view with params list
        this.setupParamsList(null);
        
        // Get the view components from xml layout
        imgMyCar = (ImageView) findViewById(R.id.layout_garage_img_mycustomcar);
        imgGarageGate = (ImageView) findViewById(R.id.layout_garage_img_gate);
        workshopContainer = (RelativeLayout) findViewById(R.id.layout_garage_workshop);

        workshopContainer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				ListView lw = new ListView(getBaseContext());
				lw.setAdapter(new GarageBaseAdapter(getBaseContext(), route.getMyCustomCarParts()));
				workshopContainer.removeAllViewsInLayout();
				workshopContainer.addView(lw);
				
				lw.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View viewSelected,	int arg2, long arg3) {
						final ExpandableListView elw = new ExpandableListView(getBaseContext());
						elw.setAdapter(new FeatureBaseAdapter(getBaseContext(), route.getPartsByCategory(viewSelected.getContentDescription().toString())));
						workshopContainer.removeAllViewsInLayout();
						workshopContainer.addView(elw);
						
						elw.setOnGroupClickListener(new OnGroupClickListener() {
							@Override
							public boolean onGroupClick(ExpandableListView arg0, View itemSelected, int arg2, long arg3) {
								Part p = route.getPartByID(""+itemSelected.getContentDescription());
								
								// Show the new performance
								if(route.getMyCustomCarParts().contains(p)) {
									setupParamsList(null);
								} else {
									setupParamsList(p);
								}
								
								// Lock expand of element not unlocked in route
								if(p.isLocked()) {
									return true;
								} else {
									return false;
								}
							}
						});
						
						elw.setOnGroupExpandListener(new OnGroupExpandListener() {
							@Override
						    public void onGroupExpand(int groupPosition){
						        //collapse the old expanded group, if not the same as new group to expand
						        if(groupPosition != lastExpandedGroupPosition){
						            elw.collapseGroup(lastExpandedGroupPosition);
						        }

						        lastExpandedGroupPosition = groupPosition;
						    }
						});
						
					}
				});
			}
		});
	}
    
    
    /**
     * Setup the params list of mycar
     */
    private void setupParamsList(Part part) {
    	// Get the view components from xml layout
        LinearLayout paramsListView = (LinearLayout) findViewById(R.id.layout_garage_list_params);
        TextView paramLabel = null;
    	ProgressBar paramValue = null;
    	
    	// Clean list of params
    	paramsListView.removeAllViewsInLayout();
    	
    	
    	// DOUBLE PROGRESS
    	if(part != null) {
    		 // Add one paramView for each mycar's param
            for(Parameter p : mycar.getPerformance()) {
            	// Get the item view elements
            	View paramsListItemView = getLayoutInflater().inflate(R.layout.layout_garage_list_item, null);
            	paramLabel = (TextView) paramsListItemView.findViewById(R.id.layout_garage_param_label);
            	paramValue = (ProgressBar) paramsListItemView.findViewById(R.id.layout_garage_param_value);
            	
            	// Set the value
            	paramLabel.setText(p.getName());
            	paramValue.setProgress(p.getValue());
            	
            	// Set secondary progress
            	for(Parameter param : part.getPerformance()) {
            		if(param.getName().compareTo(p.getName())==0) {
                		paramValue.setSecondaryProgress(p.getValue()+param.getValue());
                		break;
                	}
            	}
            	
            	// Put the item
            	paramsListView.addView(paramsListItemView);
            }
    	} 
    	// SINGLE PROGRESS
    	else {
            // Add one paramView for each mycar's param
            for(Parameter p : mycar.getPerformance()) {
            	// Get the item view elements
            	View paramsListItemView = getLayoutInflater().inflate(R.layout.layout_garage_list_item, null);
            	paramLabel = (TextView) paramsListItemView.findViewById(R.id.layout_garage_param_label);
            	paramValue = (ProgressBar) paramsListItemView.findViewById(R.id.layout_garage_param_value);
            	
            	// Set the value
            	paramLabel.setText(p.getName());
            	paramValue.setProgress(p.getValue());
            	
            	// Put the item
            	paramsListView.addView(paramsListItemView);
            }
    	}
    }
    
//    @Override
//	public void onBackPressed() {
//		if (!isFromMauto) {
//			
//			// setDialog(Messages.getString("DialogBox.TITLE_BACKTOMENU"), Messages.getString("DialogBox.MESSAGE_BACKTOMENU")).show();
//		} else {
//			if(level == 1)
//			super.onBackPressed();
//		}
//	}
    
}
