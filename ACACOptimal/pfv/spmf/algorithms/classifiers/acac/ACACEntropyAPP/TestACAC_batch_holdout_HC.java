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

public class TestACAC_batch_holdout_HC {


    String targetClassName;

    String fileName;

    double minSup = 0.1;
    double minConf = 0.8;
    double minAllConf = 0.5;

    String[] bestAttributes;

    String[] subAttributes;


    public TestACAC_batch_holdout_HC(String targetClassName, String fileName, double minSup, double minConf, double minAllConf, String[] bestAttributes, String[] subAttributes) {
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
        Double theBestEntropy = 1.0;
        int loop = 0;
        List<Double> hillChange = new ArrayList<>();

        int TimesChangeHill = 0;
        double highestAccuracy = 0.8708;
        int index = 0;
        int reach = 100;
        Double high = 0.8708;
        Vector<String[]> betterInitial = new Vector<>();
        Vector<Double> betterAccuracy = new Vector<>();
        generateRules g = new generateRules();
        g.allAtributes = new String[]{};
        g.tap_chinh_tot_nhat_ht = bestAttributes;
        g.tap_sub_tot_nhat_ht = subAttributes;
        int a = 0;
        for (int i = 0; i < reach; i++) {
            g.tap_chinh = g.avoidAlias(g.tap_chinh_tot_nhat_ht);
            g.tap_sub = g.avoidAlias(g.tap_sub_tot_nhat_ht);
            //g.add1Attributes_from_sub(i);

            g.DogenerateRules();
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
            if (temp > accuracy) {
                accuracy = temp;
                bestResult = "dataset with ACAC get highest accuracy after "+hillChange.size()+" change hill\n";
                for(int i1=0;i1<hillChange.size();i1++){
                    if(i1==hillChange.size()-1){
                        bestResult += hillChange.get(i1);
                    }else{
                        bestResult += hillChange.get(i1)+" -> ";
                    }
                }
                bestResult += "\nstep loop = "+index;
                bestResult += "\nbest_attributes = {";
                for (int i1 = 0; i1 < g.tap_chinh.length; i1++) {
                    if (g.tap_chinh[i1].equals(targetClassName)) continue;
                    if (i1 == g.tap_chinh.length - 1) {
                        bestResult += g.tap_chinh[i1];
                    } else {
                        bestResult += g.tap_chinh[i1] + ", ";
                    }
                }
                bestResult += "}";
                g.tap_chinh_tot_nhat_ht = g.tap_chinh;
                g.tap_sub_tot_nhat_ht = g.tap_sub;
                bestResult += "\n" + allResults.toPrintStats();
                i = 0;
                hillChange.add(temp);

            }
        System.out.println("change hill = "+hillChange.size()+" running at ="+i);
            index++;
        }
        return bestResult;

    }

    private String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestACAC_batch_holdout_HC.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }

}
