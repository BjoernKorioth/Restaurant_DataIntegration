package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.RestaurantBlockingByCity;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.RestaurantAddressComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.RestaurantAddressComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.RestaurantNameComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.RestaurantNameComparatotLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Restaurant;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.RestaurantXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import org.slf4j.Logger;

import java.io.File;

public class IR_yelp_2_yellowPages_machine_learning {
    private static final Logger logger = WinterLogManager.activateLogger("default");

    public static void main( String[] args ) throws Exception
    {
        // loading data
        System.out.println("*\n*\tLoading datasets\n*");

        HashedDataSet<Restaurant, Attribute> dataRestaurantZomato = new HashedDataSet<>();
        new RestaurantXMLReader().loadFromXML(new File("data/input/zomato.xml"), "/restaurants/restaurant", dataRestaurantZomato);

        HashedDataSet<Restaurant, Attribute> dataRestaurantYelp = new HashedDataSet<>();
        new RestaurantXMLReader().loadFromXML(new File("data/input/yelp.xml"), "/restaurants/restaurant", dataRestaurantYelp);

        //new RestaurantXMLReader().loadFromXML(new File("data/input/zomato.xml"), "/restaurants/restaurant", dataRestaurantYP);
        HashedDataSet<Restaurant, Attribute> dataRestaurantYP = new HashedDataSet<>();
        new RestaurantXMLReader().loadFromXML(new File("data/input/yellow.xml"), "/restaurants/restaurant", dataRestaurantYP);

        System.out.println("*\n*\tCompleted Loading datasets\n*");

        // load the training set
        MatchingGoldStandard gsTraining = new MatchingGoldStandard();
        gsTraining.loadFromCSVFile(new File(""));

        // create a matching rule
        String options[] = new String[] { "-S" };
        String modelType = "SimpleLogistic"; // use a logistic regression
        WekaMatchingRule<Restaurant, Attribute> matchingRule = new WekaMatchingRule<>(0.7, modelType, options);
        matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsTraining);

        // add comparators
        matchingRule.addComparator(new RestaurantNameComparatorJaccard());
        matchingRule.addComparator(new RestaurantNameComparatotLevenshtein());
        matchingRule.addComparator(new RestaurantAddressComparatorJaccard());
        matchingRule.addComparator(new RestaurantAddressComparatorLevenshtein());

        // train the matching rule's model
        System.out.println("*\n*\tLearning matching rule\n*");
        RuleLearner<Restaurant, Attribute> learner = new RuleLearner<>();
        learner.learnMatchingRule(dataRestaurantZomato, dataRestaurantYelp, null, matchingRule, gsTraining);
        System.out.println(String.format("Matching rule is:\n%s", matchingRule.getModelDescription()));

        // create a blocker (blocking strategy)
        StandardRecordBlocker<Restaurant, Attribute> blocker = new StandardRecordBlocker<Restaurant, Attribute>(new RestaurantBlockingByCity());
//		SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByDecadeGenerator(), 1);
        blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);

        // Initialize Matching Engine
        MatchingEngine<Restaurant, Attribute> engine = new MatchingEngine<>();

        // Execute the matching
        System.out.println("*\n*\tRunning identity resolution\n*");
        Processable<Correspondence<Restaurant, Attribute>> correspondences = engine.runIdentityResolution(
                dataRestaurantZomato, dataRestaurantYelp, null, matchingRule,
                blocker);

        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/zomato_2_yelp_correspondences.csv"), correspondences);

        // load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File(
                "data/goldstandard/gs_academy_awards_2_actors_test.csv"));

        // evaluate your result
        System.out.println("*\n*\tEvaluating result\n*");
        MatchingEvaluator<Restaurant, Attribute> evaluator = new MatchingEvaluator<Restaurant, Attribute>();
        Performance perfTest = evaluator.evaluateMatching(correspondences,
                gsTest);

        // print the evaluation result
        System.out.println("Academy Awards <-> Actors");
        System.out.println(String.format(
                "Precision: %.4f",perfTest.getPrecision()));
        System.out.println(String.format(
                "Recall: %.4f",	perfTest.getRecall()));
        System.out.println(String.format(
                "F1: %.4f",perfTest.getF1()));
    }
}
