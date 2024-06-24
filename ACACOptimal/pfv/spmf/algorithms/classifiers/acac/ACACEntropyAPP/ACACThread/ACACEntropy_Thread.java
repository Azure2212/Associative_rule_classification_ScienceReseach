package ca.pfv.spmf.algorithms.classifiers.acac.ACACEntropyAPP.ACACThread;

import ca.pfv.spmf.algorithms.classifiers.acac.ACACEntropyAPP.TestACAC_batch_holdout_FSAC;
import ca.pfv.spmf.algorithms.classifiers.acac.EntopyHandle;

import javax.swing.*;

public class ACACEntropy_Thread extends Thread {


    JTextArea Result;

    JComboBox classText;
    JTextField minSupField;
    JTextField minConfField;
    JTextField minAllConfField;

    JButton fileChoseButton, runButton;

    EntopyHandle entropyHandle;
    String fileName;
    double minSup;
    double minConf;
    double minAllConf;

    public ACACEntropy_Thread(EntopyHandle entropyHandle, String fileName, double minSup, double minConf, double minAllConf) {
        this.entropyHandle = entropyHandle;
        this.fileName = fileName;
        this.minSup = minSup;
        this.minConf = minConf;
        this.minAllConf = minAllConf;
    }

    public void declareComponent(JTextArea Result, JTextField minSup, JTextField minConf, JTextField minAllConf, JComboBox classText, JButton fileChoseButton, JButton runButton) {
        this.Result = Result;
        this.minSupField = minSup;
        this.minAllConfField = minAllConf;
        this.minConfField = minConf;
        this.classText = classText;
        this.fileChoseButton = fileChoseButton;
        this.runButton = runButton;

    }

    private void setActiveComponent(Boolean flag) {
        this.minSupField.setEditable(flag);
        this.minAllConfField.setEditable(flag);
        this.minConfField.setEditable(flag);
        this.classText.setEditable(flag);
        this.runButton.setEnabled(flag);
        this.fileChoseButton.setEnabled(flag);
    }

    @Override
    public void run() {


        TestACAC_batch_holdout_FSAC testFSAC = new TestACAC_batch_holdout_FSAC(entropyHandle, fileName, minSup, minConf, minAllConf);
        try {
            setActiveComponent(false);
            String rs = testFSAC.run();

            Result.setText(rs);
            setActiveComponent(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
