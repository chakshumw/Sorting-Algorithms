package com.group.sortalgo;

public class SortingAlgorithms {

    // Insertion Sort
    public static void insertionSort(double[] arr) {
        for (int i = 1; i < arr.length; i++) {
            double key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // Merge Sort
    public static void mergeSort(double[] arr) {
        mergeSort(arr, 0, arr.length - 1);
    }

    private static void mergeSort(double[] arr, int left, int right) {
        if (left >= right) return;

        int mid = (left + right) / 2;

        // sort left half
        mergeSort(arr, left, mid);
        // sort right half
        mergeSort(arr, mid + 1, right);
        // merge them
        merge(arr, left, mid, right);
    }

    private static void merge(double[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        double[] L = new double[n1];
        double[] R = new double[n2];

        for (int i = 0; i < n1; i++) L[i] = arr[left + i];
        for (int j = 0; j < n2; j++) R[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;

        // merge two sorted arrays L and R
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }

        // copy remaining
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }


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
