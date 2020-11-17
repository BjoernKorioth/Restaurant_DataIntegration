package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.RestaurantBlockingByCity;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.RestaurantBlockingByZipCodeTwoDigits;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.RestaurantAddressComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.RestaurantNameComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.RestaurantNameComparatotLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Restaurant;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.RestaurantXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
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
import java.util.concurrent.TimeUnit;

import org.apache.jena.ext.com.google.common.base.Stopwatch;

public class IR_test_bk {
    public static void main( String[] args ) throws Exception
    {
        // loading data
        System.out.println("*\n*\tLoading datasets\n*");
        HashedDataSet<Restaurant, Attribute> dataRestaurantZomato = new HashedDataSet<>();
        new RestaurantXMLReader().loadFromXML(new File("data/input/zomato.xml"), "/restaurants/restaurant", dataRestaurantZomato);
        
        
		/*
		 * HashedDataSet<Restaurant, Attribute> dataRestaurantYelp = new
		 * HashedDataSet<>(); new RestaurantXMLReader().loadFromXML(new
		 * File("data/input/yelp.xml"), "/restaurants/restaurant", dataRestaurantYelp);
		 */


        HashedDataSet<Restaurant, Attribute> dataRestaurantYP = new HashedDataSet<>();
        new RestaurantXMLReader().loadFromXML(new File("data/input/yellow.xml"), "/restaurants/restaurant", dataRestaurantYP);
        
        System.out.println("*\n*\tCompleted Loading datasets\n*");

        // load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File(
                "data/goldstandard/Z_YP_GS.csv"));

        // create a matching rule
        LinearCombinationMatchingRule<Restaurant, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
                0.7);
        matchingRule.activateDebugReport("data/output/debugResultsMatchingRuleBK.csv", 1000, gsTest);

        // add comparators
        matchingRule.addComparator(new RestaurantNameComparatorJaccard(), 0.5);
        matchingRule.addComparator(new RestaurantAddressComparatorJaccard(), 0.5);


        // create a blocker (blocking strategy)
        StandardRecordBlocker<Restaurant, Attribute> blocker = new StandardRecordBlocker<Restaurant, Attribute>(new RestaurantBlockingByCity());
//		NoBlocker<Movie, Attribute> blocker = new NoBlocker<>();
//		SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByTitleGenerator(), 1);
        blocker.setMeasureBlockSizes(true);
        //Write debug results to file:
        blocker.collectBlockSizeData("data/output/debugResultsBlockingBK.csv", 100);

        // Initialize Matching Engine
        MatchingEngine<Restaurant, Attribute> engineZomatoYellow = new MatchingEngine<>();

        // Execute the matching
        System.out.println("*\n*\tRunning identity resolution\n*");
        
        Stopwatch stopwatch = Stopwatch.createStarted(); // start measure time
        
        Processable<Correspondence<Restaurant, Attribute>> correspondencesZomatoYellow = engineZomatoYellow.runIdentityResolution(
                dataRestaurantZomato, dataRestaurantYP, null, matchingRule,
                blocker);
        
        stopwatch.stop(); // stop time
        System.out.println("Time elapsed: "+ stopwatch.elapsed(TimeUnit.MILLISECONDS));

        // Create a top-1 global matching
        correspondencesZomatoYellow = engineZomatoYellow.getTopKInstanceCorrespondences(correspondencesZomatoYellow, 1, 0.0);

        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/zomato_yelp_correspondencesBK.csv"), correspondencesZomatoYellow);

        System.out.println("*\n*\tEvaluating result\n*");
        // evaluate your result
        MatchingEvaluator<Restaurant, Attribute> evaluator = new MatchingEvaluator<Restaurant, Attribute>();
        Performance perfTest = evaluator.evaluateMatching(correspondencesZomatoYellow,
                gsTest);

        // print the evaluation result
        System.out.println("Zomato <-> Yelp");
        System.out.println(String.format(
                "Precision: %.4f",perfTest.getPrecision()));
        System.out.println(String.format(
                "Recall: %.4f",	perfTest.getRecall()));
        System.out.println(String.format(
                "F1: %.4f",perfTest.getF1()));
    }
}
