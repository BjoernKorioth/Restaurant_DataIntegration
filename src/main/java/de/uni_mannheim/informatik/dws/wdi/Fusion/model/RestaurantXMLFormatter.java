package de.uni_mannheim.informatik.dws.wdi.Fusion.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Cuisine;
import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Neighborhood;
import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Restaurant;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

public class RestaurantXMLFormatter extends XMLFormatter<Restaurant> {

	CuisineXMLFormatter cuisineFormatter = new CuisineXMLFormatter();
	NeighborhoodXMLFormatter neighborhoodFormatter = new NeighborhoodXMLFormatter();

	@Override
	public Element createRootElement(Document doc) {
		return doc.createElement("restaurants");
	}

	@Override
	public Element createElementFromRecord(Restaurant record, Document doc) {
		Element restaurant = doc.createElement("restaurant");

		restaurant.appendChild(createTextElement("id", record.getIdentifier(), doc));

		restaurant.appendChild(createTextElementWithProvenance("name",
				record.getName(),
				record.getMergedAttributeProvenance(Restaurant.NAME), doc));
		restaurant.appendChild(createTextElementWithProvenance("address",
				record.getAddress(),
				record.getMergedAttributeProvenance(Restaurant.ADDRESS), doc));
		restaurant.appendChild(createTextElementWithProvenance("city",
				record.getCity(),
				record.getMergedAttributeProvenance(Restaurant.CITY), doc));
		restaurant.appendChild(createTextElementWithProvenance("zip",
				Integer.toString(record.getZip()),
				record.getMergedAttributeProvenance(Restaurant.ZIP), doc));
		restaurant.appendChild(createTextElementWithProvenance("state",
				record.getState(),
				record.getMergedAttributeProvenance(Restaurant.STATE), doc));
		restaurant.appendChild(createCuisinesElement(record, doc));
		restaurant.appendChild(createTextElementWithProvenance("rating",
				Double.toString(record.getRating()),
				record.getMergedAttributeProvenance(Restaurant.RATING), doc));
		restaurant.appendChild(createNeighborhoodsElement(record, doc));
		restaurant.appendChild(createTextElementWithProvenance("price",
				record.getPrice(),
				record.getMergedAttributeProvenance(Restaurant.PRICE), doc));
		restaurant.appendChild(createTextElementWithProvenance("website",
				record.getWebsite(),
				record.getMergedAttributeProvenance(Restaurant.WEBSITE), doc));
		restaurant.appendChild(createTextElementWithProvenance("card",
				record.getCard().toString(),
				record.getMergedAttributeProvenance(Restaurant.CARD), doc));
		

		return restaurant;
	}

	protected Element createTextElementWithProvenance(String name,
			String value, String provenance, Document doc) {
		Element elem = createTextElement(name, value, doc);
		elem.setAttribute("provenance", provenance);
		return elem;
	}

	protected Element createCuisinesElement(Restaurant record, Document doc) {
		Element cuisineRoot = cuisineFormatter.createRootElement(doc);
		cuisineRoot.setAttribute("provenance",
				record.getMergedAttributeProvenance(Restaurant.CUISINES));

		for (Cuisine a : record.getCuisine()) {
			cuisineRoot.appendChild(cuisineFormatter
					.createElementFromRecord(a, doc));
		}

		return cuisineRoot;
	}

	protected Element createNeighborhoodsElement(Restaurant record, Document doc) {
		Element neighborhoodRoot = neighborhoodFormatter.createRootElement(doc);
		neighborhoodRoot.setAttribute("provenance",
				record.getMergedAttributeProvenance(Restaurant.CUISINES));

		for (Neighborhood a : record.getNeighborhood()) {
			neighborhoodRoot.appendChild(neighborhoodFormatter
					.createElementFromRecord(a, doc));
		}

		return neighborhoodRoot;
	}
}
