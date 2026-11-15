package com.algolens;

import java.util.*;

public class DFS {
    public static List<GraphStep> run(Graph graph, String start) {
        BFS.checkStart(graph, start);
        List<GraphStep> steps = new ArrayList<>();
        Set<String> visited = new LinkedHashSet<>();
        visit(graph, start, visited, steps);
        steps.add(BFS.step(visited, null, null, "DFS is complete. Traversal order: " + String.join(" → ", visited)));
        return steps;
    }

    private static void visit(Graph graph, String node, Set<String> visited, List<GraphStep> steps) {
        visited.add(node);
        steps.add(BFS.step(visited, node, null, "Visit " + node + ". Explore an unvisited neighbour as deeply as possible."));
        for (Graph.Edge edge : graph.neighbours(node)) {
            String next = edge.other(node);
            if (!visited.contains(next)) {
                steps.add(BFS.step(visited, node, edge, "Follow edge " + edge.from + "-" + edge.to + " to " + next + "."));
                visit(graph, next, visited, steps);
            }
        }
        steps.add(BFS.step(visited, node, null, "No unvisited neighbours remain at " + node + ". Backtrack."));
    }
}
