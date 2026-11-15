
package com.algolens;

import java.util.ArrayList;
import java.util.List;

public class LinearSearch {

    public static List<SearchStep> search(int[] input, int target) {

        List<SearchStep> steps = new ArrayList<>();
        int[] arr = input.clone();

        int comparisons = 0;
        int stepNumber = 0;
        int foundIndex = -1;

        steps.add(new SearchStep(
                arr, stepNumber, -1, -1, target,
                comparisons, -1, -1, -1, -1,
                "Initial Array",
                "Linear Search starts from the first element of the array. "
                + "It checks each element one by one until the target is found "
                + "or all elements have been examined.",
                "INITIAL"
        ));

        for (int i = 0; i < arr.length; i++) {

            stepNumber++;
            comparisons++;

            String explanation =
                    "We are currently examining the element at index " + i
                    + ". Its value is " + arr[i] + ".\n\n"
                    + "Now compare " + arr[i] + " with the target " + target + ".\n\n";

            if (arr[i] == target) {

                foundIndex = i;

                explanation +=
                        "Since " + arr[i] + " is equal to " + target
                        + ", the target has been successfully found.\n\n"
                        + "The algorithm stops immediately because there is "
                        + "no need to examine the remaining elements.";

                steps.add(new SearchStep(
                        arr, stepNumber, i, arr[i], target,
                        comparisons, -1, -1, -1, foundIndex,
                        "Target Found!",
                        explanation,
                        "FOUND"
                ));

                break;

            } else {

                explanation +=
                        "Since " + arr[i] + " is not equal to " + target
                        + ", this element is not the required value.\n\n";

                if (i < arr.length - 1) {
                    explanation +=
                            "The algorithm moves to the next index, "
                            + (i + 1) + ", and continues searching.";
                } else {
                    explanation +=
                            "This was the last element in the array. "
                            + "No matching element exists.";
                }

                steps.add(new SearchStep(
                        arr, stepNumber, i, arr[i], target,
                        comparisons, -1, -1, -1, -1,
                        "Comparing Elements",
                        explanation,
                        "CHECKING"
                ));
            }
        }

        if (foundIndex == -1) {

            steps.add(new SearchStep(
                    arr, stepNumber + 1, -1, -1, target,
                    comparisons, -1, -1, -1, -1,
                    "Search Completed",
                    "Every element in the array has been checked. "
                    + "The target " + target + " does not exist in the array.\n\n"
                    + "Therefore, Linear Search returns -1.",
                    "NOT_FOUND"
            ));
        }

        return steps;
    }
}
