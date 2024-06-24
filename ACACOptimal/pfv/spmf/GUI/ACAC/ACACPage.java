package ca.pfv.spmf.GUI.ACAC;

import ca.pfv.spmf.algorithms.classifiers.acac.ACACEntropyAPP.ACACThread.ACACPhase_Thread;
import ca.pfv.spmf.algorithms.classifiers.acac.ACACEntropyAPP.ACACThread.ACAC_Train_Predict_Thread;
import ca.pfv.spmf.algorithms.classifiers.acac.ACACEntropyAPP.TestACAC_batch_holdout_with_set_Attributes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ACACPage extends JFrame implements ActionListener, ItemListener {
    String title = "ACAC Algorithm with an entropy threshHold";
    JTextArea fileData;

    JTextArea Result;
    JButton chooseFileButton, runButton, backButton;
    JLabel titleTextLabel;

    JLabel NameFile;

    JLabel minsupLabel, allConfLabel, confLabel, classLabel, typeDoingLabel;
    JTextField minsupText, allConfText, confText;


    JComboBox classCombox, typeCombobox;

    String[] allAttributes;
    List<String[]> allData;

    String fileName;

    //
    JPanel trainingAttributesPanel, predictionValuePanel;
    JTextField attributes2Training, value2Prediction;


    String bestResult = "";

    public ACACPage() {
        // Tạo một JFrame (cửa sổ)
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int widthCreen = (int) (screenSize.getWidth());
        int heightCreen = (int) (screenSize.getHeight());
        this.setTitle(title);

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

        minsupLabel = new JLabel("MinSup:");
        minsupLabel.setFont(new Font("Serif", Font.BOLD, 20));
        minsupLabel.setBounds(55, 150, 800, 30);

        minsupText = new JTextField();
        minsupText.setBounds(140, 150, 150, 30);
        minsupText.setFont(new Font("Serif", Font.BOLD, 20));
        minsupText.setText("0.1");

        allConfLabel = new JLabel("minAllConf:");
        allConfLabel.setFont(new Font("Serif", Font.BOLD, 20));
        allConfLabel.setBounds(310, 150, 800, 30);

        allConfText = new JTextField();
        allConfText.setBounds(425, 150, 150, 30);
        allConfText.setFont(new Font("Serif", Font.BOLD, 20));
        allConfText.setText("0.5");

        confLabel = new JLabel("minConf:");
        confLabel.setFont(new Font("Serif", Font.BOLD, 20));
        confLabel.setBounds(55, 215, 800, 30);

        confText = new JTextField();
        confText.setBounds(140, 215, 150, 30);
        confText.setFont(new Font("Serif", Font.BOLD, 20));
        confText.setText("0.8");

        classLabel = new JLabel("Class:");
        classLabel.setFont(new Font("Serif", Font.BOLD, 20));
        classLabel.setBounds(310, 200, 800, 30);

        classCombox = new JComboBox();
        classCombox.setBounds(425, 200, 150, 30);
        classCombox.setFont(new Font("Serif", Font.BOLD, 20));

        typeDoingLabel = new JLabel("Type Doing:");
        typeDoingLabel.setFont(new Font("Serif", Font.BOLD, 20));
        typeDoingLabel.setBounds(310, 220, 800, 50);

        typeCombobox = new JComboBox();
        typeCombobox.setBounds(425, 235, 150, 30);
        typeCombobox.setFont(new Font("Serif", Font.BOLD, 20));
        String[] type = new String[]{"Training", "Prediction"};
        typeCombobox.setModel(new DefaultComboBoxModel<>(type));
        typeCombobox.addItemListener(this);

        //input attribute to training
        trainingAttributesPanel = new JPanel();
        trainingAttributesPanel.setLayout(new BorderLayout());
        trainingAttributesPanel.setBounds(50, 300, 535, 60);
        trainingAttributesPanel.setBorder(new LineBorder(Color.BLACK, 2));
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                new EmptyBorder(3, 2, 2, 2),
                "Attributes you want:",
                TitledBorder.LEFT,
                TitledBorder.DEFAULT_POSITION
        );
        trainingAttributesPanel.setBorder(titledBorder);


        attributes2Training = new JTextField();
        attributes2Training.setBounds(140, 250, 150, 50);
        attributes2Training.setFont(new Font("Serif", Font.BOLD, 20));
        attributes2Training.setText("");
        trainingAttributesPanel.add(attributes2Training);

        // input value to prediction;

        predictionValuePanel = new JPanel();
        predictionValuePanel.setLayout(new BorderLayout());
        predictionValuePanel.setBounds(50, 330, 535, 60);
        predictionValuePanel.setBorder(new LineBorder(Color.BLACK, 2));
        TitledBorder titledBorder2 = BorderFactory.createTitledBorder(
                new EmptyBorder(3, 2, 2, 2),
                "Value you want to predict:",
                TitledBorder.LEFT,
                TitledBorder.DEFAULT_POSITION
        );
        predictionValuePanel.setBorder(titledBorder2);


        value2Prediction = new JTextField();
        value2Prediction.setBounds(140, 250, 150, 50);
        value2Prediction.setFont(new Font("Serif", Font.BOLD, 20));
        value2Prediction.setText("");
        predictionValuePanel.add(value2Prediction);
        predictionValuePanel.setVisible(false);


        // Đặt kích thước và vị trí cho nút và textbox
        chooseFileButton.setBounds(700, 150, 140, 30);
        runButton.setBounds(955, 150, 140, 30);
        JScrollPane scrollPane4FileData = new JScrollPane(fileData);
        JScrollPane scrollPane4Result = new JScrollPane(Result);
        scrollPane4FileData.setBounds(700, 200, 400, (int) (heightCreen * 0.65));
        scrollPane4Result.setBounds(55, 400, 525, (int) (heightCreen * 0.42));

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
        this.add(minsupLabel);
        this.add(minsupText);
        this.add(allConfText);
        this.add(allConfLabel);
        this.add(confLabel);
        this.add(confText);
        this.add(classLabel);
        this.add(classCombox);
        this.add(runButton);
        this.add(backButton);
        this.add(typeDoingLabel);
        this.add(typeCombobox);
        this.add(trainingAttributesPanel);
        this.add(predictionValuePanel);

        // Đặt kích thước cửa sổ
        this.setSize((int) (widthCreen * 0.8), (int) (heightCreen * 0.95));

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

                    classCombox.setModel(model);
                    runButton.setEnabled(true);
                    bestResult = "";
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }

        if (e.getSource() == runButton) {
            if (attributes2Training.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please input attributes you want do training.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                double minSup = Double.parseDouble(minsupText.getText());
                double minConf = Double.parseDouble(confText.getText());
                double minAllConf = Double.parseDouble(allConfText.getText());
                String[] inputAttributesData = (attributes2Training.getText().trim()).split(", ");
                for (String i : inputAttributesData) {
                    if (i.equals(classCombox.getSelectedItem())) {
                        JOptionPane.showMessageDialog(null, "Invalid input. attributes you input can't be the same with class.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                for (String i : inputAttributesData) {
                    int flag = 0;
                    for (String j : allAttributes) {
                        if (j.equals(i)) {
                            flag = 1;
                        }

                    }
                    if (flag == 0) {
                        JOptionPane.showMessageDialog(null, "attributes you input: " + i + " not in dataset", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }


                Result.setEditable(false);
                List<String> temp = new ArrayList<>();
                for (String i : inputAttributesData) {
                    temp.add(i);
                }
                temp.add(classCombox.getSelectedItem().toString());
                inputAttributesData = temp.toArray(new String[0]);
                if (typeCombobox.getSelectedItem().equals("Training")) {
                    if (bestResult.equals("")) {
                        Result.setText("Is running ! wait until done");
                        ACAC_Train_Predict_Thread thread = new ACAC_Train_Predict_Thread(fileName, minSup, minConf, minAllConf, inputAttributesData, null, bestResult);
                        thread.declareComponent(Result, minsupText, confText, allConfText, classCombox, attributes2Training,value2Prediction, chooseFileButton, runButton,typeCombobox);
                        thread.type = "training";
                        thread.start();

                    }

                } else { // if testing
                    if (value2Prediction.getText().trim().equals("")) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please input attributes you want do predict.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String[] inputAttributesData2Predict = (value2Prediction.getText().trim()).split(", ");
                    if (inputAttributesData2Predict.length != inputAttributesData.length - 1) {
                        JOptionPane.showMessageDialog(null, "amount of attributes training must be equaled attributes prediction", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Result.setText("Is running ! wait until done");
                    ACAC_Train_Predict_Thread thread = new ACAC_Train_Predict_Thread(fileName, minSup, minConf, minAllConf, inputAttributesData, inputAttributesData2Predict, bestResult);
                    thread.declareComponent(Result, minsupText, confText, allConfText, classCombox, attributes2Training,value2Prediction, chooseFileButton, runButton,typeCombobox);
                    thread.type = "prediction";
                    thread.start();




                }


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
                }
                allData.add(line.split(" "));
                data += line + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == typeCombobox) {
            if (typeCombobox.getSelectedItem().equals("Prediction")) {
                predictionValuePanel.setVisible(true);
                trainingAttributesPanel.setLocation(trainingAttributesPanel.getX(), trainingAttributesPanel.getY() - 20);
            } else {
                predictionValuePanel.setVisible(false);
                trainingAttributesPanel.setLocation(trainingAttributesPanel.getX(), trainingAttributesPanel.getY() + 20);
            }
        }
    }
}
