
package com.algolens;

import java.util.*;

public class GraphStep {

    public final Set<String> visited;
    public final String currentNode;
    public final Graph.Edge activeEdge;
    public final Set<String> selectedEdges;
    public final Map<String, Integer> distances;
    public final Map<String, String> previousNodes;
    public final String explanation;
    public final String result;

    // Backward-compatible constructor for BFS, DFS, Prim's and Kruskal's.
    public GraphStep(
            Set<String> visited,
            String currentNode,
            Graph.Edge activeEdge,
            Set<String> selectedEdges,
            Map<String, Integer> distances,
            String explanation,
            String result
    ) {
        this(
                visited,
                currentNode,
                activeEdge,
                selectedEdges,
                distances,
                Collections.emptyMap(),
                explanation,
                result
        );
    }

    // Constructor with previous-vertex information for Dijkstra.
    public GraphStep(
            Set<String> visited,
            String currentNode,
            Graph.Edge activeEdge,
            Set<String> selectedEdges,
            Map<String, Integer> distances,
            Map<String, String> previousNodes,
            String explanation,
            String result
    ) {
        this.visited = new LinkedHashSet<>(visited);
        this.currentNode = currentNode;
        this.activeEdge = activeEdge;
        this.selectedEdges = new LinkedHashSet<>(selectedEdges);
        this.distances = new LinkedHashMap<>(distances);
        this.previousNodes = new LinkedHashMap<>(previousNodes);
        this.explanation = explanation;
        this.result = result;
    }
}
