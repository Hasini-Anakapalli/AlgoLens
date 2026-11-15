package com.algolens;

import java.util.*;

public class Prims {
    public static List<GraphStep> run(Graph graph, String start) {
        BFS.checkStart(graph, start);
        List<GraphStep> steps = new ArrayList<>();
        Set<String> visited = new LinkedHashSet<>();
        Set<String> chosen = new LinkedHashSet<>();
        PriorityQueue<Graph.Edge> pq = new PriorityQueue<>(
                Comparator.comparingInt((Graph.Edge e) -> e.weight).thenComparing(Graph.Edge::key));

        visited.add(start);
        pq.addAll(graph.neighbours(start));
        steps.add(new GraphStep(visited, start, null, chosen, Map.of(),
                "Start Prim's algorithm at " + start + ". Add its outgoing edges to the priority queue.", ""));
        int total = 0;

        while (!pq.isEmpty() && visited.size() < graph.getNodes().size()) {
            Graph.Edge edge = pq.poll();
            boolean fromIn = visited.contains(edge.from);
            boolean toIn = visited.contains(edge.to);
            if (fromIn == toIn) {
                steps.add(new GraphStep(visited, null, edge, chosen, Map.of(),
                        "Skip " + edge.from + "-" + edge.to + " because it does not connect the tree to a new node.", ""));
                continue;
            }
            String next = fromIn ? edge.to : edge.from;
            visited.add(next);
            chosen.add(edge.key());
            total += edge.weight;
            steps.add(new GraphStep(visited, next, edge, chosen, Map.of(),
                    "Choose the cheapest edge crossing from the current tree to an unvisited node: "
                            + edge.from + "-" + edge.to + " (weight " + edge.weight + ").", ""));
            for (Graph.Edge candidate : graph.neighbours(next)) {
                if (!visited.contains(candidate.other(next))) pq.add(candidate);
            }
        }

        String result = visited.size() == graph.getNodes().size()
                ? "MST total weight: " + total
                : "Graph is disconnected. Prim's algorithm reached " + visited.size() + " of " + graph.getNodes().size() + " nodes.";
        steps.add(new GraphStep(visited, null, null, chosen, Map.of(),
                "Prim's algorithm is complete. " + result, result + "\nSelected edges: " + String.join(", ", chosen)));
        return steps;
    }
}
