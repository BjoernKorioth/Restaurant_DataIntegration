package de.uni_mannheim.informatik.dws.wdi.Fusion.fusers;

import java.util.List;

import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Cuisine;
import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Neighborhood;
import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Restaurant;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.list.Union;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class CuisineFuserUnion extends AttributeValueFuser<List<Cuisine>, Restaurant, Attribute> {
	
	public CuisineFuserUnion() {
		super(new Union<Cuisine, Restaurant, Attribute>());
	}
	
	@Override
	public boolean hasValue(Restaurant record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Restaurant.CUISINES);
	}
	
	@Override
	public List<Cuisine> getValue(Restaurant record, Correspondence<Attribute, Matchable> correspondence) {
		return record.getCuisine();
	}

	@Override
	public void fuse(RecordGroup<Restaurant, Attribute> group, Restaurant fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<List<Cuisine>, Restaurant, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setCuisine(fused.getValue());
		fusedRecord.setAttributeProvenance(Restaurant.CUISINES, fused.getOriginalIds());
	}

}
