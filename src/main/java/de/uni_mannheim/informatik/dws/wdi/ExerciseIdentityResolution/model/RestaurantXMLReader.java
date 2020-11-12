package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model;

import java.util.List;


import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

public class RestaurantXMLReader extends XMLMatchableReader<Restaurant, Attribute> {
	
	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.model.io.XMLMatchableReader#initialiseDataset(de.uni_mannheim.informatik.wdi.model.DataSet)
	 */
	@Override
	protected void initialiseDataset(DataSet<Restaurant, Attribute> dataset) {
		super.initialiseDataset(dataset);
		
	}
	
	@Override
	public Restaurant createModelFromElement(Node node, String provenanceInfo) {
		String id = getValueFromChildElement(node, "id");

		// create the object with id and provenance information
		Restaurant restaurant = new Restaurant(id, provenanceInfo);

		// fill the attributes
		restaurant.setName(getValueFromChildElement(node, "name"));
		restaurant.setAddress(getValueFromChildElement(node, "address"));
		restaurant.setCity(getValueFromChildElement(node, "city"));
		restaurant.setState(getValueFromChildElement(node, "state"));
		restaurant.setPrice(getValueFromChildElement(node, "price"));
		restaurant.setWebsite(getValueFromChildElement(node, "website"));
		restaurant.setCard(getValueFromChildElement(node, "card"));
		
		// fill attributes that need conversion
		//ZIP -> Integer
		try {
			String zip = getValueFromChildElement(node, "zip");
			if (zip != null && !zip.isEmpty()) {
				int intZip = Integer.parseInt(zip);
				restaurant.setZip(intZip);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		//Rating -> Double
		try {
			String rating = getValueFromChildElement(node, "rating");
			if (rating != null && !rating.isEmpty()) {
				double doubleRating = Double.parseDouble(rating);
				restaurant.setRating(doubleRating);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		// load the lists
		
		//Neighborhoods
		List<Neighborhood> neighborhoods = getObjectListFromChildElement(node, "neighborhood",
				"neighborhood", new NeighborhoodXMLReader(), provenanceInfo);
		restaurant.setNeighborhood(neighborhoods);
		
		// Cuisines
		List<Cuisine> cuisines = getObjectListFromChildElement(node, "cuisines",
				"cuisine", new CuisineXMLReader(), provenanceInfo);
		restaurant.setCuisine(cuisines);

		return restaurant;
	}

}
