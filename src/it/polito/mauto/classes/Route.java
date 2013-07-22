package it.polito.mauto.classes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import it.polito.mauto.games.model.MatchGame;
import it.polito.mauto.games.model.QuizGame;
import android.R.integer;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

public class Route extends Application {

	private DBConnect dbc = new DBConnect();
	
	// Global storage section
	private ArrayList<Car> carsList;
	private ArrayList<Game> gamesList;
	private ArrayList<Part> partsList;
	private ArrayList<Bonus> bonusList;
	private MyCustomCar mycar;
	
	// Part storage section
	private ArrayList<String> partsCategories;
	
	// Game storage section
	private int tempGameIndex = -1;
	
	// Car storage section
	private int tempGlobalCarIndex = -1;
	
	// Route storage section
	private ArrayList<Car> carsOfRouteList;
	private int[] counterRace = {0, 0};
	private int[] counterTech = {0, 0};
	private int[] counterHist = {0, 0};
	private int[] counterFams = {0, 0};
	private int tempCarIndex = -1;
	private String routeCategory = null;

	// Constant declaration
	public static final String PREFKEY_CARS_LIST = "cars_list";
	public static final String PREFKEY_GAMES_LIST = "games_list";
	public static final String PREFKEY_PARTS_LIST = "parts_list";
	public static final String PREFKEY_PREFS_NAME = "mauto_preferences";
	public static final String PREFKEY_MYCAR_PARTS = "mycar_parts";
	public static final String PREFKEY_PREFS_DELIMITER = ",";
	public static final String BONUS_LIST = "bonus_list";
	
	
	//////////////////// INITIALIZE AND LOAD DB /////////////////////
	
	/**
	 * Initialize ArrayList
	 */
	public void initialize() {
		// Initialize variables
		carsOfRouteList = new ArrayList<Car>();
		carsList = new ArrayList<Car>();
		gamesList = new ArrayList<Game>();
		partsList = new ArrayList<Part>();
		bonusList = new ArrayList<Bonus>();
		partsCategories = new ArrayList<String>();
		mycar = new MyCustomCar();
	}
	
	/**
	 * Load cars from database
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void loadFirstPartDB() throws IOException, ParserConfigurationException, SAXException  {		
		// Load DB
		carsList = dbc.loadCars(this);
		partsList = dbc.loadParts(this);
		bonusList = dbc.loadBonus(this);
		
		SharedPreferences settings = getSharedPreferences(Route.PREFKEY_PREFS_NAME, 0);
		//SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(Route.PREFKEY_MYCAR_PARTS);
        // Commit the edits!
        editor.commit();
		
		this.assembleMyCar();
		Log.w("MyCar", ""+mycar.getParts().size());
		Log.w("Prefs", ""+this.readPreference(PREFKEY_MYCAR_PARTS));
	}
	
	/**
	 * Load the other data from database
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void loadSecondPartDB() throws IOException, ParserConfigurationException, SAXException {
		// Load DB
		gamesList = dbc.loadGames(this, carsList);
	}	
	
	
	/**
	 * Assemble mycar from default parts or saved parts in preference
	 */
	private void assembleMyCar() {
		if(mycar != null) {
			// If preference are empty I'll fill mycar with default parts
			if(this.readPreference(PREFKEY_MYCAR_PARTS) == null) {
				Iterator<Part> partsIterator = partsList.iterator();
				
				while(partsIterator.hasNext()) {
					Part p = partsIterator.next();
					
					if(p.getPartID().contains("_default")) {
						mycar.setPart(p);
						this.saveMyCarPreference(p.getPartID());
					}
				}
			} else {
				// Else fill mycar with parts that are saved in preference
				String prefs = this.readPreference(PREFKEY_MYCAR_PARTS);
				
				if(prefs == null) {
					throw new RuntimeException("assembleMyCar - Errors occurs while reading preferences");
				}
				
				// Split prefs string and put each carID into a Set
				Set<String> preferences = null;
				try {
					preferences = new HashSet<String>(Arrays.asList(prefs.split(Route.PREFKEY_PREFS_DELIMITER)));
				} catch(Exception e) {
					throw new RuntimeException("assembleMyCar - Some errors occurs during conversion from string array to set");
				}
				
				if(preferences != null && preferences.size() > 0) {
					if(partsList != null && partsList.size() > 0) {
						
						for (int i = 0; i < partsList.size(); i++) {
							// Restore preferences
							if(preferences.contains(partsList.get(i).getPartID())) {
								mycar.setPart(partsList.get(i));
							}
						}
					} else {
						throw new RuntimeException("assembleMyCar - There are no parts in database");
					}
				}
				
				
			}
		}
	}

	//////////////////// BONUS /////////////////////
	
	/**
	 * Get bonus at specified position in array
	 * @param index
	 * @return {@link Bonus}
	 */
	public Bonus getBonusByArrayIndex(int index) {
		return bonusList.get(index);
	}
	
	/**
	 * Get the number of total bonus
	 * @return {@link integer}
	 */
	public int getNumberOfBonus() {
		if(bonusList != null) {
			return bonusList.size(); 
		} else {
			throw new RuntimeException("getNumberOfBonus - "+Messages.getString("Error.DATABASE_EMPTY"));
		}
	}
	
	
	//////////////////// CAR /////////////////////
	
	/**
	 * Return car by carID
	 * @param carID
	 * @return {@link Car}
	 */
	public Car getCarByID(String carID) {
		if(carsList != null && carsList.size()>0) {
			if(tempGlobalCarIndex>-1 && carsList.get(tempGlobalCarIndex).getID().compareToIgnoreCase(carID)==0) {
				Car c = carsList.get(tempGlobalCarIndex); 
				tempGlobalCarIndex = -1;
				return c;
			} else {
				Iterator<Car> carsIterator = carsList.iterator();
				
				while(carsIterator.hasNext()) {
					Car c = carsIterator.next();
					
					if(c.getID().compareToIgnoreCase(carID)==0) {
						return c;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Check if a car is present in DB
	 * @param carID
	 * @return {@link Boolean}
	 */
	public boolean isCarExisistent(String carID) {
		if(carsList != null && carsList.size()>0) {
			Iterator<Car> carsIterator = carsList.iterator();
			int count = -1;
			
			while(carsIterator.hasNext()) {
				count++;
				if(carsIterator.next().getID().compareTo(carID)==0) {
					tempGlobalCarIndex = count;
					return true;
				}
			}
		} else {
			throw new RuntimeException("isCarExisistent - There were problems during the search of the car");
		}
		return false;
	}
	
	
	/**
	 * Set car as looked and save it in shared preferences
	 * @param carID
	 */
	public void setCarLooked(String carID) {
		if(carsOfRouteList != null && carsOfRouteList.size()>0) {
			if(tempCarIndex>-1 && carsOfRouteList.get(tempCarIndex).getID().compareToIgnoreCase(carID)==0) {
				carsOfRouteList.get(tempCarIndex).setAsLooked();
				savePreference(carID, Route.PREFKEY_CARS_LIST);
			} else {
				for (int i = 0; i < carsOfRouteList.size(); i++) {
					if(carsOfRouteList.get(i).getID().compareTo(carID)==0) {
						carsOfRouteList.get(i).setAsLooked();
						savePreference(carsOfRouteList.get(i).getID(), Route.PREFKEY_CARS_LIST);
						break;
					}
				}
			}
			tempCarIndex = -1;
		} else {
			throw new RuntimeException("setCarLooked - There were problems during the search of the car");
		}
	}
	
	//////////////////// MY CUSTOM CAR /////////////////////
	
	/**
	 * Save the parts of my custom car
	 * @param stringToAppend
	 */
	public void saveMyCarPreference(String stringToAppend) {

		// Get the preferences saved on mobile device on the key MYCAR_PARTS
		SharedPreferences settings = getSharedPreferences(Route.PREFKEY_PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		String preferences = settings.getString(PREFKEY_MYCAR_PARTS, null);
		
		if(preferences != null) {
			boolean flag = false;
			
			// Get all the parts of my car
			String tmp[] = preferences.split(Route.PREFKEY_PREFS_DELIMITER);
			// Loop all the parts of my car
			for (int i = 0; i < tmp.length; i++) {
				// Get categories of part of my car and new part to add
				String carPartCategory = this.getPartByID(tmp[i]).getCategory();
				String partToAddCategory = this.getPartByID(stringToAppend).getCategory();
				// Check if the two parts have the same category
				if(carPartCategory.compareToIgnoreCase(partToAddCategory)==0) {
					// Override the currents car parts with the new
					tmp[i] = stringToAppend;
					flag = true;
					break;
				}
			}
			
			// Save parts with the new one
			if(flag) {
				StringBuilder sb = new StringBuilder();	
				// Compose a unique string with all parts of my car delimited by a comma
				for (String p : tmp) {
				    if(sb.length() > 0) {
				        sb.append(Route.PREFKEY_PREFS_DELIMITER);
				    }
				    sb.append(p);
				}
				
				// Save the unique string into shared preferences
		        editor.putString(PREFKEY_MYCAR_PARTS, sb.toString());
		        editor.commit();
			} else {
				StringBuilder sb = new StringBuilder(preferences);			
				// Append the string
				sb.append(Route.PREFKEY_PREFS_DELIMITER+stringToAppend);
				// Save it
		        editor.putString(PREFKEY_MYCAR_PARTS, sb.toString());
		        editor.commit();
			}
		} else {
			editor.putString(PREFKEY_MYCAR_PARTS, stringToAppend);
			editor.commit();
		}
	}
	
	/**
	 * Return mycar
	 * @return {@link MyCustomCar}
	 */
	public MyCustomCar getMyCustomCar() {
		return mycar;
	}
	
	/**
	 * Return mycar parts
	 * @return {@link ArrayList<Part>}
	 */
	public ArrayList<Part> getMyCustomCarParts() {
		return mycar.getParts();
	}
	
	//////////////////// GLOBAL /////////////////////
	/**
	 * Reset temporary variables and lists
	 * @return {@link Boolean}
	 */
	public boolean clearLists() {
		boolean flag = false;
		if(carsOfRouteList != null && carsOfRouteList.size()>0) {
			carsOfRouteList.clear();
			flag = true;
		}
		tempCarIndex = -1;
		tempGameIndex = -1;
		tempGlobalCarIndex = -1;
		
		return flag;
	}
	
	
	/**
	 * Save the preference
	 * @param stringToAppend
	 * @param key
	 */
	public void savePreference(String stringToAppend, String key) {
		
		SharedPreferences settings = getSharedPreferences(Route.PREFKEY_PREFS_NAME, 0);
		//SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		SharedPreferences.Editor editor = settings.edit();
		String preferences = settings.getString(key, null);
		
		if(preferences != null) {		
			StringBuilder sb = new StringBuilder(preferences);			
			// Append the string
			sb.append(Route.PREFKEY_PREFS_DELIMITER+stringToAppend);
			// Save it
	        editor.putString(key, sb.toString());
	        editor.commit();
		} else {
			editor.putString(key, stringToAppend);
			editor.commit();
		}
	}
	
	
	/**
	 * Read the preference
	 * @param key
	 * @return {@link String}
	 */
	public String readPreference(String key) {
		SharedPreferences prefs = getSharedPreferences(Route.PREFKEY_PREFS_NAME, 0);
		return prefs.getString(key, null);
	}
	
	//////////////////// GAMES /////////////////////
	/**
	 * Return game by carID
	 * @param carID
	 * @return {@link Game}
	 */
	public Game getCarGame(String carID) {
		if(gamesList != null && gamesList.size()>0) {
			Iterator<Game> gamesIterator = gamesList.iterator();
			int index = -1;
			
			while(gamesIterator.hasNext()) {
				index++;
				Game g = gamesIterator.next();
				if(g.getCarID().compareTo(carID) == 0) {
					tempGameIndex = index;
					return g;
				}
			}
		} else {
			throw new RuntimeException("getCarGame - There were problems during the search of the game");
		}
		return null;
	}
	
	
	/**
	 * Restore preference to gamesList
	 * @param prefs
	 */
	public void compareGamesAndPreference(String prefs) {
		
		if(prefs == null) {
			return;
		}
		
		// Split prefs string and put each carID into a Set
		Set<String> preferences = null;
		try {
			preferences = new HashSet<String>(Arrays.asList(prefs.split(Route.PREFKEY_PREFS_DELIMITER)));
		} catch(Exception e) {
			throw new RuntimeException("compareGamesAndPreference - Some errors occurs during conversion from string array to set");
		}
		
		if(preferences != null && preferences.size() > 0) {
			if(gamesList != null && gamesList.size() > 0) {
				
				for (int i = 0; i < gamesList.size(); i++) {
					// Restore preferences
					if(preferences.contains(gamesList.get(i).getGameID())) {
						gamesList.get(i).setPerformed();
					}
				}
			} else {
				throw new RuntimeException("compareGamesAndPreference - There are no games in database");
			}
		}
	}
	
	/**
	 * Set game like performed
	 * @param gameID
	 */
	public void setGamePerformed(String gameID) {
		if(tempGameIndex != -1 && gamesList.get(tempGameIndex).getGameID().compareTo(gameID)==0) {
			gamesList.get(tempGameIndex).setPerformed();
		} else {
			for(int i=0; i<gamesList.size(); i++) {
				if(gamesList.get(i).getGameID().compareTo(gameID)==0) {
					gamesList.get(i).setPerformed();
					break;
				}
			}
		}
	}
	
	/**
	 * Set game like exceeded
	 * @param gameID
	 */
	public void setGameWellDone(String gameID) {
		if(tempGameIndex != -1 && gamesList.get(tempGameIndex).getGameID().compareTo(gameID)==0) {
			gamesList.get(tempGameIndex).setWellDone();
		} else {
			for(int i=0; i<gamesList.size(); i++) {
				if(gamesList.get(i).getGameID().compareTo(gameID)==0) {
					gamesList.get(i).setWellDone();
					break;
				}
			}
		}
	}
	
	//////////////////// CONTESTUAL GAMES /////////////////////
	/**
	 * Return quizgame class
	 * @param carID
	 * @return {@link QuizGame}
	 * @throws IOException
	 * @throws SAXException
	 */
	public QuizGame getQuiz(String carID) throws IOException, SAXException {
		if(tempGameIndex != -1 && gamesList.get(tempGameIndex).getCarID().compareTo(carID)==0) {
			return dbc.loadQuiz(this, gamesList.get(tempGameIndex)); 
		} else {
			for(Game g : gamesList) {
				if(g.getCarID().compareTo(carID)==0) {
					return dbc.loadQuiz(this, g);
				}
			}
		}
		return null;
	}
	
	/**
	 * Return matchgames class
	 * @param carID
	 * @return {@link MatchGame}
	 * @throws IOException
	 * @throws SAXException
	 */
	public MatchGame getMatch(String carID) throws IOException, SAXException {
		if(tempGameIndex != -1 && gamesList.get(tempGameIndex).getCarID().compareTo(carID)==0) {
			return dbc.loadMatch(this, gamesList.get(tempGameIndex)); 
		} else {
			for(Game g : gamesList) {
				if(g.getCarID().compareTo(carID)==0) {
					return dbc.loadMatch(this, g);
				}
			}
		}
		return null;
	}
	
	
	//////////////////// ROUTE /////////////////////
	
	/**
	 * Return the current routeCategory
	 * @return {@link String}
	 */
	public String getRouteCategory() {
		return routeCategory;
	}
	
	/**
	 * Load all cars of a specific route's category
	 * @param routeCategory
	 * @return {@link ArrayList}
	 */
	public void loadCarsOfRoute(String routeCategory) {
		if(carsList != null && carsList.size() > 0) {
			this.routeCategory = routeCategory;
			Iterator<Car> carsIterator = carsList.iterator();
			
			while(carsIterator.hasNext()) {
				Car c = carsIterator.next();
				
				if(c.getCategory().compareTo(routeCategory) == 0) {
					carsOfRouteList.add(c);
				}
			}
			
			if(carsOfRouteList.size() < 0) {
				throw new RuntimeException("There aren't cars in category: "+routeCategory);
			}
		} else {
			throw new RuntimeException("loadCarsOfRoute - There are no cars in database");
		}
	}
	
	
	/**
	 * Set as looked the cars contained in shared preference
	 * @param preferences
	 */
	public void compareCarsAndPreference(String prefs) {
		
		if(prefs == null) {
			return;
		}
		
		// Split prefs string and put each carID into a Set
		Set<String> preferences = null;
		try {
			preferences = new HashSet<String>(Arrays.asList(prefs.split(Route.PREFKEY_PREFS_DELIMITER)));
		} catch(Exception e) {
			throw new RuntimeException("compareCarsAndPreference - Some errors occurs during conversion from string array to set");
		}
		
		if(preferences != null && preferences.size() > 0) {
			if(carsList != null && carsList.size() > 0) {
				
				for (int i = 0; i < carsList.size(); i++) {
					// Restore preferences
					if(preferences.contains(carsList.get(i).getID())) {
						carsList.get(i).setAsLooked();
					}
					
					// Count how many cars there are for each category and how many of this are looked
					if(carsList.get(i).getCategory().compareTo("Gare e sfide")==0) {
						counterRace[0]++;
						if(carsList.get(i).isLooked()) {
							counterRace[1]++;
						}
					} else if(carsList.get(i).getCategory().compareTo("Tecnologia e design")==0) {
						counterTech[0]++;
						if(carsList.get(i).isLooked()) {
							counterTech[1]++;
						}
					} else if(carsList.get(i).getCategory().compareTo("Personaggi famosi e lusso")==0) {
						counterFams[0]++;
						if(carsList.get(i).isLooked()) {
							counterFams[1]++;
						}
					} else if(carsList.get(i).getCategory().compareTo("Primati e storia")==0) {
						counterHist[0]++;
						if(carsList.get(i).isLooked()) {
							counterHist[1]++;
						}
					}
					
				}
			} else {
				throw new RuntimeException("compareCarsAndPreference - There are no cars in database");
			}
		}
	}
	
	/**
	 * Get the next step of the route
	 * @return {@link Car}
	 */
	public Car getRouteStep() {
		Iterator<Car> carsIterator = carsOfRouteList.iterator();
		
		boolean notLookedFlag = true;
		int carCount = -1;
		int carLastIndex = -1;
		int carFirstIndex = -1;
		
		/* ************************** LEGEND ***************************
		 * notlookedFlag: is true only if no one car of route is looked
		 * carCount: store index of the loop
		 * carLastIndex: store index of last looked car 
		 * carFirstIndex: store index of first car not looked
		 */
		
		while(carsIterator.hasNext()) {
			Car c = carsIterator.next();
			
			carCount++;
			if(c.isLooked()) {
				notLookedFlag = false;
				carLastIndex = carCount;
			} else {
				if(carFirstIndex == -1) {
					carFirstIndex = carCount;
				}
			}
		}
		
		if(notLookedFlag == true) {
			// The route is just started
			return carsOfRouteList.get(0);
		} else if(carFirstIndex == -1) {
			// The route is completed
			return null;
		} else if(carFirstIndex != -1 && carsOfRouteList.get(carsOfRouteList.size()-1).isLooked()) {
			// The last car of route is looked but there are other not lloked yet
			// Return the first car not looked yet
			return carsOfRouteList.get(carFirstIndex);
		} else {
			// Return the first car not looked next to the last car looked
			return carsOfRouteList.get(carLastIndex+1);
		}		
	}
	
	/**
	 * Return the counter corresponding the slug
	 * @param counterSlug
	 * @return int[]
	 */
	public int[] getCounterBySlug(String counterSlug) {
		if(counterSlug.compareToIgnoreCase("race")==0) {
			return counterRace;
		} else if(counterSlug.compareToIgnoreCase("tech")==0) {
			return counterTech;
		} else if(counterSlug.compareToIgnoreCase("hist")==0) {
			return counterHist;
		} else {
			return counterFams;
		}		
	}
	
	/**
	 * Check if car belong to route
	 * @param carID
	 * @return {@link Boolean}
	 */
	public boolean carBelongsToRoute(String carID) {
		if(carsOfRouteList != null && carsOfRouteList.size()>0) {
			Iterator<Car> carsOfRouteIterator = carsOfRouteList.iterator();
			int index = -1;
			while(carsOfRouteIterator.hasNext()) {
				index++;
				Car c = carsOfRouteIterator.next();
				if(c.getID().compareTo(carID) == 0) {
					tempCarIndex = index;
					return true;
				}
			}
		} else {
			throw new RuntimeException("carBelongsToRoute - There were problems during the search of the car");
		}
		return false;
	}
	
	
	/**
	 * Get a car from carList or CarsOfRouteList by index
	 * @param index
	 * @param isFromMauto
	 * @return {@link Car}
	 */
	public Car getCarByArrayIndex(int index, boolean isFromMauto) {
		if(!isFromMauto) {
			if(carsOfRouteList != null) {
				return carsOfRouteList.get(index); 
			} else {
				throw new RuntimeException("getCarByArrayIndex - There are no cars in route");
			}
		} else {
			if(carsList != null) {
				return carsList.get(index);
			} else {
				throw new RuntimeException("getCarByArrayIndex - There are no cars in DB");
			}
		}
	}
	
	/**
	 * Get the number of cars of carsList or carsOfRouteList
	 * @param isInRoute
	 * @return {@link integer}
	 */
	public int getNumberOfCar(boolean isFromMauto) {
		if(!isFromMauto) {
			if(carsOfRouteList != null) {
				return carsOfRouteList.size(); 
			} else {
				throw new RuntimeException("getNumberOfCar - There are no cars in route");
			}
		} else {
			if(carsList != null) {
				return carsList.size();
			} else {
				throw new RuntimeException("getNumberOfCar - There are no cars in DB");
			}
		}
	}
	
	//////////////////// CAR PARTS /////////////////////
	/**
	 * Restore parts preference
	 * @param prefs
	 */
	public void comparePartsAndPreference(String prefs) {
		
		if(prefs == null) {
			return;
		}
		
		// Split prefs string and put each carID into a Set
		Set<String> preferences = null;
		try {
			preferences = new HashSet<String>(Arrays.asList(prefs.split(Route.PREFKEY_PREFS_DELIMITER)));
		} catch(Exception e) {
			throw new RuntimeException("comparePartsAndPreference - Some errors occurs during conversion from string array to set");
		}
		
		if(preferences != null && preferences.size() > 0) {
			if(partsList != null && partsList.size() > 0) {
				
				for (int i = 0; i < partsList.size(); i++) {
					// Restore preferences
					if(preferences.contains(partsList.get(i).getPartID())) {
						partsList.get(i).unlock();
					}
				}
			} else {
				throw new RuntimeException("comparePartsAndPreference - There are no parts in database");
			}
		}
	}
	
	/**
	 * Set parts categories
	 * @param categories
	 */
	public void setPartsCategories(ArrayList<String> categories) {
		if(categories != null && categories.size()>0) {
			partsCategories = categories;
		} else {
			throw new RuntimeException("setPartsCategories - Categories wasn't founded in DB");
		}
	}
	
	/**
	 * Return the number of parts categories
	 * @return {@link ArrayList}
	 */
	public int getNumberOfPartsCategories() {
		if(partsCategories != null && partsCategories.size()>0) {
			return partsCategories.size();
		} else {
			throw new RuntimeException("getNumberOfPartsCategories - Categories wasn't founded in DB");
		}	
	}
	
	/**
	 * Return category by index
	 * @param index
	 * @return {@link ArrayList}
	 */
	public String getPartCategoryByIndex(int index) {
		if(partsCategories != null && partsCategories.size()>0) {
			return partsCategories.get(index);
		} else {
			throw new RuntimeException("getPartCategoryByIndex - Categories wasn't founded in DB");
		}
	}
	
	
	/**
	 * Return all parts of a specified category
	 * @param category
	 * @return {@link ArrayList<Part>}
	 */
	public ArrayList<Part> getPartsByCategory(String category) {
		if(partsList != null && partsList.size()>0) {
			Iterator<Part> partsIter = partsList.iterator();
			ArrayList<Part> parts = new ArrayList<Part>();
			
			while(partsIter.hasNext()) {
				Part p = partsIter.next();
				if(p.getCategory().compareTo(category) == 0) {
					parts.add(p);
				}
			}
			return parts;
		} else {
			throw new RuntimeException("getPartsByCategory - Parts wasn't founded in DB");
		}		
	}
	

	/**
	 * Return part by passed ID
	 * @param partID
	 * @return {@link Part}
	 */
	public Part getPartByID(String partID) {
		if(partsList != null && partsList.size()>0) {
			Iterator<Part> partsIter = partsList.iterator();
			
			while(partsIter.hasNext()) {
				Part p = partsIter.next();
				
				if(p.getPartID().compareTo(partID) == 0) {
					return p;
				}
			}
		} else {
			throw new RuntimeException("getPartByID - Parts wasn't founded in DB");
		}
		return null;
	}
	
}
