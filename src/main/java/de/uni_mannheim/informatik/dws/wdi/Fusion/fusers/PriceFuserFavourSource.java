package de.uni_mannheim.informatik.dws.wdi.Fusion.fusers;


import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Restaurant;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.meta.FavourSources;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class PriceFuserFavourSource extends AttributeValueFuser<String, Restaurant, Attribute> {

	public PriceFuserFavourSource() {
		super(new FavourSources<String, Restaurant, Attribute>());
	}

	@Override
	public boolean hasValue(Restaurant record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Restaurant.PRICE);
	}

	@Override
	public String getValue(Restaurant record, Correspondence<Attribute, Matchable> correspondence) {
		return record.getPrice();
	}

	@Override
	public void fuse(RecordGroup<Restaurant, Attribute> group, Restaurant fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<String, Restaurant, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setPrice(fused.getValue());
		fusedRecord.setAttributeProvenance(Restaurant.PRICE, fused.getOriginalIds());
	}

}