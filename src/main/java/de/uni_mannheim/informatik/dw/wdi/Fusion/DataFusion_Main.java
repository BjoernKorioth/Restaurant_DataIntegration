package de.uni_mannheim.informatik.dw.wdi.Fusion;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

import de.uni_mannheim.informatik.dws.wdi.Fusion.evaluation.*;
import de.uni_mannheim.informatik.dws.wdi.Fusion.fusers.*;
import org.apache.logging.log4j.Logger;

import de.uni_mannheim.informatik.dws.wdi.Fusion.model.RestaurantXMLReader;
import de.uni_mannheim.informatik.dws.wdi.Fusion.model.Restaurant;
import de.uni_mannheim.informatik.dws.wdi.Fusion.model.RestaurantXMLFormatter;
import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEngine;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEvaluator;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionStrategy;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleDataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleHashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroupFactory;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;



public class DataFusion_Main {
	
		/*
		 * Logging Options:
		 * 		default: 	level INFO	- console
		 * 		trace:		level TRACE     - console
		 * 		infoFile:	level INFO	- console/file
		 * 		traceFile:	level TRACE	- console/file
		 *  
		 * To set the log level to trace and write the log to winter.log and console, 
		 * activate the "traceFile" logger as follows:
		 *     private static final Logger logger = WinterLogManager.activateLogger("traceFile");
		 *
		 */

	private static final org.slf4j.Logger logger = WinterLogManager.activateLogger("traceFile");
		
	    public static void main( String[] args ) throws Exception
	    {
			// Load the Data into FusibleDataSet
			System.out.println("*\n*\tLoading datasets\n*");
			FusibleDataSet<Restaurant, Attribute> ds1 = new FusibleHashedDataSet<>();
			new RestaurantXMLReader().loadFromXML(new File("data/input/zomato.xml"), "/restaurants/restaurant", ds1);
			ds1.printDataSetDensityReport();

			FusibleDataSet<Restaurant, Attribute> ds2 = new FusibleHashedDataSet<>();
			new RestaurantXMLReader().loadFromXML(new File("data/input/yelp.xml"), "/restaurants/restaurant", ds2);
			ds2.printDataSetDensityReport();

			FusibleDataSet<Restaurant, Attribute> ds3 = new FusibleHashedDataSet<>();
			new RestaurantXMLReader().loadFromXML(new File("data/input/yellow_pages.xml"), "/restaurants/restaurant", ds3);
			ds3.printDataSetDensityReport();

			// Maintain Provenance
			// Scores (e.g. from rating)
			ds1.setScore(2.0);
			ds2.setScore(3.0);
			ds3.setScore(1.0);


			// load correspondences
			System.out.println("*\n*\tLoading correspondences\n*");
			CorrespondenceSet<Restaurant, Attribute> correspondences = new CorrespondenceSet<>();
			correspondences.loadCorrespondences(new File("data/input/correspondance/Zomato_Yelp_correspondencesML.csv"),ds1, ds2);
			correspondences.loadCorrespondences(new File("data/input/correspondance/YP_Yelp_correspondencesML.csv"),ds3, ds2);
			correspondences.loadCorrespondences(new File("data/input/correspondance/Zomato_YP_correspondencesML.csv"),ds1, ds3);
			// write group size distribution
			correspondences.printGroupSizeDistribution();

			// load the gold standard
			DataSet<Restaurant, Attribute> gs = new FusibleHashedDataSet<>();
			new RestaurantXMLReader().loadFromXML(new File("data/goldstandard/Fusion/Golden_Standard_Fusion.xml"), "/restaurants/restaurant", gs);


			// define the fusion strategy
			DataFusionStrategy<Restaurant, Attribute> strategy = new DataFusionStrategy<>(new RestaurantXMLReader());

			// write debug results to file
			strategy.activateDebugReport("data/output/debugResultsDatafusion.csv", -1, gs);

			// add attribute fusers
			strategy.addAttributeFuser(Restaurant.ADDRESS, new AddressFuserShortest(),new AddressEvaluationRule());

			strategy.addAttributeFuser(Restaurant.CARD, new CardFuserVoting(), new CardEvaluationRule());

			strategy.addAttributeFuser(Restaurant.CITY,new CityFuserVoting(),new CityEvaluationRule());

			strategy.addAttributeFuser(Restaurant.ZIP,new ZIPFuserVoting(),new ZIPEvaluationRule());
			strategy.addAttributeFuser(Restaurant.STATE,new StateFuserVoting(),new StateEvaluationRule());

			strategy.addAttributeFuser(Restaurant.NAME,new NameFuserFavourSource(),new NameEvaluationRule());

			strategy.addAttributeFuser(Restaurant.WEBSITE,new WebsiteFuserShortest(),new WebsiteEvaluationRule());
			strategy.addAttributeFuser(Restaurant.PRICE,new PriceFuserFavourSource(),new PriceEvaluationRule());

			strategy.addAttributeFuser(Restaurant.CUISINES,new CuisineFuserFavourSource(),new CuisineEvaluationRule());

//		strategy.addAttributeFuser(Restaurant.NEIGHBORHOODS,new NeighborhoodFuserFavourSource(),new NeighborhoodEvaluationRule());





			//avg of rating fuser- average, evalution- threshold margin
			//

			// create the fusion engine
			DataFusionEngine<Restaurant, Attribute> engine = new DataFusionEngine<Restaurant, Attribute>(strategy);

			// print consistency report
			engine.printClusterConsistencyReport(correspondences, null);

			// run the fusion
			System.out.println("*\n*\tRunning data fusion\n*");
			FusibleDataSet<Restaurant, Attribute> fusedDataSet = engine.run(correspondences, null);
			fusedDataSet.printDataSetDensityReport();
			// write the result
			new RestaurantXMLFormatter().writeXML(new File("data/output/fused.xml"), fusedDataSet);

			// evaluate
			System.out.println("*\n*\tEvaluating results\n*");
			DataFusionEvaluator<Restaurant, Attribute> evaluator = new DataFusionEvaluator<>(
					strategy, new RecordGroupFactory<Restaurant, Attribute>());
			double accuracy = evaluator.evaluate(fusedDataSet, gs, null);

			logger.info(String.format("Accuracy: %.2f", accuracy));
		}

}
