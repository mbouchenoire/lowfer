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

package org.lowfer.domain.common;

import java.util.Random;

public enum SemanticUIColor {
    RED("red"),
    ORANGE("orange"),
    YELLOW("yellow"),
    OLIVE("olive"),
    GREEN("green"),
    TEAL("teal"),
    BLUE("blue"),
    VIOLET("violet"),
    PURPLE("purple"),
    PINK("pink"),
    BROWN("brown"),
    GREY("grey"),
    BLACK("black");

    private final String name;

    SemanticUIColor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SemanticUIColor randomFromSeed(String seed) {
        final int colorIndex = new Random(seed.hashCode()).nextInt(values().length);
        return values()[colorIndex];
    }
}
