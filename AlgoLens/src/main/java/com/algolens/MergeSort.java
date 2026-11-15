
package com.algolens;

import java.util.ArrayList;
import java.util.List;

public class MergeSort {

    private static List<App.SortStep> steps;
    private static int comparisons;
    private static int moves;

    public static List<App.SortStep> sort(int[] input) {

        steps = new ArrayList<>();
        comparisons = 0;
        moves = 0;

        int[] arr = input.clone();

        steps.add(new App.SortStep(
                arr, comparisons, moves, -1, -1,
                "Initial Array",
                "Merge Sort uses divide and conquer. It repeatedly divides the array into smaller parts, sorts them and merges them."
        ));

        mergeSort(arr, 0, arr.length - 1);

        steps.add(new App.SortStep(
                arr, comparisons, moves, -1, -1,
                "Sorting Completed",
                "All smaller portions have been merged into one fully sorted array."
        ));

        return steps;
    }

    private static void mergeSort(int[] arr, int left, int right) {

        if (left >= right) {
            return;
        }

        int middle = left + (right - left) / 2;

        steps.add(new App.SortStep(
                arr, comparisons, moves, left, right,
                "Dividing the Array",
                "Divide the current portion from index " + left +
                " to index " + right + " into two smaller portions."
        ));

        mergeSort(arr, left, middle);
        mergeSort(arr, middle + 1, right);

        merge(arr, left, middle, right);
    }

    private static void merge(
            int[] arr, int left, int middle, int right) {

        int n1 = middle - left + 1;
        int n2 = right - middle;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; i++) {
            L[i] = arr[left + i];
        }

        for (int j = 0; j < n2; j++) {
            R[j] = arr[middle + 1 + j];
        }

        steps.add(new App.SortStep(
                arr, comparisons, moves, left, right,
                "Preparing to Merge",
                "The two smaller portions are ready. We will compare their elements and combine them in sorted order."
        ));

        int i = 0;
        int j = 0;
        int k = left;

        while (i < n1 && j < n2) {

            comparisons++;

            if (L[i] <= R[j]) {

                arr[k] = L[i];
                i++;

            } else {

                arr[k] = R[j];
                j++;
            }

            moves++;

            steps.add(new App.SortStep(
                    arr, comparisons, moves, k, k,
                    "Merging Elements",
                    "The smaller available element has been copied into index " +
                    k + ". The merge process continues with the remaining elements."
            ));

            k++;
        }

        while (i < n1) {

            arr[k] = L[i];
            i++;
            moves++;

            steps.add(new App.SortStep(
                    arr, comparisons, moves, k, k,
                    "Copying Remaining Element",
                    "Copy the remaining left-side element into index " + k +
                    ". No comparison is needed because the other portion is empty."
            ));

            k++;
        }

        while (j < n2) {

            arr[k] = R[j];
            j++;
            moves++;

            steps.add(new App.SortStep(
                    arr, comparisons, moves, k, k,
                    "Copying Remaining Element",
                    "Copy the remaining right-side element into index " + k +
                    ". The merge operation is almost complete."
            ));

            k++;
        }

        steps.add(new App.SortStep(
                arr, comparisons, moves, left, right,
                "Portion Merged",
                "The portion from index " + left + " to " + right +
                " has been merged in sorted order."
        ));
    }
}
