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
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class TestACAC_batch_holdout_mergeAttribute_phase3 {


    String targetClassName;

    String fileName;

    double minSup;
    double minConf;
    double minAllConf;

    String[] bestAttributes;

    String[] subAttributes;


    public TestACAC_batch_holdout_mergeAttribute_phase3(String targetClassName, String fileName, double minSup, double minConf, double minAllConf, String[] bestAttributes, String[] subAttributes) {
        try {
            this.fileName = fileName;
            this.targetClassName = targetClassName;
            this.minSup = minSup;
            this.minConf = minConf;
            this.minAllConf = minAllConf;
            this.bestAttributes = bestAttributes;
            this.subAttributes = subAttributes;

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String run() throws Exception {
        String bestResult = "";
        Double accuracy = 0.0;
        int loop = 0;
        List<String> allAccuracyStatus = new ArrayList<>();

        double highestAccuracy = 0.0;
        Double high = 0.0;
        String thisRs="The accuracy when adding each attributes into your attributes:";

        for (int i = 0; i < subAttributes.length; i++) {
            generateRules g = new generateRules();
            g.tap_chinh_tot_nhat_ht = bestAttributes;
            g.tap_sub_tot_nhat_ht = subAttributes;
            g.tap_chinh = g.avoidAlias(g.tap_chinh_tot_nhat_ht);
            g.tap_sub = g.avoidAlias(g.tap_sub_tot_nhat_ht);
            g.add1Attributes_from_sub(i);
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
            thisRs+="\n+ add "+g.tap_sub_tot_nhat_ht[i]+" get accuracy = "+temp;
            if (temp > accuracy) {
                accuracy = temp;
                bestResult = "dataset with ACAC when add "+g.tap_sub_tot_nhat_ht[i]+" get highest accuracy = "+accuracy+" \n";
                bestResult += "\nbest_attributes = {";
                for (int i1 = 0; i1 < g.tap_chinh.length; i1++) {
                    if (g.tap_chinh[i1].equals(targetClassName)) continue;
                    if (i1 == g.tap_chinh.length - 1) {
                        bestResult += g.tap_chinh[i1] + ", "+g.tap_sub_tot_nhat_ht[i];
                    } else {
                        bestResult += g.tap_chinh[i1] + ", ";
                    }
                }
                bestResult += "}";
                g.tap_chinh_tot_nhat_ht = g.tap_chinh;
                g.tap_sub_tot_nhat_ht = g.tap_sub;
                bestResult += "\n" + allResults.toPrintStats();
                i = 0;

            }
        }
        return thisRs+"\n"+bestResult;

    }

    private String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestACAC_batch_holdout_HC.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }

}
