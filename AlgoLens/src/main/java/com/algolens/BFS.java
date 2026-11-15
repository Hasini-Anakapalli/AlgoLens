package com.algolens;

import java.util.*;

public class BFS {
    public static List<GraphStep> run(Graph graph, String start) {
        checkStart(graph, start);
        List<GraphStep> steps = new ArrayList<>();
        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new LinkedHashSet<>();
        queue.add(start);
        visited.add(start);
        steps.add(step(visited, start, null, "Start BFS at " + start + ". Add it to the queue."));
        while (!queue.isEmpty()) {
            String current = queue.remove();
            steps.add(step(visited, current, null, "Remove " + current + " from the queue and inspect its neighbours."));
            for (Graph.Edge edge : graph.neighbours(current)) {
                String next = edge.other(current);
                if (visited.add(next)) {
                    queue.add(next);
                    steps.add(step(visited, next, edge, "Visit " + next + " through " + edge.from + "-" + edge.to + " and add it to the queue."));
                }
            }
        }
        steps.add(step(visited, null, null, "BFS is complete. Traversal order: " + String.join(" → ", visited)));
        return steps;
    }

    static void checkStart(Graph graph, String start) {
        if (start == null || !graph.hasNode(start))
            throw new IllegalArgumentException("Starting node does not exist in the graph.");
    }

    static GraphStep step(Set<String> visited, String current, Graph.Edge edge, String text) {
        return new GraphStep(visited, current, edge, Set.of(), Map.of(), text, "");
    }
}
