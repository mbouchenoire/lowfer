package org.lowfer.domain.analysis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.lowfer.domain.SoftwareComponentMother.single;

class CircularDependencyTest {

    @Test
    void testIsNotRedundantWith() {
        final DependencyChain chain1 = new DependencyChain()
                .add(single("c1"))
                .add(single("c2"))
                .add(single("c3"))
                .add(single("c1"));

        final CircularDependency circularDependency1 = chain1.getCircularDependency().orElseThrow();

        final DependencyChain chain2 = new DependencyChain()
                .add(single("c4"))
                .add(single("c5"))
                .add(single("c6"))
                .add(single("c4"));

        final CircularDependency circularDependency2 = chain2.getCircularDependency().orElseThrow();


        assertFalse(circularDependency1.isRedundantWith(circularDependency2));
    }

    @Test
    void testIsRedundantWith() {
        final DependencyChain chain1 = new DependencyChain()
                .add(single("c1"))
                .add(single("c2"))
                .add(single("c3"))
                .add(single("c1"));

        final CircularDependency circularDependency1 = chain1.getCircularDependency().orElseThrow();

        final DependencyChain chain2 = new DependencyChain()
                .add(single("c9"))
                .add(single("c2"))
                .add(single("c3"))
                .add(single("c1"))
                .add(single("c2"));

        final CircularDependency circularDependency2 = chain2.getCircularDependency().orElseThrow();


        assertTrue(circularDependency1.isRedundantWith(circularDependency2));
    }
}
