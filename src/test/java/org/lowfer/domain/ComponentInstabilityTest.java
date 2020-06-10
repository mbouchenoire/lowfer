package org.lowfer.domain;

import org.junit.jupiter.api.Test;
import org.lowfer.domain.common.ComponentInstability;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.lowfer.domain.SoftwareComponentMother.single;

class ComponentInstabilityTest {

    @Test
    @SuppressWarnings("PointlessArithmeticExpression")
    void getDoubleValue() {
        assertEquals(2d / (0 + 2), new ComponentInstability(single(), 0, 2).getDoubleValue());
        assertEquals(2d / (1 + 2), new ComponentInstability(single(), 1, 2).getDoubleValue());
        assertEquals(0d / (1 + 0), new ComponentInstability(single(), 1, 0).getDoubleValue());
    }
}
