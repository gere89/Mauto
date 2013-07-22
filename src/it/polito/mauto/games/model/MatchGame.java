package it.polito.mauto.games.model;

import it.polito.mauto.classes.Game;

import java.util.ArrayList;

import android.util.Log;

public class MatchGame extends Game{
	ArrayList<MatchAnswer> dropList;
	ArrayList<MatchAnswer> holdList;

	public MatchGame(String carID, String gameID, String name, String category, String gameType, 
			ArrayList<MatchAnswer> dropList, ArrayList<MatchAnswer> holdList) {
		super(carID, gameID, name, category, gameType);
		this.dropList = dropList;
		this.holdList = holdList;
		randomAnswers();
	}
	
	
	public MatchAnswer getDrop(int index) {
		return dropList.get(index);
	}
	
	public MatchAnswer getHold(int index) {
		return holdList.get(index);
	}
	
	public int getIdDrop(String name){
		for(int i=0;i<4;i++){
			if(dropList.get(i).getName().compareTo(name)==0){
				return dropList.get(i).getMatchID();
			}
		}
		return -1;
	}
	
	public int getIdHold(String name){
		for(int i=0;i<4;i++){
			if(holdList.get(i).getName().compareTo(name)==0){
				return holdList.get(i).getMatchID();
			}
		}
		return -1;
	}
	
	public void randomAnswers() {
		ArrayList<MatchAnswer> temp = new ArrayList<MatchAnswer>();
		int[] position = {-1,-1,-1,-1};
		boolean find = true;
		
		for(int i=0;i<4;i++) {
			find = true;
			while(find) {
				int random = (int) (Math.random()*100)%4;
				find = false;
				for(int k=0;k<4;k++) {
					if(random == position[k])
						find = true;
				}
				
				if(!find) {
					position[i] = random;
					
				}
			}
		}
		
		for(int i=0;i<4;i++) {
			temp.add(holdList.get(i));
		}
		
		for(int i=0;i<4;i++) {
			Log.w("position", ""+position[i]);
			temp.set(position[i], holdList.get(i));
		}
		
		for(int i=0;i<4;i++) {
			holdList.set(i, temp.get(i));			
		}
	}

}
