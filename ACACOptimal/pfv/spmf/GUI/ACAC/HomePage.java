package ca.pfv.spmf.GUI.ACAC;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame implements ActionListener {
    JButton FSACButton;
    JButton HillClimbingACAC;
    JButton mergeAttributes;
    JButton ACAC;

    JButton EntropySelection;

    public HomePage() {

        this.setTitle("ACAC HomePage");


        FSACButton = new JButton("<html><div style='text-align: center;'>FSAC Algorithm<br>(Phase1)</div></html>");
        HillClimbingACAC = new JButton("<html><div style='text-align: center;'>Using HC & SA in ACAC<br>(Phase2)</div></html>");
        mergeAttributes = new JButton("<html><div style='text-align: center;'>MergeAttributes<br>(Phase3)</div></html>");
        ACAC = new JButton("ACAC");
        EntropySelection = new JButton("<html><div style='text-align: center;'>Attributes Selection<br>Using Entropy</div></html>");


        ACAC.setBounds(110, 120, 200, 100);
        FSACButton.setBounds(560, 120, 200, 100);
        HillClimbingACAC.setBounds(110, 360, 200, 100);
        mergeAttributes.setBounds(560, 360, 200, 100);
        EntropySelection.setBounds(335,240,200,100);


        FSACButton.addActionListener(this);
        ACAC.addActionListener(this);
        HillClimbingACAC.addActionListener(this);
        mergeAttributes.addActionListener(this);
        EntropySelection.addActionListener(this);


        this.add(ACAC);
        this.add(FSACButton);
        this.add(HillClimbingACAC);
        this.add(mergeAttributes);
        this.add(EntropySelection);


        // Đặt kích thước cửa sổ
        this.setSize(900, 600);

        // Đặt layout cho Jthis
        this.setLayout(null);

        // Đặt Jthis hiển thị ở giữa màn hình
        this.setLocationRelativeTo(null);

        // Đặt Jthis có thể đóng khi click nút đóng cửa sổ
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        // Hiển thị Jthis.this
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == FSACButton) {
            FSAC_Phase1Page page = new FSAC_Phase1Page();
            this.dispose();
        }
        if (e.getSource() == ACAC) {
            ACACPage page = new ACACPage();
            this.dispose();
        }
        if (e.getSource() == HillClimbingACAC) {
            HC_SA_Phase2Page page = new HC_SA_Phase2Page();
            this.dispose();
        }if (e.getSource() == mergeAttributes) {
            MergeAttribute_Phase3Page page = new MergeAttribute_Phase3Page();
            this.dispose();
        }if (e.getSource() == EntropySelection) {
            FeatureSelectionByEntropyPage page = new FeatureSelectionByEntropyPage();
            this.dispose();
        }
    }
}
