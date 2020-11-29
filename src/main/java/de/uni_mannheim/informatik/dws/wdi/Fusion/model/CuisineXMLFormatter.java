package de.uni_mannheim.informatik.dws.wdi.Fusion.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Cuisine;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

public class CuisineXMLFormatter extends XMLFormatter<Cuisine> {

	@Override
	public Element createRootElement(Document doc) {
		return doc.createElement("cuisines");
	}
	
	@Override
	public Element createElementFromRecord(Cuisine record, Document doc) {
		Element cuisine = doc.createElement("cuisine");


		if(record.getName()!=null) {
			cuisine.appendChild(createTextElement("name", record.getName(), doc));
		}


		return cuisine;
	}

}
