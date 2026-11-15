
package com.algolens;

import java.util.ArrayList;
import java.util.List;

public class BinarySearch {

    public static List<SearchStep> search(int[] input, int target) {

        List<SearchStep> steps = new ArrayList<>();
        int[] arr = input.clone();

        // Binary Search requires sorted input.
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                throw new IllegalArgumentException(
                        "Binary Search requires a sorted array."
                );
            }
        }

        int low = 0;
        int high = arr.length - 1;
        int comparisons = 0;
        int stepNumber = 0;

        steps.add(new SearchStep(
                arr, stepNumber, -1, -1, target,
                comparisons, low, high, -1, -1,
                "Initial Search Range",
                "Binary Search begins with the entire sorted array.\n\n"
                + "Low points to the first index, and High points to the last index.\n\n"
                + "The algorithm repeatedly checks the middle element and "
                + "eliminates half of the remaining search range.",
                "INITIAL"
        ));

        while (low <= high) {

            int mid = low + (high - low) / 2;
            comparisons++;
            stepNumber++;

            int value = arr[mid];

            String explanation =
                    "CURRENT SEARCH RANGE\n"
                    + "Low = " + low + "\n"
                    + "High = " + high + "\n\n"
                    + "MIDDLE INDEX CALCULATION\n"
                    + "Mid = Low + (High - Low) / 2\n"
                    + "Mid = " + mid + "\n\n"
                    + "The middle element is " + value + ".\n\n"
                    + "Now compare " + value + " with target " + target + ".\n\n";

            if (value == target) {

                explanation +=
                        "MATCH FOUND!\n\n"
                        + "The middle element is equal to the target.\n"
                        + "The algorithm stops searching.";

                steps.add(new SearchStep(
                        arr, stepNumber, mid, value, target,
                        comparisons, low, high, mid, mid,
                        "Target Found!",
                        explanation,
                        "FOUND"
                ));

                break;

            } else if (value < target) {

                explanation +=
                        value + " is smaller than " + target + ".\n\n"
                        + "Therefore, the target can only be in the right half.\n"
                        + "All elements from index " + low + " through " + mid
                        + " are eliminated from the remaining search range.\n\n"
                        + "Update Low = Mid + 1 = " + (mid + 1) + ".";

                steps.add(new SearchStep(
                        arr, stepNumber, mid, value, target,
                        comparisons, low, high, mid, -1,
                        "Searching the Right Half",
                        explanation,
                        "RIGHT"
                ));

                low = mid + 1;

            } else {

                explanation +=
                        value + " is greater than " + target + ".\n\n"
                        + "Therefore, the target can only be in the left half.\n"
                        + "All elements from index " + mid + " through " + high
                        + " are eliminated from the remaining search range.\n\n"
                        + "Update High = Mid - 1 = " + (mid - 1) + ".";

                steps.add(new SearchStep(
                        arr, stepNumber, mid, value, target,
                        comparisons, low, high, mid, -1,
                        "Searching the Left Half",
                        explanation,
                        "LEFT"
                ));

                high = mid - 1;
            }
        }

        if (steps.get(steps.size() - 1).getStatus().equals("FOUND")) {
            return steps;
        }

        steps.add(new SearchStep(
                arr, stepNumber + 1, -1, -1, target,
                comparisons, low, high, -1, -1,
                "Search Completed",
                "The search range is now empty because Low is greater than High.\n\n"
                + "The target " + target + " does not exist in the array.\n\n"
                + "Binary Search returns -1.",
                "NOT_FOUND"
        ));

        return steps;
    }
}
