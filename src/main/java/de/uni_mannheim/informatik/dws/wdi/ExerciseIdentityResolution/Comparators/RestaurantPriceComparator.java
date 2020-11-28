package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Restaurant;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.numeric.DeviationSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;

public class RestaurantPriceComparator implements Comparator<Restaurant, Attribute> {
	
	private static final long serialVersionUID = 1L;
	private LevenshteinSimilarity sim = new LevenshteinSimilarity();

    private ComparatorLogger comparisonLog;


    @Override
	public double compare(
			Restaurant record1,
			Restaurant record2,
			Correspondence<Attribute, Matchable> schemaCorrespondences) {
		
    	String s1 = record1.getPrice();
		String s2 = record2.getPrice();
		
		double similarity = sim.calculate(s1, s2);
		
		if(this.comparisonLog != null){
			this.comparisonLog.setComparatorName(getClass().getName());
		
			this.comparisonLog.setRecord1Value(String.valueOf(s1));
			this.comparisonLog.setRecord2Value(String.valueOf(s2));
    	
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
