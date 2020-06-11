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

package org.lowfer.domain;

import org.lowfer.domain.common.ComponentDependency;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;

import java.util.Set;
import java.util.UUID;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.lowfer.domain.common.SoftwareComponentType.*;

public final class SoftwareArchitectureMother {

    public static final String API_BACK = "api-back";
    public static final String API_FRONT = "api-front";
    public static final String FRONT = "front";
    public static final String BDD = "bdd";

    /**
     * @return front -> api2 -> (bdd + api1)
     */
    public static SoftwareArchitecture simple() {
        final SoftwareComponent bdd = new SoftwareComponent(BDD, "", DATABASE, null, emptySet(), emptySet());
        final SoftwareComponent apiBack = new SoftwareComponent(API_BACK, "", SERVICE, null, emptySet(), emptySet());
        final SoftwareComponent apiFront = new SoftwareComponent(API_FRONT, "", SERVICE, null, emptySet(), Set.of(new ComponentDependency(bdd), new ComponentDependency(apiBack)));
        final SoftwareComponent front = new SoftwareComponent(FRONT, "", FRONTEND, null, emptySet(), singleton(new ComponentDependency(apiFront)));
        return new SoftwareArchitecture(UUID.randomUUID().toString(), Set.of(bdd, apiBack, apiFront , front));
    }
}
