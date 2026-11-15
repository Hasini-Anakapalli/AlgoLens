package com.algolens;

import java.util.*;

public class Graph {
    private final Map<String, List<Edge>> adjacency = new LinkedHashMap<>();
    private final List<Edge> edges = new ArrayList<>();

    public static class Edge {
        public final String from, to;
        public final int weight;

        public Edge(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public String other(String node) {
            if (from.equals(node)) return to;
            if (to.equals(node)) return from;
            throw new IllegalArgumentException("Node is not part of this edge.");
        }

        public String key() {
            return from.compareTo(to) <= 0 ? from + "-" + to : to + "-" + from;
        }

        @Override public String toString() {
            return from + "-" + to + "(" + weight + ")";
        }
    }

    public void addEdge(String from, String to, int weight) {
        if (from == null || to == null || from.isBlank() || to.isBlank())
            throw new IllegalArgumentException("Node names cannot be empty.");
        if (from.equals(to)) throw new IllegalArgumentException("Self-loops are not supported.");
        if (weight < 0) throw new IllegalArgumentException("Weights must be non-negative.");
        adjacency.computeIfAbsent(from, k -> new ArrayList<>());
        adjacency.computeIfAbsent(to, k -> new ArrayList<>());
        Edge edge = new Edge(from, to, weight);
        edges.add(edge);
        adjacency.get(from).add(edge);
        adjacency.get(to).add(edge);
    }

    public static Graph parse(String input) {
        Graph graph = new Graph();
        if (input == null || input.isBlank())
            throw new IllegalArgumentException("Enter edges in the format A-B-4, A-C-2.");
        for (String part : input.split(",")) {
            String[] pieces = part.trim().split("-");
            if (pieces.length != 3)
                throw new IllegalArgumentException("Invalid edge '" + part.trim() + "'. Use A-B-4.");
            try {
                graph.addEdge(pieces[0].trim(), pieces[1].trim(),
                        Integer.parseInt(pieces[2].trim()));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid weight in edge: " + part.trim());
            }
        }
        if (graph.getNodes().size() < 2)
            throw new IllegalArgumentException("Enter at least two nodes.");
        return graph;
    }

    public Set<String> getNodes() {
        return Collections.unmodifiableSet(adjacency.keySet());
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public List<Edge> neighbours(String node) {
        return adjacency.getOrDefault(node, Collections.emptyList());
    }

    public boolean hasNode(String node) {
        return adjacency.containsKey(node);
    }
}
