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

public final class AutonomousDependency {

    private final ComponentDependency dependency;
    private final SoftwareComponent source;
    private final SoftwareComponent target;

    public AutonomousDependency(ComponentDependency dependency, SoftwareComponent source, SoftwareComponent target) {
        this.dependency = dependency;
        this.source = source;
        this.target = target;
    }

    public ComponentDependency getDependency() {
        return dependency;
    }

    public SoftwareComponent getSource() {
        return source;
    }

    public SoftwareComponent getTarget() {
        return target;
    }
}
