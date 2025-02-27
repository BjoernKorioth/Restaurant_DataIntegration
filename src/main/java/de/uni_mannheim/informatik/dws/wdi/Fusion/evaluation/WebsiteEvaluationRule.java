package de.uni_mannheim.informatik.dws.wdi.Fusion.evaluation;


import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Restaurant;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;

public class WebsiteEvaluationRule extends EvaluationRule<Restaurant, Attribute> {

	SimilarityMeasure<String> sim = new TokenizingJaccardSimilarity();

	@Override
	public boolean isEqual(Restaurant record1, Restaurant record2, Attribute schemaElement) {
		// the title is correct if all tokens are there, but the order does not
		// matter
		String s1 = record1.getWebsite();
		s1 = s1.replace("https://", "");
		s1 = s1.replace("http://", "");
		s1 = s1.replace("www.", "");
		String s2 = record2.getWebsite();
		s2 = s2.replace("https://", "");
		s2 = s2.replace("http://", "");
		s2 = s2.replace("www.", "");
		
		return sim.calculate(s1, s2) == 1.0;
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

