package it.polito.mauto.classes;

import it.polito.mauto.games.model.MatchAnswer;
import it.polito.mauto.games.model.MatchGame;
import it.polito.mauto.games.model.QuizAnswer;
import it.polito.mauto.games.model.QuizGame;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import android.content.Context;

public class DBConnect {
	
	// Element for read XML file
	private DocumentBuilderFactory factory;
	private DocumentBuilder parser;
	private Document document;
	private Element element;
	private NodeList list;
	private Node node;
	
	
	/**
	 * Dato il nome di un nodo ne restituisco il valore
	 * @param tag
	 * @return String
	 */
	private String getValue(String tag) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}
	
	
	/**
	 * 
	 * @return ArrayList<QuizAnswer>
	 * @throws NoCorrectAnswerSetException
	 */
	private ArrayList<QuizAnswer> getQuizAnswers() {
		// Creo l'ArrayList che conterra le risposte del quiz 
		ArrayList<QuizAnswer> answerList = new ArrayList<QuizAnswer>();
		// Prendo tutti i tag "risposta" del quiz
		NodeList nodes = element.getElementsByTagName("risposta");
		
		Element elm;
		Node node;
		
		// Ciclo tutti i nodi "risposta"
		for(int i=0; i<nodes.getLength(); i++) {
			// Prendo il nodo
			node = nodes.item(i);
			// Guardo se ha degli attributi
			if(node.hasAttributes()) {
				// Creo un element per leggere gli attributi
				elm = (Element) node;
				// Prendo il nodo all'interno del nodo risposta (si lo so � un casino)
				node = node.getChildNodes().item(0);
				try {
					// Setto la risposta
					answerList.add(new QuizAnswer(node.getNodeValue(), Boolean.parseBoolean(elm.getAttribute("corretta"))));
				} catch (Exception e) {
					throw new RuntimeException("Errore nel settare la risposta corretta - DBConnet.class - getQuizAnswers");
				}
			}
		}
		return answerList;
	}
	
	/**
	 * Load all cars from DB to an ArrayList<Car>
	 * @param is
	 * @return ArrayList<Car>
	 * @throws NoElementsInDBException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public ArrayList<Car> loadCars(Context context) throws IOException, ParserConfigurationException, SAXException {
		
		InputStream is = context.getAssets().open(MautoFunc.DB_PATH+MautoFunc.DB_CARS);
		
		ArrayList<Car> carsList = new ArrayList<Car>();
		
		// Preparo tutti gli oggetti necessari
		factory = DocumentBuilderFactory.newInstance();
		parser = factory.newDocumentBuilder();
		document = parser.parse(is);
		list = document.getElementsByTagName("automobile");
		
		if(list.getLength()>0) {
			
			for(int i=0; i<list.getLength(); i++) {
				node = list.item(i);
				
				if(node.getNodeType()==Node.ELEMENT_NODE) {
					// Prendo il primo elemento di tipo "automobile" del DOM
					element = (Element) node;
					
					// Aggiungo all'array_list un nuovo oggetto "Car"
					carsList.add(
						new Car(
							this.getValue("id"),
							Integer.parseInt(this.getValue("anno")), 
							this.getValue("modello"),
							this.getValue("casa"),
							this.getValue("descrizione"),
							this.getValue("categoria"),
							this.getValue("posizione"),
							this.getValue("path")
						)
					);
				}
			}
			return carsList;
		} else {
			throw new RuntimeException("There aren't element in DB");
		}
	}
	
	
	/**
	 * Load all games from DB
	 * @param context
	 * @param carsList
	 * @return {@link ArrayList}
	 * @throws SAXException
	 * @throws IOException
	 */
	public ArrayList<Game> loadGames(Context context, ArrayList<Car> carsList) throws SAXException, IOException {		
		
		if(carsList!=null && carsList.size()>0) {
			
			InputStream is = context.getAssets().open(MautoFunc.DB_PATH+MautoFunc.DB_GAMES);
			ArrayList<Game> gamesList = new ArrayList<Game>();

			// Preparo tutti gli oggetti necessari
			document = parser.parse(is);
			list = document.getElementsByTagName("gioco");
			
			if(list.getLength()>0) {
				
				for(int i=0; i<list.getLength(); i++) {
					node = list.item(i);
					
					if(node.getNodeType()==Node.ELEMENT_NODE) {
						// Prendo il primo elemento di tipo "gioco" del DOM
						element = (Element) node;
						// Aggiungo all'array_list un nuovo oggetto "Game"
						gamesList.add(
							new Game(
								this.getValue("carID"),
								this.getValue("gameID"),
								this.getValue("nome"),
								element.getAttribute("category"),
								this.getValue("tipo")
							)
						);
					}
				}
				return gamesList;
			} else {
				throw new RuntimeException("There aren't games in DB");
			}
		} else {
			throw new RuntimeException("Some errors occurs during conversion from string array to set");
		}
	}
	
	
	/**
	 * Load all parts type in array
	 * @param context
	 * @return ArrayList<Part>
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public ArrayList<Part> loadParts(Route route) throws ParserConfigurationException, SAXException, IOException {
		
		// Prepare all the necessary objects
		ArrayList<Part> partsList = new ArrayList<Part>();
		ArrayList<String> partsCategories = new ArrayList<String>();
		
		
		InputStream is = route.getApplicationContext().getAssets().open(MautoFunc.DB_PATH+MautoFunc.DB_PARTS);
		NodeList paramsList; 
		Node param;
		
		// Instantiate the objects
		factory = DocumentBuilderFactory.newInstance();
		parser = factory.newDocumentBuilder();
		document = parser.parse(is);
		list = document.getElementsByTagName("part");
		
		if(list.getLength() > 0) {
			// Iterate on all part elements of the DB
			for(int i=0; i<list.getLength(); i++) {
				node = list.item(i);
				
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					element = (Element) node;
					paramsList = element.getElementsByTagName("param");
					
					ArrayList<Parameter> partsParamsList = new ArrayList<Parameter>();
					
					// Iterate on all param elements of the node
					for(int j=0; j<paramsList.getLength(); j++) {
						param = paramsList.item(j);
						
						if(param.getNodeType() == Node.ELEMENT_NODE) {
							element = (Element) param;
							
							Parameter par = new Parameter(
								this.getValue("name"), 
								Integer.parseInt(this.getValue("value")) );
							
							// Fill part params list
							partsParamsList.add(par);
						}
					}
					
					element = (Element) node;
	
					Part p = new Part(
							this.getValue("partID"), 
							this.getValue("name"), 
							this.getValue("category"),
							this.getValue("path"),
							partsParamsList);
					
					if(p.getPartID().contains("default")) {
						p.unlock();
					}
					
					// Fill parts list
					partsList.add(p);
					
					// Fill categories list
					if(!partsCategories.contains(p.getCategory())) {
						partsCategories.add(p.getCategory());
					}
				}
			}
			
			// Set parts categories
			route.setPartsCategories(partsCategories);
			
		} else {
			throw new RuntimeException("Parts were not found in the database");
		}
		return partsList;
	}
	
	
	/**
	 * Load games of a specified category
	 * @param routeCategory
	 * @return ArrayList<Game>
	 * @throws NoCarsInCategoryException
	 * @throws NoElementsInDBException
	 * @throws IOException 
	 * @throws SAXException 
	 */ /*
	public ArrayList<Game> loadGames(Context context, String routeCategory) throws IOException, SAXException {
		
		InputStream is = context.getAssets().open(MautoFunc.DB_PATH+MautoFunc.DB_GAMES);
		
		if(carsList!=null && carsList.size()>0) {
			ArrayList<Game> gamesList = new ArrayList<Game>();

			// Preparo tutti gli oggetti necessari
			document = parser.parse(is);
			list = document.getElementsByTagName("gioco");
			
			if(list.getLength()>0) {
				
				for(int i=0; i<list.getLength(); i++) {
					node = list.item(i);
					
					if(node.getNodeType()==Node.ELEMENT_NODE) {
						// Prendo il primo elemento di tipo "gioco" del DOM
						element = (Element) node;
						
						if(element.getAttribute("category").compareTo(routeCategory)==0) {						
							// Aggiungo all'array_list un nuovo oggetto "Game"
							gamesList.add(
								new Game(
									this.getValue("carID"),
									this.getValue("gameID"),
									this.getValue("nome"),
									routeCategory,
									this.getValue("tipo")
								)
							);
						}
					}
				}
				return gamesList;
			} else {
				throw new RuntimeException("There aren't games in DB from the category: "+routeCategory);
			}
		} else {
			throw new RuntimeException("The cars were not loaded from XML file to ArrayList yet");
		}
	}
	*/
	
	
	
	
	public QuizGame loadQuiz(Context context, Game g) throws IOException, SAXException {
		
		InputStream is = context.getAssets().open(MautoFunc.DB_PATH+MautoFunc.DB_GAMES);
		
		// Preparo tutti gli oggetti necessari
		document = parser.parse(is);
		list = document.getElementsByTagName("quiz");
		
		if(list.getLength()>0) {
			
			for(int i=0; i<list.getLength(); i++) {
				node = list.item(i);
				
				if(node.getNodeType()==Node.ELEMENT_NODE) {
					// Prendo il primo elemento di tipo "gioco" del DOM
					element = (Element) node;
					
					if(element.getAttribute("gameID").compareTo(g.getGameID())==0) {						
						return new QuizGame(
								g.getCarID(), 
								g.getGameID(), 
								g.getName(), 
								g.getCategory(), 
								g.getGameType(), 
								this.getValue("domanda"),
								this.getQuizAnswers());
					}
				}
			}
			return null;
		} else {
			throw new RuntimeException("There aren't quiz for the game: "+g.getGameID()+", car: "+g.getCarID());
		}
	}
	
	public MatchGame loadMatch(Context context, Game g) throws IOException, SAXException {
		
		InputStream is = context.getAssets().open(MautoFunc.DB_PATH+MautoFunc.DB_GAMES);
		
		// Preparo tutti gli oggetti necessari
		document = parser.parse(is);
		list = document.getElementsByTagName("match");
		
		if(list.getLength()>0) {
			
			for(int i=0; i<list.getLength(); i++) {
				node = list.item(i);
				
				if(node.getNodeType()==Node.ELEMENT_NODE) {
					// Prendo il primo elemento di tipo "gioco" del DOM
					element = (Element) node;
					
					if(element.getAttribute("gameID").compareTo(g.getGameID())==0) {						
						return new MatchGame(
								g.getCarID(), 
								g.getGameID(), 
								g.getName(), 
								g.getCategory(), 
								g.getGameType(), 
								this.getMatch("drop"),
								this.getMatch("hold"));
					}
				}
			}
			return null;
		} else {
			throw new RuntimeException("There aren't quiz for the game: "+g.getGameID()+", car: "+g.getCarID());
		}
	}
	
	private ArrayList<MatchAnswer> getMatch(String tag) {
		// Creo l'ArrayList che conterra le risposte del quiz 
		ArrayList<MatchAnswer> matchList = new ArrayList<MatchAnswer>();
		// Prendo tutti i tag "risposta" del quiz
		NodeList nodes = element.getElementsByTagName(tag);
		
		Element elm;
		Node node;
		
		// Ciclo tutti i nodi tag
		for(int i=0; i<nodes.getLength(); i++) {
			// Prendo il nodo
			node = nodes.item(i);
			// Guardo se ha degli attributi
			if(node.hasAttributes()) {
				// Creo un element per leggere gli attributi
				elm = (Element) node;
				// Prendo il nodo all'interno del nodo tag (si lo so � un casino)
				node = node.getChildNodes().item(0);
				try {
					// Setto la risposta
					matchList.add(new MatchAnswer(Integer.parseInt(elm.getAttribute("id")),node.getNodeValue()));
				} catch (Exception e) {
					throw new RuntimeException("Errore nel settare la risposta corretta - DBConnet.class - getQuizAnswers");
				}
			}
		}
		return matchList;
	}
	
	
	/**
	 * Fill an arry with all the parts category
	 * @return ArrayList<String>
	 */ /*
	public ArrayList<String> fillPartsCategoryList() {
		ArrayList<String> partsCategory = new ArrayList<String>();
		Iterator<Part> partsIter = carPartsList.iterator();
		String category = "";
		boolean firstCat = true;
		int count = 0;
		
		while(partsIter.hasNext()) {
			category = partsIter.next().getCategory();
			
			if(firstCat) {
				partsCategory.add(category);
				firstCat = false;
			} else {
				for(int i=0; i<partsCategory.size(); i++) {
					if(partsCategory.get(i).compareTo(category) == 0) {
						count++;
					}
				}
				
				if(count == 0) {
					partsCategory.add(category);
				}
				count = 0;
			}
		}
		return partsCategory;
	} 
	
	public IndexedSet<String> getPartsCategoriesList() {
		return partsCategoriesList;
	}

	public IndexedSet<String> getCarParametersList() {
		return carParametersList;
	}
	*/

	public ArrayList<Bonus> loadBonus(Context context) throws IOException, ParserConfigurationException, SAXException {
		
		InputStream is = context.getAssets().open(MautoFunc.DB_PATH+MautoFunc.DB_BONUS);
		
		ArrayList<Bonus> bonusList = new ArrayList<Bonus>();
		
		// Preparo tutti gli oggetti necessari
		factory = DocumentBuilderFactory.newInstance();
		parser = factory.newDocumentBuilder();
		document = parser.parse(is);
		list = document.getElementsByTagName("bonus");
		
		if(list.getLength()>0) {
			
			for(int i=0; i<list.getLength(); i++) {
				node = list.item(i);
				
				if(node.getNodeType()==Node.ELEMENT_NODE) {
					// Prendo il primo elemento di tipo "automobile" del DOM
					element = (Element) node;
					
					// Aggiungo all'array_list un nuovo oggetto "Bonus"
					bonusList.add(
						new Bonus(
							this.getValue("id"),
							this.getValue("place"),
							this.getValue("address"),
							this.getValue("description"),
							this.getValue("partID"),
							this.getValue("path"),
							Double.parseDouble(this.getValue("lat")), 
							Double.parseDouble(this.getValue("lon")),
							Double.parseDouble(this.getValue("lat1")),
							Double.parseDouble(this.getValue("lon1")),
							Double.parseDouble(this.getValue("lat2")),
							Double.parseDouble(this.getValue("lon2")),
							Double.parseDouble(this.getValue("lat3")),
							Double.parseDouble(this.getValue("lon3")),
							Double.parseDouble(this.getValue("lat4")),
							Double.parseDouble(this.getValue("lon4")) 
						)
					);
				}
			}
			return bonusList;
		} else {
			throw new RuntimeException("There aren't element in DB");
		}
	}
}
