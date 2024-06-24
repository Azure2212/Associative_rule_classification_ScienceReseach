package ca.pfv.spmf.algorithms.classifiers.acac.ACACEntropyAPP;


import ca.pfv.spmf.algorithms.classifiers.acac.AlgoACAC;
import ca.pfv.spmf.algorithms.classifiers.acac.EntopyHandle;
import ca.pfv.spmf.algorithms.classifiers.acac.generateRules;
import ca.pfv.spmf.algorithms.classifiers.data.StringDataset;
import ca.pfv.spmf.algorithms.classifiers.general.ClassificationAlgorithm;
import ca.pfv.spmf.algorithms.classifiers.general.Evaluator;
import ca.pfv.spmf.algorithms.classifiers.general.OverallResults;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TestACAC_batch_holdout_Attributes_Selection {


    String targetClassName;

    String fileName;

    double entropyThreshold;


    EntopyHandle entropyHandle;


    public TestACAC_batch_holdout_Attributes_Selection(EntopyHandle entropyHandle, String targetClassName, double entropyThreshold, String fileName) {
        try {
            this.fileName = fileName;
            this.targetClassName = targetClassName;
            this.entropyThreshold = entropyThreshold;
            this.entropyHandle = entropyHandle;


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String run() throws Exception {
        String detailEntropy="\nDetail attributes according to entropy :";

        entropyHandle.doEntropy();

        for(Map.Entry<String, Double> entry: entropyHandle.getEntropyList().entrySet()){
            String key = entry.getKey();
            Double value= entry.getValue();
            detailEntropy +="\n{"+key+"} : "+value;
        }
        generateRules g = new generateRules();
        g.tap_chinh_tot_nhat_ht = entropyHandle.getAllAttributes(entropyThreshold);
        String bestResult = "There are " + (g.tap_chinh_tot_nhat_ht.length - 1) + " attributes satisfy entropyThreshold = " + entropyThreshold + "\n";
        for (int i1 = 0; i1 < g.tap_chinh_tot_nhat_ht.length-1; i1++) {

            bestResult += g.tap_chinh_tot_nhat_ht[i1];
            if(i1 !=g.tap_chinh_tot_nhat_ht.length-2) bestResult += ", ";

        }
        return bestResult+detailEntropy;


    }


}
