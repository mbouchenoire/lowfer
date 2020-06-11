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

public final class ComponentInstability {

    private final SoftwareComponent component;
    private final long fanIn;
    private final long fanOut;

    public ComponentInstability(SoftwareComponent component, long fanIn, long fanOut) {
        this.component = component;
        this.fanIn = fanIn;
        this.fanOut = fanOut;
    }

    public SoftwareComponent getComponent() {
        return component;
    }

    public long getFanIn() {
        return fanIn;
    }

    public long getFanOut() {
        return fanOut;
    }

    public long getAbsoluteInstability() {
        return fanOut - fanIn;
    }

    public double getDoubleValue() {
        return (double) fanOut / (double) (fanIn + fanOut);
    }

    public double getDoubleValue(int places) {
        return round(getDoubleValue(), places);
    }

    public String getFormattedValue(int places) {
        return Double.toString(getDoubleValue(places));
    }

    public boolean greaterThan(ComponentInstability other) {
        return this.getDoubleValue() > other.getDoubleValue();
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public String toString() {
        return "I=" + getFormattedValue(2);
    }
}
