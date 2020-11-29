package de.uni_mannheim.informatik.dws.wdi.Fusion.model;

import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Cuisine;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

public class CuisineXMLReader extends XMLMatchableReader<Cuisine, Attribute> {
	
	@Override
	public Cuisine createModelFromElement(Node node, String provenanceInfo) {
		String id = getValueFromChildElement(node, "id");

		// create the object with id and provenance information
		Cuisine cuisine = new Cuisine(id, provenanceInfo);

		// fill the attributes
		cuisine.setName(getValueFromChildElement(node, "name"));
		
		return cuisine;
	}
}
