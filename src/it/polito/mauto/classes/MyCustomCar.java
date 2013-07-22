package it.polito.mauto.classes;

import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;

public class MyCustomCar {
	
	private ArrayList<Parameter> performance;
	private ArrayList<Part> parts;
	
	public MyCustomCar() {
		performance = new ArrayList<Parameter>();
		parts = new ArrayList<Part>();
	}
	
	/**
	 * Set a new part to mycar
	 * @param part
	 */
	public void setPart(Part part) {
		if(parts != null && parts.size() > 0 ) {
			boolean flag = false;
			
			for (int i = 0; i < parts.size(); i++) {
				if(part.getCategory().compareToIgnoreCase(parts.get(i).getCategory())==0) {
					parts.set(i, part);
					flag = true;
					break;
				}
			}
			
			if(!flag) {
				parts.add(part);
			}
			
			this.calculatePerformance(part);
		} else {
			parts.add(part);
			this.calculatePerformance(part);
		}
	}
	
	
	/**
	 * Calculate performance from parts added to mycar
	 * @param pt
	 */
	public void calculatePerformance(Part pt) {		
		if(pt != null) {
			if(performance != null) {
				if(pt.getPerformance().size() > 0) {
					
					Log.w("CalcSingle", "true");
					
					// >> Calculate performance of the single part

					// For each parts loop its parameter
					for(Parameter prm : pt.getPerformance()) {
						boolean flag = false;
						// Check if there is a parameter with the same name of the part's parameter in performance
						for (int i = 0; i < performance.size(); i++) {
							if(performance.get(i).getName().compareToIgnoreCase(prm.getName())==0) {
								// If exists a parameter in performance with the same name of part's parameter, 
								// sum the two values and set it...
								performance.get(i).setValue(performance.get(i).getValue() + prm.getValue());
								flag = true;
								break;
							}
						}
						// ...else if add the entire parameter to performance's list
						if(!flag) {
							performance.add(prm);
						}
					}
				} else {
					
					Log.w("PutAll", "true");
					
					// >> There aren't performance yet
					for(Parameter p : pt.getPerformance()) {
						// Add the part parameter to mycustomcar performance
						performance.add(p);
					}
				}
			} else {
				throw new RuntimeException("calculatePerformance - The performance list has not been initialized");
			}
		} else {
			throw new RuntimeException("calculatePerformance - The part passed to add at mycar is null");
		}
	}
	
	
	/**
	 * Calculate performance for all parts of mycustomcar
	 */
	public void calculateAllPerformanceOfMyCar() {
		if(parts != null && parts.size() > 0) {
			// Loop all parts of the mycar
			for(Part prt : parts) {
				// For each parts loop its parameter
				for(Parameter prm : prt.getPerformance()) {
					boolean flag = false;
					// Check if there is a parameter with the same name of the part's parameter in performance
					for (int i = 0; i < performance.size(); i++) {
						if(performance.get(i).getName().compareToIgnoreCase(prm.getName())==0) {
							// If exists a parameter in performance with the same name of part's parameter, 
							// sum the two values and set it...
							performance.get(i).setValue(performance.get(i).getValue() + prm.getValue());
							flag = true;
							break;
						}
					}
					// ...else if add the entire parameter to performance's list
					if(!flag) {
						performance.add(prm);
					}
				}
			} 
		} else {
			throw new RuntimeException("calculateAllPerformanceOfMyCar - There aren't parts on your custom car");
		}
	}
	
	
	public ArrayList<Part> getParts() {
		return parts;
	}
	
	public ArrayList<Parameter> getPerformance() {
		return performance;
	}
}
