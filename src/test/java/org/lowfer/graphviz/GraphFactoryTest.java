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
