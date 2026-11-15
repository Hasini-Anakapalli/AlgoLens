
package com.algolens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SearchingAlgorithmsPage {

    private static final String PANEL = "#141F38";
    private static final String PURPLE = "#9655FF";
    private static final String BG = "#0B1020";

    public Parent getView() {

        VBox content = new VBox(16);
        content.setPadding(new Insets(24, 28, 24, 28));
        content.setFillWidth(true);
        content.setMaxWidth(Double.MAX_VALUE);
        content.setMinWidth(0);
        content.setStyle("-fx-background-color: " + BG + ";");

        Label title = new Label("Searching Algorithms");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label description = new Label(
                "Explore how searching algorithms locate elements through detailed step-by-step execution."
        );
        description.setWrapText(true);
        description.setStyle("-fx-text-fill: #B8C2D8; -fx-font-size: 13px;");

        // INPUT SECTION
        Label inputHeading = new Label("INPUT DATA");
        styleHeading(inputHeading);

        Label arrayLabel = new Label("Array Elements");
        arrayLabel.setStyle("-fx-text-fill: #D1D5E0;");

        TextField arrayInput = new TextField("4, 8, 12, 16, 20, 24");
        arrayInput.setPromptText("Enter numbers separated by commas");
        styleInput(arrayInput);

        Label targetLabel = new Label("Target Element");
        targetLabel.setStyle("-fx-text-fill: #D1D5E0;");

        TextField targetInput = new TextField("16");
        targetInput.setPromptText("Enter target element");
        styleInput(targetInput);

        Button linearButton = new Button("Run Linear Search");
        Button binaryButton = new Button("Run Binary Search");

        linearButton.setStyle(
                "-fx-background-color: " + PURPLE +
                "; -fx-text-fill: white; -fx-font-weight: bold;" +
                "-fx-padding: 10 18; -fx-background-radius: 8; -fx-cursor: hand;"
        );

        binaryButton.setStyle(
                "-fx-background-color: #293D63;" +
                "-fx-text-fill: white; -fx-font-weight: bold;" +
                "-fx-padding: 10 18; -fx-background-radius: 8; -fx-cursor: hand;"
        );

        HBox algorithmButtons = new HBox(10, linearButton, binaryButton);
        algorithmButtons.setAlignment(Pos.CENTER_LEFT);
        algorithmButtons.setMinHeight(40);

        VBox inputPanel = new VBox(
                9, inputHeading, arrayLabel, arrayInput,
                targetLabel, targetInput, algorithmButtons
        );
        stylePanel(inputPanel);

        // ARRAY VISUALIZATION
        Label visualizationHeading = new Label("ARRAY VISUALIZATION");
        styleHeading(visualizationHeading);

        Label visualizationDescription = new Label(
                "Highlighted cells show the element currently examined."
        );
        visualizationDescription.setWrapText(true);
        visualizationDescription.setStyle("-fx-text-fill: #AAB5CC; -fx-font-size: 12px;");

        Label indexHeading = new Label("INDEX");
        indexHeading.setStyle("-fx-text-fill: #AAB5CC;");

        HBox indexRow = new HBox(8);
        HBox arrayRow = new HBox(8);
        indexRow.setAlignment(Pos.CENTER_LEFT);
        arrayRow.setAlignment(Pos.CENTER_LEFT);

        VBox arrayVisualization = new VBox(7, indexHeading, indexRow, arrayRow);
        arrayVisualization.setPadding(new Insets(5, 2, 5, 2));

        ScrollPane arrayScroll = new ScrollPane(arrayVisualization);
        arrayScroll.setFitToHeight(true);
        arrayScroll.setFitToWidth(false);
        arrayScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        arrayScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        arrayScroll.setPrefViewportHeight(105);
        arrayScroll.setMinHeight(105);
        arrayScroll.setMaxHeight(120);
        arrayScroll.setMaxWidth(Double.MAX_VALUE);
        arrayScroll.setStyle(
                "-fx-background: " + PANEL + ";" +
                "-fx-background-color: " + PANEL + ";" +
                "-fx-control-inner-background: " + PANEL + ";" +
                "-fx-border-color: transparent;"
        );

        VBox visualizationPanel = new VBox(
                10, visualizationHeading, visualizationDescription, arrayScroll
        );
        stylePanel(visualizationPanel);

        // EXECUTION DETAILS
        Label stepCounter = new Label("Step 0");
        stepCounter.setStyle("-fx-text-fill: #AAB5CC;");

        Label stepTitle = new Label("Waiting to Start");
        stepTitle.setStyle(
                "-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;"
        );
        stepTitle.setWrapText(true);

        Label explanation = new Label(
                "Enter your array and target, then select an algorithm."
        );
        explanation.setWrapText(true);
        explanation.setStyle(
                "-fx-text-fill: #E0E6F0; -fx-font-size: 13px; -fx-line-spacing: 3px;"
        );

        Label comparisonsLabel = new Label("Total Comparisons: 0");
        Label positionLabel = new Label("Current Index: -");
        Label statusLabel = new Label("Status: Not Started");

        for (Label label : List.of(comparisonsLabel, positionLabel, statusLabel)) {
            label.setStyle("-fx-text-fill: #D1D5E0; -fx-font-size: 13px;");
        }

        VBox explanationPanel = new VBox(
                9, stepCounter, stepTitle, explanation,
                new Separator(), comparisonsLabel, positionLabel, statusLabel
        );
        stylePanel(explanationPanel);

        // NAVIGATION
        Button previousButton = new Button("← Previous");
        Button nextButton = new Button("Next →");

        Label navigationInfo = new Label("Navigate through the execution steps.");
        navigationInfo.setWrapText(true);
        navigationInfo.setStyle("-fx-text-fill: #AAB5CC;");

        HBox navigation = new HBox(12, previousButton, nextButton, navigationInfo);
        navigation.setAlignment(Pos.CENTER_LEFT);
        navigation.setPadding(new Insets(2, 0, 2, 0));

        previousButton.setStyle(
                "-fx-background-color: #293D63; -fx-text-fill: white;" +
                "-fx-padding: 9 16; -fx-background-radius: 7; -fx-cursor: hand;"
        );

        nextButton.setStyle(
                "-fx-background-color: " + PURPLE + "; -fx-text-fill: white;" +
                "-fx-padding: 9 16; -fx-background-radius: 7; -fx-cursor: hand;"
        );

        previousButton.setDisable(true);
        nextButton.setDisable(true);

        // SEARCH STATE
        List<SearchStep> steps = new ArrayList<>();
        int[] currentStep = {0};

        Runnable clearResults = () -> {
            steps.clear();
            currentStep[0] = 0;

            indexRow.getChildren().clear();
            arrayRow.getChildren().clear();

            stepCounter.setText("Step 0");
            stepTitle.setText("Waiting to Start");
            explanation.setText("Enter your array and target, then select an algorithm.");
            comparisonsLabel.setText("Total Comparisons: 0");
            positionLabel.setText("Current Index: -");
            statusLabel.setText("Status: Not Started");

            previousButton.setDisable(true);
            nextButton.setDisable(true);
        };

        Consumer<String> showError = message -> {
            clearResults.run();
            stepTitle.setText("Invalid Input");
            explanation.setText(message);
            statusLabel.setText("Status: Invalid Input");
        };

        Supplier<int[]> readArray = () -> {
            String input = arrayInput.getText().trim();

            if (input.isEmpty()) {
                throw new IllegalArgumentException("Please enter an array.");
            }

            String[] parts = input.split(",", -1);

            if (parts.length > 30) {
                throw new IllegalArgumentException(
                        "Please enter no more than 30 integers."
                );
            }

            int[] numbers = new int[parts.length];

            for (int i = 0; i < parts.length; i++) {
                if (parts[i].trim().isEmpty()) {
                    throw new IllegalArgumentException(
                            "Invalid array! Enter integers separated by commas."
                    );
                }

                numbers[i] = Integer.parseInt(parts[i].trim());
            }

            return numbers;
        };

        // RENDER STEP
        Consumer<Integer> renderStep = index -> {
            if (steps.isEmpty() || index < 0 || index >= steps.size()) return;

            currentStep[0] = index;
            SearchStep step = steps.get(index);
            int[] arr = step.getArray();

            indexRow.getChildren().clear();
            arrayRow.getChildren().clear();

            for (int i = 0; i < arr.length; i++) {
                Label indexLabel = new Label(String.valueOf(i));
                indexLabel.setMinWidth(42);
                indexLabel.setAlignment(Pos.CENTER);
                indexLabel.setStyle("-fx-text-fill: #AAB5CC; -fx-font-size: 12px;");

                Label valueLabel = new Label(String.valueOf(arr[i]));
                valueLabel.setMinSize(42, 42);
                valueLabel.setAlignment(Pos.CENTER);

                String cellStyle =
                        "-fx-background-color: #263653; -fx-text-fill: white;" +
                        "-fx-font-size: 15px; -fx-font-weight: bold;" +
                        "-fx-background-radius: 8;";

                if (i == step.getCurrentIndex()) {
                    String highlight = step.getStatus().equals("FOUND")
                            ? "#176B45" : PURPLE;

                    cellStyle =
                            "-fx-background-color: " + highlight +
                            "; -fx-text-fill: white; -fx-font-size: 15px;" +
                            "-fx-font-weight: bold; -fx-background-radius: 8;" +
                            "-fx-border-color: white; -fx-border-radius: 8;" +
                            "-fx-border-width: 1.5;";
                } else if (
                        step.getStatus().equals("LEFT") &&
                        i >= step.getMid() && i <= step.getHigh()
                ) {
                    cellStyle =
                            "-fx-background-color: #18243A;" +
                            "-fx-text-fill: #65718A; -fx-background-radius: 8;";
                } else if (
                        step.getStatus().equals("RIGHT") &&
                        i >= step.getLow() && i <= step.getMid()
                ) {
                    cellStyle =
                            "-fx-background-color: #18243A;" +
                            "-fx-text-fill: #65718A; -fx-background-radius: 8;";
                }

                valueLabel.setStyle(cellStyle);
                indexRow.getChildren().add(indexLabel);
                arrayRow.getChildren().add(valueLabel);
            }

            stepCounter.setText("Step " + (index + 1) + " of " + steps.size());
            stepTitle.setText(step.getTitle());
            explanation.setText(step.getExplanation());

            comparisonsLabel.setText("Total Comparisons: " + step.getComparisons());

            positionLabel.setText(
                    step.getCurrentIndex() == -1
                            ? "Current Index: -"
                            : "Current Index: " + step.getCurrentIndex()
            );

            statusLabel.setText("Status: " + step.getStatus().replace("_", " "));

            previousButton.setDisable(index == 0);
            nextButton.setDisable(index == steps.size() - 1);
        };

        // LINEAR SEARCH
        linearButton.setOnAction(e -> {
            clearResults.run();

            try {
                int[] numbers = readArray.get();
                String targetText = targetInput.getText().trim();

                if (targetText.isEmpty()) {
                    throw new IllegalArgumentException("Please enter a target element.");
                }

                int target = Integer.parseInt(targetText);

                steps.addAll(LinearSearch.search(numbers, target));

                if (steps.isEmpty()) {
                    showError.accept("No execution steps were generated.");
                    return;
                }

                renderStep.accept(0);

            } catch (NumberFormatException ex) {
                showError.accept("Invalid input! Please enter valid integers.");
            } catch (IllegalArgumentException ex) {
                showError.accept(ex.getMessage());
            }
        });

        // BINARY SEARCH
        binaryButton.setOnAction(e -> {
            clearResults.run();

            try {
                int[] numbers = readArray.get();
                String targetText = targetInput.getText().trim();

                if (targetText.isEmpty()) {
                    throw new IllegalArgumentException("Please enter a target element.");
                }

                int target = Integer.parseInt(targetText);

                for (int i = 1; i < numbers.length; i++) {
                    if (numbers[i] < numbers[i - 1]) {
                        throw new IllegalArgumentException(
                                "Binary Search requires a sorted array.\n\n" +
                                "Please arrange the elements in ascending order."
                        );
                    }
                }

                steps.addAll(BinarySearch.search(numbers, target));

                if (steps.isEmpty()) {
                    showError.accept("No execution steps were generated.");
                    return;
                }

                renderStep.accept(0);

            } catch (NumberFormatException ex) {
                showError.accept("Invalid input! Please enter valid integers.");
            } catch (IllegalArgumentException ex) {
                showError.accept(ex.getMessage());
            }
        });

        // PREVIOUS STEP
        previousButton.setOnAction(e -> {
            if (currentStep[0] > 0) {
                renderStep.accept(currentStep[0] - 1);
            }
        });

        // NEXT STEP
        nextButton.setOnAction(e -> {
            if (currentStep[0] < steps.size() - 1) {
                renderStep.accept(currentStep[0] + 1);
            }
        });

        content.getChildren().addAll(
                title,
                description,
                inputPanel,
                visualizationPanel,
                explanationPanel,
                navigation
        );

        return content;
    }

    private void stylePanel(VBox panel) {
        panel.setPadding(new Insets(17));
        panel.setMaxWidth(Double.MAX_VALUE);
        panel.setMinWidth(0);
        panel.setFillWidth(true);

        panel.setStyle(
                "-fx-background-color: " + PANEL +
                "; -fx-background-radius: 12;" +
                "-fx-border-color: #293653; -fx-border-radius: 12;"
        );
    }

    private void styleHeading(Label label) {
        label.setStyle(
                "-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: white;"
        );
    }

    private void styleInput(TextField field) {
        field.setMaxWidth(Double.MAX_VALUE);

        field.setStyle(
                "-fx-background-color: #202E49; -fx-text-fill: white;" +
                "-fx-prompt-text-fill: #8491AA; -fx-background-radius: 7;" +
                "-fx-padding: 10;"
        );
    }
}
