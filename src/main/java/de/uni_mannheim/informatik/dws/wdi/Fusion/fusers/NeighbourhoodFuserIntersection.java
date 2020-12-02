package de.uni_mannheim.informatik.dws.wdi.Fusion.fusers;

import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Neighborhood;
import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Restaurant;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.list.Intersection;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

import java.util.List;

public class NeighbourhoodFuserIntersection extends AttributeValueFuser<List<Neighborhood>, Restaurant, Attribute> {

    public NeighbourhoodFuserIntersection() {
        super(new Intersection<Neighborhood, Restaurant, Attribute>());
    }

    @Override
    public boolean hasValue(Restaurant record, Correspondence<Attribute, Matchable> correspondence) {
        return record.hasValue(Restaurant.NEIGHBORHOODS);
    }

    @Override
    public List<Neighborhood> getValue(Restaurant record, Correspondence<Attribute, Matchable> correspondence) {
        return record.getNeighborhood();
    }

    @Override
    public void fuse(RecordGroup<Restaurant, Attribute> group, Restaurant fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
        FusedValue<List<Neighborhood>, Restaurant, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
        fusedRecord.setNeighborhood(fused.getValue());
        fusedRecord.setAttributeProvenance(Restaurant.NEIGHBORHOODS, fused.getOriginalIds());
    }

}
