package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model;

import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

public class NeighborhoodXMLReader extends XMLMatchableReader<Neighborhood, Attribute> {
	
	@Override
	public Neighborhood createModelFromElement(Node node, String provenanceInfo) {
		String id = getValueFromChildElement(node, "id");

		// create the object with id and provenance information
		Neighborhood neighborhood = new Neighborhood(id, provenanceInfo);

		// fill the attributes
		neighborhood.setNeighborhood(getValueFromChildElement(node, "name"));
		
		return neighborhood;
	}

}
