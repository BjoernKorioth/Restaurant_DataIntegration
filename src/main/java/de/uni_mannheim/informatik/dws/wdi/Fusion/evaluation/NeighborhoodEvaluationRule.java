package de.uni_mannheim.informatik.dws.wdi.Fusion.evaluation;


import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Neighborhood;
import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Restaurant;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.util.HashSet;
import java.util.Set;

public class NeighborhoodEvaluationRule extends EvaluationRule<Restaurant, Attribute> {

	@Override
	public boolean isEqual(Restaurant record1, Restaurant record2, Attribute schemaElement) {
		Set<String> neighborhoods1 = new HashSet<>();
		Set<String> neighborhoods2 = new HashSet<>();

		try {
			for (Neighborhood a : record1.getNeighborhood()) {
				// note: evaluating using the actor's name only suffices for simple
				// lists
				// in your project, you should have actor ids which you use here
				// (and in the identity resolution)
				neighborhoods1.add(a.getName().toLowerCase());
			}
		} catch (Exception e ) {neighborhoods1 = null; return false;};
		
		try {
			for (Neighborhood a : record2.getNeighborhood()) {
				neighborhoods2.add(a.getName().toLowerCase());
			}
		} catch (Exception e ) {neighborhoods2 = null; return false;};

		return (neighborhoods2.containsAll(neighborhoods1));
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
