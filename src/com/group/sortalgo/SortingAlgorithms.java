package com.group.sortalgo;

public class SortingAlgorithms {

    // Shell Sort
    public static void shellSort(double[] arr) {
        int n = arr.length;

        // start with big gap, then reduce
        for (int gap = n / 2; gap > 0; gap /= 2) {
            // do a gapped insertion sort
            for (int i = gap; i < n; i++) {
                double temp = arr[i];
                int j = i;

                while (j >= gap && arr[j - gap] > temp) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                arr[j] = temp;
            }
        }
    }

    // Quick Sort
    public static void quickSort(double[] arr) {
        quickSort(arr, 0, arr.length - 1);
    }

    private static void quickSort(double[] arr, int low, int high) {
        if (low >= high) return;

        int p = partition(arr, low, high);
        quickSort(arr, low, p - 1);
        quickSort(arr, p + 1, high);
    }

    private static int partition(double[] arr, int low, int high) {
        double pivot = arr[high];   // last element as pivot
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                double tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
            }
        }

        double tmp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = tmp;

        return i + 1;
    }

}
