package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.classifiers.data.StringDataset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static ca.pfv.spmf.test.MainTestACAC_batch_holdout_HC.fileToPath;

public class testEntropy {

    public static void main(String[] args) {
        Random random = new Random();
       for(int i=0;i<500;i++){
           double randomValue = random.nextDouble();
           if(randomValue>=1.0)
            System.out.println(randomValue);
       }

    }



}
