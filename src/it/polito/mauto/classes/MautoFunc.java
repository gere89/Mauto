package it.polito.mauto.classes;

import it.polito.mauto.activity.R;
import it.polito.mauto.activity.R.drawable;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class MautoFunc {
	
	// Declare some constant
	public static final String DB_PATH = "database/";
	public static final String PARTS_PATH = "parts/";
	public static final String CARS_PATH = "cars/";
	public static final String DB_GAMES = "DBgames.xml";
	public static final String DB_PARTS = "DBparts.xml";
	public static final String DB_CARS = "DBcars.xml";
	public static final String DB_BONUS = "DBbonus.xml";
	
	// Strings list
	public static final String DIALOG_BUTTON_TEXT_POSITIVE = "SI";
	public static final String DIALOG_BUTTON_TEXT_NEGATIVE = "NO";
	public static final String DIALOG_TITLE_RESET = "Conferma reset";
	public static final String DIALOG_MESSAGE_RESET = "Il reset comporta la perdita dei progressi ottenuti fin'ora. Continuare?";
	public static final String DIALOG_TITLE_EXIT = "Conferma uscita";
	public static final String DIALOG_MESSAGE_EXIT = "Stai per uscire dall'applicazione. Continuare?";
	public static final String DIALOG_TITLE_BACKTOMENU = "Conferma torna al menu";
	public static final String DIALOG_MESSAGE_BACKTOMENU = "Stai per tornare al menu principale. Confermi?";
	

	/**
	 * Retrive dynamically ID of specified resurce from drawable folder
	 * Sript from: http://goo.gl/DMQ5R
	 * @param name
	 * @return int
	 */
	public static int getDrawableResourceIDbyName(String name) {
		try {
		    Class<drawable> res = R.drawable.class;
		    Field field = res.getField(name);
		    return field.getInt(null);
		}
		catch (Exception e) {
			Log.e( "MautoFunc" , "Failure to get drawable id.", e);
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * Retrive dinamically ID of specified element in layout XML file by name
	 * @param context
	 * @param name
	 * @return int
	 */
	public static int getLayoutResourceIDbyName(Context context, String name) {
		return context.getResources().getIdentifier(name, "id", context.getPackageName());
	}
	
	
	/**
	 * Check if the display orientation is portrait
	 * @return boolean
	 */
	public static boolean isPortraitDisplay(Resources resources) {
		if(resources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
			return true;
		return false;
	}
	
	
	/**
	 * Subdivide a string by the dimension of the container view
	 * @param text
	 * @param width
	 * @return ArrayList<String>
	 */
	public static ArrayList<String> subtexter(String text, int viewWidth, Paint p) {
		ArrayList<String> subtext = new ArrayList<String>();
		
		char[] c = text.toCharArray();
		String temp = "";
		
		for(int i=0; i<c.length; i++){
			temp = temp+c[i];
			if(p.measureText(temp) >= viewWidth){
				subtext.add(temp.trim());
				temp = "";
			}
		}
		subtext.add(temp.trim());
		
		return subtext;
	}
	
	
	/**
	 * Get the display size of the device
	 * @param context
	 * @return Point
	 */
	public static Point getDisplaySize(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		
		if( isAndroidVersionHigherThan22() ) {
			//display.getSize(size);
		} else {
			size.x = display.getWidth(); 
			size.y = display.getHeight();
		}
				
		return size;
	}
	
	
	/**
	 * Return true if Android OS version is higher than 2.2
	 * @return boolean
	 */
	public static boolean isAndroidVersionHigherThan22() {
		
		String androidOS = Build.VERSION.RELEASE;
		
		if (androidOS.startsWith("2.2")) {
	        return false;
		} else {
	        return true;
	    }
	}
	
}
