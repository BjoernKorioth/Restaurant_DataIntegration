package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.RestaurantBlockingByCity;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.RestaurantBlockingByState;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.RestaurantBlockingByZipCodeTwoDigits;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.RestaurantBlockingKeyByZipCode;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.RestaurantAddressComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.RestaurantAddressComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.RestaurantAddressComparatorMaxToken;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.RestaurantNameComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.RestaurantNameComparatorMaxToken;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.RestaurantNameComparatotLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Restaurant;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.RestaurantXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.MaximumBipartiteMatchingAlgorithm;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

import java.io.File;

public class IR_yelp_2_yellowPages {
    public static void main( String[] args ) throws Exception
    {
        // loading data
        System.out.println("*\n*\tLoading datasets\n*");

        HashedDataSet<Restaurant, Attribute> dataRestaurantYelp = new HashedDataSet<>();
        new RestaurantXMLReader().loadFromXML(new File("data/input/yelp.xml"), "/restaurants/restaurant", dataRestaurantYelp);

        //new RestaurantXMLReader().loadFromXML(new File("data/input/zomato.xml"), "/restaurants/restaurant", dataRestaurantYP);
        HashedDataSet<Restaurant, Attribute> dataRestaurantYP = new HashedDataSet<>();
        new RestaurantXMLReader().loadFromXML(new File("data/input/yellow_pages.xml"), "/restaurants/restaurant", dataRestaurantYP);

        System.out.println("*\n*\tCompleted Loading datasets\n*");

        // load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File(
                "data/goldstandard/gs_yp_2_yelp.csv"));

        // create a matching rule
        LinearCombinationMatchingRule<Restaurant, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
                0.75);
        matchingRule.activateDebugReport("data/output/YP_2_Yelp_debugResultsMatchingRule.csv", 10000000, gsTest);

        // add comparators
//      matchingRule.addComparator(new RestaurantNameComparatorJaccard(), 0.5);
//      matchingRule.addComparator(new RestaurantNameComparatotLevenshtein(), 0.3);
        matchingRule.addComparator(new RestaurantNameComparatorMaxToken(), 0.3);
      
//      matchingRule.addComparator(new RestaurantAddressComparatorJaccard(), 0.5);
      matchingRule.addComparator(new RestaurantAddressComparatorMaxToken(), 0.7);
//      matchingRule.addComparator(new RestaurantAddressComparatorLevenshtein(), 0.5);
      
//      matchingRule.addComparator(new RestaurantRatingComparator(), 0.1);
        
        
        // create a blocker (blocking strategy)
//        NoBlocker<Restaurant, Attribute> blocker = new NoBlocker<>();
        StandardRecordBlocker<Restaurant, Attribute> blocker = new StandardRecordBlocker<Restaurant, Attribute>(new RestaurantBlockingKeyByZipCode());
//        StandardRecordBlocker<Restaurant, Attribute> blocker = new StandardRecordBlocker<Restaurant, Attribute>(new RestaurantBlockingByZipCodeTwoDigits());
//        StandardRecordBlocker<Restaurant, Attribute> blocker = new StandardRecordBlocker<Restaurant, Attribute>(new RestaurantBlockingByCity());
//        StandardRecordBlocker<Restaurant, Attribute> blocker = new StandardRecordBlocker<Restaurant, Attribute>(new RestaurantBlockingByState());
        
        blocker.setMeasureBlockSizes(true);
        //Write debug results to file:
        blocker.collectBlockSizeData("data/output/YP_2_Yelp_debugResultsBlocking.csv", 100);

        // Initialize Matching Engine
        MatchingEngine<Restaurant, Attribute> engineYelpYellowPages = new MatchingEngine<>();

        // Execute the matching
        System.out.println("*\n*\tRunning identity resolution\n*");
        Processable<Correspondence<Restaurant, Attribute>> correspondencesYelpYellowPages = engineYelpYellowPages.runIdentityResolution(
                dataRestaurantYelp, dataRestaurantYP, null, matchingRule,
                blocker);

     // Create a top-1 global matching
        correspondencesYelpYellowPages = engineYelpYellowPages.getTopKInstanceCorrespondences(correspondencesYelpYellowPages, 1, 0);

//      Alternative: Create a maximum-weight, bipartite matching
//		 MaximumBipartiteMatchingAlgorithm<Restaurant, Attribute> maxWeight = new MaximumBipartiteMatchingAlgorithm<>(correspondencesYelpYellowPages);
//		 maxWeight.run();
//		 correspondencesYelpYellowPages = maxWeight.getResult();

        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/YP_2_Yelp_correspondences.csv"), correspondencesYelpYellowPages);

        System.out.println("*\n*\tEvaluating result\n*");
        // evaluate your result
        MatchingEvaluator<Restaurant, Attribute> evaluator = new MatchingEvaluator<Restaurant, Attribute>();
        Performance perfTest = evaluator.evaluateMatching(correspondencesYelpYellowPages,
                gsTest);

        // print the evaluation result
        System.out.println("Yelp <-> Yellow");
        System.out.println(String.format(
                "Precision: %.4f",perfTest.getPrecision()));
        System.out.println(String.format(
                "Recall: %.4f",	perfTest.getRecall()));
        System.out.println(String.format(
                "F1: %.4f",perfTest.getF1()));
    }
}
