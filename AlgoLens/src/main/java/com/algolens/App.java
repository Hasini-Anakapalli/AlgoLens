package com.algolens;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class App extends Application {

    private static final String BG = "#0B1020";
    private static final String SIDEBAR = "#11182B";
    private static final String PANEL = "#151E35";
    private static final String PURPLE = "#8B5CF6";
    private static final String LAVENDER = "#C4B5FD";
    private static final String WHITE = "#F8FAFC";
    private static final String MUTED = "#94A3B8";

    private Stage stage;
    private BorderPane root;
    private VBox sidebar;
    private Label pageName;
    private Label status;
    private TextField dashboardSearch;
    private ScrollPane dashboardScroll;

    private Button homeBtn, sortingBtn, searchingBtn, graphBtn, complexityBtn;
    private Button exitBtn;

    private String selectedAlgorithm = "Bubble Sort";
    private int[] originalArray = {8, 3, 10, 1, 6, 4};
    private final List<SortStep> sortSteps = new ArrayList<>();
    private int currentStep;
    private Timeline playback;
    private boolean playing;

    private HBox barsBox;
    private Label stepCountLabel, comparisonValue, operationValue;
    private Label explanationTitle, explanationText, resultLabel;
    private Label bestValue, averageValue, worstValue, spaceValue;
    private Button previousButton, playButton, nextButton, resetButton;
    private Slider speedSlider;
    private Label speedValue;
    private TextField arrayInput;

    static class SortStep {
        int[] values;
        int first;
        int second;
        int comparisons;
        int operations;
        String title;
        String explanation;

        SortStep(int[] values, int first, int second, int comparisons,
                 int operations, String title, String explanation) {
            this.values = values.clone();
            this.first = first;
            this.second = second;
            this.comparisons = comparisons;
            this.operations = operations;
            this.title = title;
            this.explanation = explanation;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("AlgoLens - Algorithm Visualizer");
        buildShell();
        showDashboard();
        stage.show();
    }

    private void buildShell() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG + ";");

        sidebar = new VBox(23);
        sidebar.setPrefWidth(250);
        sidebar.setMinWidth(225);
        sidebar.setPadding(new Insets(32, 20, 25, 20));
        sidebar.setStyle("-fx-background-color: " + SIDEBAR + ";" +
                "-fx-border-color: #252D45; -fx-border-width: 0 1 0 0;");

        Label logo = new Label("AlgoLens");
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 27));
        logo.setTextFill(Color.web("#A78BFA"));

        Label subtitle = new Label("ALGORITHM LEARNING");
        subtitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        subtitle.setTextFill(Color.web("#8490AA"));

        Label workspace = smallSidebarHeading("WORKSPACE");
        homeBtn = navButton("Dashboard");
        sortingBtn = navButton("Sorting");
        searchingBtn = navButton("Searching");
        graphBtn = navButton("Graph Algorithms");

        Label learning = smallSidebarHeading("YOUR LEARNING");
        complexityBtn = navButton("Complexity Analysis");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        exitBtn = navButton("Exit");

        sidebar.getChildren().addAll(logo, subtitle, workspace, homeBtn,
                sortingBtn, searchingBtn, graphBtn, learning,
                complexityBtn, spacer, exitBtn);
        root.setLeft(sidebar);

        homeBtn.setOnAction(e -> showDashboard());
        sortingBtn.setOnAction(e -> showSortingChoices());
        searchingBtn.setOnAction(e -> showSearchingPage());

        graphBtn.setOnAction(e -> showGraphPage());
        complexityBtn.setOnAction(e -> showComplexityPage());
        exitBtn.setOnAction(e -> stage.close());

        Scene scene = new Scene(root, 1220, 830);
        stage.setMinWidth(1000);
        stage.setMinHeight(720);
        stage.setScene(scene);
    }

    private Label smallSidebarHeading(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        label.setTextFill(Color.web("#64748B"));
        return label;
    }

    private Button navButton(String text) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPadding(new Insets(13, 14, 13, 14));
        b.setStyle("-fx-background-color: transparent; -fx-text-fill: #AAB5CC;" +
                "-fx-font-size: 13px; -fx-background-radius: 9; -fx-cursor: hand;");
        b.setOnMouseEntered(e -> {
            if (!b.getStyle().contains("#33275B")) {
                b.setStyle("-fx-background-color: #202B46; -fx-text-fill: #C4B5FD;" +
                        "-fx-font-size: 13px; -fx-background-radius: 9; -fx-cursor: hand;");
            }
        });
        b.setOnMouseExited(e -> {
            if (!b.getStyle().contains("#33275B")) {
                b.setStyle("-fx-background-color: transparent; -fx-text-fill: #AAB5CC;" +
                        "-fx-font-size: 13px; -fx-background-radius: 9; -fx-cursor: hand;");
            }
        });
        return b;
    }

    private void setActive(Button active) {
        Button[] all = {homeBtn, sortingBtn, searchingBtn, graphBtn, complexityBtn};
        for (Button b : all) {
            boolean selected = b == active;
            b.setStyle(selected
                    ? "-fx-background-color: #33275B; -fx-text-fill: #C4B5FD;" +
                      "-fx-background-radius: 9; -fx-font-weight: bold; -fx-font-size: 13px;" +
                      "-fx-border-color: #7657E8; -fx-border-width: 0 0 0 3;" +
                      "-fx-border-radius: 9; -fx-cursor: hand;"
                    : "-fx-background-color: transparent; -fx-text-fill: #AAB5CC;" +
                      "-fx-font-size: 13px; -fx-background-radius: 9; -fx-cursor: hand;");
        }
    }

    private VBox pageContent() {
        VBox box = new VBox(24);
        box.setPadding(new Insets(32, 38, 32, 38));
        box.setStyle("-fx-background-color: " + BG + ";");
        return box;
    }

    private ScrollPane scroll(NodeLike node) {
        ScrollPane sp = new ScrollPane(node.box);
        sp.setFitToWidth(true);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setStyle("-fx-background: " + BG + "; -fx-background-color: " + BG + ";" +
                "-fx-border-color: transparent;");
        return sp;
    }

    // Small wrapper keeps page creation and scrolling simple.
    private static class NodeLike {
        VBox box;
        NodeLike(VBox box) { this.box = box; }
    }

    private void showDashboard() {
        stopPlayback();
        setActive(homeBtn);
        pageName = new Label("Overview");
        pageName.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        pageName.setTextFill(Color.web(WHITE));

        VBox main = pageContent();
        Label intro = mutedLabel("Welcome to your algorithm learning workspace.");
        HBox titleRow = new HBox(pageName);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        HBox hero = new HBox(20);
        hero.setAlignment(Pos.CENTER_LEFT);
        hero.setPadding(new Insets(30));
        hero.setStyle("-fx-background-color: linear-gradient(to right, #282052, #192342, #17243C);" +
                "-fx-background-radius: 18; -fx-border-color: #514080; -fx-border-radius: 18;");

        VBox heroText = new VBox(12);
        HBox.setHgrow(heroText, Priority.ALWAYS);
        Label tag = new Label("YOUR ALGORITHM PLAYGROUND");
        tag.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        tag.setTextFill(Color.web(LAVENDER));
        Label heroTitle = new Label("Understand Algorithms.\nSee Every Step.");
        heroTitle.setFont(Font.font("Arial", FontWeight.BOLD, 29));
        heroTitle.setTextFill(Color.web(WHITE));
        Label desc = new Label("Explore algorithms visually, follow their operations, and understand how they reach the final result.");
        desc.setWrapText(true);
        desc.setFont(Font.font("Arial", 13));
        desc.setTextFill(Color.web("#C0C5D8"));
        Button start = primaryButton("Start Learning  →");
        start.setOnAction(e -> showSortingChoices());
        heroText.getChildren().addAll(tag, heroTitle, desc, start);
        Label star = new Label("✧");
        star.setFont(Font.font("Arial", 75));
        star.setTextFill(Color.web(LAVENDER));
        VBox decoration = new VBox(5, star, smallSidebarHeading("VISUAL LEARNING"));
        decoration.setAlignment(Pos.CENTER);
        decoration.setMinWidth(145);
        hero.getChildren().addAll(heroText, decoration);

        HBox stats = new HBox(15,
                createStat("04", "Learning Categories"),
                createStat("08+", "Algorithms Planned"),
                createStat("100%", "Step-by-Step Learning"));

        Label categories = heading("Explore Categories");
        Label categoryIntro = mutedLabel("Choose a category to explore and learn.");

        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(18);
        VBox c1 = createCategory("01", "Sorting Algorithms",
                "Bubble Sort • Selection Sort • Insertion Sort • Merge Sort • Quick Sort",
                "Explore how algorithms arrange data.", () -> showSortingChoices());
        VBox c2 = createCategory("02", "Searching Algorithms",
                "Linear Search • Binary Search",
                "Discover how algorithms find elements.", () -> openSearchingPage());
        VBox c3 = createCategory("03", "Graph Algorithms",
                "BFS • DFS • Dijkstra",
                "Explore nodes, connections and paths.", () -> showGraphPage());
        VBox c4 = createCategory("04", "Complexity Analysis",
                "Time Complexity • Space Complexity",
                "Understand algorithm performance.", () -> showComplexityPage());
        grid.add(c1, 0, 0);
        grid.add(c2, 1, 0);
        grid.add(c3, 0, 1);
        grid.add(c4, 1, 1);

        dashboardSearch = new TextField();
        dashboardSearch.setPromptText("Search algorithm categories...");
        dashboardSearch.setStyle("-fx-background-color: #151E35; -fx-text-fill: white;" +
                "-fx-prompt-text-fill: #718096; -fx-background-radius: 10;" +
                "-fx-border-color: #303B59; -fx-border-radius: 10; -fx-padding: 11 14;");
        dashboardSearch.textProperty().addListener((obs, oldV, newV) -> {
            String q = newV.toLowerCase().trim();
            VBox[] cards = {c1, c2, c3, c4};
            for (VBox card : cards) {
                boolean match = card.getUserData().toString().toLowerCase().contains(q);
                card.setVisible(match);
                card.setManaged(match);
            }
        });

        status = new Label("Welcome! Select a category to begin your journey.");
        status.setTextFill(Color.web(LAVENDER));
        VBox statusBox = new VBox(status);
        statusBox.setPadding(new Insets(14, 18, 14, 18));
        statusBox.setStyle("-fx-background-color: #151E35; -fx-background-radius: 10;" +
                "-fx-border-color: #303B59; -fx-border-radius: 10;");

        main.getChildren().addAll(titleRow, intro, hero, stats, categories,
                categoryIntro, grid, statusBox);
        dashboardScroll = new ScrollPane(main);
        dashboardScroll.setFitToWidth(true);
        dashboardScroll.setStyle("-fx-background: " + BG + "; -fx-background-color: " + BG + ";" +
                "-fx-border-color: transparent;");
        root.setCenter(dashboardScroll);
    }

    private VBox createStat(String value, String caption) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(20));
        box.setPrefWidth(260);
        box.setStyle("-fx-background-color: #151E35; -fx-background-radius: 15;" +
                "-fx-border-color: #303B59; -fx-border-radius: 15;");
        Label number = new Label(value);
        number.setFont(Font.font("Arial", FontWeight.BOLD, 27));
        number.setTextFill(Color.web("#A78BFA"));
        box.getChildren().addAll(number, mutedLabel(caption));
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    private VBox createCategory(String number, String title, String algorithms,
                                String description, Runnable action) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(24));
        card.setPrefWidth(360);
        card.setMinHeight(170);
        card.setStyle("-fx-background-color: #151E35; -fx-background-radius: 15;" +
                "-fx-border-color: #303B59; -fx-border-radius: 15;");
        Label n = new Label(number);
        n.setTextFill(Color.web("#A78BFA"));
        n.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Label t = new Label(title);
        t.setTextFill(Color.web(LAVENDER));
        t.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        Label a = new Label(algorithms);
        a.setTextFill(Color.web(WHITE));
        a.setWrapText(true);
        Label d = mutedLabel(description);
        Label link = new Label("Explore category  →");
        link.setTextFill(Color.web("#A78BFA"));
        link.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        card.getChildren().addAll(n, t, a, d, link);
        card.setUserData((title + " " + algorithms + " " + description).toLowerCase());
        card.setOnMouseClicked(e -> action.run());
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #1B2742;" +
                "-fx-background-radius: 15; -fx-border-color: #7657E8; -fx-border-radius: 15;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #151E35;" +
                "-fx-background-radius: 15; -fx-border-color: #303B59; -fx-border-radius: 15;"));
        return card;
    }

    private void showSortingChoices() {
        stopPlayback();
        setActive(sortingBtn);
        pageName = heading("Sorting Algorithms");
        VBox page = pageContent();
        page.getChildren().addAll(pageName,
                mutedLabel("Choose a sorting technique to visualize its process step by step."));
        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(18);
        String[][] items = {
                {"01", "Bubble Sort", "Repeatedly compares adjacent elements and swaps them when needed."},
                {"02", "Selection Sort", "Selects the smallest remaining element and places it in order."},
                {"03", "Insertion Sort", "Builds a sorted portion by inserting each element into its correct position."},
                {"04", "Merge Sort", "Divides the array, sorts each half, and merges the results."},
                {"05", "Quick Sort", "Partitions values around a pivot and sorts the partitions."}
        };
        for (int i = 0; i < items.length; i++) {
            final String algorithm = items[i][1];
            VBox card = createCategory(items[i][0], items[i][1], items[i][2],
                    "Open interactive visualizer.", () -> openSortingVisualizer(algorithm));
            grid.add(card, i % 2, i / 2);
        }
        page.getChildren().add(grid);
        root.setCenter(scroll(new NodeLike(page)));
        pageName.setText("Sorting Algorithms");
    }

    private void openSortingVisualizer(String algorithm) {
        selectedAlgorithm = algorithm;
        stopPlayback();
        pageName = heading(algorithm);
        VBox page = pageContent();
        Label desc = mutedLabel("Enter numbers, then move through every operation or play the animation.");
        arrayInput = new TextField("8, 3, 10, 1, 6, 4");
        arrayInput.setPromptText("Example: 8, 3, 10, 1, 6, 4");
        arrayInput.setStyle(inputStyle());
        arrayInput.setPrefWidth(450);
        Button start = primaryButton("Generate Steps");
        start.setOnAction(e -> generateSortingSteps());
        HBox inputRow = new HBox(12, arrayInput, start);
        inputRow.setAlignment(Pos.CENTER_LEFT);
        VBox inputPanel = panel(new VBox(12, smallTitle("ENTER YOUR NUMBERS"), inputRow));

        barsBox = new HBox(12);
        barsBox.setAlignment(Pos.BOTTOM_CENTER);
        barsBox.setMinHeight(280);
        barsBox.setPadding(new Insets(20));
        barsBox.setStyle("-fx-background-color: #0B1020; -fx-background-radius: 12;" +
                "-fx-border-color: #303B59; -fx-border-radius: 12;");
        VBox visualPanel = panel(new VBox(12,
                heading("Array Visualization"),
                mutedLabel("Purple = current comparison    Pink = swap / movement"),
                barsBox));

        explanationTitle = heading("Step explanation");
        explanationText = new Label("Generate steps to see a detailed explanation here.");
        explanationText.setWrapText(true);
        explanationText.setTextFill(Color.web(WHITE));
        explanationText.setFont(Font.font("Arial", 14));
        explanationText.setMinHeight(100);
        stepCountLabel = mutedLabel("Step 0 / 0");
        VBox explanationPanel = panel(new VBox(12, explanationTitle, explanationText, stepCountLabel));

        bestValue = complexityValue("Best: —");
        averageValue = complexityValue("Average: —");
        worstValue = complexityValue("Worst: —");
        spaceValue = complexityValue("Space: —");
        VBox complexityPanel = panel(new VBox(9,
                smallTitle("COMPLEXITY"),
                bestValue, averageValue, worstValue, spaceValue));

        comparisonValue = metricValue("0");
        operationValue = metricValue("0");
        HBox metrics = new HBox(12,
                metricCard("COMPARISONS", comparisonValue),
                metricCard("SWAPS / MOVES", operationValue));

        previousButton = secondaryButton("← Previous");
        playButton = primaryButton("▶ Play");
        nextButton = secondaryButton("Next →");
        resetButton = secondaryButton("↻ Reset");
        previousButton.setDisable(true);
        playButton.setDisable(true);
        nextButton.setDisable(true);
        resetButton.setDisable(true);

        speedSlider = new Slider(0.5, 2.5, 1.0);
        speedSlider.setPrefWidth(115);
        speedValue = mutedLabel("1.0×");
        speedSlider.valueProperty().addListener((obs, oldV, newV) -> {
            speedValue.setText(String.format("%.1f×", newV.doubleValue()));
            if (playing) startPlayback();
        });
        HBox speed = new HBox(7, mutedLabel("Speed"), speedSlider, speedValue);
        speed.setAlignment(Pos.CENTER_LEFT);
        HBox controls = new HBox(9, previousButton, playButton, nextButton, speed, resetButton);
        controls.setAlignment(Pos.CENTER_LEFT);

        resultLabel = mutedLabel("Enter your numbers and select Generate Steps.");
        resultLabel.setWrapText(true);
        previousButton.setOnAction(e -> showPreviousStep());
        nextButton.setOnAction(e -> showNextStep());
        playButton.setOnAction(e -> togglePlayback());
        resetButton.setOnAction(e -> resetSorting());

        HBox topVisuals = new HBox(16, visualPanel, explanationPanel);
        HBox.setHgrow(visualPanel, Priority.SOMETIMES);
        HBox.setHgrow(explanationPanel, Priority.ALWAYS);
        visualPanel.setPrefWidth(520);
        explanationPanel.setPrefWidth(390);

        page.getChildren().addAll(pageName, desc, inputPanel, topVisuals,
                complexityPanel, metrics, controls, resultLabel);
        root.setCenter(scroll(new NodeLike(page)));
        pageName.setText(algorithm);
        originalArray = new int[]{8, 3, 10, 1, 6, 4};
        drawBars(originalArray, -1, -1, false);
        updateComplexity(algorithm);
    }

    private void generateSortingSteps() {
        stopPlayback();
        try {
            String[] parts = arrayInput.getText().trim().split(",");
            if (parts.length < 2 || parts.length > 14) {
                throw new IllegalArgumentException("Enter between 2 and 14 integers.");
            }
            int[] values = new int[parts.length];
            for (int i = 0; i < parts.length; i++) values[i] = Integer.parseInt(parts[i].trim());
            originalArray = values.clone();
            sortSteps.clear();
            currentStep = 0;
            buildSteps(values);
            drawBars(originalArray, -1, -1, false);
            previousButton.setDisable(true);
            playButton.setDisable(sortSteps.isEmpty());
            nextButton.setDisable(sortSteps.isEmpty());
            resetButton.setDisable(false);
            comparisonValue.setText("0");
            operationValue.setText("0");
            explanationTitle.setText(selectedAlgorithm + " — Ready");
            explanationText.setText("The input array is " + Arrays.toString(originalArray) +
                    ". Press Next to inspect the first operation, or Play to run the animation.");
            stepCountLabel.setText("Step 0 / " + sortSteps.size());
            resultLabel.setText("Generated " + sortSteps.size() + " visualization steps.");
        } catch (NumberFormatException ex) {
            resultLabel.setText("Invalid input. Enter whole numbers separated by commas.");
        } catch (IllegalArgumentException ex) {
            resultLabel.setText(ex.getMessage());
        }
    }

    private void addStep(int[] a, int i, int j, int comparisons, int operations,
                         String title, String explanation) {
        sortSteps.add(new SortStep(a, i, j, comparisons, operations, title, explanation));
    }

    private void buildSteps(int[] input) {
        int[] a = input.clone();
        int comparisons = 0, operations = 0;
        addStep(a, -1, -1, 0, 0, "Initial array",
                "We begin with " + Arrays.toString(a) + ". The algorithm will now rearrange these values.");
        switch (selectedAlgorithm) {
            case "Bubble Sort" -> {
                for (int pass = 0; pass < a.length - 1; pass++) {
                    boolean changed = false;
                    for (int j = 0; j < a.length - pass - 1; j++) {
                        comparisons++;
                        addStep(a, j, j + 1, comparisons, operations, "Compare adjacent values",
                                "Compare " + a[j] + " and " + a[j + 1] + ". " +
                                (a[j] > a[j + 1] ? "The left value is larger, so these two values must swap." :
                                "They are already in the correct relative order, so no swap is needed."));
                        if (a[j] > a[j + 1]) {
                            int t = a[j]; a[j] = a[j + 1]; a[j + 1] = t;
                            operations++; changed = true;
                            addStep(a, j, j + 1, comparisons, operations, "Swap the values",
                                    "After swapping, the pair becomes " + a[j] + " and " + a[j + 1] +
                                    ". Larger values gradually move toward the right.");
                        }
                    }
                    addStep(a, -1, a.length - pass - 1, comparisons, operations, "Pass completed",
                            "Pass " + (pass + 1) + " is complete. The largest remaining unsorted value is now fixed at the right end.");
                    if (!changed) break;
                }
            }
            case "Selection Sort" -> {
                for (int i = 0; i < a.length - 1; i++) {
                    int min = i;
                    for (int j = i + 1; j < a.length; j++) {
                        comparisons++;
                        addStep(a, min, j, comparisons, operations, "Find the minimum",
                                "Compare current minimum " + a[min] + " with " + a[j] +
                                ". " + (a[j] < a[min] ? a[j] + " becomes the new minimum." : "The current minimum remains " + a[min] + "."));
                        if (a[j] < a[min]) min = j;
                    }
                    if (min != i) {
                        int t = a[i]; a[i] = a[min]; a[min] = t; operations++;
                        addStep(a, i, min, comparisons, operations, "Place the minimum",
                                "Swap the minimum value into position " + i + ". The left portion is now sorted.");
                    } else {
                        addStep(a, i, min, comparisons, operations, "Position already correct",
                                a[i] + " is already the smallest value in the unsorted portion.");
                    }
                }
            }
            case "Insertion Sort" -> {
                for (int i = 1; i < a.length; i++) {
                    int key = a[i], j = i - 1;
                    addStep(a, i, i, comparisons, operations, "Choose the next value",
                            "Take " + key + " as the key. The portion before index " + i + " is already sorted.");
                    while (j >= 0) {
                        comparisons++;
                        addStep(a, j, j + 1, comparisons, operations, "Compare with the key",
                                "Compare " + a[j] + " with key " + key + ". " +
                                (a[j] > key ? "It is larger and must move one position right." : "It is not larger, so the key belongs after it."));
                        if (a[j] > key) {
                            a[j + 1] = a[j]; operations++;
                            addStep(a, j, j + 1, comparisons, operations, "Shift right",
                                    "Move " + a[j] + " one position to the right to make room for the key.");
                            j--;
                        } else break;
                    }
                    a[j + 1] = key; operations++;
                    addStep(a, j + 1, i, comparisons, operations, "Insert the key",
                            "Place " + key + " at index " + (j + 1) + ". The sorted portion grows by one element.");
                }
            }
            case "Merge Sort" -> mergeSort(a, 0, a.length - 1, new int[a.length], new int[]{0, 0});
            case "Quick Sort" -> quickSort(a, 0, a.length - 1, new int[]{0, 0});
        }
        addStep(a, -1, -1, sortSteps.isEmpty() ? 0 : sortSteps.get(sortSteps.size()-1).comparisons,
                sortSteps.isEmpty() ? 0 : sortSteps.get(sortSteps.size()-1).operations,
                "Sorting complete", "The final sorted array is " + Arrays.toString(a) + ".");
    }

    private void mergeSort(int[] a, int left, int right, int[] temp, int[] counts) {
        if (left >= right) return;
        int mid = (left + right) / 2;
        addStep(a, left, right, counts[0], counts[1], "Divide the array",
                "Split the range from index " + left + " to " + right + " into two smaller parts.");
        mergeSort(a, left, mid, temp, counts);
        mergeSort(a, mid + 1, right, temp, counts);
        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            counts[0]++;
            addStep(a, i, j, counts[0], counts[1], "Compare values before merging",
                    "Compare " + a[i] + " and " + a[j] + " and copy the smaller value into the temporary array.");
            if (a[i] <= a[j]) temp[k++] = a[i++];
            else temp[k++] = a[j++];
            counts[1]++;
        }
        while (i <= mid) { temp[k++] = a[i++]; counts[1]++; }
        while (j <= right) { temp[k++] = a[j++]; counts[1]++; }
        for (k = left; k <= right; k++) a[k] = temp[k];
        addStep(a, left, right, counts[0], counts[1], "Merge the sorted portions",
                "The values in this range have been merged in ascending order.");
    }

    private void quickSort(int[] a, int low, int high, int[] counts) {
        if (low >= high) return;
        int pivot = a[high], i = low - 1;
        addStep(a, high, high, counts[0], counts[1], "Choose a pivot",
                "The last value, " + pivot + ", is selected as the pivot for this partition.");
        for (int j = low; j < high; j++) {
            counts[0]++;
            addStep(a, j, high, counts[0], counts[1], "Compare with pivot",
                    "Compare " + a[j] + " with pivot " + pivot + ". " +
                    (a[j] <= pivot ? "This value belongs on the left side." : "This value stays on the right side."));
            if (a[j] <= pivot) {
                i++;
                if (i != j) {
                    int t = a[i]; a[i] = a[j]; a[j] = t; counts[1]++;
                    addStep(a, i, j, counts[0], counts[1], "Move smaller value left",
                            "Place the value no larger than the pivot in the left partition.");
                }
            }
        }
        int p = i + 1;
        if (p != high) {
            int t = a[p]; a[p] = a[high]; a[high] = t; counts[1]++;
        }
        addStep(a, p, high, counts[0], counts[1], "Place the pivot",
                "The pivot is now in its final sorted position at index " + p + ".");
        quickSort(a, low, p - 1, counts);
        quickSort(a, p + 1, high, counts);
    }

    private void showNextStep() {
        if (currentStep >= sortSteps.size()) { stopPlayback(); return; }
        SortStep s = sortSteps.get(currentStep);
        drawBars(s.values, s.first, s.second, s.first >= 0 || s.second >= 0);
        explanationTitle.setText(s.title);
        explanationText.setText(s.explanation);
        comparisonValue.setText(String.valueOf(s.comparisons));
        operationValue.setText(String.valueOf(s.operations));
        currentStep++;
        stepCountLabel.setText("Step " + currentStep + " / " + sortSteps.size());
        previousButton.setDisable(false);
        nextButton.setDisable(currentStep >= sortSteps.size());
        resultLabel.setText(currentStep == sortSteps.size()
                ? "Completed! Result: " + Arrays.toString(s.values)
                : "Follow the highlighted values to understand this operation.");
        if (currentStep >= sortSteps.size()) stopPlayback();
    }

    private void showPreviousStep() {
        stopPlayback();
        if (currentStep <= 0) return;
        currentStep--;
        if (currentStep == 0) {
            drawBars(originalArray, -1, -1, false);
            explanationTitle.setText("Initial array");
            explanationText.setText("We begin with " + Arrays.toString(originalArray) + ".");
            comparisonValue.setText("0");
            operationValue.setText("0");
        } else {
            SortStep s = sortSteps.get(currentStep - 1);
            drawBars(s.values, s.first, s.second, s.first >= 0 || s.second >= 0);
            explanationTitle.setText(s.title);
            explanationText.setText(s.explanation);
            comparisonValue.setText(String.valueOf(s.comparisons));
            operationValue.setText(String.valueOf(s.operations));
        }
        stepCountLabel.setText("Step " + currentStep + " / " + sortSteps.size());
        previousButton.setDisable(currentStep == 0);
        nextButton.setDisable(false);
        playButton.setText("▶ Play");
    }

    private void togglePlayback() {
        if (playing) { stopPlayback(); return; }
        if (sortSteps.isEmpty()) {
            resultLabel.setText("Click Generate Steps first.");
            return;
        }
        if (currentStep >= sortSteps.size()) {
            currentStep = 0;
            drawBars(originalArray, -1, -1, false);
            previousButton.setDisable(true);
            nextButton.setDisable(false);
        }
        startPlayback();
    }

    private void startPlayback() {
        if (playback != null) playback.stop();
        playing = true;
        playButton.setText("Ⅱ Pause");
        playback = new Timeline(new KeyFrame(
                Duration.millis(1000.0 / speedSlider.getValue()), e -> showNextStep()));
        playback.setCycleCount(Timeline.INDEFINITE);
        playback.play();
    }

    private void stopPlayback() {
        if (playback != null) playback.stop();
        playing = false;
        if (playButton != null) playButton.setText("▶ Play");
    }

    private void resetSorting() {
        stopPlayback();
        currentStep = 0;
        drawBars(originalArray, -1, -1, false);
        comparisonValue.setText("0");
        operationValue.setText("0");
        explanationTitle.setText("Initial array");
        explanationText.setText("The array is ready. Press Next or Play to begin.");
        stepCountLabel.setText("Step 0 / " + sortSteps.size());
        previousButton.setDisable(true);
        nextButton.setDisable(sortSteps.isEmpty());
        playButton.setDisable(sortSteps.isEmpty());
        resultLabel.setText("Reset to the beginning.");
    }

    private void drawBars(int[] values, int first, int second, boolean highlight) {
        barsBox.getChildren().clear();
        int max = 1;
        for (int v : values) max = Math.max(max, Math.abs(v));
        for (int i = 0; i < values.length; i++) {
            VBox column = new VBox(7);
            column.setAlignment(Pos.BOTTOM_CENTER);
            Label number = new Label(String.valueOf(values[i]));
            number.setTextFill(Color.web(WHITE));
            number.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            Rectangle bar = new Rectangle(42, 55 + (Math.abs((double) values[i]) / max) * 145);
            String color = highlight && (i == first || i == second)
                    ? "#EC4899" : "#6552B8";
            bar.setArcWidth(9);
            bar.setArcHeight(9);
            bar.setFill(Color.web(color));
            Label index = new Label("Index " + i);
            index.setFont(Font.font("Arial", 10));
            index.setTextFill(Color.web(MUTED));
            column.getChildren().addAll(number, bar, index);
            barsBox.getChildren().add(column);
        }
    }

    private void updateComplexity(String algorithm) {
        switch (algorithm) {
            case "Bubble Sort" -> setComplexity("O(n)", "O(n²)", "O(n²)", "O(1)");
            case "Selection Sort" -> setComplexity("O(n²)", "O(n²)", "O(n²)", "O(1)");
            case "Insertion Sort" -> setComplexity("O(n)", "O(n²)", "O(n²)", "O(1)");
            case "Merge Sort" -> setComplexity("O(n log n)", "O(n log n)", "O(n log n)", "O(n)");
            case "Quick Sort" -> setComplexity("O(n log n)", "O(n log n)", "O(n²)", "O(log n) average; O(n) worst");
        }
    }

    private void setComplexity(String best, String avg, String worst, String space) {
        bestValue.setText("Best time: " + best);
        averageValue.setText("Average time: " + avg);
        worstValue.setText("Worst time: " + worst);
        spaceValue.setText("Space: " + space);
    }

    private void showSearchingPage() {
        stopPlayback();
        setActive(searchingBtn);
        VBox page = pageContent();
        page.getChildren().addAll(heading("Searching Algorithms"),
                mutedLabel("Searching algorithms locate a target value in a collection."));
        TextField data = new TextField("4, 8, 12, 16, 20, 24");
        data.setStyle(inputStyle());
        TextField target = new TextField("16");
        target.setStyle(inputStyle());
        Button linear = primaryButton("Run Linear Search");
        Button binary = secondaryButton("Run Binary Search");
        Label output = mutedLabel("Choose a search method to see its result.");
        output.setWrapText(true);
        linear.setOnAction(e -> {
            try {
                int[] a = parseArray(data.getText());
                int key = Integer.parseInt(target.getText().trim());
                StringBuilder log = new StringBuilder("Linear Search checks each element from left to right.\n");
                int count = 0, found = -1;
                for (int i = 0; i < a.length; i++) {
                    count++;
                    log.append("Step ").append(count).append(": check index ").append(i)
                            .append(" (").append(a[i]).append(").\n");
                    if (a[i] == key) { found = i; break; }
                }
                log.append(found >= 0 ? "Found " + key + " at index " + found : "Target not found.");
                log.append("\nComparisons: ").append(count).append("\nTime: O(n), Space: O(1)");
                output.setText(log.toString());
            } catch (Exception ex) { output.setText("Enter valid comma-separated integers and a target."); }
        });
        binary.setOnAction(e -> {
            try {
                int[] a = parseArray(data.getText());
                Arrays.sort(a);
                int key = Integer.parseInt(target.getText().trim());
                StringBuilder log = new StringBuilder("Binary Search repeatedly halves a sorted array.\nSorted data: " + Arrays.toString(a) + "\n");
                int low = 0, high = a.length - 1, count = 0, found = -1;
                while (low <= high) {
                    int mid = (low + high) / 2; count++;
                    log.append("Step ").append(count).append(": middle index ").append(mid)
                            .append(" contains ").append(a[mid]).append(".\n");
                    if (a[mid] == key) { found = mid; break; }
                    if (a[mid] < key) low = mid + 1; else high = mid - 1;
                }
                log.append(found >= 0 ? "Found " + key + " at sorted index " + found : "Target not found.");
                log.append("\nComparisons: ").append(count).append("\nTime: O(log n), Space: O(1)");
                output.setText(log.toString());
            } catch (Exception ex) { output.setText("Enter valid comma-separated integers and a target."); }
        });
        HBox fields = new HBox(12, data, target);
        HBox actions = new HBox(10, linear, binary);
        page.getChildren().addAll(panel(new VBox(12, smallTitle("INPUT"), fields, actions)),
                panel(new VBox(12, heading("Search trace"), output)));
        root.setCenter(scroll(new NodeLike(page)));
    }
        
    private void showGraphPage() {

        stopPlayback();
        setActive(graphBtn);

        GraphAlgorithmsPage graphPage =
                new GraphAlgorithmsPage();

        ScrollPane scrollPane =
                new ScrollPane(graphPage.getView());

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background: " + BG +
                "; -fx-background-color: " + BG +
                "; -fx-border-color: transparent;"
        );

        root.setCenter(scrollPane);
    }

    private void showComplexityPage() {
        stopPlayback();
        setActive(complexityBtn);

        ComplexityAnalysisPage complexityPage =
                new ComplexityAnalysisPage();

        root.setCenter(complexityPage.getView());
    }

    private Label tableCell(String text, boolean header) {
        Label l = new Label(text);
        l.setMinWidth(115);
        l.setPadding(new Insets(12));
        l.setTextFill(Color.web(header ? LAVENDER : WHITE));
        l.setFont(Font.font("Arial", header ? FontWeight.BOLD : FontWeight.NORMAL, 12));
        l.setStyle("-fx-background-color: " + (header ? "#282052" : "#202B46") +
                "; -fx-background-radius: 6;");
        return l;
    }

    private VBox infoCard(String title, String body, String complexity) {
        Label h = heading(title);
        Label b = mutedLabel(body); b.setWrapText(true);
        Label c = new Label(complexity); c.setTextFill(Color.web(LAVENDER));
        return panel(new VBox(10, h, b, c));
    }

    private int[] parseArray(String input) {
        String[] p = input.trim().split(",");
        if (p.length < 1 || p.length > 30) throw new IllegalArgumentException();
        int[] a = new int[p.length];
        for (int i = 0; i < p.length; i++) a[i] = Integer.parseInt(p[i].trim());
        return a;
    }

    private VBox metricCard(String title, Label value) {
        VBox box = new VBox(7, smallTitle(title), value);
        box.setPadding(new Insets(14));
        box.setMinWidth(145);
        box.setStyle("-fx-background-color: #202746; -fx-background-radius: 10;" +
                "-fx-border-color: #3A315F; -fx-border-radius: 10;");
        return box;
    }

    private Label metricValue(String value) {
        Label l = new Label(value);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        l.setTextFill(Color.web(LAVENDER));
        return l;
    }

    private Label complexityValue(String value) {
        Label l = new Label(value);
        l.setTextFill(Color.web(WHITE));
        l.setFont(Font.font("Arial", 13));
        return l;
    }

    private VBox panel(VBox content) {
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: " + PANEL + "; -fx-background-radius: 14;" +
                "-fx-border-color: #303B59; -fx-border-radius: 14;");
        return content;
    }

    private Button primaryButton(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: " + PURPLE + "; -fx-text-fill: white;" +
                "-fx-font-weight: bold; -fx-background-radius: 9; -fx-padding: 12 18;" +
                "-fx-cursor: hand;");
        return b;
    }

    private Button secondaryButton(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: #293653; -fx-text-fill: white;" +
                "-fx-font-weight: bold; -fx-background-radius: 9; -fx-padding: 12 14;" +
                "-fx-cursor: hand;");
        return b;
    }

    private String inputStyle() {
        return "-fx-background-color: #0B1020; -fx-text-fill: white;" +
                "-fx-prompt-text-fill: #94A3B8; -fx-background-radius: 9;" +
                "-fx-border-color: #514080; -fx-border-radius: 9; -fx-padding: 12;";
    }

    private Label heading(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        l.setTextFill(Color.web(WHITE));
        return l;
    }

    private Label smallTitle(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        l.setTextFill(Color.web(LAVENDER));
        return l;
    }

    private Label mutedLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", 13));
        l.setTextFill(Color.web(MUTED));
        return l;
    }
    private void openSearchingPage() {
    stopPlayback();
    setActive(searchingBtn);
    SearchingAlgorithmsPage searchingPage =
            new SearchingAlgorithmsPage();

    ScrollPane scrollPane = new ScrollPane(searchingPage.getView());
    scrollPane.setFitToWidth(true);
    scrollPane.setStyle(
        "-fx-background: " + BG +
        "; -fx-background-color: " + BG +
        "; -fx-border-color: transparent;"
    );

    root.setCenter(scrollPane);
}

    @Override
    public void stop() {
        stopPlayback();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
