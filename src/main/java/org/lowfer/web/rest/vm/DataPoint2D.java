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

package org.lowfer.web.rest.vm;

public final class DataPoint2D<X> {

    private final X x;
    private final long y;

    public DataPoint2D(X x, long y) {
        this.x = x;
        this.y = y;
    }

    public X getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    @Override
    public String toString() {
        return x + ";" + y;
    }
}
