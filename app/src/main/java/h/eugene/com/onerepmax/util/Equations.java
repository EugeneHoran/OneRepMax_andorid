package h.eugene.com.onerepmax.util;


public class Equations {
    public static Double BrzyckiEquation(double weight, int reps) {
        double y = 36d / (37d - reps);
        return weight * y;
    }

    //1, 2, 3, 4, 5 etc
    public static double[] percentages = {1, .95, .90, .88, .86, .83, .80, .78, .76, .75, .72, .70};

    public static double[] getPercentages(double oneRepMax) {
        double[] list = new double[percentages.length];
        for (int i = 0; i < percentages.length; i++) {
            list[i] = oneRepMax * percentages[i];
        }
        return list;
    }
}
