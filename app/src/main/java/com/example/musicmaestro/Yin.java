package com.example.musicmaestro;

// Yin.java

public class Yin {
    private static final int DEFAULT_THRESHOLD = 20;

    public static int pitchDetection(short[] audioBuffer, int sampleRate) {
        int bufferSize = audioBuffer.length;

        // Calculate the difference function
        double[] yinBuffer = new double[bufferSize / 2];
        for (int tau = 0; tau < bufferSize / 2; tau++) {
            yinBuffer[tau] = differenceFunction(audioBuffer, tau, bufferSize);
        }

        // Calculate the cumulative mean normalized difference function
        cumulativeMeanNormalizedDifference(yinBuffer);

        // Find the first minimum below the threshold
        int tauEstimate = absoluteThreshold(yinBuffer, DEFAULT_THRESHOLD);

        // Interpolate to improve the estimate
        if (tauEstimate != -1 && tauEstimate < yinBuffer.length - 1) {
            double betterTau = parabolicInterpolation(yinBuffer, tauEstimate);
            return (int) (sampleRate / betterTau);
        }

        return -1; // Unable to determine pitch
    }

    private static double differenceFunction(short[] audioBuffer, int tau, int bufferSize) {
        double difference = 0;
        for (int i = 0; i < bufferSize / 2; i++) {
            difference += Math.pow((audioBuffer[i] - audioBuffer[i + tau]), 2);
        }
        return difference;
    }

    private static void cumulativeMeanNormalizedDifference(double[] yinBuffer) {
        double runningSum = 0;
        yinBuffer[0] = 1; // Avoid division by zero
        for (int tau = 1; tau < yinBuffer.length; tau++) {
            runningSum += yinBuffer[tau];
            yinBuffer[tau] *= tau / runningSum;
        }
    }

    private static int absoluteThreshold(double[] yinBuffer, int threshold) {
        for (int tau = 2; tau < yinBuffer.length; tau++) {
            if (yinBuffer[tau] < threshold) {
                while (tau + 1 < yinBuffer.length && yinBuffer[tau + 1] < yinBuffer[tau]) {
                    tau++;
                }
                return tau;
            }
        }
        return -1; // No pitch found
    }

    private static double parabolicInterpolation(double[] yinBuffer, int tauEstimate) {
        double betterTau;
        if (tauEstimate > 1 && tauEstimate < yinBuffer.length - 1) {
            double y1 = yinBuffer[tauEstimate - 1];
            double y2 = yinBuffer[tauEstimate];
            double y3 = yinBuffer[tauEstimate + 1];
            betterTau = tauEstimate + (0.5 * (y3 - y1) / (2 * y2 - y1 - y3));
        } else {
            betterTau = tauEstimate;
        }
        return betterTau;
    }
}

