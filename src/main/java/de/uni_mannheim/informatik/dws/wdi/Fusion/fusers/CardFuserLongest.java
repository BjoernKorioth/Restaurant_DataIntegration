package de.uni_mannheim.informatik.dws.wdi.Fusion.fusers;


import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Restaurant;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.string.LongestString;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class CardFuserLongest extends
AttributeValueFuser<String, Restaurant, Attribute> {

public CardFuserLongest() {
super(new LongestString<Restaurant, Attribute>());
}

@Override
public void fuse(RecordGroup<Restaurant, Attribute> group, Restaurant fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {

// get the fused value
FusedValue<String, Restaurant, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);

// set the value for the fused record
fusedRecord.setCard(fused.getValue());

// add provenance info
fusedRecord.setAttributeProvenance(Restaurant.CARD, fused.getOriginalIds());
}

@Override
public boolean hasValue(Restaurant record, Correspondence<Attribute, Matchable> correspondence) {
return record.hasValue(Restaurant.CARD);
}

@Override
public String getValue(Restaurant record, Correspondence<Attribute, Matchable> correspondence) {
return record.getAddress();
}

}