package ca.pfv.spmf.GUI.ACAC;

import ca.pfv.spmf.algorithms.classifiers.acac.EntopyHandle;
import ca.pfv.spmf.algorithms.classifiers.acac.ACACEntropyAPP.TestACAC_batch_holdout_Attributes_Selection;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FeatureSelectionByEntropyPage extends JFrame implements ActionListener {
    String title = "Feature Selection Using Entropy";
    JTextArea fileData;

    JTextArea Result;
    JButton chooseFileButton, runButton, backButton;
    JLabel titleTextLabel;

    JLabel NameFile;

    JLabel entropyThresholdLabel, classLabel;
    JTextField entropyThresholdText;

    JComboBox classText;

    String[] allAttributes;
    List<String[]> allData;

    String fileName;


    public FeatureSelectionByEntropyPage() {
        // Tạo một JFrame (cửa sổ)
        this.setTitle(title);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int widthCreen = (int)(screenSize.getWidth());
        int heightCreen = (int)(screenSize.getHeight());

        // Tạo một JButton (nút)
        chooseFileButton = new JButton("Choose file!");
        runButton = new JButton("Run");
        backButton = new JButton("<=");
        runButton.setEnabled(false);
        titleTextLabel = new JLabel(this.title);
        titleTextLabel.setBounds(750, 50, 200, 100);
        titleTextLabel.setFont(new Font("Serif", Font.BOLD, 25));
        // Tạo một JTextField (textbox)
        fileData = new JTextArea();
        fileData.setEditable(false);
        Result = new JTextArea();
        Result.setBorder(new LineBorder(Color.BLACK));
        Result.setFont(new Font("Serif", Font.PLAIN, 20));
        fileData.setBorder(new LineBorder(Color.BLACK));
        fileData.setFont(new Font("Serif", Font.PLAIN, 20));
        NameFile = new JLabel();

        backButton.setBounds(55, 40, 80, 50);

        entropyThresholdLabel = new JLabel("Entropy:");
        entropyThresholdLabel.setFont(new Font("Serif", Font.BOLD, 20));
        entropyThresholdLabel.setBounds(55, 250, 800, 50);

        entropyThresholdText = new JTextField();
        entropyThresholdText.setBounds(140, 250, 150, 50);
        entropyThresholdText.setFont(new Font("Serif", Font.BOLD, 20));
        entropyThresholdText.setText("0.8");

        classLabel = new JLabel("Class:");
        classLabel.setFont(new Font("Serif", Font.BOLD, 20));
        classLabel.setBounds(310, 250, 800, 50);

        classText = new JComboBox();
        classText.setBounds(425, 250, 150, 50);
        classText.setFont(new Font("Serif", Font.BOLD, 20));


        // Đặt kích thước và vị trí cho nút và textbox
        chooseFileButton.setBounds(700, 150, 140, 30);
        runButton.setBounds(955, 150, 140, 30);
        JScrollPane scrollPane4FileData = new JScrollPane(fileData);
        JScrollPane scrollPane4Result = new JScrollPane(Result);
        scrollPane4FileData.setBounds(700, 200, 400, (int)(heightCreen*0.65));
        scrollPane4Result.setBounds(55, 350, 525, (int)(heightCreen*0.475));


        titleTextLabel.setBounds(425, 35, 800, 50);
        NameFile.setBounds(650, 110, 800, 50);

        // Thêm sự kiện click cho nút
        chooseFileButton.addActionListener(this);
        runButton.addActionListener(this);
        backButton.addActionListener(this);

        // Thêm nút và textbox vào JFrame
        this.add(titleTextLabel);
        this.add(chooseFileButton);
        this.add(scrollPane4FileData);
        this.add(scrollPane4Result);
        this.add(NameFile);

        this.add(entropyThresholdLabel);
        this.add(entropyThresholdText);
        this.add(classLabel);
        this.add(classText);
        this.add(runButton);
        this.add(backButton);

        // Đặt kích thước cửa sổ
        this.setSize((int)(widthCreen*0.8), (int)(heightCreen*0.95));

        // Đặt layout cho JFrame
        this.setLayout(null);

        // Đặt JFrame hiển thị ở giữa màn hình
        this.setLocationRelativeTo(null);

        // Đặt JFrame có thể đóng khi click nút đóng cửa sổ
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        // Hiển thị JFrame
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            HomePage home = new HomePage();
            this.dispose();
        }
        if (e.getSource() == chooseFileButton) {
            JFileChooser chose = new JFileChooser();
            chose.setCurrentDirectory(new File("."));

            int rest_2 = chose.showSaveDialog(null);
            if (rest_2 == JFileChooser.APPROVE_OPTION) {
                File file_path = new File(chose.getSelectedFile().getAbsolutePath());
                String file2Read = file_path.toString().replace("/", "\\");
                file2Read = file_path.toString().replace("\\", "\\\\");
                try {
                    fileName = file2Read;
                    String content = readFile(file2Read);
                    fileData.setText(content);
                    chooseFileButton.setText("File is selected");
                    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(allAttributes);

                    classText.setModel(model);
                    runButton.setEnabled(true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }

        if (e.getSource() == runButton) {
            EntopyHandle entropyHandle = new EntopyHandle(this.allAttributes, this.allData, classText.getSelectedItem().toString());
            entropyHandle.setNumberOfClass(allData);
            entropyHandle.doEntropy();
            try {
                double entropyThreshold = Double.parseDouble(entropyThresholdText.getText());

                Result.setText("Is running ! wait until done");
                Result.setEditable(false);


                TestACAC_batch_holdout_Attributes_Selection test = new TestACAC_batch_holdout_Attributes_Selection(entropyHandle, classText.getSelectedItem().toString(), entropyThreshold, fileName);

                String rs = test.run();
                Result.setText(rs);
            } catch (NumberFormatException e0) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid double.", "Error", JOptionPane.ERROR_MESSAGE);

            } catch (Exception e1) {
                System.out.println(e1);
            }

        }
    }

    private String readFile(String filePath) throws IOException {
        String data = "";
        allData = new ArrayList<>();
        Path path = Paths.get(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null) {
                if (index == 0) {
                    allAttributes = line.split(" ");
                    index++;
                    continue;
                }
                allData.add(line.split(" "));
                data += line + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


}

