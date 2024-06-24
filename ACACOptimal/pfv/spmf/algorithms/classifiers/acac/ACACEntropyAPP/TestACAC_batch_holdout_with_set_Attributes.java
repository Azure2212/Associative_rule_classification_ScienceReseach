package ca.pfv.spmf.algorithms.classifiers.acac.ACACEntropyAPP;

import ca.pfv.spmf.algorithms.classifiers.acac.*;
import ca.pfv.spmf.algorithms.classifiers.data.Instance;
import ca.pfv.spmf.algorithms.classifiers.data.StringDataset;
import ca.pfv.spmf.algorithms.classifiers.general.*;

import java.util.ArrayList;
import java.util.List;

public class TestACAC_batch_holdout_with_set_Attributes {
    String targetClassName;

    String fileName;

    double minSup;
    double minConf;
    double minAllConf;

    StringDataset dataset;

    String[] bestAttributes;

    public List<RuleACAC> rules;

    public String getBestResult() {
        return bestResult;
    }

    public void setBestResult(String bestResult) {
        this.bestResult = bestResult;
    }

    //result
    String bestResult;

    public TestACAC_batch_holdout_with_set_Attributes() {
        this.rules = new ArrayList<>();
        this.bestResult = "";
    }

    public TestACAC_batch_holdout_with_set_Attributes(String targetClassName, String fileName, double minSup, double minConf, double minAllConf, String[] bestAttributes) {
        try {
            this.fileName = fileName;
            this.targetClassName = targetClassName;
            this.minSup = minSup;
            this.minConf = minConf;
            this.minAllConf = minAllConf;
            this.bestAttributes = bestAttributes;
            this.rules = new ArrayList<>();
            this.bestResult = "";



        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void run() throws Exception {
        bestResult = "";
        //entropyHandle.doEntropy();

        Double accuracy = 0.0;
        Double theBestEntropy = 1.0;


        generateRules g = new generateRules();
        // g.tap_chinh_tot_nhat_ht = entropyHandle.getAllAttributes(entroyThreshold);
        g.tap_chinh_tot_nhat_ht = bestAttributes;
        g.tap_chinh = g.avoidAlias(g.tap_chinh_tot_nhat_ht);
        //g.add1Attributes_from_sub(i);

        System.out.println("========================== Set Dataset ===============================");

        System.out.println("========= Step 1: Read the dataset ==========");


        // Load the dataset
        String datasetPath = fileName;
        dataset = new StringDataset(g, datasetPath, targetClassName);

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
        for (Rule r : experiment1.rules) {
            if (r instanceof RuleACAC) {
                this.rules.add((RuleACAC) r);
            }
        }
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


        bestResult += " A mount of best attributes =" + (g.tap_chinh_tot_nhat_ht.length - 1);

        bestResult += "\n" + allResults.toPrintStats();




    }

    public String prediction(String[] value2Predict) {
        //ring-type, odor, spore-print-color, gill-color, stalk-surface-above-ring
        //p, p, k, k, s
        try {
            Classifier classifier = new ClassifierACAC(rules);
            Instance instance = dataset.stringToInstance(value2Predict);
            short result = classifier.predict(instance);
            return("    The predicted value is: " + dataset.getStringCorrespondingToItem(result));

        } catch (Exception e) {
            System.out.println("error whiling prediction");
            return "có lỗi rồi";
        }
    }
}
