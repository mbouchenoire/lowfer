package org.lowfer.domain.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.lowfer.domain.SoftwareArchitectureMother.*;

class ArchitectureTransformerTest {

    @Test
    void testSimplifyWithoutFilterWithGroupOf1() {
        final SoftwareArchitecture architecture = simple();

        final SoftwareArchitecture simplified =
            new ArchitectureTransformer().filter(architecture, null);

        assertEquals(architecture.getComponents().size(), simplified.getComponents().size());
    }

    @Test
    void testSimplifyFilterEdgeComponentWithGroupOf1() {
        final SoftwareArchitecture architecture = simple();

        final SoftwareArchitecture simplified =
            new ArchitectureTransformer().filter(architecture, ComponentNeighborsFilter.ofText(BDD));

        assertEquals(4, simplified.getComponents().size());
        assertEquals(2, simplified.findComponentByLabel("+1").size());
        assertTrue(simplified.findComponentByName(BDD).isPresent());
        assertTrue(simplified.findComponentByName(API_FRONT).isPresent());
    }
}
