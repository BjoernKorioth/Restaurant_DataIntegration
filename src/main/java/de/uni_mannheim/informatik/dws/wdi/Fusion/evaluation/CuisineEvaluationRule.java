package de.uni_mannheim.informatik.dws.wdi.Fusion.evaluation;


import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Cuisine;
import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Restaurant;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.util.HashSet;
import java.util.Set;

public class CuisineEvaluationRule extends EvaluationRule<Restaurant, Attribute> {

	@Override
	public boolean isEqual(Restaurant record1, Restaurant record2, Attribute schemaElement) {
		Set<String> cuisines1 = new HashSet<>();

		for (Cuisine a : record1.getCuisine()) {
			// note: evaluating using the actor's name only suffices for simple
			// lists
			// in your project, you should have actor ids which you use here
			// (and in the identity resolution)
			cuisines1.add(a.getName().toLowerCase());
		}

		Set<String> cuisines2 = new HashSet<>();
		for (Cuisine a : record2.getCuisine()) {
			cuisines2.add(a.getName().toLowerCase());
		}

		return cuisines2.containsAll(cuisines1);
	}

	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.datafusion.EvaluationRule#isEqual(java.lang.Object, java.lang.Object, de.uni_mannheim.informatik.wdi.model.Correspondence)
	 */
	@Override
	public boolean isEqual(Restaurant record1, Restaurant record2,
			Correspondence<Attribute, Matchable> schemaCorrespondence) {
		return isEqual(record1, record2, (Attribute)null);
	}

}