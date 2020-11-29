package de.uni_mannheim.informatik.dw.wdi.Fusion;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

import org.apache.logging.log4j.Logger;

import de.uni_mannheim.informatik.dws.wdi.Fusion.model.RestaurantXMLReader;
import de.uni_mannheim.informatik.dws.wdi.Fusion.evaluation.AddressEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.Fusion.evaluation.NameEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.Fusion.fusers.AddressFuserShortest;
import de.uni_mannheim.informatik.dws.wdi.Fusion.fusers.NameFuserFavourSource;
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

import de.uni_mannheim.informatik.dw.wdi.Fusion.*;

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

		private static final org.slf4j.Logger logger = WinterLogManager.activateLogger("trace");
		
	    public static void main( String[] args ) throws Exception
	    {
			// Load the Data into FusibleDataSet
			System.out.println("*\n*\tLoading datasets\n*");
			FusibleDataSet<Restaurant, Attribute> ds1 = new FusibleHashedDataSet<>();
			new RestaurantXMLReader().loadFromXML(new File("data/input/zomato.xml"), "/restaurants/restaurant", ds1);
//			ds1.printDataSetDensityReport();

			FusibleDataSet<Restaurant, Attribute> ds2 = new FusibleHashedDataSet<>();
			new RestaurantXMLReader().loadFromXML(new File("data/input/yelp.xml"), "/restaurants/restaurant", ds2);
//			ds2.printDataSetDensityReport();

			FusibleDataSet<Restaurant, Attribute> ds3 = new FusibleHashedDataSet<>();
			new RestaurantXMLReader().loadFromXML(new File("data/input/yellow_pages.xml"), "/restaurants/restaurant", ds3);
//			ds3.printDataSetDensityReport();

			// Maintain Provenance
			// Scores (e.g. from rating)
			ds1.setScore(1.0);
			ds2.setScore(3.0);
			ds3.setScore(2.0);
//			
			// Date (e.g. last update)
			DateTimeFormatter formatter = new DateTimeFormatterBuilder()
			        .appendPattern("yyyy-MM-dd")
			        .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
			        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
			        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
			        .toFormatter(Locale.ENGLISH);
			
			ds1.setDate(LocalDateTime.parse("2012-01-01", formatter));
			ds2.setDate(LocalDateTime.parse("2010-01-01", formatter));
			ds3.setDate(LocalDateTime.parse("2008-01-01", formatter));

			// load correspondences
			System.out.println("*\n*\tLoading correspondences\n*");
			CorrespondenceSet<Restaurant, Attribute> correspondences = new CorrespondenceSet<>();
			correspondences.loadCorrespondences(new File("data/input/correspondance/YP_Yelp_correspondencesML.csv"),ds3, ds2);
			correspondences.loadCorrespondences(new File("data/input/correspondance/Zomato_Yelp_correspondencesML.csv"),ds1, ds2);
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
			strategy.addAttributeFuser(Restaurant.NAME, new NameFuserFavourSource(),new NameEvaluationRule());

			
			// create the fusion engine
			DataFusionEngine<Restaurant, Attribute> engine = new DataFusionEngine<Restaurant, Attribute>(strategy);

			// print consistency report
			engine.printClusterConsistencyReport(correspondences, null);
			
			// print record groups sorted by consistency
			engine.writeRecordGroupsByConsistency(new File("data/output/recordGroupConsistencies.csv"), correspondences, null);

			
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
