
package com.algolens;

import java.util.*;

public class Dijkstra {

    private record QueueEntry(String node, int distance) {}

    public static List<GraphStep> run(Graph graph, String start) {

        if (graph == null || graph.getNodes().isEmpty()) {
            throw new IllegalArgumentException("Graph is empty.");
        }

        if (start == null || !graph.hasNode(start)) {
            throw new IllegalArgumentException(
                    "Starting vertex does not exist."
            );
        }

        List<GraphStep> steps = new ArrayList<>();

        Map<String, Integer> distances = new LinkedHashMap<>();
        Map<String, String> previous = new LinkedHashMap<>();
        Map<String, Graph.Edge> previousEdges = new HashMap<>();

        Set<String> visited = new LinkedHashSet<>();
        Set<String> exploredEdges = new LinkedHashSet<>();

        for (String node : graph.getNodes()) {
            distances.put(node, Integer.MAX_VALUE);
        }

        distances.put(start, 0);

        PriorityQueue<QueueEntry> queue =
                new PriorityQueue<>(
                        Comparator.comparingInt(QueueEntry::distance)
                );

        queue.add(new QueueEntry(start, 0));

        steps.add(makeStep(
                visited, start, null, exploredEdges,
                distances, previous,
                "STEP 1: INITIALIZATION\n\n"
                        + "We begin at vertex " + start + ".\n\n"
                        + "Distance of " + start + " = 0 because "
                        + "we are already at the starting vertex.\n\n"
                        + "All other vertices are initialized to infinity (∞) "
                        + "because their shortest distances are not yet known.\n\n"
                        + "We will repeatedly select the unvisited vertex "
                        + "with the smallest known distance.",
                ""
        ));

        while (!queue.isEmpty()) {

            QueueEntry entry = queue.poll();

            String current = entry.node();

            if (visited.contains(current)
                    || entry.distance() != distances.get(current)) {
                continue;
            }

            visited.add(current);

            steps.add(makeStep(
                    visited, current, null, exploredEdges,
                    distances, previous,
                    "STEP: SELECT THE NEXT VERTEX\n\n"
                            + "The algorithm selects vertex " + current + ".\n\n"
                            + "Its current distance from " + start + " is "
                            + distances.get(current) + ".\n\n"
                            + "This is the smallest known distance among "
                            + "all unvisited vertices.\n\n"
                            + "Therefore, its shortest distance is finalized.\n\n"
                            + "Now we examine its neighbouring vertices.",
                    ""
            ));

            for (Graph.Edge edge : graph.neighbours(current)) {

                String neighbour = edge.other(current);

                if (visited.contains(neighbour)) {
                    continue;
                }

                exploredEdges.add(edge.key());

                int currentDistance = distances.get(current);
                int weight = edge.weight;

                int newDistance = currentDistance + weight;
                int oldDistance = distances.get(neighbour);

                boolean updated = newDistance < oldDistance;

                String calculation =
                        currentDistance + " + " + weight
                                + " = " + newDistance;

                String explanation;

                if (updated) {

                    distances.put(neighbour, newDistance);
                    previous.put(neighbour, current);
                    previousEdges.put(neighbour, edge);

                    queue.add(
                            new QueueEntry(neighbour, newDistance)
                    );

                    explanation =
                            "STEP: RELAXATION — DISTANCE UPDATED\n\n"
                                    + "Current vertex: " + current + "\n"
                                    + "Neighbour: " + neighbour + "\n"
                                    + "Edge weight: " + weight + "\n\n"
                                    + "CALCULATION:\n"
                                    + "Distance to " + current
                                    + " + Edge weight\n"
                                    + "= " + calculation + "\n\n"
                                    + "Previous distance to " + neighbour
                                    + " = " + formatDistance(oldDistance) + "\n"
                                    + "New distance = " + newDistance + "\n\n"
                                    + "COMPARISON:\n"
                                    + newDistance + " < "
                                    + formatDistance(oldDistance) + "\n\n"
                                    + "DECISION: UPDATE!\n"
                                    + "The new route is shorter. We replace "
                                    + "the old distance with " + newDistance + ".\n\n"
                                    + "Previous vertex of " + neighbour
                                    + " is now " + current + ".";

                } else {

                    explanation =
                            "STEP: RELAXATION — NO UPDATE\n\n"
                                    + "Current vertex: " + current + "\n"
                                    + "Neighbour: " + neighbour + "\n"
                                    + "Edge weight: " + weight + "\n\n"
                                    + "CALCULATION:\n"
                                    + "Distance to " + current
                                    + " + Edge weight\n"
                                    + "= " + calculation + "\n\n"
                                    + "Previous distance to " + neighbour
                                    + " = " + formatDistance(oldDistance) + "\n"
                                    + "New distance = " + newDistance + "\n\n"
                                    + "COMPARISON:\n"
                                    + newDistance + " >= "
                                    + formatDistance(oldDistance) + "\n\n"
                                    + "DECISION: KEEP THE OLD DISTANCE!\n"
                                    + "The existing route is already shorter, "
                                    + "so no change is necessary.";
                }

                steps.add(makeStep(
                        visited, current, edge, exploredEdges,
                        distances, previous,
                        explanation,
                        ""
                ));
            }
        }

        // Reconstruct the final shortest paths.

        Set<String> finalPathEdges = new LinkedHashSet<>();

        StringBuilder result = new StringBuilder();

        result.append("SHORTEST PATHS FROM ")
                .append(start)
                .append("\n\n");

        for (String destination : graph.getNodes()) {

            result.append(destination).append(": ");

            if (distances.get(destination) == Integer.MAX_VALUE) {
                result.append("No path found\n");
                continue;
            }

            if (destination.equals(start)) {
                result.append(start)
                        .append(" | Distance: 0\n");
                continue;
            }

            List<String> path = new ArrayList<>();

            String current = destination;

            while (!current.equals(start)) {

                path.add(current);

                Graph.Edge edge = previousEdges.get(current);

                if (edge == null) {
                    break;
                }

                finalPathEdges.add(edge.key());

                current = previous.get(current);
            }

            path.add(start);
            Collections.reverse(path);

            result.append(String.join(" → ", path))
                    .append(" | Distance: ")
                    .append(distances.get(destination))
                    .append("\n");
        }

        steps.add(makeStep(
                visited, null, null, finalPathEdges,
                distances, previous,
                "FINAL STEP: SHORTEST PATHS FOUND!\n\n"
                        + "Every reachable vertex has now been processed.\n\n"
                        + "The shortest distances are finalized.\n\n"
                        + "We reconstruct the paths using the previous "
                        + "vertex recorded during successful relaxations.\n\n"
                        + "The green edges represent the final shortest-path tree.\n\n"
                        + "Dijkstra finds the lowest total path weight, "
                        + "not necessarily the fewest edges.",
                result.toString()
        ));

        return steps;
    }

    private static GraphStep makeStep(
            Set<String> visited,
            String currentNode,
            Graph.Edge activeEdge,
            Set<String> exploredEdges,
            Map<String, Integer> distances,
            Map<String, String> previous,
            String explanation,
            String result
    ) {
        return new GraphStep(
                visited,
                currentNode,
                activeEdge,
                exploredEdges,
                distances,
                previous,
                explanation,
                result
        );
    }

    private static String formatDistance(int distance) {

        return distance == Integer.MAX_VALUE
                ? "∞"
                : String.valueOf(distance);
    }
}
