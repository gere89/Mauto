package it.polito.mauto.activity;

import net.sourceforge.zbar.Symbol;
import it.polito.mauto.activity.R;
import it.polito.mauto.classes.Game;
import it.polito.mauto.classes.Route;
import it.polito.mauto.zbarscanner.ZBarConstants;
import it.polito.mauto.zbarscanner.ZBarScannerActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class QRScannerActivity extends BaseFragmentActivity {
	
	private static final String regularExpression = "^[a-z]{4}\\d{1}";
	private static final int ZBAR_SCANNER_REQUEST = 0;
    private static final int ZBAR_QR_SCANNER_REQUEST = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);        
		setContentView(R.layout.activity_qrscanner);
		
		this.launchQRScanner(findViewById(R.id.layout));
	}
	
	/**
	 * Start generic scanner
	 * @param v
	 */
	public void launchScanner(View v) {
        if (isCameraAvailable()) {
            Intent intent = new Intent(this, ZBarScannerActivity.class);
            startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
        } else {
            Toast.makeText(this, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

	/**
	 * Start QRScanner
	 * @param v
	 */
    public void launchQRScanner(View v) {
        if (isCameraAvailable()) {
            Intent intent = new Intent(this, ZBarScannerActivity.class);
            intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
            startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
        } else {
            Toast.makeText(this, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Check if camera is available
     * @return {@link Boolean}
     */
    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {    
		String carID = null;
		
	    if (resultCode == RESULT_OK) {
	    	carID = data.getStringExtra(ZBarConstants.SCAN_RESULT).replaceAll("\\s","");
	    	
	    	// Check if ID has a correct format
	    	if(carID!=null && carID.matches(regularExpression)) {
	    		if(route.carBelongsToRoute(carID)) {
	    			// Set car as looked
	    			route.setCarLooked(carID);
	    			
	    			// Check if car have a game
	    			Game g = route.getCarGame(carID);
	    			
	    			if(g!=null) {
	    				// Start the game
	    				Intent game = new Intent(QRScannerActivity.this, GameActivity.class);
    					game.putExtra("carID", carID);
    					startActivity(game);
    					finish();
	    			} else {
	    				// Show the technical information
    					Intent technical = new Intent(QRScannerActivity.this, TechnicalActivity.class);
    					technical.putExtra("carID", carID);
    					startActivity(technical);
    					finish();
	    			}
	    		} else {
	    			// Check if car exist in DB
	    			if(route.isCarExisistent(carID)) {
	    				// Show technical information
	    				Intent technical = new Intent(QRScannerActivity.this, TechnicalActivity.class);
    					technical.putExtra("carID", carID);
    					technical.putExtra("from_route", false);
    					startActivity(technical);
    					finish();
	    			} else {
	    				// Get a feedback to user because scanned code not belong to MautoApp
	    				Toast toast = Toast.makeText(this, "Il QR non identifica un\'auto del museo", Toast.LENGTH_SHORT);
	    				toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
	    				toast.show();
	    				
	    				// Reload the activity with a short dealy for get exhaust the toast message
	    				new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								finish();
			    				startActivity(getIntent());
							}
						}, 2000);
	    			}
	    		}
		    }
	    }  
	}

}
