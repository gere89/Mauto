package it.polito.mauto.classes;

import java.io.IOException;
import java.util.ArrayList;

import it.polito.mauto.activity.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FeatureBaseAdapter extends BaseExpandableListAdapter {
	
	private Context context;
	private Route route;
	private ArrayList<Part> parts;
	
	public FeatureBaseAdapter(Context context, ArrayList<Part> parts) {
		this.context = context;
		this.parts = parts;
		this.route = (Route) context.getApplicationContext();
		
		// Remove the extra parts from the list
		for(int i=0; i<parts.size(); i++) {
			if(parts.get(i).getPartID().contains("extra")) {
				this.parts.remove(i);
			}
		}
			
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return parts.get(groupPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		Part p = parts.get(groupPosition);
		
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.layout_garage_feature_item, null);
		}
		
		// Get view elements
		TextView label = (TextView) convertView.findViewById(R.id.layout_garage_feature_txt_label);
		ImageView addRemoveButton = (ImageView) convertView.findViewById(R.id.layout_garage_feature_btn_add);
		
		// Set background color
		if(route.getMyCustomCarParts().contains(p)) {
			convertView.setBackgroundResource(R.color.smoke);
		} else {
			convertView.setBackgroundResource(R.color.grey);
		}
		
		// Set text label with desscription
		label.setText(""+p.getDescription());

		return convertView;		
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return parts;
	}

	@Override
	public int getGroupCount() {
		return parts.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		Part p = parts.get(groupPosition);
		
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.layout_common_list_item, null);
		}
		// Set the content description of the view elements for onCLick operation (see GarageActivity for more details)
		convertView.setContentDescription(""+p.getPartID());
		
		// Get view elements
		TextView label = (TextView) convertView.findViewById(R.id.txt_common_item_label);
		ImageView icon = (ImageView) convertView.findViewById(R.id.img_common_item_icon);
		ImageView lockIcon = (ImageView) convertView.findViewById(R.id.img_common_item_lock);
		
		// Set background color
		if(route.getMyCustomCarParts().contains(p)) {
			convertView.setBackgroundResource(R.color.smoke);
		} else {
			convertView.setBackgroundResource(R.color.grey);
		}
		
		// Set text description
		label.setText(p.getName());
		label.setTextColor(Color.BLACK);
		
		// Set icon of part
		Bitmap b = null;
		try {
			b = BitmapFactory.decodeStream(context.getAssets().open(MautoFunc.PARTS_PATH+p.getPath()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		icon.setImageBitmap(b);
		RelativeLayout.LayoutParams iconLayoutParams = new RelativeLayout.LayoutParams(35, 35);
		iconLayoutParams.setMargins(40, 0, 0, 0);
		icon.setLayoutParams(iconLayoutParams);
		
		// Set lock icon visibility
		if(!p.isLocked()) {
			lockIcon.setVisibility(View.INVISIBLE);
		} else {
			lockIcon.setVisibility(View.VISIBLE);
		}
			
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
