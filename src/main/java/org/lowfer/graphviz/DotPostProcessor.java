/*
 * Copyright 2020 the original author or authors from the Lowfer project.
 *
 * This file is part of the Lowfer project, see https://github.com/mbouchenoire/lowfer
 * for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
