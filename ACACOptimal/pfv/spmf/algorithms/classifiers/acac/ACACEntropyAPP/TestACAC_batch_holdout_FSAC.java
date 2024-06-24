package ca.pfv.spmf.algorithms.classifiers.acac.ACACEntropyAPP;

import ca.pfv.spmf.algorithms.classifiers.acac.AlgoACAC;
import ca.pfv.spmf.algorithms.classifiers.acac.EntopyHandle;
import ca.pfv.spmf.algorithms.classifiers.acac.generateRules;
import ca.pfv.spmf.algorithms.classifiers.data.StringDataset;
import ca.pfv.spmf.algorithms.classifiers.general.ClassificationAlgorithm;
import ca.pfv.spmf.algorithms.classifiers.general.Evaluator;
import ca.pfv.spmf.algorithms.classifiers.general.OverallResults;
import ca.pfv.spmf.test.MainTestACAC_batch_holdout_HC;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

public class TestACAC_batch_holdout_FSAC {

    EntopyHandle entropyHandle;
    String targetClassName;

    String fileName;

    double minSup = 0.1;
    double minConf = 0.8;
    double minAllConf = 0.5;


    public TestACAC_batch_holdout_FSAC(EntopyHandle entropyHandle, String fileName, double minSup, double minConf, double minAllConf) {
        try {
            this.fileName = fileName;
            this.entropyHandle = entropyHandle;
            this.targetClassName = entropyHandle.getClassName();
            this.minSup = minSup;
            this.minConf = minConf;
            this.minAllConf = minAllConf;

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String run() throws Exception {
        String bestResult = "";
        entropyHandle.doEntropy();
        Double[] listEntropy = entropyHandle.getEntropyData();
        Double accuracy = 0.0;
        Double theBestEntropy = 1.0;

        for (int i = 0; i < listEntropy.length; i++) {
            generateRules g = new generateRules();
            g.tap_chinh_tot_nhat_ht = entropyHandle.getAllAttributes(listEntropy[i]);
            g.tap_chinh = g.avoidAlias(g.tap_chinh_tot_nhat_ht);
            //g.add1Attributes_from_sub(i);

            System.out.println("========================== Set Dataset ===============================");

            System.out.println("========= Step 1: Read the dataset ==========");


            // Load the dataset
            String datasetPath = fileName;
            StringDataset dataset = new StringDataset(g, datasetPath, targetClassName);

            // Print stats about the dataset
            dataset.printStats();

            System.out.println("==== Step 2: Training:  Apply the algorithm to build a model (a set of rules) ===");
            // Parameters of the algorithm

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
            if (temp >= accuracy) {
                theBestEntropy = listEntropy[i];
                accuracy = temp;
                bestResult = "dataset with ACAC get highest accuracy \nat entropy=" + theBestEntropy;
                bestResult += "\nbest_attributes = {";
                for (int i1 = 0; i1 < g.tap_chinh.length; i1++) {
                    if(g.tap_chinh[i1].equals(targetClassName))continue;
                    if (i1 == g.tap_chinh.length - 1) {
                        bestResult += g.tap_chinh[i1];
                    } else {
                        bestResult += g.tap_chinh[i1] + ", ";
                    }
                }
                bestResult += "}\n";
                bestResult += "\n"+allResults.toPrintStats();

            }

        }
        return bestResult;

    }

    private String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestACAC_batch_holdout_HC.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }

}
