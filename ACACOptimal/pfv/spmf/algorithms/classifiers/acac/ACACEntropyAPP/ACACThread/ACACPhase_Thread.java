package ca.pfv.spmf.algorithms.classifiers.acac.ACACEntropyAPP.ACACThread;

import ca.pfv.spmf.algorithms.classifiers.acac.ACACEntropyAPP.TestACAC_batch_holdout_FSAC;
import ca.pfv.spmf.algorithms.classifiers.acac.ACACEntropyAPP.TestACAC_batch_holdout_HC;
import ca.pfv.spmf.algorithms.classifiers.acac.ACACEntropyAPP.TestACAC_batch_holdout_SA;
import ca.pfv.spmf.algorithms.classifiers.acac.ACACEntropyAPP.TestACAC_batch_holdout_mergeAttribute_phase3;
import ca.pfv.spmf.algorithms.classifiers.acac.EntopyHandle;

import javax.swing.*;

public class ACACPhase_Thread extends Thread {

    public String typeRun = "";// phase2.1 : HC, phase 2.2: SA, Phase 3: merge
    JTextArea Result;

    JComboBox classText;
    JTextField minSupField;
    JTextField minConfField;
    JTextField minAllConfField;
    JButton fileChose, runButton;
    JTextField inputText;

    EntopyHandle entropyHandle;
    String fileName;
    double minSup;
    double minConf;
    double minAllConf;

    String[] bestAttributes;

    String[] subAttributes;

    public ACACPhase_Thread(EntopyHandle entropyHandle, String fileName, double minSup, double minConf, double minAllConf, String[] bestAttributes, String[] subAttributes) {
        this.entropyHandle = entropyHandle;
        this.fileName = fileName;
        this.minSup = minSup;
        this.minConf = minConf;
        this.minAllConf = minAllConf;
        this.bestAttributes = bestAttributes;
        this.subAttributes = subAttributes;
    }

    public void declareComponent(JTextArea Result, JTextField minSup, JTextField minConf, JTextField minAllConf, JComboBox classText, JTextField inputText, JButton fileChose, JButton runButton) {
        this.Result = Result;
        this.minSupField = minSup;
        this.minAllConfField = minAllConf;
        this.minConfField = minConf;
        this.classText = classText;
        this.inputText = inputText;
        this.fileChose = fileChose;
        this.runButton = runButton;

    }

    private void setActiveComponent(Boolean flag) {
        this.minSupField.setEditable(flag);
        this.minAllConfField.setEditable(flag);
        this.minConfField.setEditable(flag);
        this.classText.setEditable(flag);
        this.inputText.setEditable(flag);
        this.fileChose.setEnabled(flag);
        this.runButton.setEnabled(flag);
    }

    @Override
    public void run() {
        try {
            setActiveComponent(false);
            String rs = "";
            if (typeRun.equals("phase2.1")) {
                Result.setText("Hill Climbing is running ! wait until done");
                rs = new TestACAC_batch_holdout_HC(classText.getSelectedItem().toString(), fileName, minSup, minConf, minAllConf, bestAttributes, subAttributes).run();

            } else if (typeRun.equals("phase2.2")) {
                Result.setText("Simulated Annealing is running ! wait until done");
                rs = new TestACAC_batch_holdout_SA(classText.getSelectedItem().toString(), fileName, minSup, minConf, minAllConf, bestAttributes, subAttributes).run();
            } else if (typeRun.equals("phase3")) {

                rs = new TestACAC_batch_holdout_mergeAttribute_phase3(classText.getSelectedItem().toString(), fileName, minSup, minConf, minAllConf, bestAttributes, subAttributes).run();
            }

            Result.setText(rs);
            setActiveComponent(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
