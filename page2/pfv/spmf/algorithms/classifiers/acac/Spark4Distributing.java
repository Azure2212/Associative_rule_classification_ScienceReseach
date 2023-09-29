package ca.pfv.spmf.algorithms.classifiers.acac;

import ca.pfv.spmf.algorithms.classifiers.data.StringDataset;

import java.util.*;

import static ca.pfv.spmf.test.MainTestACAC_batch_holdout.fileToPath;

public class Spark4Distributing {
    public String[][] fragmentation;
    public String[] allAtributes;
    public String label;

    public Spark4Distributing(String nameFile, int quantityFragment, String label, boolean isDivideRandom) {
        try {
            this.label = label;
            makeListOfAllAttributes(nameFile);
            if (!isDivideRandom)
                fragmentAttributesGenerate(quantityFragment);
            else
                fragmentAttributesGenerateRandom(quantityFragment);
        } catch (Exception e) {
            System.out.println("error:" + e);
        }

    }

    public void fragmentAttributesGenerate(int quantityFragment) {
        fragmentation = new String[quantityFragment][];
        int groupSize = allAtributes.length / quantityFragment; // Determine the size of each group

        // Create an array of arrays to hold the groups
        String[][] groups = new String[quantityFragment][];
        List<String> list;
        // Populate the groups
        for (int i = 0; i < quantityFragment; i++) {
            int start = i * groupSize;
            int end = (i == quantityFragment - 1) ? allAtributes.length : (i + 1) * groupSize;
            int groupLength = end - start;
            groups[i] = new String[groupLength];
            System.arraycopy(allAtributes, start, groups[i], 0, groupLength);
            list = new ArrayList<>(Arrays.asList(groups[i]));
            if (!list.contains(this.label)) {
                // System.out.println(this.label);
                list.add(this.label);
            }
            groups[i] = list.toArray(new String[0]);
            fragmentation[i] = groups[i];
        }
    }

    public void fragmentAttributesGenerateRandom(int quantityFragment) {
        fragmentation = new String[quantityFragment][];
        int groupSize = allAtributes.length / quantityFragment; // Determine the size of each group

        // Create an array of arrays to hold the groups
        String[][] groups = new String[quantityFragment][];
        Vector<String>[] list = new Vector[quantityFragment];
        for (int i = 0; i < list.length; i++) {
            list[i] = new Vector<>();
        }
        // Populate the groups
        Random rd = new Random();
        int group = 0;
        for (String i : allAtributes) {
            do {
                group = rd.nextInt(quantityFragment);
            } while (list[group].size() > groupSize);
            list[group].add(i);
        }
        for (int i = 0; i < quantityFragment; i++) {
            if (!list[i].contains(this.label)) list[i].add(this.label);
            fragmentation[i] = list[i].toArray(new String[0]);
        }

        /*for (int i = 0; i < quantityFragment; i++) {
            int start = i * groupSize;
            int end = (i == quantityFragment - 1) ? allAtributes.length : (i + 1) * groupSize;
            int groupLength = end - start;
            groups[i] = new String[groupLength];
            System.arraycopy(allAtributes, start, groups[i], 0, groupLength);
            list = new ArrayList<>(Arrays.asList(groups[i]));
            if (!list.contains(this.label)) {
                // System.out.println(this.label);
                list.add(this.label);
            }
            groups[i] = list.toArray(new String[0]);
            fragmentation[i] = groups[i];
        }*/


    }

    private void makeListOfAllAttributes(String nameFile) throws Exception {

        String datasetPath = fileToPath(nameFile);
        StringDataset dataset = new StringDataset(this, datasetPath, label);

    }


}

class main2 {
    public static void main(String[] agrv) {
        String file = "spam.csv";
        int amount = 3;
        String label = "spam";
        Spark4Distributing s = new Spark4Distributing(file, amount, label, true);
        for (String[] t : s.fragmentation) {
            System.out.println(t.length);
        }
        for (int i = 0; i < s.fragmentation.length; i++) {
            System.out.print("Fragment" + (i + 1) + ": ");
            for (int j = 0; j < s.fragmentation[i].length; j++) {
                System.out.print(" " + s.fragmentation[i][j]);
            }
            System.out.println();
        }

    }

}