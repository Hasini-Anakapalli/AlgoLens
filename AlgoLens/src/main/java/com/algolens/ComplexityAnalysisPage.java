
package com.algolens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

public class ComplexityAnalysisPage {

    private final BorderPane root = new BorderPane();

    private final Color background = Color.web("#0B1220");
    private final Color cardColor = Color.web("#111C2E");
    private final Color textColor = Color.web("#E5E7EB");
    private final Color mutedColor = Color.web("#94A3B8");
    private final Color accentColor = Color.web("#60A5FA");

    private Canvas graphCanvas;
    private Label inputValueLabel;
    private Label operationLabel;
    private Label selectedComplexityLabel;
    private Label selectedSpaceLabel;
    private Label highlightedCurveLabel;

    private ComboBox<String> algorithmBox;
    private Slider inputSlider;

    private String selectedCurve = "O(n²)";

    private final List<ComplexityCurve> curves = new ArrayList<>();

    public ComplexityAnalysisPage() {
        initializeCurves();
        buildPage();
    }

    public Parent getView() {
        return root;
    }

    // --------------------------------------------------
    // INITIALIZE GRAPH CURVES
    // --------------------------------------------------

    private void initializeCurves() {

        curves.add(new ComplexityCurve("O(1)", Color.web("#22C55E")));
        curves.add(new ComplexityCurve("O(log n)", Color.web("#38BDF8")));
        curves.add(new ComplexityCurve("O(n)", Color.web("#A78BFA")));
        curves.add(new ComplexityCurve("O(n log n)", Color.web("#FACC15")));
        curves.add(new ComplexityCurve("O(n²)", Color.web("#FB923C")));
        curves.add(new ComplexityCurve("O(2ⁿ)", Color.web("#F87171")));
    }

    // --------------------------------------------------
    // MAIN PAGE LAYOUT
    // --------------------------------------------------

    private void buildPage() {

        root.setStyle("-fx-background-color: #0B1220;");

        VBox main = new VBox(20);
        main.setPadding(new Insets(25));
        main.setFillWidth(true);
        main.setMinWidth(0);
        main.setMaxWidth(Double.MAX_VALUE);
        main.setStyle("-fx-background-color: #0B1220;");

        Label heading = new Label("Complexity Analysis");
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 27));
        heading.setTextFill(textColor);

        Label subtitle = new Label(
                "Understand how algorithms perform as input size increases."
        );
        subtitle.setFont(Font.font("Arial", 13));
        subtitle.setTextFill(mutedColor);
        subtitle.setWrapText(true);

        VBox headingBox = new VBox(6, heading, subtitle);

        VBox tableCard = createComplexityTable();

        HBox graphAndSimulator = new HBox(18);
        graphAndSimulator.setAlignment(Pos.TOP_LEFT);
        graphAndSimulator.setFillHeight(false);
        graphAndSimulator.setMinWidth(0);
        graphAndSimulator.setMaxWidth(Double.MAX_VALUE);

        VBox graphCard = createGrowthGraph();
        VBox simulatorCard = createSimulator();

        graphCard.setMinWidth(0);
        graphCard.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(graphCard, Priority.ALWAYS);

        simulatorCard.setMinWidth(240);
        simulatorCard.setPrefWidth(260);
        simulatorCard.setMaxWidth(280);

        HBox.setHgrow(simulatorCard, Priority.NEVER);

        graphAndSimulator.getChildren().addAll(
                graphCard,
                simulatorCard
        );

        VBox explanationCard = createExplanationSection();

        main.getChildren().addAll(
                headingBox,
                tableCard,
                graphAndSimulator,
                explanationCard
        );

        root.setCenter(main);
    }

    // --------------------------------------------------
    // 1. COMPLEXITY COMPARISON TABLE
    // --------------------------------------------------

    private VBox createComplexityTable() {

        VBox card = createCard();

        Label title = createSectionTitle(
                "Algorithm Complexity Comparison"
        );

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(13);
        grid.setPadding(new Insets(15, 5, 5, 5));

        String[] headers = {
                "Algorithm", "Best Case", "Average Case",
                "Worst Case", "Space"
        };

        for (int i = 0; i < headers.length; i++) {

            Label header = new Label(headers[i]);
            header.setTextFill(accentColor);
            header.setFont(Font.font("Arial", FontWeight.BOLD, 12));

            grid.add(header, i, 0);
        }

        String[][] data = {
                {"Bubble Sort", "O(n)", "O(n²)", "O(n²)", "O(1)"},
                {"Selection Sort", "O(n²)", "O(n²)", "O(n²)", "O(1)"},
                {"Insertion Sort", "O(n)", "O(n²)", "O(n²)", "O(1)"},
                {"Merge Sort", "O(n log n)", "O(n log n)", "O(n log n)", "O(n)"},
                {"Quick Sort", "O(n log n)", "O(n log n)", "O(n²)", "O(log n)"},
                {"Linear Search", "O(1)", "O(n)", "O(n)", "O(1)"},
                {"Binary Search", "O(1)", "O(log n)", "O(log n)", "O(1)"}
        };

        for (int row = 0; row < data.length; row++) {

            for (int col = 0; col < data[row].length; col++) {

                Label value = new Label(data[row][col]);
                value.setTextFill(textColor);
                value.setFont(Font.font("Arial", 12));

                grid.add(value, col, row + 1);
            }
        }

        card.getChildren().addAll(title, grid);

        return card;
    }

    // --------------------------------------------------
    // 2. BIG-O GROWTH GRAPH
    // --------------------------------------------------

    private VBox createGrowthGraph() {

        VBox card = createCard();

        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);

        Label title = createSectionTitle("Big-O Growth Graph");

        Label description = new Label(
                "Compare how different time complexities grow. " +
                "The selected algorithm is highlighted."
        );

        description.setTextFill(mutedColor);
        description.setFont(Font.font("Arial", 11));
        description.setWrapText(true);

        graphCanvas = new Canvas(500, 300);

        StackPane graphArea = new StackPane(graphCanvas);
        graphArea.setMinWidth(0);
        graphArea.setPrefWidth(500);
        graphArea.setPrefHeight(300);
        graphArea.setMaxWidth(Double.MAX_VALUE);

        VBox.setVgrow(graphArea, Priority.NEVER);

        graphCanvas.widthProperty().bind(graphArea.widthProperty());

        graphCanvas.widthProperty().addListener(
                (obs, oldValue, newValue) -> drawGraph()
        );

        graphCanvas.heightProperty().addListener(
                (obs, oldValue, newValue) -> drawGraph()
        );

        highlightedCurveLabel = new Label(
                "Highlighted curve: O(n²)"
        );

        highlightedCurveLabel.setTextFill(Color.web("#FB923C"));
        highlightedCurveLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 12)
        );

        VBox legend = createLegend();

        card.getChildren().addAll(
                title,
                description,
                graphArea,
                highlightedCurveLabel,
                legend
        );

        drawGraph();

        return card;
    }

    // --------------------------------------------------
    // DRAW GRAPH WITH DYNAMIC HIGHLIGHTING
    // --------------------------------------------------

    private void drawGraph() {

        if (graphCanvas == null) {
            return;
        }

        double width = graphCanvas.getWidth();
        double height = graphCanvas.getHeight();

        if (width <= 0 || height <= 0) {
            return;
        }

        GraphicsContext gc = graphCanvas.getGraphicsContext2D();

        gc.setFill(cardColor);
        gc.fillRect(0, 0, width, height);

        double left = 55;
        double right = 20;
        double top = 20;
        double bottom = 40;

        double graphWidth = width - left - right;
        double graphHeight = height - top - bottom;

        if (graphWidth <= 0 || graphHeight <= 0) {
            return;
        }

        // GRID

        gc.setStroke(Color.web("#334155"));
        gc.setLineWidth(1);

        for (int i = 0; i <= 5; i++) {

            double y = top + i * graphHeight / 5;

            gc.strokeLine(left, y, width - right, y);
        }

        for (int i = 0; i <= 5; i++) {

            double x = left + i * graphWidth / 5;

            gc.strokeLine(x, top, x, height - bottom);
        }

        // AXIS LABELS

        gc.setFill(mutedColor);
        gc.setFont(Font.font("Arial", 10));

        gc.fillText("Growth (log scale)", 5, 13);
        gc.fillText(
                "Input size (n)",
                width / 2 - 30,
                height - 8
        );

        for (int i = 0; i <= 5; i++) {

            double value = 10 - i * 2;
            double y = top + i * graphHeight / 5;

            gc.fillText(
                    String.valueOf((int) value),
                    25,
                    y + 4
            );
        }

        for (int i = 0; i <= 5; i++) {

            double value = 1 + i * 5;
            double x = left + i * graphWidth / 5;

            gc.fillText(
                    String.valueOf((int) value),
                    x - 5,
                    height - bottom + 18
            );
        }

        // DRAW UNSELECTED CURVES FIRST

        for (ComplexityCurve curve : curves) {

            if (!curve.name.equals(selectedCurve)) {
                drawCurve(
                        gc,
                        curve,
                        left,
                        top,
                        graphWidth,
                        graphHeight,
                        0.30,
                        2.0
                );
            }
        }

        // DRAW SELECTED CURVE LAST AND BRIGHTER

        for (ComplexityCurve curve : curves) {

            if (curve.name.equals(selectedCurve)) {

                drawCurve(
                        gc,
                        curve,
                        left,
                        top,
                        graphWidth,
                        graphHeight,
                        1.0,
                        4.0
                );
            }
        }

        // AXIS LINES

        gc.setStroke(mutedColor);
        gc.setLineWidth(1.4);

        gc.strokeLine(
                left,
                top,
                left,
                height - bottom
        );

        gc.strokeLine(
                left,
                height - bottom,
                width - right,
                height - bottom
        );
    }

    // --------------------------------------------------
    // DRAW INDIVIDUAL COMPLEXITY CURVE
    // --------------------------------------------------

    private void drawCurve(
            GraphicsContext gc,
            ComplexityCurve curve,
            double left,
            double top,
            double graphWidth,
            double graphHeight,
            double opacity,
            double lineWidth
    ) {

        gc.setStroke(curve.color.deriveColor(
                0, 1, 1, opacity
        ));

        gc.setLineWidth(lineWidth);

        double previousX = -1;
        double previousY = -1;

        for (int n = 1; n <= 26; n++) {

            double logValue = getLogGrowth(curve.name, n);

            logValue = Math.min(logValue, 10);

            double x = left + ((n - 1) / 25.0) * graphWidth;

            double y = top + graphHeight
                    - (logValue / 10.0) * graphHeight;

            if (previousX >= 0) {

                gc.strokeLine(
                        previousX,
                        previousY,
                        x,
                        y
                );
            }

            previousX = x;
            previousY = y;
        }
    }

    // --------------------------------------------------
    // COMPLEXITY GRAPH VALUES
    // --------------------------------------------------

    private double getLogGrowth(String complexity, int n) {

        switch (complexity) {

            case "O(1)":
                return 0;

            case "O(log n)":
                return Math.log10(
                        Math.max(1, Math.log(n) / Math.log(2))
                );

            case "O(n)":
                return Math.log10(n);

            case "O(n log n)":
                return Math.log10(
                        n * (Math.log(n) / Math.log(2))
                );

            case "O(n²)":
                return 2 * Math.log10(n);

            case "O(2ⁿ)":
                return n * Math.log10(2);

            default:
                return 0;
        }
    }

    // --------------------------------------------------
    // COLOUR-CODED GRAPH LEGEND
    // --------------------------------------------------

    private VBox createLegend() {

        VBox legend = new VBox(8);

        HBox firstRow = new HBox(15);
        HBox secondRow = new HBox(15);
        HBox thirdRow = new HBox(15);

        firstRow.setAlignment(Pos.CENTER_LEFT);
        secondRow.setAlignment(Pos.CENTER_LEFT);
        thirdRow.setAlignment(Pos.CENTER_LEFT);

        for (int i = 0; i < curves.size(); i++) {

            ComplexityCurve curve = curves.get(i);

            HBox item = new HBox(6);
            item.setAlignment(Pos.CENTER_LEFT);

            Label dot = new Label("●");
            dot.setTextFill(curve.color);

            Label name = new Label(curve.name);
            name.setTextFill(textColor);
            name.setFont(Font.font("Arial", 11));

            item.getChildren().addAll(dot, name);

            if (i < 2) {
                firstRow.getChildren().add(item);
            } else if (i < 4) {
                secondRow.getChildren().add(item);
            } else {
                thirdRow.getChildren().add(item);
            }
        }

        legend.getChildren().addAll(
                firstRow,
                secondRow,
                thirdRow
        );

        return legend;
    }

    // --------------------------------------------------
    // 3. INPUT SIZE SIMULATOR
    // --------------------------------------------------

    private VBox createSimulator() {

        VBox card = createCard();

        card.setMinWidth(240);
        card.setPrefWidth(260);
        card.setMaxWidth(280);

        Label title = createSectionTitle(
                "Input Size Simulator"
        );

        Label description = new Label(
                "Select an algorithm and change the input size."
        );

        description.setWrapText(true);
        description.setTextFill(mutedColor);
        description.setFont(Font.font("Arial", 11));

        algorithmBox = new ComboBox<>();

        algorithmBox.getItems().addAll(
                "Bubble Sort",
                "Selection Sort",
                "Insertion Sort",
                "Merge Sort",
                "Quick Sort",
                "Linear Search",
                "Binary Search"
        );

        algorithmBox.setValue("Bubble Sort");
        algorithmBox.setMaxWidth(Double.MAX_VALUE);

        styleComboBox(algorithmBox);

        Label inputHeading = new Label("Input size (n)");
        inputHeading.setTextFill(textColor);

        inputSlider = new Slider(10, 10000, 100);

        inputSlider.setShowTickLabels(true);
        inputSlider.setShowTickMarks(true);
        inputSlider.setMajorTickUnit(2497.5);
        inputSlider.setBlockIncrement(10);
        inputSlider.setMaxWidth(Double.MAX_VALUE);

        inputValueLabel = new Label("100 elements");

        inputValueLabel.setTextFill(accentColor);
        inputValueLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 17)
        );

        Label complexityHeading = new Label(
                "Time Complexity (Worst Case)"
        );

        complexityHeading.setTextFill(mutedColor);
        complexityHeading.setWrapText(true);

        selectedComplexityLabel = new Label("O(n²)");

        selectedComplexityLabel.setTextFill(
                Color.web("#FB923C")
        );

        selectedComplexityLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 23)
        );

        Label spaceHeading = new Label(
                "Auxiliary Space Complexity"
        );

        spaceHeading.setTextFill(mutedColor);
        spaceHeading.setWrapText(true);

        selectedSpaceLabel = new Label("O(1)");

        selectedSpaceLabel.setTextFill(
                Color.web("#22C55E")
        );

        selectedSpaceLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 19)
        );

        Label operationsHeading = new Label(
                "Estimated operations"
        );

        operationsHeading.setTextFill(mutedColor);

        operationLabel = new Label("10,000");

        operationLabel.setTextFill(textColor);
        operationLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 19)
        );

        Label note = new Label(
                "Estimates use simplified worst-case theoretical " +
                "growth, not actual execution measurements."
        );

        note.setWrapText(true);
        note.setTextFill(mutedColor);
        note.setFont(Font.font("Arial", 10));

        algorithmBox.setOnAction(e -> updateSimulator());

        inputSlider.valueProperty().addListener(
                (obs, oldValue, newValue) -> updateSimulator()
        );

        card.getChildren().addAll(
                title,
                description,
                algorithmBox,
                inputHeading,
                inputSlider,
                inputValueLabel,
                complexityHeading,
                selectedComplexityLabel,
                spaceHeading,
                selectedSpaceLabel,
                operationsHeading,
                operationLabel,
                note
        );

        updateSimulator();

        return card;
    }

    // --------------------------------------------------
    // UPDATE SIMULATOR AND GRAPH TOGETHER
    // --------------------------------------------------

    private void updateSimulator() {

        if (algorithmBox == null || inputSlider == null) {
            return;
        }

        String algorithm = algorithmBox.getValue();

        int n = (int) Math.round(inputSlider.getValue());

        inputValueLabel.setText(n + " elements");

        String complexity = getAlgorithmComplexity(algorithm);

        String space = getAlgorithmSpace(algorithm);

        selectedComplexityLabel.setText(complexity);
        selectedSpaceLabel.setText(space);

        selectedComplexityLabel.setTextFill(
                getCurveColor(complexity)
        );

        selectedCurve = complexity;

        highlightedCurveLabel.setText(
                "Highlighted curve: " + complexity
        );

        highlightedCurveLabel.setTextFill(
                getCurveColor(complexity)
        );

        operationLabel.setText(
                getEstimatedOperations(complexity, n)
        );

        drawGraph();
    }

    // --------------------------------------------------
    // ALGORITHM TIME COMPLEXITY
    // WORST-CASE VALUES
    // --------------------------------------------------

    private String getAlgorithmComplexity(String algorithm) {

        switch (algorithm) {

            case "Bubble Sort":
            case "Selection Sort":
            case "Insertion Sort":
            case "Quick Sort":
                return "O(n²)";

            case "Merge Sort":
                return "O(n log n)";

            case "Linear Search":
                return "O(n)";

            case "Binary Search":
                return "O(log n)";

            default:
                return "Unknown";
        }
    }

    // --------------------------------------------------
    // ALGORITHM SPACE COMPLEXITY
    // --------------------------------------------------

    private String getAlgorithmSpace(String algorithm) {

        switch (algorithm) {

            case "Bubble Sort":
            case "Selection Sort":
            case "Insertion Sort":
            case "Linear Search":
            case "Binary Search":
                return "O(1)";

            case "Merge Sort":
                return "O(n)";

            case "Quick Sort":
                return "O(n)";

            default:
                return "Unknown";
        }
    }

    // --------------------------------------------------
    // GET CURVE COLOUR
    // --------------------------------------------------

    private Color getCurveColor(String complexity) {

        for (ComplexityCurve curve : curves) {

            if (curve.name.equals(complexity)) {
                return curve.color;
            }
        }

        return mutedColor;
    }

    // --------------------------------------------------
    // ESTIMATED OPERATIONS
    // --------------------------------------------------

    private String getEstimatedOperations(
            String complexity,
            int n
    ) {

        double result;

        switch (complexity) {

            case "O(1)":
                result = 1;
                break;

            case "O(log n)":
                result = Math.ceil(
                        Math.log(n) / Math.log(2)
                );
                break;

            case "O(n)":
                result = n;
                break;

            case "O(n log n)":
                result = Math.ceil(
                        n * (Math.log(n) / Math.log(2))
                );
                break;

            case "O(n²)":
                result = (double) n * n;
                break;

            default:
                return "Not available";
        }

        return String.format("%,.0f", result);
    }

    // --------------------------------------------------
    // 4. HOW TO READ BIG-O
    // --------------------------------------------------

    private VBox createExplanationSection() {

        VBox card = createCard();

        Label title = createSectionTitle(
                "How to Read Big-O Notation"
        );

        GridPane grid = new GridPane();

        grid.setHgap(20);
        grid.setVgap(15);

        addExplanation(
                grid, 0, 0,
                "O(1) — Constant",
                "The number of operations stays nearly the same, " +
                "regardless of input size.",
                "#22C55E"
        );

        addExplanation(
                grid, 1, 0,
                "O(log n) — Logarithmic",
                "The number of operations grows slowly. " +
                "Example: Binary Search.",
                "#38BDF8"
        );

        addExplanation(
                grid, 0, 1,
                "O(n) — Linear",
                "Operations increase proportionally with input size. " +
                "Example: Linear Search.",
                "#A78BFA"
        );

        addExplanation(
                grid, 1, 1,
                "O(n log n) — Linearithmic",
                "Efficient for many sorting algorithms. " +
                "Example: Merge Sort.",
                "#FACC15"
        );

        addExplanation(
                grid, 0, 2,
                "O(n²) — Quadratic",
                "Operations grow approximately with the square " +
                "of the input size. Example: Bubble Sort.",
                "#FB923C"
        );

        addExplanation(
                grid, 1, 2,
                "O(2ⁿ) — Exponential",
                "Operations can double with each additional " +
                "input element. It becomes expensive very quickly.",
                "#F87171"
        );

        card.getChildren().addAll(title, grid);

        return card;
    }

    private void addExplanation(
            GridPane grid,
            int col,
            int row,
            String heading,
            String description,
            String color
    ) {

        VBox item = new VBox(6);

        Label headingLabel = new Label(heading);

        headingLabel.setTextFill(Color.web(color));
        headingLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 13)
        );

        Label descriptionLabel = new Label(description);

        descriptionLabel.setTextFill(mutedColor);
        descriptionLabel.setFont(Font.font("Arial", 11));
        descriptionLabel.setWrapText(true);

        item.getChildren().addAll(
                headingLabel,
                descriptionLabel
        );

        item.setPadding(new Insets(10));
        item.setMinWidth(0);
        item.setMaxWidth(Double.MAX_VALUE);

        grid.add(item, col, row);
    }

    // --------------------------------------------------
    // COMMON UI HELPERS
    // --------------------------------------------------

    private VBox createCard() {

        VBox card = new VBox(14);

        card.setPadding(new Insets(20));
        card.setMinWidth(0);

        card.setBackground(
                javafx.scene.layout.Background.fill(cardColor)
        );

        card.setStyle(
                "-fx-background-radius: 12;" +
                "-fx-border-color: #263449;" +
                "-fx-border-radius: 12;"
        );

        return card;
    }

    private Label createSectionTitle(String text) {

        Label title = new Label(text);

        title.setTextFill(textColor);

        title.setFont(
                Font.font("Arial", FontWeight.BOLD, 17)
        );

        return title;
    }

    private void styleComboBox(ComboBox<String> combo) {

        combo.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;"
        );
    }

    // --------------------------------------------------
    // COMPLEXITY CURVE MODEL
    // --------------------------------------------------

    private static class ComplexityCurve {

        String name;
        Color color;

        ComplexityCurve(String name, Color color) {

            this.name = name;
            this.color = color;
        }
    }
}
