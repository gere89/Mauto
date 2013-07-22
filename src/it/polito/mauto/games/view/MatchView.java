package it.polito.mauto.games.view;

import it.polito.mauto.activity.R;
import it.polito.mauto.activity.TechnicalActivity;
import it.polito.mauto.games.model.MatchButton;
import it.polito.mauto.games.model.MatchGame;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MatchView extends RelativeLayout implements OnTouchListener  {
	
	private TextView[] txt;
	private MatchButton[] bm;
	private View selected_item;
	private LayoutParams imageParams;
	private MatchGame matchText;
	private int[][] idAssociated;
	private int countAssociation, offset_x = 0, offset_y = 0, crashX, crashY;
	private boolean touchFlag = false;
	private boolean[] buttonChecked = {false, false, false, false};
	
	
	public MatchView(Context context, MatchGame m) {
		super(context);
        this.matchText = m;
        
        // Faccio l'inflate della view e prendo il contenitore di tutto
		View.inflate(context, R.layout.layout_match, this);
		ViewGroup container = (ViewGroup) findViewById(R.id.container);
		
		// Preparo tutta la grafica
		this.setupView(matchText);
		
		// Preparo i flag e le variabili che mi serviranno
		bm = new MatchButton[4];
		idAssociated = new int[4][2];
		countAssociation = 0;
		
		
		container.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if (touchFlag == true) {
					switch (event.getActionMasked()) {
					
					case MotionEvent.ACTION_DOWN:
						for(int i=0; i<bm.length; i++) {
							bm[i] = new MatchButton(txt[i+4]);
						}
						break;
						
					case MotionEvent.ACTION_MOVE:
						crashX = (int) event.getX();
						crashY = (int) event.getY();

						int x = (int) event.getX() - offset_x;
						int y = (int) event.getY() - offset_y;
						
						RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
								new ViewGroup.MarginLayoutParams(
										RelativeLayout.LayoutParams.MATCH_PARENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT
								)
						);
						
						lp.setMargins(x, y, 0, 0);
						
						// Drop Image Here
						selected_item.setLayoutParams(lp);
						break;
						
					case MotionEvent.ACTION_UP:
						touchFlag = false;
						
						for(int i=0; i<4; i++) {
							if (bm[i].checkClick(crashX, crashY) && !buttonChecked[i]) {
								bm[i].setParameters(selected_item);
								buttonChecked[i] = true;
								selected_item.setVisibility(View.INVISIBLE);
								idAssociated[countAssociation][0] = matchText.getIdHold(bm[i].getText());
								TextView item = (TextView) selected_item;
								idAssociated[countAssociation][1] = matchText.getIdDrop(""+item.getText());
								countAssociation++;
								
								if(countAssociation == 4) {
									boolean correct = true;

									// Controllo se tutte le associazioni sono corrette
									for(int j=0; j<4; j++) {
										if(idAssociated[j][0] != idAssociated[j][1]) {
											correct = false;
											break;
										}
									}
									
									// Preparo l'intent
									Intent matchIntent = new Intent(getContext(), TechnicalActivity.class);
									matchIntent.putExtra("gameID", matchText.getGameID());
									matchIntent.putExtra("gamePerformed", true);
									// Preparo un messaggio di feedback per l'utente
									Toast message = null;
									
									// Mi comporto in maniera diversa in base alla correttezza del gioco
									if(correct) {
										message = Toast.makeText(getContext(),"Hai vinto!",Toast.LENGTH_SHORT);     			
					        			matchIntent.putExtra("gameWellDone", true);
									} else {
										message = Toast.makeText(getContext(),"Hai sbagliato!",Toast.LENGTH_SHORT);
					        			matchIntent.putExtra("gameWellDone", false);
									}
									
									// Lancio l'intent e mostro il messaggio
									message.show();
									getContext().startActivity(matchIntent);
									((Activity) getContext()).finish();
								}
							} else {
								selected_item.setLayoutParams(imageParams);
							}
						}
						
						break;
					default:
						break;
					}
				}
				return true;
			}
		});

	}

	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getActionMasked()) {
		
		// Pressione del dito sullo schermo touch
		case MotionEvent.ACTION_DOWN:
			touchFlag = true;
			offset_x = (int) event.getX();
			offset_y = (int) event.getY();
			selected_item = v;
			imageParams = (LayoutParams) v.getLayoutParams();
			break;
		
		// Distacco del dito dallo schermo
		case MotionEvent.ACTION_UP:
			selected_item = null;
			touchFlag = false;
			break;
		default:
			break;
		}
		return false;
	}
	
	
	/**
	 * Setto i parametri di tutti gli elementi della vista
	 */
	private void setupView(MatchGame m) {
		this.txt = findAllTextView();
		ImageView arrow = (ImageView) findViewById(R.id.arrow);
		
		// Calcolo quanto spazio lasciare tra una text e l'altra
		int displayHeight = getResources().getDisplayMetrics().heightPixels;
		int displayWidth = getResources().getDisplayMetrics().widthPixels;
        int marginBottomElement = (int) displayHeight/10;
		
		// Preparo i LayoutParameter che mi servviranno
		RelativeLayout.LayoutParams lpText = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams lpImage = new RelativeLayout.LayoutParams(
				displayWidth, 
				marginBottomElement);
        
		// Setto i parametri per ogni TextView
		for(int i=0; i<=txt.length; i++) {
			lpText = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT, 
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			if(i==4) {
				lpImage.setMargins(0,marginBottomElement*i,0,0);
				arrow.setLayoutParams(lpImage);
			} else if(i<4) {
				lpText.setMargins(0, marginBottomElement*i, 0, 0);
				txt[i].setLayoutParams(lpText);
				txt[i].setText(m.getDrop(i).getName());
				txt[i].setOnTouchListener(this);
			} else if(i>4) {
				lpText.setMargins(0, marginBottomElement*(i), 0, 0);
				txt[i-1].setLayoutParams(lpText);
				txt[i-1].setText(m.getHold(i-5).getName());
			} else {
				Log.w("MatchView.setupView", "Problemi nel setup degli elementi");
			}
		}
		
	}
	
	
	/**
	 * Prendo tutti gli elementi TextView dal layout
	 * @return View[]
	 */
	private TextView[] findAllTextView() {
		TextView[] tv = new TextView[8];
		
		tv[0] = (TextView) findViewById(R.id.textDrop1);
		tv[1] = (TextView) findViewById(R.id.textDrop2);
		tv[2] = (TextView) findViewById(R.id.textDrop3);
		tv[3] = (TextView) findViewById(R.id.textDrop4);
		tv[4] = (TextView) findViewById(R.id.textHold1);
		tv[5] = (TextView) findViewById(R.id.textHold2);
		tv[6] = (TextView) findViewById(R.id.textHold3);
		tv[7] = (TextView) findViewById(R.id.textHold4);
		
		return tv;
	}
	
	public int getCountAssociation() {
		return countAssociation;
	}
	
	public int[][] getIdAssociated() {
		return idAssociated;
	}
	
	public ViewGroup getContainer() {
		return (ViewGroup) findViewById(R.id.container);
	}
	
}
