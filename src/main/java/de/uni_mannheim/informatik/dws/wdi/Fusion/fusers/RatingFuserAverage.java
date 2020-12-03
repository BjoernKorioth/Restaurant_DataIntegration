package de.uni_mannheim.informatik.dws.wdi.Fusion.fusers;

import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Restaurant;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.meta.FavourSources;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.numeric.Average;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class RatingFuserAverage extends AttributeValueFuser<Double, Restaurant, Attribute> {

	public RatingFuserAverage() {
		super(new Average<Restaurant, Attribute>());
	}

	@Override
	public boolean hasValue(Restaurant record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Restaurant.RATING);
	}

	@Override
	public Double getValue(Restaurant record, Correspondence<Attribute, Matchable> correspondence) {
		return Double.parseDouble(record.getRating().replace(',', '.'));
	}

	@Override
	public void fuse(RecordGroup<Restaurant, Attribute> group, Restaurant fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<Double, Restaurant, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setRating(fused.getValue().toString().replace('.', ','));
		fusedRecord.setAttributeProvenance(Restaurant.RATING, fused.getOriginalIds());
	}
}
