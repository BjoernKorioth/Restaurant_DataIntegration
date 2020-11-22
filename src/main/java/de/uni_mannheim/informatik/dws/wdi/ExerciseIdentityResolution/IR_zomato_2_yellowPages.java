package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.RestaurantBlockingByCity;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.RestaurantBlockingByZipCodeTwoDigits;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.RestaurantBlockingKeyByZipCode;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.RestaurantAddressComparatorLevenshtein;
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

public class IR_zomato_2_yellowPages {
    public static void main( String[] args ) throws Exception
    {
        // loading data
        System.out.println("*\n*\tLoading datasets\n*");
        HashedDataSet<Restaurant, Attribute> dataRestaurantZomato = new HashedDataSet<>();
        new RestaurantXMLReader().loadFromXML(new File("data/input/zomato.xml"), "/restaurants/restaurant", dataRestaurantZomato);

        //new RestaurantXMLReader().loadFromXML(new File("data/input/zomato.xml"), "/restaurants/restaurant", dataRestaurantYP);
        HashedDataSet<Restaurant, Attribute> dataRestaurantYP = new HashedDataSet<>();
        new RestaurantXMLReader().loadFromXML(new File("data/input/yellow_pages.xml"), "/restaurants/restaurant", dataRestaurantYP);

        System.out.println("*\n*\tCompleted Loading datasets\n*");

        // load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File(
                "data/goldstandard/GS_Zomato_2_YP.csv"));

        // create a matching rule
        LinearCombinationMatchingRule<Restaurant, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
                0.7);
        matchingRule.activateDebugReport("data/output/Zomato_2_YP_debugResultsMatchingRule.csv", 1000, gsTest);

        // add comparators
        //matchingRule.addComparator(new RestaurantNameComparatorJaccard(), 0.3);
        //matchingRule.addComparator(new RestaurantAddressComparatorJaccard(), 0.2);
        matchingRule.addComparator(new RestaurantNameComparatotLevenshtein(), 0.6);
        matchingRule.addComparator(new RestaurantAddressComparatorLevenshtein(), 0.4);
        // create a blocker (blocking strategy)
        StandardRecordBlocker<Restaurant, Attribute> blocker = new StandardRecordBlocker<Restaurant, Attribute>(new RestaurantBlockingKeyByZipCode());
//		NoBlocker<Movie, Attribute> blocker = new NoBlocker<>();
//		SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByTitleGenerator(), 1);
        blocker.setMeasureBlockSizes(true);
        //Write debug results to file:
        blocker.collectBlockSizeData("data/output/Zomato_2_YP_DebugResultsBlocking.csv", 100);

        // Initialize Matching Engine
        MatchingEngine<Restaurant, Attribute> engineZomato_YellowPages = new MatchingEngine<>();

        // Execute the matching
        System.out.println("*\n*\tRunning identity resolution\n*");
        Processable<Correspondence<Restaurant, Attribute>> correspondencesZomatoYellowPages = engineZomato_YellowPages.runIdentityResolution(
                dataRestaurantZomato, dataRestaurantYP, null, matchingRule,
                blocker);

        // Create a top-1 global matching
        correspondencesZomatoYellowPages = engineZomato_YellowPages.getTopKInstanceCorrespondences(correspondencesZomatoYellowPages, 1, 0);

        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/Zomato_2_YP_correspondences.csv"), correspondencesZomatoYellowPages);

        System.out.println("*\n*\tEvaluating result\n*");
        // evaluate your result
        MatchingEvaluator<Restaurant, Attribute> evaluator = new MatchingEvaluator<Restaurant, Attribute>();
        Performance perfTest = evaluator.evaluateMatching(correspondencesZomatoYellowPages,
                gsTest);

        // print the evaluation result
        System.out.println("Zomato <-> YellowPages");
        System.out.println(String.format(
                "Precision: %.4f",perfTest.getPrecision()));
        System.out.println(String.format(
                "Recall: %.4f",	perfTest.getRecall()));
        System.out.println(String.format(
                "F1: %.4f",perfTest.getF1()));
    }
}
