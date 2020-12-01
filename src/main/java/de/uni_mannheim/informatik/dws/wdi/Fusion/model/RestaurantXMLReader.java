package de.uni_mannheim.informatik.dws.wdi.Fusion.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;


import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Cuisine;
import de.uni_mannheim.informatik.dws.wdi.Fusion.model.CuisineXMLReader;
import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Neighborhood;
import de.uni_mannheim.informatik.dws.wdi.Fusion.model.NeighborhoodXMLReader;
import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Restaurant;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleFactory;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

public class RestaurantXMLReader extends XMLMatchableReader<Restaurant, Attribute> implements
FusibleFactory<Restaurant, Attribute> {
	
	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.model.io.XMLMatchableReader#initialiseDataset(de.uni_mannheim.informatik.wdi.model.DataSet)
	 */
	@Override
	protected void initialiseDataset(DataSet<Restaurant, Attribute> dataset) {
		super.initialiseDataset(dataset);
		
		// the schema is defined in the Movie class and not interpreted from the file, so we have to set the attributes manually
		dataset.addAttribute(Restaurant.NAME);
		dataset.addAttribute(Restaurant.ADDRESS);
		dataset.addAttribute(Restaurant.CITY);
		dataset.addAttribute(Restaurant.STATE);
		dataset.addAttribute(Restaurant.PRICE);
		dataset.addAttribute(Restaurant.WEBSITE);
		dataset.addAttribute(Restaurant.CARD);
		dataset.addAttribute(Restaurant.ZIP);
		dataset.addAttribute(Restaurant.RATING);
		dataset.addAttribute(Restaurant.NEIGHBORHOODS);
		dataset.addAttribute(Restaurant.CUISINES);
		
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
	
		restaurant.setRating(getValueFromChildElement(node, "rating"));
		

		// load the lists
		
		//Neighborhoods
		List<Neighborhood> neighborhoods = getObjectListFromChildElement(node, "neighborhoods",
				"neighborhood", new NeighborhoodXMLReader(), provenanceInfo);
		restaurant.setNeighborhood(neighborhoods);
		
		// Cuisines
		List<Cuisine> cuisines = getObjectListFromChildElement(node, "cuisines",
				"cuisine", new CuisineXMLReader(), provenanceInfo);
		restaurant.setCuisine(cuisines);

		return restaurant;
	}
	
	@Override
	public Restaurant createInstanceForFusion(RecordGroup<Restaurant, Attribute> cluster) {
	
	List<String> ids = new LinkedList<>();
	
	for (Restaurant m : cluster.getRecords()) {
		ids.add(m.getIdentifier());
	}
	
	Collections.sort(ids);
	
	String mergedId = StringUtils.join(ids, '+');
	
	return new Restaurant(mergedId, "fused");
	}
	/*protected String getNodeAttributeValue(Node node, String attributeName) {

		if (node.hasAttributes()) {
			Attr attr = (Attr) node.getAttributes().getNamedItem(attributeName);
			if (attr != null) {
				return attr.getValue();
				//System.out.println("attribute: " + attribute);
			}
		}
		return  null;

	}
	*/

}
