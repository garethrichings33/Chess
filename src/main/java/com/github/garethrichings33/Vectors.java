package com.github.garethrichings33;

public class Vectors {
    public static int[] sum(int[] vector1, int[] vector2){
        if(vector1.length != vector2.length)
            throw new IllegalArgumentException();

        int[] result = new int[vector1.length];
        for(int i = 0; i < vector1.length; i++)
            result[i] = vector1[i] + vector2[i];

        return result;
    }

    public static int[] difference(int[] vector1, int[] vector2){
        if(vector1.length != vector2.length)
            throw new IllegalArgumentException();

        int[] result = new int[vector1.length];
        for(int i = 0; i < vector1.length; i++)
            result[i] = vector1[i] - vector2[i];

        return result;
    }

    public static int[] multiplyByInteger(int scalar, int[] vector){
        int[] result = new int[vector.length];
        for(int i = 0; i < vector.length; i++)
            result[i] = scalar * vector[i];
        return result;
    }
    public static int[] scaleToLargestValue(int[] vector){
        int[] result = new int[vector.length];
        int largestMagnitude = getLargestMagnitude(vector);
        if(largestMagnitude == 0)
            largestMagnitude = 1;

        for(int i = 0; i< vector.length; i++)
            result[i] = vector[i] / largestMagnitude;

        return result;
    }

    private static int getLargestMagnitude(int[] vector){
        int largestElement = Integer.MIN_VALUE;
        for(int i = 0; i < vector.length; i++)
            if(Math.abs(vector[i]) > largestElement)
                largestElement = Math.abs(vector[i]);

        return largestElement;
    }
}
