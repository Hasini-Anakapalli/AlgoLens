
package com.algolens;

public class SearchStep {

    private final int[] array;
    private final int stepNumber;
    private final int currentIndex;
    private final int currentValue;
    private final int target;
    private final int comparisons;
    private final int low;
    private final int high;
    private final int mid;
    private final int foundIndex;
    private final String title;
    private final String explanation;
    private final String status;

    public SearchStep(
            int[] array,
            int stepNumber,
            int currentIndex,
            int currentValue,
            int target,
            int comparisons,
            int low,
            int high,
            int mid,
            int foundIndex,
            String title,
            String explanation,
            String status) {

        this.array = array.clone();
        this.stepNumber = stepNumber;
        this.currentIndex = currentIndex;
        this.currentValue = currentValue;
        this.target = target;
        this.comparisons = comparisons;
        this.low = low;
        this.high = high;
        this.mid = mid;
        this.foundIndex = foundIndex;
        this.title = title;
        this.explanation = explanation;
        this.status = status;
    }

    public int[] getArray() {
        return array.clone();
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public int getTarget() {
        return target;
    }

    public int getComparisons() {
        return comparisons;
    }

    public int getLow() {
        return low;
    }

    public int getHigh() {
        return high;
    }

    public int getMid() {
        return mid;
    }

    public int getFoundIndex() {
        return foundIndex;
    }

    public String getTitle() {
        return title;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getStatus() {
        return status;
    }
}
