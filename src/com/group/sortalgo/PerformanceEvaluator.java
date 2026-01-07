package com.group.sortalgo;

import java.util.HashMap;
import java.util.Map;

// Measures how long each algorithm takes
public class PerformanceEvaluator {

    // Run ONE algorithm and measure its time in nanoseconds
    public static long measureTime(double[] original, String algorithmName) {
        // Clone so we don't sort the same array in-place each time
        double[] data = original.clone();

        long start = System.nanoTime();

        switch (algorithmName) {
            case "Insertion Sort":
                SortingAlgorithms.insertionSort(data);
                break;
            case "Shell Sort":
                SortingAlgorithms.shellSort(data);
                break;
            case "Merge Sort":
                SortingAlgorithms.mergeSort(data);
                break;
            case "Quick Sort":
                SortingAlgorithms.quickSort(data);
                break;
            case "Heap Sort":
                SortingAlgorithms.heapSort(data);
                break;
            default:
                throw new IllegalArgumentException("Unknown algorithm: " + algorithmName);
        }

        long end = System.nanoTime();
        return end - start;
    }

    // Run ALL algorithms and get their times in a map
    public static Map<String, Long> runAll(double[] data) {
        Map<String, Long> times = new HashMap<>();

        times.put("Insertion Sort", measureTime(data, "Insertion Sort"));
        times.put("Shell Sort",     measureTime(data, "Shell Sort"));
        times.put("Merge Sort",     measureTime(data, "Merge Sort"));
        times.put("Quick Sort",     measureTime(data, "Quick Sort"));
        times.put("Heap Sort",      measureTime(data, "Heap Sort"));

        return times;
    }

    // Find which algorithm is the fastest
    public static String bestAlgorithm(Map<String, Long> times) {
        String bestName = null;
        long bestTime = Long.MAX_VALUE;

        for (Map.Entry<String, Long> entry : times.entrySet()) {
            if (entry.getValue() < bestTime) {
                bestTime = entry.getValue();
                bestName = entry.getKey();
            }
        }

        return bestName;
    }
}
