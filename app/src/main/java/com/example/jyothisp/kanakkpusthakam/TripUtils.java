package com.example.jyothisp.kanakkpusthakam;

import android.util.Log;

public final class TripUtils {

    private TripUtils(){}

    public static int[] intArrayFromString(String s, int strength){
        String splits[] = s.split(",");
        double[] doubles = new double[strength];
        int[] ints = new int[strength];
        for (int i =0; i<doubles.length; i++){
            ints[i] = (int) Double.parseDouble(splits[i]);
            Log.v("Expense Fragment", " " + ints[i]);
        }
        return ints;
    }

    public static int[] invertArray(int[] ints){
        for (int i=0; i<ints.length; i++){
            ints[i] = - ints[i];
            Log.v("Expense Fragment", "invertArray: Inverted Array: " + ints[i]);
        }
        return ints;
    }


    public static String intArrayToString(double[] intArray) {
        String string = "";
        int i;
        for (i = 0; i < (intArray.length - 1); i++) {
            string += intArray[i];
            string += ",";
        }
        string += intArray[i];
        Log.v("Involvement fragment1", string);

        return string;
    }

    public static String intArrayToString(int[] intArray) {
        String string = "";
        int i;
        for (i = 0; i < (intArray.length - 1); i++) {
            string += intArray[i];
            string += ",";
        }
        string += intArray[i];
        Log.v("Involvement fragment", string);
        return string;
    }


}
