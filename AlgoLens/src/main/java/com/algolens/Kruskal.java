package com.algolens;

import java.util.*;

public class Kruskal {
    public static List<GraphStep> run(Graph graph) {
        List<GraphStep> steps = new ArrayList<>();
        List<Graph.Edge> sorted = new ArrayList<>(graph.getEdges());
        sorted.sort(Comparator.comparingInt((Graph.Edge e) -> e.weight)
                .thenComparing(Graph.Edge::key));
        Map<String, String> parent = new HashMap<>();
        for (String node : graph.getNodes()) parent.put(node, node);
        Set<String> chosen = new LinkedHashSet<>();
        int total = 0;

        steps.add(new GraphStep(Set.of(), null, null, chosen, Map.of(),
                "Sort all edges by increasing weight. Select an edge only if it does not create a cycle.", ""));
        for (Graph.Edge edge : sorted) {
            String a = find(parent, edge.from), b = find(parent, edge.to);
            if (!a.equals(b)) {
                parent.put(a, b);
                chosen.add(edge.key());
                total += edge.weight;
                steps.add(new GraphStep(Set.of(edge.from, edge.to), edge.to, edge, chosen, Map.of(),
                        "Accept edge " + edge.from + "-" + edge.to + " (weight " + edge.weight + "). It connects two different components.", ""));
                if (chosen.size() == graph.getNodes().size() - 1) break;
            } else {
                steps.add(new GraphStep(Set.of(edge.from, edge.to), null, edge, chosen, Map.of(),
                        "Reject edge " + edge.from + "-" + edge.to + " because it would create a cycle.", ""));
            }
        }
        String result = chosen.size() == graph.getNodes().size() - 1
                ? "MST total weight: " + total
                : "Graph is disconnected. Minimum spanning forest weight: " + total;
        steps.add(new GraphStep(Set.of(), null, null, chosen, Map.of(),
                "Kruskal's algorithm is complete. " + result, result + "\nSelected edges: " + String.join(", ", chosen)));
        return steps;
    }

    private static String find(Map<String, String> parent, String node) {
        if (!parent.get(node).equals(node))
            parent.put(node, find(parent, parent.get(node)));
        return parent.get(node);
    }
}
