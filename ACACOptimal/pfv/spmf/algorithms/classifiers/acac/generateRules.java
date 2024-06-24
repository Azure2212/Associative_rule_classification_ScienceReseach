package ca.pfv.spmf.algorithms.classifiers.acac;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public class generateRules {
    //tập dữ liệu chính - 22 cột tại entropy=0.84

    public String[] allAtributes = {"class", "cap-shape", "cap-surface", "cap-color", "bruises", "odor", "gill-attachment", "gill-spacing", "gill-size", "gill-color", "stalk-shape", "stalk-root", "stalk-surface-above-ring", "stalk-surface-below-ring", "stalk-color-above-ring", "stalk-color-below-ring", "veil-type", "veil-color", "ring-number", "ring-type", "spore-print-color", "population", "habitat"};
    public String[] tap_chinh;//dang chay
    public String[] tap_sub;// dang chay

    //public String[] tap_chinh_tot_nhat_ht={"stalk-surface-above-ring","ring-type","gill-color","spore-print-color","gill-size","odor","stalk-surface-below-ring","stalk-color-above-ring","stalk-color-below-ring","class"};
    /*public String[] tap_chinh_tot_nhat_ht={"word_freq_all", "word_freq_our", "word_freq_over", "word_freq_remove", "word_freq_internet", "word_freq_order", "word_freq_mail", "word_freq_receive", "word_freq_addresses", "word_freq_free", "word_freq_you", "word_freq_your", "word_freq_000", "word_freq_money",
            "word_freq_hpl", "word_freq_re", "word_freq_edu", "char_freq_;", "word_freq_hp", "char_freq_!", "char_freq_$", "char_freq_hash", "spam"};*/

    public String[] tap_chinh_tot_nhat_ht ;

    public String[] tap_sub_tot_nhat_ht ;

    public generateRules() {


    }

    public void DogenerateRules() {
        int delta = 2;
        // Chọn ngẫu nhiên 2 cột sẽ bị thay từ tập dữ liệu chính
        Random random = new Random();
        int[] listIndex_chinh;
        int[] listIndex_phu;
        listIndex_chinh = randomIndex(delta, tap_chinh.length - 1);
        listIndex_phu = randomIndex(delta, tap_sub.length);
        String value1 = "";

       for(int i=0;i<delta;i++) {
            value1 = tap_chinh[listIndex_chinh[i]];
            tap_chinh[listIndex_chinh[i]] = tap_sub[listIndex_phu[i]];
            tap_sub[listIndex_phu[i]] = value1;
        }


        /*System.out.println("sub 2 thg này:"+tap_sub[random_ptu1_sub]+"-----"+tap_sub[random_ptu2_sub]);
        System.out.println("bằng 2 thg này:"+tap_chinh[random_ptu1]+"-----"+tap_chinh[random_ptu2]);
        System.out.println(random_ptu1+"-----"+random_ptu2);
        System.out.println(random_ptu1_sub+"-----"+random_ptu2_sub);*/


        System.out.println();

    }

    public void add1Attributes_from_sub(int index) {
        String[] new_tap_chinh = new String[tap_chinh.length + 1];
        for (int i = 0; i < tap_chinh.length; i++) {
            new_tap_chinh[i] = tap_chinh[i];
        }

        new_tap_chinh[new_tap_chinh.length - 1] = tap_sub_tot_nhat_ht[index];
        tap_chinh = new_tap_chinh;
        System.out.println("thuoc tinh dc them vo la:" + tap_sub_tot_nhat_ht[index]);
    }

    public int[] removeHead(String[] head) {
        Vector<Integer> list = new Vector<>();// tap_chinh hien tai

        for (int i = 0; i < head.length; i++) {
            //kiem tra xem có trong tập để xét ko
            if (in(head[i]) == false) {
                list.add(i);
            }
        }
        int[] rs = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            rs[i] = list.get(i);
        }
        // vi5 trí can xóa
        return rs;
    }

    public boolean in(String str) {
        for (int i = 0; i < tap_chinh.length; i++)
            if (str.equals(tap_chinh[i])) return true;
        return false;
    }

    // xóa những thuộc tính ko trong tập đang xét
    public String[] removeDateByindex(int[] index_2_remove, String[] data) {
        String[] newLine = new String[data.length - index_2_remove.length];
        int newIndex = 0;
        for (int i = 0; i < data.length; i++) {
            if (!contain(index_2_remove, i)) {
                newLine[newIndex] = data[i];
                newIndex++;
            }
        }
        return newLine;
    }

    public String[] avoidAlias(String[] input) {
        String[] rs = new String[input.length];
        for (int i = 0; i < input.length; i++) {
            rs[i] = input[i];
        }
        return rs;
    }


    private int[] randomIndex(int n, int len) {
        int[] list = new int[n];
        Random random = new Random();
        list[0] = (random.nextInt(len));
        int index;
        int flag;
        int i = 1;
        while (i < n) {
            flag = 0;
            index = random.nextInt(len);
            for (int j = 0; j < i; j++) {
                if (list[j] == index) {
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                list[i] = index;
                i++;
            }
        }
        for (int i11 = 0; i11 < list.length; i11++) {
            System.out.print(list[i11] + " ");
        }
        System.out.println();
        return list;
    }


    // hàm kiểm tra xem thuộc tính đang xét có trong 22 thuộc tính chọn hay không
    private boolean contain(int[] arr, int num) {
        for (int n : arr) {
            if (n == num)
                return true;
        }
        return false;
    }

    //tìm phần chung của tập truyền vô và tập tất cả thuộc tính
    public String[] intersection(String[] inputArray) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < allAtributes.length; j++) {
                if (inputArray[i].equals(allAtributes[j]))
                    list.add(inputArray[i]);
            }
        }
        String[] rs = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            rs[i] = list.get(i);
        }
        return rs;
    }

    public void update_2_set(int[] index_2_remove) {
        Vector<String> tap_chinh_moi = new Vector<>();
        Vector<String> tap_sub_moi = new Vector<>();
        double flag = 0;
        for (int i = 0; i < allAtributes.length; i++) {
            flag = 0;
            for (int j = 0; j < index_2_remove.length; j++) {
                if (i == index_2_remove[j]) {
                    tap_sub_moi.add(allAtributes[i]);

                    flag = 1;
                    break;
                }
            }
            if (flag != 1) {
                tap_chinh_moi.add(allAtributes[i]);
            }
        }


        String[] list1 = new String[tap_chinh_moi.size()];
        for (int i = 0; i < tap_chinh_moi.size(); i++) {
            list1[i] = tap_chinh_moi.get(i);
        }

        String[] list2 = new String[tap_sub_moi.size()];
        for (int i = 0; i < tap_sub_moi.size(); i++) {
            list2[i] = tap_sub_moi.get(i);
        }

        this.tap_chinh = list1;
        this.tap_sub = list2;
    }

}

class main1 {
    public static void main(String[] agrv) throws InterruptedException {
        List<List<Integer>> t = new ArrayList<>();
         int[] l=new int[]{1,5,10};
        t.add(new ArrayList<>());
        t.add(new ArrayList<>());
        t.add(new ArrayList<>());

        List<List<Integer>> s = new ArrayList<>();
        System.out.println(t.size());
        for(int i=0;i<t.size();i++){
            s.add(new ArrayList<>(t.get(i)));
        }
        t.set(0,null);

        for(int i=0;i< 3 ; i++){
            System.out.println(s.get(i)+" -- "+t.get(i));
        }


    }

    public static boolean check() {
        if (2 == 1) return true;
        return false;
    }

}