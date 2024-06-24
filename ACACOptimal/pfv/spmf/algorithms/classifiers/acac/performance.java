package ca.pfv.spmf.algorithms.classifiers.acac;

public class performance {
    public double accuracy;
    public double times;
    public int rules;
    public performance(double accuracy,double times,int rules){
        this.accuracy=accuracy;
        this.times=times;
        this.rules=rules;
    }
    public String toString(){
        return("accuracy:"+accuracy+"  times="+times+" rules="+rules);
    }
}
