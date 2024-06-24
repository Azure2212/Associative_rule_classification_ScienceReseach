package ca.pfv.spmf.test;

/* This file is copyright (c) 2021 Philippe Fournier-Viger
 *
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 *
 * SPMF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Vector;

import ca.pfv.spmf.algorithms.classifiers.acac.AlgoACAC;
import ca.pfv.spmf.algorithms.classifiers.acac.generateRules;
import ca.pfv.spmf.algorithms.classifiers.data.StringDataset;
import ca.pfv.spmf.algorithms.classifiers.general.ClassificationAlgorithm;
import ca.pfv.spmf.algorithms.classifiers.general.Evaluator;
import ca.pfv.spmf.algorithms.classifiers.general.OverallResults;

/**
 * Example of how to run the ACAC algorithm
 *
 * @author Philippe Fournier-Viger, 2021
 */
public class MainTestACAC_batch_holdout {


    public static void main(String[] args) throws Exception {



        generateRules g = new generateRules();
        g.tap_chinh = g.avoidAlias(g.tap_chinh_tot_nhat_ht);
        g.tap_chinh_tot_nhat_ht=new String[]{ "word_freq_make",  "word_freq_address",  "word_freq_all",  "word_freq_3d",  "word_freq_our",  "word_freq_over",  "word_freq_remove",  "word_freq_internet",  "word_freq_order",  "word_freq_mail",  "word_freq_receive",  "word_freq_will",  "word_freq_people",  "word_freq_report",  "word_freq_addresses",  "word_freq_free",  "word_freq_business",  "word_freq_email",  "word_freq_you",  "spam" };

        //g.add1Attributes_from_sub(i);

        System.out.println("========================== Set Dataset ===============================");

        System.out.println("========= Step 1: Read the dataset ==========");

        // We choose "play" as the target attribute that we want to predict using other attributes
        String targetClassName = "class";

        // Load the dataset
        String datasetPath = "C:\\Users\\Computer\\Desktop\\ACACAPP\\page2\\pfv\\spmf\\test\\MushroomDataSet";
        StringDataset dataset = new StringDataset(g, datasetPath, targetClassName);

        //  If the dataset is in ARFF format, then use these lines instead:
//		String datasetPath = fileToPath("weather-train.arff");
//		ARFFDataset dataset = new ARFFDataset(datasetPath, targetClassName);

        // If the dataset is in CSV format, then use these lines instead:
//		String datasetPath = fileToPath("tennisExtendedCSV.txt");
//		CSVDataset dataset = new CSVDataset(datasetPath, targetClassName);

        // Print stats about the dataset
        dataset.printStats();

        // For debugging (optional)
//		dataset.printInternalRepresentation();
//		dataset.printStringRepresentation();

        System.out.println("==== Step 2: Training:  Apply the algorithm to build a model (a set of rules) ===");
        // Parameters of the algorithm
        double minSup = 0.1;
        double minConf = 0.8;
        double minAllConf = 0.5;

        // Create the algorithm
        ClassificationAlgorithm algorithmACAC = new AlgoACAC(minSup, minConf, minAllConf);

        // We create an object Evaluator to run the experiment using k-fold cross validation
        Evaluator experiment1 = new Evaluator();

        // The following line indicates that 50% of the dataset will be used for training.
        // The rest of the dataset will be used for testing.
        double percentage = 0.8;

        // We run the experiment
        ClassificationAlgorithm[] algorithms = new ClassificationAlgorithm[]{algorithmACAC};
        OverallResults allResults = experiment1.trainAndRunClassifiersHoldout(algorithms, dataset, percentage);

        // Save statistics about the execution to files (optional)
        String forTrainingPath = "outputReportForTraining.txt";
        String onTrainingPath = "outputReportOnTraining.txt";
        String onTrestingPath = "outputReportOnTesting.txt";
        allResults.saveMetricsResultsToFile(forTrainingPath, onTrainingPath, onTrestingPath);

        // Print statistics to the console (optional)
        allResults.printStats();
        double temp = allResults.accuracy;

        temp = Math.round(temp * 10000.0) / 10000.0;
        System.out.println("coi accu nee:" + temp);
//				for(int i1=0;i1<g.tap_chinh_tot_nhat_ht.length;i1++){
//					System.out.print("'"+g.tap_chinh_tot_nhat_ht[i1]+"', ");
//				}



    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestACAC_batch_holdout_HC.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }
}
