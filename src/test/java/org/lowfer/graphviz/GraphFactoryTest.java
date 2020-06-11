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

import guru.nidi.graphviz.engine.Format;
import org.junit.jupiter.api.Test;
import org.lowfer.domain.SoftwareArchitectureMother;
import org.lowfer.domain.common.SoftwareArchitecture;

import java.awt.image.BufferedImage;

import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static org.junit.Assert.assertNotNull;

class GraphFactoryTest {

    @Test
    void testCreateGraph() {
        final SoftwareArchitecture architecture = SoftwareArchitectureMother.simple();

        final GraphOptions options = new GraphOptions.Builder()
            .direction(LEFT_TO_RIGHT)
            .build();

        final BufferedImage bufferedImage = GraphFactory
            .createGraph(options, new RoughStyle(), architecture)
            .render(Format.PNG).toImage();

        assertNotNull(bufferedImage);
    }
}
