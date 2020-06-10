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
