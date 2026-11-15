
package com.algolens;

import java.util.ArrayList;
import java.util.List;

public class BubbleSort {

    public static List<App.SortStep> sort(int[] input) {

        List<App.SortStep> steps = new ArrayList<>();

        int[] arr = input.clone();

        int comparisons = 0;
        int swaps = 0;

        steps.add(new App.SortStep(
                arr, comparisons, swaps, -1, -1,
                "Initial Array",
                "This is the original array. Bubble Sort will compare adjacent elements and move larger values towards the end."
        ));

        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {

            boolean swappedInPass = false;

            for (int j = 0; j < n - i - 1; j++) {

                comparisons++;

                steps.add(new App.SortStep(
                        arr, comparisons, swaps, j, j + 1,
                        "Comparing Adjacent Elements",
                        "Compare " + arr[j] + " and " + arr[j + 1] +
                        ". If the left element is greater than the right element, they must be swapped."
                ));

                if (arr[j] > arr[j + 1]) {

                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;

                    swaps++;
                    swappedInPass = true;

                    steps.add(new App.SortStep(
                            arr, comparisons, swaps, j, j + 1,
                            "Elements Swapped",
                            "Since " + arr[j] + " is now on the left and " +
                            arr[j + 1] + " is on the right, the adjacent values have been exchanged to move the larger value forward."
                    ));

                } else {

                    steps.add(new App.SortStep(
                            arr, comparisons, swaps, j, j + 1,
                            "No Swap Required",
                            "The elements are already in the correct relative order, so no swap is needed."
                    ));
                }
            }

            if (!swappedInPass) {
                break;
            }
        }

        steps.add(new App.SortStep(
                arr, comparisons, swaps, -1, -1,
                "Sorting Completed",
                "Every required comparison has been performed. The array is now sorted in ascending order."
        ));

        return steps;
    }
}
