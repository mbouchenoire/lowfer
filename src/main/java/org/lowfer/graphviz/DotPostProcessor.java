package org.lowfer.graphviz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is needed because the current implementation of the GraphViz library
 * does not allow separating a node from its links, making the correct usage
 * of subgraphs impossible.
 */
public final class DotPostProcessor {

    /**
     * This implementation is naive and far from optimal.
     *
     * @param dot the dot string to post process
     * @return the processed dot string
     */
    public static String process(String dot) {
        final String[] dotLines = dot.split("\n");

        final List<String> newDotLines = new ArrayList<>(dotLines.length);

        for (int i = dotLines.length-1; i >= 0; i--) {
            final String line = dotLines[i];

            if (isEdge(line)) {
                newDotLines.add(1, line); // just after the closing bracket
            } else {
                newDotLines.add(line);
            }
        }

        Collections.reverse(newDotLines);

        return String.join("\n", newDotLines);
    }

    private static boolean isEdge(String line) {
        return line.contains("->");
    }
}
