package it.polito.mauto.games.view;

import java.io.InputStream;

import it.polito.mauto.activity.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class PowerPlugView extends RelativeLayout {
		
	private ImageView car, socket, background;
	private ViewGroup container;
	
	private Bitmap bmp;
	private Drawable d;
	private Point currentPoint, initialPoint;	
	
	// Listener che setta i parametri della presa al tocco
	private OnTouchListener otl = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			switch (event.getActionMasked()) {
				case MotionEvent.ACTION_DOWN:
					// Setto le coordinate del punto iniziale
					initialPoint = new Point((int) event.getX(), (int) event.getY());
					break;
					
				case MotionEvent.ACTION_MOVE:
					// Resetto il background all'originale
					resetBackground();
				    
					// Prendo le coordinate in cui mi trovo
					currentPoint.x = (int) event.getX();
					currentPoint.y = (int) event.getY();

//					// Setto la posizione della spina
//					state = new LayoutParams(50, 50);
//					left = (int) (background.getWidth() - (background.getWidth() - event.getX()));
//					top = (int) (background.getHeight() - (background.getHeight() - event.getY()));
//					state.setMargins(left, top, 0, 0);
//					plug.setLayoutParams(state);
					
//					// Calcolo l'angolo di rotazione della spina
//					initPoint = new Point((int)initialPoint_x, (int)initialPoint_y);
//					currPoint = new Point((int)currentPoint_x, (int)currentPoint_y);
					
					// Disegno il cavo
					drawPlug();
					break;
					
				case MotionEvent.ACTION_UP:
					// Resetto il background all'originale
					resetBackground();
					
					// Controllo se ho collegato bene la presa...
					
					break;
				default:
					break;
				}
			return true;
			
		}
	};
	
	public PowerPlugView(Context context) {
		super(context);
		
		// Preparo i componenti grafici
		View.inflate(getContext(), R.layout.layout_powerplug, this);
		
		// Inizializzo i parametri
		initialPoint = new Point(0, 0); // Punto di partenza
		currentPoint = new Point(0, 0); // Punto in cui sono con il dito

		// Prelevo le componenti grafiche del file xml
		car = (ImageView) findViewById(R.id.img_powerplug_car);
		socket = (ImageView) findViewById(R.id.img_powerplug_socket);
		background= (ImageView) findViewById(R.id.img_powerplug_background);
		
		// Prelevo il contenitore di tutto
		container = (ViewGroup) findViewById(R.id.rlt_powerplug_container);
		container.setOnTouchListener(otl); // Aggiungo il listener al container
	
		// Setto l'elemento su cui disegnerà la presa
		d = getResources().getDrawable(R.drawable.game_tech_powerplug_background);
		resetBackground();
	}
	
	
	private void resetBackground() {
	    // Ne creo la bitmap
		bmp = ((BitmapDrawable)d).getBitmap();
		// Setto i parametri dell'ImageView
	    background.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	    // Setto la bitmap come sfondo dell'ImageView
	    background.setImageBitmap(bmp);
	}
	
	
	public void drawPlug() {
		bmp = Bitmap.createBitmap(background.getWidth(), background.getHeight(), Config.ARGB_8888);
	    Canvas c = new Canvas(bmp);
	    background.draw(c);
	    
	    // Setto lo stile del cavo della spina elettrica
	    Paint pnt = new Paint();
	    pnt.setColor(Color.BLACK);
	    pnt.setStrokeWidth(4);
	    pnt.setStrokeCap(Paint.Cap.ROUND);
	    
	    // Disegno il cavo
	    c.drawLine(initialPoint.x, initialPoint.y, currentPoint.x, currentPoint.y, pnt);
	    
	    // Carico l'immagine della spina
	    Bitmap plugImg = BitmapFactory.decodeResource(getResources(), R.drawable.game_tech_powerplug_plug);
	    // Traslo l'immagine l'immagine
	    Matrix matrix = new Matrix();
	    int scaleX = 15,
	    	scaleY = 15;
	    matrix.setScale(scaleX, scaleY);
	    matrix.setTranslate(currentPoint.x-scaleX/2, currentPoint.y-scaleY/2);
	    
	    // Disegno la spina
	    c.drawBitmap(plugImg, matrix, null);
	    
	    background.setImageBitmap(bmp);
	}
	
}
