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
