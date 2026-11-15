
package com.algolens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class SortingTechniquesPage {

    private final Consumer<String> onAlgorithmSelected;

    public SortingTechniquesPage(Consumer<String> onAlgorithmSelected) {
        this.onAlgorithmSelected = onAlgorithmSelected;
    }

    public Parent createPage() {

        VBox page = new VBox(22);
        page.setPadding(new Insets(30));
        page.setStyle("-fx-background-color: #0B1020;");

        Label heading = new Label("Sorting Algorithms");
        heading.setStyle(
                "-fx-font-size: 29px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;"
        );

        Label subtitle = new Label(
                "Choose an algorithm to explore its working and complexity."
        );
        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #AAB4CC;"
        );

        FlowPane cards = new FlowPane();
        cards.setHgap(20);
        cards.setVgap(20);
        cards.setPrefWrapLength(900);

        cards.getChildren().addAll(
                createCard(
                        "Bubble Sort",
                        "Repeatedly compares adjacent elements and swaps them if they are in the wrong order.",
                        "Average: O(n²)",
                        "Space: O(1)"
                ),
                createCard(
                        "Selection Sort",
                        "Finds the smallest element in the unsorted part and places it in its correct position.",
                        "Average: O(n²)",
                        "Space: O(1)"
                ),
                createCard(
                        "Insertion Sort",
                        "Builds a sorted portion by inserting each element into its correct position.",
                        "Average: O(n²)",
                        "Space: O(1)"
                ),
                createCard(
                        "Merge Sort",
                        "Divides the array into smaller parts, sorts them and merges them.",
                        "Average: O(n log n)",
                        "Space: O(n)"
                ),
                createCard(
                        "Quick Sort",
                        "Uses a pivot to divide elements into smaller and larger groups.",
                        "Average: O(n log n)",
                        "Space: O(log n) average"
                )
        );

        page.getChildren().addAll(heading, subtitle, cards);

        return page;
    }

    private VBox createCard(
            String name,
            String description,
            String timeComplexity,
            String spaceComplexity) {

        VBox card = new VBox(13);
        card.setPadding(new Insets(22));
        card.setPrefWidth(290);
        card.setMinHeight(235);
        card.setStyle(
                "-fx-background-color: #171F35;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: #303B59;" +
                "-fx-border-radius: 14;"
        );

        Label title = new Label(name);
        title.setStyle(
                "-fx-font-size: 19px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #C4B5FD;"
        );

        Label details = new Label(description);
        details.setWrapText(true);
        details.setMinHeight(55);
        details.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #D1D5E5;"
        );

        Label time = new Label(timeComplexity);
        Label space = new Label(spaceComplexity);

        time.setStyle("-fx-text-fill: #AAB4CC;");
        space.setStyle("-fx-text-fill: #AAB4CC;");

        Button exploreButton = new Button("Explore Algorithm →");

        exploreButton.setStyle(
                "-fx-background-color: #6D4AFF;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 7;" +
                "-fx-padding: 10 15 10 15;" +
                "-fx-cursor: hand;"
        );

        exploreButton.setOnAction(
                e -> onAlgorithmSelected.accept(name)
        );

        card.getChildren().addAll(
                title, details, time, space, exploreButton
        );

        return card;
    }
}
