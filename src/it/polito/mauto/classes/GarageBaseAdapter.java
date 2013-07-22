package it.polito.mauto.classes;

import it.polito.mauto.activity.R;

import java.io.IOException;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GarageBaseAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<Part> parts;
	
	public GarageBaseAdapter(Context context, ArrayList<Part> parts) {
		// Setup main variables
		this.context = context;
		this.parts = parts;
	}

	
	public View getView( int position, View recycledView, ViewGroup parent ) {
		Part p = parts.get(position);
		
		if (recycledView == null) {
			recycledView = LayoutInflater.from(context).inflate(R.layout.layout_common_list_item, null);
		}
		recycledView.setBackgroundResource(R.color.grey);
		recycledView.setContentDescription(p.getCategory());
		
		TextView label = (TextView) recycledView.findViewById(R.id.txt_common_item_label);
		ImageView icon = (ImageView) recycledView.findViewById(R.id.img_common_item_icon);
		ImageView lockIcon = (ImageView) recycledView.findViewById(R.id.img_common_item_lock);
		
		label.setText(p.getName());
		label.setTextColor(Color.BLACK);
		Bitmap b = null;
		
		try {
			b = BitmapFactory.decodeStream(context.getAssets().open(MautoFunc.PARTS_PATH+p.getPath()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		icon.setImageBitmap(b);
		
		if(!p.isLocked())
			lockIcon.setVisibility(View.INVISIBLE);
		else
			lockIcon.setVisibility(View.VISIBLE);
		
		return recycledView;
	}
	
	public long getItemId(int position) {
		return position;
	}
	
	public Object getItem(int position) {
		return parts.get(position);
	}
	
	public int getCount() {
		return parts.size();
	}

}
