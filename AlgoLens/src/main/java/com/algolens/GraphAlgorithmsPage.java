
package com.algolens;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.*;

public class GraphAlgorithmsPage {

    private final BorderPane root = new BorderPane();

    private final String BG = "#0B1120";
    private final String CARD = "#151F32";
    private final String WHITE = "#F1F5F9";
    private final String MUTED = "#94A3B8";
    private final String BLUE = "#60A5FA";
    private final String GREEN = "#34D399";
    private final String PURPLE = "#A78BFA";
    private final String ORANGE = "#FBBF24";

    private TextField edgesField;
    private TextField startField;
    private ComboBox<String> algorithmBox;

    private Pane graphPane;

    private Label explanation;
    private Label resultLabel;
    private Label statusLabel;
    private Label stepLabel;
    private Label distanceTableTitle;

    private VBox distanceTableBox;

    private Button previous;
    private Button play;
    private Button next;
    private Button reset;

    private Timeline timeline;

    private Graph graph;

    private List<GraphStep> steps = new ArrayList<>();

    private int index = 0;

    private final Map<String, double[]> positions = new HashMap<>();

    public GraphAlgorithmsPage() {
        buildUI();
        loadSample();
    }

    public Parent getView() {
        return root;
    }

    // ==================================================
    // MAIN USER INTERFACE
    // ==================================================

    private void buildUI() {

        root.setStyle(
                "-fx-background-color: " + BG + ";"
        );

        // HEADER

        Label heading = new Label(
                "Graph Algorithms"
        );

        heading.setTextFill(Color.web(WHITE));
        heading.setFont(
                Font.font("Arial", FontWeight.BOLD, 26)
        );

        Label subtitle = new Label(
                "Understand how graph algorithms work, one step at a time."
        );

        subtitle.setTextFill(Color.web(MUTED));
        subtitle.setFont(Font.font("Arial", 13));

        VBox header = new VBox(
                6,
                heading,
                subtitle
        );

        header.setPadding(
                new Insets(22, 24, 16, 24)
        );

        root.setTop(header);

        // INPUT CARD

        root.setCenter(createMainContent());

        // PLAYBACK

        root.setBottom(createPlaybackControls());
    }

    // ==================================================
    // MAIN CONTENT
    // ==================================================

    private ScrollPane createMainContent() {

        VBox main = new VBox(16);

        main.setPadding(
                new Insets(0, 24, 20, 24)
        );

        main.setFillWidth(true);

        main.getChildren().addAll(
                createInputCard(),
                createVisualizationSection(),
                createDistanceSection()
        );

        ScrollPane scroll = new ScrollPane(main);

        scroll.setFitToWidth(true);

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scroll.setStyle(
                "-fx-background: " + BG + ";"
                        + "-fx-background-color: " + BG + ";"
                        + "-fx-border-color: transparent;"
        );

        return scroll;
    }

    // ==================================================
    // INPUT CARD
    // ==================================================

    private VBox createInputCard() {

        Label title = makeLabel("GRAPH CONFIGURATION");

        edgesField = new TextField();

        edgesField.setPromptText(
                "Example: A-B-4, A-C-2, C-D-1"
        );

        styleInput(edgesField);

        edgesField.setPrefWidth(350);

        startField = new TextField("A");

        styleInput(startField);

        startField.setPrefWidth(90);

        algorithmBox = new ComboBox<>();

        algorithmBox.getItems().addAll(
                "BFS",
                "DFS",
                "Dijkstra",
                "Kruskal's",
                "Prim's"
        );

        algorithmBox.setValue("Dijkstra");

        algorithmBox.setPrefWidth(145);

        Button runButton = makeButton(
                "Run Algorithm",
                BLUE
        );

        runButton.setOnAction(e -> runAlgorithm());

        Button sampleButton = makeButton(
                "Load Sample",
                PURPLE
        );

        sampleButton.setOnAction(e -> loadSample());

        VBox edgesBox = new VBox(
                7,
                makeLabel("Graph Edges"),
                edgesField
        );

        HBox.setHgrow(edgesBox, Priority.ALWAYS);

        VBox startBox = new VBox(
                7,
                makeLabel("Starting Vertex"),
                startField
        );

        VBox algorithmBoxContainer = new VBox(
                7,
                makeLabel("Algorithm"),
                algorithmBox
        );

        VBox actionBox = new VBox(
                7,
                makeLabel("Actions"),
                new HBox(8, runButton, sampleButton)
        );

        HBox fields = new HBox(
                14,
                edgesBox,
                startBox,
                algorithmBoxContainer,
                actionBox
        );

        fields.setAlignment(Pos.BOTTOM_LEFT);

        VBox card = new VBox(
                14,
                title,
                fields
        );

        styleCard(card);

        return card;
    }

    // ==================================================
    // GRAPH + EXPLANATION SECTION
    // ==================================================

    private HBox createVisualizationSection() {

        // GRAPH PANEL

        Label graphTitle = makeLabel(
                "GRAPH VISUALIZATION"
        );

        graphPane = new Pane();

        graphPane.setMinHeight(420);
        graphPane.setPrefHeight(420);

        graphPane.setStyle(
                "-fx-background-color: " + BG + ";"
        );

        VBox graphCard = new VBox(
                12,
                graphTitle,
                graphPane,
                createLegend()
        );

        styleCard(graphCard);

        HBox.setHgrow(
                graphCard,
                Priority.ALWAYS
        );

        graphCard.setPrefWidth(650);
        graphCard.setMinWidth(400);

        // EXPLANATION PANEL

        Label explanationTitle = makeLabel(
                "CURRENT STEP EXPLANATION"
        );

        explanation = new Label(
                "Select an algorithm and click Run Algorithm."
        );

        explanation.setTextFill(Color.web(WHITE));

        explanation.setFont(
                Font.font("Arial", 14)
        );

        explanation.setWrapText(true);

        explanation.setMaxWidth(Double.MAX_VALUE);

        explanation.setMinHeight(230);

        explanation.setAlignment(Pos.TOP_LEFT);

        resultLabel = new Label("");

        resultLabel.setTextFill(
                Color.web(GREEN)
        );

        resultLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 13)
        );

        resultLabel.setWrapText(true);

        resultLabel.setMaxWidth(Double.MAX_VALUE);

        statusLabel = makeLabel("Ready");

        VBox explanationContent = new VBox(
                18,
                explanationTitle,
                new Separator(),
                explanation,
                new Separator(),
                makeLabel("FINAL RESULT"),
                resultLabel,
                new Separator(),
                statusLabel
        );

        explanationContent.setPadding(
                new Insets(8)
        );

        ScrollPane explanationScroll = new ScrollPane(
                explanationContent
        );

        explanationScroll.setFitToWidth(true);

        explanationScroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        explanationScroll.setStyle(
                "-fx-background: " + CARD + ";"
                        + "-fx-background-color: " + CARD + ";"
                        + "-fx-border-color: transparent;"
        );

        VBox explanationCard = new VBox(
                explanationScroll
        );

        styleCard(explanationCard);

        explanationCard.setPrefWidth(350);
        explanationCard.setMinWidth(280);

        VBox.setVgrow(
                explanationScroll,
                Priority.ALWAYS
        );

        HBox section = new HBox(
                16,
                graphCard,
                explanationCard
        );

        section.setAlignment(Pos.TOP_LEFT);

        return section;
    }

    // ==================================================
    // GRAPH LEGEND
    // ==================================================

    private HBox createLegend() {

        HBox legend = new HBox(
                18,
                makeLegend(ORANGE, "Current edge"),
                makeLegend("#B7791F", "Explored edge"),
                makeLegend(GREEN, "Shortest path"),
                makeLegend(PURPLE, "Current vertex"),
                makeLegend(BLUE, "Unvisited")
        );

        legend.setAlignment(Pos.CENTER);
        legend.setPadding(new Insets(5));

        return legend;
    }

    private HBox makeLegend(
            String color,
            String text
    ) {

        Circle dot = new Circle(5);

        dot.setFill(Color.web(color));

        Label label = new Label(text);

        label.setTextFill(Color.web(MUTED));

        label.setFont(
                Font.font("Arial", 10)
        );

        return new HBox(
                6,
                dot,
                label
        );
    }

    // ==================================================
    // DISTANCE TABLE SECTION
    // ==================================================

    private VBox createDistanceSection() {

        distanceTableTitle = makeLabel(
                "LIVE DISTANCE TABLE"
        );

        distanceTableBox = new VBox(8);

        VBox card = new VBox(
                14,
                distanceTableTitle,
                distanceTableBox
        );

        styleCard(card);

        return card;
    }

    // ==================================================
    // PLAYBACK CONTROLS
    // ==================================================

    private HBox createPlaybackControls() {

        previous = makeButton(
                "Previous",
                CARD
        );

        play = makeButton(
                "Play",
                GREEN
        );

        next = makeButton(
                "Next",
                CARD
        );

        reset = makeButton(
                "Reset",
                CARD
        );

        stepLabel = makeLabel("Step 0 / 0");

        previous.setOnAction(e -> showPrevious());

        next.setOnAction(e -> showNext());

        play.setOnAction(e -> togglePlay());

        reset.setOnAction(e -> resetPlayback());

        HBox controls = new HBox(
                12,
                previous,
                play,
                next,
                reset,
                stepLabel
        );

        controls.setAlignment(Pos.CENTER);

        controls.setPadding(
                new Insets(14)
        );

        controls.setStyle(
                "-fx-background-color: " + BG + ";"
        );

        return controls;
    }

    // ==================================================
    // STYLING HELPERS
    // ==================================================

    private Label makeLabel(String text) {

        Label label = new Label(text);

        label.setTextFill(Color.web(WHITE));

        label.setFont(
                Font.font("Arial", FontWeight.BOLD, 12)
        );

        return label;
    }

    private Button makeButton(
            String text,
            String color
    ) {

        Button button = new Button(text);

        button.setStyle(
                "-fx-background-color: " + color + ";"
                        + "-fx-text-fill: white;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 10 15 10 15;"
                        + "-fx-background-radius: 7;"
                        + "-fx-cursor: hand;"
        );

        return button;
    }

    private void styleInput(TextField field) {

        field.setStyle(
                "-fx-background-color: #0B1120;"
                        + "-fx-text-fill: white;"
                        + "-fx-prompt-text-fill: #64748B;"
                        + "-fx-border-color: #334155;"
                        + "-fx-border-radius: 6;"
                        + "-fx-background-radius: 6;"
                        + "-fx-padding: 10;"
        );
    }

    private void styleCard(VBox card) {

        card.setPadding(
                new Insets(18)
        );

        card.setStyle(
                "-fx-background-color: " + CARD + ";"
                        + "-fx-background-radius: 12;"
        );
    }

    // ==================================================
    // LOAD SAMPLE GRAPH
    // ==================================================

    private void loadSample() {

        edgesField.setText(
                "A-B-4, A-C-2, B-C-1, B-D-5, C-D-1, D-E-3, C-E-7"
        );

        startField.setText("A");

        algorithmBox.setValue("Dijkstra");

        stopPlayback();

        try {

            graph = Graph.parse(
                    edgesField.getText()
            );

            calculatePositions();

            steps.clear();

            index = 0;

            draw(null);

            explanation.setText(
                    "Your sample graph is ready.\n\n"
                            + "Choose an algorithm and click "
                            + "Run Algorithm to begin.\n\n"
                            + "Use the playback controls to move "
                            + "through each step."
            );

            resultLabel.setText("");

            distanceTableBox.getChildren().clear();

            statusLabel.setText("Graph ready");

            stepLabel.setText("Step 0 / 0");

            previous.setDisable(true);

            next.setDisable(true);

        } catch (Exception ex) {

            showError(ex.getMessage());
        }
    }

    // ==================================================
    // CALCULATE NODE POSITIONS
    // ==================================================

    private void calculatePositions() {

        positions.clear();

        List<String> nodes = new ArrayList<>(
                graph.getNodes()
        );

        int count = nodes.size();

        double centerX = 260;
        double centerY = 210;

        double radius = Math.min(
                160,
                Math.max(90, count * 25)
        );

        for (int i = 0; i < count; i++) {

            double angle =
                    2 * Math.PI * i / count
                            - Math.PI / 2;

            double x =
                    centerX + radius * Math.cos(angle);

            double y =
                    centerY + radius * Math.sin(angle);

            positions.put(
                    nodes.get(i),
                    new double[]{x, y}
            );
        }
    }

    // ==================================================
    // RUN SELECTED ALGORITHM
    // ==================================================

    private void runAlgorithm() {

        stopPlayback();

        try {

            graph = Graph.parse(
                    edgesField.getText()
            );

            calculatePositions();

            String selected = algorithmBox.getValue();

            String start = startField.getText().trim();

            if (selected == null) {

                throw new IllegalArgumentException(
                        "Please select an algorithm."
                );
            }

            switch (selected) {

                case "BFS" ->
                        steps = BFS.run(graph, start);

                case "DFS" ->
                        steps = DFS.run(graph, start);

                case "Dijkstra" ->
                        steps = Dijkstra.run(graph, start);

                case "Kruskal's" ->
                        steps = Kruskal.run(graph);

                case "Prim's" ->
                        steps = Prims.run(graph, start);

                default ->
                        throw new IllegalArgumentException(
                                "Invalid algorithm selected."
                        );
            }

            if (steps == null || steps.isEmpty()) {

                throw new IllegalStateException(
                        "No algorithm steps were generated."
                );
            }

            index = 0;

            showStep();

            statusLabel.setText(
                    selected + " started"
            );

        } catch (Exception ex) {

            showError(ex.getMessage());
        }
    }

    // ==================================================
    // ERROR HANDLING
    // ==================================================

    private void showError(String message) {

        Alert alert = new Alert(
                Alert.AlertType.ERROR
        );

        alert.setTitle("AlgoLens Error");

        alert.setHeaderText(
                "Something went wrong"
        );

        alert.setContentText(
                message == null
                        ? "Please check your input."
                        : message
        );

        alert.showAndWait();
    }

    // ==================================================
    // DISPLAY CURRENT STEP
    // ==================================================

    private void showStep() {

        if (steps.isEmpty()) {
            return;
        }

        GraphStep step = steps.get(index);

        explanation.setText(
                step.explanation
        );

        resultLabel.setText(
                step.result == null
                        ? ""
                        : step.result
        );

        stepLabel.setText(
                "Step " + (index + 1)
                        + " / " + steps.size()
        );

        statusLabel.setText(
                index == steps.size() - 1
                        ? "Algorithm completed"
                        : "Algorithm running"
        );

        updateDistanceTable(step);

        draw(step);

        previous.setDisable(index == 0);

        next.setDisable(
                index == steps.size() - 1
        );
    }

    // ==================================================
    // UPDATE DISTANCE TABLE
    // ==================================================

    private void updateDistanceTable(GraphStep step) {

        distanceTableBox.getChildren().clear();

        boolean isDijkstra =
                "Dijkstra".equals(
                        algorithmBox.getValue()
                );

        distanceTableTitle.setVisible(isDijkstra);
        distanceTableTitle.setManaged(isDijkstra);

        distanceTableBox.setVisible(isDijkstra);
        distanceTableBox.setManaged(isDijkstra);

        if (!isDijkstra || graph == null || step == null) {
            return;
        }

        HBox header = new HBox(15);

        Label vertexHeader = makeLabel("VERTEX");

        Label distanceHeader = makeLabel("DISTANCE");

        Label previousHeader = makeLabel("PREVIOUS");

        vertexHeader.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(
                vertexHeader,
                Priority.ALWAYS
        );

        header.getChildren().addAll(
                vertexHeader,
                distanceHeader,
                previousHeader
        );

        distanceTableBox.getChildren().add(header);

        for (String node : graph.getNodes()) {

            HBox row = new HBox(15);

            Label vertex = new Label(node);

            int distance = step.distances.getOrDefault(
                    node,
                    Integer.MAX_VALUE
            );

            String distanceText =
                    distance == Integer.MAX_VALUE
                            ? "∞"
                            : String.valueOf(distance);

            String previousNode =
                    step.previousNodes.getOrDefault(
                            node,
                            "—"
                    );

            Label distanceLabel = new Label(
                    distanceText
            );

            Label previousLabel = new Label(
                    previousNode
            );

            vertex.setTextFill(Color.web(WHITE));

            distanceLabel.setTextFill(
                    distance == Integer.MAX_VALUE
                            ? Color.web(MUTED)
                            : Color.web(GREEN)
            );

            previousLabel.setTextFill(
                    Color.web(PURPLE)
            );

            vertex.setFont(
                    Font.font("Arial", FontWeight.BOLD, 13)
            );

            distanceLabel.setFont(
                    Font.font("Arial", FontWeight.BOLD, 13)
            );

            previousLabel.setFont(
                    Font.font("Arial", 13)
            );

            vertex.setMaxWidth(Double.MAX_VALUE);

            HBox.setHgrow(
                    vertex,
                    Priority.ALWAYS
            );

            if (node.equals(step.currentNode)) {

                row.setStyle(
                        "-fx-background-color: #312E81;"
                                + "-fx-background-radius: 6;"
                                + "-fx-padding: 11;"
                );

            } else {

                row.setStyle(
                        "-fx-background-color: #0B1120;"
                                + "-fx-background-radius: 6;"
                                + "-fx-padding: 11;"
                );
            }

            row.getChildren().addAll(
                    vertex,
                    distanceLabel,
                    previousLabel
            );

            distanceTableBox.getChildren().add(row);
        }
    }

    // ==================================================
    // DRAW GRAPH
    // ==================================================

    private void draw(GraphStep step) {

        graphPane.getChildren().clear();

        if (graph == null) {
            return;
        }

        // DRAW EDGES

        for (Graph.Edge edge : graph.getEdges()) {

            double[] from = positions.get(edge.from);

            double[] to = positions.get(edge.to);

            if (from == null || to == null) {
                continue;
            }

            String color = "#64748B";

            double width = 2.5;

            // Previously explored edges.

            if (step != null
                    && step.selectedEdges.contains(edge.key())) {

                if (step.result != null
                        && !step.result.isBlank()) {

                    color = GREEN;

                    width = 5;

                } else {

                    color = "#B7791F";

                    width = 3;
                }
            }

            // Currently examined edge.

            if (step != null
                    && step.activeEdge != null
                    && step.activeEdge.key().equals(edge.key())) {

                color = ORANGE;

                width = 6;
            }

            Line line = new Line(
                    from[0],
                    from[1],
                    to[0],
                    to[1]
            );

            line.setStroke(
                    Color.web(color)
            );

            line.setStrokeWidth(width);

            Label weight = new Label(
                    String.valueOf(edge.weight)
            );

            weight.setTextFill(Color.WHITE);

            weight.setStyle(
                    "-fx-background-color: #151F32;"
                            + "-fx-padding: 3 6 3 6;"
                            + "-fx-background-radius: 5;"
            );

            weight.setLayoutX(
                    (from[0] + to[0]) / 2
            );

            weight.setLayoutY(
                    (from[1] + to[1]) / 2
            );

            graphPane.getChildren().addAll(
                    line,
                    weight
            );
        }

        // DRAW VERTICES

        for (String node : graph.getNodes()) {

            double[] position = positions.get(node);

            if (position == null) {
                continue;
            }

            String color = BLUE;

            if (step != null
                    && step.visited.contains(node)) {

                color = GREEN;
            }

            if (step != null
                    && node.equals(step.currentNode)) {

                color = PURPLE;
            }

            Circle circle = new Circle(
                    position[0],
                    position[1],
                    22
            );

            circle.setFill(
                    Color.web(color)
            );

            circle.setStroke(Color.WHITE);

            circle.setStrokeWidth(1.5);

            Label nodeLabel = new Label(node);

            nodeLabel.setTextFill(Color.WHITE);

            nodeLabel.setFont(
                    Font.font("Arial", FontWeight.BOLD, 14)
            );

            nodeLabel.setLayoutX(
                    position[0] - 6
            );

            nodeLabel.setLayoutY(
                    position[1] - 10
            );

            graphPane.getChildren().addAll(
                    circle,
                    nodeLabel
            );
        }
    }

    // ==================================================
    // PREVIOUS STEP
    // ==================================================

    private void showPrevious() {

        if (index > 0) {

            index--;

            showStep();
        }
    }

    // ==================================================
    // NEXT STEP
    // ==================================================

    private void showNext() {

        if (index < steps.size() - 1) {

            index++;

            showStep();
        }
    }

    // ==================================================
    // PLAY / PAUSE
    // ==================================================

    private void togglePlay() {

        if (steps.isEmpty()) {
            return;
        }

        if (timeline != null
                && timeline.getStatus()
                == Timeline.Status.RUNNING) {

            timeline.pause();

            play.setText("Play");

        } else {

            if (index >= steps.size() - 1) {

                index = 0;

                showStep();
            }

            if (timeline == null) {

                timeline = new Timeline(
                        new KeyFrame(
                                Duration.seconds(1.5),
                                e -> {

                                    if (index < steps.size() - 1) {

                                        index++;

                                        showStep();

                                    } else {

                                        stopPlayback();
                                    }
                                }
                        )
                );

                timeline.setCycleCount(
                        Timeline.INDEFINITE
                );
            }

            timeline.play();

            play.setText("Pause");
        }
    }

    // ==================================================
    // STOP PLAYBACK
    // ==================================================

    private void stopPlayback() {

        if (timeline != null) {

            timeline.stop();

            timeline = null;
        }

        if (play != null) {

            play.setText("Play");
        }
    }

    // ==================================================
    // RESET
    // ==================================================

    private void resetPlayback() {

        stopPlayback();

        if (!steps.isEmpty()) {

            index = 0;

            showStep();

        } else {

            draw(null);
        }
    }
}
