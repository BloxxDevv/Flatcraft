package com.bloxxdev.flatcraft.world.worldgen;

public class PerlinNoise {
    private static double[] gradients;

    private static int currentSign;

    public static void generateOutputArray(int[] array, double gradRange, int gridSize, int sign, int worldHeight){
        int width = array.length;

        currentSign = sign;

        generateGradients(width, gradRange, gridSize);

        if (sign <= 0){
            Worldgen.leftGradient = gradients[0];
        }
        if (sign >= 0){
            Worldgen.rightGradient = gradients[gradients.length-1];
        }

        for (int i = 0; i < width; i++) {
            int leftGradIndex = i/gridSize;
            int rightGradIndex = leftGradIndex+1;

            double leftGrad = gradients[leftGradIndex];
            double rightGrad = gradients[rightGradIndex];

            int leftDist = i-leftGradIndex*gridSize;
            int rightDist = i-rightGradIndex*gridSize;

            double leftContribution = leftGrad*leftDist;
            double rightContribution = rightGrad*rightDist;

            double t = fade((double)leftDist/gridSize);

            array[i] = (int)Math.round(lerp(t, leftContribution, rightContribution)) + worldHeight;
        }
    }

    private static double lerp(double t, double a, double b){
        return (1-t)*a+t*b;
    }

    private static double fade(double t){
        return t*t*t*(10-t*(15-t*6));
    }

    private static void generateGradients(int width, double gradRange, int gridSize){
        gradients = new double[width/gridSize+2];

        for (int i = 0; i < gradients.length; i++) {
            gradients[i] = Math.random()*gradRange - gradRange/2;
        }

        if (currentSign < 0){
            gradients[gradients.length-1] = Worldgen.rightGradient;
        }
        if (currentSign > 0){
            gradients[0] = Worldgen.leftGradient;
        }
    }

}
