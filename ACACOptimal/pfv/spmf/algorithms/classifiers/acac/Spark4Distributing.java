package ca.pfv.spmf.algorithms.classifiers.acac;

import ca.pfv.spmf.algorithms.classifiers.data.Dataset;
import ca.pfv.spmf.algorithms.classifiers.data.Instance;
import ca.pfv.spmf.algorithms.classifiers.data.StringDataset;
import ca.pfv.spmf.algorithms.classifiers.general.ClassificationAlgorithm;
import ca.pfv.spmf.test.MainTestACAC_batch_holdout_HC;
import ca.pfv.spmf.test.MainTestACAC_batch_holdout_Original;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static ca.pfv.spmf.test.MainTestACAC_batch_holdout_HC.fileToPath;

public class Spark4Distributing {
    public String[][] fragmentation;
    public StringDataset datasetPerFragment[];
    public String[] allAtributes;
    public String label;
    public String nameFile;

    public int indexOfLabelInDatabase;

    public Spark4Distributing(String nameFile, int quantityFragment, String label) {
        try {
            this.label = label;
            this.nameFile = nameFile;
            this.datasetPerFragment = new StringDataset[quantityFragment];
            makeListOfAllAttributes();
            fragmentAttributesGenerate(quantityFragment);
        } catch (Exception e) {
            System.out.println("error:" + e);
        }

    }

    public void fragmentAttributesGenerate(int quantityFragment) {
        fragmentation = new String[quantityFragment][];
        String[] newAllAttribute = new String[allAtributes.length - 1];
        int index = 0;
        for (int i = 0; i < allAtributes.length; i++) {
            if (allAtributes[i].equals(label)) continue;
            newAllAttribute[index++] = allAtributes[i];
        }
        allAtributes = newAllAttribute;
        int groupSize = allAtributes.length / quantityFragment; // Determine the size of each group

        // Create an array of arrays to hold the groups
        String[][] groups = new String[quantityFragment][];
        // Populate the groups
        for (int i = 0; i < quantityFragment; i++) {
            int start = i * groupSize;
            int end = (i == quantityFragment - 1) ? allAtributes.length : (i + 1) * groupSize;
            int groupLength = end - start;
            groups[i] = new String[groupLength];
            System.arraycopy(allAtributes, start, groups[i], 0, groupLength);
            fragmentation[i] = groups[i];
        }
    }

    private void makeListOfAllAttributes() throws Exception {

        String datasetPath = fileToPath(nameFile);
        StringDataset dataset = new StringDataset(this, datasetPath, label);

    }

    public int gt(int n) {
        int tich = 1;
        for (int i = 1; i <= n; i++) {
            tich *= i;
        }
        return tich;
    }

    public int combination(int n, int k) {
        return gt(n) / (gt(k) * gt(n - k));
    }

    public List<RuleACAC> run(StringDataset dataset, double minSup, double minConf, double minAllConf) throws Exception {
        long minSupRelative = (long) Math.ceil(minSup * dataset.getInstances().size());
        List<RuleACAC> allRules = new ArrayList<>();
        int amount = this.fragmentation.length;
        List<List<RuleACAC>> frequents = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            frequents.add(new ArrayList<>());
        }

        //List<RuleACAC>[] frequents = new List[amount]; // tất cả luật itemset trên từng mảnh

        // đút kết các luật 1-itemset
        generate1ItemsetRules(amount, minSup, minConf, minAllConf, frequents, allRules);


        System.out.println("-----------------------------------------" + allRules.size());
        /*for (int i = 0; i < frequents.length; i++) {
            //System.out.println("luật tập " + (i + 1) + ":");
            for (RuleACAC r : frequents[i]) {
                //System.out.println(r);
            }
            //System.out.println("--------------");
        }*/
        int combination = combination(amount, 2);
        List<List<RuleACAC>> allFrequents = new ArrayList<>();
        for (int i = 0; i < combination; i++) {
            allFrequents.add(new ArrayList<>());
        }


        // join các luật trên từng mảnh với nhau tạo ra tập join 2 itemset. tập này là nền tảng cho các tập ksau
        generate2ItemsetRules(combination, dataset, minConf, minAllConf, minSupRelative, frequents, allRules, allFrequents);
        /*test*/
        List<RuleACAC>test= new ArrayList<>();
        for(int i=0;i<combination;i++){
            List<RuleACAC>temp=new ArrayList<>(allFrequents.get(i));
            test.addAll(temp);
        }
        System.out.println("coi ne tap 2="+test.size()+" ("+test.get(0));
        test=removeAllDuplicateRule(test);
        for(int i=0;i< test.size();i++){
            System.out.println(test.get(i));
        }
        System.out.println("-------------------");
        // join các luật trên từng mảnh với nhau tạo ra tập join k itemset. tập này là nền tảng cho các tập ksau

        do {
            generatekItemsetRules(combination, dataset, minConf, minAllConf, minSupRelative, allFrequents, allRules);
            System.out.println("Xong r:" + allFrequents.get(0).size() + " " + allFrequents.get(1).size() + " " + allFrequents.get(2).size());
        } while (!checkEndLoop(allFrequents));


        System.out.println("coi trc:" + allRules.size());
        allRules = removeAllDuplicateRule(allRules);
        System.out.println("tất cả luật thu dc từ 3 mảnh: ");
        for (int i = 0; i < allRules.size(); i++) {
            System.out.println(allRules.get(i));
        }
        System.out.println(allRules.size());
        System.out.println("--------------  end ----------------");
        //} while (!checkEndLoop(allFrequents));
        return null;
    }

    private boolean checkEndLoop(List<List<RuleACAC>> allFrequents) {
        int i = 0;
        for (List<RuleACAC> r : allFrequents) {
            if (r.isEmpty()) i++;
        }
        return i == allFrequents.size();


    }

    // build thread to do parabel
    public void generate1ItemsetRules(int amount, double minSup, double minConf, double minAllConf,
                                      List<List<RuleACAC>> frequents, List<RuleACAC> allRules) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(amount);
        Future<?>[] futures = new Future[amount];
        for (int i = 0; i < amount; i++) {
            final int index = i;

            futures[i] = executorService.submit(() -> {
                AprioriForACAC4Spark apriori = new AprioriForACAC4Spark();
                frequents.set(index, apriori.run(datasetPerFragment[index], minSup, minConf, minAllConf, allRules));
            });
        }
        // Wait for all tasks to complete
        for (Future<?> future : futures) {
            future.get();
        }
    }

    // build thread to do parabel
    public void generate2ItemsetRules(int amount, Dataset dataset, double minConf, double minAllConf,
                                      long minSupRelative, List<List<RuleACAC>> frequents, List<RuleACAC> allRules, List<List<RuleACAC>> allFrequents) throws Exception {
        List<List<RuleACAC>> listAllRules = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            listAllRules.add(new ArrayList<>());
        }
        CountDownLatch latch = new CountDownLatch(amount);
        Thread[] threads = new Thread[amount];
        AprioriForACAC4Spark apriori = new AprioriForACAC4Spark();
        int z = 0;
        for (int i = 0; i < amount - 1; i++) {
            for (int j = i + 1; j < amount; j++) {
                final int iCopy = i; // Create local copies of i and j
                final int jCopy = j;
                final int zCoppy = z;
                threads[z] = new Thread(() -> {
                    try {
                        List<RuleACAC> temp = apriori.generateAndTestCandidateSizeK(dataset, minConf, minAllConf, minSupRelative, listAllRules.get(zCoppy), frequents.get(iCopy), frequents.get(jCopy));
                        allFrequents.set(zCoppy, temp);
                        latch.countDown();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                threads[z++].start();
            }
        }
        latch.await();
        System.out.println("Xong r");

        for (int i = 0; i < amount; i++) {
            allRules.addAll(listAllRules.get(i));
        }
    }

    // build thread to do parabel
    public void generatekItemsetRules(int amount, Dataset dataset, double minConf, double minAllConf,
                                      long minSupRelative, List<List<RuleACAC>> allFrequents, List<RuleACAC> allRules) throws Exception {
        List<List<RuleACAC>> allFrequentsPrevious = new ArrayList<>();
        for (int i = 0; i < allFrequents.size(); i++) {
            allFrequentsPrevious.add(new ArrayList<>(allFrequents.get(i)));
        }
        List<List<RuleACAC>> listAllRules = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            listAllRules.add(new ArrayList<>());
        }
        CountDownLatch latch = new CountDownLatch(amount);
        Thread[] threads = new Thread[amount];
        AprioriForACAC4Spark apriori = new AprioriForACAC4Spark();
        int z = 0;
        for (int i = 0; i < amount - 1; i++) {
            for (int j = i + 1; j < amount; j++) {
                final int iCopy = i; // Create local copies of i and j
                final int jCopy = j;
                final int zCoppy = z;
                threads[z] = new Thread(() -> {
                    try {
                        List<RuleACAC> temp = apriori.generateAndTestCandidateSizeK(dataset, minConf, minAllConf, minSupRelative, listAllRules.get(zCoppy), allFrequentsPrevious.get(iCopy), allFrequentsPrevious.get(jCopy));
                        allFrequents.set(zCoppy, temp);
                        latch.countDown();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                threads[z++].start();
            }
        }
        latch.await();

        for (int i = 0; i < amount; i++) {
            allRules.addAll(listAllRules.get(i));
        }

        // System.exit(0);
    }


    public List<RuleACAC> removeAllDuplicateRule(List<RuleACAC> input) {
        System.out.println("coi sau:" + input.size());
        List<RuleACAC> newList = input;
        RuleACAC t = new RuleACAC(new short[0]);
        for (int i = 0; i < newList.size() - 1; i++) {
            for (int j = i + 1; j < newList.size(); j++) {
                if (newList.get(i).equals(newList.get(j))) {
                    newList.set(j, t);
                }
            }
        }

        Iterator<RuleACAC> iterator = newList.iterator();
        while (iterator.hasNext()) {
            RuleACAC element = iterator.next();
            if (element.equals(t)) {
                iterator.remove();
            }
        }


        return newList;
    }

}

class main2 {
    public static void main(String[] agrv) throws UnsupportedEncodingException, Exception {

//        String file = "tennisExtendedCSV.txt";
//        String label = "play";
        String file = "tennisExtended.txt";
        String label = "class";
        int amount = 3;

        Spark4Distributing s = new Spark4Distributing(file, amount, label);
        for (String[] i : s.fragmentation) {
            System.out.println("fragment:");
            for (String j : i) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
        System.out.println("-------------------");


        // Load the dataset
        String datasetPath = fileToPath(file);

        StringDataset dataset = new StringDataset(datasetPath, label);


        System.out.println("--------------------------------\n generate Dataset for each fragment");
        int stepStart = 0;
        for (int index = 0; index < s.fragmentation.length; index++) {
            s.datasetPerFragment[index] = new StringDataset(datasetPath, label, stepStart, stepStart + s.fragmentation[index].length - 1);
            stepStart += s.fragmentation[index].length;
            /*System.out.println("--------------------------------------");
            for (int i = 0; i < s.datasetPerFragment[index].getInstances().size(); i++) {
                for (Short j : s.datasetPerFragment[index].getInstances().get(i).getItems()) {
                    System.out.print(j + " ");
                }
                System.out.println();
            }
            System.out.println("------------------");*/
        }

        System.out.println("==== Step 2: Training:  Apply the algorithm to build a model (a set of rules) ===");
        // Parameters of the algorithm
        double minSup = 0.1;
        double minConf = 0.8;
        double minAllConf = 0.5;

        // Create the algorithm
        s.run(dataset, minSup, minConf, minAllConf);


    }


}