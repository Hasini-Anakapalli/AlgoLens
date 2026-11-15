
package com.algolens;

import java.util.ArrayList;
import java.util.List;

public class InsertionSort {

    public static List<App.SortStep> sort(int[] input) {

        List<App.SortStep> steps = new ArrayList<>();

        int[] arr = input.clone();

        int comparisons = 0;
        int moves = 0;

        steps.add(new App.SortStep(
                arr, comparisons, moves, -1, -1,
                "Initial Array",
                "Insertion Sort starts by treating the first element as a sorted portion. The remaining elements will be inserted into their correct positions."
        ));

        for (int i = 1; i < arr.length; i++) {

            int key = arr[i];
            int j = i - 1;

            steps.add(new App.SortStep(
                    arr, comparisons, moves, i, -1,
                    "Selecting the Key",
                    "The current key is " + key +
                    ". We will find its correct position in the already sorted portion to its left."
            ));

            while (j >= 0) {

                comparisons++;

                steps.add(new App.SortStep(
                        arr, comparisons, moves, j, i,
                        "Comparing with the Key",
                        "Compare " + arr[j] + " with key " + key +
                        ". If the left element is greater, it must move one position to the right."
                ));

                if (arr[j] > key) {

                    arr[j + 1] = arr[j];
                    moves++;

                    steps.add(new App.SortStep(
                            arr, comparisons, moves, j, j + 1,
                            "Shifting Element",
                            arr[j] + " is greater than " + key +
                            ". Move it one position to the right to make space for the key."
                    ));

                    j--;

                } else {
                    break;
                }
            }

            arr[j + 1] = key;
            moves++;

            steps.add(new App.SortStep(
                    arr, comparisons, moves, j + 1, i,
                    "Inserting the Key",
                    "Insert " + key + " at index " + (j + 1) +
                    ". The sorted portion now contains one more element."
            ));
        }

        steps.add(new App.SortStep(
                arr, comparisons, moves, -1, -1,
                "Sorting Completed",
                "Every element has been inserted into its correct position. The array is now sorted in ascending order."
        ));

        return steps;
    }
}
