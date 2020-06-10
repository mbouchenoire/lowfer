package org.lowfer.domain.common;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.lowfer.domain.SoftwareComponentMother.external;
import static org.lowfer.domain.SoftwareComponentMother.single;
import static org.lowfer.domain.common.SoftwareComponentType.SERVICE;

class InternalComponentFilterTest {

    @Test
    void testHideExternalComponent() {
        final SoftwareComponent internal = single();
        final SoftwareComponent external = external("external", SERVICE);

        final SoftwareArchitecture architecture =
            new SoftwareArchitecture("architecture", Set.of(internal, external));

        assertTrue(new InternalComponentFilter(false).test(architecture, external));
        assertFalse(new InternalComponentFilter(true).test(architecture, external));
    }
}
