package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

public class Restaurant implements Matchable {
	
	protected String id;
	protected String provenance;
	private String name;
	private String address;
	private String city;
	private int zip;
	private String state;
	private List<Neighborhood> neighborhood;
	private double rating;
	private List<Cuisine> cuisine;
	private String price;
	private String website;
	private String card;
	
	public Restaurant(String identifier, String provenance) {
		id = identifier;
		this.provenance = provenance;
		neighborhood = new LinkedList<>();
		cuisine = new LinkedList<>();
	}
	
	@Override
	public String getIdentifier() {
		return id;
	}

	@Override
	public String getProvenance() {
		return provenance;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String address) {
		this.city = city;
	}
	
	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public List<Neighborhood> getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(List<Neighborhood> neighborhood) {
		this.neighborhood = neighborhood;
	}
	
	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
	
	public List<Cuisine> getCuisine() {
		return cuisine;
	}

	public void setCuisine(List<Cuisine> cuisine) {
		this.cuisine = cuisine;
	}
	
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
	
	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}
	
	@Override
	public String toString() {
		return String.format("[Restaurant %s: %s / %s / %s]", getIdentifier(), getName(),
				getAddress(), getCity());
	}

	@Override
	public int hashCode() {
		return getIdentifier().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Restaurant){
			return this.getIdentifier().equals(((Restaurant) obj).getIdentifier());
		}else
			return false;
	}


}
