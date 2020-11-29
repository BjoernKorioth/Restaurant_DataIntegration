package de.uni_mannheim.informatik.dws.wdi.Fusion.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Neighborhood;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

public class NeighborhoodXMLFormatter extends XMLFormatter<Neighborhood> {

	@Override
	public Element createRootElement(Document doc) {
		return doc.createElement("neighborhoods");
	}
	
	@Override
	public Element createElementFromRecord(Neighborhood record, Document doc) {
		Element neighborhood = doc.createElement("neighborhood");


		if(record.getName()!=null) {
			neighborhood.appendChild(createTextElement("name", record.getName(), doc));
		}


		return neighborhood;
	}

}
