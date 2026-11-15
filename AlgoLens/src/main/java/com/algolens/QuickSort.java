
package com.algolens;

import java.util.ArrayList;
import java.util.List;

public class QuickSort {

    private static List<App.SortStep> steps;
    private static int comparisons;
    private static int swaps;

    public static List<App.SortStep> sort(int[] input) {

        steps = new ArrayList<>();
        comparisons = 0;
        swaps = 0;

        int[] arr = input.clone();

        steps.add(new App.SortStep(
                arr, comparisons, swaps, -1, -1,
                "Initial Array",
                "Quick Sort uses divide and conquer. It selects a pivot and rearranges elements so smaller values go to its left and larger values go to its right."
        ));

        quickSort(arr, 0, arr.length - 1);

        steps.add(new App.SortStep(
                arr, comparisons, swaps, -1, -1,
                "Sorting Completed",
                "All partitions have been processed. The array is now sorted in ascending order."
        ));

        return steps;
    }

    private static void quickSort(int[] arr, int low, int high) {

        if (low < high) {

            int pivotIndex = partition(arr, low, high);

            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {

        int pivot = arr[high];

        steps.add(new App.SortStep(
                arr, comparisons, swaps, high, -1,
                "Selecting the Pivot",
                "The last element of this portion, " + pivot +
                ", is selected as the pivot. Other elements will be arranged around it."
        ));

        int i = low - 1;

        for (int j = low; j < high; j++) {

            comparisons++;

            steps.add(new App.SortStep(
                    arr, comparisons, swaps, j, high,
                    "Comparing with Pivot",
                    "Compare " + arr[j] + " with pivot " + pivot +
                    ". Values smaller than or equal to the pivot belong on its left side."
            ));

            if (arr[j] <= pivot) {

                i++;

                if (i != j) {

                    swap(arr, i, j);
                    swaps++;

                    steps.add(new App.SortStep(
                            arr, comparisons, swaps, i, j,
                            "Swapping Elements",
                            "Move " + arr[j] +
                            " into the left partition because it is smaller than or equal to the pivot."
                    ));
                }
            }
        }

        int pivotPosition = i + 1;

        if (pivotPosition != high) {

            swap(arr, pivotPosition, high);
            swaps++;
        }

        steps.add(new App.SortStep(
                arr, comparisons, swaps, pivotPosition, pivotPosition,
                "Pivot Placed",
                "The pivot " + pivot +
                " is now at its correct position, index " + pivotPosition +
                ". Elements on its left are smaller or equal, and elements on its right are greater."
        ));

        return pivotPosition;
    }

    private static void swap(int[] arr, int i, int j) {

        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
