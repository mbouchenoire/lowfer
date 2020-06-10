package org.lowfer.domain.analysis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.lowfer.domain.SoftwareComponentMother.single;

class DependencyChainTest {

    @Test
    void testIsCircularDependency() {
        final DependencyChain dependencyChain = new DependencyChain()
                .add(single("c1"))
                .add(single("c2"))
                .add(single("c1"));

        assertTrue(dependencyChain.isCircularDependency());

        final DependencyChain extendedDependencyChain = new DependencyChain()
                .add(single("c9"))
                .add(single("c1"))
                .add(single("c2"))
                .add(single("c1"));

        assertTrue(extendedDependencyChain.isCircularDependency());
    }

    @Test
    void subFindSubChain() {
        final DependencyChain dependencyChain = new DependencyChain()
            .add(single("c1"))
            .add(single("c2"))
            .add(single("c3"))
            .add(single("c4"))
            .add(single("c5"));

        final DependencyChain subChain = dependencyChain.subChain(single("c2"), single("c4")).orElseThrow();

        assertEquals(3, subChain.getComponents().size());
        assertEquals(single("c2"), subChain.head());
        assertEquals(single("c4"), subChain.tail());
    }

    @Test
    void testCannotFindSubChain() {
        final DependencyChain dependencyChain = new DependencyChain()
            .add(single("c1"))
            .add(single("c2"))
            .add(single("c3"))
            .add(single("c4"))
            .add(single("c5"));

        assertTrue(dependencyChain.subChain(single("c3"), single("c6")).isEmpty());
    }
}
