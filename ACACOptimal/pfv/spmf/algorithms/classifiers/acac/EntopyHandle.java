package ca.pfv.spmf.algorithms.classifiers.acac;

import java.text.DecimalFormat;
import java.util.*;

public class EntopyHandle {
    String[] allAttributes;
    List<String[]> dataset;

    String className;

    int numberOfClass;

    public String[] getAllAttributes() {
        return allAttributes;
    }

    public void setAllAttributes(String[] allAttributes) {
        this.allAttributes = allAttributes;
    }

    public List<String[]> getDataset() {
        return dataset;
    }

    public void setDataset(List<String[]> dataset) {
        this.dataset = dataset;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, Double> getEntropyList() {
        return entropyList;
    }

    public void setEntropyList(Map<String, Double> entropyList) {
        this.entropyList = entropyList;
    }

    Map<String, Double> entropyList = new HashMap<>();

    public EntopyHandle(String[] allAttributes, List<String[]> dataset, String className) {
        this.allAttributes = allAttributes;
        this.dataset = dataset;
        this.className = className;
    }

    public void doEntropy() {
        int classIndex = 0;
        for (int i = 0; i < allAttributes.length; i++) {
            if (allAttributes[i].equals(className)) {
                classIndex = i;
                continue;
            }
        }
        System.out.println(classIndex);
        for (int col = 0; col < allAttributes.length; col++) {
            if (col != classIndex) {
                String attribute = allAttributes[col];
                double entropy = calculateEntropy(col, classIndex);
                DecimalFormat df = new DecimalFormat("#.###");
                double roundedEntropy = Double.parseDouble(df.format(entropy));
                entropyList.put(attribute, roundedEntropy);
            }
        }

    }

    private double calculateEntropy(int attributeIndex, int classIndex) {
        Map<String, Integer> valueCounts = new HashMap<>();
        int totalRecords = dataset.size();

        for (String[] record : dataset) {
            String value = record[attributeIndex];
            valueCounts.put(value, valueCounts.getOrDefault(value, 0) + 1);
        }

        // Calculate entropy
        double entropy = 0.0;

        for (String value : valueCounts.keySet()) {
            int valueCount = valueCounts.get(value);
            double probability = (double) valueCount / totalRecords;

            // Calculate conditional entropy based on the label column
            double conditionalEntropy = calculateConditionalEntropy(attributeIndex, value, classIndex);

            entropy += probability * conditionalEntropy;
        }

        return entropy;
    }

    private double calculateConditionalEntropy(int attributeIndex, String attributeValue, int labelIndex) {
        // Count occurrences of each label for the given attribute value
        Map<String, Integer> labelCounts = new HashMap<>();
        int totalRecords = 0;

        for (String[] record : dataset) {
            if (record[attributeIndex].equals(attributeValue)) {
                String label = record[labelIndex];
                labelCounts.put(label, labelCounts.getOrDefault(label, 0) + 1);
                totalRecords++;
            }
        }

        // Calculate conditional entropy
        double conditionalEntropy = 0.0;

        for (String label : labelCounts.keySet()) {
            double probability = (double) labelCounts.get(label) / totalRecords;
            conditionalEntropy -= probability * log2(probability);
        }

        return conditionalEntropy;
    }

    private double log2(double x) {
        return Math.log(x) / Math.log(numberOfClass);
    }

    public void removeAllAttributesGreaterThanEntropyThreshHold(Double threshHold) {
        try {
            Iterator<String> iterator = entropyList.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Double value = entropyList.get(key);
                if (value > threshHold) {
                    System.out.println("bỏ " + key);
                    iterator.remove();  // Sử dụng iterator để xoá phần tử an toàn
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Double[] getEntropyData() {
        List<Double> data = new ArrayList<>();
        for (String key : entropyList.keySet()) {
            Double value = entropyList.get(key);
            data.add(value);
        }
        Collections.sort(data, Collections.reverseOrder());
        Double[] dataArray = data.toArray(new Double[0]);

        return dataArray;
    }

    public String[] getAllAttributes(Double entropyThreshHold) {
        removeAllAttributesGreaterThanEntropyThreshHold(entropyThreshHold);


        List<String> all = new ArrayList<>();
        for (Map.Entry<String, Double> entry : entropyList.entrySet()) {
            String key = entry.getKey();
            all.add(key);
        }
        all.add(className);
        String[] dataArray = all.toArray(new String[all.size()]);
        return dataArray;
    }

    public void setNumberOfClass(List<String[]> allData) {
        int indexOfClass =0;
        for(int i=0;i<allAttributes.length;i++)
            if(allAttributes[i].equals(className))
                indexOfClass=i;
        List<String> uniqueClassValue = new ArrayList<>();
        for (String[] line : allData) {
            if (uniqueClassValue.contains(line[indexOfClass])) continue;
            uniqueClassValue.add(line[indexOfClass]);
        }
        numberOfClass = uniqueClassValue.size();
    }

}
