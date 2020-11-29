package de.uni_mannheim.informatik.dws.wdi.Fusion.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Cuisine;
import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Restaurant;
import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class Restaurant extends AbstractRecord<Attribute> implements Serializable {

	/*
	 * example entry <movie> <id>academy_awards_2</id> <title>True Grit</title>
	 * <director> <name>Joel Coen and Ethan Coen</name> </director> <actors>
	 * <actor> <name>Jeff Bridges</name> </actor> <actor> <name>Hailee
	 * Steinfeld</name> </actor> </actors> <date>2010-01-01</date> </movie>
	 */

	private static final long serialVersionUID = 1L;

	public Restaurant(String identifier, String provenance) {
		super(identifier, provenance);
		cuisines = new LinkedList<>();
		neighborhoods = new LinkedList<>();
	}
	
	private String name;
	private String address;
	private String city;
	private int zip;
	private String state;
	private List<Neighborhood> neighborhoods;
	private String rating;
	private List<Cuisine> cuisines;
	private String price;
	private String website;
	private String card;

	@Override
	public String getIdentifier() {
		return id;
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

	public void setCity(String city) {
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
		return neighborhoods;
	}

	public void setNeighborhood(List<Neighborhood> neighborhood) {
		this.neighborhoods = neighborhood;
	}
	
	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}
	
	public List<Cuisine> getCuisine() {
		return cuisines;
	}

	public void setCuisine(List<Cuisine> cuisine) {
		this.cuisines = cuisine;
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
	

	private Map<Attribute, Collection<String>> provenance = new HashMap<>();
	private Collection<String> recordProvenance;

	public void setRecordProvenance(Collection<String> provenance) {
		recordProvenance = provenance;
	}

	public Collection<String> getRecordProvenance() {
		return recordProvenance;
	}

	public void setAttributeProvenance(Attribute attribute,
			Collection<String> provenance) {
		this.provenance.put(attribute, provenance);
	}

	public Collection<String> getAttributeProvenance(String attribute) {
		return provenance.get(attribute);
	}

	public String getMergedAttributeProvenance(Attribute attribute) {
		Collection<String> prov = provenance.get(attribute);

		if (prov != null) {
			return StringUtils.join(prov, "+");
		} else {
			return "";
		}
	}

	public static final Attribute NAME = new Attribute("Name");
	public static final Attribute ADDRESS = new Attribute("Address");
	public static final Attribute CITY = new Attribute("City");
	public static final Attribute ZIP = new Attribute("Zip");
	public static final Attribute STATE = new Attribute("State");
	public static final Attribute NEIGHBORHOODS = new Attribute("Neighborhoods");
	public static final Attribute RATING = new Attribute("Rating");
	public static final Attribute CUISINES = new Attribute("Cuisines");
	public static final Attribute PRICE = new Attribute("Price");
	public static final Attribute WEBSITE = new Attribute("Website");
	public static final Attribute CARD = new Attribute("Card");
	
	@Override
	public boolean hasValue(Attribute attribute) {
		if(attribute==NAME)
			return getName() != null && !getName().isEmpty();
		else if(attribute==ADDRESS)
			return getAddress() != null && !getAddress().isEmpty();
		else if(attribute==CITY)
			return getCity() != null && !getCity().isEmpty();
		else if(attribute==ZIP)
			return getZip() != 0;
		else if(attribute==STATE)
			return getState() != null && !getState().isEmpty();
		else if(attribute==NEIGHBORHOODS)
			return getNeighborhood() != null && getNeighborhood().size() > 0;
		else if(attribute==RATING)
			return getRating() != null && !getRating().isEmpty();
		else if(attribute==CUISINES)
			return getCuisine() != null && getCuisine().size() > 0;
		else if(attribute==PRICE)
			return getPrice() != null && !getPrice().isEmpty();
		else if(attribute==WEBSITE)
			return getWebsite() != null && !getWebsite().isEmpty();
		else if(attribute==CARD)
			return getCard() != null;
		else
			return false;
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