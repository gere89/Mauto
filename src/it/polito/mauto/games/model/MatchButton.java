package it.polito.mauto.games.model;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

public class MatchButton {
	
	private int topY, leftX, rightX, bottomY;
	private TextView textHold;
	private String associated;
	private boolean checked;
	
	public MatchButton(TextView textHold) {
		this.textHold = textHold;
		topY = textHold.getTop();
		leftX = textHold.getLeft();
		rightX = textHold.getRight();
		bottomY = textHold.getBottom();
		checked = false;
	}
	
	public void setParameters(View selected_item) {
		Drawable temp = selected_item.getBackground();
		TextView itemDropped = (TextView) selected_item;
		associated = ""+itemDropped.getText();
		textHold.setBackgroundDrawable(temp);
		textHold.bringToFront();
		textHold.setVisibility(View.INVISIBLE);
	}
	
	public String getText(){
		return ""+textHold.getText();
	}
	
	public boolean checkClick(float crashX, float crashY) {
		if (crashX > leftX && crashX < rightX && crashY > topY && crashY < bottomY) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isChecked(){
		return checked;
	}
	
	public void check(){
		checked = true;
	}
	
}
