
package com.algolens;

import java.util.ArrayList;
import java.util.List;

public class SelectionSort {

    public static List<App.SortStep> sort(int[] input) {

        List<App.SortStep> steps = new ArrayList<>();

        int[] arr = input.clone();

        int comparisons = 0;
        int swaps = 0;

        steps.add(new App.SortStep(
                arr, comparisons, swaps, -1, -1,
                "Initial Array",
                "Selection Sort divides the array into a sorted and an unsorted portion. Initially, the entire array is unsorted."
        ));

        for (int i = 0; i < arr.length - 1; i++) {

            int minIndex = i;

            steps.add(new App.SortStep(
                    arr, comparisons, swaps, i, minIndex,
                    "Finding the Minimum",
                    "Assume that " + arr[i] +
                    " is the smallest element in the unsorted portion. We will compare it with the remaining elements."
            ));

            for (int j = i + 1; j < arr.length; j++) {

                comparisons++;

                steps.add(new App.SortStep(
                        arr, comparisons, swaps, minIndex, j,
                        "Comparing Elements",
                        "Compare the current minimum " + arr[minIndex] +
                        " with " + arr[j] + ". We are searching for a smaller value."
                ));

                if (arr[j] < arr[minIndex]) {

                    minIndex = j;

                    steps.add(new App.SortStep(
                            arr, comparisons, swaps, minIndex, j,
                            "New Minimum Found",
                            arr[minIndex] +
                            " is smaller than the previous minimum. Its index is now the new minimum index."
                    ));
                }
            }

            if (minIndex != i) {

                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;

                swaps++;

                steps.add(new App.SortStep(
                        arr, comparisons, swaps, i, minIndex,
                        "Minimum Element Placed",
                        "The smallest element of the unsorted portion has been swapped into position " +
                        i + ". The sorted portion has grown by one element."
                ));

            } else {

                steps.add(new App.SortStep(
                        arr, comparisons, swaps, i, minIndex,
                        "Element Already in Position",
                        "The current position already contains the smallest element, so no swap is necessary."
                ));
            }
        }

        steps.add(new App.SortStep(
                arr, comparisons, swaps, -1, -1,
                "Sorting Completed",
                "The unsorted portion is empty. All elements are now in ascending order."
        ));

        return steps;
    }
}
