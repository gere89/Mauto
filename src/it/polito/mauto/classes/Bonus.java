package it.polito.mauto.classes;

public class Bonus {
	private double lat, lon, lat1, lon1, lat2, lon2, lat3, lon3, lat4, lon4;
	private String id, place, address, description, partID, path; 

	private boolean isLooked;
	
	public Bonus(String id, String place, String address, String description, String partID, String path, double lat, double lon, double lat1, double lon1, double lat2, double lon2, double lat3, double lon3, double lat4, double lon4) {
		super();
		this.id = id;
		this.place = place;
		this.address = address;
		this.partID = partID;
		this.description = description;
		this.path = path;
		this.lat = lat;
		this.lon = lon;
		this.lat1 = lat1;
		this.lon1 = lon1;
		this.lat2 = lat2;
		this.lon2 = lon2;
		this.lat3 = lat3;
		this.lon3 = lon3;
		this.lat4 = lat4;
		this.lon4 = lon4;
		
		isLooked = false;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat1() {
		return lat1;
	}

	public void setLat1(double lat1) {
		this.lat1 = lat1;
	}

	public double getLon1() {
		return lon1;
	}

	public void setLon1(double lon1) {
		this.lon1 = lon1;
	}

	public double getLat2() {
		return lat2;
	}

	public void setLat2(double lat2) {
		this.lat2 = lat2;
	}

	public double getLon2() {
		return lon2;
	}

	public void setLon2(double lon2) {
		this.lon2 = lon2;
	}

	public double getLat3() {
		return lat3;
	}

	public void setLat3(double lat3) {
		this.lat3 = lat3;
	}

	public double getLon3() {
		return lon3;
	}

	public void setLon3(double lon3) {
		this.lon3 = lon3;
	}

	public double getLat4() {
		return lat4;
	}

	public void setLat4(double lat4) {
		this.lat4 = lat4;
	}

	public double getLon4() {
		return lon4;
	}

	public void setLon4(double lon4) {
		this.lon4 = lon4;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getpartID() {
		return partID;
	}

	public void setpartID(String partID) {
		this.partID = partID;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isLooked() {
		return isLooked;
	}

	public void setLooked(boolean isLooked) {
		this.isLooked = isLooked;
	}
}
