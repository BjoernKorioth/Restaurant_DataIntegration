package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Restaurant;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class RestaurantBlockingByZipCodeTwoDigits extends
        RecordBlockingKeyGenerator<Restaurant, Attribute> {

    private static final long serialVersionUID = 1L;

    @Override
    public void generateBlockingKeys(Restaurant record, Processable<Correspondence<Attribute, Matchable>> correspondences, DataIterator<Pair<String, Restaurant>> resultCollector) {
        try {
    	resultCollector.next(new Pair<>(Integer.toString(record.getZip()).substring(0,2), record));
        } catch (Exception e) {};
    }
}