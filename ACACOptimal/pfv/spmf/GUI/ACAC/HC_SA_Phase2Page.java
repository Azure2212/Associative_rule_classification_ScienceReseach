package ca.pfv.spmf.GUI.ACAC;

import ca.pfv.spmf.algorithms.classifiers.acac.ACACEntropyAPP.ACACThread.ACACPhase_Thread;
import ca.pfv.spmf.algorithms.classifiers.acac.EntopyHandle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
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

import ca.pfv.spmf.algorithms.classifiers.acac.ACACEntropyAPP.TestACAC_batch_holdout_HC;
import ca.pfv.spmf.algorithms.classifiers.acac.ACACEntropyAPP.TestACAC_batch_holdout_SA;

public class HC_SA_Phase2Page extends JFrame implements ActionListener {
    String title = "Hill Climbing For ACAC";
    JTextArea fileData;

    JTextArea Result;
    JButton chooseFileButton, runButton, backButton;
    JLabel titleTextLabel;

    JLabel NameFile;

    JLabel minsupLabel, allConfLabel, confLabel, classLabel, typeDoingLabel;
    JTextField minsupText, allConfText, confText;

    JComboBox classText, typeCombobox;

    JTextField attributesInput;
    JPanel alltributesPanel;
    String[] allAttributes, bestAttributes;
    List<String[]> allData;

    String fileName;


    public HC_SA_Phase2Page() {
        // Tạo một JFrame (cửa sổ)
        this.setTitle(title);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int widthCreen = (int) (screenSize.getWidth());
        int heightCreen = (int) (screenSize.getHeight());
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
        minsupLabel.setBounds(55, 150, 800, 50);

        minsupText = new JTextField();
        minsupText.setBounds(140, 150, 150, 50);
        minsupText.setFont(new Font("Serif", Font.BOLD, 20));
        minsupText.setText("0.1");

        allConfLabel = new JLabel("minAllConf:");
        allConfLabel.setFont(new Font("Serif", Font.BOLD, 20));
        allConfLabel.setBounds(310, 150, 800, 50);

        allConfText = new JTextField();
        allConfText.setBounds(425, 150, 150, 50);
        allConfText.setFont(new Font("Serif", Font.BOLD, 20));
        allConfText.setText("0.5");

        confLabel = new JLabel("minConf:");
        confLabel.setFont(new Font("Serif", Font.BOLD, 20));
        confLabel.setBounds(55, 250, 800, 50);

        confText = new JTextField();
        confText.setBounds(140, 250, 150, 50);
        confText.setFont(new Font("Serif", Font.BOLD, 20));
        confText.setText("0.8");

        classLabel = new JLabel("Class:");
        classLabel.setFont(new Font("Serif", Font.BOLD, 20));
        classLabel.setBounds(310, 230, 800, 50);

        classText = new JComboBox();
        classText.setBounds(425, 240, 150, 30);
        classText.setFont(new Font("Serif", Font.BOLD, 20));

        typeCombobox = new JComboBox();
        typeCombobox.setBounds(425, 280, 150, 30);
        typeCombobox.setFont(new Font("Serif", Font.BOLD, 20));
        String[] type = new String[]{"Hill Climbing", "Simulated Annealing"};
        typeCombobox.setModel(new DefaultComboBoxModel<>(type));

        typeDoingLabel = new JLabel("Type Doing:");
        typeDoingLabel.setFont(new Font("Serif", Font.BOLD, 20));
        typeDoingLabel.setBounds(310, 270, 800, 50);


        alltributesPanel = new JPanel();
        alltributesPanel.setLayout(new BorderLayout());
        alltributesPanel.setBounds(50, 320, 535, 60);
        alltributesPanel.setBorder(new LineBorder(Color.BLACK, 2));
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                new EmptyBorder(3, 2, 2, 2),
                "Attributes you want:",
                TitledBorder.LEFT,
                TitledBorder.DEFAULT_POSITION
        );
        alltributesPanel.setBorder(titledBorder);


        attributesInput = new JTextField();
        attributesInput.setBounds(140, 250, 150, 50);
        attributesInput.setFont(new Font("Serif", Font.BOLD, 20));
        attributesInput.setText("");
        alltributesPanel.add(attributesInput);


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
        this.add(classText);
        this.add(runButton);
        this.add(backButton);
        this.add(typeCombobox);
        this.add(typeDoingLabel);

        this.add(alltributesPanel);

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

                    classText.setModel(model);
                    runButton.setEnabled(true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }

        if (e.getSource() == runButton) {
            if (attributesInput.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please input attributes you want do hillClimbing.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            EntopyHandle entropyHandle = new EntopyHandle(this.allAttributes, this.allData, classText.getSelectedItem().toString());
            entropyHandle.doEntropy();
            try {
                double minSup = Double.parseDouble(minsupText.getText());
                double minConf = Double.parseDouble(confText.getText());
                double minAllConf = Double.parseDouble(allConfText.getText());
                String[] inputAttributesData = (attributesInput.getText().trim()).split(", ");
                for (String i : inputAttributesData) {
                    if (i.equals(classText.getSelectedItem())) {
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

                List<String> sub = new ArrayList<>();// to contain subAttributes;
                for (String i : allAttributes) {
                    int flag = 0;
                    for (String j : inputAttributesData) {
                        if (j.equals(i)) {
                            flag = 1;
                        }
                    }
                    if (flag == 0 && !i.equals(classText.getSelectedItem())) {
                        sub.add(i);
                    }
                }
                String[] subAttributes = sub.toArray(new String[0]);

                Result.setEditable(false);
                List<String> temp = new ArrayList<>();
                for (String i : inputAttributesData) {
                    temp.add(i);
                }
                temp.add(classText.getSelectedItem().toString());
                inputAttributesData = temp.toArray(new String[0]);
                bestAttributes = inputAttributesData;
                String rs = "";
                ACACPhase_Thread thread = new ACACPhase_Thread(entropyHandle, fileName, minSup, minConf, minAllConf, bestAttributes, subAttributes);
                thread.declareComponent(Result,minsupText,confText,allConfText,classText,attributesInput,chooseFileButton,runButton);

                if (typeCombobox.getSelectedItem().toString().equals("Hill Climbing")) {
                    thread.typeRun="phase2.1";
                    thread.start();
                } else {
                    Result.setText("Simulated Annealing is running ! wait until done");
                    thread.typeRun="phase2.2";
                    thread.start();
                }


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
