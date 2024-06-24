package ca.pfv.spmf.algorithms.classifiers.acac.ACACEntropyAPP.ACACThread;

import ca.pfv.spmf.algorithms.classifiers.acac.ACACEntropyAPP.*;
import ca.pfv.spmf.algorithms.classifiers.acac.EntopyHandle;

import javax.swing.*;

public class ACAC_Train_Predict_Thread extends Thread {

    public String type; // (training) or (test base on train)
    public TestACAC_batch_holdout_with_set_Attributes test;
    JTextArea Result;

    JComboBox classText, typeDoing;
    JTextField minSupField;
    JTextField minConfField;
    JTextField minAllConfField;
    JButton fileChose, runButton;
    JTextField inputText;

    JTextField data2PredictText;
    String fileName;
    double minSup;
    double minConf;
    double minAllConf;

    String bestResult;

    String[] inputAttributes;

    String[] data2Predict;

    public ACAC_Train_Predict_Thread(String fileName, double minSup, double minConf, double minAllConf, String[] inputAttributes, String[] data2Predict, String bestResult) {
        this.fileName = fileName;
        this.minSup = minSup;
        this.minConf = minConf;
        this.minAllConf = minAllConf;
        this.inputAttributes = inputAttributes;
        this.bestResult = bestResult;
        this.data2Predict = data2Predict;
    }

    public void declareComponent(JTextArea Result, JTextField minSup, JTextField minConf, JTextField minAllConf, JComboBox classText, JTextField inputText, JTextField data2PredictText, JButton fileChose, JButton runButton, JComboBox typeDoing) {
        this.Result = Result;
        this.minSupField = minSup;
        this.minAllConfField = minAllConf;
        this.minConfField = minConf;
        this.classText = classText;
        this.inputText = inputText;
        this.fileChose = fileChose;
        this.runButton = runButton;
        this.data2PredictText = data2PredictText;
        this.typeDoing = typeDoing;

    }

    private void setActiveComponent(Boolean flag) {
        this.minSupField.setEditable(flag);
        this.minAllConfField.setEditable(flag);
        this.minConfField.setEditable(flag);
        this.classText.setEditable(flag);
        this.inputText.setEditable(flag);
        this.fileChose.setEnabled(flag);
        this.runButton.setEnabled(flag);
        this.typeDoing.setEditable(flag);
        if (this.data2PredictText != null) {
            this.data2PredictText.setEnabled(flag);
        }
    }

    @Override
    public void run() {
        try {
            setActiveComponent(false);
            String rs = "";
            if (bestResult.equals("")) {
                test = new TestACAC_batch_holdout_with_set_Attributes(classText.getSelectedItem().toString(), fileName, minSup, minConf, minAllConf, inputAttributes);
                test.run();
                Result.setText(test.getBestResult());

            }

            if (type.equals("prediction")) {// testing

                Result.setText(test.prediction(data2Predict));
            }


            setActiveComponent(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
