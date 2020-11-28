package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators;


import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Restaurant;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;

public class RestaurantAddressComparatorLevenshtein implements Comparator<Restaurant, Attribute> {
	
	private static final long serialVersionUID = 1L;
	private LevenshteinSimilarity sim = new LevenshteinSimilarity();
	
	private ComparatorLogger comparisonLog;

	@Override
	public double compare(
			Restaurant record1,
			Restaurant record2,
			Correspondence<Attribute, Matchable> schemaCorrespondences) {
		
		String s1 = record1.getAddress();
		String s2 = record2.getAddress();
		
		if (s1.contains("west") || s2.contains("west")) {
			s1 = s1.replace("west", "w");
			s2 = s2.replace("west", "w");
		}
		if (s1.contains("east") || s2.contains("east")) {
			s1 = s1.replace("east", "e");
			s2 = s2.replace("east", "e");
		}
		if (s1.contains("south") || s2.contains("south")) {
			s1 = s1.replace("south", "s");
			s2 = s2.replace("south", "s");
		}
		if (s1.contains("north") || s2.contains("north")) {
			s1 = s1.replace("north", "n");
			s2 = s2.replace("north", "n");
		}
		
		double similarity = sim.calculate(s1, s2);
		
		if(this.comparisonLog != null){
			this.comparisonLog.setComparatorName(getClass().getName());
		
			this.comparisonLog.setRecord1Value(s1);
			this.comparisonLog.setRecord2Value(s2);
    	
			this.comparisonLog.setSimilarity(Double.toString(similarity));
		}
		
		return similarity;		
	}

	@Override
	public ComparatorLogger getComparisonLog() {
		return this.comparisonLog;
	}

	@Override
	public void setComparisonLog(ComparatorLogger comparatorLog) {
		this.comparisonLog = comparatorLog;
	}

}

