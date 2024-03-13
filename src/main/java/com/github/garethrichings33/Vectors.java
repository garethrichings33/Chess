package com.github.garethrichings33;

public class Vectors {
    public static boolean areEqual(int[] vector1, int[] vector2){
        if(vector1.length != vector2.length)
            return false;

        for(int i = 0; i < vector1.length; i++)
            if(vector1[i] != vector2[i])
                return false;

        return true;
    }
}
